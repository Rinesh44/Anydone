package com.treeleaf.anydone.serviceprovider.alltickets;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.EmployeeSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.PriorityAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.TicketsAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.AssignEmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.EmployeeRepo;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class AllTicketsActivity extends MvpBaseActivity<AllTicketPresenterImpl>
        implements AllTicketContract.AllTicketView {
    private static final String TAG = "ContributedTicketFragme";
    @BindView(R.id.rv_all_tickets)
    RecyclerView rvAllTickets;
    @BindView(R.id.swipe_refresh_all_tickets)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.iv_data_not_found)
    ImageView ivDataNotFound;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    @BindView(R.id.bottom_sheet)
    LinearLayout llBottomSheet;

    private TicketsAdapter ticketsAdapter;
    private List<Tickets> allTickets;
    private boolean fetchList = false;
    private Account userAccount;
    private String localAccountId;
    private BottomSheetDialog filterBottomSheet;
    private HorizontalScrollView hsvStatusContainer;
    private EditText etFromDate, etTillDate;
    private MaterialButton btnSearch;
    private AutoCompleteTextView etSearchText;
    private TextView tvReset;
    final Calendar myCalendar = Calendar.getInstance();
    String statusValue = null;
    private RadioGroup rgStatus;
    private AppCompatSpinner spPriority;
    private TextView tvPriorityHint;
    private Priority selectedPriority = new Priority("", -1);
    private int subscribeTicketPos;
    private int assignTicketPos;
    private BottomSheetDialog employeeBottomSheet;
    private String ticketId;
    private RecyclerView rvEmployee;
    private EmployeeSearchAdapter employeeSearchAdapter;
    private List<AssignEmployee> employeeList;
    private String selectedEmployeeId;
    private BottomSheetBehavior sheetBehavior;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userAccount = AccountRepo.getInstance().getAccount();
        localAccountId = userAccount.getAccountId();

        setToolbar();
        allTickets = TicketRepo.getInstance().getAllTickets();

        if (CollectionUtils.isEmpty(allTickets)) {
            ivDataNotFound.setVisibility(View.GONE);
            rvAllTickets.setVisibility(View.VISIBLE);
            presenter.getAllTickets(true, 0,
                    System.currentTimeMillis(), 100);
        } else {
            setUpRecyclerView(allTickets);
        }

        createFilterBottomSheet();
        sheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        createEmployeeBottomSheet();
        presenter.getEmployees();

        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh contributed called");

                    presenter.getAllTickets(false, 0,
                            System.currentTimeMillis(), 100);

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        //Do something after 1 setOnItemClickListener
                        if (swipeRefreshLayout != null)
                            swipeRefreshLayout.setRefreshing(false);
                    }, 1000);
                }
        );
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
        tvReset = view.findViewById(R.id.tv_reset);
        hsvStatusContainer = view.findViewById(R.id.hsv_status_container);
        spPriority = view.findViewById(R.id.sp_priority);
        tvPriorityHint = view.findViewById(R.id.tv_priority_hint);

        filterBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
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

        etFromDate.setOnClickListener(v -> new DatePickerDialog(this, fromDateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        etTillDate.setOnClickListener(v -> new DatePickerDialog(this, tillDateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        tvReset.setOnClickListener(v -> {
            toggleBottomSheet();
            etSearchText.setText("");
            etFromDate.setText("");
            etTillDate.setText("");
            resetStatus();
            selectedPriority = new Priority("", -1);
            tvPriorityHint = view.findViewById(R.id.tv_priority_hint);
            hideKeyBoard();

            List<Priority> priorityList = Collections.emptyList();
            PriorityAdapter adapter = new PriorityAdapter(this,
                    R.layout.layout_proirity, priorityList);
            spPriority.setAdapter(adapter);
            tvPriorityHint.setVisibility(View.VISIBLE);

            Hawk.put(Constants.SELECTED_TICKET_FILTER_STATUS, -1);
            List<Tickets> allTickets = TicketRepo.getInstance().getAllTickets();
            ticketsAdapter.setData(allTickets);
            ticketsAdapter.notifyDataSetChanged();
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
                        Integer.parseInt(fromDateSeparated[2]));
                calendarTillDate.set(Integer.parseInt(tillDateSeparated[0]),
                        Integer.parseInt(tillDateSeparated[1]) - 1,
                        Integer.parseInt(tillDateSeparated[2]));
                from = calendarFromDate.getTime().getTime();
                to = calendarTillDate.getTime().getTime();
            }

            Hawk.put(Constants.SELECTED_TICKET_FILTER_STATUS, rgStatus.getCheckedRadioButtonId());

            presenter.filterTickets(etSearchText.getText().toString(), from, to,
                    getTicketState(statusValue), selectedPriority);

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
    protected int getLayout() {
        return R.layout.activity_all_tickets;
    }

    private void setUpRecyclerView(List<Tickets> ticketsList) {
        rvAllTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvAllTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
            ticketsAdapter = new TicketsAdapter(ticketsList, getContext());

            ticketsAdapter.setOnSubscribeListener((id, pos) -> {
                subscribeTicketPos = pos;
                showSubscribeDialog(id);
            });


            ticketsAdapter.setOnAssignListener((id, pos) -> {
                assignTicketPos = pos;
                GlobalUtils.showLog(TAG, "assign ticket id check: " + id);
                ticketId = id;
                ticketsAdapter.closeSwipeLayout(id);
                employeeBottomSheet.show();
            });

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
                    i.putExtra("selected_ticket_type", Constants.CONTRIBUTED);
                    i.putExtra("ticket_desc", ticket.getTitle());
                    i.putExtra("selected_ticket_name", callees);
                    i.putExtra("selected_ticket_index", ticket.getTicketIndex());
                    i.putExtra("contributed", true);
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
            rvAllTickets.setAdapter(ticketsAdapter);
        } else {
            rvAllTickets.setVisibility(View.GONE);
//            final Handler handler = new Handler();

      /*      handler.postDelayed(() -> {
                if (rvOpenTickets != null) {
                    if (rvOpenTickets.getVisibility() != View.VISIBLE)
                        ivDataNotFound.setVisibility(View.VISIBLE);
                    else ivDataNotFound.setVisibility(View.GONE);
                }
            }, 50);*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    /*    boolean fetchChanges = Hawk.get(Constants.FETCH_CONTRIBUTED_LIST, false);
        if (fetchChanges) {
            presenter.getAllTickets(true, 0,
                    System.currentTimeMillis(), 100);
        } else {
            boolean ticketContributed = Hawk.get(Constants.TICKET_CONTRIBUTED, false);
            if (ticketContributed) {
                allTickets = TicketRepo.getInstance().getAllTickets();
                setUpRecyclerView(allTickets);
                Hawk.put(Constants.TICKET_CONTRIBUTED, false);
            }
        }*/
    }

    private void showSubscribeDialog(String ticketId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure you want to subscribe to this ticket?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    ticketsAdapter.closeSwipeLayout(ticketId);
                    dialog.dismiss();
                    presenter.subscribe(Long.parseLong(ticketId));
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

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void getAllTicketSuccess() {
        List<Tickets> allTickets = TicketRepo.getInstance().getAllTickets();
        setUpRecyclerView(allTickets);
     /*   Hawk.put(Constants.FETCH_CONTRIBUTED_LIST, false);
        fetchList = true;*/
    }

    @Override
    public void getAllTicketFail(String msg) {
        GlobalUtils.showLog(TAG, "failed to get all tickets");
        ivDataNotFound.setVisibility(View.VISIBLE);
        rvAllTickets.setVisibility(View.GONE);
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }
   /*     UiUtils.showSnackBar(getContext(), Objects.requireNonNull(getActivity()).getWindow()
                .getDecorView().getRootView(), msg);*/
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
        rvAllTickets.setVisibility(View.GONE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
            rvAllTickets.setVisibility(View.VISIBLE);
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

        SpannableStringBuilder str = new SpannableStringBuilder("All Tickets");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSubscribeSuccess(long ticketId) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                "Ticket" + "Subscribed");
        presenter.getAllTickets(true, 0,
                System.currentTimeMillis(), 100);
    }

    @Override
    public void onSubscribeFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
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
    public void assignSuccess() {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                "Employee assigned to ticket");
        presenter.getAllTickets(true, 0,
                System.currentTimeMillis(), 100);
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

    private void createEmployeeBottomSheet() {
        employeeBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View llBottomSheet = getLayoutInflater()
                .inflate(R.layout.bottomsheet_select_employee, null);

        employeeBottomSheet.setContentView(llBottomSheet);
        employeeBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        employeeBottomSheet.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
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

    @SuppressLint("ClickableViewAccessibility")
    private void setUpEmployeeRecyclerView(RecyclerView rvEmployeeAllUsers
    ) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvEmployeeAllUsers.setLayoutManager(mLayoutManager);

        GlobalUtils.showLog(TAG, "employee list: " + employeeList);
        employeeSearchAdapter = new EmployeeSearchAdapter(employeeList, this);
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

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
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

    private void showAssignTicketDialog(String ticketId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Assign ticket to employee?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    ticketsAdapter.closeSwipeLayout(ticketId);
                    dialog.dismiss();
                    employeeBottomSheet.dismiss();
                    GlobalUtils.showLog(TAG, "ticket Id check: " + ticketId);
                    GlobalUtils.showLog(TAG, "employee Id check: " + selectedEmployeeId);
                    presenter.assignTicket(Long.parseLong(ticketId), selectedEmployeeId);
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
}

