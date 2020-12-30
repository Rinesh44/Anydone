package com.treeleaf.anydone.serviceprovider.rest.service;

import com.treeleaf.anydone.entities.AuthProto;
import com.treeleaf.anydone.entities.BotConversationProto;
import com.treeleaf.anydone.entities.ConversationProto;
import com.treeleaf.anydone.entities.PaymentProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.AuthRpcProto;
import com.treeleaf.anydone.rpc.BotConversationRpcProto;
import com.treeleaf.anydone.rpc.ConversationRpcProto;
import com.treeleaf.anydone.rpc.OrderServiceRpcProto;
import com.treeleaf.anydone.rpc.PaymentRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface AnyDoneService {
    //    String API_BASE_URL = "https://api.anydone.net/";
    String AUTHORIZATION = "authorization";

    @POST("login")
    Observable<AuthRpcProto.AuthBaseResponse> login(@Body AuthProto.LoginRequest loginRequest);

    @POST("verify")
    Observable<UserRpcProto.UserBaseResponse>
    verifyUser(@Body UserProto.UserVerification userVerification);

    @POST("password/reset/request")
    Observable<UserRpcProto.UserBaseResponse>
    forgotPassword(@Query(value = "emailPhone") String emailPhone);

    @POST("verify/phone")
    Observable<UserRpcProto.UserBaseResponse>
    verifyCodeWithPhone(@Body UserProto.UserVerification userVerification);

    @POST("verify/email")
    Observable<UserRpcProto.UserBaseResponse>
    verifyCodeWithEmail(@Body UserProto.UserVerification userVerification);

    @PATCH("password/reset")
    Observable<UserRpcProto.UserBaseResponse>
    resetPassword(@Body UserProto.PasswordReset passwordReset);

    @PATCH("verify")
    Observable<UserRpcProto.UserBaseResponse>
    resendCode(@Query(value = "emailPhone") String emailPhone);

    @DELETE("logout")
    Observable<AuthRpcProto.AuthBaseResponse> logout(@Header(AUTHORIZATION) String authorization);

    @PATCH("password")
    Observable<UserRpcProto.UserBaseResponse>
    changePassword(@Header(AUTHORIZATION) String token,
                   @Body UserProto.PasswordChangeRequest passwordChangeRequest);

    @PATCH("employee")
    Observable<UserRpcProto.UserBaseResponse>
    editEmployeeProfile(@Header(AUTHORIZATION) String token,
                        @Body UserProto.EmployeeProfile employeeProfile);

    @PATCH("serviceprovider")
    Observable<UserRpcProto.UserBaseResponse>
    editServiceProviderProfile(@Header(AUTHORIZATION) String token,
                               @Body UserProto.ServiceProviderProfile serviceProviderProfile);

    @PATCH("phone")
    Observable<UserRpcProto.UserBaseResponse>
    addPhone(@Header(AUTHORIZATION) String token, @Query(value = "phone") String phone);

    @PATCH("email")
    Observable<UserRpcProto.UserBaseResponse>
    addEmail(@Header(AUTHORIZATION) String token, @Query("email") String email);

    @Multipart
    @POST("upload/dp")
    Observable<UserRpcProto.UserBaseResponse>
    uploadImage(@Header(AUTHORIZATION) String authorization, @Part MultipartBody.Part image);

    @GET
    Observable<ResponseBody> getPlaceAutocomplete(@Url String url);

    @GET
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse>
    filterServiceRequests(@Header(AUTHORIZATION) String token, @Url String url);

    @GET("service/order/consumer")
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse>
    filterServiceRequests(@Header(AUTHORIZATION) String token,
                          @Query("service") String serviceName,
                          @Query("from") long from,
                          @Query("to") long to,
                          @Query("serviceState") String status);

    @Multipart
    @POST("upload/image")
    Observable<UserRpcProto.UserBaseResponse>
    imageUploadConversation(@Header(AUTHORIZATION) String token, @Part MultipartBody.Part image);

    @Multipart
    @POST("upload/doc")
    Observable<UserRpcProto.UserBaseResponse>
    docUploadConversation(@Header(AUTHORIZATION) String token, @Part MultipartBody.Part doc);

    @GET("service/order/employee")
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse>
    getServiceDoers(@Header(AUTHORIZATION) String token, @Query(value = "orderId") long orderId);

    @GET("rtc/messages/{refId}")
    Observable<RtcServiceRpcProto.RtcServiceBaseResponse>
    getMessages(@Header(AUTHORIZATION) String token,
                @Path(value = "refId") long refId,
                @Query("from") long from,
                @Query("to") long to,
                @Query("pageSize") int pageSize);

    @GET("rtc/messages/{ticketId}")
    Observable<RtcServiceRpcProto.RtcServiceBaseResponse>
    getTicketMessages(@Header(AUTHORIZATION) String token,
                      @Path(value = "ticketId") long refId,
                      @Query("from") long from,
                      @Query("to") long to,
                      @Query("pageSize") int pageSize,
                      @Query("context") int context);

    @PATCH("service/order/cancel")
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse> cancelOrder(@Header(AUTHORIZATION)
                                                                                  String token,
                                                                          @Query("orderId")
                                                                                  long orderId);

    @PATCH("settings/timezone")
    Observable<UserRpcProto.UserBaseResponse> addTimezone(@Header(AUTHORIZATION) String token,
                                                          @Query("timezone") String timezone);

    @PATCH("settings/currency")
    Observable<UserRpcProto.UserBaseResponse> addCurrency(@Header(AUTHORIZATION) String token,
                                                          @Query("currency") String currency);

    @POST("settings/location")
    Observable<UserRpcProto.UserBaseResponse> addLocation(@Header(AUTHORIZATION) String token,
                                                          @Body UserProto.Location location);

    @PATCH("settings/language")
    Observable<UserRpcProto.UserBaseResponse> changeLanguage(@Header(AUTHORIZATION) String token,
                                                             @Query("lang") String language);

    @GET("av/connect/detail")
    Observable<RtcServiceRpcProto.RtcServiceBaseResponse> getJanusBaseUrl(@Header(AUTHORIZATION)
                                                                                  String token);

    @PATCH
    Observable<UserRpcProto.UserBaseResponse> makeLocationDefault(@Header(AUTHORIZATION)
                                                                          String token,
                                                                  @Url String url);

    @DELETE
    Observable<UserRpcProto.UserBaseResponse> deleteLocation(@Header(AUTHORIZATION)
                                                                     String token,
                                                             @Url String url);

    @POST("bot/faq")
    Observable<BotConversationRpcProto.BotConversationBaseResponse>
    getSuggestions(@Header(AUTHORIZATION) String token,
                   @Body BotConversationProto.ConversationRequest request);


    @POST("bot/faq")
    Observable<BotConversationRpcProto.BotConversationBaseResponse>
    getTicketSuggestions(@Header(AUTHORIZATION) String token,
                         @Body BotConversationProto.ConversationRequest request);

    @GET("service/orders")
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse> getOpenServices(@Header(AUTHORIZATION)
                                                                                      String token);

    @GET("service/order/provider")
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse> getAcceptedServices
            (@Header(AUTHORIZATION) String token);

    //actions on tickets
    @PATCH("ticket/start/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> startTicket(@Header(AUTHORIZATION)
                                                                             String token,
                                                                     @Path(value = "ticketId")
                                                                             String ticketId);

    @PATCH("ticket/complete/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> resolveTicket(@Header(AUTHORIZATION)
                                                                               String token,
                                                                       @Path(value = "ticketId")
                                                                               long ticketId);

    @PATCH("ticket/close/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> closeTicket(@Header(AUTHORIZATION)
                                                                             String token,
                                                                     @Path(value = "ticketId")
                                                                             long ticketId,
                                                                     @Query("remark")
                                                                             String remark);

    @PATCH("ticket/reopen/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> reopenTicket(@Header(AUTHORIZATION)
                                                                              String token,
                                                                      @Path(value = "ticketId")
                                                                              long ticketId,
                                                                      @Query("remark")
                                                                              String remark);

    @GET("ticket/assigned/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    getAssignedTickets(@Header(AUTHORIZATION)
                               String token,
                       @Path(value = "serviceId")
                               String serviceId,
                       @Query("from") long from,
                       @Query("to") long to,
                       @Query("page") int page,
                       @Query("sort") String order);

    @GET("ticket/pending/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    getPendingTickets(@Header(AUTHORIZATION)
                              String token,
                      @Path(value = "serviceId")
                              String serviceId,
                      @Query("from") long from,
                      @Query("to") long to,
                      @Query("page") int page,
                      @Query("sort") String order);

    @GET("ticket/inprogress/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    getInProgressTickets(@Header(AUTHORIZATION)
                                 String token,
                         @Path(value = "serviceId")
                                 String serviceId,
                         @Query("from") long from,
                         @Query("to") long to,
                         @Query("page") int page,
                         @Query("sort") String order);


    @GET("ticket/contributed/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    getContributedTickets(@Header(AUTHORIZATION)
                                  String token,
                          @Path(value = "serviceId")
                                  String serviceId,
                          @Query("from") long from,
                          @Query("to") long to,
                          @Query("page") int page,
                          @Query("sort") String order);


    @GET("ticket/subscribed/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    getSubscribedTickets(@Header(AUTHORIZATION)
                                 String token,
                         @Path(value = "serviceId")
                                 String serviceId,
                         @Query("from") long from,
                         @Query("to") long to,
                         @Query("page") int page,
                         @Query("sort") String order);

    @GET("ticket/inactive/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    getClosedResolvedTickets(@Header(AUTHORIZATION)
                                     String token,
                             @Path(value = "serviceId")
                                     String serviceId,
                             @Query("from") long from,
                             @Query("to") long to,
                             @Query("page") int page,
                             @Query("sort") String order);

    @GET("ticket/backlog/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    getBacklogTickets(@Header(AUTHORIZATION)
                              String token,
                      @Path(value = "serviceId")
                              String serviceId,
                      @Query("from") long from,
                      @Query("to") long to,
                      @Query("page") int page,
                      @Query("sort") String order);

    @GET("ticket/subscribable/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    getSubscribeableTickets(@Header(AUTHORIZATION)
                                    String token,
                            @Path(value = "serviceId")
                                    String serviceId,
                            @Query("from") long from,
                            @Query("to") long to,
                            @Query("page") int page,
                            @Query("sort") String order);

    @PATCH("ticket/unsubscribe/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> unsubscribe(@Header(AUTHORIZATION)
                                                                             String token,
                                                                     @Path(value = "ticketId")
                                                                             long ticketId);

    @PATCH("ticket/subscribe/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> subscribe(@Header(AUTHORIZATION)
                                                                           String token,
                                                                   @Path(value = "ticketId")
                                                                           long ticketId);

    @PATCH("ticket/assign/{ticketId}/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> assignToSelf(@Header(AUTHORIZATION)
                                                                              String token,
                                                                      @Path(value = "ticketId")
                                                                              long ticketId,
                                                                      @Path(value = "serviceId")
                                                                              String serviceId,
                                                                      @Body TicketProto.Ticket
                                                                              employeeAssigned);


    @POST("ticket")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    createTicket(@Header(AUTHORIZATION)
                         String token,
                 @Body TicketProto.Ticket ticket);

    @GET("customers")
    Observable<UserRpcProto.UserBaseResponse> findCustomers(@Header(AUTHORIZATION)
                                                                    String token,
                                                            @Query("query") String query,
                                                            @Query("from") long from,
                                                            @Query("to") long to,
                                                            @Query("pageSize") int pageSize);


    @GET("employees")
    Observable<UserRpcProto.UserBaseResponse> findEmployees(@Header(AUTHORIZATION)
                                                                    String token);

    @GET("consumers/service/{serviceId}")
    Observable<UserRpcProto.UserBaseResponse> findConsumers(@Header(AUTHORIZATION)
                                                                    String token,
                                                            @Path(value = "serviceId")
                                                                    String serviceId);

    @GET("ticket/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketTimeline(@Header(AUTHORIZATION)
                                                                                   String token,
                                                                           @Path(value = "ticketId")
                                                                                   long ticketId);

    @GET
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    filterTickets(@Header(AUTHORIZATION) String token, @Url String url);


    @PATCH("ticket/assign/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    assignEmployee(@Header(AUTHORIZATION)
                           String token,
                   @Path(value = "ticketId")
                           long ticketId,
                   @Body TicketProto.Ticket employeeAssigned);

    @PATCH("ticket/unassign/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    unAssignEmployee(@Header(AUTHORIZATION)
                             String token,
                     @Path(value = "ticketId")
                             long ticketId,
                     @Body TicketProto.Ticket employeeUnAssigned);

    @GET("/service/created")
    Observable<ServiceRpcProto.ServiceBaseResponse> getServices(@Header(AUTHORIZATION)
                                                                        String token);


    @GET("conversation/service/{serviceId}")
    Observable<ConversationRpcProto.ConversationBaseResponse>
    getConversationThreads(@Header(AUTHORIZATION)
                                   String token,
                           @Path(value = "serviceId")
                                   String serviceId);

    @GET("conversation/ticket/{threadId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    getLinkedTickets(@Header(AUTHORIZATION)
                             String token,
                     @Path(value = "threadId")
                             String threadId);

    @GET("conversation/{threadId}")
    Observable<ConversationRpcProto.ConversationBaseResponse>
    getConversationThreadById(@Header(AUTHORIZATION)
                                      String token,
                              @Path(value = "threadId")
                                      String threadId);

    @PATCH("conversation/{refId}/enablebotreply")
    Observable<RtcServiceRpcProto.RtcServiceBaseResponse>
    enableThreadBotReply(@Header(AUTHORIZATION)
                                 String token,
                         @Path(value = "refId")
                                 String refId);

    @PATCH("conversation/{refId}/disablebotreply")
    Observable<RtcServiceRpcProto.RtcServiceBaseResponse>
    disableThreadBotReply(@Header(AUTHORIZATION)
                                  String token,
                          @Path(value = "refId")
                                  String refId);

    @PATCH("ticket/{refId}/enablebotreply")
    Observable<RtcServiceRpcProto.RtcServiceBaseResponse>
    enableTicketBotReply(@Header(AUTHORIZATION)
                                 String token,
                         @Path(value = "refId")
                                 String refId);

    @PATCH("ticket/{refId}/disablebotreply")
    Observable<RtcServiceRpcProto.RtcServiceBaseResponse>
    disableTicketBotReply(@Header(AUTHORIZATION)
                                  String token,
                          @Path(value = "refId")
                                  String refId);

    @GET("rtc/messages/{refId}")
    Observable<RtcServiceRpcProto.RtcServiceBaseResponse>
    getThreadMessages(@Header(AUTHORIZATION) String token,
                      @Path(value = "refId") String refId,
                      @Query("from") long from,
                      @Query("to") long to,
                      @Query("pageSize") int pageSize,
                      @Query("context") int context);


    @POST("ticket/contributor/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> addContributors(@Header(AUTHORIZATION)
                                                                                 String token,
                                                                         @Path(value = "ticketId")
                                                                                 String ticketId,
                                                                         @Body TicketProto.Ticket
                                                                                 ticket);

    @HTTP(method = "DELETE", path = "ticket/contributor/{ticketId}", hasBody = true)
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    deleteContributor(@Header(AUTHORIZATION)
                              String token,
                      @Path(value = "ticketId")
                              String ticketId,
                      @Body TicketProto.TicketContributor
                              ticketContributor);

    @GET("tickets/date/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    filterTicketByDate(@Header(AUTHORIZATION)
                               String token,
                       @Path(value = "serviceId")
                               String serviceId,
                       @Query("from") long from,
                       @Query("to") long to);

    @GET("tickets/date/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketByDate(@Header(AUTHORIZATION)
                                                                                 String token,
                                                                         @Path(value = "serviceId")
                                                                                 String serviceId);

    @GET("tickets/status/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    getTicketByStatus(@Header(AUTHORIZATION)
                              String token,
                      @Path(value = "serviceId")
                              String serviceId);

    @GET("tickets/status/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    filterTicketByStatus(@Header(AUTHORIZATION)
                                 String token,
                         @Path(value = "serviceId")
                                 String serviceId,
                         @Query("from") long from,
                         @Query("to") long to);

    @GET("tickets/source/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    getTicketBySource(@Header(AUTHORIZATION)
                              String token,
                      @Path(value = "serviceId")
                              String serviceId);

    @GET("tickets/source/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    filterTicketBySource(@Header(AUTHORIZATION)
                                 String token,
                         @Path(value = "serviceId")
                                 String serviceId,
                         @Query("from") long from,
                         @Query("to") long to);

    @GET("tickets/priority/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    getTicketByPriority(@Header(AUTHORIZATION)
                                String token,
                        @Path(value = "serviceId")
                                String serviceId);

    @GET("tickets/priority/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    filterTicketByPriority(@Header(AUTHORIZATION)
                                   String token,
                           @Path(value = "serviceId")
                                   String serviceId,
                           @Query("from") long from,
                           @Query("to") long to);

    @GET("tickets/resolve/time/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    getTicketByResolvedTime(@Header(AUTHORIZATION)
                                    String token,
                            @Path(value = "serviceId")
                                    String serviceId);

    @GET("tickets/resolve/time/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    filterTicketByResolvedTime(@Header(AUTHORIZATION)
                                       String token,
                               @Path(value = "serviceId")
                                       String serviceId,
                               @Query("from") long from,
                               @Query("to") long to);

    @POST("ticket/link/generate")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    getLink(@Header(AUTHORIZATION)
                    String token,
            @Body TicketProto.GetSharableLinkRequest
                    sharableLinkRequest);

    @POST("conversation/assigned/employees")
    Observable<ConversationRpcProto.ConversationBaseResponse>
    assignEmployeeToThread(@Header(AUTHORIZATION)
                                   String token,
                           @Body ConversationProto.ConversationThread
                                   conversationThread);

    @GET("payment/card/all")
    Observable<PaymentRpcProto.PaymentBaseResponse> getPaymentCards(@Header(AUTHORIZATION)
                                                                            String token);

    @POST("payment/card")
    Observable<PaymentRpcProto.PaymentBaseResponse> addPaymentCard(@Header(AUTHORIZATION) String
                                                                           token,
                                                                   @Body PaymentProto.Card card);

    @PATCH("payment/card/default/{cardId}")
    Observable<PaymentRpcProto.PaymentBaseResponse> setPaymentCardAsPrimary(@Header(AUTHORIZATION)
                                                                                    String token,
                                                                            @Path(value = "cardId")
                                                                                    String cardId);

    @DELETE("payment/card/{cardId}")
    Observable<PaymentRpcProto.PaymentBaseResponse> deletePaymentCard(@Header(AUTHORIZATION) String
                                                                              token,
                                                                      @Path(value = "cardId")
                                                                              String cardId);

    @GET("ticket/type/service/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketTypes(@Header(AUTHORIZATION)
                                                                                String token,
                                                                        @Path(value = "serviceId")
                                                                                String serviceId);

    @GET("ticket/label/service/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketLabels(@Header(AUTHORIZATION)
                                                                                 String token,
                                                                         @Path(value = "serviceId")
                                                                                 String serviceId);


    @GET("team/service/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketTeams(@Header(AUTHORIZATION)
                                                                                String token,
                                                                        @Path(value = "serviceId")
                                                                                String serviceId);

    @PATCH("ticket/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    editTicket(@Header(AUTHORIZATION)
                       String token,
               @Path(value = "ticketId")
                       String ticketId,
               @Body TicketProto.Ticket ticket);

    @PATCH("ticket/{ticketId}/team")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    editTeam(@Header(AUTHORIZATION)
                     String token,
             @Path(value = "ticketId")
                     String ticketId,
             @Body TicketProto.Ticket ticket);

    @PATCH("ticket/{ticketId}/label")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    editLabel(@Header(AUTHORIZATION)
                      String token,
              @Path(value = "ticketId")
                      String ticketId,
              @Body TicketProto.Ticket ticket);

    @GET("ticket/suggestions/service/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketSuggestions(@Header(AUTHORIZATION)
                                                                                      String token,
                                                                              @Path(value = "serviceId")
                                                                                      String serviceId);

    @GET("ticket/suggestions/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketSuggestionById(@Header(AUTHORIZATION)
                                                                                         String token,
                                                                                 @Path(value = "serviceId")
                                                                                         String serviceId);

    @PATCH("ticket/suggestions/accept/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> acceptTicketSuggestion(@Header(AUTHORIZATION)
                                                                                        String token,
                                                                                @Path(value = "serviceId")
                                                                                        String serviceId,
                                                                                @Body TicketProto.TicketSuggestionReq
                                                                                        ticketSuggestionReq);

    @PATCH("ticket/suggestions/reject/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> rejectTicketSuggestion(@Header(AUTHORIZATION)
                                                                                        String token,
                                                                                @Path(value = "serviceId")
                                                                                        String serviceId,
                                                                                @Body TicketProto.TicketSuggestionReq
                                                                                        ticketSuggestionReq);

    @GET("ticket/auto/fill/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> getSummarySuggestions(@Header(AUTHORIZATION)
                                                                                       String token,
                                                                               @Path(value = "serviceId")
                                                                                       String serviceId,
                                                                               @Query("summary") String summary,
                                                                               @Query("id") String ticketId);


    @GET("ticket/service/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> getDependencyTickets(@Header(AUTHORIZATION)
                                                                                      String token,
                                                                              @Path(value = "serviceId")
                                                                                      String serviceId);

    @GET("ticket/search/{serviceId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> searchDependentTickets(@Header(AUTHORIZATION)
                                                                                        String token,
                                                                                @Path(value = "serviceId")
                                                                                        String serviceId,
                                                                                @Query("query")
                                                                                        String query);

    @GET("ticket/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketById(@Header(AUTHORIZATION)
                                                                               String token,
                                                                       @Path(value = "ticketId")
                                                                               long ticketId);

    @PATCH("ticket/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> updateTicket(@Header(AUTHORIZATION)
                                                                              String token,
                                                                      @Path(value = "ticketId")
                                                                              long ticketId,
                                                                      @Body TicketProto.Ticket ticket);

    @PATCH("ticket/attachments")
    Observable<TicketServiceRpcProto.TicketBaseResponse> addAttachment(@Header(AUTHORIZATION)
                                                                               String token);
}



