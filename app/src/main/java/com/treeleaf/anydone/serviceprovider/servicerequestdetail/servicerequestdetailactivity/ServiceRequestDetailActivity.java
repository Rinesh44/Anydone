package com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.treeleaf.anydone.entities.OrderServiceProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.landing.LandingActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest;
import com.treeleaf.anydone.serviceprovider.realm.repo.ServiceRequestRepo;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.ServiceRequestDetailFragment;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.activityFragment.ActivityFragment;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.anydone.serviceprovider.videocallreceive.VideoCallMvpBaseActivity;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;


public class ServiceRequestDetailActivity extends VideoCallMvpBaseActivity
        <ServiceRequestDetailActivityPresenterImpl> implements
        ServiceRequestDetailActivityContract.ServiceRequestDetailActivityView,
        View.OnClickListener {
    private static final String TAG = "ServiceRequestDetail";

    @BindView(R.id.pager)
    ViewPager2 viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.toolbar_problem_stat)
    TextView tvToolbarProblemStat;
    @BindView(R.id.pb_progress)
    ProgressBar progress;

    private ServiceRequest serviceRequest;

    public OnOutsideClickListener outsideClickListener;
    /**
     * The number of pages (wizard steps) to show in thisActivity demo.
     */
    private static final int NUM_PAGES = 2;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    private long serviceRequestId;


    private String serviceName;
    private ArrayList<String> serviceProfileUri;
    private boolean paymentSuccess = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (serviceRequest.getStatus().equalsIgnoreCase(OrderServiceProto
                .ServiceOrderState.STARTED_SERVICE_ORDER.name()) ||
                serviceRequest.getStatus().equalsIgnoreCase(OrderServiceProto.ServiceOrderState
                        .ACCEPTED_SERVICE_ORDER.name())) {
            getMenuInflater().inflate(R.menu.menu_service_details, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            //TODO: will be needed later
           /* case R.id.action_video_call:
                presenter.checkConnection(TreeleafMqttClient.mqttClient);
                return true;*/
        }
        return false;
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_service_request_detail;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        setValuesFromIntent();

        super.setRtcContext(Constants.RTC_CONTEXT_SERVICE_REQUEST);
        super.setReferenceId(serviceRequestId);
        super.setServiceName(serviceName);
        super.setServiceProfileUri(serviceProfileUri);
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    private void setUpToolbar(String serviceName, String problemStat) {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        tvToolbarTitle.setText(serviceName.replace("_", " "));
        tvToolbarProblemStat.setText(problemStat);
    }

    private void setValuesFromIntent() {
        Intent i = getIntent();
        serviceRequestId = i.getLongExtra("selected_service_id", -1);
        serviceName = i.getStringExtra("selected_service_name");
        serviceProfileUri = i.getStringArrayListExtra("selected_service_icon_uri");
        serviceRequest = ServiceRequestRepo.getInstance().
                getServiceRequestById(serviceRequestId);
        paymentSuccess = i.getBooleanExtra("payment_success", false);
        setUpToolbar(serviceRequest.getServiceName(), serviceRequest.getProblemStatement());
        GlobalUtils.showLog(TAG, "problem stat;:" + serviceRequest.getProblemStatement());
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), message);
    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String message) {
        showToastMessage(message);
    }

    @Override
    public Context getContext() {
        return this;
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
                    return new ServiceRequestDetailFragment();

                case 1:
                    return new ActivityFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UiUtils.hideKeyboardForced(this);

        if (paymentSuccess) {
            Intent i = new Intent(ServiceRequestDetailActivity.this,
                    LandingActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
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

}
