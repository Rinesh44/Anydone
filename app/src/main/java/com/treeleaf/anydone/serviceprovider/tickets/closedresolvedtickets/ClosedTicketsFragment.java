package com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.TicketsAdapter;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.tickets.TicketsFragment;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.Unbinder;
import io.realm.RealmList;

public class ClosedTicketsFragment extends BaseFragment<ClosedTicketPresenterImpl>
        implements ClosedTicketContract.ClosedTicketView,
        TicketsFragment.ClosedListListener {
    private static final String TAG = "ClosedTicketsFragment";
    @BindView(R.id.rv_closed_tickets)
    RecyclerView rvClosedTickets;
    @BindView(R.id.swipe_refresh_closed_tickets)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.iv_data_not_found)
    ImageView ivDataNotFound;
    @BindView(R.id.pb_search)
    ProgressBar progressBar;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;

    private Unbinder unbinder;
    private TicketsAdapter adapter;
    private int reopenTicketPos;
    private OnTicketReopenListener listener;
    private List<Tickets> closedTickets;
    private boolean fetchList = false;

    @Override
    public void updateClosedList(List<Tickets> ticketsList) {
        setUpRecyclerView(ticketsList);
    }

    @Override
    public void updateClosedList() {
        presenter.getClosedResolvedTickets(true, 0, System.currentTimeMillis(), 100);
    }

    @Override
    public void fetchList() {
        presenter.getClosedResolvedTickets(true, 0, System.currentTimeMillis(), 100);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TicketsFragment mFragment = (TicketsFragment) getParentFragment();
        assert mFragment != null;
        mFragment.setClosedListListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        if (fetchList) {
        closedTickets = TicketRepo.getInstance().getClosedResolvedTickets();

        if (CollectionUtils.isEmpty(closedTickets)) {
            ivDataNotFound.setVisibility(View.GONE);
            presenter.getClosedResolvedTickets(true, 0, System.currentTimeMillis(), 100);
        } else {
            setUpRecyclerView(closedTickets);
        }
//        }
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_closed_tickets;
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
        rvClosedTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvClosedTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
            adapter = new TicketsAdapter(ticketsList, getContext());
            adapter.setOnItemClickListener(ticket -> {
                Intent i = new Intent(getActivity(), TicketDetailsActivity.class);
                i.putExtra("selected_ticket_id", ticket.getTicketId());
                i.putExtra("selected_ticket_type", Constants.CLOSED_RESOLVED);
                i.putExtra("ticket_desc", ticket.getTitle());
                startActivity(i);
            });

            adapter.setOnReopenListener((id, pos) -> {
                reopenTicketPos = pos;
                showReopenDialog(id);
            });
            rvClosedTickets.setAdapter(adapter);
        } else {
            rvClosedTickets.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
        }

    }

    private void showReopenDialog(String ticketId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to reopen this ticket?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    adapter.closeSwipeLayout(ticketId);
                    dialog.dismiss();
                    presenter.reopenTicket(Long.parseLong(ticketId));
                });

        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> {
                    adapter.closeSwipeLayout(ticketId);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh close called");

                    presenter.getClosedResolvedTickets(false, 0, System.currentTimeMillis(), 100);

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        //Do something after 1 sec
                        swipeRefreshLayout.setRefreshing(false);
                    }, 1000);
                }
        );
    }


    @Override
    public void onResume() {
        super.onResume();

        boolean fetchChanges = Hawk.get(Constants.FETCH_CLOSED_LIST, false);
        if (fetchChanges) {
            presenter.getClosedResolvedTickets(true, 0, System.currentTimeMillis(), 100);
        } else {
            closedTickets = TicketRepo.getInstance().getClosedResolvedTickets();
            setUpRecyclerView(closedTickets);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void getClosedTicketSuccess() {
        List<Tickets> closedTickets = TicketRepo.getInstance().getClosedResolvedTickets();
        GlobalUtils.showLog(TAG, "closed ticket size checK:" + closedTickets.size());
        if (!CollectionUtils.isEmpty(closedTickets)) {
            setUpRecyclerView(closedTickets);
            rvClosedTickets.setVisibility(View.VISIBLE);

            Hawk.put(Constants.FETCH_CLOSED_LIST, false);
            fetchList = true;
            ivDataNotFound.setVisibility(View.GONE);
        } else {
        /*    adapter = new TicketsAdapter(closedTickets, getContext());
            adapter.setData(closedTickets);
            adapter.notifyDataSetChanged();*/

            rvClosedTickets.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getClosedTicketFail(String msg) {
        ivDataNotFound.setVisibility(View.VISIBLE);
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }
        UiUtils.showSnackBar(getContext(), getActivity().getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onReopenSuccess(long ticketId) {
        adapter.deleteItem(reopenTicketPos, ticketId);
      /*  if (listener != null)
            listener.ticketReopened();*/
        Hawk.put(Constants.FETCH__ASSIGNED_LIST, true);
    }

    @Override
    public void onReopenFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }
        UiUtils.showSnackBar(getContext(), getActivity().getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
        ivDataNotFound.setVisibility(View.GONE);
        rvClosedTickets.setVisibility(View.GONE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
            rvClosedTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                message);
    }

 /*   @Override
    public void showClosedTicketList(List<Tickets> closedTicketList) {
        setUpRecyclerView(closedTicketList);
    }*/


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // check if parent Fragment implements listener
        if (getParentFragment() instanceof OnTicketReopenListener) {
            listener = (OnTicketReopenListener) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnTicketReopenedListener");
        }
    }


}


