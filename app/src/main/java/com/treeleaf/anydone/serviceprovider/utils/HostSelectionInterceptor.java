package com.treeleaf.anydone.serviceprovider.utils;

import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;

public class HostSelectionInterceptor implements Interceptor {
    private volatile String host;

    public void setHost(String host) {
        host = Hawk.get(Constants.BASE_URL);
        this.host = Objects.requireNonNull(HttpUrl.parse(host)).host();
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String host = this.host;
        if (host != null) {
            HttpUrl newUrl = request.url().newBuilder()
                    .host(host)
                    .build();
            request = request.newBuilder()
                    .url(newUrl)
                    .build();
        }
        return chain.proceed(request);
    }
}
