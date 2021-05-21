package com.anydone.desk.inviteuserstocall;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.shasin.notificationbanner.Banner;
import com.anydone.desk.R;
import com.anydone.desk.adapters.SearchInviteUsersToCallAdapter;
import com.anydone.desk.base.activity.MvpBaseActivity;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Tickets;
import com.anydone.desk.realm.repo.TicketRepo;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.UiUtils;
import com.anydone.desk.videocallreceive.VideoCallHandleActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class InviteUsersActivity extends MvpBaseActivity<InviteUsersPresenterImpl> implements
        InviteUsersContract.InviteUsersView {

    private static final String TAG = "AssignEmployeeActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_add)
    MaterialButton btnAdd;
    @BindView(R.id.et_search_employee)
    EditText etSearchEmployee;
    @BindView(R.id.rv_employees)
    RecyclerView rvEmployees;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;

    private ProgressDialog progress;
    List<AssignEmployee> selectedEmployees = new ArrayList<>();
    private SearchInviteUsersToCallAdapter adapter;
    private long ticketId;
    private static VideoCallHandleActivity.AddedParticipantsReceiverCallback mAddedParticipantsReceiverCallback;

    public static void launch(Context context, VideoCallHandleActivity.AddedParticipantsReceiverCallback
            addedParticipantsReceiverCallback, String refId) {
        mAddedParticipantsReceiverCallback = addedParticipantsReceiverCallback;
        Intent intent = new Intent(context, InviteUsersActivity.class);
        intent.putExtra("ticket_id", refId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_invite_users;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        ticketId = Long.parseLong(i.getExtras().getString("ticket_id"));
        presenter.fetchContributors();
        ivBack.setOnClickListener(v -> onBackPressed());
        btnAdd.setOnClickListener(v -> {
            UiUtils.hideKeyboard(this);
            if (selectedEmployees.isEmpty()) {
                Toast.makeText(InviteUsersActivity.this,
                        "Please select member to add", Toast.LENGTH_SHORT).show();
            } else {
                for (AssignEmployee assignEmployee : selectedEmployees) {
                    Log.d("SelectedEmployees", "selected employees are: " + assignEmployee.getEmployeeId() + assignEmployee.getAccountId() + " " + assignEmployee.getName());
                }
                sendSelectedParticipants();
            }
        });

        etSearchEmployee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                runOnUiThread(() -> adapter.getFilter().filter(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setUpRecyclerView(List<AssignEmployee> assignEmployeeList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvEmployees.setLayoutManager(mLayoutManager);

        adapter = new SearchInviteUsersToCallAdapter(assignEmployeeList, this);
        adapter.setData(selectedEmployees);
        rvEmployees.setAdapter(adapter);


        adapter.setOnItemClickListener(new SearchInviteUsersToCallAdapter.OnItemClickListenerOnCall() {
            @Override
            public void onItemAdd(AssignEmployee employeeId) {
                GlobalUtils.showLog(TAG, "item add listen");
                selectedEmployees.add(employeeId);
                GlobalUtils.showLog(TAG, "employee list size: " + selectedEmployees.size());
                if (selectedEmployees.size() > 0) {
                    enableAddButton();
                }
            }

            @Override
            public void onItemRemove(AssignEmployee employeeId) {
                GlobalUtils.showLog(TAG, "item remove listen");
                selectedEmployees.remove(employeeId);
                GlobalUtils.showLog(TAG, "employee list size: " + selectedEmployees.size());
                if (selectedEmployees.size() == 0) {
                    disableAddButton();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    public void sendSelectedParticipants() {
        if (mAddedParticipantsReceiverCallback != null)
            mAddedParticipantsReceiverCallback.sendAddedParticipants(presenter.transform(selectedEmployees));
        finish();
    }

    @Override
    public void fetchContributorSuccess(List<AssignEmployee> contributorsList) {
        Tickets tickets = TicketRepo.getInstance().getTicketById(ticketId);
        AssignEmployee assignedEmployee = tickets.getAssignedEmployee();
        GlobalUtils.showLog(TAG, "assigned employee: " + assignedEmployee);
        AssignEmployee employeeToRemove = null;
        if (assignedEmployee != null) {
            for (AssignEmployee employee : contributorsList
            ) {
                if (employee.getEmployeeId().equalsIgnoreCase(assignedEmployee.getEmployeeId())) {
                    employeeToRemove = employee;
                }
            }
            contributorsList.remove(employeeToRemove);
        }
        setUpRecyclerView(contributorsList);
    }

    @Override
    public void fetchContributorsFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.BOTTOM, 2000).show();
    }

    @Override
    public void showProgressBar(String message) {
        pbProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        pbProgress.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                Constants.SERVER_ERROR);
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void enableAddButton() {
        btnAdd.setClickable(true);
        btnAdd.setBackgroundColor(btnAdd.getContext().getResources().getColor(R.color.colorPrimary));
    }

    private void disableAddButton() {
        btnAdd.setClickable(false);
        btnAdd.setBackgroundColor(btnAdd.getContext().getResources().getColor(R.color.btn_disabled));
    }
}