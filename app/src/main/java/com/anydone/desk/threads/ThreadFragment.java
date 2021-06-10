package com.anydone.desk.threads;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anydone.desk.utils.DateUtils;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.RtcProto;
import com.anydone.desk.R;
import com.anydone.desk.adapters.SearchServiceAdapter;
import com.anydone.desk.adapters.ThreadAdapter;
import com.anydone.desk.base.fragment.BaseFragment;
import com.anydone.desk.injection.component.ApplicationComponent;
import com.anydone.desk.mqtt.TreeleafMqttCallback;
import com.anydone.desk.mqtt.TreeleafMqttClient;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.Thread;
import com.anydone.desk.realm.model.TicketSuggestion;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.realm.repo.ThreadRepo;
import com.anydone.desk.realm.repo.TicketSuggestionRepo;
import com.anydone.desk.threaddetails.ThreadDetailActivity;
import com.anydone.desk.threads.threadtabholder.ThreadHolderFragment;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.UiUtils;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class ThreadFragment extends BaseFragment<ThreadPresenterImpl>
        implements ThreadContract.ThreadView, TreeleafMqttClient.OnMQTTConnected,
        ThreadHolderFragment.ThreadListListener {
    private static final String TAG = "ThreadFragment";
    @BindView(R.id.rv_threads)
    RecyclerView rvThreads;
    /*    @BindView(R.id.bottom_sheet)
        LinearLayout llBottomSheet;*/
 /*   @BindView(R.id.shadow)
    View bottomSheetShadow;*/
    @BindView(R.id.pb_search)
    ProgressBar pbSearch;
    @BindView(R.id.iv_thread_not_found)
    ImageView ivThreadNotFound;
    @BindView(R.id.root)
    CoordinatorLayout root;
    @BindView(R.id.swipe_refresh_threads)
    SwipeRefreshLayout swipeRefreshLayout;
    /* @BindView(R.id.rl_ticket_suggestion)
     RelativeLayout rlTicketSuggestion;*/
 /*   @BindView(R.id.tv_suggested_ticket)
    TextView tvSuggestedTicket;
    @BindView(R.id.iv_close_ticket_suggestion)
    ImageView ivCloseTicketSuggestion;*/
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_reload)
    MaterialButton btnReload;
    @BindView(R.id.tv_connection_status)
    TextView tvConnectionStatus;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;

    private RecyclerView rvServices;
    //    private BottomSheetBehavior sheetBehavior;
    private SearchServiceAdapter adapter;
    private ThreadAdapter threadAdapter;
    private BottomSheetDialog serviceSheet;
    private List<Thread> threadList;
    private List<TicketSuggestion> ticketSuggestionList;
    private boolean dataLoaded = false;
    private long mLastClickTime = 0;
    private RadioGroup rgStatus;
    String statusValue = null;
    private EditText etFromDate, etTillDate;
    private long from, to;
    private BottomSheetDialog filterBottomSheet;
    private MaterialButton btnSearch;
    private TextView tvReset;
    private HorizontalScrollView fblStatusContainer;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected int getLayout() {
        return R.layout.fragment_thread;
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public static ThreadFragment newInstance(String param1, String param2) {
        ThreadFragment fragment = new ThreadFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThreadHolderFragment mFragment = (ThreadHolderFragment) getParentFragment();
        assert mFragment != null;
        mFragment.setThreadListListener(this);


    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager
                .LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
//        presenter.getTicketSuggestions();
        TreeleafMqttClient.setOnMqttConnectedListener(this);
        List<Thread> threadList = ThreadRepo.getInstance().getThreadsByServiceId(selectedService);
        if (!CollectionUtils.isEmpty(threadList)) {
            setUpThreadRecyclerView(threadList);
            rvThreads.setVisibility(View.VISIBLE);
            ivThreadNotFound.setVisibility(View.GONE);
            btnReload.setVisibility(View.GONE);
            etSearch.setVisibility(View.VISIBLE);
        } else presenter.getConversationThreads(true);


        createFilterBottomSheet();
        setDataToSuggestionView();

        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh threads called");

                    presenter.getConversationThreads(false);
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        //Do something after 1 sec
                        if (swipeRefreshLayout != null)
                            swipeRefreshLayout.setRefreshing(false);
                    }, 1000);
                }
        );

        try {
            if (TreeleafMqttClient.mqttClient.isConnected()) {
                listenConversationMessages();
            } else {
                tvConnectionStatus.setText("Reconnecting...");
                tvConnectionStatus.setVisibility(View.VISIBLE);

                GlobalUtils.showLog(TAG, "mqtt reconnecting");
                String env = Hawk.get(Constants.BASE_URL);
                boolean prodEnv = !env.equalsIgnoreCase(Constants.DEV_BASE_URL);
                GlobalUtils.showLog(TAG, "prod env check: " + prodEnv);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    TreeleafMqttClient.start(
                            Objects.requireNonNull(getActivity()).getApplicationContext(), prodEnv,
                            new TreeleafMqttCallback() {
                                @Override
                                public void messageArrived(String topic, MqttMessage message) {
                                    GlobalUtils.showLog(TAG, "mqtt topic: " + topic);
                                    GlobalUtils.showLog(TAG, "mqtt message: " + message);
                                }
                            });
                }
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Thread> searchResults = ThreadRepo.getInstance().searchThread(s.toString());
                if (searchResults.isEmpty()) {
                    ivThreadNotFound.setVisibility(View.VISIBLE);
                } else {
                    ivThreadNotFound.setVisibility(View.GONE);
                }
                threadAdapter.setData(searchResults);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivFilter.setOnClickListener(view -> toggleBottomSheet());

    }

