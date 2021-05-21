package com.anydone.desk.ticketdetails.ticketactivitylog;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.repo.ActivityLogRepo;
import com.anydone.desk.realm.repo.Repo;
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
import retrofit2.Retrofit;

public class TicketActivityLogPresenterImpl extends BasePresenter<TicketActivityLogContract.TicketActivityLogView>
        implements TicketActivityLogContract.TicketActivityLogPresenter {
    private TicketActivityLogRepository ticketActivityLogRepository;
    private static final String TAG = "TicketActivityLogPresen";

    @Inject
    public TicketActivityLogPresenterImpl(TicketActivityLogRepository ticketActivityLogRepository) {
        this.ticketActivityLogRepository = ticketActivityLogRepository;
    }

    @Override
    public void getActivityLog(String ticketId, boolean showProgress) {
        if(showProgress)
        getView().showProgressBar("Please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String token = Hawk.get(Constants.TOKEN);

        ticketObservable = service.getActivityLog(token,
                String.valueOf(ticketId));
        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(@NonNull TicketServiceRpcProto.TicketBaseResponse
                                               activityLogResponse) {
                        GlobalUtils.showLog(TAG, "activity log response: " +
                                activityLogResponse);

                        getView().hideProgressBar();

                        if (activityLogResponse.getError()) {
                            getView().getActivityLogFail(activityLogResponse.getMsg());
                            return;
                        }

                        saveActivityLogs(activityLogResponse.getTicketActivityLogsList());
                        getView().getActivityLogSuccess();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().getActivityLogFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );
    }

    private void saveActivityLogs(List<TicketProto.TicketActivityLog> ticketActivityLogsList) {
        ActivityLogRepo.getInstance().saveLogs(ticketActivityLogsList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "activity log saved");
                getView().getActivityLogSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save activity log");
            }
        });
    }
}
