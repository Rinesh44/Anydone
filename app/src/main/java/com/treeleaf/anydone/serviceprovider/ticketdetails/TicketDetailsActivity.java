package com.treeleaf.anydone.serviceprovider.ticketdetails;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation.TicketConversationFragment;
import com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline.TicketTimelineFragment;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.Objects;

import butterknife.BindView;

public class TicketDetailsActivity extends MvpBaseActivity<TicketDetailsPresenterImpl> implements
        TicketDetailsContract.TicketDetailsView {
    private static final int NUM_PAGES = 2;
    @BindView(R.id.pager)
    ViewPager2 viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.toolbar_problem_stat)
    TextView tvToolbarProblemStat;

    private ProgressDialog progress;
    public OnOutsideClickListener outsideClickListener;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_ticket_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        long ticketId = i.getLongExtra("selected_ticket_id", 0);
        String ticketTitle = i.getStringExtra("ticket_desc");
        setUpToolbar(ticketId, ticketTitle);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
//            case R.id.action_video_call:
//                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_service_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void showProgressBar(String message) {
        progress = ProgressDialog.show(this, null, message, true);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), message);
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void setUpToolbar(long ticketId, String problemStat) {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        tvToolbarTitle.setText("#" + ticketId);
        tvToolbarProblemStat.setText(problemStat);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            outsideClickListener.onOutsideClick(event);
        }
        return super.dispatchTouchEvent(event);
    }

    public interface OnOutsideClickListener {
        void onOutsideClick(MotionEvent event);
    }

    public void setOutSideTouchListener(OnOutsideClickListener listener) {
        outsideClickListener = listener;
    }

    private class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentManager fragmentManager,
                                @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new TicketConversationFragment();

                case 1:
                    return new TicketTimelineFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}