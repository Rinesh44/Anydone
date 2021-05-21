package com.anydone.desk.customertickets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.anydone.desk.R;
import com.anydone.desk.adapters.TicketsAdapter;
import com.anydone.desk.base.activity.MvpBaseActivity;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Customer;
import com.anydone.desk.realm.model.Label;
import com.anydone.desk.realm.model.Service;
import com.anydone.desk.realm.model.Tags;
import com.anydone.desk.realm.model.Tickets;
import com.anydone.desk.realm.repo.AvailableServicesRepo;
import com.anydone.desk.realm.repo.TicketRepo;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.UiUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmList;

public class CustomerTicketActivity extends MvpBaseActivity<CustomerTicketPresenterImpl>
        implements CustomerTicketContract.CustomerTicketView {
    private static final String TAG = "CustomerTicketActivity";

    @BindView(R.id.rv_customer_tickets)
    RecyclerView rvCustomerTickets;
    @BindView(R.id.swipe_refresh_customer_tickets)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.iv_data_not_found)
    ImageView ivDataNotFound;
    @BindView(R.id.pb_search)
    ProgressBar progressBar;
    @BindView(R.id.pb_progress)
    ProgressBar progress;

    private TicketsAdapter ticketsAdapter;
    private List<Tickets> customerTickets;
    private BottomSheetDialog ticketBottomSheet;
    private View llBottomSheet;
    private TextView tvTicketStatus;
    private TextView tvTicketId;
    private TextView tvTicketCreatedDate;
    private TextView tvTicketCreatedTime;
    private TextView tvTicketType;
    private TextView tvTicketTitle;
    private TextView tvEstimatedTimeValue;
    private TextView tvEstimatedTime;
    private TextView tvTicketDesc;
    private TextView tvTicketCreatedBy;
    private CircleImageView civTicketCreatedBy;
    private LinearLayout llTags;
    private LinearLayout llLabelHolder;
    private TextView tvPriority;
    private ImageView ivPriority;
    private TextView tvService;
    private ImageView ivService;
    private TextView tvTicketAssignedEmployee;
    private CircleImageView civTicketAssignedEmployee;
    private TextView tvTicketCustomerName;
    private LinearLayout llTicketCustomerPhone;
    private LinearLayout llTicketCustomerEmail;
    private CircleImageView civTicketCustomer;
    private LinearLayout llContributorList;
    private LinearLayout llLabels;
    private TextView tvTicketCustomerPhone;
    private TextView tvTicketCustomerEmail;
    private NestedScrollView nestedScrollView;
    private BottomSheetDialog employeeBottomSheet;
    private RecyclerView rvEmployee;
    private ImageView ivTick;
    private LinearLayout llEmployeeAsSelf;

    @Override
    protected int getLayout() {
        return R.layout.activity_customer_ticket;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar();
        Intent i = getIntent();
        String customerId = i.getStringExtra("customer_id");
        customerTickets = TicketRepo.getInstance().getCustomerTicketsById(customerId);

        if (CollectionUtils.isEmpty(customerTickets)) {
            GlobalUtils.showLog(TAG, "customer tickets empty");
            ivDataNotFound.setVisibility(View.GONE);
            rvCustomerTickets.setVisibility(View.VISIBLE);
            presenter.getCustomerTickets(customerId, 0, System.currentTimeMillis(), 100);
        } else {
            setUpRecyclerView(customerTickets);
        }

        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh customer ticket called");

                    presenter.getCustomerTickets(customerId, 0, System.currentTimeMillis(),
                            100);

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        if (swipeRefreshLayout != null)
                            swipeRefreshLayout.setRefreshing(false);
                    }, 1000);
                }
        );

        createBottomSheet();
    }

    private void createBottomSheet() {
        ticketBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        llBottomSheet = getLayoutInflater()
                .inflate(R.layout.bottomsheet_linked_tickets, null);

        ticketBottomSheet.setContentView(llBottomSheet);

        ticketBottomSheet.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            setupSheetHeight(d, BottomSheetBehavior.STATE_EXPANDED);
        });


        tvTicketStatus = llBottomSheet.findViewById(R.id.tv_ticket_status);
        tvTicketId = llBottomSheet.findViewById(R.id.tv_ticket_id);
        tvTicketCreatedDate = llBottomSheet.findViewById(R.id.tv_ticket_created_date);
        tvTicketType = llBottomSheet.findViewById(R.id.tv_ticket_type_value);
        tvTicketCreatedTime = llBottomSheet.findViewById(R.id.tv_ticket_created_time);
        tvTicketTitle = llBottomSheet.findViewById(R.id.tv_ticket_title);
        tvEstimatedTimeValue = llBottomSheet.findViewById(R.id.tv_estimated_time_value);
        tvEstimatedTime = llBottomSheet.findViewById(R.id.tv_estimated_time);
        tvTicketDesc = llBottomSheet.findViewById(R.id.tv_ticket_desc);
        tvTicketCreatedBy = llBottomSheet.findViewById(R.id.tv_ticket_created_by);
        civTicketCreatedBy = llBottomSheet.findViewById(R.id.civ_ticket_created_by);
        llTags = llBottomSheet.findViewById(R.id.ll_tags);
        llLabelHolder = llBottomSheet.findViewById(R.id.ll_label_holder);
        tvPriority = llBottomSheet.findViewById(R.id.tv_priority);
        ivPriority = llBottomSheet.findViewById(R.id.iv_priority);
        tvService = llBottomSheet.findViewById(R.id.tv_service);
        ivService = llBottomSheet.findViewById(R.id.iv_service);
        tvTicketAssignedEmployee = llBottomSheet.findViewById(R.id.tv_assigned_employee);
        civTicketAssignedEmployee = llBottomSheet.findViewById(R.id.civ_assigned_employee);
        tvTicketCustomerName = llBottomSheet.findViewById(R.id.tv_customer_name);
        llTicketCustomerPhone = llBottomSheet.findViewById(R.id.ll_customer_phone);
        llTicketCustomerEmail = llBottomSheet.findViewById(R.id.ll_customer_email);
        civTicketCustomer = llBottomSheet.findViewById(R.id.civ_customer);
        llContributorList = llBottomSheet.findViewById(R.id.ll_contributor_list);
        llLabels = llBottomSheet.findViewById(R.id.ll_labels);
        tvTicketCustomerPhone = llBottomSheet.findViewById(R.id.tv_customer_phone);
        tvTicketCustomerEmail = llBottomSheet.findViewById(R.id.tv_customer_email);
        nestedScrollView = llBottomSheet.findViewById(R.id.scroll_view);

        ticketBottomSheet.setOnDismissListener(dialog -> nestedScrollView.fullScroll(ScrollView.FOCUS_UP));
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void getCustomerTicketSuccess() {
        List<Tickets> customerTickets = TicketRepo.getInstance().getCustomerTickets();
        setUpRecyclerView(customerTickets);
    }

    private void setUpRecyclerView(List<Tickets> ticketsList) {
        rvCustomerTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvCustomerTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
            ticketsAdapter = new TicketsAdapter(ticketsList, getContext(), rvCustomerTickets);
            ticketsAdapter.setOnItemClickListener(ticket -> setTicketDetails(ticket.getTicketId()));

            rvCustomerTickets.setAdapter(ticketsAdapter);
        } else {
            GlobalUtils.showLog(TAG, "data not found");
            rvCustomerTickets.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
        }
    }

    private void setupSheetHeight(BottomSheetDialog bottomSheetDialog, int state) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

            int windowHeight = getWindowHeight();
            if (layoutParams != null) {
                layoutParams.height = windowHeight;
            }
            bottomSheet.setLayoutParams(layoutParams);
            behavior.setState(state);
        } else {
            Toast.makeText(this, "bottom sheet null", Toast.LENGTH_SHORT).show();
        }
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    public void getCustomerTicketFail(String msg) {
        ivDataNotFound.setVisibility(View.VISIBLE);
        rvCustomerTickets.setVisibility(View.GONE);
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
        }
