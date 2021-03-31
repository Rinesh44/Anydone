package com.treeleaf.anydone.serviceprovider.searchconversation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.SearchedListAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.inboxdetails.InboxDetailActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.repo.ConversationRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ProtoMapper;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
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
import io.realm.RealmList;
import retrofit2.Retrofit;

public class SearchConversation extends MvpBaseActivity<SearchConversationPresenterImpl> implements
        SearchConversationContract.SearchConversationView {
    private static final String TAG = "SearchConversation";
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.rv_conversations)
    RecyclerView rvConversations;
    @BindView(R.id.et_search)
    EditText etSearch;

    String inboxId;
    private String searchedText;

    SearchedListAdapter adapter;
    List<Conversation> conversationList = new RealmList<>();
    Disposable disposable = new CompositeDisposable();


    @Override
    protected int getLayout() {
        return R.layout.activity_search_conversation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        inboxId = i.getStringExtra("inbox_id");

        GlobalUtils.showLog(TAG, "received inbox id: " + inboxId);
        setToolbar();
        setUpConversations(conversationList);

        observeSearchView();
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder(getString(R.string.search_title));
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpConversations(List<Conversation> conversationList) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false);
        rvConversations.setLayoutManager(mLayoutManager);

        adapter = new SearchedListAdapter(conversationList, this);
        rvConversations.setAdapter(adapter);

        adapter.setOnItemClickListener(conversation -> {
            Intent i = new Intent(this, InboxDetailActivity.class);
            i.putExtra("inbox_id", conversation.getRefId());
            i.putExtra("searched_conversation", true);
            i.putExtra("msg_id", conversation.getConversationId());
            startActivity(i);
        });

        rvConversations.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager)
                    Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return false;
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }


    @Override
    public void onSearchConversationSuccess(List<Conversation> conversationList) {
        adapter.setData(conversationList, searchedText);
    }

    @Override
    public void onSearchConversationFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void showProgressBar(String message) {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
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

    private void observeSearchView() {
        disposable = fromView(etSearch)
                .map(s -> s.toLowerCase().trim())
                .debounce(800, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .flatMap((Function<String,
                        ObservableSource<RtcServiceRpcProto.RtcServiceBaseResponse>>) query -> {
                    searchedText = query;
                    return searchMessage(query);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RtcServiceRpcProto.RtcServiceBaseResponse>() {
                    @Override
                    public void onNext(@NonNull RtcServiceRpcProto.RtcServiceBaseResponse o) {
                        GlobalUtils.showLog("TAG", "search response: " + o);

                        runOnUiThread(() -> progressBar.setVisibility(View.GONE));

                        if (o.getError()) {
                            onSearchConversationFail(o.getMsg());
                            return;
                        }

                        GlobalUtils.showLog(TAG, "messages response: " +
                                o.getRtcMessagesList());
                        if (!CollectionUtils.isEmpty(o.getRtcMessagesList())) {
                            saveConversations(o.getRtcMessagesList());
                        } else {
                            onSearchConversationFail("stop progress");
                        }
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

    public Observable<RtcServiceRpcProto.RtcServiceBaseResponse> searchMessage(String query) {
        GlobalUtils.showLog(TAG, "search inbox called()");
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));

//        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<RtcServiceRpcProto.RtcServiceBaseResponse> inboxObservable;
        String token = Hawk.get(Constants.TOKEN);

        inboxObservable = service.searchInboxMessages(token, inboxId,
                AnydoneProto.ServiceContext.INBOX_CONTEXT_VALUE,
                query);

        return inboxObservable;
    }


    private void saveConversations(List<RtcProto.RtcMessage> rtcMessagesList) {
        RealmList<Conversation> conversations = ProtoMapper.transformConversation(rtcMessagesList, false);
        ConversationRepo.getInstance().saveConversationList(conversations, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "all conversations saved");
                onSearchConversationSuccess(conversations);
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save conversations");
            }
        });
    }

}