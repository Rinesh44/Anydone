package com.treeleaf.anydone.serviceprovider.dashboard;

import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class DashboardRepositoryImpl implements DashboardRepository {
    private AnyDoneService anyDoneService;

    public DashboardRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<ServiceRpcProto.ServiceBaseResponse> getServices(String token) {
        return anyDoneService.getServices(token);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketByDate(String token, String serviceId) {
        return anyDoneService.getTicketByDate(token, serviceId);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> filterTicketByDate(String token, String serviceId, long from, long to) {
        return anyDoneService.filterTicketByDate(token, serviceId, from, to);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketByResolvedTime(String token, String serviceId) {
        return anyDoneService.getTicketByResolvedTime(token, serviceId);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> filterTicketByResolvedTime(String token, String serviceId, long from, long to) {
        return anyDoneService.filterTicketByResolvedTime(token, serviceId, from, to);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketByStatus(String token, String serviceId) {
        return anyDoneService.getTicketByStatus(token, serviceId);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> filterTicketByStatus(String token, String serviceId, long from, long to) {
        return anyDoneService.filterTicketByStatus(token, serviceId, from, to);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketByPriority(String token, String serviceId) {
        return anyDoneService.getTicketByPriority(token, serviceId);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> filterTicketByPriority(String token, String serviceId, long from, long to) {
        return anyDoneService.filterTicketByPriority(token, serviceId, from, to);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketBySource(String token, String serviceId) {
        return anyDoneService.getTicketBySource(token, serviceId);
    }

    @Override
    public Observable<TicketServiceRpcProto.TicketBaseResponse> filterTicketBySource(String token, String serviceId, long from, long to) {
        return anyDoneService.filterTicketBySource(token, serviceId, from, to);
    }
}
