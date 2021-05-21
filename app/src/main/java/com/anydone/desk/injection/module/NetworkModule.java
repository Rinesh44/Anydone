package com.anydone.desk.injection.module;

import com.anydone.desk.BuildConfig;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .addNetworkInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    requestBuilder.header("Content-Type", "application/protobuf");
                    requestBuilder.header("Accept", "application/protobuf");
                    return chain.proceed(requestBuilder.build());
                })
                .writeTimeout(3, TimeUnit.MINUTES);

        okHttpClient.addInterceptor(logging);
        return okHttpClient.build();
    }

    @Provides
    @Singleton
    Retrofit getRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.DEV_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(ProtoConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    AnyDoneService getSchService(Retrofit retrofit) {
        return retrofit.create(AnyDoneService.class);
    }

/*
    @Provides
    @Singleton
    HostSelectionInterceptor getChangeURLInterceptor() {
        return new HostSelectionInterceptor();
    }*/

}
