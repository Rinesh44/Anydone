package com.treeleaf.anydone.serviceprovider.injection.component;

import com.treeleaf.anydone.serviceprovider.account.AccountFragment;
import com.treeleaf.anydone.serviceprovider.dashboard.DashboardFragment;
import com.treeleaf.anydone.serviceprovider.injection.module.ActivityModule;
import com.treeleaf.anydone.serviceprovider.injection.module.ApplicationModule;
import com.treeleaf.anydone.serviceprovider.injection.module.NetworkModule;
import com.treeleaf.anydone.serviceprovider.injection.module.PresenterModule;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.ServiceRequestDetailFragment;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.activityFragment.ActivityFragment;
import com.treeleaf.anydone.serviceprovider.servicerequests.ServiceRequestFragment;
import com.treeleaf.anydone.serviceprovider.servicerequests.accepted.AcceptedRequestFragment;
import com.treeleaf.anydone.serviceprovider.threaddetails.threadconversation.ThreadConversationFragment;
import com.treeleaf.anydone.serviceprovider.threaddetails.threadtimeline.ThreadTimelineFragment;
import com.treeleaf.anydone.serviceprovider.threads.ThreadFragment;
import com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation.TicketConversationFragment;
import com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline.TicketTimelineFragment;
import com.treeleaf.anydone.serviceprovider.tickets.TicketsFragment;
import com.treeleaf.anydone.serviceprovider.tickets.assignedtickets.AssignedTicketsFragment;
import com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets.ClosedTicketsFragment;
import com.treeleaf.anydone.serviceprovider.tickets.subscribetickets.SubscribeTicketsFragment;

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

    void inject(AssignedTicketsFragment assignedTicketsFragment);

    void inject(SubscribeTicketsFragment subscribeTicketsFragment);

    void inject(ClosedTicketsFragment closedTicketsFragment);

    void inject(TicketConversationFragment ticketConversationFragment);

    void inject(TicketTimelineFragment ticketTimelineFragment);

    void inject(ThreadFragment threadFragment);

    void inject(ThreadConversationFragment threadConversationFragment);

    void inject(ThreadTimelineFragment threadTimelineFragment);

    void inject(DashboardFragment dashboardFragment);

}
