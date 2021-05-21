package com.anydone.desk.injection.module;

import com.anydone.desk.account.AccountRepository;
import com.anydone.desk.account.AccountRepositoryImpl;
import com.anydone.desk.addcontributor.AddContributorRepository;
import com.anydone.desk.addcontributor.AddContributorRepositoryImpl;
import com.anydone.desk.addparticipant.AddParticipantRepository;
import com.anydone.desk.addparticipant.AddParticipantRepositoryImpl;
import com.anydone.desk.addpaymentcard.AddCardRepository;
import com.anydone.desk.addpaymentcard.AddCardRepositoryImpl;
import com.anydone.desk.addticket.AddTicketRepository;
import com.anydone.desk.addticket.AddTicketRepositoryImpl;
import com.anydone.desk.alltickets.AllTicketRepository;
import com.anydone.desk.alltickets.AllTicketsRepositoryImpl;
import com.anydone.desk.assignedtickets.AssignedTicketRepository;
import com.anydone.desk.assignedtickets.AssignedTicketRepositoryImpl;
import com.anydone.desk.changepassword.ChangePasswordRepository;
import com.anydone.desk.changepassword.ChangePasswordRepositoryImpl;
import com.anydone.desk.creategroup.CreateGroupRepository;
import com.anydone.desk.creategroup.CreateGroupRepositoryImpl;
import com.anydone.desk.customertickets.CustomerTicketRepository;
import com.anydone.desk.customertickets.CustomerTicketRepositoryImpl;
import com.anydone.desk.dashboard.DashboardRepository;
import com.anydone.desk.dashboard.DashboardRepositoryImpl;
import com.anydone.desk.editInbox.EditInboxRepository;
import com.anydone.desk.editInbox.EditInboxRepositoryImpl;
import com.anydone.desk.editprofile.EditProfileRepository;
import com.anydone.desk.editprofile.EditProfileRepositoryImpl;
import com.anydone.desk.editticket.EditTicketRepository;
import com.anydone.desk.editticket.EditTicketRepositoryImpl;
import com.anydone.desk.forgotpassword.ForgotPasswordRepository;
import com.anydone.desk.forgotpassword.ForgotPasswordRepositoryImpl;
import com.anydone.desk.forgotpassword.resetpassword.ResetPasswordRepository;
import com.anydone.desk.forgotpassword.resetpassword.ResetPasswordRepositoryImpl;
import com.anydone.desk.forgotpassword.verifyCode.VerifyCodeRepository;
import com.anydone.desk.forgotpassword.verifyCode.VerifyCodeRepositoryImpl;
import com.anydone.desk.forwardMessage.ForwardMessageRepository;
import com.anydone.desk.forwardMessage.ForwardMessageRepositoryImpl;
import com.anydone.desk.inbox.InboxRepository;
import com.anydone.desk.inbox.InboxRepositoryImpl;
import com.anydone.desk.inboxdetails.InboxDetailRepositoryImpl;
import com.anydone.desk.inboxdetails.InboxDetailsRepository;
import com.anydone.desk.inboxdetails.inboxConversation.InboxConversationRepository;
import com.anydone.desk.inboxdetails.inboxConversation.InboxConversationRepositoryImpl;
import com.anydone.desk.inboxdetails.inboxtimeline.InboxTimelineRepository;
import com.anydone.desk.inboxdetails.inboxtimeline.InboxTimelineRepositoryImpl;
import com.anydone.desk.linkshare.LinkShareRepository;
import com.anydone.desk.linkshare.LinkShareRepositoryImpl;
import com.anydone.desk.login.LoginRepository;
import com.anydone.desk.login.LoginRepositoryImpl;
import com.anydone.desk.moretickets.MoreTicketRepository;
import com.anydone.desk.moretickets.MoreTicketRepositoryImpl;
import com.anydone.desk.opentickets.OpenTicketRepository;
import com.anydone.desk.opentickets.OpenTicketRepositoryImpl;
import com.anydone.desk.ownedtickets.OwnedTicketRepository;
import com.anydone.desk.ownedtickets.OwnedTicketRepositoryImpl;
import com.anydone.desk.paymentmethod.PaymentMethodRepository;
import com.anydone.desk.paymentmethod.PaymentMethodRepositoryImpl;
import com.anydone.desk.picklocation.PickLocationRepository;
import com.anydone.desk.picklocation.PickLocationRepositoryImpl;
import com.anydone.desk.profile.ProfileRepository;
import com.anydone.desk.profile.ProfileRepositoryImpl;
import com.anydone.desk.reply.ReplyRepository;
import com.anydone.desk.reply.ReplyRepositoryImpl;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.searchconversation.SearchConversationRepository;
import com.anydone.desk.searchconversation.SearchConversationRepositoryImpl;
import com.anydone.desk.servicerequestdetail.servicerequestdetailactivity.ServiceRequestDetailActivityRepository;
import com.anydone.desk.servicerequestdetail.servicerequestdetailactivity.ServiceRequestDetailActivityRepositoryImpl;
import com.anydone.desk.setting.currency.CurrencyRepository;
import com.anydone.desk.setting.currency.CurrencyRepositoryImpl;
import com.anydone.desk.setting.language.LanguageRepository;
import com.anydone.desk.setting.language.LanguageRepositoryImpl;
import com.anydone.desk.setting.location.AddLocationRepository;
import com.anydone.desk.setting.location.AddLocationRepositoryImpl;
import com.anydone.desk.setting.location.showLocation.ShowLocationRepository;
import com.anydone.desk.setting.location.showLocation.ShowLocationRepositoryImpl;
import com.anydone.desk.setting.timezone.TimezoneRepository;
import com.anydone.desk.setting.timezone.TimezoneRepositoryImpl;
import com.anydone.desk.suggestedTicketPreview.SuggestedTicketPreviewRepository;
import com.anydone.desk.suggestedTicketPreview.SuggestedTicketPreviewRepositoryImpl;
import com.anydone.desk.threaddetails.ThreadDetailRepositoryImpl;
import com.anydone.desk.threaddetails.ThreadDetailsRepository;
import com.anydone.desk.threaddetails.threadconversation.ThreadConversationRepository;
import com.anydone.desk.threaddetails.threadconversation.ThreadConversationRepositoryImpl;
import com.anydone.desk.threaddetails.threadtimeline.ThreadTimelineRepository;
import com.anydone.desk.threaddetails.threadtimeline.ThreadTimelineRepositoryImpl;
import com.anydone.desk.threads.ThreadRepository;
import com.anydone.desk.threads.ThreadRepositoryImpl;
import com.anydone.desk.threads.threadtabholder.ThreadHolderRepository;
import com.anydone.desk.threads.threadtabholder.ThreadHolderRepositoryImpl;
import com.anydone.desk.threads.threadusers.UsersRepository;
import com.anydone.desk.threads.threadusers.UsersRepositoryImpl;
import com.anydone.desk.ticketdetails.TicketDetailsRepository;
import com.anydone.desk.ticketdetails.TicketDetailsRepositoryImpl;
import com.anydone.desk.ticketdetails.ticketactivitylog.TicketActivityLogRepository;
import com.anydone.desk.ticketdetails.ticketactivitylog.TicketActivityLogRepositoryImpl;
import com.anydone.desk.ticketdetails.ticketattachment.TicketAttachmentRepository;
import com.anydone.desk.ticketdetails.ticketattachment.TicketAttachmentRepositoryImpl;
import com.anydone.desk.ticketdetails.ticketconversation.TicketConversationRepository;
import com.anydone.desk.ticketdetails.ticketconversation.TicketConversationRepositoryImpl;
import com.anydone.desk.ticketdetails.ticketfrontholder.TicketFrontHolderRepository;
import com.anydone.desk.ticketdetails.ticketfrontholder.TicketFrontHolderRepositoryImpl;
import com.anydone.desk.ticketdetails.tickettimeline.TicketTimelineRepository;
import com.anydone.desk.ticketdetails.tickettimeline.TicketTimelineRepositoryImpl;
import com.anydone.desk.tickets.TicketsRepository;
import com.anydone.desk.tickets.TicketsRepositoryImpl;
import com.anydone.desk.tickets.closedresolvedtickets.ClosedTicketRepository;
import com.anydone.desk.tickets.closedresolvedtickets.ClosedTicketRepositoryImpl;
import com.anydone.desk.tickets.contributedtickets.ContributedTicketRepository;
import com.anydone.desk.tickets.contributedtickets.ContributedTicketRepositoryImpl;
import com.anydone.desk.tickets.inprogresstickets.InProgressTicketRepository;
import com.anydone.desk.tickets.inprogresstickets.InProgressTicketRepositoryImpl;
import com.anydone.desk.tickets.pendingtickets.PendingTicketRepository;
import com.anydone.desk.tickets.pendingtickets.PendingTicketRepositoryImpl;
import com.anydone.desk.tickets.unassignedtickets.UnassignedTicketRepository;
import com.anydone.desk.tickets.unassignedtickets.UnassignedTicketRepositoryImpl;
import com.anydone.desk.tickets.unsubscribedtickets.UnsubscribedTicketRepository;
import com.anydone.desk.tickets.unsubscribedtickets.UnsubscribedTicketRepositoryImpl;
import com.anydone.desk.ticketsuggestions.TicketSuggestionRepository;
import com.anydone.desk.ticketsuggestions.TicketSuggestionRepositoryImpl;
import com.anydone.desk.verification.VerificationRepository;
import com.anydone.desk.verification.VerificationRepositoryImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Provides respective presenter instance
 */
