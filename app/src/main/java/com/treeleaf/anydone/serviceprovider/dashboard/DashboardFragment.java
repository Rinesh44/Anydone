package com.treeleaf.anydone.serviceprovider.dashboard;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.SearchServiceAdapter;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketStatByDate;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketStatByPriority;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketStatByResolvedTime;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketStatBySource;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketStatByStatus;
import com.treeleaf.anydone.serviceprovider.realm.repo.AvailableServicesRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketStatRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.DateUtils;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import io.realm.RealmList;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class DashboardFragment extends BaseFragment<DashboardPresenterImpl>
        implements DashboardContract.DashboardView {

    private static final String TAG = "DashboardFragment";
    public static final int[] STATUS_COLORS = {
            rgb("#F41803"), rgb("#3A5090"), rgb("#8808FF"), rgb("#0DED6E"),
            rgb("#117BFF")};
    public static final int[] PRIORITY_COLORS = {
            rgb("#F50000"), rgb("#FF7A00"), rgb("#FFC700"), rgb("#008F40"),
            rgb("#00DF63")};
    public static final int[] SOURCE_COLORS = {
            rgb("#3A5090"), rgb("#00C156"), rgb("#FBC400"), rgb("#FF2626")};

    private final String[] AXIS_HOURS = {"1 AM", "2 AM", "3 AM", "4 AM", "5 AM", "6 AM",
            "7 AM", "8 AM", "9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM",
            "5 PM", "6 PM", "7 PM", "8 PM ", "9 PM", "10 PM", "11 PM", "0 AM"};

    private final String[] AXIS_WEEK = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private final String[] AXIS_YEAR = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec"};

    @BindView(R.id.line_chart)
    LineChart lineChart;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_service)
    ImageView ivService;
    @BindView(R.id.tv_new_tickets)
    TextView tvNewTickets;
    @BindView(R.id.tv_resolved_tickets)
    TextView tvResolvedTickets;
    @BindView(R.id.tv_unresolved_tickets)
    TextView tvUnresolvedTickets;
    @BindView(R.id.tv_closed_tickets)
    TextView tvClosedTickets;
    @BindView(R.id.tv_reopened_tickets)
    TextView tvReopenedTickets;
    @BindView(R.id.tv_total_tickets)
    TextView tvTotalTickets;
    @BindView(R.id.tv_max)
    TextView tvMax;
    @BindView(R.id.tv_min)
    TextView tvMin;
    @BindView(R.id.tv_average)
    TextView tvAverage;
    @BindView(R.id.pie_chart_by_status)
    PieChart pieChartByStatus;
    @BindView(R.id.tv_started_value)
    TextView tvStartedValue;
    @BindView(R.id.tv_todo_value)
    TextView tvTodoValue;
    @BindView(R.id.tv_resolved_value)
    TextView tvResolvedValue;
    @BindView(R.id.tv_closed_value)
    TextView tvClosedValue;
    @BindView(R.id.tv_reopen_value)
    TextView tvReopenValue;
    @BindView(R.id.pie_chart_by_priority)
    PieChart pieChartByPriority;
    @BindView(R.id.tv_highest_value)
    TextView tvHighestValue;
    @BindView(R.id.tv_high_value)
    TextView tvHighValue;
    @BindView(R.id.tv_medium_value)
    TextView tvMediumValue;
    @BindView(R.id.tv_low_value)
    TextView tvLowValue;
    @BindView(R.id.tv_lowest_value)
    TextView tvLowestValue;
    @BindView(R.id.tv_third_party_value)
    TextView tvThirdPartyValue;
    @BindView(R.id.tv_third_party)
    TextView tvThirdParty;
    @BindView(R.id.tv_manual)
    TextView tvManual;
    @BindView(R.id.tv_phone_call)
    TextView tvPhoneCall;
    @BindView(R.id.tv_highest)
    TextView tvHighest;
    @BindView(R.id.tv_high)
    TextView tvHigh;
    @BindView(R.id.tv_medium)
    TextView tvMedium;
    @BindView(R.id.tv_low)
    TextView tvLow;
    @BindView(R.id.tv_lowest)
    TextView tvLowest;
    @BindView(R.id.tv_bot)
    TextView tvBot;
    @BindView(R.id.tv_started)
    TextView tvStarted;
    @BindView(R.id.tv_todo)
    TextView tvTodo;
    @BindView(R.id.tv_resolved)
    TextView tvResolved;
    @BindView(R.id.tv_closed)
    TextView tvClosed;
    @BindView(R.id.tv_reopen)
    TextView tvReopen;
    @BindView(R.id.tv_manual_value)
    TextView tvManualValue;
    @BindView(R.id.tv_phone_call_value)
    TextView tvPhoneCallValue;
    @BindView(R.id.tv_bot_value)
    TextView tvBotValue;
    @BindView(R.id.pie_chart_by_source)
    PieChart pieChartBySource;
    @BindView(R.id.pb_line_chart_progress)
    ProgressBar pbLineChart;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.tv_line_chart_not_available)
    TextView tvLineChartNotAvailable;
    @BindView(R.id.tv_pie_chart_priority_not_available)
    RelativeLayout tvPieChartPriorityNotAvailable;
    @BindView(R.id.tv_pie_chart_source_not_available)
    RelativeLayout tvPieChartSourceNotAvailable;
    @BindView(R.id.tv_pie_chart_status_not_available)
    RelativeLayout tvPieChartStatusNotAvailable;
    @BindView(R.id.tv_selection)
    TextView tvSelection;
    @BindView(R.id.tv_trends_selection)
    TextView tvTrendSelection;

    private BottomSheetDialog serviceBottomSheet;
    private RecyclerView rvServices;
    private String selectedServiceId;
    private SearchServiceAdapter adapter;
    private BottomSheetDialog filterBottomSheet;
    private MaterialButton btnSearch;
    private EditText etFromDate, etTillDate;
    private TextView tvReset;
    private AppCompatSpinner spTime;
    private long from, to;
    final Calendar myCalendar = Calendar.getInstance();
    private String trend = "past 30 days";
    private RadioGroup rgStatus;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String statusValue = null;
    private HorizontalScrollView hsvStatusContainer;


    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createServiceBottomSheet();
        createFilterBottomSheet();

        ivFilter.setOnClickListener(v -> toggleBottomSheet());

        boolean reFetchData = Hawk.get(Constants.REFETCH_TICKET_STAT, true);
        GlobalUtils.showLog(TAG, "check refetch: " + reFetchData);
        String selectedService = Hawk.get(Constants.SELECTED_SERVICE, "");
        if (reFetchData && !selectedService.isEmpty()) {
            pbLineChart.setVisibility(View.VISIBLE);
            presenter.getTicketByPriority();
            presenter.getTicketByResolveTime();
            presenter.getTicketBySource();
            presenter.getTicketByStatus();
            presenter.getTicketsByDate();
        } else {
            if (!reFetchData) {
                loadDataFromDb();
                pbLineChart.setVisibility(View.GONE);
            }
        }

        tvToolbarTitle.setOnClickListener(v -> {
            serviceBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            toggleServiceBottomSheet();
        });

        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh called");
                    trend = "past 30 days";
                    pbLineChart.setVisibility(View.VISIBLE);
                    lineChart.setVisibility(View.GONE);
                    tvLineChartNotAvailable.setVisibility(View.GONE);
                    Hawk.put(Constants.XA_XIS_TYPE, "MONTH");
                    presenter.getTicketByPriority();
                    presenter.getTicketByResolveTime();
                    presenter.getTicketBySource();
                    presenter.getTicketByStatus();
                    presenter.getTicketsByDate();

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        //Do something after 1 sec
                        if (swipeRefreshLayout != null)
                            swipeRefreshLayout.setRefreshing(false);
                    }, 1000);
                }
        );
    }

    private void loadDataFromDb() {
        getTicketByDateSuccess();
        getTicketByPrioritySuccess();
        getTicketBySourceSuccess();
        getTicketByStatusSuccess();
        getTicketByResolvedTimeSuccess();
    }

    public void toggleServiceBottomSheet() {
        if (serviceBottomSheet.isShowing()) {
            serviceBottomSheet.dismiss();
        } else {
            serviceBottomSheet.show();
        }
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

        serviceBottomSheet.setOnDismissListener(dialog -> {
            searchService.clearFocus();
            UiUtils.hideKeyboardForced(getActivity());
        });

        List<Service> serviceList = AvailableServicesRepo.getInstance().getAvailableServices();
        if (CollectionUtils.isEmpty(serviceList)) {
            presenter.getServices();
        } else {
            selectedServiceId = Hawk.get(Constants.SELECTED_SERVICE);
            if (selectedServiceId == null) {
                Service firstService = serviceList.get(0);
                tvToolbarTitle.setText(firstService.getName().replace("_", " "));

             /*   RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_browse_service)
                        .error(R.drawable.ic_browse_service);*/

                Glide.with(Objects.requireNonNull(getContext()))
                        .load(firstService.getServiceIconUrl())
                        .placeholder(R.drawable.ic_service_ph)
                        .error(R.drawable.ic_service_ph)
//                        .apply(options)
                        .into(ivService);
                Hawk.put(Constants.SELECTED_SERVICE, firstService.getServiceId());
            } else {
                Service selectedService = AvailableServicesRepo.getInstance()
                        .getAvailableServiceById(selectedServiceId);
                tvToolbarTitle.setText(selectedService.getName().replace("_", " "));

           /*     RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_browse_service)
                        .error(R.drawable.ic_browse_service);*/

                Glide.with(Objects.requireNonNull(getContext()))
                        .load(selectedService.getServiceIconUrl())
                        .placeholder(R.drawable.ic_service_ph)
                        .error(R.drawable.ic_service_ph)
//                        .apply(options)
                        .into(ivService);
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

    private void setUpRecyclerView(List<Service> serviceList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvServices.setLayoutManager(mLayoutManager);

        adapter = new SearchServiceAdapter(serviceList, getActivity());
        rvServices.setAdapter(adapter);

        adapter.setOnItemClickListener(service -> {
            hideKeyBoard();
            Hawk.put(Constants.SELECTED_SERVICE, service.getServiceId());
            Hawk.put(Constants.SERVICE_CHANGED_DASHBOARD, true);
            tvToolbarTitle.setText(service.getName().replace("_", " "));

       /*     RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_browse_service)
                    .error(R.drawable.ic_browse_service);*/

            Glide.with(Objects.requireNonNull(getContext()))
                    .load(service.getServiceIconUrl())
                    .placeholder(R.drawable.ic_service_ph)
                    .error(R.drawable.ic_service_ph)
//                    .apply(options)
                    .into(ivService);

            serviceBottomSheet.dismiss();

            lineChart.setVisibility(View.GONE);
            pbLineChart.setVisibility(View.VISIBLE);
            tvLineChartNotAvailable.setVisibility(View.GONE);
            presenter.getTicketByPriority();
            presenter.getTicketByResolveTime();
            presenter.getTicketBySource();
            presenter.getTicketByStatus();
            presenter.getTicketsByDate();
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
            Toast.makeText(getActivity(), "bottom sheet null", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("DefaultLocale")
    private void setUpPieChartByStatus(TicketStatByStatus ticketStatByStatus) {
        List<PieEntry> pieEntries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        float closedTickets = (float) ticketStatByStatus.getClosedTickets();
        float newTickets = (float) ticketStatByStatus.getNewTickets();
        float reopenedTickets = (float) ticketStatByStatus.getReOpenedTickets();
        float resolvedTickets = (float) ticketStatByStatus.getResolvedTickets();
        float unResolvedTickets = (float) ticketStatByStatus.getUnResolvedTickets();

        if (closedTickets > 0) {
            pieEntries.add(new PieEntry(closedTickets, String.format("%.0f", closedTickets)));
            colors.add(STATUS_COLORS[0]);
            tvClosed.setVisibility(View.VISIBLE);
            tvClosedValue.setVisibility(View.VISIBLE);
        } else {
            tvClosed.setVisibility(View.GONE);
            tvClosedValue.setVisibility(View.GONE);
        }

        if (newTickets > 0) {
            pieEntries.add(new PieEntry(newTickets, String.format("%.0f", newTickets)));
            colors.add(STATUS_COLORS[1]);
            tvTodo.setVisibility(View.VISIBLE);
            tvTodoValue.setVisibility(View.VISIBLE);
        } else {
            tvTodo.setVisibility(View.GONE);
            tvTodoValue.setVisibility(View.GONE);
        }

        if (reopenedTickets > 0) {
            pieEntries.add(new PieEntry(reopenedTickets, String.format("%.0f", reopenedTickets)));
            colors.add(STATUS_COLORS[2]);
            tvReopen.setVisibility(View.VISIBLE);
            tvReopenValue.setVisibility(View.VISIBLE);
        } else {
            tvReopen.setVisibility(View.GONE);
            tvReopenValue.setVisibility(View.GONE);
        }

        if (resolvedTickets > 0) {
            pieEntries.add(new PieEntry(resolvedTickets, String.format("%.0f", resolvedTickets)));
            colors.add(STATUS_COLORS[3]);
            tvResolved.setVisibility(View.VISIBLE);
            tvResolvedValue.setVisibility(View.VISIBLE);
        } else {
            tvResolved.setVisibility(View.GONE);
            tvResolvedValue.setVisibility(View.GONE);
        }

        if (unResolvedTickets > 0) {
            pieEntries.add(new PieEntry(unResolvedTickets, String.format("%.0f", unResolvedTickets)));
            colors.add(STATUS_COLORS[4]);
            tvStartedValue.setVisibility(View.VISIBLE);
            tvStarted.setVisibility(View.VISIBLE);
        } else {
            tvStartedValue.setVisibility(View.GONE);
            tvStarted.setVisibility(View.GONE);
        }

        if ((closedTickets == 0) && (newTickets == 0) && (reopenedTickets == 0) &&
                resolvedTickets == 0 && (unResolvedTickets == 0)) {
            tvPieChartStatusNotAvailable.setVisibility(View.VISIBLE);
        } else {
            tvPieChartStatusNotAvailable.setVisibility(View.GONE);
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "By status");

        pieDataSet.setSliceSpace(1.2f);
        pieDataSet.setColors(colors);
        pieDataSet.setValueLinePart1OffsetPercentage(100f);
        pieDataSet.setValueLinePart1Length(0.4f);
        pieDataSet.setValueLinePart2Length(0);
        pieDataSet.setValueTextColor(getResources().getColor(R.color.black));
        pieDataSet.setDrawValues(false);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData pieData = new PieData(pieDataSet);
        pieChartByStatus.setData(pieData);
        pieChartByStatus.animateY(1000);
        pieChartByStatus.setDrawEntryLabels(true);
        pieChartByStatus.getDescription().setEnabled(false);
        pieChartByStatus.setHoleRadius(74);
        pieChartByStatus.setEntryLabelTextSize(9);
        pieChartByStatus.setEntryLabelColor(getResources().getColor(R.color.charcoal_text));
        pieChartByStatus.getLegend().setEnabled(false);
        pieChartByStatus.setExtraBottomOffset(8.5f);
        pieChartByStatus.setExtraLeftOffset(7.5f);
        pieChartByStatus.setExtraRightOffset(7.5f);
        pieChartByStatus.setExtraTopOffset(7.5f);
//        pieChartByStatus.invalidate();
    }

    @SuppressLint("DefaultLocale")
    private void setUpPieChartByPriority(TicketStatByPriority ticketStatByPriority) {
        List<PieEntry> pieEntries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        float highest = (float) ticketStatByPriority.getHighest();
        float high = (float) ticketStatByPriority.getHigh();
        float medium = (float) ticketStatByPriority.getMedium();
        float low = (float) ticketStatByPriority.getLow();
        float lowest = (float) ticketStatByPriority.getLowest();

        if (highest > 0) {
            pieEntries.add(new PieEntry(highest, String.format("%.0f", highest)));
            colors.add(PRIORITY_COLORS[0]);
            tvHighestValue.setVisibility(View.VISIBLE);
            tvHighest.setVisibility(View.VISIBLE);
        } else {
            tvHighestValue.setVisibility(View.GONE);
            tvHighest.setVisibility(View.GONE);
        }

        if (high > 0) {
            pieEntries.add(new PieEntry(high, String.format("%.0f", high)));
            colors.add(PRIORITY_COLORS[1]);
            tvHigh.setVisibility(View.VISIBLE);
            tvHighValue.setVisibility(View.VISIBLE);
        } else {
            tvHigh.setVisibility(View.GONE);
            tvHighValue.setVisibility(View.GONE);
        }

        if (medium > 0) {
            pieEntries.add(new PieEntry(medium, String.format("%.0f", medium)));
            colors.add(PRIORITY_COLORS[2]);
            tvMedium.setVisibility(View.VISIBLE);
            tvMediumValue.setVisibility(View.VISIBLE);
        } else {
            tvMedium.setVisibility(View.GONE);
            tvMediumValue.setVisibility(View.GONE);
        }

        if (low > 0) {
            pieEntries.add(new PieEntry(low, String.format("%.0f", low)));
            colors.add(PRIORITY_COLORS[3]);
            tvLow.setVisibility(View.VISIBLE);
            tvLowValue.setVisibility(View.VISIBLE);
        } else {
            tvLow.setVisibility(View.GONE);
            tvLowValue.setVisibility(View.GONE);
        }

        if (lowest > 0) {
            pieEntries.add(new PieEntry(lowest, String.format("%.0f", lowest)));
            colors.add(PRIORITY_COLORS[4]);
            tvLowest.setVisibility(View.VISIBLE);
            tvLowestValue.setVisibility(View.VISIBLE);
        } else {
            tvLowest.setVisibility(View.GONE);
            tvLowestValue.setVisibility(View.GONE);
        }

        if ((highest == 0) && (high == 0) && (medium == 0) &&
                low == 0 && (lowest == 0)) {
            tvPieChartPriorityNotAvailable.setVisibility(View.VISIBLE);
        } else {
            tvPieChartPriorityNotAvailable.setVisibility(View.GONE);
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "By priority");
        pieDataSet.setSliceSpace(1.2f);
        pieDataSet.setColors(colors);
        pieDataSet.setValueLinePart1OffsetPercentage(100f);
        pieDataSet.setValueLinePart1Length(0.4f);
        pieDataSet.setValueLinePart2Length(0);
        pieDataSet.setValueTextColor(getResources().getColor(R.color.black));
        pieDataSet.setDrawValues(false);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData pieData = new PieData(pieDataSet);
        pieChartByPriority.setData(pieData);
//        pieData.setValueFormatter(new PieChartValueFormatter());
        pieChartByPriority.animateY(1000);
        pieChartByPriority.getDescription().setEnabled(false);
        pieChartByPriority.setHoleRadius(74);
        pieChartByPriority.setDrawEntryLabels(true);
        pieChartByPriority.setEntryLabelTextSize(9);
        pieChartByPriority.setEntryLabelColor(getResources().getColor(R.color.charcoal_text));
        pieChartByPriority.getLegend().setEnabled(false);
        pieChartByPriority.setExtraBottomOffset(8.5f);
        pieChartByPriority.setExtraLeftOffset(7.5f);
        pieChartByPriority.setExtraRightOffset(7.5f);
        pieChartByPriority.setExtraTopOffset(7.5f);
        pieChartByPriority.invalidate();
    }

    @SuppressLint("DefaultLocale")
    private void setUpPieChartBySource(float thirdPartyPercent, float manualPercent,
                                       float phoneCallPercent, float botPercent) {
        List<PieEntry> pieEntries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        if (thirdPartyPercent > 0) {
            pieEntries.add(new PieEntry(thirdPartyPercent, String.format("%.0f",
                    thirdPartyPercent) + "%"));
            colors.add(SOURCE_COLORS[0]);
            tvThirdParty.setVisibility(View.VISIBLE);
            tvThirdPartyValue.setVisibility(View.VISIBLE);
        } else {
            tvThirdParty.setVisibility(View.GONE);
            tvThirdPartyValue.setVisibility(View.GONE);
        }

        if (manualPercent > 0) {
            pieEntries.add(new PieEntry(manualPercent, String.format("%.0f",
                    manualPercent) + "%"));
            colors.add(SOURCE_COLORS[1]);
            tvManual.setVisibility(View.VISIBLE);
            tvManualValue.setVisibility(View.VISIBLE);
        } else {
            tvManual.setVisibility(View.GONE);
            tvManualValue.setVisibility(View.GONE);
        }

        if (phoneCallPercent > 0) {
            pieEntries.add(new PieEntry(phoneCallPercent, String.format("%.0f",
                    phoneCallPercent) + "%"));
            colors.add(SOURCE_COLORS[2]);
            tvPhoneCall.setVisibility(View.VISIBLE);
            tvPhoneCallValue.setVisibility(View.VISIBLE);
        } else {
            tvPhoneCallValue.setVisibility(View.GONE);
            tvPhoneCall.setVisibility(View.GONE);
        }

        if (botPercent > 0) {
            pieEntries.add(new PieEntry(botPercent, String.format("%.0f", botPercent) + "%"));
            colors.add(SOURCE_COLORS[3]);
            tvBot.setVisibility(View.VISIBLE);
            tvBotValue.setVisibility(View.VISIBLE);
        } else {
            tvBot.setVisibility(View.GONE);
            tvBotValue.setVisibility(View.GONE);
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "By source");
        pieDataSet.setSliceSpace(1.2f);
        pieDataSet.setColors(colors);
        pieDataSet.setValueLinePart1OffsetPercentage(100f);
        pieDataSet.setValueLinePart1Length(0.4f);
        pieDataSet.setValueLinePart2Length(0);
        pieDataSet.setValueTextColor(getResources().getColor(R.color.black));
        pieDataSet.setDrawValues(false);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData pieData = new PieData(pieDataSet);
        pieChartBySource.setData(pieData);
        pieChartBySource.animateY(1000);
        pieChartBySource.getDescription().setEnabled(false);
        pieChartBySource.setHoleRadius(74);
        pieChartBySource.setDrawEntryLabels(true);
        pieChartBySource.setEntryLabelTextSize(9);
        pieChartBySource.setEntryLabelColor(getResources().getColor(R.color.charcoal_text));
        pieChartBySource.getLegend().setEnabled(false);
        pieChartBySource.setExtraBottomOffset(8.5f);
        pieChartBySource.setExtraLeftOffset(7.5f);
        pieChartBySource.setExtraRightOffset(7.5f);
        pieChartBySource.setExtraTopOffset(7.5f);
        pieChartBySource.invalidate();
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
    private void createFilterBottomSheet() {
        @SuppressLint("InflateParams") View statusView = getLayoutInflater()
                .inflate(R.layout.layout_commonly_used, null);
        rgStatus = statusView.findViewById(R.id.rg_status);
        RadioButton today = statusView.findViewById(R.id.btn_today);
        RadioButton yesterday = statusView.findViewById(R.id.btn_yesterday);
        RadioButton thisWeek = statusView.findViewById(R.id.btn_this_week);
        RadioButton lastWeek = statusView.findViewById(R.id.btn_last_week);
        RadioButton thisMonth = statusView.findViewById(R.id.btn_this_month);
        RadioButton lastMonth = statusView.findViewById(R.id.btn_last_month);
        RadioButton thisYear = statusView.findViewById(R.id.btn_this_year);
        RadioButton lastYear = statusView.findViewById(R.id.btn_last_year);


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

        today.setOnClickListener(v -> {
            trend = "TODAY";
            Hawk.put(Constants.XA_XIS_TYPE, "HOUR");
            from = DateUtils.getStartOfDay();
            to = DateUtils.getEndOfDay();

            GlobalUtils.showLog(TAG, "from: " + from);
            GlobalUtils.showLog(TAG, "to: " + to);
            etFromDate.setText(GlobalUtils.getDateTimeline(from));
            etTillDate.setText(GlobalUtils.getDateTimeline(to));
        });

        yesterday.setOnClickListener(v -> {
            trend = "YESTERDAY";
            Hawk.put(Constants.XA_XIS_TYPE, "HOUR");
            from = DateUtils.getStartOfDayYesterday();
            to = DateUtils.getEndOfDayYesterday();

            GlobalUtils.showLog(TAG, "from1: " + from);
            GlobalUtils.showLog(TAG, "to2: " + to);
            etFromDate.setText(GlobalUtils.getDateTimeline(from));
            etTillDate.setText(GlobalUtils.getDateTimeline(to));
        });

        thisWeek.setOnClickListener(v -> {
            trend = "THIS WEEK";
            Hawk.put(Constants.XA_XIS_TYPE, "WEEK");
            Calendar thisWeek1 = Calendar.getInstance();
            thisWeek1.set(Calendar.DAY_OF_WEEK, thisWeek1.getFirstDayOfWeek());
            from = DateUtils.getStartOfDay(thisWeek1);
            thisWeek1.set(Calendar.DAY_OF_WEEK, 7);
            to = DateUtils.getEndOfDay(thisWeek1);
            GlobalUtils.showLog(TAG, "weekFrom: " + from);
            GlobalUtils.showLog(TAG, "weekTo: " + to);
            etFromDate.setText(GlobalUtils.getDateTimeline(from));
            etTillDate.setText(GlobalUtils.getDateTimeline(to));
        });

        lastWeek.setOnClickListener(v -> {
            trend = "LAST WEEK";
            Hawk.put(Constants.XA_XIS_TYPE, "WEEK");
            Calendar lastWeek1 = Calendar.getInstance();
            lastWeek1.set(Calendar.DAY_OF_WEEK, lastWeek1.getFirstDayOfWeek());
            lastWeek1.add(Calendar.WEEK_OF_YEAR, -1);
            from = DateUtils.getStartOfDay(lastWeek1);
            lastWeek1.set(Calendar.DAY_OF_WEEK, 7);
            to = DateUtils.getEndOfDay(lastWeek1);
            GlobalUtils.showLog(TAG, "weekFrom1: " + from);
            GlobalUtils.showLog(TAG, "weekTo1: " + to);
            etFromDate.setText(GlobalUtils.getDateTimeline(from));
            etTillDate.setText(GlobalUtils.getDateTimeline(to));
        });

        thisMonth.setOnClickListener(v -> {
            trend = "THIS MONTH";
            Hawk.put(Constants.XA_XIS_TYPE, "MONTH");
            Calendar thisMonth1 = Calendar.getInstance();
            thisMonth1.set(Calendar.DAY_OF_MONTH, 1);
            from = DateUtils.getStartOfDay(thisMonth1);
            thisMonth1.set(Calendar.DAY_OF_MONTH, thisMonth1.getActualMaximum(Calendar.DAY_OF_MONTH));
            to = DateUtils.getEndOfDay(thisMonth1);
            GlobalUtils.showLog(TAG, "monthFrom: " + from);
            GlobalUtils.showLog(TAG, "monthTo: " + to);
            etFromDate.setText(GlobalUtils.getDateTimeline(from));
            etTillDate.setText(GlobalUtils.getDateTimeline(to));
        });

        lastMonth.setOnClickListener(v -> {
            trend = "LAST MONTH";
            Hawk.put(Constants.XA_XIS_TYPE, "MONTH");
            Calendar lastMonth1 = Calendar.getInstance();
            lastMonth1.set(Calendar.DAY_OF_MONTH, 1);
            lastMonth1.add(Calendar.MONTH, -1);
            from = DateUtils.getStartOfDay(lastMonth1);
            lastMonth1.set(Calendar.DAY_OF_MONTH, lastMonth1.getActualMaximum(Calendar.DAY_OF_MONTH));
            to = DateUtils.getEndOfDay(lastMonth1);
            GlobalUtils.showLog(TAG, "monthFrom: " + from);
            GlobalUtils.showLog(TAG, "monthTo: " + to);
            etFromDate.setText(GlobalUtils.getDateTimeline(from));
            etTillDate.setText(GlobalUtils.getDateTimeline(to));
        });

        thisYear.setOnClickListener(v -> {
            trend = "THIS YEAR";
            Hawk.put(Constants.XA_XIS_TYPE, "YEAR");
            Calendar thisYear1 = Calendar.getInstance();
            thisYear1.set(Calendar.DAY_OF_YEAR, 1);
            from = DateUtils.getStartOfDay(thisYear1);
            thisYear1.set(Calendar.DAY_OF_YEAR, thisYear1.getActualMaximum(Calendar.DAY_OF_YEAR));
            to = DateUtils.getEndOfDay(thisYear1);
            GlobalUtils.showLog(TAG, "yearFrom: " + from);
            GlobalUtils.showLog(TAG, "yearTo: " + to);
            etFromDate.setText(GlobalUtils.getDateTimeline(from));
            etTillDate.setText(GlobalUtils.getDateTimeline(to));
        });

        lastYear.setOnClickListener(v -> {
            trend = "LAST YEAR";
            Hawk.put(Constants.XA_XIS_TYPE, "YEAR");
            Calendar lastYear1 = Calendar.getInstance();
            lastYear1.set(Calendar.DAY_OF_YEAR, 1);
            lastYear1.add(Calendar.YEAR, -1);
            from = DateUtils.getStartOfDay(lastYear1);
            lastYear1.set(Calendar.DAY_OF_YEAR, lastYear1.getActualMaximum(Calendar.DAY_OF_YEAR));
            to = DateUtils.getEndOfDay(lastYear1);
            GlobalUtils.showLog(TAG, "yearFrom1: " + from);
            GlobalUtils.showLog(TAG, "yearTo1: " + to);
            etFromDate.setText(GlobalUtils.getDateTimeline(from));
            etTillDate.setText(GlobalUtils.getDateTimeline(to));
        });

        filterBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.layout_bottom_sheet_filter_stats, null);

        filterBottomSheet.setContentView(view);
        btnSearch = view.findViewById(R.id.btn_search);
        etFromDate = view.findViewById(R.id.et_from_date);
        etTillDate = view.findViewById(R.id.et_till_date);
        tvReset = view.findViewById(R.id.tv_reset);
        hsvStatusContainer = view.findViewById(R.id.hsv_status_container);

//        spTime = view.findViewById(R.id.sp_time);


//        createTimeSpinner();

        hsvStatusContainer.removeAllViews();
        hsvStatusContainer.addView(rgStatus);
        hsvStatusContainer.setVisibility(View.VISIBLE);

        DatePickerDialog.OnDateSetListener fromDateListener = (view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            myCalendar.set(year, month, dayOfMonth, 0, 0, 0);
            updateFromDate();

            Calendar calendarFromDate = Calendar.getInstance();
            String[] fromDateSeparated = etFromDate.getText().toString().trim().split("/");

            calendarFromDate.set(Integer.parseInt(fromDateSeparated[0]),
                    Integer.parseInt(fromDateSeparated[1]) - 1,
                    Integer.parseInt(fromDateSeparated[2]));
            calendarFromDate.set(year, month, dayOfMonth, 0, 0, 0);
            from = calendarFromDate.getTimeInMillis();
            GlobalUtils.showLog(TAG, "manual from: " + from);
        };

        DatePickerDialog.OnDateSetListener tillDateListener = (view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            myCalendar.set(year, month, dayOfMonth, 23, 59, 59);
            updateToDate();

            Calendar calendarTillDate = Calendar.getInstance();
            String[] tillDateSeparated = etTillDate.getText().toString().trim().split("/");

            calendarTillDate.set(Integer.parseInt(tillDateSeparated[0]),
                    Integer.parseInt(tillDateSeparated[1]) - 1,
                    Integer.parseInt(tillDateSeparated[2]));
            calendarTillDate.set(year, month, dayOfMonth, 23, 59, 59);
            to = calendarTillDate.getTimeInMillis();
            GlobalUtils.showLog(TAG, "manual to: " + to);
        };

        etFromDate.setOnClickListener(v -> {
            new DatePickerDialog(Objects.requireNonNull(getActivity()),
                    fromDateListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            spTime.setSelection(8);
            Hawk.put(Constants.XA_XIS_TYPE, "");
            Hawk.put(Constants.MANUAL_DATE, true);
        });

        etTillDate.setOnClickListener(v -> {
            new DatePickerDialog(Objects.requireNonNull(getActivity()),
                    tillDateListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            spTime.setSelection(8);
            Hawk.put(Constants.XA_XIS_TYPE, "");
            Hawk.put(Constants.MANUAL_DATE, true);
        });

        tvReset.setOnClickListener(v -> {
            toggleBottomSheet();
            etFromDate.setText("");
            etTillDate.setText("");
//            spTime.setSelection(8);

            resetStatus();
            pbLineChart.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.GONE);
            Hawk.put(Constants.XA_XIS_TYPE, "MONTH");
            trend = "past 30 days";
            presenter.getTicketByPriority();
            presenter.getTicketByResolveTime();
            presenter.getTicketBySource();
            presenter.getTicketByStatus();
            presenter.getTicketsByDate();
            hideKeyBoard();
        });

        btnSearch.setOnClickListener(v -> {
            String fromDate = etFromDate.getText().toString().trim();
            String tillDate = etTillDate.getText().toString().trim();

            if (!fromDate.isEmpty() && !tillDate.isEmpty()) {
                if (from > to) {
                    Toast.makeText(getContext(),
                            "please select end date greater than start date",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                GlobalUtils.showLog(TAG, "final from: " + from);
                GlobalUtils.showLog(TAG, "final to: " + to);

                lineChart.setVisibility(View.GONE);
                presenter.filterByDate(from, to);
                presenter.filterByPriority(from, to);
                presenter.filterByResolvedTime(from, to);
                presenter.filterBySource(from, to);
                presenter.filterByStatus(from, to);
                toggleBottomSheet();
            } else {
                Toast.makeText(getContext(), "Please enter dates", Toast.LENGTH_SHORT).show();
            }

        });
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

    private void createTimeSpinner() {
        String[] arraySpinner = new String[]{"Today", "Yesterday", "This week", "Last week",
                "This month", "Last month", "This year", "Last year", "Select"};

        final int listSize = arraySpinner.length - 1;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()),
                R.layout.layout_commonly_use, arraySpinner) {

            @Override
            public int getCount() {
                return listSize;
            }
        };

        spTime.setAdapter(adapter);
        spTime.setSelection(listSize);

        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Hawk.put(Constants.MANUAL_DATE, false);
                String selectedItem = arraySpinner[position];

                TextView textView = (TextView) spTime.getSelectedView();
                textView.setText(selectedItem);

                switch (selectedItem) {
                    case "Select":
                        etFromDate.setText("");
                        etTillDate.setText("");
                        break;

                    case "Today":
                        trend = "TODAY";
                        Hawk.put(Constants.XA_XIS_TYPE, "HOUR");
                        from = DateUtils.getStartOfDay();
                        to = DateUtils.getEndOfDay();

                        GlobalUtils.showLog(TAG, "from: " + from);
                        GlobalUtils.showLog(TAG, "to: " + to);
                        etFromDate.setText(GlobalUtils.getDateTimeline(from));
                        etTillDate.setText(GlobalUtils.getDateTimeline(to));
                        break;

                    case "Yesterday":
                        trend = "YESTERDAY";
                        Hawk.put(Constants.XA_XIS_TYPE, "HOUR");
                        from = DateUtils.getStartOfDayYesterday();
                        to = DateUtils.getEndOfDayYesterday();

                        GlobalUtils.showLog(TAG, "from1: " + from);
                        GlobalUtils.showLog(TAG, "to2: " + to);
                        etFromDate.setText(GlobalUtils.getDateTimeline(from));
                        etTillDate.setText(GlobalUtils.getDateTimeline(to));
                        break;

                    case "This week":
                        trend = "THIS WEEK";
                        Hawk.put(Constants.XA_XIS_TYPE, "WEEK");
                        Calendar thisWeek = Calendar.getInstance();
                        thisWeek.set(Calendar.DAY_OF_WEEK, thisWeek.getFirstDayOfWeek());
                        from = DateUtils.getStartOfDay(thisWeek);
                        thisWeek.set(Calendar.DAY_OF_WEEK, 7);
                        to = DateUtils.getEndOfDay(thisWeek);
                        GlobalUtils.showLog(TAG, "weekFrom: " + from);
                        GlobalUtils.showLog(TAG, "weekTo: " + to);
                        etFromDate.setText(GlobalUtils.getDateTimeline(from));
                        etTillDate.setText(GlobalUtils.getDateTimeline(to));
                        break;

                    case "Last week":
                        trend = "LAST WEEK";
                        Hawk.put(Constants.XA_XIS_TYPE, "WEEK");
                        Calendar lastWeek = Calendar.getInstance();
                        lastWeek.set(Calendar.DAY_OF_WEEK, lastWeek.getFirstDayOfWeek());
                        lastWeek.add(Calendar.WEEK_OF_YEAR, -1);
                        from = DateUtils.getStartOfDay(lastWeek);
                        lastWeek.set(Calendar.DAY_OF_WEEK, 7);
                        to = DateUtils.getEndOfDay(lastWeek);
                        GlobalUtils.showLog(TAG, "weekFrom1: " + from);
                        GlobalUtils.showLog(TAG, "weekTo1: " + to);
                        etFromDate.setText(GlobalUtils.getDateTimeline(from));
                        etTillDate.setText(GlobalUtils.getDateTimeline(to));
                        break;

                    case "This month":
                        trend = "THIS MONTH";
                        Hawk.put(Constants.XA_XIS_TYPE, "MONTH");
                        Calendar thisMonth = Calendar.getInstance();
                        thisMonth.set(Calendar.DAY_OF_MONTH, 1);
                        from = DateUtils.getStartOfDay(thisMonth);
                        thisMonth.set(Calendar.DAY_OF_MONTH, thisMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
                        to = DateUtils.getEndOfDay(thisMonth);
                        GlobalUtils.showLog(TAG, "monthFrom: " + from);
                        GlobalUtils.showLog(TAG, "monthTo: " + to);
                        etFromDate.setText(GlobalUtils.getDateTimeline(from));
                        etTillDate.setText(GlobalUtils.getDateTimeline(to));
                        break;

                    case "Last month":
                        trend = "LAST MONTH";
                        Hawk.put(Constants.XA_XIS_TYPE, "MONTH");
                        Calendar lastMonth = Calendar.getInstance();
                        lastMonth.set(Calendar.DAY_OF_MONTH, 1);
                        lastMonth.add(Calendar.MONTH, -1);
                        from = DateUtils.getStartOfDay(lastMonth);
                        lastMonth.set(Calendar.DAY_OF_MONTH, lastMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
                        to = DateUtils.getEndOfDay(lastMonth);
                        GlobalUtils.showLog(TAG, "monthFrom: " + from);
                        GlobalUtils.showLog(TAG, "monthTo: " + to);
                        etFromDate.setText(GlobalUtils.getDateTimeline(from));
                        etTillDate.setText(GlobalUtils.getDateTimeline(to));
                        break;

                    case "This year":
                        trend = "THIS YEAR";
                        Hawk.put(Constants.XA_XIS_TYPE, "YEAR");
                        Calendar thisYear = Calendar.getInstance();
                        thisYear.set(Calendar.DAY_OF_YEAR, 1);
                        from = DateUtils.getStartOfDay(thisYear);
                        thisYear.set(Calendar.DAY_OF_YEAR, thisYear.getActualMaximum(Calendar.DAY_OF_YEAR));
                        to = DateUtils.getEndOfDay(thisYear);
                        GlobalUtils.showLog(TAG, "yearFrom: " + from);
                        GlobalUtils.showLog(TAG, "yearTo: " + to);
                        etFromDate.setText(GlobalUtils.getDateTimeline(from));
                        etTillDate.setText(GlobalUtils.getDateTimeline(to));
                        break;


                    case "Last year":
                        trend = "LAST YEAR";
                        Hawk.put(Constants.XA_XIS_TYPE, "YEAR");
                        Calendar lastYear = Calendar.getInstance();
                        lastYear.set(Calendar.DAY_OF_YEAR, 1);
                        lastYear.add(Calendar.YEAR, -1);
                        from = DateUtils.getStartOfDay(lastYear);
                        lastYear.set(Calendar.DAY_OF_YEAR, lastYear.getActualMaximum(Calendar.DAY_OF_YEAR));
                        to = DateUtils.getEndOfDay(lastYear);
                        GlobalUtils.showLog(TAG, "yearFrom1: " + from);
                        GlobalUtils.showLog(TAG, "yearTo1: " + to);
                        etFromDate.setText(GlobalUtils.getDateTimeline(from));
                        etTillDate.setText(GlobalUtils.getDateTimeline(to));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
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

    private void setUpLineChart(RealmList<TicketStatByStatus> ticketStatByStatusList,
                                String xAXisType) {

        ArrayList<Entry> newTickets = new ArrayList<>();
        ArrayList<Entry> resolvedTickets = new ArrayList<>();
        ArrayList<Entry> closedTickets = new ArrayList<>();

        ArrayList<String> dayInMonth = new ArrayList<>();

        for (int i = 0; i < ticketStatByStatusList.size(); i++) {
            //inflating data in y-axis
            TicketStatByStatus ticketStatByStatus = ticketStatByStatusList.get(i);
            if (ticketStatByStatus != null) {
                int closedTicket = ticketStatByStatus.getClosedTickets();
                int resolvedTicket = ticketStatByStatus.getResolvedTickets();
                int newTicket = ticketStatByStatus.getNewTickets();

                boolean isManualDate = Hawk.get(Constants.MANUAL_DATE, false);
                if (!isManualDate) {
//                if (newTicket != 0)
                    newTickets.add(new Entry(i, newTicket));

//                if (resolvedTicket != 0)
                    resolvedTickets.add(new Entry(i, resolvedTicket));

//                if (closedTicket != 0)
                    closedTickets.add(new Entry(i, closedTicket));

                    long timeStamp = ticketStatByStatus.getTimestamp();
                    String date = getDateFromTimeStamp(timeStamp);
                    dayInMonth.add(date);
                } else {
                    // for manual selection implement different axis formatter, for this we'll have to assign timestamp to x
                    newTickets.add(new Entry(ticketStatByStatus.getTimestamp(), newTicket));
                    resolvedTickets.add(new Entry(ticketStatByStatus.getTimestamp(), resolvedTicket));
                    closedTickets.add(new Entry(ticketStatByStatus.getTimestamp(), closedTicket));
                }

            }

        }

        if ((closedTickets.isEmpty()) && (resolvedTickets.isEmpty()) && (newTickets.isEmpty())) {
            tvLineChartNotAvailable.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.GONE);
        } else {
            tvLineChartNotAvailable.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet newTicketDataSet = new LineDataSet(newTickets, "New Tickets");
        newTicketDataSet.setDrawCircles(false);
        newTicketDataSet.enableDashedLine(10f, 5f, 0f);
        newTicketDataSet.setDrawValues(false);
        newTicketDataSet.setColor(getResources().getColor(R.color.ticket_created_text));

        LineDataSet resolvedTicketDataSet = new LineDataSet(resolvedTickets, "Resolved Tickets");
        resolvedTicketDataSet.setDrawCircles(false);
        resolvedTicketDataSet.setDrawValues(false);
        resolvedTicketDataSet.enableDashedLine(10f, 5f, 0f);
        resolvedTicketDataSet.setColor(getResources().getColor(R.color.ticket_resolved_text));

        LineDataSet closedTicketDataSet = new LineDataSet(closedTickets, "Closed Tickets");
        closedTicketDataSet.setDrawCircles(false);
        closedTicketDataSet.setDrawValues(false);
        closedTicketDataSet.enableDashedLine(10f, 5f, 0f);
        closedTicketDataSet.setColor(getResources().getColor(R.color.ticket_closed_text));

        lineDataSets.add(newTicketDataSet);
        lineDataSets.add(resolvedTicketDataSet);
        lineDataSets.add(closedTicketDataSet);

/*        lineChart.getAxisLeft().setAxisMinimum(0f);
        lineChart.getAxisRight().setAxisMinimum(0f);*/
        boolean isManualSelection = Hawk.get(Constants.MANUAL_DATE, false);
        switch (xAXisType) {
            case "HOUR":
                lineChart.getXAxis().setLabelCount(12);
                lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(AXIS_HOURS));
                break;

            case "WEEK":
                lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(AXIS_WEEK));
                lineChart.getXAxis().setLabelCount(6);
                break;

            case "MONTH":
                if (isManualSelection) {
                    lineChart.getXAxis().setValueFormatter(
                            new ManualMonthFormatter(ticketStatByStatusList.size()));
                    GlobalUtils.showLog(TAG, "list size check: " + ticketStatByStatusList.size());
                } else {
                    lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dayInMonth));
                    lineChart.getXAxis().setLabelCount(10);
                }
                break;

            case "YEAR":
                if (isManualSelection) {
                    lineChart.getXAxis().setValueFormatter(
                            new ManualYearFormatter(ticketStatByStatusList.size()));
                } else {
                    lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(AXIS_YEAR));
                    lineChart.getXAxis().setLabelCount(12);
                }
                break;

        }

        lineChart.setData(new LineData(lineDataSets));
//        lineChart.setVisibleXRangeMaximum(65f);
        lineChart.setPinchZoom(false);
        lineChart.setScaleEnabled(false);
        lineChart.animateXY(500, 500);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setTextSize(8);
        lineChart.getAxis(YAxis.AxisDependency.LEFT).setTextSize(8);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.invalidate();

        lineChart.setVisibility(View.VISIBLE);
        pbLineChart.setVisibility(View.GONE);
    }

    private String getDateFromTimeStamp(long initialTimeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(initialTimeStamp);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String monthInWords = GlobalUtils.getMonth(month);
        if (!monthInWords.isEmpty()) {
            return day + monthInWords;
        }

        return "";
    }

    private String getMonthNameFromTimeStamp(long initialTimeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(initialTimeStamp);
        int month = calendar.get(Calendar.MONTH) + 1;
        return GlobalUtils.getMonth(month);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_dashboard;
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void showProgressBar(String message) {
        pbLineChart.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressBar() {
//        pbLineChart.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(getActivity(), Objects.requireNonNull(getActivity()).getWindow()
                .getDecorView().getRootView(), Constants.SERVER_ERROR);

        pbLineChart.setVisibility(View.GONE);
    }

    @Override
    public void getServicesSuccess() {
        List<Service> serviceList = AvailableServicesRepo.getInstance().getAvailableServices();
        Service firstService = serviceList.get(0);
        Hawk.put(Constants.SELECTED_SERVICE, firstService.getServiceId());
        GlobalUtils.showLog(TAG, "first service id saved");

        tvToolbarTitle.setText(firstService.getName().replace("_", " "));

     /*   RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_browse_service)
                .error(R.drawable.ic_browse_service);*/

        Glide.with(Objects.requireNonNull(getContext()))
                .load(firstService.getServiceIconUrl())
                .placeholder(R.drawable.ic_service_ph)
                .error(R.drawable.ic_service_ph)
//                .apply(options)
                .into(ivService);

        setUpRecyclerView(serviceList);

        presenter.getTicketByPriority();
        presenter.getTicketByResolveTime();
        presenter.getTicketBySource();
        presenter.getTicketByStatus();
        presenter.getTicketsByDate();
    }

    public void toggleBottomSheet() {
        if (filterBottomSheet.isShowing()) filterBottomSheet.dismiss();
        else {
            filterBottomSheet.show();
        }
    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager)
                Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
    }

    @Override
    public void getServicesFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        pbLineChart.setVisibility(View.GONE);
        tvLineChartNotAvailable.setVisibility(View.VISIBLE);
        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
    }

    @Override
    public void getTicketByDateSuccess() {
        boolean isManualSelection = Hawk.get(Constants.MANUAL_DATE, false);
        if (isManualSelection) {
            tvTrendSelection.setVisibility(View.GONE);
        } else {
            setTrendText();
        }
        Hawk.put(Constants.REFETCH_TICKET_STAT, false);
        tvLineChartNotAvailable.setVisibility(View.GONE);
        TicketStatByDate ticketStatByDate = TicketStatRepo.getInstance().getTicketStatByDate();
        GlobalUtils.showLog(TAG, "list count: " + ticketStatByDate.getTicketStatByStatusRealmList().size());
        if (!CollectionUtils.isEmpty(ticketStatByDate.getTicketStatByStatusRealmList())) {
            String xAXisType = Hawk.get(Constants.XA_XIS_TYPE, "MONTH");
            if (xAXisType.equalsIgnoreCase("WEEK")) {
                lineChart.setVisibility(View.VISIBLE);
                setUpLineChart(ticketStatByDate.getTicketStatByStatusRealmList(), xAXisType);
            } else {
                String responseType = Objects.requireNonNull(
                        ticketStatByDate.getTicketStatByStatusRealmList().get(0)).getStatType();
                String xAxisType = getAxisType(responseType);
                if (!xAxisType.isEmpty()) {
                    lineChart.setVisibility(View.VISIBLE);
                    setUpLineChart(ticketStatByDate.getTicketStatByStatusRealmList(), xAxisType);
                }
            }
        } else {
            pbLineChart.setVisibility(View.GONE);
            tvLineChartNotAvailable.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.GONE);
        }
    }

    private String getAxisType(String responseType) {
        switch (responseType) {
            case "1":
                return "HOUR";

            case "2":
                return "MONTH";

            case "3":
                return "YEAR";
        }

        return "";
    }

    private void setTrendText() {
        StringBuilder trendTextBuilder = new StringBuilder("of ");
        trendTextBuilder.append(trend.toLowerCase());
        tvTrendSelection.setText(trendTextBuilder);
        tvTrendSelection.setVisibility(View.VISIBLE);
    }

    @Override
    public void getTicketByDateFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        GlobalUtils.showLog(TAG, "its date");
        pbLineChart.setVisibility(View.GONE);
        tvLineChartNotAvailable.setVisibility(View.VISIBLE);
     /*   UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);*/
    }

    @Override
    public void getTicketByStatusSuccess() {
        tvPieChartStatusNotAvailable.setVisibility(View.GONE);
        TicketStatByStatus ticketStatByStatus = TicketStatRepo.getInstance().getTicketStatByStatus();
        tvNewTickets.setText(String.valueOf(ticketStatByStatus.getNewTickets()));
        tvResolvedTickets.setText(String.valueOf(ticketStatByStatus.getResolvedTickets()));
        tvUnresolvedTickets.setText(String.valueOf(ticketStatByStatus.getUnResolvedTickets()));
        tvClosedTickets.setText(String.valueOf(ticketStatByStatus.getClosedTickets()));
        tvReopenedTickets.setText(String.valueOf(ticketStatByStatus.getReOpenedTickets()));
        tvTotalTickets.setText(String.valueOf(ticketStatByStatus.getTotalTickets()));

        tvStartedValue.setText(String.valueOf(ticketStatByStatus.getUnResolvedTickets()));
        tvTodoValue.setText(String.valueOf(ticketStatByStatus.getNewTickets()));
        tvResolvedValue.setText(String.valueOf(ticketStatByStatus.getResolvedTickets()));
        tvReopenValue.setText(String.valueOf(ticketStatByStatus.getReOpenedTickets()));
        tvClosedValue.setText(String.valueOf(ticketStatByStatus.getClosedTickets()));
        setUpPieChartByStatus(ticketStatByStatus);
    }

    @Override
    public void getTicketByStatusFail(String msg) {
        if (msg != null && msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        GlobalUtils.showLog(TAG, "its status");
      /*  UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);*/
    }

    @Override
    public void getTicketByPrioritySuccess() {
        TicketStatByPriority ticketStatByPriority = TicketStatRepo.getInstance().getTicketStatByPriority();
        if (ticketStatByPriority != null) {
            tvHighestValue.setText(String.valueOf(ticketStatByPriority.getHighest()));// error here
            tvHighValue.setText(String.valueOf(ticketStatByPriority.getHigh()));
            tvMediumValue.setText(String.valueOf(ticketStatByPriority.getMedium()));
            tvLowValue.setText(String.valueOf(ticketStatByPriority.getLow()));
            tvLowestValue.setText(String.valueOf(ticketStatByPriority.getLowest()));
            setUpPieChartByPriority(ticketStatByPriority);
        }
    }

    @Override
    public void getTicketByPriorityFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        GlobalUtils.showLog(TAG, "its priority");
     /*   UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);*/
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getTicketBySourceSuccess() {
        TicketStatBySource ticketStatBySource = TicketStatRepo.getInstance().getTicketStatBySource();
        float thirdParty = (float) ticketStatBySource.getThirdParty();
        float manual = (float) ticketStatBySource.getManual();
        float phoneCall = (float) ticketStatBySource.getPhoneCall();
        float bot = (float) ticketStatBySource.getBot();

        float total = thirdParty + manual + phoneCall + bot;
        float thirdPartyPercent = thirdParty / total * 100;
        float manualPercent = manual / total * 100;
        float phoneCallPercent = phoneCall / total * 100;
        float botPercent = bot / total * 100;

        tvThirdPartyValue.setText(Math.round(thirdPartyPercent) + "%");
        tvManualValue.setText(Math.round(manualPercent) + "%");
        tvPhoneCallValue.setText(Math.round(phoneCallPercent) + "%");
        tvBotValue.setText(Math.round(botPercent) + "%");

        if ((thirdParty == 0) && (manual == 0) && (phoneCall == 0) &&
                (bot == 0)) {
            tvPieChartSourceNotAvailable.setVisibility(View.VISIBLE);
            pieChartBySource.setVisibility(View.GONE);
        } else {
            pieChartBySource.setVisibility(View.VISIBLE);
            tvPieChartSourceNotAvailable.setVisibility(View.GONE);
            setUpPieChartBySource(thirdPartyPercent, manualPercent, phoneCallPercent, botPercent);
        }

    }

    @Override
    public void getTicketBySourceFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        GlobalUtils.showLog(TAG, "its source");
    /*    UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);*/
    }

    @Override
    public void getTicketByResolvedTimeSuccess() {
        TicketStatByResolvedTime ticketStatResolveTime = TicketStatRepo.getInstance()
                .getTicketStatByResolvedTime();
        if (ticketStatResolveTime != null) {
            if (ticketStatResolveTime.getMax() != 0)
                tvMax.setText(DateUtils.getElapsedTime(ticketStatResolveTime.getMax()));
            else
                tvMax.setText("N/A");
            if (ticketStatResolveTime.getAvg() != 0)
                tvAverage.setText(DateUtils.getElapsedTime(ticketStatResolveTime.getAvg()));
            else
                tvAverage.setText("N/A");
            if (ticketStatResolveTime.getMin() != 0)
                tvMin.setText(DateUtils.getElapsedTime
                        (ticketStatResolveTime.getMin()));
            else
                tvMin.setText("N/A");
        }
    }

    @Override
    public void getTicketByResolvedTimeFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        GlobalUtils.showLog(TAG, "its resolved time");
     /*   UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);*/
    }

    @Override
    public void onFilterByDateFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        GlobalUtils.showLog(TAG, "its date");
        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);

        pbLineChart.setVisibility(View.GONE);
    }

    @Override
    public void onFilterByStatusFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        GlobalUtils.showLog(TAG, "its status");
        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
    }

    @Override
    public void onFilterByPriorityFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        GlobalUtils.showLog(TAG, "its priority");
        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
    }

    @Override
    public void onFilterBySourceFail(String msg) {
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
    public void onFilterByResolvedTimeFail(String msg) {
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
    public void onResume() {
        super.onResume();

        String dashboardServiceId = Hawk.get(Constants.DASHBOARD_SERVICE_ID, "");
        String selectedServiceId = Hawk.get(Constants.SELECTED_SERVICE);
        boolean serviceChanged = !dashboardServiceId.equalsIgnoreCase(selectedServiceId);

        if (serviceChanged) {
            Hawk.put(Constants.DASHBOARD_SERVICE_ID, selectedServiceId);
            pbLineChart.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.GONE);
            presenter.getTicketByPriority();
            presenter.getTicketByResolveTime();
            presenter.getTicketBySource();
            presenter.getTicketByStatus();
            presenter.getTicketsByDate();

        }
    }

    private class ManualMonthFormatter extends ValueFormatter {
        private int count;

        public ManualMonthFormatter(int count) {
            this.count = count;
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            long timeStamp = (long) value;
            String date = getDateFromTimeStamp(timeStamp);
            axis.setLabelCount(Math.min(count, 10));
            return date;
        }
    }

    private class ManualYearFormatter extends ValueFormatter {
        private int count;

        public ManualYearFormatter(int count) {
            this.count = count;
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            long timeStamp = (long) value;
            axis.setLabelCount(count);
            return getMonthNameFromTimeStamp(timeStamp);
        }
    }

    private static class PieChartValueFormatter extends ValueFormatter {
        public PieChartValueFormatter() {
        }

        @SuppressLint("DefaultLocale")
        @Override
        public String getPieLabel(float value, PieEntry pieEntry) {
            String formattedValue = String.format("%.0f", value);
            GlobalUtils.showLog(TAG, "value check:" + formattedValue);
            if (formattedValue.equalsIgnoreCase("0")) {
                GlobalUtils.showLog(TAG, "true case");
                return "";
            }
            return String.valueOf(value);
        }

        /*   @SuppressLint("DefaultLocale")
        @Override
        public String getFormattedValue(float value) {
            String formattedValue = String.format("%.0f", value);
            GlobalUtils.showLog(TAG, "value check:" + formattedValue);
            if (formattedValue.equalsIgnoreCase("0")) {
                return "";
            }
            return String.valueOf(value);
        }*/
    }


}

