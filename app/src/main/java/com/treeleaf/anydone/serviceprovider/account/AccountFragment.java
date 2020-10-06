package com.treeleaf.anydone.serviceprovider.account;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.aboutus.AboutUsActivity;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.billing.BillingActivity;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.login.LoginActivity;
import com.treeleaf.anydone.serviceprovider.profile.ProfileActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.setting.SettingsActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

public class AccountFragment extends BaseFragment<AccountPresenterImpl>
        implements AccountContract.AccountView {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.iv_profile_icon)
    ImageView ivProfileIcon;
    @BindView(R.id.iv_profile_pic_set)
    CircleImageView ivProfilePicSet;
    @BindView(R.id.tv_profile)
    TextView tvProfile;
    @BindView(R.id.pb_progress)
    ProgressBar progress;


    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();

        Account account = AccountRepo.getInstance().getAccount();
        if (account != null) {
            tvProfile.setText(account.getFullName());
            String profilePicUrl = account.getProfilePic();
            if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                ivProfilePicSet.setVisibility(View.VISIBLE);
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_profile_icon)
                        .error(R.drawable.ic_profile_icon);

                Glide.with(this).load(profilePicUrl).apply(options).into(ivProfilePicSet);
            }
        }
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_account;
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressBar() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(getActivity(), Objects.requireNonNull(getActivity()).getWindow()
                .getDecorView().getRootView(), message);
    }

    @OnClick(R.id.rl_profile_acc)
    void onClickRlProfile() {
        startActivity(new Intent(getActivity(), ProfileActivity.class));
    }

    @OnClick(R.id.rl_logout)
    void onClickLogout() {
        showLogoutDialog();
    }

    @OnClick(R.id.rl_setting_acc)
    void onClickSetting() {
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }

    @OnClick(R.id.rl_about)
    void onClickAbout() {
        startActivity(new Intent(getActivity(), AboutUsActivity.class));
    }

    @OnClick(R.id.rl_billing)
    void onClickBilling() {
        startActivity(new Intent(getActivity(), BillingActivity.class));
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to logout?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    String token = Hawk.get(Constants.TOKEN);
                    if (token != null) {
                        presenter.logout(token);
                    } else {
                        UiUtils.showSnackBar(getActivity(),
                                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                                "Authorization failed");
                    }

                });

        builder1.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(R.color.colorPrimary));

        });
        alert11.show();
    }


    @Override
    public void onLogoutSuccess() {
        //clear all databases
        Hawk.deleteAll();
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> realm1.deleteAll());

        Intent i = new Intent(getActivity(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onLogoutFail(String msg) {
        UiUtils.showSnackBar(getActivity(), Objects.requireNonNull(
                getActivity()).getWindow().getDecorView().getRootView(), msg);
    }
}
