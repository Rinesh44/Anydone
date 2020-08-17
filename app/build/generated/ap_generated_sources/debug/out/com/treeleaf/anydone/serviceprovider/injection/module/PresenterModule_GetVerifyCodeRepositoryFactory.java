// Generated by Dagger (https://google.github.io/dagger).
package com.treeleaf.anydone.serviceprovider.injection.module;

import com.treeleaf.anydone.serviceprovider.forgotpassword.verifyCode.VerifyCodeRepository;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class PresenterModule_GetVerifyCodeRepositoryFactory
    implements Factory<VerifyCodeRepository> {
  private final PresenterModule module;

  private final Provider<AnyDoneService> anyDoneServiceProvider;

  public PresenterModule_GetVerifyCodeRepositoryFactory(
      PresenterModule module, Provider<AnyDoneService> anyDoneServiceProvider) {
    this.module = module;
    this.anyDoneServiceProvider = anyDoneServiceProvider;
  }

  @Override
  public VerifyCodeRepository get() {
    return provideInstance(module, anyDoneServiceProvider);
  }

  public static VerifyCodeRepository provideInstance(
      PresenterModule module, Provider<AnyDoneService> anyDoneServiceProvider) {
    return proxyGetVerifyCodeRepository(module, anyDoneServiceProvider.get());
  }

  public static PresenterModule_GetVerifyCodeRepositoryFactory create(
      PresenterModule module, Provider<AnyDoneService> anyDoneServiceProvider) {
    return new PresenterModule_GetVerifyCodeRepositoryFactory(module, anyDoneServiceProvider);
  }

  public static VerifyCodeRepository proxyGetVerifyCodeRepository(
      PresenterModule instance, AnyDoneService anyDoneService) {
    return Preconditions.checkNotNull(
        instance.getVerifyCodeRepository(anyDoneService),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}