/*    @OnClick(R.id.rl_ticket_suggestion)
    void onTicketSuggestionClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        Intent i = new Intent(getActivity(), TicketSuggestionActivity.class);
        startActivity(i);
    }*/

/*    @OnClick(R.id.iv_close_ticket_suggestion)
    void onTicketSuggestionClose() {
        rlTicketSuggestion.setVisibility(View.GONE);
    }*/


    @OnClick(R.id.btn_reload)
    void reload() {
        btnReload.setVisibility(View.GONE);
        ivThreadNotFound.setVisibility(View.GONE);
        presenter.getConversationThreads(true);
    }


    private void listenConversationMessages() throws MqttException {
        GlobalUtils.showLog(TAG, "listen convo");
        Account userAccount = AccountRepo.getInstance().getAccount();
//        Employee userAccount = EmployeeRepo.getInstance().getEmployee();
        if (userAccount != null) {
            String SUBSCRIBE_TOPIC = "anydone/rtc/relay/response/" + userAccount.getAccountId();

            GlobalUtils.showLog(TAG, "user Id: " + userAccount.getAccountId());
            //listen for conversation thread messages
            TreeleafMqttClient.subscribe(SUBSCRIBE_TOPIC, new TreeleafMqttCallback() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    GlobalUtils.showLog(TAG, "message arrived");
                    RtcProto.RelayResponse relayResponse = RtcProto.RelayResponse
                            .parseFrom(message.getPayload());

                    if (relayResponse.getResponseType().equals(RtcProto
                            .RelayResponse.RelayResponseType.RTC_MESSAGE_RESPONSE)) {
                        GlobalUtils.showLog(TAG, "message type text");
                        new Handler(Looper.getMainLooper()).post(() -> {
                            String threadId = relayResponse.getRtcMessage().getRefId();

                            if (threadList != null) {
                                for (Thread existingThread : threadList
                                ) {
                                    GlobalUtils.showLog(TAG, "inside for loop");
                                    if (existingThread.getThreadId().equalsIgnoreCase(threadId)) {
                                        GlobalUtils.showLog(TAG, "thread exists");
                                        updateThread(existingThread, relayResponse);
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }

    }


    private void updateThread(Thread thread,
                              RtcProto.RelayResponse relayResponse) {

        new Handler(Looper.getMainLooper()).post(() -> ThreadRepo.getInstance()
                .updateThread(thread,
                        System.currentTimeMillis(),
                        relayResponse.getRtcMessage().getSentAt(),
                        relayResponse.getRtcMessage().getText().getMessage(),
                        false,
                        new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                GlobalUtils.showLog(TAG, "thread updated");
                                String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
                                List<Thread> updatedThreadList = ThreadRepo.getInstance()
                                        .getThreadsByServiceId(serviceId);
                                threadAdapter.setData(updatedThreadList);
                            }

                            @Override
                            public void fail() {
                                GlobalUtils.showLog(TAG, "failed to update thread");
                            }
                        }));
    }


    private void setUpThreadRecyclerView(List<Thread> threadList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvThreads.setLayoutManager(mLayoutManager);

        threadAdapter = new ThreadAdapter(threadList, getActivity());
        rvThreads.setAdapter(threadAdapter);

        threadAdapter.setOnItemClickListener(thread -> {
            Intent i = new Intent(getContext(), ThreadDetailActivity.class);
            i.putExtra("thread_id", thread.getThreadId());
            i.putExtra("customer_name", thread.getCustomerName());
            i.putExtra("customer_img", thread.getCustomerImageUrl());

            ThreadRepo.getInstance().setSeenStatus(thread);
            startActivity(i);

            //clear text on search with proper UX
            Handler handler = new Handler();
            handler.postDelayed(() -> etSearch.getText().clear(), 2000);
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

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
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
        ivThreadNotFound.setVisibility(View.GONE);
        btnReload.setVisibility(View.GONE);
    }

    @Override
    public void showToastMessage(String message) {

    }


    @Override
    public void onResume() {
        super.onResume();
        boolean suggestionAccepted = Hawk.get(Constants.SUGGESTION_ACCEPTED, false);
        boolean suggestionRejected = Hawk.get(Constants.SUGGESTION_REJECTED, false);
        if (suggestionAccepted || suggestionRejected) {
            TicketSuggestionRepo.getInstance().deleteAllTicketSuggestions();
            presenter.getTicketSuggestions();
        }

        presenter.getConversationThreads(false);
  /*      boolean serviceChanged = Hawk.get(Constants.SERVICE_CHANGED_TICKET, false);
        if (serviceChanged) {
            presenter.getConversationThreads();
            Hawk.put(Constants.SERVICE_CHANGED_TICKET, false);
        }

*/
        try {
            if (TreeleafMqttClient.mqttClient.isConnected()) {
                listenConversationMessages();
            } else {
                tvConnectionStatus.setText("Reconnecting...");

                GlobalUtils.showLog(TAG, "mqtt reconnecting");
                String env = Hawk.get(Constants.BASE_URL);
                boolean prodEnv = !env.equalsIgnoreCase(Constants.DEV_BASE_URL);
                GlobalUtils.showLog(TAG, "prod env check: " + prodEnv);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    TreeleafMqttClient.start(
                            Objects.requireNonNull(getActivity()).getApplicationContext(), prodEnv,
                            new TreeleafMqttCallback() {
                                @Override
                                public void messageArrived(String topic, MqttMessage message) {
                                    GlobalUtils.showLog(TAG, "mqtt topic: " + topic);
                                    GlobalUtils.showLog(TAG, "mqtt message: " + message);
                                }
                            });
                }
            }
        } catch (MqttException e) {
            GlobalUtils.showLog(TAG, "check mqtt exception: " + e.toString());
            e.printStackTrace();
        }
//        presenter.getConversationThreads(false);
    }

    @Override
    public void hideProgressBar() {
        if (pbSearch != null) {
            pbSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String message) {
    /*    UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                Constants.SERVER_ERROR);*/

        ivThreadNotFound.setVisibility(View.VISIBLE);
        btnReload.setVisibility(View.VISIBLE);
    }

    @Override
    public void getConversationThreadSuccess() {
        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
        threadList = ThreadRepo.getInstance().getThreadsByServiceId(selectedService);
        setUpThreadRecyclerView(threadList);
        rvThreads.setVisibility(View.VISIBLE);
        if (!CollectionUtils.isEmpty(threadList)) {
            ivThreadNotFound.setVisibility(View.GONE);
            btnReload.setVisibility(View.GONE);
            etSearch.setVisibility(View.VISIBLE);
        } else etSearch.setVisibility(View.GONE);
    }

    @Override
    public void getConversationThreadFail(String msg) {
        if (msg != null && msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        ThreadRepo.getInstance().deleteAllThreads();

     /*   UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);*/
//        showCustomSnackBar(msg);
        rvThreads.setVisibility(View.GONE);
        etSearch.setVisibility(View.GONE);
        ivThreadNotFound.setVisibility(View.VISIBLE);
        btnReload.setVisibility(View.VISIBLE);
    }


    @Override
    public void getTicketSuggestionSuccess() {
        setDataToSuggestionView();
        dataLoaded = true;
        Hawk.put(Constants.SUGGESTION_REJECTED, false);
        Hawk.put(Constants.SUGGESTION_ACCEPTED, false);
    }

    private void setDataToSuggestionView() {
        ticketSuggestionList = TicketSuggestionRepo.getInstance().getAllTicketSuggestions();
        if (ticketSuggestionList != null && !ticketSuggestionList.isEmpty()) {
            int suggestionCount = ticketSuggestionList.size();
            StringBuilder suggestedTicketCount = new StringBuilder(String.valueOf(suggestionCount));
            if (suggestionCount > 1)
                suggestedTicketCount.append(" New Tickets");
            else suggestedTicketCount.append(" New Ticket");
//            tvSuggestedTicket.setText(suggestedTicketCount);
//            rlTicketSuggestion.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNoTicketSuggestion() {
        dataLoaded = true;
//        rlTicketSuggestion.setVisibility(View.GONE);
        TicketSuggestionRepo.getInstance().deleteAllTicketSuggestions();
    }

    @Override
    public void getTicketSuggestionFail(String msg) {
        dataLoaded = true;
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
        }

      /*  UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                Constants.SERVER_ERROR);*/
    }

    private void showCustomSnackBar(String msg) {
        Snackbar snack = Snackbar.make(root, Constants.SERVER_ERROR, Snackbar.LENGTH_LONG);
        snack.show();
    }

    @Override
    public void mqttConnected() {
        if (tvConnectionStatus != null)
            tvConnectionStatus.setText("Connected");
      /*  Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                getActivity(), Banner.SUCCESS, "Connected", Banner.TOP, 2000).show();*/

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (tvConnectionStatus != null) tvConnectionStatus.setVisibility(View.GONE);
        }, 2000);

        try {
            listenConversationMessages();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mqttNotConnected() {

    }

    @Override
    public void fetchList() {
        if (btnReload != null) btnReload.setVisibility(View.GONE);
        GlobalUtils.showLog(TAG, "fetch list called");
        presenter.getConversationThreads(true);
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
            from = DateUtils.getStartOfDay();
            to = DateUtils.getEndOfDay();

            GlobalUtils.showLog(TAG, "from: " + from);
            GlobalUtils.showLog(TAG, "to: " + to);
            etFromDate.setText(GlobalUtils.getDateTimeline(from));
            etTillDate.setText(GlobalUtils.getDateTimeline(to));
        });

        yesterday.setOnClickListener(v -> {
            from = DateUtils.getStartOfDayYesterday();
            to = DateUtils.getEndOfDayYesterday();

            GlobalUtils.showLog(TAG, "from1: " + from);
            GlobalUtils.showLog(TAG, "to2: " + to);
            etFromDate.setText(GlobalUtils.getDateTimeline(from));
            etTillDate.setText(GlobalUtils.getDateTimeline(to));
        });

        thisWeek.setOnClickListener(v -> {
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
                .inflate(R.layout.layout_bottom_sheet_filter_messages, null);

        filterBottomSheet.setContentView(view);
        btnSearch = view.findViewById(R.id.btn_search);
        etFromDate = view.findViewById(R.id.et_from_date);
        etTillDate = view.findViewById(R.id.et_till_date);
        tvReset = view.findViewById(R.id.tv_reset);
        fblStatusContainer = view.findViewById(R.id.fbl_status_container);

//        spTime = view.findViewById(R.id.sp_time);


//        createTimeSpinner();

        fblStatusContainer.removeAllViews();
        fblStatusContainer.addView(rgStatus);
        fblStatusContainer.setVisibility(View.VISIBLE);

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
            hideKeyBoard();
        });

        btnSearch.setOnClickListener(v -> {
            if (from > to) {
                Toast.makeText(getActivity(),
                        "please select end date greater than start date",
                        Toast.LENGTH_LONG).show();
                return;
            }

            GlobalUtils.showLog(TAG, "final from: " + from);
            GlobalUtils.showLog(TAG, "final to: " + to);

            String service = Hawk.get(Constants.SELECTED_SERVICE);
            toggleBottomSheet();
            if (service != null) {
                //todo call filter API
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
            filterBottomSheet.show();
        }
    }
}
