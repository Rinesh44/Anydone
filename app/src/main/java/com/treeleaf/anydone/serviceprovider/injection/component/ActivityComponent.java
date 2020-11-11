package com.treeleaf.anydone.serviceprovider.injection.component;

import com.treeleaf.anydone.serviceprovider.addcontributor.AddContributorActivity;
import com.treeleaf.anydone.serviceprovider.addpaymentcard.AddCardActivity;
import com.treeleaf.anydone.serviceprovider.addticket.AddTicketActivity;
import com.treeleaf.anydone.serviceprovider.changepassword.ChangePasswordActivity;
import com.treeleaf.anydone.serviceprovider.contributed.ContributedTicketsActivity;
import com.treeleaf.anydone.serviceprovider.editprofile.EditProfileActivity;
import com.treeleaf.anydone.serviceprovider.editticket.EditTicketActivity;
import com.treeleaf.anydone.serviceprovider.forgotpassword.ForgotPasswordActivity;
import com.treeleaf.anydone.serviceprovider.forgotpassword.resetpassword.ResetPasswordActivity;
import com.treeleaf.anydone.serviceprovider.forgotpassword.verifyCode.VerifyCodeActivity;
import com.treeleaf.anydone.serviceprovider.injection.module.ActivityModule;
import com.treeleaf.anydone.serviceprovider.injection.scope.ScopeActivity;
import com.treeleaf.anydone.serviceprovider.linkshare.LinkShareActivity;
import com.treeleaf.anydone.serviceprovider.login.LoginActivity;
import com.treeleaf.anydone.serviceprovider.paymentmethod.PaymentMethodActivity;
import com.treeleaf.anydone.serviceprovider.picklocation.PickLocationActivity;
import com.treeleaf.anydone.serviceprovider.profile.ProfileActivity;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity.ServiceRequestDetailActivity;
import com.treeleaf.anydone.serviceprovider.setting.SettingsActivity;
import com.treeleaf.anydone.serviceprovider.setting.currency.SelectCurrencyActivity;
import com.treeleaf.anydone.serviceprovider.setting.language.LanguagesActivity;
import com.treeleaf.anydone.serviceprovider.setting.location.AddLocationActivity;
import com.treeleaf.anydone.serviceprovider.setting.location.showLocation.ShowLocationActivity;
import com.treeleaf.anydone.serviceprovider.setting.timezone.SelectTimezoneActivity;
import com.treeleaf.anydone.serviceprovider.subscribed.SubscribedTicketsActivity;
import com.treeleaf.anydone.serviceprovider.suggestedTicketPreview.SuggestedTicketPreviewActivity;
import com.treeleaf.anydone.serviceprovider.threaddetails.ThreadDetailActivity;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.tickets.unassignedtickets.UnassignedTicketsActivity;
import com.treeleaf.anydone.serviceprovider.tickets.unsubscribedtickets.UnSubscribedTicketsActivity;
import com.treeleaf.anydone.serviceprovider.ticketsuggestions.TicketSuggestionActivity;
import com.treeleaf.anydone.serviceprovider.verification.VerificationActivity;
import com.treeleaf.anydone.serviceprovider.videocallreceive.VideoCallHandleActivity;

import dagger.Subcomponent;
import retrofit2.Retrofit;

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

    void inject(ServiceRequestDetailActivity serviceRequestActivity);

    void inject(AddLocationActivity addLocationActivity);

    void inject(SelectTimezoneActivity selectTimezoneActivity);

    void inject(SelectCurrencyActivity selectCurrencyActivity);

    void inject(ShowLocationActivity showLocationActivity);

    void inject(PickLocationActivity pickLocationActivity);

    void inject(LanguagesActivity languagesActivity);

    void inject(UnassignedTicketsActivity unassignedTicketsActivity);

    void inject(UnSubscribedTicketsActivity unSubscribedTicketsActivity);

    void inject(TicketDetailsActivity ticketDetailsActivity);

    void inject(AddTicketActivity addTicketActivity);

    void inject(ThreadDetailActivity threadDetailActivity);

    void inject(AddContributorActivity addContributorActivity);

    void inject(LinkShareActivity linkShareActivity);

    void inject(AddCardActivity addCardActivity);

    void inject(PaymentMethodActivity paymentMethodActivity);

    void inject(EditTicketActivity editTicketActivity);

    void inject(ContributedTicketsActivity contributedTicketsActivity);

    void inject(SubscribedTicketsActivity subscribedTicketsActivity);

    void inject(TicketSuggestionActivity ticketSuggestionActivity);

    void inject(SuggestedTicketPreviewActivity suggestedTicketPreviewActivity);
}
