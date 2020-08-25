package com.treeleaf.anydone.serviceprovider.addticket;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.CustomerSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.EmployeeSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.TagSearchAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.repo.AssignEmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.CustomerRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.EmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TagRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddTicketActivity extends MvpBaseActivity<AddTicketPresenterImpl> implements
        AddTicketContract.AddTicketView {

    private static final String TAG = "AddTicketActivity";

    @BindView(R.id.et_customer_name)
    AutoCompleteTextView etCustomerName;
    @BindView(R.id.et_label)
    AppCompatAutoCompleteTextView etLabel;
    @BindView(R.id.fbl_label)
    FlexboxLayout fblLabel;
    @BindView(R.id.search_employee)
    ScrollView svSearchEmployee;
    @BindView(R.id.fbl_employee)
    FlexboxLayout fblEmployee;
    @BindView(R.id.civ_image_self)
    CircleImageView civSelfImage;
    @BindView(R.id.tv_name_self)
    TextView tvSelfName;
    @BindView(R.id.et_assign_employee)
    EditText etAssignEmployee;
    @BindView(R.id.rv_all_users)
    RecyclerView rvAllUsers;
    @BindView(R.id.ll_self)
    LinearLayout llSelf;
    @BindView(R.id.btn_create_ticket)
    MaterialButton btnCreateTicket;
    @BindView(R.id.et_email)
    TextInputEditText etEmail;
    @BindView(R.id.et_phone)
    TextInputEditText etPhone;
    @BindView(R.id.il_phone)
    TextInputLayout ilPhone;
    @BindView(R.id.il_email)
    TextInputLayout ilEmail;
    @BindView(R.id.il_summary)
    TextInputLayout ilSummary;
    @BindView(R.id.il_description)
    TextInputLayout ilDesc;
    @BindView(R.id.il_customer_name)
    TextInputLayout ilCustomerName;
    @BindView(R.id.et_summary)
    TextInputEditText etSummary;
    @BindView(R.id.et_description)
    TextInputEditText etDesc;
    @BindView(R.id.rl_customer_self_holder)
    RelativeLayout rlCustomerSelfHolder;
    @BindView(R.id.civ_self)
    CircleImageView civSelf;
    @BindView(R.id.tv_customer_self)
    TextView tvCustomerSelf;
    @BindView(R.id.pb_progress)
    ProgressBar progress;

    private List<AssignEmployee> employeeList;
    private List<Customer> customerList;
    private List<Tags> tagsList;

    private TagSearchAdapter tagSearchAdapter;
    private EmployeeSearchAdapter employeeSearchAdapter;
    private CustomerSearchAdapter customerSearchAdapter;
    private Customer selectedCustomer;
    private Tags selectedTag;
    private Employee selfEmployee;

    @Override
    protected int getLayout() {
        return R.layout.activity_add_ticket;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar();
        presenter.findCustomers();
        presenter.findEmployees();
        presenter.findTags();

        setSelfDetails();

        selfEmployee = EmployeeRepo.getInstance().getEmployee();
        tagsList = TagRepo.getInstance().getAllTags();
        GlobalUtils.showLog(TAG, "tag list size: " + tagsList.size());
        tagSearchAdapter = new TagSearchAdapter(this, tagsList);
        etLabel.setAdapter(tagSearchAdapter);

        etCustomerName.setOnItemClickListener((parent, view, position, id) -> {
            UiUtils.hideKeyboard(this);
            if (!CollectionUtils.isEmpty(customerList)) {
                selectedCustomer = customerList.get(position);
                if (selectedCustomer.getEmail() != null && !selectedCustomer.getEmail().isEmpty()) {
                    etEmail.setText(selectedCustomer.getEmail());
                    etEmail.setFocusable(false);
                    etEmail.setEnabled(false);
                }

                if (selectedCustomer.getPhone() != null && !selectedCustomer.getPhone().isEmpty()) {
                    etPhone.setText(selectedCustomer.getPhone());
                    etPhone.setFocusable(false);
                    etPhone.setEnabled(false);
                }

            }
        });

        etCustomerName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tvCustomerSelf.setText(selfEmployee.getName() + " (Me)");

                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_profile_icon)
                        .error(R.drawable.ic_profile_icon);

                Glide.with(AddTicketActivity.this).load(selfEmployee.getEmployeeImageUrl())
                        .apply(options).into(civSelf);

                rlCustomerSelfHolder.setVisibility(View.VISIBLE);
            }
        });

        rlCustomerSelfHolder.setOnClickListener(v -> {
            selectedCustomer = new Customer();
            GlobalUtils.showLog(TAG, "employee checkout:" + selfEmployee);
            selectedCustomer.setCustomerId(selfEmployee.getEmployeeId());
            selectedCustomer.setEmail(selfEmployee.getEmail());
            selectedCustomer.setPhone(selfEmployee.getPhone());
            selectedCustomer.setFullName(selfEmployee.getName());
            selectedCustomer.setProfilePic(selfEmployee.getEmployeeImageUrl());

            etCustomerName.setText(selectedCustomer.getFullName());
            etCustomerName.dismissDropDown();
            if (selectedCustomer.getEmail() != null && !selectedCustomer.getEmail().isEmpty()) {
                etEmail.setText(selectedCustomer.getEmail());
                etEmail.setFocusable(false);
                etEmail.setEnabled(false);
            }

            if (selectedCustomer.getPhone() != null && !selectedCustomer.getPhone().isEmpty()) {
                etPhone.setText(selectedCustomer.getPhone());
                etPhone.setFocusable(false);
                etPhone.setEnabled(false);
            }

            etCustomerName.setSelection(etCustomerName.getText().length());
            rlCustomerSelfHolder.setVisibility(View.GONE);
        });

        etCustomerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    tvCustomerSelf.setText(selfEmployee.getName() + " (Me)");

                    RequestOptions options = new RequestOptions()
                            .fitCenter()
                            .placeholder(R.drawable.ic_profile_icon)
                            .error(R.drawable.ic_profile_icon);

                    Glide.with(AddTicketActivity.this).load(selfEmployee.getEmployeeImageUrl())
                            .apply(options).into(civSelfImage);

                    rlCustomerSelfHolder.setVisibility(View.VISIBLE);
                } else {
                    rlCustomerSelfHolder.setVisibility(View.GONE);
                    etEmail.setEnabled(true);
                    etEmail.setFocusableInTouchMode(true);
                    etEmail.setText("");

                    etPhone.setEnabled(true);
                    etPhone.setFocusableInTouchMode(true);
                    etPhone.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etLabel.setOnItemClickListener((parent, view, position, id) -> {
            if (!CollectionUtils.isEmpty(tagsList)) {
                selectedTag = tagsList.get(position);
                addNewTagChip(selectedTag);
            }
            etLabel.setText("");
        });

        llSelf.setOnClickListener(v -> {
            Employee self = EmployeeRepo.getInstance().getEmployee();
            if (self != null) {
                AssignEmployee selfEmployee = new AssignEmployee();
                selfEmployee.setPhone(self.getPhone());
                selfEmployee.setName(self.getName());
                selfEmployee.setEmployeeImageUrl(self.getEmployeeImageUrl());
                selfEmployee.setEmployeeId(self.getEmployeeId());
                selfEmployee.setEmail(self.getEmail());
                selfEmployee.setCreatedAt(self.getCreatedAt());
                selfEmployee.setAccountId(self.getAccountId());

                addNewEmployeeChip(selfEmployee);
            }

            etAssignEmployee.setText("");
        });

        etAssignEmployee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {
                    GlobalUtils.showLog(TAG, "text changed");
                    employeeList = AssignEmployeeRepo.getInstance().searchEmployee(s.toString());
                    GlobalUtils.showLog(TAG, "searched list size: " + employeeList.size());
                    if (svSearchEmployee.getVisibility() == View.GONE)
                        svSearchEmployee.setVisibility(View.VISIBLE);
                    employeeSearchAdapter.setData(employeeList);
                    employeeSearchAdapter.notifyDataSetChanged();
                } else {
                    svSearchEmployee.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnCreateTicket.setOnClickListener(v -> {
            List<String> tags = new ArrayList<>();
            List<String> assignedEmployees = new ArrayList<>();

            if (fblLabel.getChildCount() > 1) {
                for (int i = 0; i < fblLabel.getChildCount(); i++) {
                    View tag = fblLabel.getChildAt(i);
                    TextView tagId = tag.findViewById(R.id.tv_chip_id);
                    if (tagId != null) {
                        GlobalUtils.showLog(TAG, "tags ids: " + tagId.getText());
                        tags.add(tagId.getText().toString().trim());
                    }
                }
            }

            if (fblEmployee.getChildCount() > 1) {
                for (int i = 0; i < fblEmployee.getChildCount(); i++) {
                    View employee = fblEmployee.getChildAt(i);
                    TextView employeeId = employee.findViewById(R.id.tv_chip_id);
                    if (employeeId != null) {
                        GlobalUtils.showLog(TAG, "tags ids: " + employeeId.getText());
                        assignedEmployees.add(employeeId.getText().toString().trim());
                    }

                }
            }

            if (selectedCustomer != null) {
                presenter.createTicket(UiUtils.getString(etSummary), UiUtils.getString(etDesc),
                        selectedCustomer.getCustomerId(), UiUtils.getString(etEmail),
                        UiUtils.getString(etPhone), selectedCustomer.getFullName(), tags,
                        assignedEmployees);
            } else {
                presenter.createTicket(UiUtils.getString(etSummary), UiUtils.getString(etDesc),
                        null, UiUtils.getString(etEmail),
                        UiUtils.getString(etPhone), UiUtils.getString(etCustomerName), tags,
                        assignedEmployees);
            }
        });


    }

    private void setSelfDetails() {
        Employee employee = EmployeeRepo.getInstance().getEmployee();
        if (employee != null) {
            tvSelfName.setText(employee.getName() + " (Me)");

            String profilePicUrl = employee.getEmployeeImageUrl();
            if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_profile_icon)
                        .error(R.drawable.ic_profile_icon);

                Glide.with(this).load(profilePicUrl).apply(options).into(civSelfImage);
            }
        }
    }

    private void addNewTagChip(Tags selectedTag) {
        View chip = getLayoutInflater().inflate(R.layout.custom_chip, null);
        CircleImageView civEmployee = chip.findViewById(R.id.civ_employee);
        TextView tvEmployee = chip.findViewById(R.id.tv_name);
        ImageView ivCancel = chip.findViewById(R.id.iv_cancel);
        TextView tvId = chip.findViewById(R.id.tv_chip_id);

        civEmployee.setVisibility(View.GONE);
        tvEmployee.setText(selectedTag.getLabel());
        tvId.setText(selectedTag.getTagId());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 20, 10);
        chip.setLayoutParams(params);
        fblLabel.addView(chip, fblLabel.getChildCount() - 1);
        ivCancel.setOnClickListener(v -> fblLabel.removeView(chip));
    }

    private void addNewEmployeeChip(AssignEmployee selectedEmployee) {
        View chip = getLayoutInflater().inflate(R.layout.custom_chip, null);
        CircleImageView civEmployee = chip.findViewById(R.id.civ_employee);
        TextView tvEmployee = chip.findViewById(R.id.tv_name);
        ImageView ivCancel = chip.findViewById(R.id.iv_cancel);
        TextView tvId = chip.findViewById(R.id.tv_chip_id);

        tvId.setText(selectedEmployee.getEmployeeId());
        String profilePicUrl = selectedEmployee.getEmployeeImageUrl();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_profile_icon)
                    .error(R.drawable.ic_profile_icon);

            Glide.with(this).load(profilePicUrl).apply(options).into(civEmployee);
        }
        tvEmployee.setText(selectedEmployee.getName());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 20, 10);
        chip.setLayoutParams(params);

        fblEmployee.addView(chip, fblEmployee.getChildCount() - 1);
        ivCancel.setOnClickListener(v -> fblEmployee.removeView(chip));
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void findEmployeeSuccess() {
        employeeList = AssignEmployeeRepo.getInstance().getAllAssignEmployees();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvAllUsers.setLayoutManager(mLayoutManager);

        employeeSearchAdapter = new EmployeeSearchAdapter(employeeList, this);
        rvAllUsers.setAdapter(employeeSearchAdapter);

        if (employeeSearchAdapter != null) {
            employeeSearchAdapter.setOnItemClickListener((employee) -> {
                svSearchEmployee.setVisibility(View.GONE);
                hideKeyBoard();
                addNewEmployeeChip(employee);

                etAssignEmployee.setText("");
            });
        }
    }

    @Override
    public void findEmployeeFail(String msg) {
        employeeList = AssignEmployeeRepo.getInstance().getAllAssignEmployees();
        setUpRecyclerView();

        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();

    }

    @Override
    public void findCustomerSuccess() {
        customerList = CustomerRepo.getInstance().getAllCustomers();
        customerSearchAdapter = new CustomerSearchAdapter(this, customerList);
        etCustomerName.setAdapter(customerSearchAdapter);
    }

    @Override
    public void findCustomerFail(String msg) {

    }

    @Override
    public void findTagsSuccess() {
        tagsList = TagRepo.getInstance().getAllTags();
        tagSearchAdapter = new TagSearchAdapter(this, tagsList);
        etLabel.setAdapter(tagSearchAdapter);
    }

    @Override
    public void findTagsFail(String msg) {
        tagsList = TagRepo.getInstance().getAllTags();
        GlobalUtils.showLog(TAG, "tag list size: " + tagsList.size());
        tagSearchAdapter = new TagSearchAdapter(this, tagsList);
        etLabel.setAdapter(tagSearchAdapter);

        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.BOTTOM, 2000).show();

    }

    @Override
    public void onCreateTicketSuccess() {
        finish();
    }

    @Override
    public void onCreateTicketFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.BOTTOM, 2000).show();

    }

    @Override
    public void onInvalidSummary() {
        etSummary.requestFocus();
        ilSummary.setErrorEnabled(true);
        ilSummary.setError("Invalid Summary");

        ilDesc.setErrorEnabled(false);
        ilCustomerName.setErrorEnabled(false);
        ilPhone.setErrorEnabled(false);
        ilEmail.setErrorEnabled(false);

    }

    @Override
    public void onInvalidDesc() {
        etDesc.requestFocus();
        ilDesc.setErrorEnabled(true);
        ilDesc.setError("Invalid Description");

        ilSummary.setErrorEnabled(false);
        ilCustomerName.setErrorEnabled(false);
        ilPhone.setErrorEnabled(false);
        ilEmail.setErrorEnabled(false);
    }

    @Override
    public void onInvalidCustomer() {
        etCustomerName.requestFocus();
        ilCustomerName.setErrorEnabled(true);
        ilCustomerName.setError("Invalid Customer");

        ilDesc.setErrorEnabled(false);
        ilSummary.setErrorEnabled(false);
        ilPhone.setErrorEnabled(false);
        ilEmail.setErrorEnabled(false);
    }

    @Override
    public void onInvalidPhone() {
        etPhone.requestFocus();
        ilPhone.setErrorEnabled(true);
        ilPhone.setError("Invalid Phone");

        ilDesc.setErrorEnabled(false);
        ilCustomerName.setErrorEnabled(false);
        ilSummary.setErrorEnabled(false);
        ilEmail.setErrorEnabled(false);
    }

    @Override
    public void onInvalidEmail() {
        etEmail.requestFocus();
        ilEmail.setErrorEnabled(true);
        ilEmail.setError("Invalid Email");

        ilDesc.setErrorEnabled(false);
        ilCustomerName.setErrorEnabled(false);
        ilPhone.setErrorEnabled(false);
        ilSummary.setErrorEnabled(false);
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

    private void setToolbar() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Create Ticket");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0,
                str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}