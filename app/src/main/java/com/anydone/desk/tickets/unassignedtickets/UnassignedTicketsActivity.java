package com.anydone.desk.tickets.unassignedtickets;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.anydone.desk.R;
import com.anydone.desk.adapters.EmployeeSearchAdapter;
import com.anydone.desk.adapters.PriorityAdapter;
import com.anydone.desk.adapters.SearchServiceAdapter;
import com.anydone.desk.adapters.TagSearchAdapter;
import com.anydone.desk.adapters.TicketCategorySearchAdapter;
import com.anydone.desk.adapters.TicketsAdapter;
import com.anydone.desk.base.activity.MvpBaseActivity;
import com.anydone.desk.model.Priority;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Employee;
import com.anydone.desk.realm.model.Service;
import com.anydone.desk.realm.model.Tags;
import com.anydone.desk.realm.model.TicketCategory;
import com.anydone.desk.realm.model.Tickets;
import com.anydone.desk.realm.repo.AssignEmployeeRepo;
import com.anydone.desk.realm.repo.AvailableServicesRepo;
import com.anydone.desk.realm.repo.EmployeeRepo;
import com.anydone.desk.realm.repo.TagRepo;
import com.anydone.desk.realm.repo.TicketCategoryRepo;
import com.anydone.desk.realm.repo.TicketRepo;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.UiUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UnassignedTicketsActivity extends MvpBaseActivity<UnassignedTicketPresenterImpl>
        implements UnassignedTicketsContract.UnassignedView {

    public static final int EMPLOYEE_ASSIGN_REQUEST = 3454;
    private static final String TAG = "UnassignedTicketsActivi";
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    @BindView(R.id.rv_assignable_tickets)
    RecyclerView rvAssignableTickets;
    private TicketsAdapter adapter;
    @BindView(R.id.iv_data_not_found)
    ImageView ivDataNotFound;
    @BindView(R.id.swipe_refresh_assignable_tickets)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.bottom_sheet)
    LinearLayout llBottomSheet;
    @BindView(R.id.shadow)
    View bottomSheetShadow;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_service)
    ImageView ivService;
    @BindView(R.id.iv_export)
    ImageView ivExport;
    @BindView(R.id.tv_title)
    TextView tvTitle;


    private int assignTicketPos;
    List<Tickets> assignableTickets;
    private BottomSheetDialog filterBottomSheet;
    private HorizontalScrollView hsvStatusContainer;
    private EditText etFromDate, etTillDate;
    private MaterialButton btnSearch;
    private AutoCompleteTextView etSearchText;
    private TextView tvReset;
    final Calendar myCalendar = Calendar.getInstance();
    String statusValue = null;
    private RadioGroup rgStatus;
    private BottomSheetBehavior sheetBehavior;
    private BottomSheetDialog exportBottomSheet;

    private EmployeeSearchAdapter employeeSearchAdapter;
    private String selectedEmployeeId;
    private List<AssignEmployee> employeeList;
    private RecyclerView rvEmployee;
    private ScrollView svSearchEmployee;
    private AutoCompleteTextView searchEmployee;
    private LinearLayout llSelf;
    private TextView tvSelfName;
    private CircleImageView civSelfImage;
    private Employee selfEmployee;
    private String ticketId;
    private TextView tvAllUsers;
    private Priority selectedPriority = new Priority("", -1);
    private AppCompatSpinner spPriority;
    private TextView tvPriorityHint;
    private BottomSheetDialog employeeBottomSheet;
    private AutoCompleteTextView etEmployee;
    private AutoCompleteTextView etTeam;
    private AutoCompleteTextView etTicketType;
    private AutoCompleteTextView etService;
    private LinearLayout llEmployeeSearchResult;
    private TextView tvEmployeeAsSelf;
    private RecyclerView rvEmployeeResults;
    private AssignEmployee selectedEmployee;
    private CircleImageView civEmployeeAsSelf;
    private LinearLayout llEmployeeAsSelf;
    TextView tvStatus;
    TicketCategory selectedTicketType;
    Tags selectedTeam;
    Service selectedService;
    private BottomSheetDialog serviceBottomSheet;
    private RecyclerView rvServices;
    private String selectedServiceId;

    private SearchServiceAdapter serviceAdapter;
    private long from, to;

    @Override
    protected int getLayout() {
        return R.layout.activity_unassigned_tickets;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assignableTickets = TicketRepo.getInstance().getAssignableTickets();
        selfEmployee = EmployeeRepo.getInstance().getEmployee();

        if (CollectionUtils.isEmpty(assignableTickets)) {
            presenter.getAssignableTickets(true, 0, System.currentTimeMillis(), 200);
        } else {
            setCount(assignableTickets.size());
            setUpRecyclerView(assignableTickets);
        }

        createServiceBottomSheet();
        createFilterBottomSheet();
        createExportBottomSheet();
        setUpTicketTypeFilterData();
        setUpTeamFilterData();
//        setUpServiceFilterData();

        sheetBehavior = BottomSheetBehavior.from(llBottomSheet);
//        handleEmployeeBottomSheet();
        createEmployeeBottomSheet();
        presenter.getEmployees();

        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh assignable called");

                    presenter.getAssignableTickets(false, 0, System.currentTimeMillis(), 200);

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        //Do something after 1 sec
                        if (swipeRefreshLayout != null)
                            swipeRefreshLayout.setRefreshing(false);
                    }, 1000);
                }
        );

        tvToolbarTitle.setOnClickListener(v -> {
            serviceBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            toggleServiceBottomSheet();
        });

        ivBack.setOnClickListener(v -> onBackPressed());
        ivFilter.setOnClickListener(v -> {
            @SuppressLint("InflateParams") View statusView = getLayoutInflater()
                    .inflate(R.layout.layout_status_buttons_alternate, null);
            rgStatus = statusView.findViewById(R.id.rg_status);

            rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton selectedRadioButton = group.findViewById(checkedId);
                //highlight selected button and disable unselected
                int count = group.getChildCount();
                for (int i = 0; i < count; i++) {
                    RadioButton rb = (RadioButton) group.getChildAt(i);
                    rb.setBackground(getResources().getDrawable(R.drawable.round_line_inactive));
                    rb.setTextColor(getResources().getColor(R.color.grey));
                }

                selectedRadioButton.setBackground(getResources()
                        .getDrawable(R.drawable.round_line_active));
                selectedRadioButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                statusValue = selectedRadioButton.getText().toString().trim();
            });

            hsvStatusContainer.removeAllViews();
            hsvStatusContainer.addView(rgStatus);

            toggleBottomSheet();
        });

        ivExport.setOnClickListener(view -> exportBottomSheet.show());

    }

    private void createExportBottomSheet() {
        exportBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View llBottomSheet = getLayoutInflater()
                .inflate(R.layout.bottom_sheet_export, null);

        exportBottomSheet.setContentView(llBottomSheet);

        RelativeLayout rlPdf = llBottomSheet.findViewById(R.id.rl_pdf);
        RelativeLayout rlExcel = llBottomSheet.findViewById(R.id.rl_excel);

        rlPdf.setOnClickListener(view -> {
            presenter.export(etSearchText.getText().toString(), from, to,
                    getTicketState(statusValue), selectedPriority, selectedEmployee, selectedTicketType,
                    selectedTeam, selectedService, "UNASSIGNED", "PDF");
            exportBottomSheet.dismiss();
        });

        rlExcel.setOnClickListener(view -> {
            presenter.export(etSearchText.getText().toString(), from, to,
                    getTicketState(statusValue), selectedPriority, selectedEmployee, selectedTicketType,
                    selectedTeam, selectedService, "UNASSIGNED", "SPREADSHEET");
            exportBottomSheet.dismiss();
        });

    }

    private void setCount(int size) {
        if (size > 0) {
            String count = "Unassigned Tickets";
            count = count + " (" + size + ")";
            tvTitle.setText(count);
        } else {
            tvTitle.setText("Unassigned Tickets");
        }
    }

    private void handleEmployeeBottomSheet() {
        searchEmployee = llBottomSheet.findViewById(R.id.et_search_employee);
        MaterialButton btnAssign = llBottomSheet.findViewById(R.id.btn_assign);
        svSearchEmployee = llBottomSheet.findViewById(R.id.search_employee);
        rvEmployee = llBottomSheet.findViewById(R.id.rv_all_users);
        civSelfImage = llBottomSheet.findViewById(R.id.civ_image_self);
        tvSelfName = llBottomSheet.findViewById(R.id.tv_name_self);
        llSelf = llBottomSheet.findViewById(R.id.ll_self);
        tvAllUsers = llBottomSheet.findViewById(R.id.tv_all_users);

//        setSelfDetails();
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

                selectedEmployeeId = self.getEmployeeId();
            }

            searchEmployee.setText(selfEmployee.getName());
            searchEmployee.setSelection(selfEmployee.getName().length());
            svSearchEmployee.setVisibility(View.GONE);

        /*    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) llBottomSheet.getLayoutParams();
            params.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT;
            llBottomSheet.setLayoutParams(params);*/
            hideKeyBoard();
        });

        searchEmployee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {
                    GlobalUtils.showLog(TAG, "text changed");
                    employeeList = AssignEmployeeRepo.getInstance().searchEmployee(s.toString());
                    if (CollectionUtils.isEmpty(employeeList)) {
                        tvAllUsers.setVisibility(View.GONE);
                    } else {
                        tvAllUsers.setVisibility(View.VISIBLE);
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
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchEmployee.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) llBottomSheet.getLayoutParams();
                params.height = CoordinatorLayout.LayoutParams.MATCH_PARENT;
                llBottomSheet.setLayoutParams(params);

                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetShadow.setVisibility(View.GONE);
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) llBottomSheet.getLayoutParams();
                    params.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT;
                    llBottomSheet.setLayoutParams(params);
                    hideKeyBoard();
                    searchEmployee.setText("");
                    searchEmployee.clearFocus();
                }

                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    searchEmployee.setText("");
                    searchEmployee.clearFocus();
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events

            }
        });

        btnAssign.setOnClickListener(v -> showAssignTicketDialog(ticketId));
    }
    /*
     */

    /**
     * manually opening / closing bottom sheet on button click
     */
    public void toggleBottomSheet() {
        if (filterBottomSheet.isShowing()) filterBottomSheet.dismiss();
        else {
            int selectedRadioBtn = Hawk.get(Constants.SELECTED_TICKET_FILTER_STATUS, -1);
            if (selectedRadioBtn != -1) {
                int rgCount = rgStatus.getChildCount();
                for (int i = 0; i < rgCount; i++) {
                    RadioButton button = (RadioButton) rgStatus.getChildAt(i);
                    if (button.getId() == selectedRadioBtn) {
                        button.setChecked(true);
                        button.setBackground(getResources().getDrawable(R.drawable.round_line_active));
                        button.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
            }
            filterBottomSheet.show();
        }
    }

    public void toggleServiceBottomSheet() {
        if (serviceBottomSheet.isShowing()) {
            serviceBottomSheet.dismiss();
        } else {
            serviceBottomSheet.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.getAssignableTickets(false, 0,
                System.currentTimeMillis(), 200);
    }

    private void createServiceBottomSheet() {
        serviceBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View llBottomSheet = getLayoutInflater()
                .inflate(R.layout.bottomsheet_select_service, null);

        serviceBottomSheet.setContentView(llBottomSheet);
        serviceBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        serviceBottomSheet.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
         /*   if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);*/
            setupSheetHeight(d, BottomSheetBehavior.STATE_HALF_EXPANDED);
        });


        EditText searchService = llBottomSheet.findViewById(R.id.et_search_service);
        rvServices = llBottomSheet.findViewById(R.id.rv_services);

        searchService.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setupSheetHeight(serviceBottomSheet, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        serviceBottomSheet.setOnDismissListener(dialog -> searchService.clearFocus());

        List<Service> serviceList = AvailableServicesRepo.getInstance().getAvailableServices();
        selectedServiceId = Hawk.get(Constants.SELECTED_SERVICE);
        if (selectedServiceId == null && !serviceList.isEmpty()) {
            Service firstService = serviceList.get(0);
            tvToolbarTitle.setText(firstService.getName().replace("_", " "));
            Glide.with(Objects.requireNonNull(getContext())).load
                    (firstService.getServiceIconUrl())
                    .placeholder(R.drawable.ic_service_ph)
                    .error(R.drawable.ic_service_ph)
                    .into(ivService);
            Hawk.put(Constants.SELECTED_SERVICE, firstService.getServiceId());
        } else {
            Service selectedService = AvailableServicesRepo.getInstance()
                    .getAvailableServiceById(selectedServiceId);
            if (selectedService != null) {
                tvToolbarTitle.setText(selectedService.getName().replace("_", " "));
                Glide.with(Objects.requireNonNull(getContext()))
                        .load(selectedService.getServiceIconUrl())
                        .placeholder(R.drawable.ic_service_ph)
                        .error(R.drawable.ic_service_ph)
                        .into(ivService);

                setUpServiceRecyclerView(serviceList);
            }
        }


        searchService.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                serviceAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setUpServiceRecyclerView(List<Service> serviceList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvServices.setLayoutManager(mLayoutManager);

        serviceAdapter = new SearchServiceAdapter(serviceList, this);
        rvServices.setAdapter(serviceAdapter);

        serviceAdapter.setOnItemClickListener(service -> {
            hideKeyBoard();
            Hawk.put(Constants.SELECTED_SERVICE, service.getServiceId());
//            Hawk.put(Constants.SERVICE_CHANGED_TICKET, true);
            tvToolbarTitle.setText(service.getName().replace("_", " "));
            Glide.with(getContext()).load(service.getServiceIconUrl())
                    .placeholder(R.drawable.ic_service_ph)
                    .error(R.drawable.ic_service_ph)
                    .into(ivService);
//            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            bottomSheetShadow.setVisibility(View.GONE);
            serviceBottomSheet.dismiss();
            ivDataNotFound.setVisibility(View.GONE);

            presenter.getAssignableTickets(true, 0,
                    System.currentTimeMillis(), 200);
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void createFilterBottomSheet() {
        filterBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.layout_bottomsheet_filter_alternate, null);

        filterBottomSheet.setContentView(view);
        btnSearch = view.findViewById(R.id.btn_search);
        etSearchText = view.findViewById(R.id.et_search);
        etFromDate = view.findViewById(R.id.et_from_date);
        etTillDate = view.findViewById(R.id.et_till_date);
        spPriority = view.findViewById(R.id.sp_priority);
        tvReset = view.findViewById(R.id.tv_reset);
        tvStatus = view.findViewById(R.id.tv_status);
        hsvStatusContainer = view.findViewById(R.id.hsv_status_container);
        tvPriorityHint = view.findViewById(R.id.tv_priority_hint);
        etTeam = view.findViewById(R.id.et_team);
        etTicketType = view.findViewById(R.id.et_ticket_type);
//        etService = view.findViewById(R.id.et_service);
//        spPriority.setSelection(0);

        filterBottomSheet.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        });


        spPriority.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                List<Priority> priorityList = GlobalUtils.getPriorityList();
                PriorityAdapter adapter = new PriorityAdapter(this,
                        R.layout.layout_proirity, priorityList);
                spPriority.setAdapter(adapter);
            }
            return false;
        });

        selectedPriority = (Priority) spPriority.getSelectedItem();

        spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPriority = (Priority) spPriority.getItemAtPosition(position);
                GlobalUtils.showLog(TAG, "selected Priority" + selectedPriority.getValue());
                tvPriorityHint.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DatePickerDialog.OnDateSetListener fromDateListener = (view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateFromDate();
        };

        DatePickerDialog.OnDateSetListener tillDateListener = (view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateToDate();
        };

        etFromDate.setOnClickListener(v -> new DatePickerDialog(this, fromDateListener,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        etTillDate.setOnClickListener(v -> new DatePickerDialog(this, tillDateListener,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        tvReset.setOnClickListener(v -> {
            toggleBottomSheet();
            etSearchText.setText("");
            etFromDate.setText("");
            etTillDate.setText("");
//            etEmployee.setText("");
            etTicketType.setText("");
            etTeam.setText("");
//            etService.setText("");

            resetStatus();
            hideKeyBoard();

            selectedEmployee = null;
            selectedTicketType = null;
            selectedTeam = null;
            selectedService = null;

            Hawk.put(Constants.SELECTED_TICKET_FILTER_STATUS, -1);

            List<Priority> priorityList = Collections.emptyList();
            PriorityAdapter adapter = new PriorityAdapter(this,
                    R.layout.layout_proirity, priorityList);
            spPriority.setAdapter(adapter);
            tvPriorityHint.setVisibility(View.VISIBLE);

            selectedPriority = new Priority("", -1);
        });

        etSearchText.setOnItemClickListener((parent, v, position, id) -> hideKeyBoard());

        btnSearch.setOnClickListener(v -> {
            String fromDate = etFromDate.getText().toString().trim();
            String tillDate = etTillDate.getText().toString().trim();

            from = 0;
            to = 0;

            if (!fromDate.isEmpty() && !tillDate.isEmpty()) {
                Calendar calendarFromDate = Calendar.getInstance();
                Calendar calendarTillDate = Calendar.getInstance();
                String[] fromDateSeparated = fromDate.split("/");
                String[] tillDateSeparated = tillDate.split("/");

                calendarFromDate.set(Integer.parseInt(fromDateSeparated[0]),
                        Integer.parseInt(fromDateSeparated[1]) - 1,
                        Integer.parseInt(fromDateSeparated[2]), 0, 0, 0);

                calendarTillDate.set(Integer.parseInt(tillDateSeparated[0]),
                        Integer.parseInt(tillDateSeparated[1]) - 1,
                        Integer.parseInt(tillDateSeparated[2]), 23, 59, 59);

                from = calendarFromDate.getTime().getTime();
                to = calendarTillDate.getTime().getTime();
            }

      /*      if (etEmployee.getText().toString().isEmpty()) {
                selectedEmployee = null;
            }*/

            if (etTicketType.getText().toString().isEmpty()) {
                selectedTicketType = null;
            }

            if (etTeam.getText().toString().isEmpty()) {
                selectedTeam = null;
            }

          /*  if (etService.getText().toString().isEmpty()) {
                selectedService = null;
            }*/

            Hawk.put(Constants.SELECTED_TICKET_FILTER_STATUS, rgStatus.getCheckedRadioButtonId());

            presenter.filterAssignableTickets(etSearchText.getText().toString(), from, to,
                    getTicketState(statusValue), selectedPriority, selectedEmployee, selectedTicketType,
                    selectedTeam, selectedService);
            toggleBottomSheet();
        });

        etSearchText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyBoard();
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpEmployeeRecyclerView(RecyclerView rvEmployeeAllUsers
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
                UiUtils.hideKeyboardForced(this);
                selectedEmployeeId = employee.getEmployeeId();
                showAssignTicketDialog(ticketId);
            });
        }
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
   /*         if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);*/
            setupSheetHeight(d, BottomSheetBehavior.STATE_HALF_EXPANDED);
        });


        EditText searchEmployee = llBottomSheet.findViewById(R.id.et_search_employee);
        LinearLayout llEmployeeAsSelf = llBottomSheet.findViewById(R.id.ll_self);
        CircleImageView civEmployeeAsSelf = llBottomSheet.findViewById(R.id.civ_image_self);
        TextView tvEmployeeAsSelf = llBottomSheet.findViewById(R.id.tv_name_self);
        TextView tvEmployeeAllUsers = llBottomSheet.findViewById(R.id.tv_all_users);
        TextView tvSuggestions = llBottomSheet.findViewById(R.id.tv_suggestions);
        rvEmployee = llBottomSheet.findViewById(R.id.rv_all_users);

        setSelfDetails(llEmployeeAsSelf, tvEmployeeAsSelf, civEmployeeAsSelf, tvSuggestions);

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
                showAssignTicketDialog(ticketId);
            }

            hideKeyBoard();
        });

        searchEmployee.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setupSheetHeight(employeeBottomSheet, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        employeeBottomSheet.setOnDismissListener(dialog -> {
            searchEmployee.clearFocus();
            searchEmployee.getText().clear();
            if (rvEmployee.getChildCount() > 0) rvEmployee.scrollToPosition(0);
        });

        setUpEmployeeRecyclerView(rvEmployee);
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
    }

