package com.treeleaf.anydone.serviceprovider.ticketdetails;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.linkshare.LinkShareActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation.OnStatusChangeListener;
import com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation.TicketConversationFragment;
import com.treeleaf.anydone.serviceprovider.ticketdetails.ticketfrontholder.TicketFrontHolderFragment;
import com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline.TicketTimelineFragment;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.DialogUtils;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.anydone.serviceprovider.videocallreceive.VideoCallMvpBaseActivity;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static com.treeleaf.anydone.serviceprovider.utils.Constants.RTC_CONTEXT_TICKET;
import static com.treeleaf.januswebrtc.Const.CONSUMER_TYPE;
import static com.treeleaf.januswebrtc.Const.SERVICE_PROVIDER_TYPE;

public class TicketDetailsActivity extends VideoCallMvpBaseActivity<TicketDetailsPresenterImpl> implements
        TicketDetailsContract.TicketDetailsView, OnStatusChangeListener {
    private static final String TAG = "TicketDetailsActivity";
    private static final int NUM_PAGES = 2;
    private static final String MQTT = "MQTT_EVENT_CHECK";
    @BindView(R.id.pager)
    ViewPager2 viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_ticket_status)
    TextView tvTicketStatus;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.ic_video_call)
    ImageView ivVideoCall;
    @BindView(R.id.tv_connection_status)
    TextView tvConnectionStatus;
    /*    @BindView(R.id.ic_info)
        ImageView ivInfo;*/
    @BindView(R.id.iv_back)
    ImageView ivBack;

    public OnOutsideClickListener outsideClickListener;
    private FragmentStateAdapter pagerAdapter;
    private BottomSheetDialog linkShareBottomSheet;
    private RelativeLayout rlCopy;
    private RelativeLayout rlSms;
    private RelativeLayout rlEmail;
    private RelativeLayout rlOther;
    private String ticketType;
    String shareLink = "";
    private long ticketId;
    private String ticketStatus;
    private Account userAccount;
    private boolean contributed = false;
    private Tickets ticket;
    private boolean isServiceProvider = false;
    private long ticketIndex;
    private String localAccountId;
    private String accountType = SERVICE_PROVIDER_TYPE;//default is service provider
    private TicketFrontHolderFragment ticketFrontHolderFragment;
    private TicketTimelineFragment ticketTimelineFragment;
    private static boolean isTicketCallableAndSharable;

    static {
        isTicketCallableAndSharable = true;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_ticket_details;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        ticketType = i.getStringExtra("selected_ticket_type");
        ticketStatus = i.getStringExtra("selected_ticket_status");
        ticketId = i.getLongExtra("selected_ticket_id", 0);
        ticketIndex = i.getLongExtra("selected_ticket_index", 0);
        contributed = i.getBooleanExtra("contributed", false);
//        String ticketTitle = i.getStringExtra("ticket_desc");
        String serviceName = i.getStringExtra("selected_ticket_name");
        ArrayList<String> serviceProfileUri = i.getStringArrayListExtra("selected_ticket_icon_uri");

        GlobalUtils.showLog(TAG, "ticket type print: " + ticketType);
        ticket = TicketRepo.getInstance().getTicketByIdAndStatus(ticketId, ticketStatus);
        setUpToolbar(ticketIndex, ticket.getTicketStatus());

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);

        createLinkShareBottomSheet();

        userAccount = AccountRepo.getInstance().getAccount();
        localAccountId = userAccount.getAccountId();

        isServiceProvider = ticket.getTicketType().equalsIgnoreCase(Constants.SERVICE_PROVIDER);

        if (serviceProfileUri != null && !(serviceProfileUri.size() > 0)) {
            isTicketCallableAndSharable = false;
        }

        setVideoCallVisibility();

        GlobalUtils.showLog(TAG, "ticket status: " + ticketStatus);
        if (ticketStatus != null && ticketStatus.equalsIgnoreCase("TICKET_CREATED")) {
            GlobalUtils.showLog(TAG, "disable menu options called()");
            isTicketCallableAndSharable = false;
        }


