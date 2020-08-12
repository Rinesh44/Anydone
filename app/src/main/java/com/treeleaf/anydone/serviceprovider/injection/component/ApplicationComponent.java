package com.treeleaf.anydone.serviceprovider.injection.component;

import com.treeleaf.anydone.serviceprovider.account.AccountFragment;
import com.treeleaf.anydone.serviceprovider.injection.module.ActivityModule;
import com.treeleaf.anydone.serviceprovider.injection.module.ApplicationModule;
import com.treeleaf.anydone.serviceprovider.injection.module.NetworkModule;
import com.treeleaf.anydone.serviceprovider.injection.module.PresenterModule;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.ServiceRequestDetailFragment;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.activityFragment.ActivityFragment;
import com.treeleaf.anydone.serviceprovider.servicerequests.ServiceRequestFragment;
import com.treeleaf.anydone.serviceprovider.servicerequests.accepted.AcceptedRequestFragment;
import com.treeleaf.anydone.serviceprovider.tickets.TicketsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Initialize modules{@link NetworkModule}
 */
@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class, PresenterModule.class})
public interface ApplicationComponent {

    ActivityComponent plus(ActivityModule activityModule);

    void inject(AccountFragment accountFragment);

    void inject(TicketsFragment ticketsFragment);

    void inject(ServiceRequestDetailFragment homeFragment);

    void inject(ActivityFragment activityFragment);

    void inject(AcceptedRequestFragment acceptedRequestFragment);

    void inject(ServiceRequestFragment serviceRequestFragment);

}