/*    private void setUpServiceFilterData() {
        List<Service> serviceList = AvailableServicesRepo.getInstance().getAvailableServices();
        ServiceFilterAdapter adapter = new ServiceFilterAdapter(getContext(), serviceList);
        etService.setThreshold(1);
        etService.setAdapter(adapter);

        etService.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etService.showDropDown();
            } else {
                etService.dismissDropDown();
            }
        });

        etService.setOnItemClickListener((parent, view, position, id) -> {
            selectedService = serviceList.get(position);
            GlobalUtils.showLog(TAG, "selected service: " + selectedService.getName());
        });

        etService.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }*/

    private void setUpTeamFilterData() {
        List<Tags> teamList = TagRepo.getInstance().getAllTags();
        TagSearchAdapter adapter = new TagSearchAdapter
                (Objects.requireNonNull(getContext()), teamList);
        etTeam.setThreshold(1);
        etTeam.setAdapter(adapter);

        etTeam.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etTeam.showDropDown();
            } else {
                etTeam.dismissDropDown();
            }
        });


        etTeam.setOnClickListener(v -> {
            if (!teamList.isEmpty()) {
                etTeam.showDropDown();
            } else {
                Toast.makeText(this, "Teams not available", Toast.LENGTH_SHORT).show();
            }
        });

        etTeam.setOnItemClickListener((parent, view, position, id) -> {
            selectedTeam = teamList.get(position);
            GlobalUtils.showLog(TAG, "selected team: " + selectedTeam.getLabel());
        });
    }

    private void setUpTicketTypeFilterData() {
        List<TicketCategory> ticketTypeList = TicketCategoryRepo.getInstance().getAllTicketCategories();
        TicketCategorySearchAdapter adapter = new TicketCategorySearchAdapter
                (Objects.requireNonNull(getContext()), ticketTypeList);
        etTicketType.setThreshold(1);
        etTicketType.setAdapter(adapter);

        etTicketType.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etTicketType.showDropDown();
            } else {
                etTicketType.dismissDropDown();
            }
        });


        etTicketType.setOnClickListener(v -> {
            if (!ticketTypeList.isEmpty()) {
                etTicketType.showDropDown();
            } else {
                Toast.makeText(this, "Ticket Types not available", Toast.LENGTH_SHORT).show();
            }
        });

        etTicketType.setOnItemClickListener((parent, view, position, id) -> {
            selectedTicketType = ticketTypeList.get(position);
            GlobalUtils.showLog(TAG, "selected ticket type: " + selectedTicketType.getName());
        });
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

    private int getTicketState(String statusValue) {
        if (statusValue != null) {
            switch (statusValue) {
                case "STARTED":
                    return TicketProto.TicketState.TICKET_STARTED.getNumber();

                case "TODO":
                    return TicketProto.TicketState.TICKET_CREATED.getNumber();

                case "REOPENED":
                    return TicketProto.TicketState.TICKET_REOPENED.getNumber();

                case "RESOLVED":
                    return TicketProto.TicketState.TICKET_RESOLVED.getNumber();

                case "CLOSED":
                    return TicketProto.TicketState.TICKET_CLOSED.getNumber();

                default:
                    break;
            }
        }
        return -1;
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    private void setUpRecyclerView(List<Tickets> ticketsList) {
        rvAssignableTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvAssignableTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
            adapter = new TicketsAdapter(ticketsList, getContext(), rvAssignableTickets);
      /*      adapter.setOnItemClickListener(ticket -> {
                Intent i = new Intent(this, TicketDetailsActivity.class);
                i.putExtra("selected_ticket_id", ticket.getTicketId());
                i.putExtra("ticket_desc", ticket.getTitle());
                startActivity(i);
            });*/

            adapter.setOnAssignListener((id, pos) -> {
                assignTicketPos = pos;
//                showAssignTicketDialog(id);
                GlobalUtils.showLog(TAG, "assign ticket id check: " + id);
                ticketId = id;
                adapter.closeSwipeLayout(id);
                employeeBottomSheet.show();
           /*     Intent i = new Intent(this, AssignEmployeeActivity.class);
                i.putExtra("ticket_id", Long.valueOf(id));
                startActivityForResult(i, EMPLOYEE_ASSIGN_REQUEST);*/
            });
            rvAssignableTickets.setAdapter(adapter);
        } else {
            rvAssignableTickets.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
        }
    }

    private void showAssignTicketDialog(String ticketId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Assign ticket to employee?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    adapter.closeSwipeLayout(ticketId);
                    dialog.dismiss();
                    employeeBottomSheet.dismiss();
                    GlobalUtils.showLog(TAG, "ticket Id check: " + ticketId);
                    GlobalUtils.showLog(TAG, "employee Id check: " + selectedEmployeeId);
                    presenter.assignTicket(Long.parseLong(ticketId), selectedEmployeeId);
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

    private void resetStatus() {
        int rgCount = rgStatus.getChildCount();
        for (int i = 0; i < rgCount; i++) {
            statusValue = "null";
            RadioButton button = (RadioButton) rgStatus.getChildAt(i);
            button.setChecked(false);
            button.setBackground(getResources().getDrawable(R.drawable.round_line_inactive));
            button.setTextColor(getResources().getColor(R.color.grey));
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //prevent bottom sheet from changing layout height
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) llBottomSheet.getLayoutParams();
        params.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT;
        llBottomSheet.setLayoutParams(params);
    }

    @Override
    public void getAssignableTicketSuccess() {
        List<Tickets> assignableTickets = TicketRepo.getInstance().getAssignableTickets();

        if (CollectionUtils.isEmpty(assignableTickets)) {
            ivDataNotFound.setVisibility(View.VISIBLE);
            rvAssignableTickets.setVisibility(View.GONE);
        } else {
            setCount(assignableTickets.size());
            setUpRecyclerView(assignableTickets);
        }
    }

    public void toggleEmployeeBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_HALF_EXPANDED) {
            bottomSheetShadow.setVisibility(View.VISIBLE);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            searchEmployee.requestFocus();
        } else if (sheetBehavior.getState() == BottomSheetBehavior.STATE_HALF_EXPANDED) {
            bottomSheetShadow.setVisibility(View.GONE);
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            bottomSheetShadow.setVisibility(View.GONE);
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void getAssignableTicketFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
//        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    private void updateFromDate() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etFromDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateToDate() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etTillDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void assignSuccess() {
        presenter.getAssignableTickets(true, 0, System.currentTimeMillis(), 200);
        Hawk.put(Constants.FETCH_PENDING_LIST, true);
        Hawk.put(Constants.FETCH_IN_PROGRESS_LIST, true);
    }

    @Override
    public void assignFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void updateAssignableTickets(List<Tickets> ticketsList) {
        setCount(ticketsList.size());
        setUpRecyclerView(ticketsList);
    }

    @Override
    public void filterAssignableTicketFailed(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void getEmployeeSuccess() {
        employeeList = AssignEmployeeRepo.getInstance().getAllAssignEmployees();
        setUpEmployeeRecyclerView(rvEmployee);
    }

    @Override
    public void getEmployeeFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void showProgressExport() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onExportSuccess(String url, String fileType) {
        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();
        GlobalUtils.downloadFile(url, fileType, this);
    }

    @Override
    public void onExportFail(String msg) {
        GlobalUtils.showLog(TAG, "failed to export tickets");
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
        ivDataNotFound.setVisibility(View.GONE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EMPLOYEE_ASSIGN_REQUEST && resultCode == 2) {
            if (data != null) {
                boolean employeeAssigned = data.getBooleanExtra("employee_assigned", false);

                if (employeeAssigned) {
                    assignableTickets = TicketRepo.getInstance().getAssignableTickets();
                    setUpRecyclerView(assignableTickets);
                }
            }
        }
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
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

}