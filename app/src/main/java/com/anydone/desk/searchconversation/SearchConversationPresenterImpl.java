package com.anydone.desk.searchconversation;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.model.Conversation;
import com.anydone.desk.realm.repo.ConversationRepo;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.ProtoMapper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmList;
import retrofit2.Retrofit;

public class SearchConversationPresenterImpl extends BasePresenter<SearchConversationContract.SearchConversationView>
        implements SearchConversationContract.SearchConversationPresenter {
    private static final String TAG = "SearchConversationPrese";
    private SearchConversationRepository conversationRepository;

    @Inject
    public SearchConversationPresenterImpl(SearchConversationRepository searchConversationRepository) {
        this.conversationRepository = searchConversationRepository;
    }

    @Override
    public void SearchConversations(String query, String inboxId) {
        getView().showProgressBar("show progress");
        Observable<RtcServiceRpcProto.RtcServiceBaseResponse> getMessagesObservable;
        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        getMessagesObservable = service.searchInboxMessages(token,
                inboxId, AnydoneProto.ServiceContext.INBOX_CONTEXT_VALUE, query);

        addSubscription(getMessagesObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RtcServiceRpcProto.RtcServiceBaseResponse>() {
                    @Override
                    public void onNext(@NonNull RtcServiceRpcProto.RtcServiceBaseResponse
                                               inboxBaseResponse) {
                        GlobalUtils.showLog(TAG, "messages inbox response: " +
                                inboxBaseResponse);

                        if (inboxBaseResponse.getError()) {
                            getView().onSearchConversationFail(inboxBaseResponse.getMsg());
                            return;
                        }

                        GlobalUtils.showLog(TAG, "messages response: " +
                                inboxBaseResponse.getRtcMessagesList());
                        if (!CollectionUtils.isEmpty(inboxBaseResponse.getRtcMessagesList())) {
                            saveConversations(inboxBaseResponse.getRtcMessagesList());
                        } else {
                            getView().onSearchConversationFail("stop progress");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().onSearchConversationFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );

    }

    private void saveConversations(List<RtcProto.RtcMessage> rtcMessagesList) {
        RealmList<Conversation> conversations = ProtoMapper.transformConversation(rtcMessagesList, false);
        ConversationRepo.getInstance().saveConversationList(conversations, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "all conversations saved");
                getView().onSearchConversationSuccess(conversations);
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save conversations");
            }
        });
    }
}