//        ivInfo.setOnClickListener(view -> viewPager.setCurrentItem(1, true));

        //set link share visibility
        if (!ticket.getAssignedEmployee().getAccountId().equalsIgnoreCase(userAccount.getAccountId())
                && !ticket.getCreatedById().equalsIgnoreCase(userAccount.getAccountId())
                && !contributed && !isServiceProvider) {
            isTicketCallableAndSharable = false;
        }

        if (ticket.getTicketStatus().equals("TICKET_CLOSED") ||
                ticket.getTicketStatus().equals("TICKET_RESOLVED")) {
            isTicketCallableAndSharable = false;
        }

        //check if account type is customer or not.

        Customer customer = ticket.getCustomer();
        String customerName = customer.getFullName();

        if (customer != null && localAccountId.equals(customer.getCustomerId())
                && !customerName.isEmpty()) {
            accountType = CONSUMER_TYPE;
        } else
            accountType = SERVICE_PROVIDER_TYPE;

        super.setReferenceId(String.valueOf(ticketId));
        super.setServiceName(serviceName);
        super.setServiceProfileUri(serviceProfileUri);
        super.setAccountType(accountType);


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

        /*        if (position == 1) {
                    ivInfo.setVisibility(View.GONE);
                } else {
                    ivInfo.setVisibility(View.VISIBLE);
                }*/
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        ivBack.setOnClickListener(view -> onBackPressed());
        presenter.getShareLink(String.valueOf(ticketId));
    }

    @Override
    protected String getCallContext() {
        return RTC_CONTEXT_TICKET;
    }

    private void setVideoCallVisibility() {
        boolean isCustomer = ticket.getCustomer().getCustomerId().equalsIgnoreCase(userAccount.getAccountId());
        if (ticket.getAssignedEmployee().getAccountId().equalsIgnoreCase(userAccount.getAccountId())
                || contributed || isCustomer) {
            isTicketCallableAndSharable = true;
        } else {
            isTicketCallableAndSharable = false;
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void createLinkShareBottomSheet() {
        linkShareBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.bottom_sheet_link, null);

        linkShareBottomSheet.setContentView(view);
        rlCopy = view.findViewById(R.id.rl_copy);
        rlSms = view.findViewById(R.id.rl_sms);
        rlEmail = view.findViewById(R.id.rl_email);
        rlOther = view.findViewById(R.id.rl_other);

        rlCopy.setOnClickListener(v -> {
            if (!shareLink.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) Objects.requireNonNull(getContext())
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copied_text", shareLink);
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                linkShareBottomSheet.dismiss();
                Toast.makeText(this, "Link copied", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, "No link found", Toast.LENGTH_SHORT).show();
                presenter.getShareLink(String.valueOf(ticketId));
            }
        });

        rlOther.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareLink);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

        rlSms.setOnClickListener(v -> {
            linkShareBottomSheet.dismiss();
            Intent i = new Intent(this, LinkShareActivity.class);
            i.putExtra("ticket_id", ticketId);
            i.putExtra("is_email", false);
            i.putExtra("link", shareLink);
            startActivity(i);
        });

        rlEmail.setOnClickListener(v -> {
            linkShareBottomSheet.dismiss();
            Intent i = new Intent(this, LinkShareActivity.class);
            i.putExtra("ticket_id", ticketId);
            i.putExtra("is_email", true);
            i.putExtra("link", shareLink);
            startActivity(i);
        });
    }

    @OnClick(R.id.iv_share)
    public void share() {
        if (isTicketCallableAndSharable) {
            if (linkShareBottomSheet.isShowing()) {
                linkShareBottomSheet.dismiss();
            } else {
                linkShareBottomSheet.show();
            }
        } else {
            showAlertDialog("You cannot generate link until ticket is started!!!");
        }
    }

    @OnClick(R.id.ic_video_call)
    public void startVideoCall() {
        if (isTicketCallableAndSharable)
            checkConnection(accountType);
        else {
            showAlertDialog("Please start the ticket to execute the call !!!");
        }
    }

    private void showAlertDialog(String message) {
        DialogUtils.Builder builder = new DialogUtils.Builder(getContext());
        DialogUtils dialogFragment = builder
                .setTitle("Error")
                .setMessage(message)
                .setCanceleable(true)
                .setPositiveButtonTitle(getString(R.string.ok))
                .setNegativeButtonTitle(getString(R.string.cancel))
                .setDialogCallback(new AlertDialogCallback() {
                    @Override
                    public void onPositiveButtonClicked() {

                    }

                    @Override
                    public void onNegativeButtonClicked() {

                    }
                })
                .build();
        dialogFragment.show(getSupportFragmentManager(), TAG);
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }*/


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ticket_details, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

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

    private void setUpToolbar(long ticketIndex, String ticketStatus) {
//        setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("");
        tvToolbarTitle.setText("#" + ticketIndex);
        setTicketStatus(ticketStatus);
    }

    private void setTicketStatus(String ticketStatus) {
        switch (ticketStatus) {
            case "TICKET_CREATED":
                tvTicketStatus.setTextColor(getResources().getColor(R.color.ticket_created_text));
                tvTicketStatus.setBackground(getResources()
                        .getDrawable(R.drawable.created_bg));
                tvTicketStatus.setText("TODO");
                break;

            case "TICKET_STARTED":
                tvTicketStatus.setTextColor(getResources().getColor(R.color.ticket_started_text));
                tvTicketStatus.setBackground(getResources()
                        .getDrawable(R.drawable.started_bg));
                tvTicketStatus.setText("STARTED");
                break;

            case "TICKET_RESOLVED":
                tvTicketStatus.setTextColor(getResources().getColor(R.color.ticket_resolved_text));
                tvTicketStatus.setBackground(getResources()
                        .getDrawable(R.drawable.resolved_bg));
                tvTicketStatus.setText("RESOLVED");

                break;

            case "TICKET_CLOSED":
                tvTicketStatus.setTextColor(getResources().getColor(R.color.ticket_closed_text));
                tvTicketStatus.setBackground(getResources()
                        .getDrawable(R.drawable.closed_bg));
                tvTicketStatus.setText("CLOSED");
                break;

            case "TICKET_REOPENED":
                tvTicketStatus.setTextColor(getResources().getColor(R.color.ticket_reopened_text));
                tvTicketStatus.setBackground(getResources()
                        .getDrawable(R.drawable.reopened_bg));
                tvTicketStatus.setText("REOPENED");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //todo replace with ticket front holder
//        ticketConversationFragment.unSubscribeMqttTopics();

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

    @Override
    public void onLinkShareSuccess(String link) {
        shareLink = link;
    }

    @Override
    public void onLinkShareFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void onTaskStarted() {
        setTicketStatus("TICKET_STARTED");
        setVideoCallVisibility();
    }

    @Override
    public void onTaskResolved() {
        setTicketStatus("TICKET_RESOLVED");
    }

    @Override
    public void onTaskClosed() {
        setTicketStatus("TICKET_CLOSED");
    }

    @Override
    public void onTaskReopened() {
        setTicketStatus("TICKET_REOPENED");
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
                    ticketFrontHolderFragment = new TicketFrontHolderFragment();
                    return ticketFrontHolderFragment;

                case 1:
                    ticketTimelineFragment = new TicketTimelineFragment();
                    return ticketTimelineFragment;
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof TicketTimelineFragment) {
            ((TicketTimelineFragment) fragment).setOnTicketStartListener(this);
        }

        if (fragment instanceof TicketConversationFragment) {
            ((TicketConversationFragment) fragment).setOnVideoCallBackListener(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public interface MqttDelegate {
        void unSubscribeMqttTopics();
    }

    @Override
    public void mqttConnected() {
        if (tvConnectionStatus != null)
            tvConnectionStatus.setText("Connected");
      /*  Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                getActivity(), Banner.SUCCESS, "Connected", Banner.TOP, 2000).show();*/

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (tvConnectionStatus != null) tvConnectionStatus.setVisibility(View.GONE);
        }, 2000);

    }

    @Override
    public void mqttNotConnected() {

    }

}