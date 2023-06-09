package com.anydone.desk.ticketdetails.tickettimeline;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface TicketTimelineRepository {

    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketTimeline(String token,
                                                                           long ticketId);

    Observable<TicketServiceRpcProto.TicketBaseResponse>
    unAssignContributor(String token,
                        String ticketId,
                        TicketProto.TicketContributor
                                ticketContributor);

    Observable<TicketServiceRpcProto.TicketBaseResponse> resolveTicket(String token,
                                                                       long ticketId);

    Observable<TicketServiceRpcProto.TicketBaseResponse> reopenTicket(String token,
                                                                      long ticketId,
                                                                      String remark);

    Observable<TicketServiceRpcProto.TicketBaseResponse> closeTicket(String token,
                                                                     long ticketId,
                                                                     String remark);

    Observable<UserRpcProto.UserBaseResponse> findEmployees(String token);

    Observable<TicketServiceRpcProto.TicketBaseResponse> assignTicket(String token,
                                                                      long ticketId,
                                                                      TicketProto.Ticket assignedEmployee);

    Observable<RtcServiceRpcProto.RtcServiceBaseResponse> enableBot(String token, String ticketId);

    Observable<RtcServiceRpcProto.RtcServiceBaseResponse> disableBot(String token, String ticketId);


}
