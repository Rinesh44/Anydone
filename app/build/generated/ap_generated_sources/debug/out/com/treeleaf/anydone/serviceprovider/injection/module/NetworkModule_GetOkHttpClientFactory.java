// Generated by Dagger (https://google.github.io/dagger).
package com.treeleaf.anydone.serviceprovider.injection.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import okhttp3.OkHttpClient;

public final class NetworkModule_GetOkHttpClientFactory implements Factory<OkHttpClient> {
  private final NetworkModule module;

  public NetworkModule_GetOkHttpClientFactory(NetworkModule module) {
    this.module = module;
  }

  @Override
  public OkHttpClient get() {
    return provideInstance(module);
  }

  public static OkHttpClient provideInstance(NetworkModule module) {
    return proxyGetOkHttpClient(module);
  }

  public static NetworkModule_GetOkHttpClientFactory create(NetworkModule module) {
    return new NetworkModule_GetOkHttpClientFactory(module);
  }

  public static OkHttpClient proxyGetOkHttpClient(NetworkModule instance) {
    return Preconditions.checkNotNull(
        instance.getOkHttpClient(), "Cannot return null from a non-@Nullable @Provides method");
  }
}