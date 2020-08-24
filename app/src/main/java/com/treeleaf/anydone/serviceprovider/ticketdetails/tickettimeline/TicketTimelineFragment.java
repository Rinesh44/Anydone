package com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.treeleaf.anydone.entities.OrderServiceProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.assignemployee.AssignEmployeeActivity;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.ServiceOrderEmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.Objects;

import butterknife.BindView;
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
    @BindView(R.id.tv_ticket_title)
    TextView tvTicketTitle;
    @BindView(R.id.tv_ticket_desc)
    TextView tvTicketDesc;
    @BindView(R.id.ll_tags)
    LinearLayout llTags;
    @BindView(R.id.tv_assign_employee)
    TextView tvAssignEmployee;

    private boolean expandActivity = true;
    private boolean expandEmployee = true;
    private boolean expandCustomer = true;
    private boolean expandTicketDetails = true;
    private int viewHeight = 0;
    private long ticketId;
    private BottomSheetBehavior sheetBehavior;
    private String status;
    private ProgressDialog progress;


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
//            presenter.getAssignedEmployees(ticketId);
            presenter.getTicketTimeline(ticketId);

            Tickets tickets = TicketRepo.getInstance().getTicketById(ticketId);
            setTicketDetails(tickets);
        }

        Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
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

        Intent intent = new Intent(getActivity(), AssignEmployeeActivity.class);
        i.putExtra("ticket_id", ticketId);
        tvAssignEmployee.setOnClickListener(v -> startActivityForResult(intent,
                ASSIGN_EMPLOYEE_REQUEST));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ASSIGN_EMPLOYEE_REQUEST && resultCode == 2) {
            if (data != null) {
                boolean employeeAssigned = data.getBooleanExtra("employee_assigned", false);

                if (employeeAssigned) {
                    presenter.getTicketTimeline(ticketId);
                }
            }
        }
    }

    private void setTicketDetails(Tickets tickets) {
        switch (tickets.getTicketStatus()) {
            case "TICKET_CREATED":
                tvTicketStatus.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.ticket_created_text));
                tvTicketStatus.setBackground(getActivity().getResources().getDrawable(R.drawable.created_bg));
                tvTicketStatus.setText("TODO");
                break;

            case "TICKET_STARTED":
                tvTicketStatus.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.ticket_started_text));
                tvTicketStatus.setBackground(getActivity().getResources().getDrawable(R.drawable.started_bg));
                tvTicketStatus.setText("STARTED");
                break;

            case "TICKET_RESOLVED":
                tvTicketStatus.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.ticket_resolved_text));
                tvTicketStatus.setBackground(getActivity().getResources().getDrawable(R.drawable.resolved_bg));
                tvTicketStatus.setText("RESOLVED");
                break;

            case "TICKET_CLOSED":
                tvTicketStatus.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.ticket_closed_text));
                tvTicketStatus.setBackground(getActivity().getResources().getDrawable(R.drawable.closed_bg));
                tvTicketStatus.setText("CLOSED");
                break;

            case "TICKET_REOPENED":
                tvTicketStatus.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.ticket_reopened_text));
                tvTicketStatus.setBackground(getActivity().getResources().getDrawable(R.drawable.reopened_bg));
                tvTicketStatus.setText("REOPENED");
                break;
        }

        tvTicketId.setText(String.valueOf(tickets.getTicketId()));
        tvTicketCreatedDate.setText(GlobalUtils.getDateAlternate(tickets.getCreatedAt()));
        tvTicketTitle.setText(tickets.getTitle());
        tvTicketDesc.setText(tickets.getDescription());
        tvTicketCreatedBy.setText(tickets.getCreatedByName());

        String profilePicUrl = tickets.getCreatedByPic();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_profile_icon)
                    .error(R.drawable.ic_profile_icon);

            Glide.with(this).load(profilePicUrl).apply(options).into(civTicketCreatedBy);
        }

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
        progress = ProgressDialog.show(getActivity(), null, message, true);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.dismiss();
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
        GlobalUtils.showLog(TAG, "assigned emp size check: " + assignedEmployee.size());
        for (Employee serviceDoer : assignedEmployee
        ) {
            @SuppressLint("InflateParams") View viewAssignedEmployee = getLayoutInflater()
                    .inflate(R.layout.layout_task_assigned, null, false);
            TextView employeeName = viewAssignedEmployee.findViewById(R.id.tv_field);
            CircleImageView employeePic = viewAssignedEmployee.findViewById(R.id.civ_field);

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
        if (CollectionUtils.isEmpty(assignedEmployee)) {
            llAssignedEmployeeTop.setVisibility(View.GONE);
        } else {
            inflateAssignedEmployeeLayout(assignedEmployee, llAssignedEmployeeTop);
        }
    }
}
