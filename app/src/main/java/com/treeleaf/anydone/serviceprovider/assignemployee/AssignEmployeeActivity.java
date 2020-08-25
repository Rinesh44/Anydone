package com.treeleaf.anydone.serviceprovider.assignemployee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.SearchEmployeeAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AssignEmployeeActivity extends MvpBaseActivity<AssignEmployeePresenterImpl> implements
        AssignEmployeeContract.AssignEmployeeView {

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
    ProgressBar progress;

    List<Employee> employees = new ArrayList<>();
    private SearchEmployeeAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_assign_employee;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent i = getIntent();
        long ticketId = i.getLongExtra("ticket_id", -1);
        GlobalUtils.showLog(TAG, "received ticket id: " + ticketId);
        presenter.getEmployees();
        ivBack.setOnClickListener(v -> onBackPressed());
        btnAdd.setOnClickListener(v -> {
            if (employees.isEmpty()) {
                Toast.makeText(AssignEmployeeActivity.this, "Please select employee to add", Toast.LENGTH_SHORT).show();
            } else {
                presenter.assignEmployee(ticketId, employees);
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

    private void setUpRecyclerView(List<Employee> assignEmployeeList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvEmployees.setLayoutManager(mLayoutManager);

        adapter = new SearchEmployeeAdapter(assignEmployeeList, this);
        rvEmployees.setAdapter(adapter);

        adapter.setOnItemClickListener(new SearchEmployeeAdapter.OnItemClickListener() {
            @Override
            public void onItemAdd(Employee employee) {
                GlobalUtils.showLog(TAG, "item add listen");
                Employee addEmployee = new Employee();
                addEmployee.setName(employee.getName());
                addEmployee.setEmployeeImageUrl(employee.getEmployeeImageUrl());
                addEmployee.setEmployeeId(employee.getEmployeeId());
                addEmployee.setCreatedAt(employee.getCreatedAt());
                addEmployee.setAccountId(employee.getAccountId());
                addEmployee.setEmail(employee.getEmail());
                addEmployee.setPhone(employee.getPhone());
                employees.add(addEmployee);
                GlobalUtils.showLog(TAG, "employee list size: " + employees.size());
                if (employees.size() > 0) {
                    enableAddButton();
                }
            }

            @Override
            public void onItemRemove(Employee employee) {
                GlobalUtils.showLog(TAG, "item remove listen");
                employees.remove(employee);
                GlobalUtils.showLog(TAG, "employee list size: " + employees.size());
                if (employees.size() == 0) {
                    disableAddButton();
                }
            }
        });
    }


    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void assignEmployeeSuccess() {
        Intent intent = new Intent();
        intent.putExtra("employee_assigned", true);
        setResult(2, intent);
        finish();
    }

    @Override
    public void assignEmployeeFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.BOTTOM, 2000).show();
    }

    @Override
    public void getEmployeesSuccess(List<Employee> assignEmployeeList) {
        setUpRecyclerView(assignEmployeeList);
    }

    @Override
    public void getEmployeesFail(String msg) {
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
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), message);
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