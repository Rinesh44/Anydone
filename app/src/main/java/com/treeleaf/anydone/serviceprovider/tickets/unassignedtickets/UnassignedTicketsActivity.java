package com.treeleaf.anydone.serviceprovider.tickets.unassignedtickets;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.EmployeeSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.PriorityAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.TicketsAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.AssignEmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.EmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

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
            presenter.getAssignableTickets(true, 0, System.currentTimeMillis(), 100);
        } else {
            setUpRecyclerView(assignableTickets);
        }

        createFilterBottomSheet();
        sheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        handleEmployeeBottomSheet();
        presenter.getEmployees();

        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh assignable called");

                    presenter.getAssignableTickets(false, 0, System.currentTimeMillis(), 100);

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        //Do something after 1 sec
                        if (swipeRefreshLayout != null)
                            swipeRefreshLayout.setRefreshing(false);
                    }, 1000);
                }
        );

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

        setSelfDetails();
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
            setUpRecyclerView(assignableTickets);
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

            presenter.filterAssignableTickets(etSearchText.getText().toString(), from, to,
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

    private void setUpEmployeeRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvEmployee.setLayoutManager(mLayoutManager);

        employeeSearchAdapter = new EmployeeSearchAdapter(employeeList, this);
        rvEmployee.setAdapter(employeeSearchAdapter);

        if (employeeSearchAdapter != null) {
            employeeSearchAdapter.setOnItemClickListener((employee) -> {
                hideKeyBoard();

                selectedEmployeeId = employee.getEmployeeId();
                searchEmployee.setText(employee.getName());
                searchEmployee.setSelection(employee.getName().length());
                svSearchEmployee.setVisibility(View.GONE);

              /*  CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) llBottomSheet.getLayoutParams();
                params.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT;
                llBottomSheet.setLayoutParams(params);*/
            });
        }
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
            adapter = new TicketsAdapter(ticketsList, getContext());
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
                toggleEmployeeBottomSheet();
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
        builder1.setMessage("Are you sure to assign to this employee?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    adapter.closeSwipeLayout(ticketId);
                    dialog.dismiss();
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
        } else {
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
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
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

        presenter.getAssignableTickets(true, 0, System.currentTimeMillis(), 100);
        Hawk.put(Constants.FETCH__ASSIGNED_LIST, true);
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
        setUpEmployeeRecyclerView();
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
}