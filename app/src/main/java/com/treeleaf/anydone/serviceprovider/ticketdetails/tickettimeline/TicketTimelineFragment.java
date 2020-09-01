package com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.OrderServiceProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.assignemployee.AssignEmployeeActivity;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.ServiceOrderEmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.januswebrtc.Const;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmList;

public class TicketTimelineFragment extends BaseFragment<TicketTimelinePresenterImpl> implements
        TicketTimelineContract.TicketTimelineView,
        TicketDetailsActivity.OnOutsideClickListener {
    private static final String TAG = "TicketTimelineFragment";

    public static final int ASSIGN_EMPLOYEE_REQUEST = 8789;
    /*    @BindView(R.id.ll_activities)
        LinearLayout llActivities;*/
    @BindView(R.id.tv_assigned_employee_dropdown)
    TextView tvAssignedEmployeeDropDown;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    /*    @BindView(R.id.tv_activity_dropdown)
        TextView tvActivityDropDown;
        @BindView(R.id.expandable_layout_activities)
        ExpandableLayout elActivities;*/
    @BindView(R.id.expandable_layout_employee)
    ExpandableLayout elEmployee;
    @BindView(R.id.iv_dropdown_employee)
    ImageView ivDropdownEmployee;
    /*    @BindView(R.id.iv_dropdown_activity)
        ImageView ivDropdownActivity;*/
    @BindView(R.id.ll_assigned_employee)
    LinearLayout llAssignedEmployee;
    @BindView(R.id.ll_assined_employee_top)
    LinearLayout llAssignedEmployeeTop;
    /*    @BindView(R.id.tv_elapsed_time)
        TextView tvElapsedTime;*/
    @BindView(R.id.bottom_sheet_profile)
    LinearLayout mBottomSheet;
    @BindView(R.id.tv_customer_dropdown)
    TextView tvCustomerDropdown;
    @BindView(R.id.iv_dropdown_customer)
    ImageView ivDropdownCustomer;
    @BindView(R.id.expandable_layout_customer)
    ExpandableLayout elCustomer;
    @BindView(R.id.ll_customer_email)
    LinearLayout llCustomerEmail;
    @BindView(R.id.ll_customer_phone)
    LinearLayout llCustomerPhone;
    @BindView(R.id.tv_customer_name)
    TextView tvCustomerName;
    @BindView(R.id.tv_customer_email)
    TextView tvCustomerEmail;
    @BindView(R.id.tv_customer_phone)
    TextView tvCustomerPhone;
    @BindView(R.id.civ_customer)
    CircleImageView civCustomer;
    @BindView(R.id.tv_ticket_details_dropdown)
    TextView tvTicketDetailsDropdown;
    @BindView(R.id.tv_ticket_status)
    TextView tvTicketStatus;
    @BindView(R.id.iv_dropdown_ticket_details)
    ImageView ivDropdownTicketDetails;
    @BindView(R.id.expandable_layout_ticket_details)
    ExpandableLayout elTicketDetails;
    @BindView(R.id.tv_ticket_id)
    TextView tvTicketId;
    @BindView(R.id.civ_ticket_created_by)
    CircleImageView civTicketCreatedBy;
    @BindView(R.id.tv_ticket_created_by)
    TextView tvTicketCreatedBy;
    @BindView(R.id.tv_ticket_created_date)
    TextView tvTicketCreatedDate;
    @BindView(R.id.tv_ticket_created_time)
    TextView tvTicketCreatedTime;
    @BindView(R.id.tv_ticket_title)
    TextView tvTicketTitle;
    @BindView(R.id.tv_ticket_desc)
    TextView tvTicketDesc;
    @BindView(R.id.ll_tags)
    LinearLayout llTags;
    @BindView(R.id.tv_assign_employee)
    TextView tvAssignEmployee;
    @BindView(R.id.ll_status_options)
    LinearLayout llStatusOptions;
    @BindView(R.id.tv_resolve)
    TextView tvResolve;
    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.v_separator)
    View vSeparator;
    @BindView(R.id.rl_selected_status)
    RelativeLayout rlSelectedStatus;
    @BindView(R.id.iv_dropdown_status)
    ImageView ivDropDownStatus;
    @BindView(R.id.tv_assigned_employee_count)
    TextView tvAssignedEmployeeCount;
    @BindView(R.id.tv_reopen)
    TextView tvReopen;
    @BindView(R.id.v_separator1)
    View vSeparator1;
    @BindView(R.id.rl_bot_reply_holder)
    RelativeLayout rlBotReplyHolder;
    @BindView(R.id.tv_status_selected)
    TextView tvStatusSelected;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.btn_reopen)
    MaterialButton btnReopen;


    private boolean expandActivity = true;
    private boolean expandEmployee = true;
    private boolean expandCustomer = true;
    private boolean expandTicketDetails = true;
    private int viewHeight = 0;
    private long ticketId;
    private BottomSheetBehavior sheetBehavior;
    private String status;
    private Animation rotation;


    public TicketTimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent i = Objects.requireNonNull(getActivity()).getIntent();
        ticketId = i.getLongExtra("selected_ticket_id", -1);
        if (ticketId != -1) {
            GlobalUtils.showLog(TAG, "ticket id check:" + ticketId);
            presenter.getCustomerDetails(ticketId);
            presenter.getAssignedEmployees(ticketId);
//            presenter.getTicketTimeline(ticketId);

            setTicketDetails();
        }

        rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        TicketDetailsActivity mActivity = (TicketDetailsActivity) getActivity();
        assert mActivity != null;
        mActivity.setOutSideTouchListener(this);

