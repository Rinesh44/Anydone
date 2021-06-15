package com.anydone.desk.threads;

import android.widget.Toast;

import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Customer;
import com.anydone.desk.realm.model.FilterData;
import com.anydone.desk.realm.model.MessageFilterData;
import com.anydone.desk.realm.model.Service;
import com.anydone.desk.realm.model.Tags;
import com.anydone.desk.realm.model.Thread;
import com.anydone.desk.realm.model.TicketCategory;
import com.anydone.desk.realm.model.Tickets;
import com.anydone.desk.realm.repo.ConversationThreadLabelRepo;
import com.anydone.desk.realm.repo.FilterDataRepo;
import com.anydone.desk.realm.repo.MessageFilterDataRepo;
import com.anydone.desk.realm.repo.TicketRepo;
import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.ConversationProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.ConversationRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.realm.repo.ThreadRepo;
import com.anydone.desk.realm.repo.TicketSuggestionRepo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmList;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;

public class ThreadPresenterImpl extends BasePresenter<ThreadContract.ThreadView> implements
        ThreadContract.ThreadPresenter {
    private static final String TAG = "ThreadPresenterImpl";
    private ThreadRepository threadRepository;

    @Inject
    public ThreadPresenterImpl(ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
    }

    @Override
    public void getConversationThreads(boolean showProgress) {
        if (showProgress)
            getView().showProgressBar("Please wait");
        Observable<ConversationRpcProto.ConversationBaseResponse> threadBaseResponseObservable;

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService anyDoneService = retrofit.create(AnyDoneService.class);
        String token = Hawk.get(Constants.TOKEN);
        String service = Hawk.get(Constants.SELECTED_SERVICE);

        threadBaseResponseObservable = anyDoneService.getConversationThreads(token, service);
        addSubscription(threadBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<ConversationRpcProto.ConversationBaseResponse>() {
                            @Override
                            public void onNext(@NonNull ConversationRpcProto.ConversationBaseResponse
                                                       conversationThreadBaseResponse) {
                                GlobalUtils.showLog(TAG, "get conversation threads response: "
                                        + conversationThreadBaseResponse);

                                getView().hideProgressBar();

                                if (conversationThreadBaseResponse.getError()) {
                                    getView().getConversationThreadFail(
                                            conversationThreadBaseResponse.getMsg());
                                    return;
                                }

                                GlobalUtils.showLog(TAG, "conversation thread size: " +
                                        conversationThreadBaseResponse.getConversationsList().size());
                                if (!CollectionUtils.isEmpty(
                                        conversationThreadBaseResponse.getConversationsList())) {
                                    saveConversationThreads(conversationThreadBaseResponse
                                            .getConversationsList());
                                } else {
                                    getView().getConversationThreadFail("Not found");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getConversationThreadFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    private void saveConversationThreads(List<ConversationProto.ConversationThread> conversationsList) {
        ThreadRepo.getInstance().saveThreads(conversationsList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().getConversationThreadSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG,
                        "error on saving conversation threads");
            }
        });
    }

    @Override
    public void getTicketSuggestions() {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        ticketObservable = service.getTicketSuggestions(token, serviceId);
        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(@NonNull TicketServiceRpcProto.TicketBaseResponse
                                                       ticketSuggestionResponse) {
                                GlobalUtils.showLog(TAG, "ticket suggestions response: "
                                        + ticketSuggestionResponse);

                                if (ticketSuggestionResponse.getError()) {
                                    getView().getTicketSuggestionFail(ticketSuggestionResponse.getMsg());
                                    return;
                                }

                                if (!CollectionUtils.isEmpty(
                                        ticketSuggestionResponse.getTicketSuggestionsList())) {
                                    TicketSuggestionRepo.getInstance().saveTicketSuggestionList(
                                            ticketSuggestionResponse.getTicketSuggestionsList(),
                                            ticketSuggestionResponse.getEstimatedTime(), new Repo.Callback() {
                                                @Override
                                                public void success(Object o) {
                                                    GlobalUtils.showLog(TAG, "ticket suggestions saved");
                                                    getView().getTicketSuggestionSuccess();
                                                }

                                                @Override
                                                public void fail() {
                                                    GlobalUtils.showLog(TAG, "failed to save ticket" +
                                                            "suggestions");
                                                }
                                            });

                                } else {
                                    getView().onNoTicketSuggestion();
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                getView().getTicketSuggestionFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                            }
                        })
        );
    }

    @Override
    public void getConversationLabels() {
        Observable<ConversationRpcProto.ConversationBaseResponse> conversationObservable;
        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
        conversationObservable = service.getConversationLabels(token, selectedService);

        addSubscription(conversationObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ConversationRpcProto.ConversationBaseResponse>() {
                    @Override
                    public void onNext(@NonNull ConversationRpcProto.ConversationBaseResponse conversationBaseResponse) {
                        GlobalUtils.showLog(TAG, "get conversation label response:"
                                + conversationBaseResponse.getLabelsList());

                        if (conversationBaseResponse.getError()) {
                            getView().getConversationLabelFail(conversationBaseResponse.getMsg());
                            return;
                        }

                        saveConversationLabels(conversationBaseResponse.getLabelsList());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    private Retrofit getRetrofitInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .build();

        String base_url = Hawk.get(Constants.BASE_URL);
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(base_url)
                .addConverterFactory(ProtoConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Override
    public void filterMessages(String searchQuery, long from, long to, boolean followUp, boolean
            isImportant, RealmList<String> sources, RealmList<String> labels, boolean showProgress) {
        Observable<ConversationRpcProto.ConversationBaseResponse> messageBaseResponse;

        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String filterUrl = getMessageFilterUrl(searchQuery, from, to, followUp, isImportant,
                sources, labels);

        if (!filterUrl.isEmpty()) {
            if(showProgress)
            getView().showProgressBar("Filtering...");
            messageBaseResponse = service.filterMessages(token, filterUrl);
            addSubscription(messageBaseResponse
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(
                            new DisposableObserver<ConversationRpcProto.ConversationBaseResponse>() {
                                @Override
                                public void onNext(@NonNull ConversationRpcProto.ConversationBaseResponse
                                                           filterMessageBaseResponse) {
                                    GlobalUtils.showLog(TAG, "filter messages response: "
                                            + filterMessageBaseResponse);

                                    getView().hideProgressBar();

                                    if (filterMessageBaseResponse.getError()) {
                                        getView().filterMessagesFail(filterMessageBaseResponse.getMsg());
                                        return;
                                    }

                                    if (!CollectionUtils.isEmpty(
                                            filterMessageBaseResponse.getConversationsList())) {

                                        MessageFilterData filterData = createFilterData(searchQuery,
                                                from, to, followUp, isImportant, sources, labels);

                                        MessageFilterDataRepo.getInstance().saveFilterData(filterData, new Repo.Callback() {
                                            @Override
                                            public void success(Object o) {
                                                GlobalUtils.showLog(TAG, "message filter data saved");
                                            }

                                            @Override
                                            public void fail() {
                                                GlobalUtils.showLog(TAG, "failed to save message " +
                                                        "filter data");
                                            }
                                        });

                                        saveMessagesToRealm(filterMessageBaseResponse.getConversationsList());
                                    } else {
                                        getView().filterMessagesFail("Not found");
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    getView().hideProgressBar();
                                    getView().filterMessagesFail(e.getLocalizedMessage());
                                }

                                @Override
                                public void onComplete() {
                                    getView().hideProgressBar();
                                }
                            })
            );
        }
    }

    private void saveMessagesToRealm(List<ConversationProto.ConversationThread> conversationsList) {
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
        List<Thread> threadList = ThreadRepo.getInstance().getThreadsByServiceId(serviceId);
        if (CollectionUtils.isEmpty(threadList)) {
            saveMessages(conversationsList);
        } else {
            ThreadRepo.getInstance().deleteMessagesConsiderService(new Repo.Callback() {
                @Override
                public void success(Object o) {
                    GlobalUtils.showLog(TAG, "deleted messages");
                }

                @Override
                public void fail() {
                    GlobalUtils.showLog(TAG, "failed to delete messages");
                }
            });

            saveMessages(conversationsList);
        }
    }

    private void saveMessages(List<ConversationProto.ConversationThread> messagesList) {
        ThreadRepo.getInstance().saveThreads(messagesList,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().filterMessagesSuccess();
                    }

                    @Override
                    public void fail() {
                        GlobalUtils.showLog(TAG, "failed to filter messages");
                        getView().filterMessagesFail("filter messages failed");
                    }
                });
    }

    private void saveConversationLabels(List<ConversationProto.ConversationLabel> employeesList) {
        ConversationThreadLabelRepo.getInstance().saveLabelList(employeesList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved conversation labels");
                getView().getConversationLabelSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save conversation " +
                        "labels");
            }
        });
    }

    private String getMessageFilterUrl(String searchQuery, long
            from, long to, boolean followUp, boolean isImportant,
                                       List<String> sources, List<String> labels) {
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        StringBuilder filterUrlBuilder = new StringBuilder("conversation/service/" + serviceId + "?");

        if (searchQuery.isEmpty() && from == 0 && to == 0 && !followUp && !isImportant &&
                sources.isEmpty() && labels.isEmpty()) {
            Toast.makeText(getContext(), "Please enter filter terms", Toast.LENGTH_SHORT).show();
            return "";
        }

        if (!searchQuery.isEmpty()) {
            filterUrlBuilder.append("query=");
            filterUrlBuilder.append(searchQuery);
        }

        if (from != 0) {
            filterUrlBuilder.append("&from=");
            filterUrlBuilder.append(from);
        }

        if (to != 0) {
            filterUrlBuilder.append("&to=");
            filterUrlBuilder.append(to);
        }

        if (followUp) {
            filterUrlBuilder.append("&fu=TRUE");
        }

        if (isImportant) {
            filterUrlBuilder.append("&imp=TRUE");
        }

        if (sources != null && !sources.isEmpty()) {
            for (String sourceId : sources
            ) {
                filterUrlBuilder.append("&source=").append(sourceId);
            }
        }

        if (labels != null && !labels.isEmpty()) {
            for (String labelId : labels
            ) {
                filterUrlBuilder.append("&label=").append(labelId);
            }
        }
/*
        filterUrlBuilder.append("&sort=DESC");
        filterUrlBuilder.append("&page=200");*/
        return filterUrlBuilder.toString();
    }

    private MessageFilterData createFilterData(String searchQuery,
                                               long from, long to,
                                               boolean isFollowUp, boolean isImportant,
                                               RealmList<String> sources, RealmList<String> labels) {
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        MessageFilterData filterData = new MessageFilterData();
        filterData.setServiceId(serviceId);
        filterData.setFrom(from);
        filterData.setTo(to);
        filterData.setSearchQuery(searchQuery);
        filterData.setFollowUp(isFollowUp);
        filterData.setImportant(isImportant);
        filterData.setLabels(labels);
        filterData.setSources(sources);
        return filterData;
    }

}
