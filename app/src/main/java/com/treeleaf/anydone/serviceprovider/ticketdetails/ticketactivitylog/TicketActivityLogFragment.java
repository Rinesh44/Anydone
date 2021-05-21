package com.treeleaf.anydone.serviceprovider.ticketdetails.ticketactivitylog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.ActivityLogAdapter;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.realm.model.ActivityLog;
import com.treeleaf.anydone.serviceprovider.realm.repo.ActivityLogRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class TicketActivityLogFragment extends BaseFragment<TicketActivityLogPresenterImpl>
        implements TicketActivityLogContract.TicketActivityLogView {
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.rv_activity_log)
    RecyclerView rvActivityLog;
    private long ticketId;

    private static final String TAG = "TicketActivityLogFragme";
    private ActivityLogAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.layout_activity_log;
    }

    @Override
    public void onViewCreated(@NonNull @io.reactivex.annotations.NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GlobalUtils.showLog(TAG, "view created called");
        Intent i = Objects.requireNonNull(getActivity()).getIntent();
        ticketId = i.getLongExtra("selected_ticket_id", -1);

        List<ActivityLog> activityLogList = ActivityLogRepo.getInstance().getAllLogs(ticketId);
        setUpLogRecyclerView(activityLogList);
        if (activityLogList.isEmpty()) {
            presenter.getActivityLog(String.valueOf(ticketId), true);
        }
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getActivityLog(String.valueOf(ticketId), false);

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
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                Constants.SERVER_ERROR);
    }

    @Override
    public void getActivityLogSuccess() {
        List<ActivityLog> activityLogList = ActivityLogRepo.getInstance().getAllLogs(ticketId);
        adapter.setData(activityLogList);
    }

    private void setUpLogRecyclerView(List<ActivityLog> logList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvActivityLog.setLayoutManager(mLayoutManager);

        adapter = new ActivityLogAdapter(logList, getActivity());
        rvActivityLog.setAdapter(adapter);
    }

    @Override
    public void getActivityLogFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
    }
}
