package com.treeleaf.anydone.serviceprovider.addticket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.CustomerSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.EmployeeSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.SearchTeamAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.repo.AssignEmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.CustomerRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.EmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ServiceProviderRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TagRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddTicketActivity extends MvpBaseActivity<AddTicketPresenterImpl> implements
        AddTicketContract.AddTicketView {

    private static final String TAG = "AddTicketActivity";
    @BindView(R.id.root)
    LinearLayout llRoot;
    @BindView(R.id.et_customer_name)
    AutoCompleteTextView etCustomerName;
    @BindView(R.id.et_assign_employee)
    EditText etAssignEmployee;
    @BindView(R.id.search_employee)
    ScrollView svSearchEmployee;
    @BindView(R.id.ll_self)
    LinearLayout llEmployeeAsSelf;
    @BindView(R.id.civ_image_self)
    CircleImageView civEmployeeAsSelf;
    @BindView(R.id.tv_name_self)
    TextView tvEmployeeAsSelf;
    @BindView(R.id.tv_all_users)
    TextView tvEmployeeAllUsers;
    @BindView(R.id.rv_all_users)
    RecyclerView rvEmployeeAllUsers;
    @BindView(R.id.btn_create_ticket)
    MaterialButton btnCreateTicket;
    @BindView(R.id.et_email)
    AppCompatEditText etEmail;
    @BindView(R.id.et_phone)
    AppCompatEditText etPhone;
    @BindView(R.id.et_summary)
    AppCompatEditText etSummary;
    @BindView(R.id.et_description)
    AppCompatEditText etDesc;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    @BindView(R.id.et_priority)
    AppCompatEditText etPriority;
    @BindView(R.id.rl_customer_self_holder)
    RelativeLayout rlCustomerSelfHolder;
    @BindView(R.id.tv_customer_self)
    TextView tvCustomerSelf;
    @BindView(R.id.civ_self)
    CircleImageView civSelf;
    @BindView(R.id.iv_priority)
    ImageView ivPriority;
    @BindView(R.id.civ_customer)
    CircleImageView civCustomer;
    @BindView(R.id.civ_assign_employee)
    CircleImageView civAssignEmployee;
    @BindView(R.id.fbl_label)
    FlexboxLayout fblLabel;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.tv_suggestions)
    TextView tvSuggestions;

    private List<AssignEmployee> employeeList;
    private List<Customer> customerList;
    private List<Tags> tagsList;

    private EmployeeSearchAdapter employeeSearchAdapter;
    private Customer selectedCustomer;
    private Tags selectedTag;
    private Employee selfEmployee;
    private ServiceProvider serviceProvider;
    private String selectedEmployeeId;
    private boolean createTicketFromThread;
    private BottomSheetDialog prioritySheet;
    private BottomSheetDialog teamSheet;
    private int priorityNum = 3;
    private EditText etSearchTeam;
    private SearchTeamAdapter teamAdapter;
    List<String> tags = new ArrayList<>();
    private RecyclerView rvTeams;
    private int lastDescCursorPosition = 0;
    private String description = "";
    private TicketProto.TicketSource ticketSource = TicketProto.TicketSource.MANUAL_TICKET_SOURCE;

    @Override
    protected int getLayout() {
        return R.layout.activity_add_ticket;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar();
        selfEmployee = EmployeeRepo.getInstance().getEmployee();
        serviceProvider = ServiceProviderRepo.getInstance().getServiceProvider();
        employeeList = AssignEmployeeRepo.getInstance().getAllAssignEmployees();
        setUpRecyclerView();

        customerList = CustomerRepo.getInstance().getAllCustomers();
        CustomerSearchAdapter customerSearchAdapter = new CustomerSearchAdapter(this, customerList);
        etCustomerName.setAdapter(customerSearchAdapter);

        tagsList = TagRepo.getInstance().getAllTags();
        createPriorityBottomSheet();
        createTeamBottomSheet();
//        setUpTeamRecyclerView();

        setSelfDetails();

        Intent intent = getIntent();
        createTicketFromThread = intent.getBooleanExtra("create_ticket_from_thread", false);

        if (createTicketFromThread) {
            setDataFromThread(intent);
            ticketSource = TicketProto.TicketSource.CONVERSATION_TICKET_SOURCE;
        }

        etDesc.setOnTouchListener((v, event) -> {
            if (v.getId() == R.id.et_description) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }
            }
            return false;
        });

        etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastDescCursorPosition = etDesc.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etDesc.removeTextChangedListener(this);

                if (etDesc.getLineCount() > 7) {
                    etDesc.setText(description);
                    etDesc.setSelection(lastDescCursorPosition);
                } else
                    description = Objects.requireNonNull(etDesc.getText()).toString();

                etDesc.addTextChangedListener(this);
            }
        });

        fblLabel.setOnClickListener(v -> teamSheet.show());

        etCustomerName.setOnItemClickListener((parent, view, position, id) -> {
            UiUtils.hideKeyboard(this);
            etCustomerName.setError(null);
            rlCustomerSelfHolder.setVisibility(View.GONE);
            if (!CollectionUtils.isEmpty(customerList)) {
                selectedCustomer = customerList.get(position);
                setEmailAndPhoneIfAvailable();
                showCustomerWithImage();
            }
        });


        etCustomerName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && selfEmployee != null) {
                etCustomerName.setError(null);
                if (selfEmployee != null) {
                    StringBuilder selfCustomerText = new StringBuilder(selfEmployee.getName());
                    selfCustomerText.append(" (Me)");
                    tvCustomerSelf.setText(selfCustomerText);
                } else {
                    tvCustomerSelf.setVisibility(View.GONE);
                }

                if (selfEmployee != null) {
                    RequestOptions options = new RequestOptions()
                            .fitCenter()
                            .placeholder(R.drawable.ic_profile_icon)
                            .error(R.drawable.ic_profile_icon);

                    Glide.with(AddTicketActivity.this).load(selfEmployee.getEmployeeImageUrl())
                            .apply(options).into(civSelf);
                } else {
                    civSelf.setVisibility(View.GONE);
                }

                rlCustomerSelfHolder.setVisibility(View.VISIBLE);
            } else {
                rlCustomerSelfHolder.setVisibility(View.GONE);
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
            showCustomerWithImage();

            if (selectedCustomer.getEmail() != null && !selectedCustomer.getEmail().isEmpty()) {
                etEmail.setText(selectedCustomer.getEmail());
                etEmail.setFocusable(false);
                etEmail.setEnabled(false);

                etPhone.setFocusable(false);
                etPhone.setEnabled(false);
            }

            if (selectedCustomer.getPhone() != null && !selectedCustomer.getPhone().isEmpty()) {
                etPhone.setText(selectedCustomer.getPhone());
                etPhone.setFocusable(false);
                etPhone.setEnabled(false);

                etEmail.setFocusable(false);
                etEmail.setEnabled(false);
            }

            etCustomerName.setSelection(etCustomerName.getText().length());
            rlCustomerSelfHolder.setVisibility(View.GONE);
            hideKeyBoard();
        });

        etPriority.setOnClickListener(v -> prioritySheet.show());

        etCustomerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && selfEmployee != null) {
                    StringBuilder selfCustomerText = new StringBuilder(selfEmployee.getName());
                    selfCustomerText.append(" (Me)");
                    tvCustomerSelf.setText(selfCustomerText);

                    RequestOptions options = new RequestOptions()
                            .fitCenter()
                            .placeholder(R.drawable.ic_profile_icon)
                            .error(R.drawable.ic_profile_icon);

                    Glide.with(AddTicketActivity.this).load(selfEmployee.getEmployeeImageUrl())
                            .apply(options).into(civSelf);

                    rlCustomerSelfHolder.setVisibility(View.VISIBLE);
                    civCustomer.setVisibility(View.GONE);
                    etCustomerName.setPadding(15, 0, 40, 0);
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

        llEmployeeAsSelf.setOnClickListener(v -> {
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

                selectedEmployeeId = self.getEmployeeId();
                showEmployeeWithImage(selfEmployee);
            }

            etAssignEmployee.setText(selfEmployee.getName());
            etAssignEmployee.setSelection(selfEmployee.getName().length());
            svSearchEmployee.setVisibility(View.GONE);

            hideKeyBoard();
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
                    if (CollectionUtils.isEmpty(employeeList)) {
                        tvEmployeeAllUsers.setVisibility(View.GONE);
                    } else {
                        tvEmployeeAllUsers.setVisibility(View.VISIBLE);
                    }
                    GlobalUtils.showLog(TAG, "searched list size: " + employeeList.size());
                    if (svSearchEmployee.getVisibility() == View.GONE)
                        svSearchEmployee.setVisibility(View.VISIBLE);
                    if (employeeSearchAdapter != null) {
                        employeeSearchAdapter.setData(employeeList);
                        employeeSearchAdapter.notifyDataSetChanged();
                    }
                } else {
                    svSearchEmployee.setVisibility(View.GONE);
                    etAssignEmployee.setPadding(15, 0, 40, 0);
                    civAssignEmployee.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etAssignEmployee.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                svSearchEmployee.setVisibility(View.GONE);
            }
        });

        btnCreateTicket.setOnClickListener(v -> {
            GlobalUtils.showLog(TAG, "emp id checK: " + selectedEmployeeId);

            if (selectedCustomer != null) {
                presenter.createTicket(UiUtils.getString(etSummary), UiUtils.getString(etDesc),
                        selectedCustomer.getCustomerId(), UiUtils.getString(etEmail),
                        UiUtils.getString(etPhone), selectedCustomer.getFullName(), tags,
                        selectedEmployeeId, priorityNum, ticketSource);
            } else {
                presenter.createTicket(UiUtils.getString(etSummary), UiUtils.getString(etDesc),
                        null, UiUtils.getString(etEmail),
                        UiUtils.getString(etPhone), UiUtils.getString(etCustomerName), tags,
                        selectedEmployeeId, priorityNum, ticketSource);
            }
        });
    }

    private void showCustomerWithImage() {
        etCustomerName.setPadding(80, 0, 40, 0);
        civCustomer.setVisibility(View.VISIBLE);

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_profile_icon)
                .error(R.drawable.ic_profile_icon);

        Glide.with(this).load(selectedCustomer.getProfilePic()).apply(options).into(civCustomer);
    }

    private void createTeamBottomSheet() {
        teamSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.layout_bottom_sheet_team, null);

        teamSheet.setContentView(view);
        TextView tvTeamDone = view.findViewById(R.id.tv_done);
        etSearchTeam = view.findViewById(R.id.et_search_employee);
        rvTeams = view.findViewById(R.id.rv_teams);

        setUpTeamRecyclerView(tagsList, rvTeams);

        teamSheet.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            setupFullHeight(d);
            etSearchTeam.requestFocus();
            UiUtils.showKeyboardForced(this);

            //check mark selected teams
            teamAdapter.setData(tags);

            llRoot.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                int heightDiff = llRoot.getRootView().getHeight() - llRoot.getHeight();
                ViewGroup.LayoutParams params = rvTeams.getLayoutParams();
                params.height = getWindowHeight() - heightDiff + 100;
            });
        });


        etSearchTeam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                runOnUiThread(() -> teamAdapter.getFilter().filter(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvTeamDone.setOnClickListener(v -> teamSheet.dismiss());

        teamSheet.setOnDismissListener(dialog -> {
            GlobalUtils.showLog(TAG, "team dismissed");
            //clear first then add
            fblLabel.removeAllViews();

            if (!CollectionUtils.isEmpty(tags)) {
                wrapFlexBoxContent();
            } else {
                addStaticHeightToFlexBox();
            }

            addTeamsToLayout();

            etSearchTeam.setText("");
            UiUtils.hideKeyboardForced(this);
        });
    }


    private void addTeamsToLayout() {
        //add selected teams
        for (String tagId : tags
        ) {
            Tags tag = TagRepo.getInstance().getTagById(tagId);

            @SuppressLint("InflateParams") View view1 = getLayoutInflater()
                    .inflate(R.layout.layout_tag, null);

            TextView teamLabel = view1.findViewById(R.id.tv_tag);
            teamLabel.setText(tag.getLabel());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(15, 8, 0, 0);
            view1.setLayoutParams(params);
            fblLabel.addView(view1);
        }
    }


    private void addStaticHeightToFlexBox() {
        ViewGroup.LayoutParams params = fblLabel.getLayoutParams();
        params.height = 60;
    }

    private void wrapFlexBoxContent() {
        ViewGroup.LayoutParams params = fblLabel.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

            int windowHeight = getWindowHeight();
            if (layoutParams != null) {
                layoutParams.height = windowHeight;
            }
            bottomSheet.setLayoutParams(layoutParams);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            Toast.makeText(this, "bottom sheet null", Toast.LENGTH_SHORT).show();
        }
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void setUpTeamRecyclerView(List<Tags> tagsList, RecyclerView rvTeams) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvTeams.setLayoutManager(mLayoutManager);

        teamAdapter = new SearchTeamAdapter(tagsList, this);
        rvTeams.setAdapter(teamAdapter);

        teamAdapter.setOnItemClickListener(new SearchTeamAdapter.OnItemClickListener() {
            @Override
            public void onItemAdd(Tags tag) {
                GlobalUtils.showLog(TAG, "item add");
                if (!tags.contains(tag.getTagId())) {
                    tags.add(tag.getTagId());
                }
                GlobalUtils.showLog(TAG, "tags: " + tags);
            }

            @Override
            public void onItemRemove(Tags tag) {
                GlobalUtils.showLog(TAG, "item remove");
                tags.remove(tag.getTagId());
                GlobalUtils.showLog(TAG, "tags: " + tags);

            }
        });
    }

    private void createPriorityBottomSheet() {
        prioritySheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.layout_bottomsheet_priority, null);

        prioritySheet.setContentView(view);
        LinearLayout llLowestPriority = view.findViewById(R.id.ll_priority_lowest);
        LinearLayout llLowPriority = view.findViewById(R.id.ll_priority_low);
        LinearLayout llMediumPriority = view.findViewById(R.id.ll_priority_medium);
        LinearLayout llHighestPriority = view.findViewById(R.id.ll_priority_highest);
        LinearLayout llHighPriority = view.findViewById(R.id.ll_priority_high);

        llLowPriority.setOnClickListener(v -> {
            etPriority.setText(R.string.low);
            priorityNum = 2;
            ivPriority.setImageDrawable(getResources().getDrawable(R.drawable.ic_low));
            prioritySheet.dismiss();
        });

        llLowestPriority.setOnClickListener(v -> {
            etPriority.setText(R.string.lowest);
            priorityNum = 1;
            ivPriority.setImageDrawable(getResources().getDrawable(R.drawable.ic_lowest));
            prioritySheet.dismiss();
        });

        llMediumPriority.setOnClickListener(v -> {
            etPriority.setText(R.string.medium);
            priorityNum = 3;
            ivPriority.setImageDrawable(getResources().getDrawable(R.drawable.ic_medium));
            prioritySheet.dismiss();
        });

        llHighPriority.setOnClickListener(v -> {
            etPriority.setText(R.string.high);
            priorityNum = 4;
            ivPriority.setImageDrawable(getResources().getDrawable(R.drawable.ic_high));
            prioritySheet.dismiss();
        });

        llHighestPriority.setOnClickListener(v -> {
            etPriority.setText(R.string.highest);
            priorityNum = 5;
            ivPriority.setImageDrawable(getResources().getDrawable(R.drawable.ic_highest));
            prioritySheet.dismiss();
        });
    }

    private void setDataFromThread(Intent i) {
        String summaryText = i.getStringExtra("summary_text");
        String customerName = i.getStringExtra("customer_name");
        String customerPic = i.getStringExtra("customer_pic");
        String employeeId = i.getStringExtra("employee_id");
        String teamId = i.getStringExtra("team");

        GlobalUtils.showLog(TAG, "customer Name:" + customerName);

        etSummary.setText(summaryText);
//        selectedCustomer = CustomerRepo.getInstance().getCustomerById(customerId);
//        etCustomerName.setText(selectedCustomer.getFullName());
//        showCustomerWithImage();
//        setEmailAndPhoneIfAvailable();

        etCustomerName.setText(customerName);

        if (customerPic != null && !customerPic.isEmpty()) {
            etCustomerName.setPadding(80, 0, 40, 0);
            civCustomer.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_profile_icon)
                    .error(R.drawable.ic_profile_icon);
            Glide.with(this).load(customerPic).apply(options).into(civCustomer);
        }

        if (employeeId != null) {
            AssignEmployee employee = AssignEmployeeRepo.getInstance()
                    .getAssignedEmployeeById(employeeId);
            selectedEmployeeId = employee.getEmployeeId();
            etAssignEmployee.setText(employee.getName());
            showEmployeeWithImage(employee);
        }

        if (teamId != null) {
            selectedTag = TagRepo.getInstance().getTagById(teamId);
            tags.add(selectedTag.getTagId());
            addTeamsToLayout();
//            addNewTagChip(tags);
        }
    }

    private void setEmailAndPhoneIfAvailable() {
        if (selectedCustomer.getEmail() != null && !selectedCustomer.getEmail().isEmpty()) {
            etEmail.setText(selectedCustomer.getEmail());
            etEmail.setFocusable(false);
            etEmail.setEnabled(false);

            etPhone.setFocusable(false);
            etPhone.setEnabled(false);
        }/* else {
            etEmail.setText("");
            etEmail.setFocusable(true);
            etEmail.setEnabled(true);
            etEmail.setFocusableInTouchMode(true);
        }*/

        if (selectedCustomer.getPhone() != null && !selectedCustomer.getPhone().isEmpty()) {
            etPhone.setText(selectedCustomer.getPhone());
            etPhone.setFocusable(false);
            etPhone.setEnabled(false);

            etEmail.setFocusable(false);
            etEmail.setEnabled(false);
        } /*else {
            etPhone.setText("");
            etPhone.setFocusable(true);
            etPhone.setEnabled(true);
        }*/
        etPhone.setFocusableInTouchMode(true);

        if (selectedCustomer.getPhone() == null && selectedCustomer.getEmail() == null) {
            etPhone.setText("");
            etPhone.setEnabled(true);
            etPhone.setFocusable(true);
            etPhone.setFocusableInTouchMode(true);

            etEmail.setText("");
            etEmail.setEnabled(true);
            etEmail.setFocusable(true);
            etEmail.setFocusableInTouchMode(true);
        }
    }

    private void setSelfDetails() {
        Employee employee = EmployeeRepo.getInstance().getEmployee();
        GlobalUtils.showLog(TAG, "employee check: " + employee);
        if (employee != null) {
            tvSuggestions.setVisibility(View.VISIBLE);
            llEmployeeAsSelf.setVisibility(View.VISIBLE);

            StringBuilder selfEmployeeText = new StringBuilder(employee.getName());
            selfEmployeeText.append(" (Me)");
            tvEmployeeAsSelf.setText(selfEmployeeText);

            String profilePicUrl = employee.getEmployeeImageUrl();
            if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_profile_icon)
                        .error(R.drawable.ic_profile_icon);

                Glide.with(this).load(profilePicUrl).apply(options).into(civEmployeeAsSelf);
            }
        } else {
            tvSuggestions.setVisibility(View.GONE);
            llEmployeeAsSelf.setVisibility(View.GONE);
        }
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }


    private void setUpRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvEmployeeAllUsers.setLayoutManager(mLayoutManager);

        employeeSearchAdapter = new EmployeeSearchAdapter(employeeList, this);
        rvEmployeeAllUsers.setAdapter(employeeSearchAdapter);

        if (employeeSearchAdapter != null) {
            employeeSearchAdapter.setOnItemClickListener((employee) -> {
                hideKeyBoard();

                selectedEmployeeId = employee.getEmployeeId();
                etAssignEmployee.setText(employee.getName());
                etAssignEmployee.setSelection(employee.getName().length());
                svSearchEmployee.setVisibility(View.GONE);

                showEmployeeWithImage(employee);
            });
        }
    }

    private void showEmployeeWithImage(AssignEmployee employee) {
        etAssignEmployee.setPadding(80, 0, 40, 0);
        civAssignEmployee.setVisibility(View.VISIBLE);

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_profile_icon)
                .error(R.drawable.ic_profile_icon);

        Glide.with(this).load(employee.getEmployeeImageUrl()).apply(options).into(civAssignEmployee);
    }

    @Override
    public void onCreateTicketSuccess() {

        if (createTicketFromThread) {
            Intent intent = new Intent();
            setResult(2, intent);
        }
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
        etSummary.setError("This field is required");
        scrollView.fullScroll(ScrollView.FOCUS_UP);

     /*   ilSummary.setErrorEnabled(true);
        ilSummary.setError("Invalid Summary");

        ilDesc.setErrorEnabled(false);
        ilCustomerName.setErrorEnabled(false);*/
       /* ilPhone.setErrorEnabled(false);
        ilEmail.setErrorEnabled(false);*/

    }

    @Override
    public void onInvalidDesc() {
        etDesc.requestFocus();
  /*      ilDesc.setErrorEnabled(true);
        ilDesc.setError("Invalid Description");

        ilSummary.setErrorEnabled(false);
        ilCustomerName.setErrorEnabled(false);*/
   /*     ilPhone.setErrorEnabled(false);
        ilEmail.setErrorEnabled(false);*/
    }

    @Override
    public void onInvalidCustomer() {
        etCustomerName.setError("This field is required");
       /* ilCustomerName.setErrorEnabled(true);
        ilCustomerName.setError("Invalid Customer");

        ilDesc.setErrorEnabled(false);
        ilSummary.setErrorEnabled(false);*/
/*        ilPhone.setErrorEnabled(false);
        ilEmail.setErrorEnabled(false);*/
    }

    @Override
    public void onInvalidPhone() {
        etPhone.requestFocus();
      /*  ilPhone.setErrorEnabled(true);
        ilPhone.setError("Invalid Phone");*/

     /*   ilDesc.setErrorEnabled(false);
        ilCustomerName.setErrorEnabled(false);
        ilSummary.setErrorEnabled(false);*/
//        ilEmail.setErrorEnabled(false);
    }

    @Override
    public void onInvalidEmail() {
        etEmail.requestFocus();
  /*      ilEmail.setErrorEnabled(true);
        ilEmail.setError("Invalid Email");

        ilPhone.setErrorEnabled(false);*/
  /*      ilDesc.setErrorEnabled(false);
        ilCustomerName.setErrorEnabled(false);
        ilSummary.setErrorEnabled(false);*/
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
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
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