@Module(includes = NetworkModule.class)
public class PresenterModule {

    @Provides
    LoginRepository getLoginRepository(AnyDoneService anyDoneService) {
        return new LoginRepositoryImpl(anyDoneService);
    }

    @Provides
    ChangePasswordRepository getChangePasswordRepository(AnyDoneService anyDoneService) {
        return new ChangePasswordRepositoryImpl(anyDoneService);
    }

    @Provides
    EditProfileRepository getEditProfileRepository(AnyDoneService anyDoneService) {
        return new EditProfileRepositoryImpl(anyDoneService);
    }

    @Provides
    ProfileRepository getProfileRepository(AnyDoneService anyDoneService) {
        return new ProfileRepositoryImpl(anyDoneService);
    }

    @Provides
    VerificationRepository getVerificationRepository(AnyDoneService anyDoneService) {
        return new VerificationRepositoryImpl(anyDoneService);
    }

    @Provides
    ForgotPasswordRepository getForgotPasswordRepository(AnyDoneService anyDoneService) {
        return new ForgotPasswordRepositoryImpl(anyDoneService);
    }

    @Provides
    VerifyCodeRepository getVerifyCodeRepository(AnyDoneService anyDoneService) {
        return new VerifyCodeRepositoryImpl((anyDoneService));
    }

