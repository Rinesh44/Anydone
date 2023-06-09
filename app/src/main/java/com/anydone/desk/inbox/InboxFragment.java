package com.anydone.desk.inbox;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.InboxProto;
import com.treeleaf.anydone.entities.NotificationProto;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.rpc.InboxRpcProto;
import com.anydone.desk.R;
import com.anydone.desk.adapters.InboxAdapter;
import com.anydone.desk.adapters.SearchServiceAdapter;
import com.anydone.desk.base.fragment.BaseFragment;
import com.anydone.desk.creategroup.CreateGroupActivity;
import com.anydone.desk.inboxdetails.InboxDetailActivity;
import com.anydone.desk.injection.component.ApplicationComponent;
import com.anydone.desk.mqtt.TreeleafMqttCallback;
import com.anydone.desk.mqtt.TreeleafMqttClient;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.Inbox;
import com.anydone.desk.realm.model.Service;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.InboxRepo;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.CustomLayoutManager;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.ProtoMapper;
import com.anydone.desk.utils.UiUtils;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Retrofit;

import static com.anydone.desk.utils.PaginationScrollListener.PAGE_START;

public class InboxFragment extends BaseFragment<InboxPresenterImpl> implements
        InboxContract.InboxView, TreeleafMqttClient.OnMQTTConnected {
    private static final String TAG = "InboxFragment";
    private LocalBroadcastManager broadcastManager;

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
    FloatingActionMenu fabNewMessage;
    @BindView(R.id.tv_connection_status)
    TextView tvConnectionStatus;
    @BindView(R.id.fab_create_group)
    FloatingActionButton fabCreateGroup;
    @BindView(R.id.fab_create_thread)
    FloatingActionButton fabCreateThread;

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
    private BottomSheetDialog convertDialog;
    Disposable disposable = new CompositeDisposable();
    private List<String> searchedInboxIds = new ArrayList<>();


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

        GlobalUtils.showLog(TAG, "on activity created called()");
        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager
                .LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        broadcastManager = LocalBroadcastManager.getInstance(getActivity());

        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
        List<Inbox> inboxList = InboxRepo.getInstance().getAllInbox();
        setUpInboxRecyclerView(inboxList);
        if (!CollectionUtils.isEmpty(inboxList)) {
//            setUpInboxRecyclerView(inboxList);
            rvInbox.setVisibility(View.VISIBLE);
            ivInboxNotFound.setVisibility(View.GONE);
            btnReload.setVisibility(View.GONE);
            etSearch.setVisibility(View.VISIBLE);
            presenter.getInboxMessages(false, System.currentTimeMillis());
        } else {
            presenter.getInboxMessages(true, System.currentTimeMillis());
        }

//        presenter.getServices();

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

/*        rvInbox.getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                hideProgressBar();
                                // At this point the layout is complete and the
                                // dimensions of recyclerView and any child views
                                // are known.
                                rvInbox
                                        .getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                            }
                        });*/

        try {
            if (TreeleafMqttClient.mqttClient.isConnected()) {
                listenConversationMessages();
                listenNewGroup();
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

//        observeSearchView();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
     /*           if (s.length() > 0) {
                    showProgressBar("");
                    Handler handler = new Handler();
                    handler.postDelayed(() -> presenter.searchInbox(s.toString()), 2000);
                } else {
                    List<Inbox> inboxList = InboxRepo.getInstance().getAllInbox();
                    inboxAdapter.setData(inboxList);
                }*/

//                presenter.searchInbox(s.toString());
                if (inboxAdapter != null)
                    inboxAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rvInbox.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                fabNewMessage.close(false);
                if (dy > 0)
                    fabNewMessage.setVisibility(View.GONE);
                else if (dy < 0)
                    fabNewMessage.setVisibility(View.VISIBLE);
            }
        });


        TreeleafMqttClient.setOnMqttConnectedListener(this);

        fabCreateGroup.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), CreateGroupActivity.class);
            i.putExtra("group", true);
            startActivity(i);
            fabNewMessage.close(false);
        });


        fabCreateThread.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), CreateGroupActivity.class);
            i.putExtra("group", false);
            startActivity(i);
            fabNewMessage.close(false);
        });
    }

    public Observable<String> fromView(EditText searchView) {
        final PublishSubject<String> subject = PublishSubject.create();

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                GlobalUtils.showLog(TAG, "from view: " + s.toString());
//                subject.onNext(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
//                subject.onComplete();
                subject.onNext(s.toString());

            }
        });

        return subject;
    }

    public Observable<InboxRpcProto.InboxBaseResponse> findInbox(String query) {
        GlobalUtils.showLog(TAG, "search inbox called()");
//        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<InboxRpcProto.InboxBaseResponse> inboxObservable;
        String token = Hawk.get(Constants.TOKEN);


        inboxObservable = service.searchInbox(token, query);

        return inboxObservable;
    }

    private void observeSearchView() {
        disposable = fromView(etSearch)
                .map(s -> s.toLowerCase().trim())
                .debounce(800, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .flatMap((Function<String, ObservableSource<InboxRpcProto.InboxBaseResponse>>) this::findInbox)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<InboxRpcProto.InboxBaseResponse>() {
                    @Override
                    public void onNext(@NonNull InboxRpcProto.InboxBaseResponse o) {
                        GlobalUtils.showLog("TAG", "search response: " + o);
                        GlobalUtils.showLog("TAG", "search list size: " + o.getInboxResponse().getInboxList().size());
                        List<Inbox> searchedList = ProtoMapper.transformInbox(o.getInboxResponse().getInboxList());
                        GlobalUtils.showLog(TAG, "converted list size: " + searchedList.size());
                        searchedInboxIds.clear();
                        for (Inbox inbox : searchedList
                        ) {
                            searchedInboxIds.add(inbox.getInboxId());
                        }

                        GlobalUtils.showLog(TAG, "inbox ids " + searchedInboxIds.size());

                        saveInboxList(o.getInboxResponse().getInboxList());

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        GlobalUtils.showLog(TAG, "on error: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private void setUpInboxRecyclerView(List<Inbox> inboxList) {
        LinearLayoutManager mLayoutManager = new CustomLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
                false);
        rvInbox.setLayoutManager(mLayoutManager);

//        rvInbox.setHasFixedSize(true);
//        inboxList = InboxRepo.getInstance().getAllInbox();
        inboxAdapter = new InboxAdapter(inboxList, getActivity());
        rvInbox.setAdapter(inboxAdapter);

        inboxAdapter.setOnItemClickListener(inbox -> {
            if (!inbox.isSeen() && !inbox.isSelfInbox()) {
                InboxRepo.getInstance().setSeenStatus(inbox);

                //send broadcast about notification count decrement
                Intent intent = new Intent("broadcast_inbox");
                intent.putExtra("decrement", true);
                broadcastManager.sendBroadcast(intent);

            }

            //need to set unread message count to 0 once clicked
            InboxRepo.getInstance().setInboxAsRead(inbox.getInboxId(), new Repo.Callback() {
                @Override
                public void success(Object o) {
                    GlobalUtils.showLog(TAG, "set inbox unread count to 0");
                }

                @Override
                public void fail() {
                    GlobalUtils.showLog(TAG, "failed to update unread count to 0");
                }
            });


            Intent i = new Intent(getContext(), InboxDetailActivity.class);
            i.putExtra("inbox_id", inbox.getInboxId());
//            ThreadRepo.getInstance().setSeenStatus(thread);
            startActivity(i);

            if (etSearch != null) {
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (etSearch != null)
                        etSearch.getText().clear();
                }, 2000);
            }
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

        inboxAdapter.setOnJoinClickListener(inbox -> presenter.joinGroup(inbox.getInboxId()));

        inboxAdapter.setOnConvertToGroupClickListener(this::createConvertToGroupSheet);
    }

    public void toggleServiceBottomSheet() {
        if (serviceSheet.isShowing()) {
            serviceSheet.dismiss();
        } else {
            serviceSheet.show();
        }
    }


    private void createConvertToGroupSheet(Inbox inbox) {
        convertDialog = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View llBottomSheet = getLayoutInflater()
                .inflate(R.layout.layout_convert_to_grp_dialog, null);

        convertDialog.setContentView(llBottomSheet);
//        convertDialog.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

 /*       convertDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
         *//*   if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);*//*
            setupSheetHeight(d, BottomSheetBehavior.STATE_HALF_EXPANDED);
        });*/

        TextInputEditText grpName = llBottomSheet.findViewById(R.id.et_group_name);
        TextView tvConvert = llBottomSheet.findViewById(R.id.btn_ok);
        TextView tvCancel = llBottomSheet.findViewById(R.id.btn_cancel);


        if (!inbox.getSubject().isEmpty()) {
            grpName.setText(inbox.getSubject());
        }

        grpName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setupSheetHeight(convertDialog, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        convertDialog.setOnDismissListener(dialog -> grpName.clearFocus());

        tvCancel.setOnClickListener(v -> {
            convertDialog.dismiss();
            inboxAdapter.closeSwipeLayout(inbox.getInboxId());
        });

        tvConvert.setOnClickListener(v -> {
            convertDialog.dismiss();
            String group = Objects.requireNonNull(grpName.getText()).toString();
            if (group.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter group", Toast.LENGTH_SHORT).show();
                return;
            }


            presenter.convertToGroup(inbox, group);
            inboxAdapter.closeSwipeLayout(inbox.getInboxId());
        });

        convertDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();

//        presenter.getInboxMessages(false, System.currentTimeMillis());
        UiUtils.hideKeyboardForced(getContext());
        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
        inboxList = InboxRepo.getInstance().getAllInbox();
        GlobalUtils.showLog(TAG, "inbox list size on resume:" + inboxList.size());
//        setUpInboxRecyclerView(inboxList);
        if (inboxAdapter != null)
            inboxAdapter.setData(inboxList);

        //check and update unread count
        List<Inbox> unreadInbox = InboxRepo.getInstance().getUnreadInboxList();
        //send broadcast about notification count decrement
        Intent intent = new Intent("broadcast_inbox");
        intent.putExtra("update", true);
        intent.putExtra("count", unreadInbox.size());
        broadcastManager.sendBroadcast(intent);


//        presenter.getInboxMessages(false);
        try {
            if (TreeleafMqttClient.mqttClient.isConnected()) {
                listenConversationMessages();
                listenNewGroup();
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
    }

    private void showGroupConvertDialog(Inbox inbox) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_convert_to_grp_dialog);

        TextInputEditText etGrpName = dialog.findViewById(R.id.et_group_name);
        TextView convert = dialog.findViewById(R.id.btn_ok);
        TextView cancel = dialog.findViewById(R.id.btn_cancel);

        UiUtils.showKeyboardForced(getActivity());
        etGrpName.requestFocus();
        etGrpName.setText(inbox.getSubject());

        if (!inbox.getSubject().isEmpty()) {
            etGrpName.setSelection(inbox.getSubject().length());
        }

        cancel.setOnClickListener(v -> {
            UiUtils.hideKeyboardForced(getContext());
            inboxAdapter.closeSwipeLayout(inbox.getInboxId());
            dialog.dismiss();
        });

        convert.setOnClickListener(v -> {
            String grpName = Objects.requireNonNull(etGrpName.getText()).toString().trim();
            if (grpName.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter group name", Toast.LENGTH_SHORT).show();
                return;
            }
            inboxAdapter.closeSwipeLayout(inbox.getInboxId());
            presenter.convertToGroup(inbox, etGrpName.getText().toString().trim());

        });

        dialog.show();


/*        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to convert to group?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Convert",
                (dialog, id) -> {
                    presenter.convertToGroup(inbox);
                    inboxAdapter.closeSwipeLayout(inbox.getInboxId());
                    dialog.dismiss();
                });

        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> {
                    inboxAdapter.closeSwipeLayout(inbox.getInboxId());
                    dialog.dismiss();
                });


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface ->
        {

            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(R.color.colorPrimary));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                    .getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                    .getColor(android.R.color.holo_red_dark));

      *//*      alert11.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            alert11.getButton(AlertDialog.BUTTON_NEUTRAL).setAllCaps(false);
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);*//*

        });
        alert11.show();*/
    }

    private void showDeleteDialog(Inbox inbox) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to leave this conversation?");
        builder1.setCancelable(true);

        if (inbox.isLeftGroup() ||
                inbox.getInboxType().equalsIgnoreCase(InboxProto.Inbox.InboxType.DIRECT_MESSAGE.name())) {
            builder1.setMessage("Are you sure you want to delete this conversation?");
            builder1.setPositiveButton(
                    "Delete",
                    (dialog, id) -> {
                        presenter.leaveAndDeleteConversation(inbox);
                        inboxAdapter.closeSwipeLayout(inbox.getInboxId());
                        dialog.dismiss();
                    });

            builder1.setNegativeButton(
                    "Cancel",
                    (dialog, id) -> {
                        inboxAdapter.closeSwipeLayout(inbox.getInboxId());
                        dialog.dismiss();
                    });
        } else {
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

        }


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface ->
        {
            if (inbox.isLeftGroup() || inbox.getInboxType().equalsIgnoreCase(InboxProto.Inbox.InboxType.DIRECT_MESSAGE.name())) {
                alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setBackgroundColor(getResources().getColor(R.color.transparent));
                alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(getResources().getColor(R.color.colorPrimary));

                alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                        .getColor(R.color.transparent));
                alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                        .getColor(android.R.color.holo_red_dark));

            } else {
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

            }

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

        List<Inbox> unreadInbox = InboxRepo.getInstance().getUnreadInboxList();
        GlobalUtils.showLog(TAG, "unread inbox count: " + unreadInbox.size());
        //send broadcast about notification count decrement
        Intent intent = new Intent("broadcast_inbox");
        intent.putExtra("update", true);
        intent.putExtra("count", unreadInbox.size());
        broadcastManager.sendBroadcast(intent);


//        setUpInboxRecyclerView(inboxList);
        inboxAdapter.setData(inboxList);
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

    private void saveInboxList(List<InboxProto.Inbox> inboxList) {
        InboxRepo.getInstance().saveInboxes(inboxList, true, new Repo.Callback() {
            @Override
            public void success(Object o) {
                fetchSearchedListFromDb();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG,
                        "error on saving inbox list");
            }
        });
    }

    private void fetchSearchedListFromDb() {
        List<Inbox> searchedInbox = new ArrayList<>();
        for (String inboxId : searchedInboxIds
        ) {
            Inbox inbox = InboxRepo.getInstance().getInboxById(inboxId);
            searchedInbox.add(inbox);
        }

        GlobalUtils.showLog(TAG, "searched list from db: " + searchedInbox.size());
        inboxAdapter.setSearchedData(searchedInbox);
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
        inboxAdapter.updateInbox(inbox);
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
    public void onStop() {
        super.onStop();
        disposable.dispose();
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
    public void onConvertToGroupSuccess(Inbox inbox) {
        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView(),
                "Converted to group");
        GlobalUtils.showLog(TAG, "convert to group success");
        inboxAdapter.updateInbox(inbox);
    }

    @Override
    public void onConvertToGroupFail(String msg) {
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
    public void onJoinGroupSuccess(String inboxId) {
        Inbox inbox = InboxRepo.getInstance().getInboxById(inboxId);
        inboxAdapter.updateInbox(inbox);
    }

    @Override
    public void onJoinGroupFail(String msg) {
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
    public void searchInboxSuccess(List<Inbox> inboxList) {
        GlobalUtils.showLog(TAG, "search inbox success callback called");
//        inboxAdapter.setData(inboxList);
        setUpInboxRecyclerView(inboxList);
    }

    @Override
    public void searchInboxFail(String msg) {
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
            if (layoutParams != null)
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
            String sessionId = Hawk.get(Constants.SESSION_ID);
            String SUBSCRIBE_TOPIC = "anydone/notification/" + userAccount.getAccountId() + "/" + sessionId;

            GlobalUtils.showLog(TAG, "new inbox subscribe topic: " + SUBSCRIBE_TOPIC);

            GlobalUtils.showLog(TAG, "user Id: " + userAccount.getAccountId());
            //listen for new group creation
            TreeleafMqttClient.subscribe(SUBSCRIBE_TOPIC, new TreeleafMqttCallback() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    GlobalUtils.showLog(TAG, "message arrived");

                    NotificationProto.Notification notification =
                            NotificationProto.Notification.parseFrom(message.getPayload());

                    GlobalUtils.showLog(TAG, "incoming new inbox info: " + notification);
                    presenter.getInboxMessages(false, System.currentTimeMillis());

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

                                        //delay for 500 ms for notification count handling
                                        Handler handler = new Handler();
                                        handler.postDelayed(() -> updateInbox(existingInbox,
                                                relayResponse), 500);

//                                        updateInbox(existingInbox, relayResponse);
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
        String msg = "";
        Account user = AccountRepo.getInstance().getAccount();
        GlobalUtils.showLog(TAG, "update inbox called()");
        GlobalUtils.showLog(TAG, "check msg:" + relayResponse.getRtcMessage().getText().getMessage());
        switch (relayResponse.getRtcMessage().getRtcMessageType().name()) {
            case "TEXT_RTC_MESSAGE":
                if (relayResponse.getRtcMessage().getSenderAccountObj()
                        .getAccountId().equals(user.getAccountId())) {
                    msg = "You: " + relayResponse.getRtcMessage().getText().getMessage().trim();
                } else {
                    String sender = relayResponse.getRtcMessage().getSenderAccountObj().getFullName();
                    msg = sender + ": " + relayResponse.getRtcMessage().getText().getMessage().trim();
                }
                break;

            case "LINK_RTC_MESSAGE":
                if (relayResponse.getRtcMessage().getSenderAccountObj().getAccountId()
                        .equals(user.getAccountId())) {
                    msg = "You: Sent a link";
                } else {
                    String sender = relayResponse.getRtcMessage().getSenderAccountObj().getFullName();
                    msg = sender + ": Sent a link";
                }
                break;

            case "IMAGE_RTC_MESSAGE":
                if (relayResponse.getRtcMessage().getSenderAccountObj().getAccountId()
                        .equals(user.getAccountId())) {
                    msg = "You: Sent an image";
                } else {
                    String sender = relayResponse.getRtcMessage().getSenderAccountObj().getFullName();
                    msg = sender + ": Sent an image";
                }
                break;

            case "DOC_RTC_MESSAGE":
                if (relayResponse.getRtcMessage().getSenderAccountObj().getAccountId()
                        .equals(user.getAccountId())) {
                    msg = "You: Sent a file";
                } else {
                    String sender = relayResponse.getRtcMessage().getSenderAccountObj().getFullName();
                    msg = sender + ": Sent a file";
                }
                break;

            case "VIDEO_CALL_RTC_MESSAGE":
//                    if (senderId != null)
                if (relayResponse.getRtcMessage().getSenderAccountObj().getAccountId()
                        .equals(user.getAccountId())) {
                    inbox.setLastMsg(("You: Made a call"));
                } else {
                    String sender = relayResponse.getRtcMessage().getSenderAccountObj().getFullName();
                    inbox.setLastMsg(sender + ": Made a call");
                }
//                    inbox.setLastMsg("Made a call");
                break;
        }

        String finalMsg = msg;
        boolean setSeen = false;
        GlobalUtils.showLog(TAG, "is seen: " + setSeen);

        GlobalUtils.showLog(TAG, "user id: " + user.getAccountId());
        GlobalUtils.showLog(TAG, "sender id: " + relayResponse.getRtcMessage().getSenderAccountObj().getAccountId());
        if (user.getAccountId().equalsIgnoreCase(relayResponse.getRtcMessage()
                .getSenderAccountObj().getAccountId())) {
            setSeen = true;
        }
        GlobalUtils.showLog(TAG, "is seen: " + setSeen);


        boolean finalSetSeen = setSeen;
        new Handler(Looper.getMainLooper()).post(() ->
                InboxRepo.getInstance().updateInbox(inbox,
                        System.currentTimeMillis(),
                        relayResponse.getRtcMessage().getSentAt(),
                        finalMsg,
                        relayResponse.getRtcMessage().getSenderAccountObj().getFullName(),
                        finalSetSeen,
                        new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                GlobalUtils.showLog(TAG, "inbox updated");
                                String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
                                List<Inbox> updatedInboxList = InboxRepo.getInstance()
                                        .getAllInbox();

                                List<Inbox> unreadInboxList = InboxRepo.getInstance()
                                        .getUnreadInboxList();

                                if (rvInbox != null)
                                    rvInbox.post(() -> inboxAdapter.setData(updatedInboxList));


                                Intent intent = new Intent("broadcast_inbox");
                                intent.putExtra("update", true);
                                intent.putExtra("count", unreadInboxList.size());
                                broadcastManager.sendBroadcast(intent);

                             /*   Inbox updatedInbox = InboxRepo.getInstance().getInboxById(inbox.getInboxId());
                                inboxAdapter.updateInbox(updatedInbox);*/
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
            listenNewGroup();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mqttNotConnected() {

    }
}
