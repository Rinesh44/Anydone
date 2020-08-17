package com.treeleaf.anydone.serviceprovider.tickets.assignedtickets;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.TicketsAdapter;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity.ServiceRequestDetailActivity;
import com.treeleaf.anydone.serviceprovider.servicerequests.OnSwipeListener;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.tickets.unassignedtickets.UnassignedTicketsActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.januswebrtc.Const;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AssignedTicketsFragment extends BaseFragment<AssignedTicketPresenterImpl>
        implements AssignedTicketContract.AssignedTicketView {
    private static final String TAG = "OpenTicketsFragment";
    @BindView(R.id.rv_open_tickets)
    RecyclerView rvOpenTickets;
    @BindView(R.id.swipe_refresh_open_tickets)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.iv_data_not_found)
    ImageView ivDataNotFound;
    @BindView(R.id.fab_assign)
    FloatingActionButton fabAssign;
    @BindView(R.id.pb_search)
    ProgressBar progressBar;
    private Unbinder unbinder;
    private OnSwipeListener swipeListener;
    private TicketsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     /*   TicketsFragment mFragment = (TicketsFragment) getParentFragment();
        assert mFragment != null;
        mFragment.setAssignedTicketListener(this);*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Tickets> assignedTickets = TicketRepo.getInstance().getAssignedTickets();

        if (CollectionUtils.isEmpty(assignedTickets)) {
            presenter.getAssignedTickets(true, 0, System.currentTimeMillis(), 100);
        } else {
            setUpRecyclerView(assignedTickets);
        }
    }

    public void reFetchList() {
        presenter.getAssignedTickets(true, 0, System.currentTimeMillis(), 100);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_open_tickets;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    private void setUpRecyclerView(List<Tickets> ticketsList) {
        rvOpenTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvOpenTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
            adapter = new TicketsAdapter(ticketsList, getContext());
            adapter.setOnItemClickListener(ticket -> {
                Intent i = new Intent(getActivity(), TicketDetailsActivity.class);
                i.putExtra("selected_ticket_id", ticket.getTicketId());
                startActivity(i);
            });
            rvOpenTickets.setAdapter(adapter);
        } else {
            rvOpenTickets.setVisibility(View.GONE);
//            ivDataNotFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean fetchChanges = Hawk.get(Constants.FETCH__ASSIGNED_LIST, false);
        if (fetchChanges) {
            presenter.getAssignedTickets(true, 0, System.currentTimeMillis(), 100);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh close called");

                    // This method performs the actual data-refresh operation
                    if (swipeListener != null) {
                        swipeListener.onSwipeRefresh();
                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        //Do something after 1 sec
                        swipeRefreshLayout.setRefreshing(false);
                    }, 1000);
                }
        );
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        swipeListener = (OnSwipeListener) getParentFragment();
//        onAssignedTicketsListener = (OnAssignedTicketsListener) getParentFragment();
    }

    @Override
    public void getAssignedTicketSuccess() {
        List<Tickets> assignedTickets = TicketRepo.getInstance().getAssignedTickets();
        setUpRecyclerView(assignedTickets);
        Hawk.put(Constants.FETCH__ASSIGNED_LIST, false);
    }

    @Override
    public void getAssignedTicketFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }
        UiUtils.showSnackBar(getContext(), getActivity().getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void showProgressBar(String message) {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                message);
    }

    @OnClick(R.id.fab_assign)
    void gotoAssignableTicketList() {
        Intent i = new Intent(getActivity(), UnassignedTicketsActivity.class);
        startActivity(i);
    }
}