    @Provides
    ResetPasswordRepository getResetPasswordRepository(AnyDoneService anyDoneService) {
        return new ResetPasswordRepositoryImpl((anyDoneService));
    }

    @Provides
    AccountRepository getAccountRepository(AnyDoneService anyDoneService) {
        return new AccountRepositoryImpl((anyDoneService));
    }

    @Provides
    ServiceRequestDetailActivityRepository getServiceRequestDetailActivityRepository(AnyDoneService anyDoneService) {
        return new ServiceRequestDetailActivityRepositoryImpl(anyDoneService);
    }

    @Provides
    AddLocationRepository getAddLocationRepository(AnyDoneService anyDoneService) {
        return new AddLocationRepositoryImpl(anyDoneService);
    }

    @Provides
    TimezoneRepository getTimezoneRepository(AnyDoneService anyDoneService) {
        return new TimezoneRepositoryImpl(anyDoneService);
    }

    @Provides
    CurrencyRepository getCurrencyRepository(AnyDoneService anyDoneService) {
        return new CurrencyRepositoryImpl(anyDoneService);
    }

    @Provides
    ShowLocationRepository getShowLocationRepository(AnyDoneService anyDoneService) {
        return new ShowLocationRepositoryImpl(anyDoneService);
    }

    @Provides
    PickLocationRepository getPickLocationRepository(AnyDoneService anyDoneService) {
        return new PickLocationRepositoryImpl(anyDoneService);
    }

