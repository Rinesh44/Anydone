package com.treeleaf.anydone.serviceprovider.opentickets;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.EmployeeSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.PriorityAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.SearchServiceAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.ServiceFilterAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.TagSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.TicketCategorySearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.TicketsAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketCategory;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.AssignEmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.AvailableServicesRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TagRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketCategoryRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class OpenTicketActivity extends MvpBaseActivity<OpenTicketPresenterImpl>
        implements OpenTicketContract.OpenTicketView {

    private static final String TAG = "OpenTicketActivity";
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    @BindView(R.id.rv_open_tickets)
    RecyclerView rvOpenTickets;
    private TicketsAdapter ticketAdapter;
    @BindView(R.id.iv_data_not_found)
    ImageView ivDataNotFound;
    @BindView(R.id.swipe_refresh_open_tickets)
    SwipeRefreshLayout swipeRefreshLayout;
    /*    @BindView(R.id.bottom_sheet)
        LinearLayout llBottomSheet;*/
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_service)
    ImageView ivService;


    List<Tickets> openTickets;
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
    private String localAccountId;
    private Account userAccount;
    private TicketsAdapter ticketsAdapter;
    TicketCategory selectedTicketType;
    Tags selectedTeam;
    private LinearLayout llEmployeeSearchResult;
    private TextView tvEmployeeAsSelf;
    private RecyclerView rvEmployeeResults;
    private AssignEmployee selectedEmployee;
    private CircleImageView civEmployeeAsSelf;
    private LinearLayout llEmployeeAsSelf;
    TextView tvStatus;
    private AutoCompleteTextView etEmployee;
    private AutoCompleteTextView etTeam;
    private AutoCompleteTextView etTicketType;
    private AutoCompleteTextView etService;
    Service selectedService;
    private BottomSheetDialog serviceBottomSheet;
    private RecyclerView rvServices;
    private String selectedServiceId;
    private SearchServiceAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_open_ticket;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userAccount = AccountRepo.getInstance().getAccount();
        localAccountId = userAccount.getAccountId();

//        setToolbar();
        openTickets = TicketRepo.getInstance().getOpenTickets();

        if (CollectionUtils.isEmpty(openTickets)) {
            GlobalUtils.showLog(TAG, "open tickets empty");
            ivDataNotFound.setVisibility(View.GONE);
            rvOpenTickets.setVisibility(View.VISIBLE);
            presenter.getOpenTickets(true, 0, System.currentTimeMillis(), 100);
        } else {
            setUpRecyclerView(openTickets);
        }

        createServiceBottomSheet();
        createFilterBottomSheet();
        setUpEmployeeFilterData();
        setUpTicketTypeFilterData();
        setUpTeamFilterData();
