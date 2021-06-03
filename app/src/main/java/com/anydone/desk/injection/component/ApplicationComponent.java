package com.anydone.desk.injection.component;

import com.anydone.desk.account.AccountFragment;
import com.anydone.desk.dashboard.DashboardFragment;
import com.anydone.desk.inbox.InboxFragment;
import com.anydone.desk.inboxdetails.inboxConversation.InboxConversationFragment;
import com.anydone.desk.inboxdetails.inboxtimeline.InboxTimelineFragment;
import com.anydone.desk.injection.module.ActivityModule;
import com.anydone.desk.injection.module.ApplicationModule;
import com.anydone.desk.injection.module.NetworkModule;
import com.anydone.desk.injection.module.PresenterModule;
import com.anydone.desk.moretickets.MoreTicketFragment;
import com.anydone.desk.threaddetails.threadconversation.ThreadConversationFragment;
import com.anydone.desk.threaddetails.threadfrontholder.ThreadFrontHolderFragment;
import com.anydone.desk.threaddetails.threadfrontholder.threadactivitylog.ThreadActivityLogFragment;
import com.anydone.desk.threaddetails.threadfrontholder.threadcalllog.ThreadCallLogFragment;
import com.anydone.desk.threaddetails.threadfrontholder.threadcomments.ThreadCommentsFragment;
import com.anydone.desk.threaddetails.threadtimeline.ThreadTimelineFragment;
import com.anydone.desk.threads.ThreadFragment;
import com.anydone.desk.threads.threadanalytics.AnalyticsFragment;
import com.anydone.desk.threads.threadcalls.CallsFragment;
import com.anydone.desk.threads.threadtabholder.ThreadHolderFragment;
import com.anydone.desk.threads.threadusers.UsersFragment;
import com.anydone.desk.ticketdetails.ticketactivitylog.TicketActivityLogFragment;
import com.anydone.desk.ticketdetails.ticketattachment.TicketAttachmentFragment;
import com.anydone.desk.ticketdetails.ticketconversation.TicketConversationFragment;
import com.anydone.desk.ticketdetails.ticketfrontholder.TicketFrontHolderFragment;
import com.anydone.desk.ticketdetails.tickettimeline.TicketTimelineFragment;
import com.anydone.desk.tickets.TicketsFragment;
import com.anydone.desk.tickets.closedresolvedtickets.ClosedTicketsFragment;
import com.anydone.desk.tickets.contributedtickets.ContributedTicketFragment;
import com.anydone.desk.tickets.inprogresstickets.InProgressTicketsFragment;
import com.anydone.desk.tickets.pendingtickets.PendingTicketsFragment;

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

    void inject(TicketFrontHolderFragment ticketFrontHolderFragment);

    void inject(TicketActivityLogFragment ticketActivityLogFragment);

    void inject(TicketAttachmentFragment ticketAttachmentFragment);

    void inject(ThreadFragment threadFragment);

    void inject(ThreadConversationFragment threadConversationFragment);

    void inject(ThreadTimelineFragment threadTimelineFragment);

    void inject(DashboardFragment dashboardFragment);

    void inject(ContributedTicketFragment contributedTicketFragment);

    void inject(MoreTicketFragment moreTicketFragment);

    void inject(InboxFragment inboxFragment);

    void inject(InboxConversationFragment inboxConversationFragment);

    void inject(InboxTimelineFragment inboxTimelineFragment);

    void inject(ThreadHolderFragment threadHolderFragment);

    void inject(UsersFragment usersFragment);

    void inject(CallsFragment callsFragment);

    void inject(AnalyticsFragment analyticsFragment);

    void inject(ThreadFrontHolderFragment threadFrontHolderFragment);

    void inject(ThreadActivityLogFragment threadActivityLogFragment);

    void inject(ThreadCallLogFragment threadCallLogFragment);

    void inject(ThreadCommentsFragment threadCommentsFragment);

}
