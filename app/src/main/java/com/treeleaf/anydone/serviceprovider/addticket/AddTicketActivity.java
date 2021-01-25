package com.treeleaf.anydone.serviceprovider.addticket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.CustomerSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.DependentTicketSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.EmployeeSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.SearchLabelAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.SearchTeamAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.TicketCategorySearchAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.DependentTicket;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Label;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketCategory;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.AssignEmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.CustomerRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.DependentTicketRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.EmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.LabelRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ServiceProviderRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TagRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketCategoryRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.ArrayList;
import java.util.Collections;
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
    @BindView(R.id.iv_priority)
    ImageView ivPriority;
    @BindView(R.id.civ_customer)
    CircleImageView civCustomer;
    @BindView(R.id.civ_assign_employee)
    CircleImageView civAssignEmployee;
    @BindView(R.id.fbl_team)
    FlexboxLayout fblTeam;
    @BindView(R.id.fbl_label)
    FlexboxLayout fblLabel;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.et_ticket_type)
    AutoCompleteTextView etTicketType;
    @BindView(R.id.et_estimated_time)
    AppCompatEditText etEstimatedTime;
    @BindView(R.id.et_depends_on)
    AppCompatEditText etDependsOn;
    @BindView(R.id.tv_assign_to_me)
    TextView tvAssignToMe;
    @BindView(R.id.ll_emp_suggestion)
    LinearLayout llEmpSuggestion;
    @BindView(R.id.civ_suggested_emp)
    CircleImageView civSuggestedEmp;
    @BindView(R.id.tv_emp_suggestion)
    TextView tvEmpSuggestion;
    @BindView(R.id.ll_team_suggestion)
    LinearLayout llTeamSuggestion;
    @BindView(R.id.tv_team_suggestion)
    TextView tvTeamSuggestion;
    @BindView(R.id.ll_label_suggestion)
    LinearLayout llLabelSuggestion;
    @BindView(R.id.tv_label_suggestion)
    TextView tvLabelSuggestion;
    @BindView(R.id.ll_priority_suggestion)
    LinearLayout llPrioritySuggestion;
    @BindView(R.id.tv_priority_suggestion)
    TextView tvPrioritySuggestion;
    @BindView(R.id.ll_est_time_suggestion)
    LinearLayout llEstTimeSuggestion;
    @BindView(R.id.tv_est_time_suggestion)
    TextView tvEstTimeSuggestion;
    @BindView(R.id.iv_priority_suggestion)
    ImageView ivPrioritySuggestion;
    @BindView(R.id.tv_assign_to_me_customer)
    TextView tvAssignToMeCustomer;

    private List<AssignEmployee> employeeList = new ArrayList<>();
    private List<Customer> customerList = new ArrayList<>();
    private List<Tags> tagsList = new ArrayList<>();
    private List<Label> labelList = new ArrayList<>();
    private List<DependentTicket> dependentTicketList = new ArrayList<>();

    private EmployeeSearchAdapter employeeSearchAdapter;
    private Customer selectedCustomer;
    private Tags selectedTag;
    private Employee selfEmployee;
    private ServiceProvider serviceProvider;
    private String selectedEmployeeId;
    private boolean createTicketFromThread;
    private BottomSheetDialog prioritySheet;
    private BottomSheetDialog teamSheet;
    private BottomSheetDialog labelSheet;
    private BottomSheetDialog ticketDependencySheet;
    private int priorityNum = 3;
    private EditText etSearchTeam;
    private EditText etSearchLabel;
    private AppCompatEditText etSearchTicket;
    private SearchLabelAdapter labelAdapter;
    private SearchTeamAdapter teamAdapter;
    private DependentTicketSearchAdapter dependentTicketAdapter;
    List<String> tags = new ArrayList<>();
    List<Label> labels = new ArrayList<>();
    private RecyclerView rvTeams;
    private RecyclerView rvLabels;
    private RecyclerView rvTicket;
    private int lastDescCursorPosition = 0;
    private String description = "";
    private List<TicketCategory> ticketTypeList = new ArrayList<>();
    private String ticketCategoryId;
    private TicketProto.TicketSource ticketSource = TicketProto.TicketSource.MANUAL_TICKET_SOURCE;
    private boolean customerAsSelf;
    private String threadId = "";
    private BottomSheetDialog estimatedTimeBottomSheet;
    private BottomSheetDialog employeeBottomSheet;
    private BottomSheetDialog customerBottomSheet;
    private RecyclerView rvTicketType;
    private RecyclerView rvCustomers;
    private TicketCategorySearchAdapter ticketCategorySearchAdapter;
    private CustomerSearchAdapter customerSearchAdapter;
    private LinearLayout llEmployeeAsSelf;
    private Label suggestedLabel;
    private Tags suggestedTeam;
    private AssignEmployee suggestedEmployee;
    private Priority suggestedPriority;
    private String suggestedEstTime;
    private ImageView ivTick, ivTickCustomer;
    private DependentTicket dependentTicket;


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
        GlobalUtils.showLog(TAG, "tagList: " + tagsList);

        ticketTypeList = TicketCategoryRepo.getInstance().getAllTicketCategories();
        customerList = CustomerRepo.getInstance().getAllCustomers();
        employeeList = AssignEmployeeRepo.getInstance().getAllAssignEmployees();
        tagsList = TagRepo.getInstance().getAllTags();
        labelList = LabelRepo.getInstance().getAllLabels();
        dependentTicketList = DependentTicketRepo.getInstance().getAllDependentTickets();

        if (dependentTicketList.isEmpty()) {
            presenter.getDependencyListTickets();
        }

        createCustomerBottomSheet();
        createEmployeeBottomSheet();
        createTeamBottomSheet();
        createLabelBottomSheet();
        createPriorityBottomSheet();
        createEstimatedTimeBottomSheet();
        createTicketDependencyBottomSheet();

        //set assign to me only if employee login
        Account account = AccountRepo.getInstance().getAccount();
        if (account.getAccountType().equalsIgnoreCase("SERVICE_PROVIDER")) {
            tvAssignToMe.setVisibility(View.GONE);
            tvAssignToMeCustomer.setVisibility(View.GONE);
        } else {
            tvAssignToMe.setVisibility(View.VISIBLE);
            tvAssignToMeCustomer.setVisibility(View.VISIBLE);
        }

        tvAssignToMe.setOnClickListener(v -> {
            setAssignedEmployeeAsSelf();
            tvAssignToMe.setVisibility(View.GONE);
        });

        tvAssignToMeCustomer.setOnClickListener(v -> {
            setCustomerAsSelf();
            tvAssignToMeCustomer.setVisibility(View.GONE);
        });

