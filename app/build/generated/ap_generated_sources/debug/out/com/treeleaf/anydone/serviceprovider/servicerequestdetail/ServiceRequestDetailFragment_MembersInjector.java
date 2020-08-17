// Generated by Dagger (https://google.github.io/dagger).
package com.treeleaf.anydone.serviceprovider.servicerequestdetail;

import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment_MembersInjector;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class ServiceRequestDetailFragment_MembersInjector
    implements MembersInjector<ServiceRequestDetailFragment> {
  private final Provider<ServiceRequestDetailPresenterImpl> presenterProvider;

  public ServiceRequestDetailFragment_MembersInjector(
      Provider<ServiceRequestDetailPresenterImpl> presenterProvider) {
    this.presenterProvider = presenterProvider;
  }

  public static MembersInjector<ServiceRequestDetailFragment> create(
      Provider<ServiceRequestDetailPresenterImpl> presenterProvider) {
    return new ServiceRequestDetailFragment_MembersInjector(presenterProvider);
  }

  @Override
  public void injectMembers(ServiceRequestDetailFragment instance) {
    BaseFragment_MembersInjector.injectPresenter(instance, presenterProvider.get());
  }
}