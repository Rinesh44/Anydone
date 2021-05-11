package com.treeleaf.anydone.serviceprovider.inviteuserstocall;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.SearchContributorAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

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
    List<String> employeeIds = new ArrayList<>();
    private SearchContributorAdapter adapter;
    private long ticketId;

    @Override
    protected int getLayout() {
        return R.layout.activity_invite_users;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        ticketId = Long.parseLong(i.getExtras().getString("ticket_id"));
        presenter.findContributors();
        ivBack.setOnClickListener(v -> onBackPressed());
        btnAdd.setOnClickListener(v -> {
            if (employeeIds.isEmpty()) {
                Toast.makeText(InviteUsersActivity.this,
                        "Please select contributor to add", Toast.LENGTH_SHORT).show();
            } else {
                presenter.addContributor(ticketId, employeeIds);
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

        UiUtils.showKeyboard(this, etSearchEmployee);

    }

    private void setUpRecyclerView(List<AssignEmployee> assignEmployeeList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvEmployees.setLayoutManager(mLayoutManager);

        adapter = new SearchContributorAdapter(assignEmployeeList, this);
        adapter.setData(employeeIds);
        rvEmployees.setAdapter(adapter);


        adapter.setOnItemClickListener(new SearchContributorAdapter.OnItemClickListener() {
            @Override
            public void onItemAdd(String employeeId) {
                GlobalUtils.showLog(TAG, "item add listen");
                employeeIds.add(employeeId);
                GlobalUtils.showLog(TAG, "employee list size: " + employeeIds.size());
                if (employeeIds.size() > 0) {
                    enableAddButton();
                }
            }

            @Override
            public void onItemRemove(String employeeId) {
                GlobalUtils.showLog(TAG, "item remove listen");
                employeeIds.remove(employeeId);
                GlobalUtils.showLog(TAG, "employee list size: " + employeeIds.size());
                if (employeeIds.size() == 0) {
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

    @Override
    public void addContributorSuccess() {
        Intent intent = new Intent();
        intent.putExtra("contributor_added", true);
        intent.putStringArrayListExtra("contributors", (ArrayList<String>) employeeIds);
        setResult(2, intent);
        finish();
    }

    @Override
    public void addContributorFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.BOTTOM, 2000).show();
    }

    @Override
    public void getContributorSuccess(List<AssignEmployee> contributorsList) {
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
    public void getContributorsFail(String msg) {
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