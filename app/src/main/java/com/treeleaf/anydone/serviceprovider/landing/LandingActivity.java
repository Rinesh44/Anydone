package com.treeleaf.anydone.serviceprovider.landing;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.account.AccountFragment;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.inbox.InboxFragment;
import com.treeleaf.anydone.serviceprovider.threads.ThreadFragment;
import com.treeleaf.anydone.serviceprovider.tickets.TicketsFragment;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import butterknife.BindView;

public class
LandingActivity extends MvpBaseActivity<LandingPresenterImpl>
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        LandingContract.LandingView {

    private static final String TAG = "LandingActivity";

    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        openFragment(TicketsFragment.newInstance("", ""));
        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        bottomNavigationView.getOrCreateBadge(R.id.navigation_inbox)
                .setNumber(4);
        bottomNavigationView.getBadge(R.id.navigation_inbox)
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_landing;
    }

    @Override
    protected void injectDagger() {

    }

    @Override
    public void showProgressBar(String message) {

    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_tickets:
//                setActionTitle(HOME);
                openFragment(TicketsFragment.newInstance("", ""));
                return true;

            case R.id.navigation_threads:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    openFragment(ThreadFragment.newInstance("", ""));
                } else {
                    Banner.make(getWindow().getDecorView().getRootView(),
                            this, Banner.ERROR, "Some of our features are not" +
                                    " supported in your device. Sorry for inconvenience",
                            Banner.BOTTOM, 2000).show();
                }
                return true;

            case R.id.navigation_inbox:
                openFragment(InboxFragment.newInstance("", ""));
                return true;

            case R.id.navigation_account:
//                setActionTitle(ACCOUNT);
                openFragment(AccountFragment.newInstance("", ""));
                return true;

        /*    case R.id.navigation_tick:
                openFragment(MoreTicketFragment.newInstance("", ""));
                return true;*/

       /*     case R.id.navigation_service_requests:
//                setActionTitle(SERVICE_REQUESTS);
                openFragment(ServiceRequestFragment.newInstance("", ""));
                return true;*/
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
