package com.treeleaf.anydone.serviceprovider.assignedtickets.opentickets;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.util.CollectionUtils;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.assignedtickets.AssignedTicketFragment;
import com.treeleaf.anydone.serviceprovider.assignedtickets.closedresolvedtickets.OnClosedTicketsListener;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.servicerequests.OnSwipeListener;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OpenTicketsFragment extends Fragment implements
        AssignedTicketFragment.OpenTicketListListener {
    private static final String TAG = "OpenTicketsFragment";
    @BindView(R.id.rv_open_tickets)
    RecyclerView rvOpenTickets;
    @BindView(R.id.swipe_refresh_open_tickets)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.iv_data_not_found)
    ImageView ivDataNotFound;
    private Unbinder unbinder;
    private OnSwipeListener swipeListener;
    private OnOpenTicketsListener onOpenTicketsListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AssignedTicketFragment mFragment = (AssignedTicketFragment) getParentFragment();
        assert mFragment != null;
        mFragment.setOpenTicketListListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_open_tickets, container,
                false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setUpRecyclerView(List<Tickets> ticketsList) {
        rvOpenTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvOpenTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
      /*      adapter = new ServiceRequestAdapter(ticketsList, getContext());
            adapter.setOnItemClickListener(service -> {
                Intent i = new Intent(getActivity(), ServiceRequestDetailActivity.class);
                i.putExtra("selected_service_id", service.getServiceOrderId());
                startActivity(i);
            });
            rvClosedRequests.setAdapter(adapter);*/
        } else {
            rvOpenTickets.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
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

        if (onOpenTicketsListener != null) {
            onOpenTicketsListener.onOpenTicketsCreated();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        swipeListener = (OnSwipeListener) getParentFragment();
        onOpenTicketsListener = (OnOpenTicketsListener) getParentFragment();
    }

    @Override
    public void showOpenTickets(List<Tickets> openTicketList) {
        setUpRecyclerView(openTicketList);
    }
}


