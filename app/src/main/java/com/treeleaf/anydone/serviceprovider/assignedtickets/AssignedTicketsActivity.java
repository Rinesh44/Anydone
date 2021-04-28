package com.treeleaf.anydone.serviceprovider.assignedtickets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
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
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.EmployeeSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.SearchServiceAdapter;
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
import com.treeleaf.anydone.serviceprovider.realm.repo.AvailableServicesRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AssignedTicketsActivity extends MvpBaseActivity<AssignedTicketPresenterImpl>
        implements AssignedTicketContract.AssignedTicketView {

    private static final String TAG = "AssignedTicketsActivity";
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    @BindView(R.id.rv_open_tickets)
    RecyclerView rvAssignedTicket;
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
        return R.layout.activity_assigned_tickets;
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
            rvAssignedTicket.setVisibility(View.VISIBLE);
            presenter.getAssignedTickets(true, 0, System.currentTimeMillis(), 100);
        } else {
            setUpRecyclerView(openTickets);
        }

        createServiceBottomSheet();
//        createFilterBottomSheet();
//        setUpTicketTypeFilterData();
//        setUpTeamFilterData();
//        setUpServiceFilterData();

        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh assigned ticket called");

                    presenter.getAssignedTickets(false, 0, System.currentTimeMillis(), 100);

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

    public void toggleServiceBottomSheet() {
        if (serviceBottomSheet.isShowing()) {
            serviceBottomSheet.dismiss();
        } else {
            serviceBottomSheet.show();
        }
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

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void getAssignedTicketSuccess() {

    }

    @Override
    public void getAssignedTicketFail(String msg) {
        ivDataNotFound.setVisibility(View.VISIBLE);
        rvAssignedTicket.setVisibility(View.GONE);
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
        }
        UiUtils.showSnackBar(getContext(), getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void updateTickets(List<Tickets> ticketsList) {

    }

    @Override
    public void filterTicketsFailed(String msg) {

    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
        ivDataNotFound.setVisibility(View.GONE);
        rvAssignedTicket.setVisibility(View.GONE);
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
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

            presenter.getAssignedTickets(true, 0,
                    System.currentTimeMillis(), 100);
        });
    }

    private void setUpRecyclerView(List<Tickets> ticketsList) {
        rvAssignedTicket.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvAssignedTicket.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
            ticketAdapter = new TicketsAdapter(ticketsList, getContext(), rvAssignedTicket);
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

            rvAssignedTicket.setAdapter(ticketAdapter);
        } else {
            rvAssignedTicket.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
        }
    }

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

    @OnClick(R.id.iv_back)
    public void goBack() {
        onBackPressed();
    }
}