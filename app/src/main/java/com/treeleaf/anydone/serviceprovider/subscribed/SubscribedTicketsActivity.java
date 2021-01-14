package com.treeleaf.anydone.serviceprovider.subscribed;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
import android.widget.TextView;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.EmployeeSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.PriorityAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.ServiceFilterAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.TagSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.TicketCategorySearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.TicketsAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
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
import com.treeleaf.anydone.serviceprovider.tickets.inprogresstickets.OnInProgressTicketsListener;
import com.treeleaf.anydone.serviceprovider.tickets.unsubscribedtickets.UnSubscribedTicketsActivity;
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
    @BindView(R.id.fab_subscribe)
    FloatingActionButton fabSubscribe;
    @BindView(R.id.pb_search)
    ProgressBar progressBar;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    private Unbinder unbinder;
    private OnInProgressTicketsListener onInProgressTicketsListener;
    private TicketsAdapter ticketsAdapter;
    private int unsubscribedTicketPos;
    private boolean fetchList = false;
    private List<Tickets> subscribedTickets;
    private Account userAccount;
    private String localAccountId;
    private BottomSheetDialog filterBottomSheet;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userAccount = AccountRepo.getInstance().getAccount();
        localAccountId = userAccount.getAccountId();

        setToolbar();
        subscribedTickets = TicketRepo.getInstance().getSubscribedTickets();

        if (CollectionUtils.isEmpty(subscribedTickets)) {
            GlobalUtils.showLog(TAG, "subscribe tickets empty");
            ivDataNotFound.setVisibility(View.GONE);
            rvSubscribeTickets.setVisibility(View.VISIBLE);
            presenter.getSubscribedTickets(true, 0, System.currentTimeMillis(), 100);
        } else {
            setUpRecyclerView(subscribedTickets);
        }

        createFilterBottomSheet();
        setUpEmployeeFilterData();
        setUpTicketTypeFilterData();
        setUpTeamFilterData();
        setUpServiceFilterData();


        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh subscribe called");

                    presenter.getSubscribedTickets(false, 0, System.currentTimeMillis(), 100);

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        //Do something after 1 sec
                        if (swipeRefreshLayout != null)
                            swipeRefreshLayout.setRefreshing(false);
                    }, 1000);
                }
        );
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

        rvSubscribeTickets.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
        });
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


    private void setUpServiceFilterData() {
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
    }


 /*   @Override
    public void showSubscribeTickets(List<Tickets> subscribeTicketList) {
        setUpRecyclerView(subscribeTicketList);
    }*/

    @OnClick(R.id.fab_subscribe)
    void subscribe() {
        Intent i = new Intent(this, UnSubscribedTicketsActivity.class);
        startActivity(i);
    }

    @Override
    public void getSubscribedTicketsSuccess() {
        List<Tickets> subscribedTickets = TicketRepo.getInstance().getSubscribedTickets();
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
//        UiUtils.showSnackBar(getContext(), getActivity().getWindow().getDecorView().getRootView(), msg);

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
        etService = view.findViewById(R.id.et_service);
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
            etService.setText("");
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

            if (etService.getText().toString().isEmpty()) {
                selectedService = null;
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

    @Override
    public void onResume() {
        super.onResume();
        boolean fetchChanges = Hawk.get(Constants.FETCH_SUBSCRIBED_LIST, false);
        if (fetchChanges) {
            GlobalUtils.showLog(TAG, "on resume fetch");
            presenter.getSubscribedTickets(true, 0, System.currentTimeMillis(), 100);
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
        rvSubscribeTickets.setVisibility(View.GONE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
            rvSubscribeTickets.setVisibility(View.VISIBLE);
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
}

