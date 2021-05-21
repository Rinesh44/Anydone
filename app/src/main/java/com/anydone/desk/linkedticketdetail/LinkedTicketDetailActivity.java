package com.anydone.desk.linkedticketdetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.util.CollectionUtils;
import com.anydone.desk.R;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Customer;
import com.anydone.desk.realm.model.Label;
import com.anydone.desk.realm.model.Service;
import com.anydone.desk.realm.model.Tags;
import com.anydone.desk.realm.model.Tickets;
import com.anydone.desk.realm.repo.AvailableServicesRepo;
import com.anydone.desk.realm.repo.TicketRepo;
import com.anydone.desk.utils.GlobalUtils;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmList;

public class LinkedTicketDetailActivity extends AppCompatActivity {
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
    @BindView(R.id.tv_ticket_status)
    TextView tvTicketStatus;
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
    @BindView(R.id.iv_service)
    ImageView ivService;
    @BindView(R.id.tv_service)
    TextView tvService;
    @BindView(R.id.iv_priority)
    ImageView ivPriority;
    @BindView(R.id.tv_priority)
    TextView tvPriority;
    @BindView(R.id.tv_assigned_employee)
    TextView tvAssignedEmployee;
    @BindView(R.id.civ_assigned_employee)
    CircleImageView civAssignedEmployee;
    @BindView(R.id.ll_contributors)
    LinearLayout llContributors;
    @BindView(R.id.tv_contributors_dropdown)
    TextView tvContributorDropDown;
    @BindView(R.id.iv_dropdown_contributor)
    ImageView ivContributorDropDown;
    @BindView(R.id.expandable_layout_contributor)
    ExpandableLayout elContributor;
    @BindView(R.id.ll_contributor_list)
    LinearLayout llContributorList;
    @BindView(R.id.tv_ticket_type_value)
    TextView tvTicketType;
    @BindView(R.id.ll_label_holder)
    LinearLayout llLabelHolder;
    @BindView(R.id.ll_labels)
    LinearLayout llLabels;
    @BindView(R.id.tv_estimated_time_value)
    TextView tvEstimatedTimeValue;
    @BindView(R.id.tv_estimated_time)
    TextView tvEstimatedTime;

    private boolean expandCustomer = true;
    private boolean expandEmployee = true;
    private Animation rotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linked_ticket_detail);

        ButterKnife.bind(this);
        setToolbar();

        Intent i = getIntent();
        long ticketId = i.getLongExtra("selected_ticket_id", -1);
        if (ticketId != -1) setTicketDetails(ticketId);


        rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        tvCustomerDropdown.setOnClickListener(v -> {
            expandCustomer = !expandCustomer;
            ivDropdownCustomer.startAnimation(rotation);
            if (expandCustomer) {
                ivDropdownCustomer.setImageDrawable(getResources()
                        .getDrawable(R.drawable.ic_dropup));
            } else {
                ivDropdownCustomer.setImageDrawable(getResources()
                        .getDrawable(R.drawable.ic_dropdown_toggle));
            }
            elCustomer.toggle();
        });

        tvContributorDropDown.setOnClickListener(v -> {
            expandEmployee = !expandEmployee;
            ivContributorDropDown.startAnimation(rotation);
            if (expandEmployee) {
                ivContributorDropDown.setImageDrawable(getResources()
                        .getDrawable(R.drawable.ic_dropup));
            } else {
                ivContributorDropDown.setImageDrawable(getResources()
                        .getDrawable(R.drawable.ic_dropdown_toggle));
            }
            elContributor.toggle();
        });

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

        tvTicketId.setText(String.valueOf(tickets.getTicketId()));
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
            tvTicketDesc.setText("N/A");
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

        addTeamsToLayout(tickets);
        setLabels(tickets);
        setPriority(tickets.getPriority());
        setService(tickets.getServiceId());

        setAssignedEmployee(tickets.getAssignedEmployee());
        setCustomerDetails(tickets.getCustomer());
        setContributors(tickets);

    }

    private void setContributors(Tickets tickets) {
        RealmList<AssignEmployee> contributorList = tickets.getContributorList();
        if (!CollectionUtils.isEmpty(contributorList)) {
            inflateContributorLayout(contributorList, llContributorList);
        } else {
            TextView textView = new TextView(this);
            textView.setText("N/A");
            textView.setTextColor(getResources().getColor(R.color.black));
            llContributorList.addView(textView);
        }
    }

    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Ticket Details");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0,
                str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
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
                        .placeholder(R.drawable.ic_profile_icon)
                        .error(R.drawable.ic_profile_icon);

                Glide.with(this).load(contributor.getEmployeeImageUrl())
                        .apply(options).into(employeePic);
            }

            parent.addView(viewAssignedEmployee);
        }
    }

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
            TextView textView = new TextView(this);
            textView.setText("N/A");
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
            TextView textView = new TextView(this);
            textView.setText("N/A");
            textView.setTextColor(getResources().getColor(R.color.black));
            llLabelHolder.addView(textView);
        }
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

    public void setAssignedEmployee(AssignEmployee assignedEmployee) {
        if (!assignedEmployee.getName().isEmpty()) {
            tvAssignedEmployee.setText(assignedEmployee.getName());
        } else {
            tvAssignedEmployee.setText(R.string.unassigned);
        }

        String employeeImage = assignedEmployee.getEmployeeImageUrl();
        if (employeeImage != null && !employeeImage.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_profile_icon)
                    .error(R.drawable.ic_profile_icon);

            Glide.with(this)
                    .load(employeeImage)
                    .apply(options)
                    .into(civAssignedEmployee);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}