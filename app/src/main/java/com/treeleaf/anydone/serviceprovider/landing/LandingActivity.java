package com.treeleaf.anydone.serviceprovider.landing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.account.AccountFragment;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.dashboard.DashboardFragment;
import com.treeleaf.anydone.serviceprovider.servicerequests.ServiceRequestFragment;
import com.treeleaf.anydone.serviceprovider.threads.ThreadFragment;
import com.treeleaf.anydone.serviceprovider.tickets.TicketsFragment;

import butterknife.BindView;

public class LandingActivity extends MvpBaseActivity<LandingPresenterImpl>
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        LandingContract.LandingView {

    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        openFragment(DashboardFragment.newInstance("", ""));
        bottomNavigationView.getMenu().getItem(2).setChecked(true);
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
                openFragment(ThreadFragment.newInstance("", ""));
                return true;

            case R.id.navigation_dashboard:
                openFragment(DashboardFragment.newInstance("", ""));
                return true;

            case R.id.navigation_account:
//                setActionTitle(ACCOUNT);
                openFragment(AccountFragment.newInstance("", ""));
                return true;
            case R.id.navigation_service_requests:
//                setActionTitle(SERVICE_REQUESTS);
                openFragment(ServiceRequestFragment.newInstance("", ""));
                return true;
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