//        setUpServiceFilterData();

        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh open ticket called");

                    presenter.getOpenTickets(false, 0, System.currentTimeMillis(), 100);

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

        ivFilter.setOnClickListener(v -> {
            @SuppressLint("InflateParams") View statusView = getLayoutInflater()
                    .inflate(R.layout.layout_status_buttons_all, null);
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
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void getOpenTicketSuccess() {
        List<Tickets> openTickets = TicketRepo.getInstance().getOpenTickets();
        setUpRecyclerView(openTickets);
    }


    public void toggleServiceBottomSheet() {
        if (serviceBottomSheet.isShowing()) {
            serviceBottomSheet.dismiss();
        } else {
            serviceBottomSheet.show();
        }
    }

    @Override
    public void getOpenTicketFail(String msg) {
        ivDataNotFound.setVisibility(View.VISIBLE);
        rvOpenTickets.setVisibility(View.GONE);
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
        }
//        UiUtils.showSnackBar(getContext(), getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!CollectionUtils.isEmpty(subscribeableTickets)) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            @SuppressLint("InflateParams") View statusView = getLayoutInflater()
                    .inflate(R.layout.layout_status_buttons_all, null);
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
        }
        return false;
    }

    @Override
    public void updateTickets(List<Tickets> ticketsList) {
        setUpRecyclerView(ticketsList);
    }

    @Override
    public void filterTicketsFailed(String msg) {
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
        rvOpenTickets.setVisibility(View.GONE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
//            rvOpenTickets.setVisibility(View.VISIBLE);
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


    private void setUpRecyclerView(List<Tickets> ticketsList) {
        rvOpenTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvOpenTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
            ticketAdapter = new TicketsAdapter(ticketsList, getContext(), rvOpenTickets);
            ticketAdapter.setOnItemClickListener(ticket -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    ArrayList<String> employeeProfileUris = new ArrayList<>();
                    StringBuilder builder = new StringBuilder();
                    AssignEmployee assignedEmployee = ticket.getAssignedEmployee();
                    String assignedEmployeeName = assignedEmployee.getName();

                    Customer customer = ticket.getCustomer();
                    String customerName = customer.getFullName();

                    if (!localAccountId.equals(customer.getCustomerId()) && !customerName.isEmpty()) {
                        builder.append(customerName);
                        builder.append(", ");
                        employeeProfileUris.add(customer.getProfilePic());
                    }

                    if (!localAccountId.equals(assignedEmployee.getAccountId()) &&
                            assignedEmployeeName != null && !assignedEmployeeName.isEmpty()) {
                        builder.append(assignedEmployeeName);
                        builder.append(", ");
                        employeeProfileUris.add(assignedEmployee.getEmployeeImageUrl());
                    }
                    for (AssignEmployee employee : ticket.getContributorList()) {
                        if (!localAccountId.equals(employee.getAccountId())) {
                            builder.append(employee.getName());
                            builder.append(", ");
                            employeeProfileUris.add(employee.getEmployeeImageUrl());
                        }
                    }

                    String assignedEmployeeList = builder.toString().trim();
                    String callees = GlobalUtils.removeLastCharater(assignedEmployeeList);

                    Intent i = new Intent(this, TicketDetailsActivity.class);
                    i.putExtra("selected_ticket_id", ticket.getTicketId());
                    i.putExtra("ticket_desc", ticket.getTitle());
                    i.putExtra("selected_ticket_type", Constants.SUBSCRIBED);
                    i.putExtra("selected_ticket_name", callees);
                    i.putExtra("selected_ticket_index", ticket.getTicketIndex());
                    i.putExtra("selected_ticket_status", ticket.getTicketStatus());
                    i.putStringArrayListExtra("selected_ticket_icon_uri", employeeProfileUris);
                    startActivity(i);
                } else {
                    Banner.make(getWindow().getDecorView().getRootView(),
                            this, Banner.INFO, "Some of our features are not " +
                                    "supported in your device. " +
                                    "Sorry for inconvenience",
                            Banner.TOP, 2000).show();
                }
            });

            rvOpenTickets.setAdapter(ticketAdapter);
        } else {
            rvOpenTickets.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
        }
    }

    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Requested By Me");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpEmployeeFilterData() {
        List<AssignEmployee> employeeList = AssignEmployeeRepo.getInstance().getAllAssignEmployees();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvEmployeeResults.setLayoutManager(mLayoutManager);
        EmployeeSearchAdapter employeeSearchAdapter = new EmployeeSearchAdapter(employeeList, getContext(), true);
        rvEmployeeResults.setAdapter(employeeSearchAdapter);

        rvEmployeeResults.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager)
                    Objects.requireNonNull(this.getSystemService(Context.INPUT_METHOD_SERVICE));
            assert imm != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return false;
        });


        etEmployee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    employeeSearchAdapter.getFilter().filter(s);
                    llEmployeeSearchResult.setVisibility(View.VISIBLE);
                    Account userAccount = AccountRepo.getInstance().getAccount();
                    if (userAccount.getAccountType().equals("SERVICE_PROVIDER")) {
                        llEmployeeAsSelf.setVisibility(View.GONE);
                    } else {
                        llEmployeeAsSelf.setVisibility(View.VISIBLE);
                        tvEmployeeAsSelf.setText(userAccount.getFullName() + "(Me)");

                        Glide.with(getContext()).load(userAccount.getProfilePic()).into(civEmployeeAsSelf);

                        tvEmployeeAsSelf.setOnClickListener(v1 -> {
                            selectedEmployee = AssignEmployeeRepo.getInstance()
                                    .getAssignedEmployeeByAccountId(userAccount.getAccountId());
                            llEmployeeSearchResult.setVisibility(View.GONE);
                            etEmployee.setText(selectedEmployee.getName());
                        });
                    }

                } else {
                    llEmployeeSearchResult.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        employeeSearchAdapter.setOnItemClickListener(employee -> {
            selectedEmployee = employee;
            etEmployee.setText(selectedEmployee.getName());
            llEmployeeSearchResult.setVisibility(View.GONE);
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

        etTicketType.addTextChangedListener(new TextWatcher() {
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
        if (selectedServiceId == null) {
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
            tvToolbarTitle.setText(selectedService.getName().replace("_", " "));
            Glide.with(Objects.requireNonNull(getContext()))
                    .load(selectedService.getServiceIconUrl())
                    .placeholder(R.drawable.ic_service_ph)
                    .error(R.drawable.ic_service_ph)
                    .into(ivService);

            setUpServiceRecyclerView(serviceList);
        }


        searchService.addTextChangedListener(new TextWatcher() {
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


    private void setupSheetHeight(BottomSheetDialog bottomSheetDialog, int state) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
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

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void setUpServiceRecyclerView(List<Service> serviceList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvServices.setLayoutManager(mLayoutManager);

        adapter = new SearchServiceAdapter(serviceList, this);
        rvServices.setAdapter(adapter);

        adapter.setOnItemClickListener(service -> {
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

            presenter.getOpenTickets(true, 0,
                    System.currentTimeMillis(), 100);
        });
    }

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

        etTeam.addTextChangedListener(new TextWatcher() {
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
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createFilterBottomSheet() {
        filterBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.layout_bottomsheet_filter_tickets, null);

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
        etEmployee = view.findViewById(R.id.et_employee);
        etTeam = view.findViewById(R.id.et_team);
        etTicketType = view.findViewById(R.id.et_ticket_type);
        llEmployeeSearchResult = view.findViewById(R.id.ll_employee_search_results);
        tvEmployeeAsSelf = view.findViewById(R.id.tv_employee_as_self);
        rvEmployeeResults = view.findViewById(R.id.rv_employee_results);
        civEmployeeAsSelf = view.findViewById(R.id.civ_employee_as_self);
        llEmployeeAsSelf = view.findViewById(R.id.ll_employee_as_self);
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
            etEmployee.setText("");
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

            long from = 0;
            long to = 0;

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

            if (etEmployee.getText().toString().isEmpty()) {
                selectedEmployee = null;
            }

            if (etTicketType.getText().toString().isEmpty()) {
                selectedTicketType = null;
            }

            if (etTeam.getText().toString().isEmpty()) {
                selectedTeam = null;
            }

            Hawk.put(Constants.SELECTED_TICKET_FILTER_STATUS, rgStatus.getCheckedRadioButtonId());

            presenter.filterTickets(etSearchText.getText().toString(), from, to,
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

    private int getTicketState(String statusValue) {
        if (statusValue != null) {
            switch (statusValue.toLowerCase()) {
                case "started":
                    return TicketProto.TicketState.TICKET_STARTED.getNumber();

                case "todo":
                    return TicketProto.TicketState.TICKET_CREATED.getNumber();

                case "reopened":
                    return TicketProto.TicketState.TICKET_REOPENED.getNumber();

                case "resolved":
                    return TicketProto.TicketState.TICKET_RESOLVED.getNumber();

                case "closed":
                    return TicketProto.TicketState.TICKET_CLOSED.getNumber();

                default:
                    break;
            }
        }
        return -1;
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


    @OnClick(R.id.iv_back)
    public void goBack() {
        onBackPressed();
    }
}