    @Provides
    LanguageRepository getLanguageRepository(AnyDoneService anyDoneService) {
        return new LanguageRepositoryImpl(anyDoneService);
    }

    @Provides
    TicketsRepository getTicketsRepository(AnyDoneService anyDoneService) {
        return new TicketsRepositoryImpl(anyDoneService);
    }

    @Provides
    PendingTicketRepository getAssignedTicketRepository(AnyDoneService anyDoneService) {
        return new PendingTicketRepositoryImpl(anyDoneService);
    }

    @Provides
    InProgressTicketRepository getSubscribedTicketRepository(AnyDoneService anyDoneService) {
        return new InProgressTicketRepositoryImpl(anyDoneService);
    }

    @Provides
    ClosedTicketRepository getClosedTicketRepository(AnyDoneService anyDoneService) {
        return new ClosedTicketRepositoryImpl(anyDoneService);
    }

    @Provides
    UnassignedTicketRepository getAssignableTicketRepository(AnyDoneService anyDoneService) {
        return new UnassignedTicketRepositoryImpl(anyDoneService);
    }

    @Provides
    UnsubscribedTicketRepository getSubscribeableRepository(AnyDoneService anyDoneService) {
        return new UnsubscribedTicketRepositoryImpl(anyDoneService);
    }

    @Provides
    TicketDetailsRepository getTicketDetailRepository(AnyDoneService anyDoneService) {
        return new TicketDetailsRepositoryImpl(anyDoneService);
    }

    @Provides
    AddTicketRepository getAddTicketRepository(AnyDoneService anyDoneService) {
        return new AddTicketRepositoryImpl(anyDoneService);
    }


    @Provides
    TicketConversationRepository getTicketConversationRepository(AnyDoneService anyDoneService) {
        return new TicketConversationRepositoryImpl(anyDoneService);
    }

    @Provides
    TicketTimelineRepository getTicketTimelineRepository(AnyDoneService anyDoneService) {
        return new TicketTimelineRepositoryImpl(anyDoneService);
    }

    @Provides
    TicketAttachmentRepository getTicketAttachmentRepository(AnyDoneService anyDoneService) {
        return new TicketAttachmentRepositoryImpl(anyDoneService);
    }

    @Provides
    TicketActivityLogRepository getTicketActivityLogRepository(AnyDoneService anyDoneService) {
        return new TicketActivityLogRepositoryImpl(anyDoneService);
    }

    @Provides
    TicketFrontHolderRepository getTicketFrontHolderRepository(AnyDoneService anyDoneService) {
        return new TicketFrontHolderRepositoryImpl(anyDoneService);
    }

    @Provides
    ThreadRepository getThreadRepository(AnyDoneService anyDoneService) {
        return new ThreadRepositoryImpl(anyDoneService);
    }

    @Provides
    ThreadDetailsRepository getThreadDetailsRepository(AnyDoneService anyDoneService) {
        return new ThreadDetailRepositoryImpl(anyDoneService);
    }


    @Provides
    ThreadConversationRepository getThreadConversationRepository(AnyDoneService anyDoneService) {
        return new ThreadConversationRepositoryImpl(anyDoneService);
    }

    @Provides
    ThreadTimelineRepository getThreadTimelineRepository(AnyDoneService anyDoneService) {
        return new ThreadTimelineRepositoryImpl(anyDoneService);
    }

    @Provides
    UsersRepository getUsersRepository(AnyDoneService anyDoneService) {
        return new UsersRepositoryImpl(anyDoneService);
    }

    @Provides
    AddContributorRepository getContributorRepository(AnyDoneService anyDoneService) {
        return new AddContributorRepositoryImpl(anyDoneService);
    }

    @Provides
    ThreadHolderRepository getThreadHolderRepository(AnyDoneService anyDoneService) {
        return new ThreadHolderRepositoryImpl(anyDoneService);
    }

    @Provides
    DashboardRepository getDashBoardRepository(AnyDoneService anyDoneService) {
        return new DashboardRepositoryImpl(anyDoneService);
    }

