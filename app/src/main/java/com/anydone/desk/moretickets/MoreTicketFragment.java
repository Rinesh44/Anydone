package com.anydone.desk.moretickets;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anydone.desk.R;
import com.anydone.desk.alltickets.AllTicketsActivity;
import com.anydone.desk.base.fragment.BaseFragment;
import com.anydone.desk.contributed.ContributedTicketsActivity;
import com.anydone.desk.injection.component.ApplicationComponent;
import com.anydone.desk.opentickets.OpenTicketActivity;
import com.anydone.desk.ownedtickets.OwnedTicketActivity;
import com.anydone.desk.subscribed.SubscribedTicketsActivity;
import com.anydone.desk.tickets.unassignedtickets.UnassignedTicketsActivity;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.UiUtils;

import java.util.Objects;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreTicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreTicketFragment extends BaseFragment<MoreTicketPresenterImpl>
        implements MoreTicketContract.MoreTicketView {

    private static final String TAG = "MoreTicketFragment";
    @BindView(R.id.rl_contributed)
    RelativeLayout rlContributed;
    @BindView(R.id.rl_subscribed)
    RelativeLayout rlSubscribed;
    @BindView(R.id.rl_backlog)
    RelativeLayout rlBacklog;
    @BindView(R.id.rl_all)
    RelativeLayout rlAll;
    @BindView(R.id.rl_owned)
    RelativeLayout rlOwned;
    @BindView(R.id.rl_open_for_me)
    RelativeLayout rlOpen;
    private long mLastClickTime = 0;
 /*   @BindView(R.id.btn_add_ticket)
    MaterialButton btnAddTicket;*/

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoreTicketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreTicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreTicketFragment newInstance(String param1, String param2) {
        MoreTicketFragment fragment = new MoreTicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter.getServices();
        rlAll.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            startActivity(new Intent(getActivity(), AllTicketsActivity.class));
        });

        rlContributed.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            startActivity(new Intent(getActivity(), ContributedTicketsActivity.class));
        });

        rlBacklog.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            startActivity(new Intent(getActivity(), UnassignedTicketsActivity.class));
        });

        rlSubscribed.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            startActivity(new Intent(getActivity(), SubscribedTicketsActivity.class));
        });

        rlOpen.setOnClickListener(v ->
        {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            startActivity(new Intent(getActivity(), OpenTicketActivity.class));
        });

        rlOwned.setOnClickListener(v ->
        {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            startActivity(new Intent(getActivity(), OwnedTicketActivity.class));
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_more_ticket;
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.findEmployees();
        presenter.findTeams();
        presenter.findTicketTypes();
    }

    @Override
    public void getEmployeeSuccess() {
        GlobalUtils.showLog(TAG, "get employees success");
    }

    @Override
    public void getEmployeeFail(String msg) {

    }

    @Override
    public void getTicketTypeSuccess() {
        GlobalUtils.showLog(TAG, "get ticket type success");
    }

    @Override
    public void getTicketTypeFail(String msg) {

    }

    @Override
    public void getTeamSuccess() {
        GlobalUtils.showLog(TAG, "get teams success");
    }

    @Override
    public void getTeamFail(String msg) {

    }

    @Override
    public void getServicesSuccess() {

    }

    @Override
    public void getServicesFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
    }

    @Override
    public void showProgressBar(String message) {

    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void onFailure(String message) {

    }

/*    @OnClick(R.id.btn_add_ticket)
    void addTicket() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        Intent i = new Intent(getActivity(), AddTicketActivity.class);
        startActivity(i);
    }*/
}