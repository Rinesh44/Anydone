package com.treeleaf.anydone.serviceprovider.injection.component;

import com.treeleaf.anydone.serviceprovider.account.AccountFragment;
import com.treeleaf.anydone.serviceprovider.dashboard.DashboardFragment;
import com.treeleaf.anydone.serviceprovider.inbox.InboxFragment;
import com.treeleaf.anydone.serviceprovider.inboxdetails.inboxConversation.InboxConversationFragment;
import com.treeleaf.anydone.serviceprovider.inboxdetails.inboxtimeline.InboxTimelineFragment;
import com.treeleaf.anydone.serviceprovider.injection.module.ActivityModule;
import com.treeleaf.anydone.serviceprovider.injection.module.ApplicationModule;
import com.treeleaf.anydone.serviceprovider.injection.module.NetworkModule;
import com.treeleaf.anydone.serviceprovider.injection.module.PresenterModule;
import com.treeleaf.anydone.serviceprovider.moretickets.MoreTicketFragment;
import com.treeleaf.anydone.serviceprovider.threaddetails.threadconversation.ThreadConversationFragment;
import com.treeleaf.anydone.serviceprovider.threaddetails.threadtimeline.ThreadTimelineFragment;
import com.treeleaf.anydone.serviceprovider.threads.ThreadFragment;
import com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation.TicketConversationFragment;
import com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline.TicketTimelineFragment;
import com.treeleaf.anydone.serviceprovider.tickets.TicketsFragment;
import com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets.ClosedTicketsFragment;
import com.treeleaf.anydone.serviceprovider.tickets.contributedtickets.ContributedTicketFragment;
import com.treeleaf.anydone.serviceprovider.tickets.inprogresstickets.InProgressTicketsFragment;
import com.treeleaf.anydone.serviceprovider.tickets.pendingtickets.PendingTicketsFragment;

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

    void inject(PendingTicketsFragment pendingTicketsFragment);

    void inject(InProgressTicketsFragment inProgressTicketsFragment);

    void inject(ClosedTicketsFragment closedTicketsFragment);

    void inject(TicketConversationFragment ticketConversationFragment);

    void inject(TicketTimelineFragment ticketTimelineFragment);

    void inject(ThreadFragment threadFragment);

    void inject(ThreadConversationFragment threadConversationFragment);

    void inject(ThreadTimelineFragment threadTimelineFragment);

    void inject(DashboardFragment dashboardFragment);

    void inject(ContributedTicketFragment contributedTicketFragment);

    void inject(MoreTicketFragment moreTicketFragment);

    void inject(InboxFragment inboxFragment);

    void inject(InboxConversationFragment inboxConversationFragment);

    void inject(InboxTimelineFragment inboxTimelineFragment);

}
