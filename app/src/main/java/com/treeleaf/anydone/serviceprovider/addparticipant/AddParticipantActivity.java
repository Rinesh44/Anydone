package com.treeleaf.anydone.serviceprovider.addparticipant;

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
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;
import com.treeleaf.anydone.serviceprovider.realm.model.Participant;
import com.treeleaf.anydone.serviceprovider.realm.repo.InboxRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AddParticipantActivity extends MvpBaseActivity<AddParticipantPresenterImpl> implements
        AddParticipantContract.AddParticipantView {

    private static final String TAG = "AddParticipantActivity";
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
    private String inboxId;

    @Override
    protected int getLayout() {
        return R.layout.activity_add_participant;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        inboxId = i.getStringExtra("inbox_id");
        Inbox inbox = InboxRepo.getInstance().getInboxById(inboxId);

        List<AssignEmployee> assignEmployeeList = getEmployeeList(inbox);
        presenter.findParticipants(assignEmployeeList);
        ivBack.setOnClickListener(v -> onBackPressed());
        btnAdd.setOnClickListener(v -> {
            if (employeeIds.isEmpty()) {
                Toast.makeText(AddParticipantActivity.this,
                        "Please select participant to add", Toast.LENGTH_SHORT).show();
            } else {
                for (AssignEmployee emp : assignEmployeeList
                ) {
                    employeeIds.add(emp.getEmployeeId());
                }
                presenter.addParticipant(inboxId, employeeIds);
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

    private List<AssignEmployee> getEmployeeList(Inbox inbox) {
        List<AssignEmployee> employees = new ArrayList<>();
        for (Participant participant : inbox.getParticipantList()
        ) {
            employees.add(participant.getEmployee());
        }

        return employees;
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
    public void addParticipantSuccess() {
        Intent intent = new Intent();
        intent.putExtra("participant_added", true);
//        intent.putStringArrayListExtra("participants", (ArrayList<String>) employeeIds);
        setResult(2, intent);
        finish();
    }

    @Override
    public void addParticipantFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void getParticipantSuccess(List<AssignEmployee> participantList) {
        setUpRecyclerView(participantList);
    }

    @Override
    public void getParticipantFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();
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