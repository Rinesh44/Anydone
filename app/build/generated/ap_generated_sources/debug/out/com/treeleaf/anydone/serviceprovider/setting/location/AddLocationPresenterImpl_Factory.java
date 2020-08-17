// Generated by Dagger (https://google.github.io/dagger).
package com.treeleaf.anydone.serviceprovider.setting.location;

import dagger.internal.Factory;
import javax.inject.Provider;

public final class AddLocationPresenterImpl_Factory implements Factory<AddLocationPresenterImpl> {
  private final Provider<AddLocationRepository> addLocationRepositoryProvider;

  public AddLocationPresenterImpl_Factory(
      Provider<AddLocationRepository> addLocationRepositoryProvider) {
    this.addLocationRepositoryProvider = addLocationRepositoryProvider;
  }

  @Override
  public AddLocationPresenterImpl get() {
    return provideInstance(addLocationRepositoryProvider);
  }

  public static AddLocationPresenterImpl provideInstance(
      Provider<AddLocationRepository> addLocationRepositoryProvider) {
    return new AddLocationPresenterImpl(addLocationRepositoryProvider.get());
  }

  public static AddLocationPresenterImpl_Factory create(
      Provider<AddLocationRepository> addLocationRepositoryProvider) {
    return new AddLocationPresenterImpl_Factory(addLocationRepositoryProvider);
  }

  public static AddLocationPresenterImpl newAddLocationPresenterImpl(
      AddLocationRepository addLocationRepository) {
    return new AddLocationPresenterImpl(addLocationRepository);
  }
}