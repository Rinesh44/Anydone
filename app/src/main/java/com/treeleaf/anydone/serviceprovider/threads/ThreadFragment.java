package com.treeleaf.anydone.serviceprovider.threads;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.SearchServiceAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.ThreadAdapter;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttCallback;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttClient;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.Thread;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketSuggestion;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.AvailableServicesRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ThreadRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketSuggestionRepo;
import com.treeleaf.anydone.serviceprovider.threaddetails.ThreadDetailActivity;
import com.treeleaf.anydone.serviceprovider.threads.threadtabholder.ThreadHolderFragment;
import com.treeleaf.anydone.serviceprovider.tickets.TicketsFragment;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;
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

    private RecyclerView rvServices;
    //    private BottomSheetBehavior sheetBehavior;
    private SearchServiceAdapter adapter;
    private ThreadAdapter threadAdapter;
    private BottomSheetDialog serviceSheet;
    private List<Thread> threadList;
    private List<TicketSuggestion> ticketSuggestionList;
    private boolean dataLoaded = false;
    private long mLastClickTime = 0;

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
}
