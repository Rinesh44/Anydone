package com.anydone.desk.subscribed;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.treeleaf.anydone.entities.TicketProto;
import com.anydone.desk.R;
import com.anydone.desk.adapters.PriorityAdapter;
import com.anydone.desk.adapters.SearchServiceAdapter;
import com.anydone.desk.adapters.TagSearchAdapter;
import com.anydone.desk.adapters.TicketCategorySearchAdapter;
import com.anydone.desk.adapters.TicketsAdapter;
import com.anydone.desk.base.activity.MvpBaseActivity;
import com.anydone.desk.model.Priority;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Customer;
import com.anydone.desk.realm.model.Service;
import com.anydone.desk.realm.model.Tags;
import com.anydone.desk.realm.model.TicketCategory;
import com.anydone.desk.realm.model.Tickets;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.AvailableServicesRepo;
import com.anydone.desk.realm.repo.TagRepo;
import com.anydone.desk.realm.repo.TicketCategoryRepo;
import com.anydone.desk.realm.repo.TicketRepo;
import com.anydone.desk.ticketdetails.TicketDetailsActivity;
import com.anydone.desk.tickets.inprogresstickets.OnInProgressTicketsListener;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.UiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class SubscribedTicketsActivity extends MvpBaseActivity<SubscribedTicketPresenterImpl>
        implements SubscribedTicketContract.SubscribeTicketsView {
    private static final String TAG = "SubscribeTicketsFragmen";
    @BindView(R.id.rv_subscribe_tickets)
    RecyclerView rvSubscribeTickets;
    @BindView(R.id.swipe_refresh_subscribe_tickets)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.iv_data_not_found)
    ImageView ivDataNotFound;
    /*    @BindView(R.id.fab_subscribe)
        FloatingActionButton fabSubscribe;*/
    @BindView(R.id.pb_search)
    ProgressBar progressBar;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_service)
    ImageView ivService;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.iv_export)
    ImageView ivExport;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private Unbinder unbinder;
    private OnInProgressTicketsListener onInProgressTicketsListener;
    private TicketsAdapter ticketsAdapter;
    private int unsubscribedTicketPos;
    private boolean fetchList = false;
    private List<Tickets> subscribedTickets;
    private Account userAccount;
    private String localAccountId;
    private BottomSheetDialog filterBottomSheet;
    private BottomSheetDialog exportBottomSheet;
    private MaterialButton btnSearch;
    private RadioGroup rgStatus;
    String statusValue = null;
    private HorizontalScrollView hsvStatusContainer;
    private EditText etFromDate, etTillDate;
    private TextView tvReset;
    private AppCompatSpinner spPriority;
    private TextView tvPriorityHint;
    private Priority selectedPriority = new Priority("", -1);
    private AutoCompleteTextView etSearchText;
    final Calendar myCalendar = Calendar.getInstance();
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
    private String selectedServiceId;
    private RecyclerView rvServices;
    private BottomSheetDialog serviceBottomSheet;
    private SearchServiceAdapter adapter;
    private long from, to;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userAccount = AccountRepo.getInstance().getAccount();
        localAccountId = userAccount.getAccountId();

//        setToolbar();
        subscribedTickets = TicketRepo.getInstance().getSubscribedTickets();

        if (CollectionUtils.isEmpty(subscribedTickets)) {
            GlobalUtils.showLog(TAG, "subscribe tickets empty");
            ivDataNotFound.setVisibility(View.GONE);
            rvSubscribeTickets.setVisibility(View.VISIBLE);
            presenter.getSubscribedTickets(true, 0, System.currentTimeMillis(), 200);
        } else {
            setCount(subscribedTickets.size());
            setUpRecyclerView(subscribedTickets);
        }

        createServiceBottomSheet();
        createFilterBottomSheet();
        createExportBottomSheet();
        setUpTicketTypeFilterData();
        setUpTeamFilterData();
