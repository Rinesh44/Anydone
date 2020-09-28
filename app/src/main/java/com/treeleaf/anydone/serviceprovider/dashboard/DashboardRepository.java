package com.treeleaf.anydone.serviceprovider.dashboard;

import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface DashboardRepository {
    Observable<ServiceRpcProto.ServiceBaseResponse> getServices(String token);

    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketByDate(String token,
                                                                         String serviceId);

    Observable<TicketServiceRpcProto.TicketBaseResponse> filterTicketByDate(String token,
                                                                            String serviceId,
                                                                            long from,
                                                                            long to);

    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketByResolvedTime(String token,
                                                                                 String serviceId);

    Observable<TicketServiceRpcProto.TicketBaseResponse> filterTicketByResolvedTime(String token,
                                                                                    String serviceId,
                                                                                    long from,
                                                                                    long to);

    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketByStatus(String token,
                                                                           String serviceId);

    Observable<TicketServiceRpcProto.TicketBaseResponse> filterTicketByStatus(String token,
                                                                              String serviceId,
                                                                              long from,
                                                                              long to);

    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketByPriority(String token,
                                                                             String serviceId);

    Observable<TicketServiceRpcProto.TicketBaseResponse> filterTicketByPriority(String token,
                                                                                String serviceId,
                                                                                long from,
                                                                                long to);

    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketBySource(String token,
                                                                           String serviceId);


    Observable<TicketServiceRpcProto.TicketBaseResponse> filterTicketBySource(String token,
                                                                              String serviceId,
                                                                              long from,
                                                                              long to);


}
