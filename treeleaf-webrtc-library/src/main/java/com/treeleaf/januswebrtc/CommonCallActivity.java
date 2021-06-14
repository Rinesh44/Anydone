package com.treeleaf.januswebrtc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.treeleaf.januswebrtc.tickets.AttachmentListener;
import com.treeleaf.januswebrtc.tickets.ViewPagerAdapter;
import com.treeleaf.januswebrtc.tickets.fragments.TicketsActivityLogFragment;
import com.treeleaf.januswebrtc.tickets.fragments.TicketsAttachmentFragment;
import com.treeleaf.januswebrtc.tickets.fragments.TicketsConversationFragment;
import com.treeleaf.januswebrtc.tickets.model.Attachment;
import com.treeleaf.januswebrtc.tickets.model.Tickets;
import com.treeleaf.januswebrtc.utils.NonSwipeableViewPager;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.treeleaf.januswebrtc.ClientActivity.mhostActivityCallback;


public abstract class CommonCallActivity extends PermissionHandlerActivity implements AttachmentListener {


    private ImageView imageTicketDetail, ivTicketDeadline;


    private BottomSheetDialog ticketDetailBottomSheet;
    private NonSwipeableViewPager viewPagerTicket;
    private TabLayout tabLayoutTicket;
    private Tickets tickets;
    private TextView tvTicketIdValue, tvPriority, tvTicketType, tvTicketCreatedDuration,
            tvTicketSummary, tvTicketDeadline, tvTicketAssignedEmployee, tvTicketStatus, tvTicketDesc;
    private CircleImageView civTicketAssignedEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        imageTicketDetail = findViewById(R.id.image_ticket_detail);
        imageTicketDetail.setOnClickListener(ticketDetailClickListener);
        setUpTicketDetailBottomSheet();
    }

    @Override
    public List<Attachment> getAttachments() {
        if (mhostActivityCallback != null)
            return mhostActivityCallback.getAttachments();
        return null;
    }

    private void setUpTicketDetailBottomSheet() {
        ticketDetailBottomSheet = new BottomSheetDialog(Objects.requireNonNull(this));
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.bottom_sheet_ticket_detail, null);
        ticketDetailBottomSheet.setContentView(view);

        tabLayoutTicket = view.findViewById(R.id.tl_ticket);
        viewPagerTicket = view.findViewById(R.id.vp_ticket);

        tvTicketIdValue = view.findViewById(R.id.tv_ticket_id_value);
        tvPriority = view.findViewById(R.id.tv_priority);
        tvTicketType = view.findViewById(R.id.tv_ticket_type);
        tvTicketCreatedDuration = view.findViewById(R.id.tv_ticket_created_duration);
        tvTicketSummary = view.findViewById(R.id.tv_ticket_summary);
        tvTicketDeadline = view.findViewById(R.id.tv_ticket_deadline);
        ivTicketDeadline = view.findViewById(R.id.iv_deadline_icon);
        civTicketAssignedEmployee = view.findViewById(R.id.civ_assigned_employee);
        tvTicketAssignedEmployee = view.findViewById(R.id.tv_assigned_employee);
        tvTicketStatus = view.findViewById(R.id.tv_ticket_status);
        tvTicketDesc = view.findViewById(R.id.tv_ticket_desc);


        setUpViewPager();
        tabLayoutTicket.setupWithViewPager(viewPagerTicket);


    }

    private void setUpViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TicketsConversationFragment(), "Comments");
        adapter.addFragment(new TicketsAttachmentFragment(), "Attachment");
        adapter.addFragment(new TicketsActivityLogFragment(), "Activity Log");
        viewPagerTicket.setAdapter(adapter);
    }

    protected abstract int getLayout();

    View.OnClickListener ticketDetailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mhostActivityCallback != null) {


                //should do this inside backgroud thread
                tickets = mhostActivityCallback.getTicket();

                populateTicketDetails();
                ticketDetailBottomSheet.show();
            }
        }
    };

    private void populateTicketDetails() {
        tvTicketIdValue.setText("#" + tickets.getTicketIndex());
        setPriority(tickets.getPriority());
        tvTicketCreatedDuration.setText(tickets.getRelativeTime());
        setTicketStatus(tickets.getTicketStatus());
        tvTicketSummary.setText(tickets.getTitle());
        tvTicketDesc.setText(tickets.getDescription());
        String employeeId = tickets.getAssignedEmployee().getEmployeeId();
        if (employeeId != null && !employeeId.isEmpty())
            tvTicketAssignedEmployee.setText(tickets.getAssignedEmployee().getName());


        String employeeImage = tickets.getAssignedEmployee().getEmployeeImageUrl();
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                .error(R.drawable.ic_empty_profile_holder_icon);

        Glide.with(Objects.requireNonNull(this))
                .load(employeeImage)
                .apply(options)
                .into(civTicketAssignedEmployee);


        showEstimatedTime();

    }

    private void showEstimatedTime() {
        if (tickets.getEstimatedTime().isEmpty()) {
            tvTicketDeadline.setVisibility(View.GONE);
            ivTicketDeadline.setVisibility(View.GONE);
        } else {
            tvTicketDeadline.setVisibility(View.VISIBLE);
            ivTicketDeadline.setVisibility(View.VISIBLE);
            ivTicketDeadline.setImageDrawable(getResources().getDrawable(R.drawable.ic_date_icon));
            if (tickets.getEstimatedTimeStamp() != 0) {
                setRemainingTime();
            } else {
                tvTicketDeadline.setText(tickets.getEstimatedTime());
            }
        }
    }

    private void setRemainingTime() {
        StringBuilder estTime = new StringBuilder(tickets.getEstimatedTime());
        if (tickets.getEstimatedTimeStamp() > System.currentTimeMillis()) {
            String remainingTime = DateUtils.getRelativeTimeSpanString
                    (tickets.getEstimatedTimeStamp()).toString();
            boolean estTimeInDate = false;
            if (remainingTime.contains("In")) {
                remainingTime = remainingTime.replace("In", "");
            } else {
                estTimeInDate = true;
            }

//            estTime.append(" (");
            estTime.append(remainingTime);
//            if (!estTimeInDate) estTime.append(" remaining");
//            estTime.append(" )");

        } else if (tickets.getEstimatedTimeStamp() == 0) {

        } else {
            /*estTime.append(" (");
            estTime.append("time exceeded");
            estTime.append(")");*/
            ivTicketDeadline.setImageDrawable(getResources().getDrawable(R.drawable.ic_date_deadline));
        }
        tvTicketDeadline.setText(estTime);
    }

    private void setPriority(int priority) {
        switch (priority) {
            case 1:
                tvPriority.setText("Lowest");
                tvPriority.setCompoundDrawablesWithIntrinsicBounds
                        (R.drawable.ic_lowest_small, 0, 0, 0);
                break;

            case 2:
                tvPriority.setText("Low");
                tvPriority.setCompoundDrawablesWithIntrinsicBounds
                        (R.drawable.ic_low_small, 0, 0, 0);
                break;

            case 4:
                tvPriority.setText("High");
                tvPriority.setCompoundDrawablesWithIntrinsicBounds
                        (R.drawable.ic_high_small, 0, 0, 0);
                break;

            case 5:
                tvPriority.setText("Highest");
                tvPriority.setCompoundDrawablesWithIntrinsicBounds
                        (R.drawable.ic_highest_small, 0, 0, 0);
                break;

            default:
                tvPriority.setText("Medium");
                tvPriority.setCompoundDrawablesWithIntrinsicBounds
                        (R.drawable.ic_medium_small, 0, 0, 0);
                break;
        }
    }

    private void setTicketStatus(String ticketStatus) {
        switch (ticketStatus) {
            case "TICKET_CREATED":
                tvTicketStatus.setTextColor
                        (getResources().getColor(R.color.ticket_created_text));
                tvTicketStatus.setBackground
                        (getResources().getDrawable(R.drawable.created_bg));
                tvTicketStatus.setText("TODO");
                break;

            case "TICKET_STARTED":
                tvTicketStatus.setTextColor(getResources().getColor
                        (R.color.ticket_started_text));
                tvTicketStatus.setBackground(getResources().getDrawable
                        (R.drawable.started_bg));
                tvTicketStatus.setText("STARTED");
                break;

            case "TICKET_RESOLVED":
                tvTicketStatus.setTextColor(getResources().getColor
                        (R.color.ticket_resolved_text));
                tvTicketStatus.setBackground(getResources().getDrawable
                        (R.drawable.resolved_bg));
                tvTicketStatus.setText("RESOLVED");
                break;


            case "TICKET_REOPENED":
                tvTicketStatus.setTextColor(getResources().getColor
                        (R.color.ticket_reopened_text));
                tvTicketStatus.setBackground(getResources().getDrawable
                        (R.drawable.reopened_bg));
                tvTicketStatus.setText("REOPENED");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPermissionGranted() {

    }

}
