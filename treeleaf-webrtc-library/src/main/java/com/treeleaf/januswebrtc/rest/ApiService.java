package com.treeleaf.januswebrtc.rest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.math.BigInteger;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {


    @POST("/janus")
    Observable<JsonElement> createSession(@Body JsonObject jsonObject);

    @GET("{sessionId}")
    Observable<JsonElement> longPollReq(@Path("sessionId") BigInteger sessionId, @Query("apisecret") String apiSecret);

    @POST("{sessionId}")
    Observable<JsonElement> attachVideoPlugin(@Path("sessionId") BigInteger sessionId, @Body JsonObject jsonObject);

    @POST("{sessionId}/{pluginEndPointId}")
    Observable<JsonElement> listParticipants(@Path("sessionId") BigInteger sessionId, @Path("pluginEndPointId") BigInteger pluginEndPointId, @Body JsonObject jsonObject);

    @POST("{sessionId}/{pluginEndPointId}")
    Observable<JsonElement> createVideoRoom(@Path("sessionId") BigInteger sessionId, @Path("pluginEndPointId") BigInteger pluginEndPointId, @Body JsonObject jsonObject);

    @POST("{sessionId}/{pluginEndPointId}")
    Observable<JsonElement> joinVideoRoom(@Path("sessionId") BigInteger sessionId, @Path("pluginEndPointId") BigInteger pluginEndPointId, @Body JsonObject jsonObject);

    @POST("{sessionId}/{pluginEndPointId}")
    Observable<JsonElement> publishVideo(@Path("sessionId") BigInteger sessionId, @Path("pluginEndPointId") BigInteger pluginEndPointId, @Body JsonObject jsonObject);

    @POST("{sessionId}/{pluginEndPointId}")
    Observable<JsonElement> startVideo(@Path("sessionId") BigInteger sessionId, @Path("pluginEndPointId") BigInteger pluginEndPointId, @Body JsonObject jsonObject);

    @POST("{sessionId}/{pluginEndPointId}")
    Observable<JsonElement> unPublish(@Path("sessionId") BigInteger sessionId, @Path("pluginEndPointId") BigInteger pluginEndPointId, @Body JsonObject jsonObject);

    @POST("{sessionId}/{pluginEndPointId}")
    Observable<JsonElement> detachPlugin(@Path("sessionId") BigInteger sessionId, @Path("pluginEndPointId") BigInteger pluginEndPointId, @Body JsonObject jsonObject);

    @POST("{sessionId}")
    Observable<JsonElement> destroySession(@Path("sessionId") BigInteger sessionId, @Body JsonObject jsonObject);

}