//        setUpServiceFilterData();

        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh subscribe called");

                    presenter.getSubscribedTickets(false, 0, System.currentTimeMillis(), 200);

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


        ivExport.setOnClickListener(view -> exportBottomSheet.show());

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
    }


    public void toggleServiceBottomSheet() {
        if (serviceBottomSheet.isShowing()) {
            serviceBottomSheet.dismiss();
        } else {
            serviceBottomSheet.show();
        }
    }


    private void setCount(int size) {
        if (size > 0) {
            String count = "Subscribed Tickets";
            count = count + " (" + size + ")";
            tvTitle.setText(count);
        } else {
            tvTitle.setText("Subscribed Tickets");
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_subscribed_tickets;
    }

    private void setUpRecyclerView(List<Tickets> ticketsList) {
        rvSubscribeTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvSubscribeTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
            ticketsAdapter = new TicketsAdapter(ticketsList, getContext(), rvSubscribeTickets);
            ticketsAdapter.setOnItemClickListener(ticket -> {
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

            ticketsAdapter.setOnUnsubscribeListener((id, pos) -> {
                unsubscribedTicketPos = pos;
                showUnsubscribeDialog(id);
            });

            rvSubscribeTickets.setAdapter(ticketsAdapter);
        } else {
            GlobalUtils.showLog(TAG, "data not found");
            rvSubscribeTickets.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
        }

  /*      rvSubscribeTickets.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fabSubscribe.isShown())
                    fabSubscribe.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && fabSubscribe != null)
                    fabSubscribe.show();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });*/
    }


    private void createExportBottomSheet() {
        exportBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View llBottomSheet = getLayoutInflater()
                .inflate(R.layout.bottom_sheet_export, null);

        exportBottomSheet.setContentView(llBottomSheet);

        RelativeLayout rlPdf = llBottomSheet.findViewById(R.id.rl_pdf);
        RelativeLayout rlExcel = llBottomSheet.findViewById(R.id.rl_excel);

        rlPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.export(etSearchText.getText().toString(), from, to,
                        getTicketState(statusValue), selectedPriority, selectedEmployee, selectedTicketType,
                        selectedTeam, selectedService, "SUBSCRIBED", "PDF");
                exportBottomSheet.dismiss();
            }
        });

        rlExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.export(etSearchText.getText().toString(), from, to,
                        getTicketState(statusValue), selectedPriority, selectedEmployee, selectedTicketType,
                        selectedTeam, selectedService, "SUBSCRIBED", "SPREADSHEET");
                exportBottomSheet.dismiss();
            }
        });

    }

    private void showUnsubscribeDialog(String ticketId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure you want to unsubscribe to this ticket?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    ticketsAdapter.closeSwipeLayout(ticketId);
                    dialog.dismiss();
                    presenter.unsubscribeTicket(Long.parseLong(ticketId));
                });

        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> {
                    ticketsAdapter.closeSwipeLayout(ticketId);
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

            presenter.getSubscribedTickets(true, 0,
                    System.currentTimeMillis(), 200);

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


 /*   @Override
    public void showSubscribeTickets(List<Tickets> subscribeTicketList) {
        setUpRecyclerView(subscribeTicketList);
    }*/

/*    @OnClick(R.id.fab_subscribe)
    void subscribe() {
        Intent i = new Intent(this, UnSubscribedTicketsActivity.class);
        startActivity(i);
    }*/

    @Override
    public void getSubscribedTicketsSuccess() {
        List<Tickets> subscribedTickets = TicketRepo.getInstance().getSubscribedTickets();
        setCount(subscribedTickets.size());
        setUpRecyclerView(subscribedTickets);
        Hawk.put(Constants.FETCH_SUBSCRIBED_LIST, false);
        fetchList = true;
    }


    @Override
    public void getSubscribedTicketsFail(String msg) {
        ivDataNotFound.setVisibility(View.VISIBLE);
        rvSubscribeTickets.setVisibility(View.GONE);
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        UiUtils.showSnackBar(getContext(), getWindow().getDecorView().getRootView(), "User don't have permission");

    }

    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Subscribed Tickets");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @Override
    public void onUnsubscribeSuccess(long ticketId) {
        ticketsAdapter.deleteItem(unsubscribedTicketPos, ticketId);
        Hawk.put(Constants.FETCH_SUBSCRIBEABLE_LIST, true);
        TicketRepo.getInstance().changeTicketTypeToSubscribable(ticketId);

        subscribedTickets = TicketRepo.getInstance().getSubscribedTickets();
        if (subscribedTickets.isEmpty()) {
            ivDataNotFound.setVisibility(View.VISIBLE);
            rvSubscribeTickets.setVisibility(View.GONE);
        } else {
            ivDataNotFound.setVisibility(View.GONE);
            rvSubscribeTickets.setVisibility(View.VISIBLE);
        }
    }

/*    @Override
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
        }
        return false;
    }*/

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

        /*    if (etEmployee.getText().toString().isEmpty()) {
                selectedEmployee = null;
            }*/

            if (etTicketType.getText().toString().isEmpty()) {
                selectedTicketType = null;
            }

            if (etTeam.getText().toString().isEmpty()) {
                selectedTeam = null;
            }

         /*   if (etService.getText().toString().isEmpty()) {
                selectedService = null;
            }*/

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

    @Override
    public void onResume() {
        super.onResume();
        boolean fetchChanges = Hawk.get(Constants.FETCH_SUBSCRIBED_LIST, false);
        if (fetchChanges) {
            GlobalUtils.showLog(TAG, "on resume fetch");
            presenter.getSubscribedTickets(true, 0, System.currentTimeMillis(), 200);
        } else {
            boolean ticketSubscribed = Hawk.get(Constants.TICKET_SUBSCRIBED, false);
            if (ticketSubscribed) {
                subscribedTickets = TicketRepo.getInstance().getSubscribedTickets();
                setUpRecyclerView(subscribedTickets);
                Hawk.put(Constants.TICKET_SUBSCRIBED, false);
            }
        }
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
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void onUnsubscribeFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }
        UiUtils.showSnackBar(getContext(), getWindow().getDecorView().getRootView(), msg);

    }

    @Override
    public void updateTickets(List<Tickets> ticketsList) {
        setCount(ticketsList.size());
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
    public void showProgressExport() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
        ivDataNotFound.setVisibility(View.GONE);
        rvSubscribeTickets.setVisibility(View.GONE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
//            rvSubscribeTickets.setVisibility(View.VISIBLE);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

    @OnClick(R.id.iv_back)
    public void goBack() {
        onBackPressed();
    }
}

