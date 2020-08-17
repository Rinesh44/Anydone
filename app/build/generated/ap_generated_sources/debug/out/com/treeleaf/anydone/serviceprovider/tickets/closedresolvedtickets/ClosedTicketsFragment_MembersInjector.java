// Generated by Dagger (https://google.github.io/dagger).
package com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets;

import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment_MembersInjector;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class ClosedTicketsFragment_MembersInjector
    implements MembersInjector<ClosedTicketsFragment> {
  private final Provider<ClosedTicketPresenterImpl> presenterProvider;

  public ClosedTicketsFragment_MembersInjector(
      Provider<ClosedTicketPresenterImpl> presenterProvider) {
    this.presenterProvider = presenterProvider;
  }

  public static MembersInjector<ClosedTicketsFragment> create(
      Provider<ClosedTicketPresenterImpl> presenterProvider) {
    return new ClosedTicketsFragment_MembersInjector(presenterProvider);
  }

  @Override
  public void injectMembers(ClosedTicketsFragment instance) {
    BaseFragment_MembersInjector.injectPresenter(instance, presenterProvider.get());
  }
}