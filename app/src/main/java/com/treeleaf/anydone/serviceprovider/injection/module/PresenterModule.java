package com.treeleaf.anydone.serviceprovider.injection.module;

import com.treeleaf.anydone.serviceprovider.account.AccountRepository;
import com.treeleaf.anydone.serviceprovider.account.AccountRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.addcontributor.AddContributorRepository;
import com.treeleaf.anydone.serviceprovider.addcontributor.AddContributorRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.addpaymentcard.AddCardRepository;
import com.treeleaf.anydone.serviceprovider.addpaymentcard.AddCardRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.addticket.AddTicketRepository;
import com.treeleaf.anydone.serviceprovider.addticket.AddTicketRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.changepassword.ChangePasswordRepository;
import com.treeleaf.anydone.serviceprovider.changepassword.ChangePasswordRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.dashboard.DashboardRepository;
import com.treeleaf.anydone.serviceprovider.dashboard.DashboardRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.editprofile.EditProfileRepository;
import com.treeleaf.anydone.serviceprovider.editprofile.EditProfileRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.forgotpassword.ForgotPasswordRepository;
import com.treeleaf.anydone.serviceprovider.forgotpassword.ForgotPasswordRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.forgotpassword.resetpassword.ResetPasswordRepository;
import com.treeleaf.anydone.serviceprovider.forgotpassword.resetpassword.ResetPasswordRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.forgotpassword.verifyCode.VerifyCodeRepository;
import com.treeleaf.anydone.serviceprovider.forgotpassword.verifyCode.VerifyCodeRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.linkshare.LinkShareRepository;
import com.treeleaf.anydone.serviceprovider.linkshare.LinkShareRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.login.LoginRepository;
import com.treeleaf.anydone.serviceprovider.login.LoginRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.paymentmethod.PaymentMethodRepository;
import com.treeleaf.anydone.serviceprovider.paymentmethod.PaymentMethodRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.picklocation.PickLocationRepository;
import com.treeleaf.anydone.serviceprovider.picklocation.PickLocationRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.profile.ProfileRepository;
import com.treeleaf.anydone.serviceprovider.profile.ProfileRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.ServiceRequestDetailRepository;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.ServiceRequestDetailRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.activityFragment.ActivityRepository;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.activityFragment.ActivityRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity.ServiceRequestDetailActivityRepository;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity.ServiceRequestDetailActivityRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.servicerequests.ServiceRequestRepository;
import com.treeleaf.anydone.serviceprovider.servicerequests.ServiceRequestRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.servicerequests.accepted.AcceptedRepository;
import com.treeleaf.anydone.serviceprovider.servicerequests.accepted.AcceptedRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.setting.currency.CurrencyRepository;
import com.treeleaf.anydone.serviceprovider.setting.currency.CurrencyRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.setting.language.LanguageRepository;
import com.treeleaf.anydone.serviceprovider.setting.language.LanguageRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.setting.location.AddLocationRepository;
import com.treeleaf.anydone.serviceprovider.setting.location.AddLocationRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.setting.location.showLocation.ShowLocationRepository;
import com.treeleaf.anydone.serviceprovider.setting.location.showLocation.ShowLocationRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.setting.timezone.TimezoneRepository;
import com.treeleaf.anydone.serviceprovider.setting.timezone.TimezoneRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.threaddetails.ThreadDetailRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.threaddetails.ThreadDetailsRepository;
import com.treeleaf.anydone.serviceprovider.threaddetails.threadconversation.ThreadConversationRepository;
import com.treeleaf.anydone.serviceprovider.threaddetails.threadconversation.ThreadConversationRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.threaddetails.threadtimeline.ThreadTimelineRepository;
import com.treeleaf.anydone.serviceprovider.threaddetails.threadtimeline.ThreadTimelineRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.threads.ThreadRepository;
import com.treeleaf.anydone.serviceprovider.threads.ThreadRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsRepository;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation.TicketConversationRepository;
import com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation.TicketConversationRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline.TicketTimelineRepository;
import com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline.TicketTimelineRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.tickets.TicketsRepository;
import com.treeleaf.anydone.serviceprovider.tickets.TicketsRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.tickets.assignedtickets.AssignedTicketRepository;
import com.treeleaf.anydone.serviceprovider.tickets.assignedtickets.AssignedTicketRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets.ClosedTicketRepository;
import com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets.ClosedTicketRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.tickets.subscribetickets.SubscribeTicketRepository;
import com.treeleaf.anydone.serviceprovider.tickets.subscribetickets.SubscribeTicketRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.tickets.unassignedtickets.UnassignedTicketRepository;
import com.treeleaf.anydone.serviceprovider.tickets.unassignedtickets.UnassignedTicketRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.tickets.unsubscribedtickets.UnsubscribedTicketRepository;
import com.treeleaf.anydone.serviceprovider.tickets.unsubscribedtickets.UnsubscribedTicketRepositoryImpl;
import com.treeleaf.anydone.serviceprovider.verification.VerificationRepository;
import com.treeleaf.anydone.serviceprovider.verification.VerificationRepositoryImpl;

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
    ServiceRequestRepository getServiceRequestRepository(AnyDoneService anyDoneService) {
        return new ServiceRequestRepositoryImpl(anyDoneService);
    }

    @Provides
    ServiceRequestDetailRepository getServiceRequestDetailRepository(AnyDoneService anyDoneService) {
        return new ServiceRequestDetailRepositoryImpl(anyDoneService);
    }

    @Provides
    ServiceRequestDetailActivityRepository getServiceRequestDetailActivityRepository(AnyDoneService anyDoneService) {
        return new ServiceRequestDetailActivityRepositoryImpl(anyDoneService);
    }

    @Provides
    ActivityRepository getActivityRepository(AnyDoneService anyDoneService) {
        return new ActivityRepositoryImpl(anyDoneService);
    }

    @Provides
    AddLocationRepository getAddLocationRepository(AnyDoneService anyDoneService) {
        return new AddLocationRepositoryImpl(anyDoneService);
    }

    @Provides
    AcceptedRepository getOngoingRepository(AnyDoneService anyDoneService) {
        return new AcceptedRepositoryImpl(anyDoneService);
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
    AssignedTicketRepository getAssignedTicketRepository(AnyDoneService anyDoneService) {
        return new AssignedTicketRepositoryImpl(anyDoneService);
    }

    @Provides
    SubscribeTicketRepository getSubscribedTicketRepository(AnyDoneService anyDoneService) {
        return new SubscribeTicketRepositoryImpl(anyDoneService);
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
    AddContributorRepository getContributorRepository(AnyDoneService anyDoneService) {
        return new AddContributorRepositoryImpl(anyDoneService);
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
}

