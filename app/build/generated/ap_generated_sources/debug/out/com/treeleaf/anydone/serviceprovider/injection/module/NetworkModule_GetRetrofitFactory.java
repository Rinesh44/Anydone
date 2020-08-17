// Generated by Dagger (https://google.github.io/dagger).
package com.treeleaf.anydone.serviceprovider.injection.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public final class NetworkModule_GetRetrofitFactory implements Factory<Retrofit> {
  private final NetworkModule module;

  private final Provider<OkHttpClient> okHttpClientProvider;

  public NetworkModule_GetRetrofitFactory(
      NetworkModule module, Provider<OkHttpClient> okHttpClientProvider) {
    this.module = module;
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public Retrofit get() {
    return provideInstance(module, okHttpClientProvider);
  }

  public static Retrofit provideInstance(
      NetworkModule module, Provider<OkHttpClient> okHttpClientProvider) {
    return proxyGetRetrofit(module, okHttpClientProvider.get());
  }

  public static NetworkModule_GetRetrofitFactory create(
      NetworkModule module, Provider<OkHttpClient> okHttpClientProvider) {
    return new NetworkModule_GetRetrofitFactory(module, okHttpClientProvider);
  }

  public static Retrofit proxyGetRetrofit(NetworkModule instance, OkHttpClient okHttpClient) {
    return Preconditions.checkNotNull(
        instance.getRetrofit(okHttpClient),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}