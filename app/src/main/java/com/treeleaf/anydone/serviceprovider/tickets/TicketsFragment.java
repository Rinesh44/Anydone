package com.treeleaf.anydone.serviceprovider.tickets;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.PriorityAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.SearchServiceAdapter;
import com.treeleaf.anydone.serviceprovider.addticket.AddTicketActivity;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.AvailableServicesRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.tickets.assignedtickets.AssignedTicketsFragment;
import com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets.ClosedTicketsFragment;
import com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets.OnTicketReopenListener;
import com.treeleaf.anydone.serviceprovider.tickets.subscribetickets.SubscribeTicketsFragment;
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

public class TicketsFragment extends BaseFragment<TicketsPresenterImpl>
        implements TicketsContract.TicketsView, OnTicketReopenListener {
    private static final String TAG = "ServiceRequestFragment";
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
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

    private List<Tickets> assignedTicketList;
    private List<Tickets> subscribedTicketList;
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
    private AutoCompleteTextView etSearchText;
    private TextView tvReset;
    final Calendar myCalendar = Calendar.getInstance();
    private AssignedListListener assignedListListener;
    private SubscribedListListener subscribedListListener;
    private ClosedListListener closedListListener;
    //    private BottomSheetBehavior sheetBehavior;
    private SearchServiceAdapter adapter;
    private RecyclerView rvServices;
    private Priority selectedPriority = new Priority("", -1);
    private TextView tvPriorityHint;
    private RelativeLayout rlPriorityHolder;
    private String selectedServiceId;

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

        presenter.findCustomers();
        presenter.findEmployees();
        presenter.findTags();

/*        sheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetShadow.setVisibility(View.GONE);
                    hideKeyBoard();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });*/

        assignedTicketList = TicketRepo.getInstance().getAssignedTickets();
        subscribedTicketList = TicketRepo.getInstance().getSubscribedTickets();
        closedTicketList = TicketRepo.getInstance().getClosedResolvedTickets();
        createServiceBottomSheet();
        createFilterBottomSheet();

        setupViewPager(mViewpager);
        mTabs.setupWithViewPager(mViewpager);

        tvToolbarTitle.setOnClickListener(v -> toggleServiceBottomSheet());
    }

    private void createServiceBottomSheet() {
        serviceBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View llBottomSheet = getLayoutInflater()
                .inflate(R.layout.bottomsheet_select_service, null);

        serviceBottomSheet.setContentView(llBottomSheet);

        serviceBottomSheet.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);
            setupSheetHeight(d, BottomSheetBehavior.STATE_HALF_EXPANDED);
        });

        EditText searchService = llBottomSheet.findViewById(R.id.et_search_service);
        rvServices = llBottomSheet.findViewById(R.id.rv_services);

        searchService.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setupSheetHeight(serviceBottomSheet, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        List<Service> serviceList = AvailableServicesRepo.getInstance().getAvailableServices();
        if (CollectionUtils.isEmpty(serviceList)) {
            presenter.getServices();
        } else {
            selectedServiceId = Hawk.get(Constants.SELECTED_SERVICE);
            if (selectedServiceId == null) {
                Service firstService = serviceList.get(0);
                tvToolbarTitle.setText(firstService.getName().replace("_", " "));
                Glide.with(Objects.requireNonNull(getContext())).load(firstService.getServiceIconUrl()).into(ivService);
                Hawk.put(Constants.SELECTED_SERVICE, firstService.getServiceId());
            } else {
                Service selectedService = AvailableServicesRepo.getInstance().getAvailableServiceById(selectedServiceId);
                tvToolbarTitle.setText(selectedService.getName().replace("_", " "));
                Glide.with(Objects.requireNonNull(getContext())).load(selectedService.getServiceIconUrl()).into(ivService);
            }
            setUpRecyclerView(serviceList);
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


    public void toggleServiceBottomSheet() {
     /*   if (sheetBehavior.getState() != BottomSheetBehavior.STATE_HALF_EXPANDED) {
            bottomSheetShadow.setVisibility(View.VISIBLE);
            sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        } else if (sheetBehavior.getState() == BottomSheetBehavior.STATE_HALF_EXPANDED) {
            bottomSheetShadow.setVisibility(View.GONE);
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            sheetBehavior.setPeekHeight(0);
            bottomSheetShadow.setVisibility(View.GONE);
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }*/

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
            tvToolbarTitle.setText(service.getName().replace("_", " "));
            Glide.with(getContext()).load(service.getServiceIconUrl()).into(ivService);
//            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            bottomSheetShadow.setVisibility(View.GONE);
            serviceBottomSheet.dismiss();

            if (assignedListListener != null) {
                GlobalUtils.showLog(TAG, "interface applied for assigned");
                assignedListListener.updateAssignedList();
            }

            if (subscribedListListener != null) {
                GlobalUtils.showLog(TAG, "interface applied for subscribed");
                subscribedListListener.updateSubscribedList();
            }

            if (closedListListener != null) {
                GlobalUtils.showLog(TAG, "interface applied for closed");
                closedListListener.updateClosedList();
            } else {
                Hawk.put(Constants.FETCH_CLOSED_LIST, true);
            }

            assignedTicketList = TicketRepo.getInstance().getAssignedTickets();
            subscribedTicketList = TicketRepo.getInstance().getSubscribedTickets();
            closedTicketList = TicketRepo.getInstance().getClosedResolvedTickets();

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
        hsvStatusContainer = view.findViewById(R.id.hsv_status_container);
        tvPriorityHint = view.findViewById(R.id.tv_priority_hint);

//        spPriority.setSelection(0);

    /*    filterBottomSheet.setOnShowListener((DialogInterface.OnShowListener) dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            setupFullHeight(d);
        });*/


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

        etFromDate.setOnClickListener(v -> new DatePickerDialog(getActivity(), fromDateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        etTillDate.setOnClickListener(v -> new DatePickerDialog(getActivity(), tillDateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        tvReset.setOnClickListener(v -> {
            toggleBottomSheet();
            etSearchText.setText("");
            etFromDate.setText("");
            etTillDate.setText("");
            resetStatus();
            hideKeyBoard();

            Hawk.put(Constants.SELECTED_TICKET_FILTER_STATUS, -1);

            if (mViewpager.getCurrentItem() == 0) {
                if (assignedListListener != null) {
                    assignedListListener.updateAssignedList(assignedTicketList);
                }
            } else if (mViewpager.getCurrentItem() == 1) {
                if (subscribedListListener != null) {
                    subscribedListListener.updateSubscribedList(subscribedTicketList);
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
                        Integer.parseInt(fromDateSeparated[2]));
                calendarTillDate.set(Integer.parseInt(tillDateSeparated[0]),
                        Integer.parseInt(tillDateSeparated[1]) - 1,
                        Integer.parseInt(tillDateSeparated[2]));
                from = calendarFromDate.getTime().getTime();
                to = calendarTillDate.getTime().getTime();
            }

            Hawk.put(Constants.SELECTED_TICKET_FILTER_STATUS, rgStatus.getCheckedRadioButtonId());
            if (mViewpager.getCurrentItem() == 0) {
                presenter.filterAssignedTickets(etSearchText.getText().toString(), from, to,
                        getTicketState(statusValue), selectedPriority);
            } else if (mViewpager.getCurrentItem() == 1) {
                presenter.filterSubscribedTickets(etSearchText.getText().toString(), from, to,
                        getTicketState(statusValue), selectedPriority);
            } else {
                GlobalUtils.showLog(TAG, "get ticket status check: " + getTicketState(statusValue));
                presenter.filterClosedTickets(etSearchText.getText().toString(), from, to,
                        getTicketState(statusValue), selectedPriority);
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
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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
        Intent i = new Intent(getActivity(), AddTicketActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.iv_filter)
    void filterRequests() {
        statusValue = null;
        int fragmentIndex = mViewpager.getCurrentItem();
        if (fragmentIndex == 0) {
            if (!CollectionUtils.isEmpty(assignedTicketList)) {
                GlobalUtils.showLog(TAG, "assigned list check: " + assignedTicketList);
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
                toggleBottomSheet();
            } else {
                Toast.makeText(getActivity(), "No list found to filter", Toast.LENGTH_SHORT).show();
            }
        } else if (fragmentIndex == 1) {
            if (!CollectionUtils.isEmpty(subscribedTicketList)) {
                GlobalUtils.showLog(TAG, "subscribed list check: " + subscribedTicketList);
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
                    statusValue = selectedRadioButton.getText().toString().trim().toUpperCase();
                });

                hsvStatusContainer.removeAllViews();
                hsvStatusContainer.addView(rgStatus);
                toggleBottomSheet();
            } else {
                Toast.makeText(getActivity(), "No list found to filter", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!CollectionUtils.isEmpty(closedTicketList)) {
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
                toggleBottomSheet();
            } else {
                Toast.makeText(getActivity(), "No list found to filter", Toast.LENGTH_SHORT).show();
            }
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

        boolean serviceChanged = Hawk.get(Constants.SERVICE_CHANGED_THREAD, false);
        GlobalUtils.showLog(TAG, "service changed Check: " + serviceChanged);
        if (serviceChanged) {
            if (assignedListListener != null) {
                GlobalUtils.showLog(TAG, "interface applied for assigned");
                assignedListListener.updateAssignedList();
            } else {
                Hawk.put(Constants.FETCH__ASSIGNED_LIST, true);
            }

            if (subscribedListListener != null) {
                GlobalUtils.showLog(TAG, "interface applied for subscribed");
                subscribedListListener.updateSubscribedList();
            } else {
                Hawk.put(Constants.FETCH_SUBSCRIBED_LIST, true);
            }

            if (closedListListener != null) {
                GlobalUtils.showLog(TAG, "interface applied for closed");
                closedListListener.updateClosedList();
            } else {
                Hawk.put(Constants.FETCH_CLOSED_LIST, true);
            }

            Hawk.put(Constants.SERVICE_CHANGED_THREAD, false);
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
                message);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new AssignedTicketsFragment(), "Assigned");
        viewPagerAdapter.addFragment(new SubscribeTicketsFragment(), "Subscribed");
        viewPagerAdapter.addFragment(new ClosedTicketsFragment(), "Closed/Resolved");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void updateAssignedTicketList(List<Tickets> ticketsList) {
        if (assignedListListener != null) {
            assignedListListener.updateAssignedList(ticketsList);
        } else {
            GlobalUtils.showLog(TAG, "assigned list listener null");
        }
    }

    @Override
    public void filterAssignedTicketsFailed(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
        List<Tickets> emptyList = new ArrayList<>();
        assignedListListener.updateAssignedList(emptyList);
    }

    @Override
    public void updateSubscribedTicketList(List<Tickets> ticketsList) {
        if (subscribedListListener != null) {
            subscribedListListener.updateSubscribedList(ticketsList);
        } else {
            GlobalUtils.showLog(TAG, "subscribed list listener null");
        }
    }

    @Override
    public void filterSubscribedTicketFailed(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
        List<Tickets> emptyList = new ArrayList<>();
        subscribedListListener.updateSubscribedList(emptyList);
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
        List<Tickets> emptyList = new ArrayList<>();
//        closedListListener.updateClosedList(emptyList);
    }

    @Override
    public void getServiceSuccess() {
        List<Service> serviceList = AvailableServicesRepo.getInstance().getAvailableServices();
        Service firstService = serviceList.get(0);
        Hawk.put(Constants.SELECTED_SERVICE, firstService.getServiceId());
        GlobalUtils.showLog(TAG, "first service id saved");

        if (assignedListListener != null) {
            assignedListListener.fetchList();
        }

        if (subscribedListListener != null) {
            subscribedListListener.fetchList();
        }

        if (closedListListener != null) {
            closedListListener.fetchList();
        }

        tvToolbarTitle.setText(firstService.getName().replace("_", " "));
        Glide.with(Objects.requireNonNull(getContext())).load(firstService.getServiceIconUrl()).into(ivService);
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

    @Override
    public void findEmployeeSuccess() {

    }

    @Override
    public void findEmployeeFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }
        Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                getActivity(), Banner.ERROR, msg, Banner.TOP, 2000).show();

    }

    @Override
    public void findCustomerSuccess() {

    }

    @Override
    public void findCustomerFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }
        Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                getActivity(), Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void findTagsSuccess() {

    }

    @Override
    public void findTagsFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }
        Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                getActivity(), Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void ticketReopened() {
        GlobalUtils.showLog(TAG, "on ticket reopened interface implemented");
        if (assignedListListener != null) {
            assignedListListener.updateAssignedList(assignedTicketList);
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

    public interface AssignedListListener {
        void updateAssignedList(List<Tickets> ticketsList);

        void updateAssignedList();

        void fetchList();
    }

    public void setAssignedListListener(AssignedListListener listener) {
        assignedListListener = listener;
    }

    public interface SubscribedListListener {
        void updateSubscribedList(List<Tickets> ticketsList);

        void updateSubscribedList();

        void fetchList();
    }

    public void setSubscribedListListener(SubscribedListListener listener) {
        subscribedListListener = listener;
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