//        UiUtils.showSnackBar(getContext(), getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
        ivDataNotFound.setVisibility(View.GONE);
        rvCustomerTickets.setVisibility(View.GONE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Tickets");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
            rvCustomerTickets.setVisibility(View.VISIBLE);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setTicketDetails(long ticketId) {
        Tickets tickets = TicketRepo.getInstance().getTicketById(ticketId);

        switch (tickets.getTicketStatus()) {
            case "TICKET_CREATED":
                tvTicketStatus.setTextColor(Objects.requireNonNull(
                        getResources().getColor(R.color.ticket_created_text)));
                tvTicketStatus.setBackground(getResources()
                        .getDrawable(R.drawable.created_bg));
                tvTicketStatus.setText("TODO");
                break;

            case "TICKET_STARTED":
                tvTicketStatus.setTextColor(Objects.requireNonNull(
                        getResources().getColor(R.color.ticket_started_text)));
                tvTicketStatus.setBackground(getResources()
                        .getDrawable(R.drawable.started_bg));
                tvTicketStatus.setText("STARTED");
                break;

            case "TICKET_RESOLVED":
                tvTicketStatus.setTextColor(Objects.requireNonNull
                        (getResources().getColor(R.color.ticket_resolved_text)));
                tvTicketStatus.setBackground(getResources().getDrawable(R.drawable.resolved_bg));
                tvTicketStatus.setText("RESOLVED");
                break;

            case "TICKET_CLOSED":
                tvTicketStatus.setTextColor(Objects.requireNonNull(
                        getResources().getColor(R.color.ticket_closed_text)));
                tvTicketStatus.setBackground(getResources()
                        .getDrawable(R.drawable.closed_bg));
                tvTicketStatus.setText("CLOSED");

                break;

            case "TICKET_REOPENED":
                tvTicketStatus.setTextColor(Objects.requireNonNull(
                        getResources().getColor(R.color.ticket_reopened_text)));
                tvTicketStatus.setBackground(getResources()
                        .getDrawable(R.drawable.reopened_bg));
                tvTicketStatus.setText("REOPENED");
                break;
        }

        tvTicketId.setText(String.valueOf(tickets.getTicketIndex()));
        tvTicketCreatedDate.setText(GlobalUtils.getDateAlternate(tickets.getCreatedAt()));
        tvTicketCreatedTime.setText(GlobalUtils.getTime(tickets.getCreatedAt()));
        tvTicketType.setText(tickets.getTicketCategory());
        tvTicketTitle.setText(tickets.getTitle());

        if (tickets.getEstimatedTime().isEmpty()) {
            tvEstimatedTimeValue.setVisibility(View.GONE);
            tvEstimatedTime.setVisibility(View.GONE);
        } else {
            tvEstimatedTimeValue.setVisibility(View.VISIBLE);
            tvEstimatedTime.setVisibility(View.VISIBLE);

            if (tickets.getEstimatedTimeStamp() != 0) {
                setRemainingTime(tickets);
            } else {
                tvEstimatedTimeValue.setText(tickets.getEstimatedTime());
            }
        }

        if (tickets.getDescription() == null || tickets.getDescription().isEmpty()) {
            tvTicketDesc.setText("No description");
        } else {
            tvTicketDesc.setText(tickets.getDescription());
        }

        tvTicketCreatedBy.setText(tickets.getCreatedByName());

        String profilePicUrl = tickets.getCreatedByPic();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(this).load(profilePicUrl).apply(options).into(civTicketCreatedBy);
        }

        addTeamsToLayout(tickets);
        setLabels(tickets);
        setPriority(tickets.getPriority());
        setService(tickets.getServiceId());

        setAssignedEmployee(tickets.getAssignedEmployee());
        setLinkedTicketCustomerDetails(tickets.getCustomer());
        setContributors(tickets);
        ticketBottomSheet.show();

    }

    public void setLinkedTicketCustomerDetails(Customer customerDetails) {
        tvTicketCustomerName.setText(customerDetails.getFullName());
        if (customerDetails.getPhone() == null || customerDetails.getPhone().isEmpty()) {
            llTicketCustomerPhone.setVisibility(View.GONE);
        } else {
            tvTicketCustomerPhone.setText(customerDetails.getPhone());
        }

        if (customerDetails.getEmail() == null || customerDetails.getEmail().isEmpty()) {
            llTicketCustomerEmail.setVisibility(View.GONE);
        } else {
            tvTicketCustomerEmail.setText(customerDetails.getEmail());
        }

        String profilePicUrl = customerDetails.getProfilePic();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(this).load(profilePicUrl).apply(options).into(civTicketCustomer);
        }
    }

    private void addTeamsToLayout(Tickets tickets) {
        //add selected teams
        if (tickets.getTagsRealmList() != null &&
                !CollectionUtils.isEmpty(tickets.getTagsRealmList())) {
            llTags.removeAllViews();
            for (Tags tag : tickets.getTagsRealmList()
            ) {
                @SuppressLint("InflateParams") TextView tvTag = (TextView) getLayoutInflater()
                        .inflate(R.layout.layout_tag, null);
                tvTag.setText(tag.getLabel());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMarginEnd(20);
                tvTag.setLayoutParams(params);
                tvTag.setTextSize(14);
                llTags.addView(tvTag);
            }
        } else {
            llTags.removeAllViews();
            TextView textView = new TextView(getContext());
            textView.setText("Not Available");
            textView.setTextColor(getResources().getColor(R.color.black));
            llTags.addView(textView);
        }
    }

    private void setLabels(Tickets tickets) {
        if (!CollectionUtils.isEmpty(tickets.getLabelRealmList())) {
            llLabels.removeAllViews();
            for (Label label : tickets.getLabelRealmList()
            ) {
                @SuppressLint("InflateParams") TextView tvLabel = (TextView) getLayoutInflater()
                        .inflate(R.layout.layout_tag, null);
                tvLabel.setText(label.getName());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMarginEnd(20);
                tvLabel.setLayoutParams(params);
                tvLabel.setTextSize(14);
                llLabels.addView(tvLabel);
            }
        } else {
            llLabels.removeAllViews();
            TextView textView = new TextView(getContext());
            textView.setText("Not Available");
            textView.setTextColor(getResources().getColor(R.color.black));
            llLabels.addView(textView);
        }
    }

    private void setPriority(int priority) {
        switch (priority) {
            case 1:
                tvPriority.setText("Lowest");
                ivPriority.setImageDrawable(getResources().getDrawable(R.drawable.ic_lowest));
                break;

            case 2:
                tvPriority.setText("Low");
                ivPriority.setImageDrawable(getResources().getDrawable(R.drawable.ic_low));
                break;

            case 4:
                tvPriority.setText("High");
                ivPriority.setImageDrawable(getResources().getDrawable(R.drawable.ic_high));
                break;

            case 5:
                tvPriority.setText("Highest");
                ivPriority.setImageDrawable(getResources().getDrawable(R.drawable.ic_highest));
                break;

            default:
                tvPriority.setText("Medium");
                ivPriority.setImageDrawable(getResources().getDrawable(R.drawable.ic_medium));
                break;
        }
    }


    private void setService(String serviceId) {
        Service service = AvailableServicesRepo.getInstance().getAvailableServiceById(serviceId);
        tvService.setText(service.getName());

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_service_ph)
                .error(R.drawable.ic_service_ph);

        Glide.with(this)
                .load(service.getServiceIconUrl())
                .apply(options)
                .into(ivService);
    }


    private void setRemainingTime(Tickets tickets) {
        StringBuilder estTime = new StringBuilder(tickets.getEstimatedTime());
        if (tickets.getEstimatedTimeStamp() > System.currentTimeMillis()) {
            String remainingTime = DateUtils.getRelativeTimeSpanString
                    (tickets.getEstimatedTimeStamp()).toString();

            estTime.append(" (");
            estTime.append(remainingTime.substring(2));
            estTime.append(" remaining ");
            estTime.append(")");
        } else if (tickets.getEstimatedTimeStamp() == 0) {

        } else {
            estTime.append(" ( ");
            estTime.append("time exceeded ");
            estTime.append(")");
        }


        tvEstimatedTimeValue.setText(estTime);
    }


    public void setAssignedEmployee(AssignEmployee assignedEmployee) {
        if (!assignedEmployee.getName().isEmpty()) {
            tvTicketAssignedEmployee.setText(assignedEmployee.getName());
        } else {
            tvTicketAssignedEmployee.setText(R.string.unassigned);
        }

        String employeeImage = assignedEmployee.getEmployeeImageUrl();
        if (employeeImage != null && !employeeImage.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(this)
                    .load(employeeImage)
                    .apply(options)
                    .into(civTicketAssignedEmployee);
        }
    }

    private void setContributors(Tickets tickets) {
        RealmList<AssignEmployee> contributorList = tickets.getContributorList();
        if (!CollectionUtils.isEmpty(contributorList)) {
            inflateContributorLayout(contributorList, llContributorList);
        } else {
            llContributorList.removeAllViews();
            TextView textView = new TextView(getContext());
            textView.setText("Not Available");
            textView.setTextColor(getResources().getColor(R.color.black));
            llContributorList.addView(textView);
        }
    }

    private void inflateContributorLayout(RealmList<AssignEmployee> contributors,
                                          LinearLayout parent) {
        parent.removeAllViews();
        for (AssignEmployee contributor : contributors
        ) {
            @SuppressLint("InflateParams") View viewAssignedEmployee = getLayoutInflater()
                    .inflate(R.layout.layout_contributor_row, null, false);
            TextView employeeName = viewAssignedEmployee.findViewById(R.id.tv_field);
            CircleImageView employeePic = viewAssignedEmployee.findViewById(R.id.civ_field);
            ImageView deleteEmployee = viewAssignedEmployee.findViewById(R.id.iv_delete);

            deleteEmployee.setVisibility(View.GONE);

            employeeName.setText(contributor.getName());
            if (contributor.getEmployeeImageUrl() != null &&
                    !contributor.getEmployeeImageUrl().isEmpty()) {
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_empty_profile_holder_icon)
                        .error(R.drawable.ic_empty_profile_holder_icon);

                Glide.with(this).load(contributor.getEmployeeImageUrl())
                        .apply(options).into(employeePic);
            }

            parent.addView(viewAssignedEmployee);
        }
    }

}