package com.treeleaf.anydone.serviceprovider.assignemployee;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;

public interface AssignEmployeeRepository {

    Observable<TicketServiceRpcProto.TicketBaseResponse> assignEmployee(String token, long ticketId,
                                                                        TicketProto.Ticket employees);

    Observable<UserRpcProto.UserBaseResponse> findEmployees(String token);

}
