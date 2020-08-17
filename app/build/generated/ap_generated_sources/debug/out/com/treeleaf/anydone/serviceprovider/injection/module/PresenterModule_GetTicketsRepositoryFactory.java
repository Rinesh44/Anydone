// Generated by Dagger (https://google.github.io/dagger).
package com.treeleaf.anydone.serviceprovider.injection.module;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.tickets.TicketsRepository;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class PresenterModule_GetTicketsRepositoryFactory
    implements Factory<TicketsRepository> {
  private final PresenterModule module;

  private final Provider<AnyDoneService> anyDoneServiceProvider;

  public PresenterModule_GetTicketsRepositoryFactory(
      PresenterModule module, Provider<AnyDoneService> anyDoneServiceProvider) {
    this.module = module;
    this.anyDoneServiceProvider = anyDoneServiceProvider;
  }

  @Override
  public TicketsRepository get() {
    return provideInstance(module, anyDoneServiceProvider);
  }

  public static TicketsRepository provideInstance(
      PresenterModule module, Provider<AnyDoneService> anyDoneServiceProvider) {
    return proxyGetTicketsRepository(module, anyDoneServiceProvider.get());
  }

  public static PresenterModule_GetTicketsRepositoryFactory create(
      PresenterModule module, Provider<AnyDoneService> anyDoneServiceProvider) {
    return new PresenterModule_GetTicketsRepositoryFactory(module, anyDoneServiceProvider);
  }

  public static TicketsRepository proxyGetTicketsRepository(
      PresenterModule instance, AnyDoneService anyDoneService) {
    return Preconditions.checkNotNull(
        instance.getTicketsRepository(anyDoneService),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}