    @Provides
    LinkShareRepository getLinkShareRepository(AnyDoneService anyDoneService) {
        return new LinkShareRepositoryImpl(anyDoneService);
    }

    @Provides
    AddCardRepository getCardRepository(AnyDoneService anyDoneService) {
        return new AddCardRepositoryImpl(anyDoneService);
    }

    @Provides
    PaymentMethodRepository getPaymentRepository(AnyDoneService anyDoneService) {
        return new PaymentMethodRepositoryImpl(anyDoneService);
    }

    @Provides
    ContributedTicketRepository getContributedTicketRepository(AnyDoneService anyDoneService) {
        return new ContributedTicketRepositoryImpl(anyDoneService);
    }

    @Provides
    EditTicketRepository getEditTicketRepository(AnyDoneService anyDoneService) {
        return new EditTicketRepositoryImpl(anyDoneService);
    }

    @Provides
    TicketSuggestionRepository getTicketSuggestionRepository(AnyDoneService anyDoneService) {
        return new TicketSuggestionRepositoryImpl(anyDoneService);
    }

    @Provides
    SuggestedTicketPreviewRepository getSuggestedTicketPreviewRepository(AnyDoneService anyDoneService) {
        return new SuggestedTicketPreviewRepositoryImpl(anyDoneService);
    }

    @Provides
    AllTicketRepository getAllTicketRepository(AnyDoneService anyDoneService) {
        return new AllTicketsRepositoryImpl(anyDoneService);
    }

    @Provides
    OpenTicketRepository getOpenTicketRepository(AnyDoneService anyDoneService) {
        return new OpenTicketRepositoryImpl(anyDoneService);
    }

    @Provides
    OwnedTicketRepository getOwnedTicketRepository(AnyDoneService anyDoneService) {
        return new OwnedTicketRepositoryImpl(anyDoneService);
    }

    @Provides
    CustomerTicketRepository getCustomerTicketRepository(AnyDoneService anyDoneService) {
        return new CustomerTicketRepositoryImpl(anyDoneService);
    }

    @Provides
    MoreTicketRepository getMoreTicketRepository(AnyDoneService anyDoneService) {
        return new MoreTicketRepositoryImpl(anyDoneService);
    }

    @Provides
    InboxRepository getInboxRepository(AnyDoneService anyDoneService) {
        return new InboxRepositoryImpl(anyDoneService);
    }

    @Provides
    InboxDetailsRepository getInboxDetailRepository(AnyDoneService anyDoneService) {
        return new InboxDetailRepositoryImpl(anyDoneService);
    }

    @Provides
    InboxConversationRepository getInboxConversationRepository(AnyDoneService anyDoneService) {
        return new InboxConversationRepositoryImpl(anyDoneService);
    }

    @Provides
    InboxTimelineRepository getInboxTimelineRepository(AnyDoneService anyDoneService) {
        return new InboxTimelineRepositoryImpl(anyDoneService);
    }

    @Provides
    AddParticipantRepository getParticipantRepository(AnyDoneService anyDoneService) {
        return new AddParticipantRepositoryImpl(anyDoneService);
    }

    @Provides
    CreateGroupRepository getCreateGroupRepository(AnyDoneService anyDoneService) {
        return new CreateGroupRepositoryImpl(anyDoneService);
    }

    @Provides
    EditInboxRepository getEditInboxRepository(AnyDoneService anyDoneService) {
        return new EditInboxRepositoryImpl(anyDoneService);
    }

    @Provides
    ForwardMessageRepository getForwardMessageRepository(AnyDoneService anyDoneService) {
        return new ForwardMessageRepositoryImpl(anyDoneService);
    }

    @Provides
    ReplyRepository getReplyRepository(AnyDoneService anyDoneService) {
        return new ReplyRepositoryImpl(anyDoneService);
    }

    @Provides
    SearchConversationRepository getSearchConversationRepository(AnyDoneService anyDoneService) {
        return new SearchConversationRepositoryImpl(anyDoneService);
    }

    @Provides
    AssignedTicketRepository getAssignTicketRepository(AnyDoneService anyDoneService) {
        return new AssignedTicketRepositoryImpl(anyDoneService);
    }
}

