package com.treeleaf.anydone.serviceprovider.threads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.SearchServiceAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.ThreadAdapter;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.Thread;
import com.treeleaf.anydone.serviceprovider.realm.repo.AvailableServicesRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ThreadRepo;
import com.treeleaf.anydone.serviceprovider.threaddetails.ThreadDetailActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class ThreadFragment extends BaseFragment<ThreadPresenterImpl>
        implements ThreadContract.ThreadView {
    private static final String TAG = "ThreadFragment";
    @BindView(R.id.rv_threads)
    RecyclerView rvThreads;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_service)
    ImageView ivService;
    @BindView(R.id.bottom_sheet)
    LinearLayout llBottomSheet;
    @BindView(R.id.shadow)
    View bottomSheetShadow;
    @BindView(R.id.pb_search)
    ProgressBar pbSearch;
    @BindView(R.id.iv_thread_not_found)
    ImageView ivThreadNotFound;
    @BindView(R.id.root)
    CoordinatorLayout root;
    @BindView(R.id.swipe_refresh_threads)
    SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView rvServices;
    private BottomSheetBehavior sheetBehavior;
    private SearchServiceAdapter adapter;
    private ThreadAdapter threadAdapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_thread;
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public static ThreadFragment newInstance(String param1, String param2) {
        ThreadFragment fragment = new ThreadFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager
                .LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        sheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetShadow.setVisibility(View.GONE);
                    hideKeyBoard();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        EditText searchService = llBottomSheet.findViewById(R.id.et_search_service);
        rvServices = llBottomSheet.findViewById(R.id.rv_services);

        List<Service> serviceList = AvailableServicesRepo.getInstance().getAvailableServices();
        if (CollectionUtils.isEmpty(serviceList)) {
            presenter.getServices();
        } else {
            String selectedServiceId = Hawk.get(Constants.SELECTED_SERVICE);
            if (selectedServiceId == null) {
                Service firstService = serviceList.get(0);
                tvToolbarTitle.setText(firstService.getName().replace("_", " "));
                Glide.with(Objects.requireNonNull(getContext())).load(firstService.getServiceIconUrl()).into(ivService);
                Hawk.put(Constants.SELECTED_SERVICE, firstService.getServiceId());
            } else {
                Service selectedService = AvailableServicesRepo.getInstance().getAvailableServiceById(selectedServiceId);
                tvToolbarTitle.setText(selectedService.getName().replace("_", " "));
                Glide.with(Objects.requireNonNull(getContext())).load(selectedService.getServiceIconUrl()).into(ivService);
            }
            setUpServiceRecyclerView(serviceList);
        }

        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
        List<Thread> threadList = ThreadRepo.getInstance().getThreadsByServiceId(selectedService);
        if (!CollectionUtils.isEmpty(threadList)) {
            setUpThreadRecyclerView(threadList);
        } else {
            presenter.getConversationThreads(true);
        }

        searchService.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvToolbarTitle.setOnClickListener(v -> toggleServiceBottomSheet());

        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh threads called");

                    presenter.getConversationThreads(false);
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        //Do something after 1 sec
                        swipeRefreshLayout.setRefreshing(false);
                    }, 1000);
                }
        );
    }


    public void toggleServiceBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_HALF_EXPANDED) {
            bottomSheetShadow.setVisibility(View.VISIBLE);
            sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        } else if (sheetBehavior.getState() == BottomSheetBehavior.STATE_HALF_EXPANDED) {
            bottomSheetShadow.setVisibility(View.GONE);
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            sheetBehavior.setPeekHeight(0);
            bottomSheetShadow.setVisibility(View.GONE);
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void setUpServiceRecyclerView(List<Service> serviceList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvServices.setLayoutManager(mLayoutManager);

        adapter = new SearchServiceAdapter(serviceList, getActivity());
        rvServices.setAdapter(adapter);

        adapter.setOnItemClickListener(service -> {
            hideKeyBoard();
            Hawk.put(Constants.SELECTED_SERVICE, service.getServiceId());
            Hawk.put(Constants.SERVICE_CHANGED_THREAD, true);
            tvToolbarTitle.setText(service.getName().replace("_", " "));
            Glide.with(getContext()).load(service.getServiceIconUrl()).into(ivService);
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            bottomSheetShadow.setVisibility(View.GONE);

            ivThreadNotFound.setVisibility(View.GONE);
            presenter.getConversationThreads(true);
        });
    }

    private void setUpThreadRecyclerView(List<Thread> threadList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvThreads.setLayoutManager(mLayoutManager);

        threadAdapter = new ThreadAdapter(threadList, getActivity());
        rvThreads.setAdapter(threadAdapter);

        threadAdapter.setOnItemClickListener(thread -> {
            Intent i = new Intent(getContext(), ThreadDetailActivity.class);
            i.putExtra("thread_id", thread.getThreadId());
            i.putExtra("customer_name", thread.getCustomerName());
            i.putExtra("customer_img", thread.getCustomerImageUrl());
            startActivity(i);
        });
    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager)
                Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
    }


    @Override
    public void showProgressBar(String message) {
        pbSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void onResume() {
        super.onResume();

  /*      boolean serviceChanged = Hawk.get(Constants.SERVICE_CHANGED_TICKET, false);
        if (serviceChanged) {
            presenter.getConversationThreads();
            Hawk.put(Constants.SERVICE_CHANGED_TICKET, false);
        }
*/
        presenter.getConversationThreads(false);
    }

    @Override
    public void hideProgressBar() {
        if (pbSearch != null) {
            pbSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                message);

        ivThreadNotFound.setVisibility(View.VISIBLE);
    }

    @Override
    public void getConversationThreadSuccess() {
        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
        List<Thread> threadList = ThreadRepo.getInstance().getThreadsByServiceId(selectedService);
        setUpThreadRecyclerView(threadList);
        rvThreads.setVisibility(View.VISIBLE);
    }

    @Override
    public void getConversationThreadFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

     /*   UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);*/
        showCustomSnackBar(msg);
        rvThreads.setVisibility(View.GONE);
        ivThreadNotFound.setVisibility(View.VISIBLE);

    }

    @Override
    public void getServiceSuccess() {
        List<Service> serviceList = AvailableServicesRepo.getInstance().getAvailableServices();
        Service firstService = serviceList.get(0);
        Hawk.put(Constants.SELECTED_SERVICE, firstService.getServiceId());
        GlobalUtils.showLog(TAG, "first thread service id saved");

        tvToolbarTitle.setText(firstService.getName().replace("_", " "));
        Glide.with(Objects.requireNonNull(getContext())).load(firstService.getServiceIconUrl()).into(ivService);
        setUpServiceRecyclerView(serviceList);
    }

    @Override
    public void getServiceFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
    }

    private void showCustomSnackBar(String msg) {
        Snackbar snack = Snackbar.make(root, msg, Snackbar.LENGTH_LONG);
        snack.show();
    }
}
