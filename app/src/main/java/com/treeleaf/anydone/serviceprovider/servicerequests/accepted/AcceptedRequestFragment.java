package com.treeleaf.anydone.serviceprovider.servicerequests.accepted;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.ServiceRequestAdapter;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity.ServiceRequestDetailActivity;
import com.treeleaf.anydone.serviceprovider.servicerequests.OnSwipeListener;
import com.treeleaf.anydone.serviceprovider.servicerequests.ServiceRequestFragment;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class AcceptedRequestFragment extends BaseFragment<AcceptedPresenterImpl> implements
        AcceptedRequestContract.OngoingView,
        ServiceRequestFragment.AcceptedListListener {
    private static final String TAG = "AcceptedRequestFragment";
    @BindView(R.id.rv_accepted_requests)
    RecyclerView rvAcceptedRequests;
    @BindView(R.id.swipe_refresh_ongoing)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.iv_data_not_found)
    ImageView ivDataNotFound;
    @BindView(R.id.pb_progress)
    ProgressBar progress;

    private ServiceRequestAdapter adapter;
    private OnSwipeListener swipeListener;
    private OnAcceptedFragmentReadyListener onFragmentsReadyListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ServiceRequestFragment mFragment = (ServiceRequestFragment) getParentFragment();
        assert mFragment != null;
        mFragment.setAcceptedListListener(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_accepted_requests;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GlobalUtils.showLog(TAG, "onview created for accepted");
        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh called");

                    // This method performs the actual data-refresh operation.
                    if (swipeListener != null) {
                        swipeListener.onSwipeRefresh();
                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        //Do something after 1 sec
                        if (swipeRefreshLayout != null)
                            swipeRefreshLayout.setRefreshing(false);
                    }, 1000);


                }
        );

        if (onFragmentsReadyListener != null)
            onFragmentsReadyListener.onAcceptedFragmentsCreated();

    }

    private void setUpRecyclerView(List<ServiceRequest> serviceList) {
        GlobalUtils.showLog(TAG, "setUpRecyclerview called");
        if (!CollectionUtils.isEmpty(serviceList)) {
            rvAcceptedRequests.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
            rvAcceptedRequests.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new ServiceRequestAdapter(serviceList, getContext());
            adapter.setOnDeleteListener((id, pos) -> showCancelRequestDialog(id));

            adapter.setOnItemClickListener(service -> {
                Intent i = new Intent(getActivity(), ServiceRequestDetailActivity.class);
                i.putExtra("selected_service_id", service.getServiceOrderId());
                i.putExtra("selected_service_name",
                        service.getServiceProvider().getFullName());
                i.putStringArrayListExtra("selected_service_icon_uri",
                        new ArrayList<>(Arrays.asList(service.getServiceProvider().getProfilePic())));
                startActivity(i);
            });
            rvAcceptedRequests.setAdapter(adapter);
        } else {
            rvAcceptedRequests.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
        }
    }


    private void showCancelRequestDialog(String requestId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Cancel request?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    adapter.closeSwipeLayout(requestId);
                    dialog.dismiss();
                    String token = Hawk.get(Constants.TOKEN);
                    presenter.cancelOrder(token, Long.parseLong(requestId));
                });

        builder1.setNegativeButton(
                "No",
                (dialog, id) -> {
                    adapter.closeSwipeLayout(requestId);
                    dialog.dismiss();
                });


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(R.color.colorPrimary));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                    .getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                    .getColor(android.R.color.holo_red_dark));

        });
        alert11.show();
    }

    @Override
    public void showAcceptedRequests(List<ServiceRequest> acceptedRequestList) {
        setUpRecyclerView(acceptedRequestList);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        swipeListener = (OnSwipeListener) getParentFragment();
        onFragmentsReadyListener = (OnAcceptedFragmentReadyListener) getParentFragment();
    }

    @Override
    public void onOrderCancelSuccess() {
        if (swipeListener != null) {
            swipeListener.onSwipeRefresh();
        }
    }

    @Override
    public void onOrderCancelFail(String msg) {
        UiUtils.showSnackBar(getActivity(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(getActivity(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                message);
    }

}

