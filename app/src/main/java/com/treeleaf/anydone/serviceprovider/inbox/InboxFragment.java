package com.treeleaf.anydone.serviceprovider.inbox;

import android.app.AlertDialog;
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
import com.treeleaf.anydone.entities.NotificationProto;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.InboxAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.SearchServiceAdapter;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.creategroup.CreateGroupActivity;
import com.treeleaf.anydone.serviceprovider.inboxdetails.InboxDetailActivity;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttCallback;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttClient;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.InboxRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

import static com.treeleaf.anydone.serviceprovider.utils.PaginationScrollListener.PAGE_START;

public class InboxFragment extends BaseFragment<InboxPresenterImpl> implements
        InboxContract.InboxView {
    private static final String TAG = "InboxFragment";

    @BindView(R.id.pb_search)
    ProgressBar pbSearch;
    @BindView(R.id.iv_inbox_not_found)
    ImageView ivInboxNotFound;
    @BindView(R.id.rv_inbox)
    RecyclerView rvInbox;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_service)
    ImageView ivService;
    @BindView(R.id.swipe_refresh_inbox)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_reload)
    MaterialButton btnReload;
    @BindView(R.id.fab_new_message)
    FloatingActionButton fabNewMessage;

    private RecyclerView rvServices;
    private SearchServiceAdapter adapter;
    private BottomSheetDialog serviceSheet;
    private InboxAdapter inboxAdapter;
    private List<Inbox> inboxList = new ArrayList<>();
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;

    public static InboxFragment newInstance(String param1, String param2) {
        InboxFragment fragment = new InboxFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.layout_inbox;
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager
                .LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
        List<Inbox> inboxList = InboxRepo.getInstance().getAllInbox();
        if (!CollectionUtils.isEmpty(inboxList)) {
            setUpInboxRecyclerView(inboxList);
            rvInbox.setVisibility(View.VISIBLE);
            ivInboxNotFound.setVisibility(View.GONE);
            btnReload.setVisibility(View.GONE);
            etSearch.setVisibility(View.VISIBLE);
        } else presenter.getInboxMessages(true, System.currentTimeMillis());

        presenter.getServices();

        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh inbox called");
                    itemCount = 0;
                    currentPage = PAGE_START;
                    isLastPage = false;
//                    inboxAdapter.clear();

                    presenter.getInboxMessages(false, System.currentTimeMillis());

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
            listenNewGroup();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inboxAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rvInbox.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fabNewMessage.hide();
                else if (dy < 0)
                    fabNewMessage.show();
            }
        });

        fabNewMessage.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), CreateGroupActivity.class);
            startActivity(i);
        });
    }

    private void setUpInboxRecyclerView(List<Inbox> inboxList) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvInbox.setLayoutManager(mLayoutManager);

        rvInbox.setHasFixedSize(true);
        inboxAdapter = new InboxAdapter(inboxList, getActivity());
        rvInbox.setAdapter(inboxAdapter);

        inboxAdapter.setOnItemClickListener(inbox -> {
            if (!inbox.isSeen())
                InboxRepo.getInstance().setSeenStatus(inbox);
            Intent i = new Intent(getContext(), InboxDetailActivity.class);
            i.putExtra("inbox_id", inbox.getInboxId());
//            ThreadRepo.getInstance().setSeenStatus(thread);
            startActivity(i);
        });

   /*     rvInbox.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;

                LinearLayoutManager layoutManager = (LinearLayoutManager) rvInbox.getLayoutManager();
                int lastVisibleItemPos = layoutManager.findLastVisibleItemPosition() - 2;
                Inbox lastItem = inboxAdapter.getInboxAt(lastVisibleItemPos);
                GlobalUtils.showLog(TAG, "last item pos: " + lastVisibleItemPos);
                GlobalUtils.showLog(TAG, "last item created date: " +
                        lastItem.getLastMsgDate());
                GlobalUtils.showLog(TAG, "last item subject: " + lastItem.getSubject());
                presenter.getInboxMessages(false, lastItem.getLastMsgDate());
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });*/

        inboxAdapter.setOnDeleteClickListener(this::showDeleteDialog);

        inboxAdapter.setOnMuteClickListener(inbox -> presenter.muteInboxNotification(inbox.getInboxId(),
                false));

        inboxAdapter.setOnUnMuteClickListener(inbox -> presenter.unMuteNotification(inbox.getInboxId()));
    }

    public void toggleServiceBottomSheet() {
        if (serviceSheet.isShowing()) {
            serviceSheet.dismiss();
        } else {
            serviceSheet.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        UiUtils.hideKeyboardForced(getContext());
        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
        inboxList = InboxRepo.getInstance().getAllInbox();
        setUpInboxRecyclerView(inboxList);
//        presenter.getInboxMessages(false);
        try {
            listenConversationMessages();
            listenNewGroup();
        } catch (MqttException e) {
            GlobalUtils.showLog(TAG, "check mqtt exception: " + e.toString());
            e.printStackTrace();
        }
    }


    private void showDeleteDialog(Inbox inbox) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to leave this conversation?");
        builder1.setCancelable(true);

        builder1.setNeutralButton(
                "Cancel",
                (dialog, id) -> {
                    inboxAdapter.closeSwipeLayout(inbox.getInboxId());
                    dialog.dismiss();
                });


        builder1.setPositiveButton(
                "Leave & delete",
                (dialog, id) -> {
                    presenter.leaveAndDeleteConversation(inbox);
                    inboxAdapter.closeSwipeLayout(inbox.getInboxId());
                    dialog.dismiss();
                });

        builder1.setNegativeButton(
                "Leave",
                (dialog, id) -> {
                    presenter.leaveConversation(inbox);
                    inboxAdapter.closeSwipeLayout(inbox.getInboxId());
                    dialog.dismiss();
                });


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            alert11.getButton(AlertDialog.BUTTON_NEUTRAL)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEUTRAL)
                    .setTextColor(getResources().getColor(R.color.colorPrimary));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                    .getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                    .getColor(android.R.color.holo_red_dark));

      /*      alert11.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            alert11.getButton(AlertDialog.BUTTON_NEUTRAL).setAllCaps(false);
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);*/

        });
        alert11.show();
    }

    @Override
    public void getServicesSuccess() {
        GlobalUtils.showLog(TAG, "fetched services from inbox");
    }

    @Override
    public void getServicesFail(String msg) {
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
    public void getInboxMessageSuccess() {
        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
        inboxList = InboxRepo.getInstance().getAllInbox();
        GlobalUtils.showLog(TAG, "fetched inbox list size: " + inboxList.size());
        setUpInboxRecyclerView(inboxList);
//        inboxAdapter.setData(inboxList);
        rvInbox.setVisibility(View.VISIBLE);
        if (!CollectionUtils.isEmpty(inboxList)) {
            ivInboxNotFound.setVisibility(View.GONE);
            btnReload.setVisibility(View.GONE);
            etSearch.setVisibility(View.VISIBLE);
        } else etSearch.setVisibility(View.GONE);

        /*       *//**
         * manage progress view
         *//*
        if (currentPage != PAGE_START) inboxAdapter.removeLoading();

        // check weather is last page or not
        if (currentPage < totalPage) {
            inboxAdapter.addLoading();
        } else {
            isLastPage = true;
        }
        isLoading = false;*/
    }

    @Override
    public void getInboxMessageFail(String msg) {
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
    public void onMuteNotificationSuccess(String inboxId) {
        inboxAdapter.closeSwipeLayout(inboxId);
        inboxAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMuteNotificationFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(), getActivity()
                .getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onUnMuteSuccess(String inboxId) {
        inboxAdapter.closeSwipeLayout(inboxId);
        inboxAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUnMuteFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(), getActivity()
                .getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onConversationLeaveSuccess(Inbox inbox) {
        GlobalUtils.showLog(TAG, "conversation left");
    }

    @Override
    public void onConversationLeaveFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(), getActivity()
                .getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onConversationDeleteSuccess(Inbox inbox) {
        int index = inboxList.indexOf(inbox);
        GlobalUtils.showLog(TAG, "position check: " + index);

//        inboxAdapter.notifyItemRemoved(index);
        inboxList.remove(index);
        inboxAdapter.notifyDataSetChanged();
        InboxRepo.getInstance().deleteInbox(inbox.getInboxId());
    }

    @Override
    public void onConversationDeleteFail(String msg) {
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

        ivInboxNotFound.setVisibility(View.VISIBLE);
        rvInbox.setVisibility(View.GONE);
        btnReload.setVisibility(View.VISIBLE);
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


    private void setUpServiceRecyclerView(List<Service> serviceList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvServices.setLayoutManager(mLayoutManager);

        adapter = new SearchServiceAdapter(serviceList, getActivity());
        rvServices.setAdapter(adapter);

        adapter.setOnItemClickListener(service -> {
            hideKeyBoard();
            Hawk.put(Constants.SELECTED_SERVICE, service.getServiceId());
            Hawk.put(Constants.SERVICE_CHANGED_INBOX, true);
            tvToolbarTitle.setText(service.getName().replace("_", " "));

            Glide.with(Objects.requireNonNull(getContext()))
                    .load(service.getServiceIconUrl())
                    .placeholder(R.drawable.ic_service_ph)
                    .error(R.drawable.ic_service_ph)
                    .into(ivService);

            serviceSheet.dismiss();

            ivInboxNotFound.setVisibility(View.GONE);
            btnReload.setVisibility(View.GONE);
            presenter.getInboxMessages(true, System.currentTimeMillis());
        });
    }

    private void listenNewGroup() throws MqttException {
        GlobalUtils.showLog(TAG, "listen new grp");
        Account userAccount = AccountRepo.getInstance().getAccount();
//        Employee userAccount = EmployeeRepo.getInstance().getEmployee();
        if (userAccount != null) {
            String SUBSCRIBE_TOPIC = "anydone/notification/" + userAccount.getAccountId();

            GlobalUtils.showLog(TAG, "user Id: " + userAccount.getAccountId());
            //listen for new group creation
            TreeleafMqttClient.subscribe(SUBSCRIBE_TOPIC, new TreeleafMqttCallback() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    GlobalUtils.showLog(TAG, "message arrived");

                    NotificationProto.Notification notification =
                            NotificationProto.Notification.parseFrom(message.getPayload());

                    GlobalUtils.showLog(TAG, "incoming notification inbox: " + notification);
                }
            });
        }
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

                    NotificationProto.Notification notification =
                            NotificationProto.Notification.parseFrom(message.getPayload());

                    GlobalUtils.showLog(TAG, "incoming for inbox: " + notification);

                    if (relayResponse.getResponseType().equals(RtcProto
                            .RelayResponse.RelayResponseType.RTC_MESSAGE_RESPONSE)) {
                        GlobalUtils.showLog(TAG, "message type text");
                        new Handler(Looper.getMainLooper()).post(() -> {
                            String inboxId = relayResponse.getRtcMessage().getRefId();

                            if (inboxList != null) {
                                for (Inbox existingInbox : inboxList
                                ) {
                                    GlobalUtils.showLog(TAG, "inside for loop");
                                    if (existingInbox.isValid() &&
                                            existingInbox.getInboxId().equalsIgnoreCase(inboxId)) {
                                        GlobalUtils.showLog(TAG, "inbox exists");
                                        updateInbox(existingInbox, relayResponse);
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }

    }

    private void updateInbox(Inbox inbox,
                             RtcProto.RelayResponse relayResponse) {
        new Handler(Looper.getMainLooper()).post(() -> InboxRepo.getInstance()
                .updateInbox(inbox,
                        System.currentTimeMillis(),
                        relayResponse.getRtcMessage().getSentAt(),
                        relayResponse.getRtcMessage().getText().getMessage(),
                        relayResponse.getRtcMessage().getSenderAccountObj().getFullName(),
                        false,
                        new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                GlobalUtils.showLog(TAG, "inbox updated");
                                String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
                          /*      List<Inbox> updatedInboxList = InboxRepo.getInstance()
                                        .getAllInbox();*/
                                Inbox updatedInbox = InboxRepo.getInstance().getInboxById(inbox.getInboxId());
                                inboxAdapter.updateInbox(updatedInbox);
                            }

                            @Override
                            public void fail() {
                                GlobalUtils.showLog(TAG, "failed to update inbox");
                            }
                        }));
    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager)
                Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
    }

    @OnClick(R.id.btn_reload)
    void reload() {
        btnReload.setVisibility(View.GONE);
        ivInboxNotFound.setVisibility(View.GONE);
//        presenter.getConversationThreads(true);
    }
}