/*        tvActivityDropDown.setOnClickListener(v -> {
            expandActivity = !expandActivity;
            ivDropdownActivity.startAnimation(rotation);
            if (!expandActivity) {
                ivDropdownActivity.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropup));
            } else {
                ivDropdownActivity.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropdown_toggle));
            }
            elActivities.toggle();
        });*/

        btnReopen.setOnClickListener(v -> reopenTicket());

        tvCustomerDropdown.setOnClickListener(v -> {
            expandCustomer = !expandCustomer;
            ivDropdownCustomer.startAnimation(rotation);
            if (!expandCustomer) {
                ivDropdownCustomer.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropup));
            } else {
                ivDropdownCustomer.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropdown_toggle));
            }
            elCustomer.toggle();
        });

        tvTicketDetailsDropdown.setOnClickListener(v -> {
            expandTicketDetails = !expandTicketDetails;
            ivDropdownTicketDetails.startAnimation(rotation);
            if (!expandTicketDetails) {
                ivDropdownTicketDetails.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropup));
            } else {
                ivDropdownTicketDetails.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropdown_toggle));
            }
            elTicketDetails.toggle();
        });

        tvAssignedEmployeeDropDown.setOnClickListener(v -> {
            expandEmployee = !expandEmployee;
            ivDropdownEmployee.startAnimation(rotation);
            if (!expandEmployee) {
                ivDropdownEmployee.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropup));
            } else {
                ivDropdownEmployee.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropdown_toggle));
            }
            elEmployee.toggle();
        });

       /* btnMarkComplete.setOnClickListener(v -> startActivity(new Intent(getActivity(),
                PaymentSummary.class)));*/
        sheetBehavior = BottomSheetBehavior.from(mBottomSheet);
