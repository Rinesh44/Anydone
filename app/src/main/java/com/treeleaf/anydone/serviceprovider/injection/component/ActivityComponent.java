package com.treeleaf.anydone.serviceprovider.injection.component;

import com.treeleaf.anydone.serviceprovider.changepassword.ChangePasswordActivity;
import com.treeleaf.anydone.serviceprovider.editprofile.EditProfileActivity;
import com.treeleaf.anydone.serviceprovider.forgotpassword.ForgotPasswordActivity;
import com.treeleaf.anydone.serviceprovider.forgotpassword.resetpassword.ResetPasswordActivity;
import com.treeleaf.anydone.serviceprovider.forgotpassword.verifyCode.VerifyCodeActivity;
import com.treeleaf.anydone.serviceprovider.injection.module.ActivityModule;
import com.treeleaf.anydone.serviceprovider.injection.scope.ScopeActivity;
import com.treeleaf.anydone.serviceprovider.login.LoginActivity;

import com.treeleaf.anydone.serviceprovider.picklocation.PickLocationActivity;
import com.treeleaf.anydone.serviceprovider.profile.ProfileActivity;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity.ServiceRequestDetailActivity;
import com.treeleaf.anydone.serviceprovider.setting.SettingsActivity;
import com.treeleaf.anydone.serviceprovider.setting.currency.SelectCurrencyActivity;
import com.treeleaf.anydone.serviceprovider.setting.language.LanguagesActivity;
import com.treeleaf.anydone.serviceprovider.setting.location.AddLocationActivity;
import com.treeleaf.anydone.serviceprovider.setting.location.showLocation.ShowLocationActivity;
import com.treeleaf.anydone.serviceprovider.setting.timezone.SelectTimezoneActivity;
import com.treeleaf.anydone.serviceprovider.verification.VerificationActivity;

import dagger.Subcomponent;

@Subcomponent(modules = {ActivityModule.class})
@ScopeActivity
public interface ActivityComponent {
    void inject(LoginActivity loginActivity);

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
}
