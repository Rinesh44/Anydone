// Generated by Dagger (https://google.github.io/dagger).
package com.treeleaf.anydone.serviceprovider.injection.module;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.verification.VerificationRepository;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class PresenterModule_GetVerificationRepositoryFactory
    implements Factory<VerificationRepository> {
  private final PresenterModule module;

  private final Provider<AnyDoneService> anyDoneServiceProvider;

  public PresenterModule_GetVerificationRepositoryFactory(
      PresenterModule module, Provider<AnyDoneService> anyDoneServiceProvider) {
    this.module = module;
    this.anyDoneServiceProvider = anyDoneServiceProvider;
  }

  @Override
  public VerificationRepository get() {
    return provideInstance(module, anyDoneServiceProvider);
  }

  public static VerificationRepository provideInstance(
      PresenterModule module, Provider<AnyDoneService> anyDoneServiceProvider) {
    return proxyGetVerificationRepository(module, anyDoneServiceProvider.get());
  }

  public static PresenterModule_GetVerificationRepositoryFactory create(
      PresenterModule module, Provider<AnyDoneService> anyDoneServiceProvider) {
    return new PresenterModule_GetVerificationRepositoryFactory(module, anyDoneServiceProvider);
  }

  public static VerificationRepository proxyGetVerificationRepository(
      PresenterModule instance, AnyDoneService anyDoneService) {
    return Preconditions.checkNotNull(
        instance.getVerificationRepository(anyDoneService),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}