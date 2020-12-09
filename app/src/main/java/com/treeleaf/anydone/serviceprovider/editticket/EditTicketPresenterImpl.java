package com.treeleaf.anydone.serviceprovider.editticket;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class EditTicketPresenterImpl extends BasePresenter<EditTicketContract.EditTicketView>
        implements EditTicketContract.EditTicketPresenter {
    private static final String TAG = "EditTicketPresenterImpl";
    private EditTicketRepository editTicketRepository;

    @Inject
    public EditTicketPresenterImpl(EditTicketRepository editTicketRepository) {
        this.editTicketRepository = editTicketRepository;
    }

    @Override
    public void editTicketTitle(String ticketId, String title) {
        getView().showProgressBar("Please wait");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        Tickets tickets = TicketRepo.getInstance().getTicketById(Long.parseLong(ticketId));

        TicketProto.TicketType ticketType = TicketProto.TicketType.newBuilder()
                .setTicketTypeId(tickets.getTicketCategoryId())
                .build();
        TicketProto.Ticket ticket = TicketProto.Ticket.newBuilder()
                .setTitle(title)
                .setPriority(getTicketPriority(tickets.getPriority()))
                .setDescription(tickets.getDescription())
                .setType(ticketType)
                .build();

        ticketObservable = service.editTicket(token, ticketId, ticket);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse response) {
                        GlobalUtils.showLog(TAG, "edit title response:"
                                + response);

                        getView().hideProgressBar();
                        if (response == null) {
                            getView().onTitleEditFail("Failed to edit title");
                            return;
                        }

                        if (response.getError()) {
                            getView().onTitleEditFail(response.getMsg());
                            return;
                        }

                        TicketRepo.getInstance().editTicketTitle(Long.parseLong(ticketId), title);
                        getView().onTitleEditSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onTitleEditFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Override
    public void editTicketDesc(String ticketId, String desc) {
        getView().showProgressBar("Please wait");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        Tickets tickets = TicketRepo.getInstance().getTicketById(Long.parseLong(ticketId));
        TicketProto.TicketType ticketType = TicketProto.TicketType.newBuilder()
                .setTicketTypeId(tickets.getTicketCategoryId())
                .build();
        TicketProto.Ticket ticket = TicketProto.Ticket.newBuilder()
                .setTitle(tickets.getTitle())
                .setPriority(getTicketPriority(tickets.getPriority()))
                .setDescription(desc)
                .setType(ticketType)
                .build();

        ticketObservable = service.editTicket(token, ticketId, ticket);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse response) {
                        GlobalUtils.showLog(TAG, "edit desc response:"
                                + response);

                        getView().hideProgressBar();
                        if (response == null) {
                            getView().onDescEditFail("Failed to edit desc");
                            return;
                        }

                        if (response.getError()) {
                            getView().onDescEditFail(response.getMsg());
                            return;
                        }

                        TicketRepo.getInstance().editTicketDescription(Long.parseLong(ticketId), desc);
                        getView().onDescEditSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onDescEditFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Override
    public void editTicketEstimatedTime(String ticketId, String estimatedTime) {
        getView().showProgressBar("Please wait");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        Tickets tickets = TicketRepo.getInstance().getTicketById(Long.parseLong(ticketId));
        TicketProto.TicketType ticketType = TicketProto.TicketType.newBuilder()
                .setTicketTypeId(tickets.getTicketCategoryId())
                .build();


        GlobalUtils.showLog(TAG, "estimated time check: " + estimatedTime);
        TicketProto.Ticket ticket = TicketProto.Ticket.newBuilder()
                .setTitle(tickets.getTitle())
                .setPriority(getTicketPriority(tickets.getPriority()))
                .setDescription(tickets.getDescription())
                .setEstimatedTimeDesc(estimatedTime)
                .setType(ticketType)
                .build();

        ticketObservable = service.editTicket(token, ticketId, ticket);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse response) {
                        GlobalUtils.showLog(TAG, "edit estimated time response:"
                                + response);

                        getView().hideProgressBar();
                        if (response == null) {
                            getView().onEstimatedTimeEditFail("Failed to edit est time");
                            return;
                        }

                        if (response.getError()) {
                            getView().onEstimatedTimeEditFail(response.getMsg());
                            return;
                        }


                        TicketRepo.getInstance().editTicketEstimatedTime(Long.parseLong(ticketId),
                                estimatedTime, response.getTicket().getEstimatedTime());
                        getView().onEstimatedTimeEditSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onEstimatedTimeEditFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    private TicketProto.TicketPriority getTicketPriority(int priority) {
        switch (priority) {
            case 1:
                return TicketProto.TicketPriority.LOWEST_TICKET_PRIORITY;

            case 2:
                return TicketProto.TicketPriority.LOW_TICKET_PRIORITY;

            case 3:
                return TicketProto.TicketPriority.MEDIUM_TICKET_PRIORITY;

            case 4:
                return TicketProto.TicketPriority.HIGH_TICKET_PRIORITY;

            case 5:
                return TicketProto.TicketPriority.HIGHEST_TICKET_PRIORITY;
        }
        return TicketProto.TicketPriority.UNKNOWN_TICKET_PRIORITY;
    }
}

