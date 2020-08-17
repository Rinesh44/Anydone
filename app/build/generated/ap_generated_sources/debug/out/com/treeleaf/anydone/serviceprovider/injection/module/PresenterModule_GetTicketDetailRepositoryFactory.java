// Generated by Dagger (https://google.github.io/dagger).
package com.treeleaf.anydone.serviceprovider.injection.module;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsRepository;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class PresenterModule_GetTicketDetailRepositoryFactory
    implements Factory<TicketDetailsRepository> {
  private final PresenterModule module;

  private final Provider<AnyDoneService> anyDoneServiceProvider;

  public PresenterModule_GetTicketDetailRepositoryFactory(
      PresenterModule module, Provider<AnyDoneService> anyDoneServiceProvider) {
    this.module = module;
    this.anyDoneServiceProvider = anyDoneServiceProvider;
  }

  @Override
  public TicketDetailsRepository get() {
    return provideInstance(module, anyDoneServiceProvider);
  }

  public static TicketDetailsRepository provideInstance(
      PresenterModule module, Provider<AnyDoneService> anyDoneServiceProvider) {
    return proxyGetTicketDetailRepository(module, anyDoneServiceProvider.get());
  }

  public static PresenterModule_GetTicketDetailRepositoryFactory create(
      PresenterModule module, Provider<AnyDoneService> anyDoneServiceProvider) {
    return new PresenterModule_GetTicketDetailRepositoryFactory(module, anyDoneServiceProvider);
  }

  public static TicketDetailsRepository proxyGetTicketDetailRepository(
      PresenterModule instance, AnyDoneService anyDoneService) {
    return Preconditions.checkNotNull(
        instance.getTicketDetailRepository(anyDoneService),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}