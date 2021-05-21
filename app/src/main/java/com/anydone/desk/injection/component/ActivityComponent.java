package com.anydone.desk.injection.component;

import com.anydone.desk.addcontributor.AddContributorActivity;
import com.anydone.desk.addparticipant.AddParticipantActivity;
import com.anydone.desk.addpaymentcard.AddCardActivity;
import com.anydone.desk.addticket.AddTicketActivity;
import com.anydone.desk.alltickets.AllTicketsActivity;
import com.anydone.desk.assignedtickets.AssignedTicketsActivity;
import com.anydone.desk.changepassword.ChangePasswordActivity;
import com.anydone.desk.contributed.ContributedTicketsActivity;
import com.anydone.desk.creategroup.CreateGroupActivity;
import com.anydone.desk.customertickets.CustomerTicketActivity;
import com.anydone.desk.dashboard.DashboardFragment;
import com.anydone.desk.editInbox.EditInboxActivity;
import com.anydone.desk.editprofile.EditProfileActivity;
import com.anydone.desk.editticket.EditTicketActivity;
import com.anydone.desk.forgotpassword.ForgotPasswordActivity;
import com.anydone.desk.forgotpassword.resetpassword.ResetPasswordActivity;
import com.anydone.desk.forgotpassword.verifyCode.VerifyCodeActivity;
import com.anydone.desk.forwardMessage.ForwardMessageActivity;
import com.anydone.desk.inboxdetails.InboxDetailActivity;
import com.anydone.desk.injection.module.ActivityModule;
import com.anydone.desk.injection.scope.ScopeActivity;
import com.anydone.desk.inviteuserstocall.InviteUsersActivity;
import com.anydone.desk.linkshare.LinkShareActivity;
import com.anydone.desk.login.LoginActivity;
import com.anydone.desk.opentickets.OpenTicketActivity;
import com.anydone.desk.ownedtickets.OwnedTicketActivity;
import com.anydone.desk.paymentmethod.PaymentMethodActivity;
import com.anydone.desk.picklocation.PickLocationActivity;
import com.anydone.desk.profile.ProfileActivity;
import com.anydone.desk.reply.ReplyActivity;
import com.anydone.desk.searchconversation.SearchConversation;
import com.anydone.desk.servicerequestdetail.servicerequestdetailactivity.ServiceRequestDetailActivity;
import com.anydone.desk.setting.SettingsActivity;
import com.anydone.desk.setting.currency.SelectCurrencyActivity;
import com.anydone.desk.setting.language.LanguagesActivity;
import com.anydone.desk.setting.location.AddLocationActivity;
import com.anydone.desk.setting.location.showLocation.ShowLocationActivity;
import com.anydone.desk.setting.timezone.SelectTimezoneActivity;
import com.anydone.desk.subscribed.SubscribedTicketsActivity;
import com.anydone.desk.suggestedTicketPreview.SuggestedTicketPreviewActivity;
import com.anydone.desk.threaddetails.ThreadDetailActivity;
import com.anydone.desk.ticketdetails.TicketDetailsActivity;
import com.anydone.desk.tickets.unassignedtickets.UnassignedTicketsActivity;
import com.anydone.desk.tickets.unsubscribedtickets.UnSubscribedTicketsActivity;
import com.anydone.desk.ticketsuggestions.TicketSuggestionActivity;
import com.anydone.desk.verification.VerificationActivity;
import com.anydone.desk.videocallreceive.VideoCallHandleActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class})
@ScopeActivity
public interface ActivityComponent {
    void inject(LoginActivity loginActivity);

    void inject(VideoCallHandleActivity videoReceiveActivity);

    void inject(VerificationActivity verificationActivity);

    void inject(ProfileActivity profileActivity);

    void inject(ChangePasswordActivity changePasswordActivity);

    void inject(EditProfileActivity editProfileActivity);

    void inject(SettingsActivity settingsActivity);

    void inject(ForgotPasswordActivity forgotPasswordActivity);

    void inject(VerifyCodeActivity verifyCodeActivity);

    void inject(ResetPasswordActivity resetPasswordActivity);

    void inject(AddLocationActivity addLocationActivity);

    void inject(SelectTimezoneActivity selectTimezoneActivity);

    void inject(SelectCurrencyActivity selectCurrencyActivity);

    void inject(ShowLocationActivity showLocationActivity);

    void inject(PickLocationActivity pickLocationActivity);

    void inject(LanguagesActivity languagesActivity);

    void inject(ServiceRequestDetailActivity serviceRequestActivity);

    void inject(UnassignedTicketsActivity unassignedTicketsActivity);

    void inject(UnSubscribedTicketsActivity unSubscribedTicketsActivity);

    void inject(TicketDetailsActivity ticketDetailsActivity);

    void inject(AddTicketActivity addTicketActivity);

    void inject(ThreadDetailActivity threadDetailActivity);

    void inject(AddContributorActivity addContributorActivity);

    void inject(InviteUsersActivity inviteUsersActivity);

    void inject(LinkShareActivity linkShareActivity);

    void inject(AddCardActivity addCardActivity);

    void inject(PaymentMethodActivity paymentMethodActivity);

    void inject(EditTicketActivity editTicketActivity);

    void inject(ContributedTicketsActivity contributedTicketsActivity);

    void inject(SubscribedTicketsActivity subscribedTicketsActivity);

    void inject(TicketSuggestionActivity ticketSuggestionActivity);

    void inject(SuggestedTicketPreviewActivity suggestedTicketPreviewActivity);

    void inject(AllTicketsActivity allTicketsActivity);

    void inject(OpenTicketActivity openTicketActivity);

    void inject(OwnedTicketActivity ownedTicketActivity);

    void inject(CustomerTicketActivity customerTicketActivity);

    void inject(InboxDetailActivity inboxDetailActivity);

    void inject(AddParticipantActivity addParticipantActivity);

    void inject(CreateGroupActivity createGroupActivity);

    void inject(EditInboxActivity editInboxActivity);

    void inject(DashboardFragment dashboardFragment);

    void inject(ForwardMessageActivity forwardMessageActivity);

    void inject(ReplyActivity replyActivity);

    void inject(SearchConversation searchConversation);

    void inject(AssignedTicketsActivity assignedTicketsActivity);
}