//        setUpTeamRecyclerView();

        Intent intent = getIntent();
        createTicketFromThread = intent.getBooleanExtra("create_ticket_from_thread",
                false);

        if (createTicketFromThread) {
            setDataFromThread(intent);
            ticketSource = TicketProto.TicketSource.CONVERSATION_TICKET_SOURCE;
        }

/*        etDesc.addTextChangedListener(new TextWatcher() {
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

                if (etDesc.getLineCount() > 5) {
                    etDesc.setText(description);
                    etDesc.setSelection(lastDescCursorPosition);
                } else
                    description = Objects.requireNonNull(etDesc.getText()).toString();

                etDesc.addTextChangedListener(this);
            }
        });*/

        fblTeam.setOnClickListener(v -> {
            teamSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            clearFocusFromInputFields();
            if (!tagsList.isEmpty()) {
                teamSheet.show();
            } else {
                Toast.makeText(this, "Teams not available", Toast.LENGTH_SHORT).show();
            }
        });

        fblLabel.setOnClickListener(v -> {
            labelSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            clearFocusFromInputFields();
            if (!labelList.isEmpty()) {
                labelSheet.show();
            } else {
                Toast.makeText(this, "Labels not available", Toast.LENGTH_SHORT).show();
            }
        });

        etTicketType.setOnClickListener(v -> {
            clearFocusFromInputFields();
            if (!ticketTypeList.isEmpty()) {
                etTicketType.showDropDown();
            } else {
                Toast.makeText(this, "Ticket Types not available", Toast.LENGTH_SHORT).show();
            }
        });

        etTicketType.setOnItemClickListener((parent, view, position, id) -> {
            UiUtils.hideKeyboard(this);
            etTicketType.setError(null);
            ticketCategoryId = ticketTypeList.get(position).getCategoryId();
        });

        etCustomerName.setOnClickListener(v -> {
            customerBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            clearFocusFromInputFields();
            customerBottomSheet.show();
        });

        etAssignEmployee.setOnClickListener(v -> {
            employeeBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            clearFocusFromInputFields();
            if (!employeeList.isEmpty()) {
                employeeBottomSheet.show();
            } else {
                Toast.makeText(this, "Employees not available", Toast.LENGTH_SHORT).show();
            }
        });

        etPriority.setOnClickListener(v -> {
            clearFocusFromInputFields();
            prioritySheet.show();
        });

        etEstimatedTime.setOnClickListener(v -> {
            estimatedTimeBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            clearFocusFromInputFields();
            estimatedTimeBottomSheet.show();
        });

        etDependsOn.setOnClickListener(v -> {
            dependentTicketList = DependentTicketRepo.getInstance().getAllDependentTickets();
            createTicketDependencyBottomSheet();
            clearFocusFromInputFields();
            ticketDependencySheet.show();
        });


        btnCreateTicket.setOnClickListener(v -> {
            GlobalUtils.showLog(TAG, "emp id checK: " + selectedEmployeeId);
            if (selectedCustomer != null) {
                presenter.createTicket(ticketCategoryId, UiUtils.getString(etSummary),
                        UiUtils.getString(etDesc), selectedCustomer.getCustomerId(),
                        UiUtils.getString(etEmail), UiUtils.getString(etPhone),
                        selectedCustomer.getFullName(), selectedCustomer.getProfilePic(), tags,
                        labels, UiUtils.getString(etEstimatedTime),
                        selectedEmployeeId, priorityNum, ticketSource, customerAsSelf,
                        threadId, dependentTicket);
            } else {
                presenter.createTicket(ticketCategoryId, UiUtils.getString(etSummary),
                        UiUtils.getString(etDesc), null, UiUtils.getString(etEmail),
                        UiUtils.getString(etPhone), UiUtils.getString(etCustomerName),
                        "", tags,
                        labels, UiUtils.getString(etEstimatedTime),
                        selectedEmployeeId, priorityNum, ticketSource, customerAsSelf, threadId,
                        dependentTicket);
            }
        });

        etSummary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.getSummarySuggestions(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setClickListenersOnSuggestions();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setClickListenersOnSuggestions() {
        llEmpSuggestion.setOnClickListener(v -> {
            showEmployeeWithImage(suggestedEmployee);
            etAssignEmployee.setText(suggestedEmployee.getName());

            selectedEmployeeId = suggestedEmployee.getEmployeeId();
            employeeSearchAdapter.setChecked(suggestedEmployee.getEmployeeId());
            llEmpSuggestion.setVisibility(View.GONE);
        });

        llLabelSuggestion.setOnClickListener(v -> {
            boolean labelExists = checkIfLabelExists(suggestedLabel);

            if (!labelExists) {
                labels.add(suggestedLabel);
                addLabelsToLayout();
            }

            llLabelSuggestion.setVisibility(View.GONE);
        });

        llTeamSuggestion.setOnClickListener(v -> {
            if (!tags.contains(suggestedTeam.getTagId())) {
                tags.add(suggestedTeam.getTagId());
                addTeamsToLayout();
            }

            llTeamSuggestion.setVisibility(View.GONE);
        });

        llPrioritySuggestion.setOnClickListener(v -> {
            etPriority.setText(tvPrioritySuggestion.getText().toString());
            priorityNum = suggestedPriority.getIcon();

            switch (suggestedPriority.getIcon()) {
                case 1:
                    ivPriority.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_lowest));
                    break;

                case 2:
                    ivPriority.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_low));
                    break;

                case 3:
                    ivPriority.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_medium));
                    break;

                case 4:
                    ivPriority.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_high));
                    break;

                case 5:
                    ivPriority.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_highest));
                    break;

            }

            llPrioritySuggestion.setVisibility(View.GONE);
        });

        llEstTimeSuggestion.setOnClickListener(v -> {
            etEstimatedTime.setText(suggestedEstTime);
            llEstTimeSuggestion.setVisibility(View.GONE);
        });
    }

    private boolean checkIfLabelExists(Label label) {
        for (Label existing : labels
        ) {
            if (existing.getLabelId().equalsIgnoreCase(label.getLabelId())) {
                return true;
            }
        }
        return false;
    }

    private Label getExistingLabel(Label label) {
        for (Label existing : labels
        ) {
            if (existing.getLabelId().equalsIgnoreCase(label.getLabelId())) {
                return existing;
            }
        }
        return null;
    }

    private void createTicketDependencyBottomSheet() {
        ticketDependencySheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.layout_bottom_sheet_ticket_dependency, null);

        ticketDependencySheet.setContentView(view);
        ticketDependencySheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        etSearchTicket = view.findViewById(R.id.et_search_ticket);
        rvTicket = view.findViewById(R.id.rv_ticket);

        setUpDependentTicketRecyclerView(dependentTicketList, rvTicket);

        ticketDependencySheet.setOnShowListener(dialog -> {
            etSearchTicket.requestFocus();
            UiUtils.showKeyboardForced(this);
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            setupSheetHeight(d, BottomSheetBehavior.STATE_EXPANDED);
        });


        etSearchTicket.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    dependentTicketAdapter.getFilter().filter(s);
                } else dependentTicketAdapter.setData(dependentTicketList);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ticketDependencySheet.setOnDismissListener(dialog -> {
            etSearchTicket.clearFocus();
            etSearchTicket.getText().clear();
            UiUtils.hideKeyboardForced(this);
        });
    }


    private void createEstimatedTimeBottomSheet() {
        estimatedTimeBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View llBottomSheet = getLayoutInflater()
                .inflate(R.layout.bottomsheet_estimated_time, null);

        estimatedTimeBottomSheet.setContentView(llBottomSheet);
        estimatedTimeBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        EditText etSetEstimatedTime = llBottomSheet.findViewById(R.id.et_estimated_time);
        TextView tvDone = llBottomSheet.findViewById(R.id.tv_done);
        TextView tv1hr = llBottomSheet.findViewById(R.id.tv_1_hr);
        TextView tv4Hour = llBottomSheet.findViewById(R.id.tv_4_hour);
        TextView tv1Day = llBottomSheet.findViewById(R.id.tv_1_day);
        TextView tv1Week = llBottomSheet.findViewById(R.id.tv_1_week);

        estimatedTimeBottomSheet.setOnShowListener(dialog -> {
            etSetEstimatedTime.setText(etEstimatedTime.getText().toString().trim());
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
       /*     if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);*/
            setupSheetHeight(d, BottomSheetBehavior.STATE_HALF_EXPANDED);
        });

        tv1hr.setOnClickListener(v1 -> {
            etEstimatedTime.setText(tv1hr.getText().toString().trim());
            etEstimatedTime.setSelection(etEstimatedTime.length());
            estimatedTimeBottomSheet.dismiss();
            hideKeyBoard();
        });

        tv4Hour.setOnClickListener(v1 -> {
            etEstimatedTime.setText(tv4Hour.getText().toString().trim());
            etEstimatedTime.setSelection(etEstimatedTime.length());
            estimatedTimeBottomSheet.dismiss();
            hideKeyBoard();
        });

        tv1Day.setOnClickListener(v1 -> {
            etEstimatedTime.setText(tv1Day.getText().toString().trim());
            etEstimatedTime.setSelection(etEstimatedTime.length());
            estimatedTimeBottomSheet.dismiss();
            hideKeyBoard();
        });

        tv1Week.setOnClickListener(v1 -> {
            etEstimatedTime.setText(tv1Week.getText().toString().trim());
            etEstimatedTime.setSelection(etEstimatedTime.length());
            estimatedTimeBottomSheet.dismiss();
            hideKeyBoard();
        });

        etSetEstimatedTime.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setupSheetHeight(estimatedTimeBottomSheet, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        estimatedTimeBottomSheet.setOnDismissListener(dialog -> {
            setSheetHalfExpanded(dialog);
            etSetEstimatedTime.clearFocus();
            etSetEstimatedTime.getText().clear();
        });

        tvDone.setOnClickListener(v -> {
            UiUtils.hideKeyboardForced(this);
            scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
            etEstimatedTime.setText(etSetEstimatedTime.getText().toString().trim());
            estimatedTimeBottomSheet.dismiss();
            clearFocusFromInputFields();
        });
    }

    private void setSheetHalfExpanded(DialogInterface dialog) {
        BottomSheetDialog d = (BottomSheetDialog) dialog;
        FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null)
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    private void createEmployeeBottomSheet() {
        employeeBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View llBottomSheet = getLayoutInflater()
                .inflate(R.layout.bottomsheet_select_employee, null);

        employeeBottomSheet.setContentView(llBottomSheet);
        employeeBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        employeeBottomSheet.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
/*            if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);*/
            setupSheetHeight(d, BottomSheetBehavior.STATE_HALF_EXPANDED);
        });

        EditText searchEmployee = llBottomSheet.findViewById(R.id.et_search_employee);
        llEmployeeAsSelf = llBottomSheet.findViewById(R.id.ll_self);
        CircleImageView civEmployeeAsSelf = llBottomSheet.findViewById(R.id.civ_image_self);
        TextView tvEmployeeAsSelf = llBottomSheet.findViewById(R.id.tv_name_self);
        TextView tvEmployeeAllUsers = llBottomSheet.findViewById(R.id.tv_all_users);
        TextView tvSuggestions = llBottomSheet.findViewById(R.id.tv_suggestions);
        RecyclerView rvEmployee = llBottomSheet.findViewById(R.id.rv_all_users);
        ivTick = llBottomSheet.findViewById(R.id.iv_tick);

        setSelfDetails(llEmployeeAsSelf, tvEmployeeAsSelf, civEmployeeAsSelf, tvSuggestions);

        llEmployeeAsSelf.setOnClickListener(v -> setAssignedEmployeeAsSelf());

        searchEmployee.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setupSheetHeight(employeeBottomSheet, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        employeeBottomSheet.setOnDismissListener(dialog -> {
            setSheetHalfExpanded(dialog);
            searchEmployee.clearFocus();
            searchEmployee.getText().clear();
            if (rvEmployee.getChildCount() > 0) rvEmployee.scrollToPosition(0);
        });

        setUpEmployeeRecyclerView(rvEmployee, ivTick);
        searchEmployee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                employeeSearchAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        employeeBottomSheet.setOnShowListener(dialog -> {
            Employee self = EmployeeRepo.getInstance().getEmployee();
            if (selectedEmployeeId != null && selectedEmployeeId
                    .equalsIgnoreCase(self.getEmployeeId())) {
                ivTick.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setAssignedEmployeeAsSelf() {
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
            ivTick.setVisibility(View.VISIBLE);
            llEmployeeAsSelf.setClickable(false);
            employeeSearchAdapter.removeCheckMark();
        }

        etAssignEmployee.setText(selfEmployee.getName());
        employeeBottomSheet.dismiss();
        hideKeyBoard();
    }

    private void clearFocusFromInputFields() {
        etSummary.clearFocus();
        etDesc.clearFocus();
        etPhone.clearFocus();
    }

    private void createCustomerBottomSheet() {
        customerBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View llBottomSheet = getLayoutInflater()
                .inflate(R.layout.bottomsheet_select_customer, null);

        customerBottomSheet.setContentView(llBottomSheet);
        customerBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        customerBottomSheet.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
    /*        if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);*/
            setupSheetHeight(d, BottomSheetBehavior.STATE_HALF_EXPANDED);
        });

        EditText searchCustomer = llBottomSheet.findViewById(R.id.et_search_customer);
        RelativeLayout rlCustomerSelfHolder = llBottomSheet.findViewById(R.id.rl_customer_self_holder);
        TextView tvSuggestions = llBottomSheet.findViewById(R.id.tv_suggestions);
        TextView tvCustomerSelf = llBottomSheet.findViewById(R.id.tv_customer_self);
        CircleImageView civSelf = llBottomSheet.findViewById(R.id.civ_self);
        RelativeLayout rlNewCustomer = llBottomSheet.findViewById(R.id.rl_new_customer);
        TextView tvNewCustomer = llBottomSheet.findViewById(R.id.tv_new_customer);
        ivTickCustomer = llBottomSheet.findViewById(R.id.iv_tick_customer);
        rvCustomers = llBottomSheet.findViewById(R.id.rv_customer);

        Employee employee = EmployeeRepo.getInstance().getEmployee();
        if (employee == null) {
            tvSuggestions.setVisibility(View.GONE);
            rlCustomerSelfHolder.setVisibility(View.GONE);
        }
        setCustomerSuggestions(rlCustomerSelfHolder, tvCustomerSelf, civSelf);
        rlCustomerSelfHolder.setOnClickListener(v ->
                setCustomerAsSelf());

        searchCustomer.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (hasFocus) {
                setupSheetHeight(customerBottomSheet, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        customerBottomSheet.setOnDismissListener(dialog ->
        {
            setSheetHalfExpanded(dialog);
            searchCustomer.clearFocus();
            searchCustomer.getText().clear();
            if (rvCustomers.getChildCount() > 0) rvCustomers.scrollToPosition(0);
        });

        setUpCustomerRecyclerView(rlNewCustomer, tvNewCustomer, searchCustomer, ivTickCustomer);

        searchCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customerSearchAdapter.getFilter().filter(s);
                if (s.length() == 0) {
                    rlNewCustomer.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setCustomerAsSelf() {
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
        ivTickCustomer.setVisibility(View.VISIBLE);
//            llEmployeeAsSelf.setClickable(false);
        customerSearchAdapter.removeCheckMark();

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

        hideKeyBoard();
        customerAsSelf = true;
        customerBottomSheet.dismiss();
    }

    private void setCustomerSuggestions(RelativeLayout rlCustomerSelfHolder,
                                        TextView tvCustomerSelf,
                                        CircleImageView civSelf) {
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
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpCustomerRecyclerView(RelativeLayout rlNewCustomer, TextView tvNewCustomer,
                                           EditText etSearchCustomer, ImageView ivTick) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvCustomers.setLayoutManager(mLayoutManager);

        customerSearchAdapter = new CustomerSearchAdapter
                (this, customerList);
        rvCustomers.setAdapter(customerSearchAdapter);

        rvCustomers.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return false;
        });

        customerSearchAdapter.setOnFilterListEmptyListener(new CustomerSearchAdapter.OnFilterListEmptyListener() {
            @Override
            public void showNewCustomer() {
                tvNewCustomer.setText(etSearchCustomer.getText().toString().trim());
                if (etSearchCustomer.getText().toString().length() > 0)
                    rlNewCustomer.setVisibility(View.VISIBLE);

                rlNewCustomer.setOnClickListener(v -> {
                    etCustomerName.setText(etSearchCustomer.getText().toString().trim());
                    rlNewCustomer.setVisibility(View.GONE);
                    etSearchCustomer.setText("");
                    customerBottomSheet.dismiss();
                    UiUtils.hideKeyboardForced(AddTicketActivity.this);

                    etEmail.setText("");
                    etEmail.setFocusable(true);
                    etEmail.setEnabled(true);
                    etEmail.setFocusableInTouchMode(true);

                    etPhone.setText("");
                    etPhone.setFocusable(true);
                    etPhone.setEnabled(true);
                    etPhone.setFocusableInTouchMode(true);
                });
            }

            @Override
            public void hideNewCustomer() {
                rlNewCustomer.setVisibility(View.GONE);
            }
        });

        customerSearchAdapter.setOnItemClickListener((customer) -> {
            GlobalUtils.showLog(TAG, "selected customer id: " + customer.getCustomerId());
            ivTickCustomer.setVisibility(View.GONE);
            llEmployeeAsSelf.setClickable(true);
            customerSearchAdapter.setChecked(customer.getCustomerId());
            UiUtils.hideKeyboardForced(this);
            etCustomerName.setError(null);
            selectedCustomer = customer;
            setEmailAndPhoneIfAvailable();
            showCustomerWithImage();
            customerAsSelf = false;

            etCustomerName.setText(customer.getFullName());
            customerBottomSheet.dismiss();
        });

    }


    private void createLabelBottomSheet() {
        labelSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.layout_bottom_sheet_label, null);

        labelSheet.setContentView(view);
        labelSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        TextView tvLabelDone = view.findViewById(R.id.tv_done);
        etSearchLabel = view.findViewById(R.id.et_search_label);
        rvLabels = view.findViewById(R.id.rv_labels);
        RelativeLayout rlNewLabel = view.findViewById(R.id.rl_new_label);
        TextView tvNewLabel = view.findViewById(R.id.tv_new_label);

        setUpLabelRecyclerView(labelList, rvLabels, rlNewLabel, tvNewLabel);

        labelSheet.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById
                    (com.google.android.material.R.id.design_bottom_sheet);
 /*           if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);*/
            setupSheetHeight(d, BottomSheetBehavior.STATE_HALF_EXPANDED);

            //check mark selected teams
            if (!labels.isEmpty())
                labelAdapter.setData(labels);

        /*    llRoot.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                int heightDiff = llRoot.getRootView().getHeight() - llRoot.getHeight();
                ViewGroup.LayoutParams params = rvLabels.getLayoutParams();
                params.height = getWindowHeight() - heightDiff + 100;
            });*/
        });

        etSearchLabel.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setupSheetHeight(labelSheet, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        etSearchLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                runOnUiThread(() -> labelAdapter.getFilter().filter(s));
                if (s.length() == 0) {
                    rlNewLabel.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvLabelDone.setOnClickListener(v -> {
            labelSheet.dismiss();
            hideKeyBoard();
        });

        labelSheet.setOnDismissListener(dialog -> {
            GlobalUtils.showLog(TAG, "label dismissed");
            setSheetHalfExpanded(dialog);
            //clear first then add
            fblLabel.removeAllViews();

            if (!CollectionUtils.isEmpty(labels)) {
                wrapLabelFlexBoxContent();
            } else {
                addStaticHeightToLabelFlexBox();
            }

            addLabelsToLayout();

            etSearchLabel.clearFocus();
            etSearchLabel.getText().clear();
            UiUtils.hideKeyboardForced(this);
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpLabelRecyclerView(List<Label> labelList, RecyclerView rvLabels,
                                        RelativeLayout rlNewLabel, TextView tvNewLabel) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvLabels.setLayoutManager(mLayoutManager);

        labelAdapter = new SearchLabelAdapter(labelList, this);
        rvLabels.setAdapter(labelAdapter);

        rvLabels.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return false;
        });

        labelAdapter.setOnFilterListEmptyListener(() -> {
            tvNewLabel.setText(etSearchLabel.getText().toString().trim());
            if (etSearchLabel.getText().toString().length() > 0)
                rlNewLabel.setVisibility(View.VISIBLE);

            rlNewLabel.setOnClickListener(v -> {
                addNewLabel(labelList,
                        tvNewLabel.getText().toString().trim());
                rlNewLabel.setVisibility(View.GONE);
                etSearchLabel.setText("");
            });
        });

        labelAdapter.setOnItemClickListener(new SearchLabelAdapter.OnItemClickListener() {
            @Override
            public void onItemAdd(Label label) {
                GlobalUtils.showLog(TAG, "item add");
                if (!labels.contains(label)) {
                    labels.add(label);
                }
                GlobalUtils.showLog(TAG, " added labels: " + label);
            }

            @Override
            public void onItemRemove(Label label) {
                GlobalUtils.showLog(TAG, "item remove");
                Label labelExists = getExistingLabel(label);
                if (labelExists != null)
                    labels.remove(labelExists);
                GlobalUtils.showLog(TAG, "after removed labels: " + labels.size());
            }
        });
    }

    private void addNewLabel(List<Label> labelList, String labelName) {
        Label newLabel = new Label();
        newLabel.setName(labelName);
        newLabel.setLabelId("");

        labelList.add(newLabel);
        labelAdapter.setNewData(labelList);
        labels.add(newLabel);
    }

    private void showCustomerWithImage() {
        etCustomerName.setPadding(GlobalUtils.convertDpToPixel(getContext(), 30),
                0, GlobalUtils.convertDpToPixel(getContext(), 15), 0);
        civCustomer.setVisibility(View.VISIBLE);

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                .error(R.drawable.ic_empty_profile_holder_icon);

        Glide.with(this)
                .load(selectedCustomer.getProfilePic())
                .apply(options)
                .into(civCustomer);
    }

    private void createTeamBottomSheet() {
        teamSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.layout_bottom_sheet_team, null);

        teamSheet.setContentView(view);
        teamSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        TextView tvTeamDone = view.findViewById(R.id.tv_done);
        etSearchTeam = view.findViewById(R.id.et_search_employee);
        rvTeams = view.findViewById(R.id.rv_teams);

        setUpTeamRecyclerView(tagsList, rvTeams);

        teamSheet.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
          /*  if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);*/
            setupSheetHeight(d, BottomSheetBehavior.STATE_HALF_EXPANDED);

            //check mark selected teams
            teamAdapter.setData(tags);

        /*    llRoot.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                int heightDiff = llRoot.getRootView().getHeight() - llRoot.getHeight();
                ViewGroup.LayoutParams params = rvTeams.getLayoutParams();
                params.height = getWindowHeight() - heightDiff + 100;
            });*/
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

        etSearchTeam.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setupSheetHeight(teamSheet, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        tvTeamDone.setOnClickListener(v -> teamSheet.dismiss());

        teamSheet.setOnDismissListener(dialog -> {
            GlobalUtils.showLog(TAG, "team dismissed");
            setSheetHalfExpanded(dialog);
            //clear first then add
            fblTeam.removeAllViews();

            if (!CollectionUtils.isEmpty(tags)) {
                wrapFlexBoxContent();
            } else {
                addStaticHeightToFlexBox();
            }

            addTeamsToLayout();

            etSearchTeam.clearFocus();
            etSearchTeam.getText().clear();
            UiUtils.hideKeyboardForced(this);
        });
    }


    private void addTeamsToLayout() {
        fblTeam.removeAllViews();
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
            fblTeam.addView(view1);
        }
    }

    private void addLabelsToLayout() {
        fblLabel.removeAllViews();
        //add selected teams
        for (Label ticketLabel : labels
        ) {
            @SuppressLint("InflateParams") View view1 = getLayoutInflater()
                    .inflate(R.layout.layout_tag, null);

            TextView tvLabel = view1.findViewById(R.id.tv_tag);
            tvLabel.setText(ticketLabel.getName());
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
        ViewGroup.LayoutParams params = fblTeam.getLayoutParams();
        params.height = 60;
    }

    private void addStaticHeightToLabelFlexBox() {
        ViewGroup.LayoutParams params = fblLabel.getLayoutParams();
        params.height = 60;
    }

    private void wrapFlexBoxContent() {
        ViewGroup.LayoutParams params = fblTeam.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    private void wrapLabelFlexBoxContent() {
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

    @SuppressLint("ClickableViewAccessibility")
    private void setUpTeamRecyclerView(List<Tags> tagsList, RecyclerView rvTeams) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvTeams.setLayoutManager(mLayoutManager);

        teamAdapter = new SearchTeamAdapter(tagsList, this);
        rvTeams.setAdapter(teamAdapter);

        rvTeams.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return false;
        });

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

    @SuppressLint("ClickableViewAccessibility")
    private void setUpDependentTicketRecyclerView(List<DependentTicket> ticketList,
                                                  RecyclerView rvDependentTickets) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvDependentTickets.setLayoutManager(mLayoutManager);

        dependentTicketAdapter = new DependentTicketSearchAdapter(this, ticketList);
        rvDependentTickets.setAdapter(dependentTicketAdapter);

        rvDependentTickets.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return false;
        });

        dependentTicketAdapter.setOnItemClickListener(ticket -> {
            dependentTicket = ticket;
            String dependentTicket = "#" +
                    ticket.getIndex() +
                    " " +
                    ticket.getSummary();
            etDependsOn.setText(dependentTicket);
            ticketDependencySheet.dismiss();
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
        threadId = i.getStringExtra("thread_id");
        String customerPic = i.getStringExtra("customer_pic");
        String customerId = i.getStringExtra("customer_id");
        String employeeId = i.getStringExtra("employee_id");
        String teamId = i.getStringExtra("team");

        GlobalUtils.showLog(TAG, "customer Name:" + customerName);
        GlobalUtils.showLog(TAG, "Team id: " + teamId);
        GlobalUtils.showLog(TAG, "summary check: " + summaryText);

        etSummary.setText(summaryText);
        selectedCustomer = CustomerRepo.getInstance().getCustomerById(customerId);
        if (selectedCustomer != null) {
            etCustomerName.setText(selectedCustomer.getFullName());
            showCustomerWithImage();
            setEmailAndPhoneIfAvailable();
        }


    /*    if (customerPic != null && !customerPic.isEmpty()) {
            etCustomerName.setPadding(80, 0, 40, 0);
            civCustomer.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);
            Glide.with(this).load(customerPic).apply(options).into(civCustomer);
        }*/

        if (employeeId != null) {
            AssignEmployee employee = AssignEmployeeRepo.getInstance()
                    .getAssignedEmployeeById(employeeId);
            selectedEmployeeId = employee.getEmployeeId();
            etAssignEmployee.setText(employee.getName());
            showEmployeeWithImage(employee);
        }

        if (teamId != null) {
            selectedTag = TagRepo.getInstance().getTagById(teamId);
            if (selectedTag != null) {
                tags.add(selectedTag.getTagId());
                addTeamsToLayout();
            }
//            addNewTagChip(tags);
        }

        GlobalUtils.showLog(TAG, "tag obj: " + tags);
    }

    private void setEmailAndPhoneIfAvailable() {
        if (selectedCustomer.getEmail() != null && !selectedCustomer.getEmail().isEmpty()) {
            etEmail.setText(selectedCustomer.getEmail());
            etEmail.setFocusable(false);
            etEmail.setEnabled(false);

            etPhone.setFocusable(false);
            etPhone.setEnabled(false);
        } else {
            etEmail.setText("");
      /*      etEmail.setFocusable(true);
            etEmail.setEnabled(true);
            etEmail.setFocusableInTouchMode(true);*/
        }

        if (selectedCustomer.getPhone() != null && !selectedCustomer.getPhone().isEmpty()) {
            etPhone.setText(selectedCustomer.getPhone());
            etPhone.setFocusable(false);
            etPhone.setEnabled(false);

            etEmail.setFocusable(false);
            etEmail.setEnabled(false);
        } else {
            etPhone.setText("");
        /*    etPhone.setFocusable(true);
            etPhone.setEnabled(true);
            etPhone.setFocusableInTouchMode(true);*/
        }


        if (selectedCustomer.getPhone().isEmpty()
                && selectedCustomer.getEmail().isEmpty()) {
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

    private void setSelfDetails(LinearLayout llEmployeeAsSelf, TextView tvEmployeeAsSelf,
                                CircleImageView civEmployeeAsSelf, TextView tvSuggestions) {
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
    protected void onResume() {
        super.onResume();

        presenter.findCustomers();
        presenter.findEmployees();
        presenter.getTicketTypes();
        presenter.findTags();
        presenter.getLabels();
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setUpEmployeeRecyclerView(RecyclerView rvEmployeeAllUsers, ImageView ivTick
    ) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvEmployeeAllUsers.setLayoutManager(mLayoutManager);

        GlobalUtils.showLog(TAG, "employee list: " + employeeList);
        employeeSearchAdapter = new EmployeeSearchAdapter(employeeList, this, false);
        rvEmployeeAllUsers.setAdapter(employeeSearchAdapter);

        rvEmployeeAllUsers.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return false;
        });

        if (employeeSearchAdapter != null) {
            employeeSearchAdapter.setOnItemClickListener((employee) -> {
                ivTick.setVisibility(View.GONE);
                llEmployeeAsSelf.setClickable(true);

                employeeSearchAdapter.setChecked(employee.getEmployeeId());
                UiUtils.hideKeyboardForced(this);
                selectedEmployeeId = employee.getEmployeeId();
                etAssignEmployee.setText(employee.getName());
                etAssignEmployee.setSelection(employee.getName().length());

                showEmployeeWithImage(employee);
                employeeBottomSheet.dismiss();
            });
        }
    }

    private void showEmployeeWithImage(AssignEmployee employee) {
        etAssignEmployee.setPadding(GlobalUtils.convertDpToPixel(getContext(), 30),
                0, GlobalUtils.convertDpToPixel(getContext(), 15), 0);
        civAssignEmployee.setVisibility(View.VISIBLE);

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                .error(R.drawable.ic_empty_profile_holder_icon);

        Glide.with(this).load(employee.getEmployeeImageUrl())
                .apply(options).into(civAssignEmployee);
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
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();

    }

    @Override
    public void onInvalidSummary() {
        etSummary.requestFocus();
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.INFO,
                "Summary is required",
                Banner.TOP,
                2000).show();

    }

    @Override
    public void onInvalidDesc() {
        etDesc.requestFocus();
    }

    @Override
    public void onInvalidCustomer() {
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.INFO, "Customer is required", Banner.TOP, 2000).show();
    }

    @Override
    public void onInvalidTicketType() {
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.INFO, "Ticket Type is required", Banner.TOP, 2000).show();
    }

    @Override
    public void onInvalidEstTime() {
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.INFO, "Invalid estimated time", Banner.TOP, 2000).show();
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
    public void findTagsSuccess() {
        tagsList = TagRepo.getInstance().getAllTags();
        createTeamBottomSheet();
    }

    @Override
    public void findTagsFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
        }
    }

    @Override
    public void getLabelSuccess() {
        labelList = LabelRepo.getInstance().getAllLabels();
        createLabelBottomSheet();
    }

    @Override
    public void getLabelFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
        }
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
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                Constants.SERVER_ERROR);
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


    private void setupSheetHeight(BottomSheetDialog bottomSheetDialog, int state) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

            int windowHeight = getWindowHeight();
            if (layoutParams != null) {
                layoutParams.height = windowHeight;
            }
            bottomSheet.setLayoutParams(layoutParams);
            behavior.setState(state);
        } else {
            Toast.makeText(this, "bottom sheet null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void findEmployeeSuccess() {
        employeeList = AssignEmployeeRepo.getInstance().getAllAssignEmployees();
        createEmployeeBottomSheet();
    }

    @Override
    public void findEmployeeFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
        }

    }

    @Override
    public void findCustomerSuccess() {
        customerList = CustomerRepo.getInstance().getAllCustomers();
        GlobalUtils.showLog(TAG, "customer list size: " + customerList.size());
        createCustomerBottomSheet();
    }

    @Override
    public void findCustomerFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
        }
    }

    @Override
    public void getTypeSuccess() {
        ticketTypeList = TicketCategoryRepo.getInstance().getAllTicketCategories();
        TicketCategorySearchAdapter ticketCategorySearchAdapter = new TicketCategorySearchAdapter
                (this, ticketTypeList);
        etTicketType.setAdapter(ticketCategorySearchAdapter);
    }

    @Override
    public void getTypeFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
        }
    }

    @Override
    public void getSummarySuggestionSuccess(TicketProto.TicketAutofillSuggestionRes autoFillResponse) {
        if (Objects.requireNonNull(etSummary.getText()).toString().isEmpty()) {
            llPrioritySuggestion.setVisibility(View.GONE);
            llTeamSuggestion.setVisibility(View.GONE);
            llLabelSuggestion.setVisibility(View.GONE);
            llEstTimeSuggestion.setVisibility(View.GONE);
            llEmpSuggestion.setVisibility(View.GONE);
        } else {
            setSuggestions(autoFillResponse);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setSuggestions(TicketProto.TicketAutofillSuggestionRes autoFillResponse) {
        if (autoFillResponse.getEmployee() != null && autoFillResponse.hasEmployee()) {
            suggestedEmployee = new AssignEmployee();
            suggestedEmployee.setAccountId(autoFillResponse.getEmployee().getAccount().getAccountId());
            suggestedEmployee.setEmployeeId(autoFillResponse.getEmployee().getEmployeeProfileId());
            suggestedEmployee.setName(autoFillResponse.getEmployee().getAccount().getFullName());
            suggestedEmployee.setEmployeeImageUrl(autoFillResponse.getEmployee().getAccount().getProfilePic());

            Glide.with(this)
                    .load(suggestedEmployee.getEmployeeImageUrl())
                    .error(R.drawable.ic_empty_profile_holder_icon)
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .into(civSuggestedEmp);
            tvEmpSuggestion.setText(suggestedEmployee.getName());
            llEmpSuggestion.setVisibility(View.VISIBLE);
        } else {
            llEmpSuggestion.setVisibility(View.GONE);
        }


        if (autoFillResponse.getLabel() != null && autoFillResponse.hasLabel()) {
            suggestedLabel = new Label();
            suggestedLabel.setName(autoFillResponse.getLabel().getName());
            suggestedLabel.setLabelId(autoFillResponse.getLabel().getLabelId());

            tvLabelSuggestion.setText(suggestedLabel.getName());
            llLabelSuggestion.setVisibility(View.VISIBLE);
        } else {
            llLabelSuggestion.setVisibility(View.GONE);
        }

        if (autoFillResponse.getTeam() != null && autoFillResponse.hasTeam()) {
            suggestedTeam = new Tags();
            suggestedTeam.setLabel(autoFillResponse.getTeam().getLabel());
            suggestedTeam.setTagId(autoFillResponse.getTeam().getTeamId());

            tvTeamSuggestion.setText(suggestedTeam.getLabel());
            llTeamSuggestion.setVisibility(View.VISIBLE);
        } else {
            llTeamSuggestion.setVisibility(View.GONE);
        }

        if (!autoFillResponse.getPriority().name().isEmpty()) {
            suggestedPriority = new Priority();
            suggestedPriority.setIcon(autoFillResponse.getPriorityValue());
            suggestedPriority.setValue(autoFillResponse.getPriority().name());

            switch (autoFillResponse.getPriority().getNumber()) {
                case 1:
                    tvPrioritySuggestion.setText("Lowest");
                    ivPrioritySuggestion.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_lowest_small));
                    break;

                case 2:
                    tvPrioritySuggestion.setText("Low");
                    ivPrioritySuggestion.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_low_small));
                    break;

                case 3:
                    tvPrioritySuggestion.setText("Medium");
                    ivPrioritySuggestion.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_medium_small));
                    break;

                case 4:
                    tvPrioritySuggestion.setText("High");
                    ivPrioritySuggestion.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_high_small));
                    break;

                case 5:
                    tvPrioritySuggestion.setText("Highest");
                    ivPrioritySuggestion.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_highest_small));
                    break;

            }
            llPrioritySuggestion.setVisibility(View.VISIBLE);
        } else {
            llPrioritySuggestion.setVisibility(View.GONE);
        }

        if (autoFillResponse.getEstimatedTime() != null && !autoFillResponse.getEstimatedTime().isEmpty()) {
            suggestedEstTime = autoFillResponse.getEstimatedTime();
            tvEstTimeSuggestion.setText(suggestedEstTime);

            llEstTimeSuggestion.setVisibility(View.VISIBLE);
        } else {
            llEstTimeSuggestion.setVisibility(View.GONE);
        }
    }

    @Override
    public void getSummarySuggestionFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
        }

        llPrioritySuggestion.setVisibility(View.GONE);
        llTeamSuggestion.setVisibility(View.GONE);
        llLabelSuggestion.setVisibility(View.GONE);
        llEstTimeSuggestion.setVisibility(View.GONE);
        llEmpSuggestion.setVisibility(View.GONE);
    }

    @Override
    public void getDependencyTicketsListSuccess() {

    }

    @Override
    public void getDependencyTicketsListFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
        }
    }

    @Override
    public void searchDependentTicketSuccess(List<DependentTicket> ticketList) {
        dependentTicketAdapter.setData(ticketList);
    }

    @Override
    public void searchDependentTicketFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
        }
    }

}