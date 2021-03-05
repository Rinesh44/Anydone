package com.treeleaf.anydone.serviceprovider.tickets;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.EmployeeSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.PriorityAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.SearchServiceAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.TagSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.TicketCategorySearchAdapter;
import com.treeleaf.anydone.serviceprovider.addticket.AddTicketActivity;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
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
import com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets.ClosedTicketsFragment;
import com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets.OnTicketReopenListener;
import com.treeleaf.anydone.serviceprovider.tickets.inprogresstickets.InProgressTicketsFragment;
import com.treeleaf.anydone.serviceprovider.tickets.pendingtickets.PendingTicketsFragment;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.NonSwipeableViewPager;
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

public class TicketsFragment extends BaseFragment<TicketsPresenterImpl>
        implements TicketsContract.TicketsView, OnTicketReopenListener {
    private static final String TAG = "ServiceRequestFragment";
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.viewpager)
    NonSwipeableViewPager mViewpager;
    /*    @BindView(R.id.bottom_sheet)
        LinearLayout llBottomSheet;*/
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.pb_search)
    ProgressBar pbSearch;
    @BindView(R.id.btn_add_ticket)
    MaterialButton btnAddTicket;
    @BindView(R.id.iv_service)
    ImageView ivService;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;

 /*   @BindView(R.id.shadow)
    View bottomSheetShadow;*/

    private List<Tickets> pendingTicketList;
    private List<Tickets> inProgressTicketList;
    private List<Tickets> closedTicketList;
    String statusValue = null;
    private RadioGroup rgStatus;
    private boolean filter = false;
    private BottomSheetDialog filterBottomSheet;
    private BottomSheetDialog serviceBottomSheet;
    private HorizontalScrollView hsvStatusContainer;
    private EditText etFromDate, etTillDate;
    private AppCompatSpinner spPriority;
    private MaterialButton btnSearch;
    private AutoCompleteTextView etEmployee;
    private AutoCompleteTextView etTeam;
    private AutoCompleteTextView etTicketType;
    private AutoCompleteTextView etSearchText;
    private AutoCompleteTextView etService;
    private TextView tvReset;
    final Calendar myCalendar = Calendar.getInstance();
    private PendingListListener pendingListListener;
    private InProgressListListener inProgressListListener;
    private ClosedListListener closedListListener;
    //    private BottomSheetBehavior sheetBehavior;
    private SearchServiceAdapter adapter;
    private RecyclerView rvServices;
    private Priority selectedPriority = new Priority("", -1);
    private TextView tvPriorityHint;
    private RelativeLayout rlPriorityHolder;
    private String selectedServiceId;
    TextView tvStatus;
    private long mLastClickTime = 0;
    TicketCategory selectedTicketType;
    Service selectedService;
    Tags selectedTeam;
    private LinearLayout llEmployeeSearchResult;
    private TextView tvEmployeeAsSelf;
    private TextView tvEmployeeTitle;
    private RecyclerView rvEmployeeResults;
    private AssignEmployee selectedEmployee;
    private CircleImageView civEmployeeAsSelf;
    private LinearLayout llEmployeeAsSelf;

    @Override
    protected int getLayout() {
        return R.layout.fragment_tickets;
    }

    public static TicketsFragment newInstance(String param1, String param2) {
        TicketsFragment fragment = new TicketsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager
                .LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        pendingTicketList = TicketRepo.getInstance().getPendingTickets();
        inProgressTicketList = TicketRepo.getInstance().getInProgressTickets();
        closedTicketList = TicketRepo.getInstance().getClosedResolvedTickets();

        presenter.getServices();
        createServiceBottomSheet();

        createFilterBottomSheet();
//        setUpServiceFilterData();

        setupViewPager(mViewpager);
        mTabs.setupWithViewPager(mViewpager);

        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                GlobalUtils.showLog(TAG, "tab position: " + tab.getPosition());
                boolean ticketPending = Hawk.get(Constants.TICKET_PENDING, false);
                boolean ticketInProgress = Hawk.get(Constants.TICKET_IN_PROGRESS, false);
                boolean ticketResolved = Hawk.get(Constants.TICKET_RESOLVED, false);
                if (tab.getPosition() == 0 && ticketPending) {
                    if (pendingListListener != null) {
                        GlobalUtils.showLog(TAG, "interface applied for pending");
                        pendingTicketList = TicketRepo.getInstance().getPendingTickets();
                        pendingListListener.updatePendingList(pendingTicketList);
                    }
                }

                if (tab.getPosition() == 1 && ticketInProgress) {
                    if (inProgressListListener != null) {
                        inProgressTicketList = TicketRepo.getInstance().getInProgressTickets();
                        inProgressListListener.updateInProgressList(inProgressTicketList);
                    }
                }

                if (tab.getPosition() == 2 && ticketResolved) {
                    if (closedListListener != null) {
                        closedTicketList = TicketRepo.getInstance().getClosedResolvedTickets();
                        closedListListener.updateClosedList(closedTicketList);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tvToolbarTitle.setOnClickListener(v -> {
            serviceBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            toggleServiceBottomSheet();
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
        if (!serviceList.isEmpty()) {
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
            }
            setUpRecyclerView(serviceList);


            if (pendingListListener != null) {
                pendingListListener.fetchList();
            }

            if (inProgressListListener != null) {
                inProgressListListener.fetchList();
            }

            if (closedListListener != null) {
                closedListListener.fetchList();
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

    }


    public void toggleServiceBottomSheet() {
        if (serviceBottomSheet.isShowing()) {
            serviceBottomSheet.dismiss();
        } else {
            serviceBottomSheet.show();
        }
    }

    private void setUpRecyclerView(List<Service> serviceList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvServices.setLayoutManager(mLayoutManager);

        adapter = new SearchServiceAdapter(serviceList, getActivity());
        rvServices.setAdapter(adapter);

        adapter.setOnItemClickListener(service -> {
            hideKeyBoard();
            Hawk.put(Constants.SELECTED_SERVICE, service.getServiceId());
            Hawk.put(Constants.SERVICE_CHANGED_TICKET, true);
            tvToolbarTitle.setText(service.getName().replace("_", " "));
            Glide.with(getContext()).load(service.getServiceIconUrl())
                    .placeholder(R.drawable.ic_service_ph)
                    .error(R.drawable.ic_service_ph)
                    .into(ivService);
//            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            bottomSheetShadow.setVisibility(View.GONE);
            serviceBottomSheet.dismiss();

            if (pendingListListener != null) {
                GlobalUtils.showLog(TAG, "interface applied for pending");
                pendingListListener.updatePendingList();
            }

            if (inProgressListListener != null) {
                GlobalUtils.showLog(TAG, "interface applied for in progress");
                inProgressListListener.updateInProgressList();
            }

            if (closedListListener != null) {
                GlobalUtils.showLog(TAG, "interface applied for closed");
                closedListListener.updateClosedList();
            } else {
                Hawk.put(Constants.FETCH_CLOSED_LIST, true);
            }

            pendingTicketList = TicketRepo.getInstance().getPendingTickets();
            inProgressTicketList = TicketRepo.getInstance().getInProgressTickets();
            closedTicketList = TicketRepo.getInstance().getClosedResolvedTickets();

//            presenter.findCustomers();
//            presenter.findEmployees();
//            presenter.findTicketTypes();
//            presenter.findTeams();

//            presenter.getLabels();

//            Hawk.put(Constants.FETCH_CLOSED_LIST, true);
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
//        etService = view.findViewById(R.id.et_service);
        llEmployeeSearchResult = view.findViewById(R.id.ll_employee_search_results);
        tvEmployeeAsSelf = view.findViewById(R.id.tv_employee_as_self);
        rvEmployeeResults = view.findViewById(R.id.rv_employee_results);
        civEmployeeAsSelf = view.findViewById(R.id.civ_employee_as_self);
        llEmployeeAsSelf = view.findViewById(R.id.ll_employee_as_self);
        tvEmployeeTitle = view.findViewById(R.id.tv_employee_title);

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
                PriorityAdapter adapter = new PriorityAdapter(getActivity(),
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

        etFromDate.setOnClickListener(v -> new DatePickerDialog(getActivity(), fromDateListener,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        etTillDate.setOnClickListener(v -> new DatePickerDialog(getActivity(), tillDateListener,
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

            if (mViewpager.getCurrentItem() == 0) {
                if (pendingListListener != null) {
                    pendingListListener.updatePendingList(pendingTicketList);
                }
            } else if (mViewpager.getCurrentItem() == 1) {
                if (inProgressListListener != null) {
                    inProgressListListener.updateInProgressList(inProgressTicketList);
                }
            } else {
                if (closedListListener != null) {
                    closedListListener.updateClosedList(closedTicketList);
                }
            }

            List<Priority> priorityList = Collections.emptyList();
            PriorityAdapter adapter = new PriorityAdapter(getActivity(),
                    R.layout.layout_proirity, priorityList);
            spPriority.setAdapter(adapter);
            tvPriorityHint.setVisibility(View.VISIBLE);

            selectedPriority = new Priority("", -1);
        });

        etSearchText.setOnItemClickListener((parent, v, position, id) -> hideKeyBoard());

        btnSearch.setOnClickListener(v -> {
            filter = true;
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

            if (rgStatus != null)
                Hawk.put(Constants.SELECTED_TICKET_FILTER_STATUS, rgStatus.getCheckedRadioButtonId());
            if (mViewpager.getCurrentItem() == 0) {
                presenter.filterPendingTickets(etSearchText.getText().toString(), from, to,
                        getTicketState(statusValue), selectedPriority, selectedEmployee, selectedTicketType,
                        selectedTeam, selectedService);
            } else if (mViewpager.getCurrentItem() == 1) {
                presenter.filterInProgressTickets(etSearchText.getText().toString(), from, to,
                        getTicketState(statusValue), selectedPriority, selectedEmployee, selectedTicketType,
                        selectedTeam, selectedService);
            } else {
                GlobalUtils.showLog(TAG, "get ticket status check: " + getTicketState(statusValue));
                presenter.filterClosedTickets(etSearchText.getText().toString(), from, to,
                        getTicketState(statusValue), selectedPriority, selectedEmployee, selectedTicketType,
                        selectedTeam, selectedService);
            }

            toggleBottomSheet();
        });


        etSearchText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyBoard();
            }
            return false;
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
            Toast.makeText(getActivity(), "bottom sheet null", Toast.LENGTH_SHORT).show();
        }
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
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

    @OnClick(R.id.btn_add_ticket)
    void addTicket() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        Intent i = new Intent(getActivity(), AddTicketActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.iv_filter)
    void filterRequests() {
        statusValue = null;
        int fragmentIndex = mViewpager.getCurrentItem();
        if (fragmentIndex == 0) {
            tvStatus.setVisibility(View.VISIBLE);
            pendingTicketList = TicketRepo.getInstance().getPendingTickets();
            GlobalUtils.showLog(TAG, "pending list check: " + pendingTicketList);
            @SuppressLint("InflateParams") View statusView = getLayoutInflater()
                    .inflate(R.layout.layout_status_buttons_assigned, null);
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
                statusValue = selectedRadioButton.getText().toString().trim().toUpperCase();
            });

            hsvStatusContainer.removeAllViews();
            hsvStatusContainer.addView(rgStatus);
            hsvStatusContainer.setVisibility(View.VISIBLE);
            toggleBottomSheet();
        } else if (fragmentIndex == 1) {
            hsvStatusContainer.removeAllViews();
            hsvStatusContainer.setVisibility(View.GONE);
            tvStatus.setVisibility(View.GONE);
            toggleBottomSheet();
        } else {
            tvStatus.setVisibility(View.VISIBLE);
            closedTicketList = TicketRepo.getInstance().getClosedResolvedTickets();
            GlobalUtils.showLog(TAG, "closed list check: " + closedTicketList);
            @SuppressLint("InflateParams") View statusView = getLayoutInflater()
                    .inflate(R.layout.layout_status_buttons_closed, null);
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
                statusValue = selectedRadioButton.getText().toString().trim().toUpperCase();
            });

            hsvStatusContainer.removeAllViews();
            hsvStatusContainer.addView(rgStatus);
            hsvStatusContainer.setVisibility(View.VISIBLE);
            toggleBottomSheet();
        }
        GlobalUtils.showLog(TAG, "fragment index: " + fragmentIndex);
    }


    private int getTicketState(String statusValue) {
        GlobalUtils.showLog(TAG, "check status value:" + statusValue);
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_filter, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        UiUtils.hideKeyboardForced(getContext());

        presenter.findEmployees();
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
        if (serviceId != null) {
            presenter.findTicketTypes();
            presenter.findTeams();
        }

//        boolean serviceChanged = Hawk.get(Constants.SERVICE_CHANGED_THREAD, false);
        String ticketServiceId = Hawk.get(Constants.TICKET_SERVICE_ID, "");
        String selectedServiceId = Hawk.get(Constants.SELECTED_SERVICE);
        boolean serviceChanged = !ticketServiceId.equalsIgnoreCase(selectedServiceId);
        GlobalUtils.showLog(TAG, "service changed Check: " + serviceChanged);
        if (serviceChanged) {
            if (pendingListListener != null) {
                GlobalUtils.showLog(TAG, "interface applied for pending");
                pendingListListener.updatePendingList();
            } else {
                Hawk.put(Constants.FETCH_PENDING_LIST, true);
            }

            if (inProgressListListener != null) {
                GlobalUtils.showLog(TAG, "interface applied for in progress");
                inProgressListListener.updateInProgressList();
            } else {
                Hawk.put(Constants.FETCH_IN_PROGRESS_LIST, true);
            }

            if (closedListListener != null) {
                GlobalUtils.showLog(TAG, "interface applied for closed");
                closedListListener.updateClosedList();
            } else {
                Hawk.put(Constants.FETCH_CLOSED_LIST, true);
            }

//            Hawk.put(Constants.SERVICE_CHANGED_THREAD, false);
            Hawk.put(Constants.TICKET_SERVICE_ID, selectedServiceId);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    /**
     * manually opening / closing bottom sheet on button click
     */
    public void toggleBottomSheet() {
        if (filterBottomSheet.isShowing()) filterBottomSheet.dismiss();
        else {
            int selectedRadioBtn = Hawk.get(Constants.SELECTED_TICKET_FILTER_STATUS, -1);
            if (selectedRadioBtn != -1 && rgStatus != null) {
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

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager)
                Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
    }

    @Override
    public void showProgressBar(String message) {
        pbSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (pbSearch != null) {
            pbSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                Constants.SERVER_ERROR);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new PendingTicketsFragment(), "Pending");
//        viewPagerAdapter.addFragment(new ContributedTicketFragment(), "Contributed");
        viewPagerAdapter.addFragment(new InProgressTicketsFragment(), "In Progress");
        viewPagerAdapter.addFragment(new ClosedTicketsFragment(), "Resolved");
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void updatePendingTicketList(List<Tickets> ticketsList) {
        if (pendingListListener != null) {
            pendingListListener.updatePendingList(ticketsList);
        } else {
            GlobalUtils.showLog(TAG, "pending list listener null");
        }
    }

    @Override
    public void filterPendingTicketsFailed(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
        List<Tickets> emptyList = new ArrayList<>();
        pendingListListener.updatePendingList(emptyList);
    }

    @Override
    public void updateInProgressTicketList(List<Tickets> ticketsList) {
        if (inProgressListListener != null) {
            inProgressListListener.updateInProgressList(ticketsList);
        } else {
            GlobalUtils.showLog(TAG, "in progress list listener null");
        }
    }

    @Override
    public void filterInProgressTicketFailed(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
        List<Tickets> emptyList = new ArrayList<>();
        inProgressListListener.updateInProgressList(emptyList);
    }

    @Override
    public void updateClosedTicketList(List<Tickets> ticketsList) {
        if (closedListListener != null) {
            closedListListener.updateClosedList(ticketsList);
        } else {
            GlobalUtils.showLog(TAG, "closed list listener null");
        }
    }

    @Override
    public void filterClosedTicketFailed(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
//        closedListListener.updateClosedList(emptyList);
    }

    @Override
    public void getServiceSuccess() {
        List<Service> serviceList = AvailableServicesRepo.getInstance().getAvailableServices();
        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
        Service firstService;
        if (selectedService != null) {
            firstService = AvailableServicesRepo.getInstance().getAvailableServiceById(selectedService);
            Hawk.put(Constants.SELECTED_SERVICE, firstService.getServiceId());
        } else {
            firstService = serviceList.get(0);
            Hawk.put(Constants.SELECTED_SERVICE, firstService.getServiceId());

            if (pendingListListener != null) {
                pendingListListener.fetchList();
            }

            if (inProgressListListener != null) {
                inProgressListListener.fetchList();
            }

            if (closedListListener != null) {
                closedListListener.fetchList();
            }
        }
        GlobalUtils.showLog(TAG, "first service id saved");

        tvToolbarTitle.setText(firstService.getName().replace("_", " "));
        Glide.with(Objects.requireNonNull(getContext()))
                .load(firstService.getServiceIconUrl())
                .placeholder(R.drawable.ic_service_ph)
                .error(R.drawable.ic_service_ph)
                .into(ivService);
        setUpRecyclerView(serviceList);
    }

    @Override
    public void getServiceFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void getEmployeeSuccess() {
        List<AssignEmployee> employeeList = AssignEmployeeRepo.getInstance().getAllAssignEmployees();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvEmployeeResults.setLayoutManager(mLayoutManager);
        EmployeeSearchAdapter employeeSearchAdapter = new EmployeeSearchAdapter(employeeList, getContext(), true);
        rvEmployeeResults.setAdapter(employeeSearchAdapter);

        rvEmployeeResults.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager)
                    Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
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

                        Glide.with(getContext())
                                .load(userAccount.getProfilePic())
                                .error(R.drawable.ic_empty_profile_holder_icon)
                                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                                .into(civEmployeeAsSelf);

                        tvEmployeeAsSelf.setOnClickListener(v1 -> {
                            selectedEmployee = AssignEmployeeRepo.getInstance()
                                    .getAssignedEmployeeByAccountId(userAccount.getAccountId());
                            etEmployee.setText(selectedEmployee.getName());
                            llEmployeeSearchResult.setVisibility(View.GONE);

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

    @Override
    public void getEmployeeFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
        }
    }

    @Override
    public void getTicketTypeSuccess() {
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
                Toast.makeText(getContext(), "Ticket Types not available", Toast.LENGTH_SHORT).show();
            }
        });

        etTicketType.setOnItemClickListener((parent, view, position, id) -> {
            selectedTicketType = ticketTypeList.get(position);
            GlobalUtils.showLog(TAG, "selected ticket type: " + selectedTicketType.getName());
        });

    }

    @Override
    public void getTicketTypeFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
        }
    }

    @Override
    public void getTeamSuccess() {
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
                Toast.makeText(getContext(), "Teams not available", Toast.LENGTH_SHORT).show();
            }
        });

        etTeam.setOnItemClickListener((parent, view, position, id) -> {
            selectedTeam = teamList.get(position);
            GlobalUtils.showLog(TAG, "selected team: " + selectedTeam.getLabel());
        });
    }

    @Override
    public void getTeamFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
        }
    }


    @Override
    public void ticketReopened() {
        GlobalUtils.showLog(TAG, "on ticket reopened interface implemented");
        if (pendingListListener != null) {
            pendingListListener.updatePendingList(pendingTicketList);
        } else {
            GlobalUtils.showLog(TAG, "assinged list listnner is null");
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public interface PendingListListener {
        void updatePendingList(List<Tickets> ticketsList);

        void updatePendingList();

        void fetchList();
    }

    public void setPendingListListener(PendingListListener listener) {
        pendingListListener = listener;
    }

    public interface InProgressListListener {
        void updateInProgressList(List<Tickets> ticketsList);

        void updateInProgressList();

        void fetchList();
    }

    public void setInProgressListListener(InProgressListListener listener) {
        inProgressListListener = listener;
    }

    public interface ClosedListListener {
        void updateClosedList(List<Tickets> ticketsList);

        void updateClosedList();

        void fetchList();
    }

    public void setClosedListListener(ClosedListListener listener) {
        closedListListener = listener;
    }
}

