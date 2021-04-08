package com.treeleaf.anydone.serviceprovider.threads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
        implements ThreadContract.ThreadView {
    private static final String TAG = "ThreadFragment";
    @BindView(R.id.rv_threads)
    RecyclerView rvThreads;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_service)
    ImageView ivService;
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager
                .LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
//        presenter.getTicketSuggestions();
        presenter.getServices();
        List<Thread> threadList = ThreadRepo.getInstance().getThreadsByServiceId(selectedService);
        if (!CollectionUtils.isEmpty(threadList)) {
            setUpThreadRecyclerView(threadList);
            rvThreads.setVisibility(View.VISIBLE);
            ivThreadNotFound.setVisibility(View.GONE);
            btnReload.setVisibility(View.GONE);
            etSearch.setVisibility(View.VISIBLE);
        } else presenter.getConversationThreads(true);

        createServiceBottomSheet();
        setDataToSuggestionView();
        tvToolbarTitle.setOnClickListener(v -> {
            serviceSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            toggleServiceBottomSheet();
        });

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
            listenConversationMessages();
        } catch (MqttException e) {
            GlobalUtils.showLog(TAG, "check exception for mqtt: " + e.getMessage());
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

    private void createServiceBottomSheet() {
        serviceSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View llBottomSheet = getLayoutInflater()
                .inflate(R.layout.bottomsheet_select_service, null);

        serviceSheet.setContentView(llBottomSheet);
        serviceSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        serviceSheet.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById
                    (com.google.android.material.R.id.design_bottom_sheet);
 /*           if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);*/
            setupSheetHeight(d, BottomSheetBehavior.STATE_HALF_EXPANDED);
        });

        EditText searchService = llBottomSheet.findViewById(R.id.et_search_service);
        rvServices = llBottomSheet.findViewById(R.id.rv_services);

        searchService.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setupSheetHeight(serviceSheet, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        serviceSheet.setOnDismissListener(dialog -> {
            UiUtils.hideKeyboardForced(Objects.requireNonNull(getActivity()));
            searchService.clearFocus();
        });

        List<Service> serviceList = AvailableServicesRepo.getInstance().getAvailableServices();
        String selectedServiceId = Hawk.get(Constants.SELECTED_SERVICE);
        if (selectedServiceId == null && serviceList != null && !serviceList.isEmpty()) {
            Service firstService = serviceList.get(0);
            tvToolbarTitle.setText(firstService.getName().replace("_", " "));
            Glide.with(Objects.requireNonNull(getContext()))
                    .load(firstService.getServiceIconUrl())
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
            }
        }

        if (serviceList != null && !serviceList.isEmpty())
            setUpServiceRecyclerView(serviceList);

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
        if (serviceSheet.isShowing()) {
            serviceSheet.dismiss();
        } else {
            serviceSheet.show();
        }
    }

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


    private void setUpServiceRecyclerView(List<Service> serviceList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvServices.setLayoutManager(mLayoutManager);

        adapter = new SearchServiceAdapter(serviceList, getActivity());
        rvServices.setAdapter(adapter);

        adapter.setOnItemClickListener(service -> {
            hideKeyBoard();
            etSearch.getText().clear();
            Hawk.put(Constants.SELECTED_SERVICE, service.getServiceId());
            Hawk.put(Constants.SERVICE_CHANGED_THREAD, true);
            tvToolbarTitle.setText(service.getName().replace("_", " "));
            Glide.with(getContext()).load(service.getServiceIconUrl())
                    .placeholder(R.drawable.ic_service_ph)
                    .error(R.drawable.ic_service_ph)
                    .into(ivService);
          /*  sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            bottomSheetShadow.setVisibility(View.GONE);*/
            serviceSheet.dismiss();

            ivThreadNotFound.setVisibility(View.GONE);
            btnReload.setVisibility(View.GONE);
            presenter.getConversationThreads(true);
            TicketSuggestionRepo.getInstance().deleteAllTicketSuggestions();
            presenter.getTicketSuggestions();
        });
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
            listenConversationMessages();
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
    public void getServiceSuccess() {
        List<Service> serviceList = AvailableServicesRepo.getInstance().getAvailableServices();
        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
        Service firstService;
        if (selectedService != null) {
            firstService = AvailableServicesRepo.getInstance().getAvailableServiceById(selectedService);
        } else firstService = serviceList.get(0);
        Hawk.put(Constants.SELECTED_SERVICE, firstService.getServiceId());
        GlobalUtils.showLog(TAG, "first thread service id saved");

        tvToolbarTitle.setText(firstService.getName().replace("_", " "));
        Glide.with(Objects.requireNonNull(getContext())).load(firstService.getServiceIconUrl())
                .placeholder(R.drawable.ic_service_ph)
                .error(R.drawable.ic_service_ph)
                .into(ivService);
        setUpServiceRecyclerView(serviceList);
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
}
