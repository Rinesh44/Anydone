package com.anydone.desk.threaddetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.anydone.desk.R;
import com.anydone.desk.base.activity.MvpBaseActivity;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.Thread;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.ThreadRepo;
import com.anydone.desk.threaddetails.threadfrontholder.ThreadFrontHolderFragment;
import com.anydone.desk.threaddetails.threadtimeline.ThreadTimelineFragment;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.UiUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ThreadDetailActivity extends MvpBaseActivity<ThreadDetailPresenterImpl> implements
        ThreadDetailContract.ThreadDetailView {
    private static final String TAG = "ThreadDetailActivity";
    private static final int NUM_PAGES = 2;
    private static final String MQTT = "MQTT_EVENT_CHECK";
    @BindView(R.id.pager)
    ViewPager2 viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    @BindView(R.id.civ_customer)
    CircleImageView civCustomer;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_info)
    ImageView ivInfo;
    @BindView(R.id.tv_marked_as_important)
    TextView tvMarkAsImportant;
    @BindView(R.id.tv_follow_up)
    TextView tvFollowUp;

    public OnOutsideClickListener outsideClickListener;
    public OnTitleClickListener onTitleClickListener;
    private FragmentStateAdapter pagerAdapter;

    private Account userAccount;
    private String customerId;
    private String threadId;
    private int pagePosition = 0;

    @Override
    protected int getLayout() {
        return R.layout.layout_thread_details;
    }

    @Override
    protected void onResume() {
        super.onResume();

        showImportantSign();
        showFollowUpSign();

    }

    private void showFollowUpSign() {
        Thread thread = ThreadRepo.getInstance().getThreadById(threadId);
        if (thread.isFollowUp()) {
            tvFollowUp.setVisibility(View.VISIBLE);
        } else {
            tvFollowUp.setVisibility(View.GONE);
        }
    }

    private void showImportantSign() {
        Thread thread = ThreadRepo.getInstance().getThreadById(threadId);
        if (thread.isImportant()) {
            tvMarkAsImportant.setVisibility(View.VISIBLE);
        } else {
            tvMarkAsImportant.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        threadId = i.getStringExtra("thread_id");
        String customerName = i.getStringExtra("customer_name");
        String customerImg = i.getStringExtra("customer_img");
        setUpToolbar(customerName, customerImg);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        userAccount = AccountRepo.getInstance().getAccount();

        Thread thread = ThreadRepo.getInstance().getThreadById(threadId);
        customerId = thread.getCustomerId();
        GlobalUtils.showLog(TAG, "thread status check after: " + thread.isSeen());

        ivInfo.setOnClickListener(view -> viewPager.setCurrentItem(1, true));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                pagePosition = position;

                if (position == 1) {
                    ivInfo.setVisibility(View.GONE);
                    tvMarkAsImportant.setVisibility(View.GONE);
                } else {
                    ivInfo.setVisibility(View.VISIBLE);
                    showImportantSign();
                }

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                GlobalUtils.showLog(TAG, "on page selected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
//            case R.id.action_video_call:
//                return true;
        }
        return false;
    }

    @OnClick(R.id.iv_back)
    public void back() {
        hideKeyBoard();
        onBackPressed();
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
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                Constants.SERVER_ERROR);
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void setUpToolbar(String customerName, String customerImg) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tvToolbarTitle.setText(customerName);

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                .error(R.drawable.ic_empty_profile_holder_icon);

        Glide.with(this).load(customerImg).apply(options).into(civCustomer);

        tvToolbarTitle.setOnClickListener(v -> {
            if (onTitleClickListener != null) {
                onTitleClickListener.onTitleClick(customerId);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 1) {
            viewPager.setCurrentItem(0);
        } else super.onBackPressed();
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

    public interface OnTitleClickListener {
        void onTitleClick(String customerId);
    }

    public void setTitleClickListener(OnTitleClickListener listener) {
        onTitleClickListener = listener;
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
                    return new ThreadFrontHolderFragment();

                case 1:
                    return new ThreadTimelineFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}