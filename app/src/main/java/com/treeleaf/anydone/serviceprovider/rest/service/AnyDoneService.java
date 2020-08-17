package com.treeleaf.anydone.serviceprovider.rest.service;

import com.treeleaf.anydone.entities.AuthProto;
import com.treeleaf.anydone.entities.BotConversationProto;
import com.treeleaf.anydone.entities.SearchServiceProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.AuthRpcProto;
import com.treeleaf.anydone.rpc.BotConversationRpcProto;
import com.treeleaf.anydone.rpc.OrderServiceRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.rpc.SearchServiceRpcProto;
import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface AnyDoneService {
    String API_BASE_URL = "https://api.anydone.net/";
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

    @POST("service/parse/search")
    Observable<SearchServiceRpcProto.SearchServiceBaseResponse>
    searchService(@Header(AUTHORIZATION) String token,
                  @Body SearchServiceProto.SearchServiceRequest searchServiceRequest);

    @GET("service/available")
    Observable<ServiceRpcProto.ServiceBaseResponse>
    getAllServices(@Header(AUTHORIZATION) String token);

    @GET
    Observable<ResponseBody> getPlaceAutocomplete(@Url String url);


    //    @GET("service/order/consumer?pageSize=20")
    @GET("service/order/consumer")
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse>
    getOrderService(@Header(AUTHORIZATION) String token);

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

    @PATCH("service/order/close")
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse> closeOrder(@Header(AUTHORIZATION)
                                                                                 String token,
                                                                         @Query("orderId")
                                                                                 long orderId);

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

    @GET("ticket")
    Observable<TicketServiceRpcProto.TicketBaseResponse>
    getAllTickets(@Header(AUTHORIZATION) String token);

    @GET("service/orders")
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse> getOpenServices(@Header(AUTHORIZATION)
                                                                                      String token);

    @GET("service/order/provider")
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse> getAcceptedServices
            (@Header(AUTHORIZATION) String token);

    //actions on orders
    @PATCH("service/order/accept")
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse> acceptOrder(@Header(AUTHORIZATION)
                                                                                  String token);

    @PATCH("service/order/cancel")
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse> acceptOrder(@Header(AUTHORIZATION)
                                                                                  String token,
                                                                          @Query("orderId") String orderId);

    @PATCH("service/assign")
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse> assignOrder(@Header(AUTHORIZATION)
                                                                                  String token);

    @PATCH("service/order/start")
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse> startOrder(@Header(AUTHORIZATION)
                                                                                 String token,
                                                                         @Query("orderId") String orderId);

    @PATCH("service/order/complete")
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse> completeOrder(@Header(AUTHORIZATION)
                                                                                    String token,
                                                                            @Query("orderId") String orderId);

    @PATCH("service/order/close")
    Observable<OrderServiceRpcProto.OrderServiceBaseResponse> closeOrder(@Header(AUTHORIZATION)
                                                                                 String token,
                                                                         @Query("orderId") String orderId);

    //actions on tickets
    @PATCH("ticket/start/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> startTicket(@Header(AUTHORIZATION)
                                                                             String token,
                                                                     @Path(value = "ticketId") String ticketId);

    @PATCH("ticket/complete/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> completeTicket(@Header(AUTHORIZATION)
                                                                                String token,
                                                                        @Path(value = "ticketId") String ticketId);

    @PATCH("ticket/close/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> closeTicket(@Header(AUTHORIZATION)
                                                                             String token,
                                                                     @Path(value = "ticketId") String ticketId,
                                                                     @Query("remark") String remark);

    @PATCH("ticket/reopen/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> reopenTicket(@Header(AUTHORIZATION)
                                                                              String token,
                                                                      @Path(value = "ticketId") long ticketId,
                                                                      @Query("remark") String remark);

    @GET("ticket/assigned")
    Observable<TicketServiceRpcProto.TicketBaseResponse> getAssignedTickets(@Header(AUTHORIZATION)
                                                                                    String token,
                                                                            @Query("from") long from,
                                                                            @Query("to") long to,
                                                                            @Query("page") int page);

    @GET("ticket/subscribed")
    Observable<TicketServiceRpcProto.TicketBaseResponse> getSubscribedTickets(@Header(AUTHORIZATION)
                                                                                      String token,
                                                                              @Query("from") long from,
                                                                              @Query("to") long to,
                                                                              @Query("page") int page);

    @GET("ticket/inactive")
    Observable<TicketServiceRpcProto.TicketBaseResponse> getClosedResolvedTickets(@Header(AUTHORIZATION)
                                                                                          String token,
                                                                                  @Query("from") long from,
                                                                                  @Query("to") long to,
                                                                                  @Query("page") int page);


    @GET("ticket/assignable")
    Observable<TicketServiceRpcProto.TicketBaseResponse> getAssignableTickets(@Header(AUTHORIZATION)
                                                                                      String token,
                                                                              @Query("from") long from,
                                                                              @Query("to") long to,
                                                                              @Query("page") int page);

    @GET("ticket/subscribable")
    Observable<TicketServiceRpcProto.TicketBaseResponse> getSubscribeableTickets(@Header(AUTHORIZATION)
                                                                                         String token,
                                                                                 @Query("from") long from,
                                                                                 @Query("to") long to,
                                                                                 @Query("page") int page);

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

    @PATCH("ticket/assign/{ticketId}")
    Observable<TicketServiceRpcProto.TicketBaseResponse> assignToSelf(@Header(AUTHORIZATION)
                                                                              String token,
                                                                      @Path(value = "ticketId")
                                                                              long ticketId,
                                                                      @Body TicketProto.Ticket employeeAssigned);


    @POST("ticket")
    Observable<TicketServiceRpcProto.TicketBaseResponse> createTicket(@Header(AUTHORIZATION)
                                                                              String token,
                                                                      TicketProto.Ticket ticket);

}



