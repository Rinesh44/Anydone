package com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;

import io.reactivex.Observable;

public interface TicketTimelineRepository {

    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketTimeline(String token,
                                                                           long ticketId);

    Observable<TicketServiceRpcProto.TicketBaseResponse> unAssignEmployee(String token,
                                                                          long ticketId,
                                                                          TicketProto.Ticket
                                                                                  employeeUnAssign);

    Observable<TicketServiceRpcProto.TicketBaseResponse> resolveTicket(String token,
                                                                       long ticketId);

    Observable<TicketServiceRpcProto.TicketBaseResponse> reopenTicket(String token,
                                                                      long ticketId,
                                                                      String remark);

    Observable<TicketServiceRpcProto.TicketBaseResponse> closeTicket(String token,
                                                                     long ticketId,
                                                                     String remark);

}
