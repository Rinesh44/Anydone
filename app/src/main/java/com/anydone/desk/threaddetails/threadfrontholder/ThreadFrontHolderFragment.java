package com.anydone.desk.threaddetails.threadfrontholder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.anydone.desk.R;
import com.anydone.desk.base.fragment.BaseFragment;
import com.anydone.desk.injection.component.ApplicationComponent;
import com.anydone.desk.realm.model.Thread;
import com.anydone.desk.realm.repo.ThreadRepo;
import com.anydone.desk.threaddetails.threadconversation.ThreadConversationFragment;
import com.anydone.desk.threaddetails.threadfrontholder.threadactivitylog.ThreadActivityLogFragment;
import com.anydone.desk.threaddetails.threadfrontholder.threadcalllog.ThreadCallLogFragment;
import com.anydone.desk.threaddetails.threadfrontholder.threadcomments.ThreadCommentsFragment;
import com.anydone.desk.utils.NonSwipeableViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class ThreadFrontHolderFragment extends BaseFragment<ThreadFrontHolderPresenterImpl>
        implements ThreadFrontHolderContract.ThreadFrontHolderView {

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    NonSwipeableViewPager viewPager;
    @BindView(R.id.tv_marked_as_important)
    TextView tvMarkAsImportant;

    private String threadId;

    @Override
    protected int getLayout() {
        return R.layout.fragment_thread_front_holder;
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        Intent i = Objects.requireNonNull(getActivity()).getIntent();
        threadId = i.getStringExtra("thread_id");

    }

    @Override
    public void onResume() {
        super.onResume();

/*        Thread thread = ThreadRepo.getInstance().getThreadById(threadId);
        if (thread.isImportant()) {
            tvMarkAsImportant.setVisibility(View.VISIBLE);
        } else {
            tvMarkAsImportant.setVisibility(View.GONE);
        }*/


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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new ThreadConversationFragment(), "Messages");
        viewPagerAdapter.addFragment(new ThreadCallLogFragment(), "Call Log");
        viewPagerAdapter.addFragment(new ThreadCommentsFragment(), "Comments");
        viewPagerAdapter.addFragment(new ThreadActivityLogFragment(), "Activity Log");
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(viewPagerAdapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
