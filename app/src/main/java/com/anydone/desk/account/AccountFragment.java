package com.anydone.desk.account;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.hawk.Hawk;
import com.anydone.desk.R;
import com.anydone.desk.aboutus.AboutUsActivity;
import com.anydone.desk.base.fragment.BaseFragment;
import com.anydone.desk.billing.BillingActivity;
import com.anydone.desk.dashboard.DashboardFragment;
import com.anydone.desk.injection.component.ApplicationComponent;
import com.anydone.desk.login.LoginActivity;
import com.anydone.desk.profile.ProfileActivity;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.setting.SettingsActivity;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.UiUtils;

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
    @BindView(R.id.rl_billing)
    RelativeLayout rlBilling;
    @BindView(R.id.rl_dashboard)
    RelativeLayout rlDashboard;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;
    /*  @BindView(R.id.rl_contributed)
      RelativeLayout rlContributed;
      @BindView(R.id.rl_subscribed)
      RelativeLayout rlSubscribed;*/
    private long mLastClickTime = 0;

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Account account = AccountRepo.getInstance().getAccount();
        if (account.getAccountType().equalsIgnoreCase("SERVICE_PROVIDER")) {
            rlBilling.setVisibility(View.VISIBLE);
        } else {
            rlBilling.setVisibility(View.GONE);
        }
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
                .getDecorView().getRootView(), Constants.SERVER_ERROR);
    }

    @OnClick(R.id.rl_profile_acc)
    void onClickRlProfile() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        startActivity(new Intent(getActivity(), ProfileActivity.class));
    }

    @OnClick(R.id.rl_logout)
    void onClickLogout() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        showLogoutDialog();
    }

    @OnClick(R.id.rl_setting_acc)
    void onClickSetting() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }

    @OnClick(R.id.rl_about)
    void onClickAbout() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        startActivity(new Intent(getActivity(), AboutUsActivity.class));
    }

    @OnClick(R.id.rl_billing)
    void onClickBilling() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        startActivity(new Intent(getActivity(), BillingActivity.class));
    }

    @OnClick(R.id.rl_dashboard)
    void onClickDashboard() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        startActivity(new Intent(getActivity(), DashboardFragment.class));
    }

/*    @OnClick(R.id.rl_contributed)
    void onClickContributed() {
        startActivity(new Intent(getActivity(), ContributedTicketsActivity.class));
    }

    @OnClick(R.id.rl_subscribed)
    void onClickSubscribed() {
        startActivity(new Intent(getActivity(), SubscribedTicketsActivity.class));
    }*/

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
        //disconnect mqtt
//        TreeleafMqttClient.disconnect();

        //clear all databases
        Hawk.deleteAll();
        final Realm realm = Realm.getDefaultInstance();
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
