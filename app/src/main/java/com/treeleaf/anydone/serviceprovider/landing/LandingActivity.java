package com.treeleaf.anydone.serviceprovider.landing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.account.AccountFragment;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.inbox.InboxFragment;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;
import com.treeleaf.anydone.serviceprovider.realm.repo.InboxRepo;
import com.treeleaf.anydone.serviceprovider.threads.ThreadFragment;
import com.treeleaf.anydone.serviceprovider.tickets.TicketsFragment;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

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

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String inboxId = intent.getExtras().getString("inbox_id");
            incrementInboxCount(inboxId);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("broadcast_data"));
        LocalBroadcastManager.getInstance(this).registerReceiver(decrementListener,
                new IntentFilter("broadcast_inbox"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(decrementListener);
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

    public void incrementInboxCount(String inboxId) {
        boolean matchFound = false;
        GlobalUtils.showLog(TAG, "actual inbox id: " + inboxId);
        GlobalUtils.showLog(TAG, "increment inbox count called()");
        List<Inbox> unreadInboxList = InboxRepo.getInstance().getUnreadInboxList();
        GlobalUtils.showLog(TAG, "unread list count: " + unreadInboxList.size());
        for (Inbox inbox : unreadInboxList
        ) {
            GlobalUtils.showLog(TAG, "list inbox id: " + inbox.getInboxId());
            if (inbox.getInboxId().equalsIgnoreCase(inboxId)) {
                GlobalUtils.showLog(TAG, "room for increment");
                matchFound = true;
                break;
            }
        }

        if (!matchFound) {
            int currentCount = bottomNavigationView.getOrCreateBadge(R.id.navigation_inbox).getNumber();
            bottomNavigationView.getOrCreateBadge(R.id.navigation_inbox)
                    .setNumber(currentCount + 1);
            bottomNavigationView.getBadge(R.id.navigation_inbox)
                    .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

    }


    private BroadcastReceiver decrementListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean decrement = intent.getExtras().getBoolean("decrement");
            if (decrement) {
                int currentCount = bottomNavigationView.getOrCreateBadge(R.id.navigation_inbox).getNumber();
                if (currentCount > 1)
                    bottomNavigationView.getOrCreateBadge(R.id.navigation_inbox)
                            .setNumber(currentCount - 1);
                else if (currentCount == 1) {
                    bottomNavigationView.removeBadge(R.id.navigation_inbox);
                }
            }
        }
    };
}
