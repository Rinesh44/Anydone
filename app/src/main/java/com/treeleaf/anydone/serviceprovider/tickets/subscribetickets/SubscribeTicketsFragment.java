package com.treeleaf.anydone.serviceprovider.tickets.subscribetickets;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.servicerequests.OnSwipeListener;
import com.treeleaf.anydone.serviceprovider.tickets.TicketsFragment;
import com.treeleaf.anydone.serviceprovider.tickets.UnSubscribedTicketsActivity;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SubscribeTicketsFragment extends Fragment implements
        TicketsFragment.SubscribeTicketListListener {
    private static final String TAG = "SubscribeTicketsFragmen";
    @BindView(R.id.rv_subscribe_tickets)
    RecyclerView rvSubscribeTickets;
    @BindView(R.id.swipe_refresh_subscribe_tickets)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.iv_data_not_found)
    ImageView ivDataNotFound;
    @BindView(R.id.fab_subscribe)
    FloatingActionButton fabSubscribe;
    private Unbinder unbinder;
    private OnSwipeListener swipeListener;
    private OnSubscribeTicketsListener onSubscribeTicketsListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TicketsFragment mFragment = (TicketsFragment) getParentFragment();
        assert mFragment != null;
        mFragment.setSubscribeTicketListListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscribe_tickets, container,
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
        rvSubscribeTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvSubscribeTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
      /*      adapter = new ServiceRequestAdapter(ticketsList, getContext());
            adapter.setOnItemClickListener(service -> {
                Intent i = new Intent(getActivity(), ServiceRequestDetailActivity.class);
                i.putExtra("selected_service_id", service.getServiceOrderId());
                startActivity(i);
            });
            rvClosedRequests.setAdapter(adapter);*/
        } else {
            rvSubscribeTickets.setVisibility(View.GONE);
//            ivDataNotFound.setVisibility(View.VISIBLE);
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

        if (onSubscribeTicketsListener != null) {
            onSubscribeTicketsListener.onSubscribeTicketFragmentCreated();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        swipeListener = (OnSwipeListener) getParentFragment();
        onSubscribeTicketsListener = (OnSubscribeTicketsListener) getParentFragment();
    }

    @Override
    public void showSubscribeTickets(List<Tickets> subscribeTicketList) {
        setUpRecyclerView(subscribeTicketList);
    }

    @OnClick(R.id.fab_subscribe)
    void subscribe() {
        Intent i = new Intent(getActivity(), UnSubscribedTicketsActivity.class);
        startActivity(i);
    }
}