//        getServiceDoers();


        tvAssignEmployee.setOnClickListener(v -> {
            GlobalUtils.showLog(TAG, "sender ticket Id: " + ticketId);
            Intent intent = new Intent(getActivity(), AssignEmployeeActivity.class);
            intent.putExtra("ticket_id", ticketId);
            startActivityForResult(intent,
                    ASSIGN_EMPLOYEE_REQUEST);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ASSIGN_EMPLOYEE_REQUEST && resultCode == 2) {
            if (data != null) {
                boolean employeeAssigned = data.getBooleanExtra("employee_assigned", false);

                if (employeeAssigned) {
//                    presenter.getTicketTimeline(ticketId);
                    presenter.getAssignedEmployees(ticketId);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean isTicketStarted = Hawk.get(Constants.TICKET_STARTED, false);

        if (isTicketStarted) {
            onTicketStarted();
            Hawk.put(Constants.TICKET_STARTED, false);
        }
    }

    @OnClick(R.id.rl_selected_status)
    public void toggleStatusOptions() {
        if (llStatusOptions.getVisibility() == View.VISIBLE) {
            llStatusOptions.setVisibility(View.GONE);

            ivDropDownStatus.setImageDrawable(getActivity().getResources()
                    .getDrawable(R.drawable.ic_oc_drop_down_blue));
        } else {
            llStatusOptions.setVisibility(View.VISIBLE);

            ivDropDownStatus.setImageDrawable(getActivity().getResources()
                    .getDrawable(R.drawable.ic_drop_up_blue));
        }
    }


    @OnClick(R.id.tv_close)
    public void closeTicket() {
        llStatusOptions.setVisibility(View.GONE);
        showStatusChangeConfirmation("Are you sure you want to close this ticket?", "close");
    }

    @OnClick(R.id.tv_reopen)
    public void reopenTicket() {
        llStatusOptions.setVisibility(View.GONE);
        showStatusChangeConfirmation("Are you sure you want to re-open this ticket?", "reopen");
    }

    @OnClick(R.id.tv_resolve)
    public void resolveTicket() {
        llStatusOptions.setVisibility(View.GONE);
        showStatusChangeConfirmation("Are you sure you want to resolve this ticket?", "resolve");
    }

    public void setTicketDetails() {
        Tickets tickets = TicketRepo.getInstance().getTicketById(ticketId);
        switch (tickets.getTicketStatus()) {
            case "TICKET_CREATED":
                tvTicketStatus.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.ticket_created_text));
                tvTicketStatus.setBackground(getActivity().getResources().getDrawable(R.drawable.created_bg));
                tvTicketStatus.setText("TODO");

                rlSelectedStatus.setVisibility(View.GONE);
                removeScrollviewMargin();
                btnReopen.setVisibility(View.GONE);
                break;

            case "TICKET_STARTED":
                onTicketStarted();
                btnReopen.setVisibility(View.GONE);
                break;

            case "TICKET_RESOLVED":
                tvTicketStatus.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.ticket_resolved_text));
                tvTicketStatus.setBackground(getActivity().getResources().getDrawable(R.drawable.resolved_bg));
                tvTicketStatus.setText("RESOLVED");

                tvStatusSelected.setText("RESOLVED");
                tvResolve.setVisibility(View.GONE);
                vSeparator.setVisibility(View.GONE);

                tvReopen.setVisibility(View.VISIBLE);
                vSeparator1.setVisibility(View.VISIBLE);

                addScrollviewMargin();
                btnReopen.setVisibility(View.GONE);

                rlBotReplyHolder.setVisibility(View.GONE);

                for (int i = 0; i < llAssignedEmployeeTop.getChildCount(); i++) {
                    View assignedEmployeeView = llAssignedEmployeeTop.getChildAt(i);
                    ImageView deleteEmployee = assignedEmployeeView.findViewById(R.id.iv_delete);
                    deleteEmployee.setVisibility(View.GONE);
                }
                tvAssignEmployee.setVisibility(View.GONE);
                break;

            case "TICKET_CLOSED":
                tvTicketStatus.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.ticket_closed_text));
                tvTicketStatus.setBackground(getActivity().getResources().getDrawable(R.drawable.closed_bg));
                tvTicketStatus.setText("CLOSED");

                addScrollviewMargin();
                btnReopen.setVisibility(View.VISIBLE);
                hideActions();
                break;

            case "TICKET_REOPENED":
                btnReopen.setVisibility(View.GONE);
                tvTicketStatus.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.ticket_reopened_text));
                tvTicketStatus.setBackground(getActivity().getResources().getDrawable(R.drawable.reopened_bg));
                tvTicketStatus.setText("REOPENED");

                rlSelectedStatus.setVisibility(View.GONE);
                removeScrollviewMargin();
                break;
        }

        tvTicketId.setText(String.valueOf(tickets.getTicketId()));
        tvTicketCreatedDate.setText(GlobalUtils.getDateAlternate(tickets.getCreatedAt()));
        tvTicketCreatedTime.setText(GlobalUtils.getTime(tickets.getCreatedAt()));
        tvTicketTitle.setText(tickets.getTitle());
        if (tickets.getDescription() == null || tickets.getDescription().isEmpty()) {
            tvTicketDesc.setVisibility(View.GONE);
        } else {
            tvTicketDesc.setText(tickets.getDescription());
        }
        tvTicketCreatedBy.setText(tickets.getCreatedByName());

        String profilePicUrl = tickets.getCreatedByPic();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_profile_icon)
                    .error(R.drawable.ic_profile_icon);

            Glide.with(this).load(profilePicUrl).apply(options).into(civTicketCreatedBy);
        }

        if (!CollectionUtils.isEmpty(tickets.getTagsRealmList())) {
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
                llTags.addView(tvTag);
            }
        } else {
            llTags.setVisibility(View.GONE);
        }


        if (tickets.getTicketType().equalsIgnoreCase(Constants.SUBSCRIBED)) {
            hideActions();
        }
    }

    public void onTicketStarted() {
        tvTicketStatus.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.ticket_started_text));
        tvTicketStatus.setBackground(getActivity().getResources().getDrawable(R.drawable.started_bg));
        tvTicketStatus.setText("STARTED");

        tvReopen.setVisibility(View.GONE);
        vSeparator1.setVisibility(View.GONE);
        tvStatusSelected.setText("STARTED");
        tvStatusSelected.setVisibility(View.VISIBLE);
        rlSelectedStatus.setVisibility(View.VISIBLE);

        tvResolve.setVisibility(View.VISIBLE);
        vSeparator.setVisibility(View.VISIBLE);

        addScrollviewMargin();
    }

    private void removeScrollviewMargin() {
        //remove scroll view bottom margin
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) scrollView
                .getLayoutParams();

        layoutParams.setMargins(0, 0, 0, 0);
        scrollView.setLayoutParams(layoutParams);
    }

    private void addScrollviewMargin() {
        //remove scroll view bottom margin
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) scrollView
                .getLayoutParams();

        layoutParams.setMargins(0, 0, 0, 170);
        scrollView.setLayoutParams(layoutParams);
    }

    private void hideActions() {
        rlSelectedStatus.setVisibility(View.GONE);
        rlBotReplyHolder.setVisibility(View.GONE);

        for (int i = 0; i < llAssignedEmployeeTop.getChildCount(); i++) {
            View assignedEmployeeView = llAssignedEmployeeTop.getChildAt(i);
            ImageView deleteEmployee = assignedEmployeeView.findViewById(R.id.iv_delete);
            deleteEmployee.setVisibility(View.GONE);
        }
        tvAssignEmployee.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    private void setUpProfileBottomSheet(String name, String imageUrl, float rating) {
        TextView ratingNumber = mBottomSheet.findViewById(R.id.tv_rate_number);
        RatingBar ratingBar = mBottomSheet.findViewById(R.id.rating);
        CircleImageView profileImage = mBottomSheet.findViewById(R.id.iv_profile_user_image);
        TextView profileName = mBottomSheet.findViewById(R.id.tv_profile_username);

        profileName.setText(name);
        ratingBar.setRating(rating);
        ratingNumber.setText("(" + rating + ")");
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                .error(R.drawable.ic_empty_profile_holder_icon);

        Glide.with(this)
                .load(imageUrl)
                .apply(options)
                .into(profileImage);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_ticket_timeline;
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
        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(getActivity(),
                Objects.requireNonNull(getActivity())
                        .getWindow().getDecorView().getRootView(), message);
    }

/*    @Override
    public void setRequestValues(String requesterName, String requesterImage, String date,
                                 String location, String requestedDate, String requestedTime,
                                 String serviceStatus, ServiceProvider serviceProvider,
                                 long startedAt, long completedAt, long acceptedAt,
                                 long closedAt) {
        @SuppressLint("InflateParams") View viewRequestedBy = getLayoutInflater()
                .inflate(R.layout.layout_timeline_requested_by, null);
        RelativeLayout rlRequestedBy = viewRequestedBy.findViewById(R.id.rl_requested_by_container);
        TextView tvDate = viewRequestedBy.findViewById(R.id.tv_date);
        CircleImageView civRequesterImage = viewRequestedBy.findViewById(R.id.civ_field1);
        TextView tvRequesterName = viewRequestedBy.findViewById(R.id.tv_field1);
        TextView tvLocation = viewRequestedBy.findViewById(R.id.tv_field2);
        LinearLayout llLocationField = viewRequestedBy.findViewById(R.id.ll_field2);
        TextView tvRequestedDate = viewRequestedBy.findViewById(R.id.tv_field3);
        View line = viewRequestedBy.findViewById(R.id.view_line);
        LinearLayout llDateTimeContainer = viewRequestedBy.findViewById(R.id.ll_datetime_container);

        setElapsedTime(startedAt, closedAt);
        if (date != null && !date.isEmpty()) {
            tvDate.setText(date);
            tvDate.setVisibility(View.VISIBLE);
        }

        tvRequesterName.setText(requesterName);
        if (requesterImage != null && !requesterImage.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_profile_icon)
                    .error(R.drawable.ic_profile_icon);

            Glide.with(this).load(requesterImage).apply(options).into(civRequesterImage);
        }

        status = serviceStatus;
        GlobalUtils.showLog(TAG, "location check: " + location);
        if (location != null && !location.isEmpty()) tvLocation.setText(location);
        else llLocationField.setVisibility(View.GONE);
//        tvRequestedDate.setText(requestedDate + " - " + requestedTime);
        if (requestedDate != null && !requestedDate.isEmpty()) {
            tvRequestedDate.setText(requestedDate);
            llDateTimeContainer.setVisibility(View.VISIBLE);
        } else {
            llDateTimeContainer.setVisibility(View.GONE);
        }
        llActivities.addView(viewRequestedBy);

        GlobalUtils.showLog(TAG, "service status; " + serviceStatus);
        GlobalUtils.showLog(TAG, "service provider; " + serviceProvider.toString());

        if (acceptedAt != 0) {
            inflateAcceptedLayout(serviceProvider,
                    GlobalUtils.getDateTimeline(acceptedAt));
        }

        rlRequestedBy.post(() -> {
            GlobalUtils.showLog(TAG, "layout height requested by" + rlRequestedBy.getHeight());
            LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(3,
                    rlRequestedBy.getHeight() - getResources().getDimensionPixelOffset(R.dimen.dimen_10x2));
            layoutParam.gravity = Gravity.CENTER_HORIZONTAL;
            line.setLayoutParams(layoutParam);
        });


        if (serviceStatus.equalsIgnoreCase(
                OrderServiceProto.ServiceOrderState.STARTED_SERVICE_ORDER.name()) ||
                serviceStatus.equalsIgnoreCase(
                        OrderServiceProto.ServiceOrderState.COMPLETED_SERVICE_ORDER.name())) {
            btnMarkComplete.setVisibility(View.VISIBLE);
        } else {
            btnMarkComplete.setVisibility(View.GONE);
        }

    }*/

/*    private void setElapsedTime(long startedAt, long closedAt) {
        long endTime;
        if (startedAt != 0) {
            if (closedAt != 0) {
                endTime = closedAt;
            } else {
                endTime = System.currentTimeMillis();
            }
            com.treeleaf.anydone.serviceprovider.utils.DateUtils dateUtils =
                    new com.treeleaf.anydone.serviceprovider.utils.DateUtils();
            String elapsed = dateUtils.printDifference(startedAt, endTime);
            tvElapsedTime.setText(elapsed);
        } else {
            tvElapsedTime.setVisibility(View.GONE);
        }
    }*/

    private void inflateAcceptedLayout(ServiceProvider serviceProvider, String acceptedDate) {
        @SuppressLint("InflateParams") View viewAcceptedBy = getLayoutInflater()
                .inflate(R.layout.layout_timeline_accepted_by, null);
        RelativeLayout rlAcceptedByContainer = viewAcceptedBy
                .findViewById(R.id.rl_accepted_by_container);
        TextView tvAcceptedDate = viewAcceptedBy.findViewById(R.id.tv_date);
        CircleImageView civAcceptedBy = viewAcceptedBy.findViewById(R.id.civ_accepted_by);
        TextView tvAcceptedBy = viewAcceptedBy.findViewById(R.id.tv_accepted_by);
        View line = viewAcceptedBy.findViewById(R.id.view_line);

        tvAcceptedDate.setText(acceptedDate);
        tvAcceptedBy.setText(serviceProvider.getFullName());
        if (serviceProvider.getProfilePic() != null && !serviceProvider.getProfilePic().isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_profile_icon)
                    .error(R.drawable.ic_profile_icon);

            Glide.with(this).load(serviceProvider.getProfilePic()).apply(options)
                    .into(civAcceptedBy);
        }

        rlAcceptedByContainer.post(() -> {
            viewHeight = rlAcceptedByContainer.getHeight();
            GlobalUtils.showLog(TAG, "layout height post" + viewHeight);
            LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(3,
                    viewHeight - getResources().getDimensionPixelOffset(R.dimen.dimen_10x2));
            layoutParam.gravity = Gravity.CENTER_HORIZONTAL;
            line.setLayoutParams(layoutParam);
        });

//        llActivities.addView(viewAcceptedBy);
        civAcceptedBy.setOnClickListener(v -> {
            setUpProfileBottomSheet(serviceProvider.getFullName(),
                    serviceProvider.getProfilePic(),
                    serviceProvider.getAvgRating());
            toggleBottomSheet();
        });

        tvAcceptedBy.setOnClickListener(v -> {
            setUpProfileBottomSheet(serviceProvider.getFullName(),
                    serviceProvider.getProfilePic(),
                    serviceProvider.getAvgRating());
            toggleBottomSheet();
        });
    }


    public void getServiceDoers() {
        ServiceOrderEmployee assignedEmployee = ServiceOrderEmployeeRepo.getInstance()
                .getServiceOrderEmployeeById(ticketId);
        if (assignedEmployee != null && !CollectionUtils.isEmpty
                (assignedEmployee.getServiceDoerList())) {
            @SuppressLint("InflateParams") View viewTaskAssigned = getLayoutInflater()
                    .inflate(R.layout.layout_timeline_task_assigned, null);
            RelativeLayout rlTaskAssignedContainer = viewTaskAssigned
                    .findViewById(R.id.rl_task_assigned);
            TextView tvAssignedDate = viewTaskAssigned.findViewById(R.id.tv_assigend_date);
            View line = viewTaskAssigned.findViewById(R.id.view_line);
            LinearLayout llAssignedEmployees = viewTaskAssigned
                    .findViewById(R.id.ll_assigned_employees);

            tvAssignedDate.setText(GlobalUtils.getDateTimeline(assignedEmployee.getCreatedAt()));

            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

         /*   inflateAssignedEmployeeLayout(assignedEmployee, llAssignedEmployees);
            inflateAssignedEmployeeLayout(assignedEmployee, llAssignedEmployeeTop);*/

            rlTaskAssignedContainer.post(() -> {
                GlobalUtils.showLog(TAG, "layout height task assigned" +
                        rlTaskAssignedContainer.getHeight());
                LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(3,
                        rlTaskAssignedContainer.getHeight() - getResources().getDimensionPixelOffset(R.dimen.dimen_10x2));
                layoutParam.gravity = Gravity.CENTER_HORIZONTAL;
                line.setLayoutParams(layoutParam);
            });

//            llActivities.addView(viewTaskAssigned);
            llAssignedEmployee.setVisibility(View.VISIBLE);
        }

        if (status.equalsIgnoreCase(OrderServiceProto.ServiceOrderState
                .CANCELLED_SERVICE_ORDER.name())) {
            @SuppressLint("InflateParams") View viewCancelled = getLayoutInflater()
                    .inflate(R.layout.layout_timeline_cancelled, null);
//            llActivities.addView(viewCancelled);
        } else if (status.equalsIgnoreCase(OrderServiceProto.ServiceOrderState
                .COMPLETED_SERVICE_ORDER.name())) {
            @SuppressLint("InflateParams") View viewCompleted = getLayoutInflater()
                    .inflate(R.layout.layout_timeline_completed, null);
//            llActivities.addView(viewCompleted);
        } else if (status.equalsIgnoreCase(OrderServiceProto.ServiceOrderState
                .CLOSED_SERVICE_ORDER.name())) {
            @SuppressLint("InflateParams") View viewClosed = getLayoutInflater()
                    .inflate(R.layout.layout_timeline_closed, null);
//            llActivities.addView(viewClosed);
        } else if (status.equalsIgnoreCase(OrderServiceProto.ServiceOrderState
                .STARTED_SERVICE_ORDER.name())) {
            @SuppressLint("InflateParams") View viewStarted = getLayoutInflater()
                    .inflate(R.layout.layout_timeline_started, null);
//            llActivities.addView(viewStarted);
        } else if (status.equalsIgnoreCase(OrderServiceProto.ServiceOrderState
                .ACCEPTED_SERVICE_ORDER.name())) {
            @SuppressLint("InflateParams") View viewAccepted = getLayoutInflater()
                    .inflate(R.layout.layout_timeline_accepted, null);
//            llActivities.addView(viewAccepted);
        } else if (status.equalsIgnoreCase(OrderServiceProto.ServiceOrderState
                .PENDING_SERVICE_ORDER.name())) {
            @SuppressLint("InflateParams") View viewInProgress = getLayoutInflater()
                    .inflate(R.layout.layout_timeline_in_progress, null);
//            llActivities.addView(viewInProgress);
        }
    }

    private void inflateAssignedEmployeeLayout(RealmList<Employee> assignedEmployee,
                                               LinearLayout parent) {
        parent.removeAllViews();
        GlobalUtils.showLog(TAG, "assigned emp size check: " + assignedEmployee.size());
        tvAssignedEmployeeCount.setText(String.valueOf(assignedEmployee.size()));
        for (Employee serviceDoer : assignedEmployee
        ) {
            @SuppressLint("InflateParams") View viewAssignedEmployee = getLayoutInflater()
                    .inflate(R.layout.layout_task_assigned, null, false);
            TextView employeeName = viewAssignedEmployee.findViewById(R.id.tv_field);
            CircleImageView employeePic = viewAssignedEmployee.findViewById(R.id.civ_field);
            ImageView deleteEmployee = viewAssignedEmployee.findViewById(R.id.iv_delete);

            deleteEmployee.setOnClickListener(v -> {
                GlobalUtils.showLog(TAG, "employee id: " + serviceDoer.getEmployeeId());
                int pos = parent.indexOfChild(v);
                showDeleteDialog(serviceDoer.getEmployeeId());
            });

            employeeName.setText(serviceDoer.getName());
            if (serviceDoer.getEmployeeImageUrl() != null && !serviceDoer.getEmployeeImageUrl().isEmpty()) {
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_profile_icon)
                        .error(R.drawable.ic_profile_icon);

                Glide.with(this).load(serviceDoer.getEmployeeImageUrl())
                        .apply(options).into(employeePic);
            }

            parent.addView(viewAssignedEmployee);
     /*       employeeName.setOnClickListener(v -> {
                setUpProfileBottomSheet(serviceDoer.getName(),
                        serviceDoer.getEmployeeImageUrl(), serviceDoer.get());
                toggleBottomSheet();
            });*/

      /*      employeePic.setOnClickListener(v -> {
                setUpProfileBottomSheet(serviceDoer.getFullName(),
                        serviceDoer.getProfilePic(), serviceDoer.getAvgRating());
                toggleBottomSheet();
            });*/

        }
    }


    private void showDeleteDialog(String employeeId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to delete?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> {
                    presenter.unAssignEmployee(ticketId, employeeId);
                    dialog.dismiss();
                });

        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> dialog.dismiss());


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                    .getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                    .getColor(R.color.colorPrimary));

        });
        alert11.show();
    }

    private void showStatusChangeConfirmation(String title, String type) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(title);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    switch (type) {
                        case "resolve":
                            presenter.resolveTicket(ticketId);
                            break;

                        case "close":
                            presenter.closeTicket(ticketId);
                            break;

                        case "reopen":
                            presenter.reopenTicket(ticketId);
                            break;
                    }
                    dialog.dismiss();
                });

        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> dialog.dismiss());


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                    .getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                    .getColor(R.color.colorPrimary));

        });
        alert11.show();
    }


    /**
     * manually opening / closing bottom sheet on button click
     */
    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }


    @Override
    public void onOutsideClick(MotionEvent event) {
        GlobalUtils.showLog(TAG, "on outside click second");
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            Rect outRect = new Rect();
            mBottomSheet.getGlobalVisibleRect(outRect);

            if (!outRect.contains((int) event.getRawX(), (int) event.getRawY()))
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void getTicketTimelineSuccess(RealmList<Employee> assignedEmployeeList) {
        GlobalUtils.showLog(TAG, "assinged emp count check: " + assignedEmployeeList.size());
        if (CollectionUtils.isEmpty(assignedEmployeeList)) {
            llAssignedEmployeeTop.setVisibility(View.GONE);
            llAssignedEmployee.setVisibility(View.GONE);
        } else {
            llAssignedEmployee.setVisibility(View.VISIBLE);
            llAssignedEmployeeTop.setVisibility(View.VISIBLE);
            GlobalUtils.showLog(TAG, "get timeline ok");
            inflateAssignedEmployeeLayout(assignedEmployeeList, llAssignedEmployeeTop);
        }
    }

    @Override
    public void geTicketTimelineFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(),
                Objects.requireNonNull(getActivity())
                        .getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void setCustomerDetails(Customer customerDetails) {
        tvCustomerName.setText(customerDetails.getFullName());
        if (customerDetails.getPhone() == null || customerDetails.getPhone().isEmpty()) {
            llCustomerPhone.setVisibility(View.GONE);
        } else {
            tvCustomerPhone.setText(customerDetails.getPhone());
        }

        if (customerDetails.getEmail() == null || customerDetails.getEmail().isEmpty()) {
            llCustomerEmail.setVisibility(View.GONE);
        } else {
            tvCustomerEmail.setText(customerDetails.getEmail());
        }

        String profilePicUrl = customerDetails.getProfilePic();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_profile_icon)
                    .error(R.drawable.ic_profile_icon);

            Glide.with(this).load(profilePicUrl).apply(options).into(civCustomer);
        }
    }

    @Override
    public void setAssignedEmployee(RealmList<Employee> assignedEmployee) {
        GlobalUtils.showLog(TAG, "assinged emp count check: " + assignedEmployee.size());
        if (CollectionUtils.isEmpty(assignedEmployee)) {
            llAssignedEmployeeTop.setVisibility(View.GONE);
            llAssignedEmployee.setVisibility(View.GONE);
        } else {
            llAssignedEmployee.setVisibility(View.VISIBLE);
            llAssignedEmployeeTop.setVisibility(View.VISIBLE);
            GlobalUtils.showLog(TAG, "get timeline ok");
            inflateAssignedEmployeeLayout(assignedEmployee, llAssignedEmployeeTop);
        }
    }

    @Override
    public void onEmployeeUnAssignSuccess(String empId) {
        presenter.getAssignedEmployees(ticketId);
    }

    @Override
    public void onEmployeeUnAssignFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(),
                Objects.requireNonNull(getActivity())
                        .getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onTicketCloseSuccess() {
        TicketRepo.getInstance().changeTicketStatusToClosed(ticketId);
        setTicketDetails();
        Hawk.put(Constants.REFETCH_TICKET, true);
    }

    @Override
    public void onTicketCloseFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(),
                Objects.requireNonNull(getActivity())
                        .getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onTicketReopenSuccess() {
        TicketRepo.getInstance().changeTicketStatusToReopened(ticketId);
        setTicketDetails();
        Hawk.put(Constants.REFETCH_TICKET, true);
    }

    @Override
    public void onTicketReopenFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(),
                Objects.requireNonNull(getActivity())
                        .getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onTicketResolveSuccess() {
        TicketRepo.getInstance().changeTicketStatusToResolved(ticketId);
        setTicketDetails();
        Hawk.put(Constants.REFETCH_TICKET, true);
    }

    @Override
    public void onTicketResolveFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(),
                Objects.requireNonNull(getActivity())
                        .getWindow().getDecorView().getRootView(), msg);
    }

}
