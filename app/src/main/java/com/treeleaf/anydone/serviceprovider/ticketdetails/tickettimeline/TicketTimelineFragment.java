package com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.DependentTicketSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.EmployeeSearchAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.SearchLabelAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.SearchTeamAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.SearchTicketTypeAdapter;
import com.treeleaf.anydone.serviceprovider.addcontributor.AddContributorActivity;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.editticket.EditTicketActivity;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.DependentTicket;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Label;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketCategory;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.AssignEmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.AvailableServicesRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.DependentTicketRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.EmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.LabelRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TagRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketCategoryRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation.OnStatusChangeListener;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmList;

public class TicketTimelineFragment extends BaseFragment<TicketTimelinePresenterImpl> implements
        TicketTimelineContract.TicketTimelineView
        /*      TicketDetailsActivity.OnOutsideClickListener*/ {
    private static final String TAG = "TicketTimelineFragment";
    public static final int ADD_CONTRIBUTOR = 4560;
    public static final int EDIT_RESULT = 9980;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
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
    @BindView(R.id.tv_team)
    TextView tvTeam;
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
    @BindView(R.id.iv_service)
    ImageView ivService;
    @BindView(R.id.tv_service)
    TextView tvService;
    @BindView(R.id.iv_priority)
    ImageView ivPriority;
    @BindView(R.id.tv_priority)
    TextView tvPriority;
    @BindView(R.id.iv_assign_employee)
    ImageView ivAssignEmployee;
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
    @BindView(R.id.tv_contributor)
    TextView tvContributor;
    @BindView(R.id.switch_bot_reply)
    Switch botReply;
    @BindView(R.id.tv_ticket_type_value)
    TextView tvTicketType;
    @BindView(R.id.tv_ticket_type)
    TextView tvTicketTypeTitle;
    @BindView(R.id.ll_label_holder)
    LinearLayout llLabelHolder;
    @BindView(R.id.ll_labels)
    LinearLayout llLabels;
    @BindView(R.id.tv_estimated_time_value)
    TextView tvEstimatedTimeValue;
    @BindView(R.id.tv_estimated_time)
    TextView tvEstimatedTime;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.tv_description)
    TextView tvDescTitle;
    @BindView(R.id.tv_add_label)
    TextView tvAddLabel;
    @BindView(R.id.tv_add_team)
    TextView tvAddTeam;
    @BindView(R.id.tv_add_desc)
    TextView tvAddDesc;
    @BindView(R.id.tv_add_est_time)
    TextView tvAddEstTime;
    @BindView(R.id.ll_status_holder)
    LinearLayout llStatusHolder;
    @BindView(R.id.btn_start_task)
    MaterialButton btnStartTask;
    @BindView(R.id.rl_depends_on)
    RelativeLayout rlDependsOn;
    @BindView(R.id.tv_dependent_ticket_id)
    TextView tvDependentTicketId;
    @BindView(R.id.tv_dependent_ticket_summary)
    TextView tvDependentTicketSummary;
    @BindView(R.id.iv_edit_depend_on)
    ImageView ivEditDependsOn;
    @BindView(R.id.iv_delete_depend_on)
    ImageView ivDeleteDependsOn;
    @BindView(R.id.rl_depends_on_title)
    RelativeLayout rlDependsOnTitle;
    @BindView(R.id.expandable_layout_depends_on)
    ExpandableLayout elDependsOn;
    @BindView(R.id.iv_depends_on_dropdown)
    ImageView ivDependsOnDropDown;
    @BindView(R.id.ll_depends_on_details)
    LinearLayout llDependsOnDetails;
    @BindView(R.id.tv_add_dependent_ticket)
    TextView tvAddDependentTicket;

    private boolean expandEmployee = true;
    private boolean expandCustomer = true;
    private boolean expandDependsOn = true;
    private long ticketId;
    private BottomSheetBehavior sheetBehavior;
    private String status;
    private Animation rotation;
    private List<AssignEmployee> employeeList;
    private EmployeeSearchAdapter employeeSearchAdapter;
    private String selectedEmployeeId;
    private Employee selfEmployee;
    private AssignEmployee selectedEmployee;
    private LinearLayout llSelf;
    private TextView tvAllUsers;
    private RecyclerView rvAllUsers;
    private CircleImageView civSelf;
    private TextView tvSelf;
    private ProgressBar pbEmployee;
    private boolean isEditable = false;
    private BottomSheetDialog ticketTypeSheet;
    private RecyclerView rvTicketTypes;
    private SearchTicketTypeAdapter adapter;
    private BottomSheetDialog prioritySheet;
    private BottomSheetDialog teamSheet;
    private EditText etSearchTeam;
    private RecyclerView rvTeams;
    private SearchTeamAdapter teamAdapter;
    List<String> tags = new ArrayList<>();
    List<Label> labels = new ArrayList<>();
    private List<Tags> tagsList;
    private List<Label> labelList;
    private Tickets tickets;
    private BottomSheetDialog labelSheet;
    private EditText etSearchLabel;
    private SearchLabelAdapter labelAdapter;
    private RecyclerView rvLabels;
    private boolean subscribed, contributed;
    private String userType;
    private BottomSheetDialog employeeBottomSheet;
    private RecyclerView rvEmployee;
    private OnStatusChangeListener listener;
    private ImageView ivTick;
    private LinearLayout llEmployeeAsSelf;
    private List<DependentTicket> dependentTicketList = new ArrayList<>();
    private AppCompatEditText etSearchTicket;
    private BottomSheetDialog ticketDependencySheet;
    private RecyclerView rvTicket;
    private DependentTicketSearchAdapter dependentTicketAdapter;
    private DependentTicket dependentTicket;


    public TicketTimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent i = Objects.requireNonNull(getActivity()).getIntent();
        ticketId = i.getLongExtra("selected_ticket_id", -1);
        contributed = i.getBooleanExtra("contributed", false);
        subscribed = i.getBooleanExtra("subscribed", false);
        tickets = TicketRepo.getInstance().getTicketById(ticketId);
        dependentTicketList = DependentTicketRepo.getInstance().getAllDependentTickets();
        createTicketDependencyBottomSheet();

        if (contributed) {
            setContributorPermissions();
            removeScrollviewMargin();
        } else if (subscribed) {
            setSubscriberPermissions();
            removeScrollviewMargin();
        }

        Account userAccount = AccountRepo.getInstance().getAccount();
        boolean isCustomer = tickets.getCustomer().getCustomerId().equalsIgnoreCase(userAccount.getAccountId());
        boolean isAssigned = tickets.getAssignedEmployee().getAccountId().equalsIgnoreCase(userAccount.getAccountId());
        if (isCustomer) {
            llStatusHolder.setVisibility(View.GONE);
        }
        if (isAssigned) {
            llStatusHolder.setVisibility(View.VISIBLE);
        }

        createEmployeeBottomSheet();
        if (ticketId != -1) {
            GlobalUtils.showLog(TAG, "ticket id check:" + ticketId);
            presenter.getTicketTypes();
            presenter.getCustomerDetails(ticketId);
            presenter.getAssignedEmployees(ticketId);
            presenter.getTicketTimeline(ticketId);
            presenter.getEmployees();
            presenter.getTicketDetailsById(ticketId);

            if (dependentTicketList.isEmpty()) {
                presenter.getDependencyListTickets();
            }
            setTicketDetails();
            setContributors();
            setUserType();
            setPermissions();
        }

        rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);

       /* TicketDetailsActivity mActivity = (TicketDetailsActivity) getActivity();
        assert mActivity != null;
        mActivity.setOutSideTouchListener(this);*/

        selfEmployee = EmployeeRepo.getInstance().getEmployee();

        btnReopen.setOnClickListener(v -> reopenTicket());

        btnStartTask.setOnClickListener(v -> presenter.startTask(ticketId));

        tvCustomerDropdown.setOnClickListener(v -> {
            ivDropdownCustomer.setImageTintList(AppCompatResources.getColorStateList
                    (Objects.requireNonNull(getContext()), R.color.colorPrimary));
            expandCustomer = !expandCustomer;
            ivDropdownCustomer.startAnimation(rotation);
            if (expandCustomer) {
                ivDropdownCustomer.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropup));
            } else {
                ivDropdownCustomer.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropdown_toggle));
            }
            elCustomer.toggle();
        });

        tvContributorDropDown.setOnClickListener(v -> {
            ivContributorDropDown.setImageTintList(AppCompatResources.getColorStateList
                    (Objects.requireNonNull(getContext()), R.color.colorPrimary));
            expandEmployee = !expandEmployee;
            ivContributorDropDown.startAnimation(rotation);
            if (expandEmployee) {
                ivContributorDropDown.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropup));
            } else {
                ivContributorDropDown.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropdown_toggle));
            }
            elContributor.toggle();
        });

        tvContributor.setOnClickListener(v -> {
            GlobalUtils.showLog(TAG, "sender ticket Id: " + ticketId);
            Intent intent = new Intent(getActivity(), AddContributorActivity.class);
            intent.putExtra("ticket_id", ticketId);
            startActivityForResult(intent,
                    ADD_CONTRIBUTOR);
        });

        sheetBehavior = BottomSheetBehavior.from(mBottomSheet);

        setBotReplyChangeListener();

        ivEditDependsOn.setOnClickListener(v -> ticketDependencySheet.show());

        ivDeleteDependsOn.setOnClickListener(v -> showRemoveDependencyDialog());

        tvAddDependentTicket.setOnClickListener(v -> ticketDependencySheet.show());
    }

    private void showRemoveDependencyDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to remove this dependency?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> presenter.updateTicket(ticketId, null));

        builder1.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());

        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(R.color.colorPrimary));

        });
        alert11.show();
    }

    private void setUserType() {

    }

    private void setPermissions() {

    }

    private void setSubscriberPermissions() {
        llStatusHolder.setVisibility(View.GONE);
        ivAssignEmployee.setVisibility(View.GONE);
        tvContributor.setVisibility(View.GONE);
//        rlBotReplyHolder.setVisibility(View.GONE);
    }

    private void setContributorPermissions() {
        llStatusHolder.setVisibility(View.GONE);
        ivAssignEmployee.setVisibility(View.GONE);
        tvContributor.setVisibility(View.GONE);
    }

    private void setContributors() {
        tickets = TicketRepo.getInstance().getTicketById(ticketId);
        RealmList<AssignEmployee> contributorList = tickets.getContributorList();
        if (!CollectionUtils.isEmpty(contributorList)) {
            GlobalUtils.showLog(TAG, "contributors list not empty");
            llContributors.setVisibility(View.VISIBLE);
            inflateContributorLayout(contributorList, llContributorList);
        } else {
            GlobalUtils.showLog(TAG, "contirbutors empty");
            llContributors.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CONTRIBUTOR && resultCode == 2) {
            if (data != null) {
                boolean employeeAssigned = data.getBooleanExtra("contributor_added",
                        false);
                List<String> contributorIds = data.getStringArrayListExtra("contributors");
                if (employeeAssigned && contributorIds != null) {
                    addContributorsLocally(contributorIds);
                }
            }
        }

        if (requestCode == EDIT_RESULT && resultCode == 2) {
            if (data != null) {
                tickets = TicketRepo.getInstance().getTicketById(ticketId);
                String type = data.getStringExtra("type");
//                String editedText = data.getStringExtra("edited_text");

                switch (type) {
                    case "title":
                        tvTicketTitle.setText(tickets.getTitle());
                        break;

                    case "desc":
                        tvAddDesc.setVisibility(View.GONE);
                        tvTicketDesc.setVisibility(View.VISIBLE);
                        tvTicketDesc.setText(tickets.getDescription());

                        if (tickets.getDescription().isEmpty()) {
                            tvAddDesc.setVisibility(View.VISIBLE);
                            tvTicketDesc.setVisibility(View.GONE);
                        }
                        break;

                    case "estimated_time":
                        tvAddEstTime.setVisibility(View.GONE);
                        tvEstimatedTimeValue.setVisibility(View.VISIBLE);
                        tvEstimatedTimeValue.setText(tickets.getEstimatedTime());
                        setRemainingTime();
                        break;
                }
            }
        }
    }

    private void addContributorsLocally(List<String> contributorIds) {
        GlobalUtils.showLog(TAG, "add contributors locally");
        GlobalUtils.showLog(TAG, "contributor list size: " + contributorIds.size());
        RealmList<AssignEmployee> contributorList = new RealmList<>();
        for (String contributorId : contributorIds
        ) {
            AssignEmployee employee = AssignEmployeeRepo.getInstance()
                    .getAssignedEmployeeById(contributorId);
            if (employee != null) GlobalUtils.showLog(TAG, "Employee: " +
                    employee.getEmployeeId());
            contributorList.add(employee);
        }

        Tickets ticket = TicketRepo.getInstance().getTicketById(ticketId);
        for (AssignEmployee contributor : ticket.getContributorList()
        ) {
            if (!contributorList.contains(contributor)) {
                contributorList.add(contributor);
            }
        }
//        contributorList.addAll(ticket.getContributorList());
        TicketRepo.getInstance().setContributors(ticketId, contributorList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "contributors added");
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> setContributors());
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "error while adding contributors");
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        boolean isTicketStarted = Hawk.get(Constants.TICKET_STARTED, false);

        if (isTicketStarted) {
            onTicketStarted();
            Hawk.put(Constants.TICKET_STARTED, false);
        }

        Tickets ticket = TicketRepo.getInstance().getTicketById(ticketId);
        if (!ticket.isBotEnabled()) {
            botReply.setChecked(false);
        }
    }

    @OnClick(R.id.rl_selected_status)
    public void toggleStatusOptions() {
        ivDropDownStatus.setImageTintList(AppCompatResources.getColorStateList
                (Objects.requireNonNull(getContext()), R.color.colorPrimary));
        if (llStatusOptions.getVisibility() == View.VISIBLE) {
            llStatusOptions.setVisibility(View.GONE);

            ivDropDownStatus.setImageDrawable(Objects.requireNonNull(getActivity()).getResources()
                    .getDrawable(R.drawable.ic_oc_drop_down_blue));
        } else {
            llStatusOptions.setVisibility(View.VISIBLE);

            ivDropDownStatus.setImageDrawable(Objects.requireNonNull(getActivity()).getResources()
                    .getDrawable(R.drawable.ic_drop_up_blue));
        }
    }


    @OnClick(R.id.tv_close)
    public void closeTicket() {
        llStatusOptions.setVisibility(View.GONE);
        showStatusChangeConfirmation("Are you sure you want to close this ticket?",
                "close");
    }

    @OnClick(R.id.tv_reopen)
    public void reopenTicket() {
        llStatusOptions.setVisibility(View.GONE);
        showStatusChangeConfirmation("Are you sure you want to re-open this ticket?",
                "reopen");
    }

    @OnClick(R.id.tv_resolve)
    public void resolveTicket() {
        llStatusOptions.setVisibility(View.GONE);
        showStatusChangeConfirmation("Are you sure you want to resolve this ticket?",
                "resolve");
    }

    @SuppressLint("SetTextI18n")
    public void setTicketDetails() {
        Account userAccount = AccountRepo.getInstance().getAccount();
        tickets = TicketRepo.getInstance().getTicketById(ticketId);
        if (userAccount.getAccountId().equalsIgnoreCase(tickets.getCreatedById())
                || userAccount.getAccountId().equalsIgnoreCase(tickets.getAssignedEmployee().getAccountId())
                || userAccount.getAccountType().equalsIgnoreCase(AnydoneProto.AccountType.SERVICE_PROVIDER.name())) {
            isEditable = true;
        }

        if (isEditable) {
            setCheckedTags(tickets);
            labels.addAll(tickets.getLabelRealmList());
            makeViewsEditable();
        }

        showHideDependentTicket();
        rlDependsOnTitle.setOnClickListener(v -> {
            ivDependsOnDropDown.setImageTintList(AppCompatResources.getColorStateList
                    (Objects.requireNonNull(getContext()), R.color.colorPrimary));
            expandDependsOn = !expandDependsOn;
            ivDependsOnDropDown.startAnimation(rotation);
            if (expandDependsOn) {
                ivDependsOnDropDown.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropup));
            } else {
                ivDependsOnDropDown.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropdown_toggle));
            }
            elDependsOn.toggle();
        });


        switch (tickets.getTicketStatus()) {
            case "TICKET_CREATED":
                GlobalUtils.showLog(TAG, "ticket type is TODO");
                rlSelectedStatus.setVisibility(View.GONE);
//                removeScrollviewMargin();
                btnReopen.setVisibility(View.GONE);

                if (tickets.getAssignedEmployee().getAccountId()
                        .equalsIgnoreCase(userAccount.getAccountId())) {
                    addScrollviewMargin();
                    btnStartTask.setVisibility(View.VISIBLE);
                    GlobalUtils.showLog(TAG, "start button set to visible");
                }
                break;

            case "TICKET_STARTED":
                onTicketStarted();
                btnReopen.setVisibility(View.GONE);
                if (tickets.getTicketType().equalsIgnoreCase(Constants.SUBSCRIBED) ||
                        tickets.getTicketType().equalsIgnoreCase(Constants.CONTRIBUTED)) {
                    removeScrollviewMargin();
                }
                btnStartTask.setVisibility(View.GONE);
                GlobalUtils.showLog(TAG, "start button set to gone");
                break;

            case "TICKET_RESOLVED":

                tvStatusSelected.setText("RESOLVED");
                tvResolve.setVisibility(View.GONE);
                vSeparator.setVisibility(View.GONE);

                tvReopen.setVisibility(View.VISIBLE);
                vSeparator1.setVisibility(View.VISIBLE);

                addScrollviewMargin();
                btnReopen.setVisibility(View.GONE);

//                rlBotReplyHolder.setVisibility(View.GONE);
                btnStartTask.setVisibility(View.GONE);
                GlobalUtils.showLog(TAG, "start button set to gone");

                break;

            case "TICKET_CLOSED":
                addScrollviewMargin();
                btnReopen.setVisibility(View.VISIBLE);
                hideActions();
                btnStartTask.setVisibility(View.GONE);
                GlobalUtils.showLog(TAG, "start button set to gone");
                break;

            case "TICKET_REOPENED":
                btnReopen.setVisibility(View.GONE);
                rlSelectedStatus.setVisibility(View.GONE);
//                removeScrollviewMargin();
                if (tickets.getAssignedEmployee().getAccountId()
                        .equalsIgnoreCase(userAccount.getAccountId())) {
                    btnStartTask.setVisibility(View.VISIBLE);
                    GlobalUtils.showLog(TAG, "start button set to visible");
                    addScrollviewMargin();
                }
                break;
        }

     /*   if (tickets.isBotEnabled()) {
            botReply.setChecked(true);
        }*/
        tvTicketId.setText(String.valueOf(tickets.getTicketIndex()));
        tvTicketCreatedDate.setText(GlobalUtils.getDateLong(tickets.getCreatedAt()));
        tvTicketCreatedTime.setText(GlobalUtils.getTimeExcludeMillis(tickets.getCreatedAt()));
        tvTicketTitle.setText(tickets.getTitle());

        if (!tickets.getTicketCategory().isEmpty())
            tvTicketType.setText(tickets.getTicketCategory());
        else {
            tvTicketType.setVisibility(View.GONE);
            tvTicketTypeTitle.setVisibility(View.GONE);
        }

        GlobalUtils.showLog(TAG, "est time check: " + tickets.getEstimatedTime());
        GlobalUtils.showLog(TAG, "est time stamp check: " + tickets.getEstimatedTimeStamp());
        if (tickets.getEstimatedTime().isEmpty()) {
            if (isEditable) {
                tvAddEstTime.setVisibility(View.VISIBLE);
                tvEstimatedTimeValue.setVisibility(View.GONE);

                tvAddEstTime.setOnClickListener(v -> {
                    Intent i = new Intent(getActivity(), EditTicketActivity.class);
                    i.putExtra("type", "estimated_time");
                    i.putExtra("text", tvEstimatedTimeValue.getText().toString().trim());
                    i.putExtra("ticket_id", String.valueOf(ticketId));
                    startActivityForResult(i, EDIT_RESULT);
                });
            } else {
                tvEstimatedTimeValue.setVisibility(View.VISIBLE);
                tvEstimatedTime.setVisibility(View.VISIBLE);
                tvEstimatedTimeValue.setText("N/A");
            }
        } else {
            tvEstimatedTimeValue.setVisibility(View.VISIBLE);
            tvEstimatedTime.setVisibility(View.VISIBLE);

            if (tickets.getEstimatedTimeStamp() != 0) {
                setRemainingTime();
            } else {
                tvEstimatedTimeValue.setText(tickets.getEstimatedTime());
            }
        }

        if (tickets.getDescription() == null || tickets.getDescription().isEmpty()) {
            if (isEditable) {
                tvAddDesc.setVisibility(View.VISIBLE);
                tvTicketDesc.setVisibility(View.GONE);
                tvAddDesc.setOnClickListener(v -> {
                    Intent i = new Intent(getActivity(), EditTicketActivity.class);
                    i.putExtra("type", "desc");
                    i.putExtra("text", tvTicketDesc.getText().toString().trim());
                    i.putExtra("ticket_id", String.valueOf(ticketId));
                    startActivityForResult(i, EDIT_RESULT);
                });

            } else {
                tvDescTitle.setVisibility(View.VISIBLE);
                tvTicketDesc.setVisibility(View.VISIBLE);
                tvTicketDesc.setText("N/A");
            }

        } else {
            tvDescTitle.setVisibility(View.VISIBLE);
            tvTicketDesc.setVisibility(View.VISIBLE);
            tvTicketDesc.setText(tickets.getDescription());
        }

        GlobalUtils.showLog(TAG, "created by check: " + tickets.getCreatedByName());
        tvTicketCreatedBy.setText(tickets.getCreatedByName());

        String profilePicUrl = tickets.getCreatedByPic();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(this).load(profilePicUrl).apply(options).into(civTicketCreatedBy);
        }

//        getTicketTimelineSuccess(tickets.getAssignedEmployee());

        addTeamsToLayout();
        setLabels();
        setPriority(tickets.getPriority());
        setService(tickets.getServiceId());

        if (tickets.getTicketType().equalsIgnoreCase(Constants.SUBSCRIBED)) {
            hideActions();
        }

        handleAssignedCase(tickets, userAccount);
        handleSubscriberCase(tickets, userAccount);

        botReply.setChecked(tickets.isBotEnabled());
    }

    @SuppressLint("SetTextI18n")
    private void showHideDependentTicket() {
        if (tickets.getDependentTicket() != null) {
            GlobalUtils.showLog(TAG, "dependent ticket not null");
            rlDependsOn.setVisibility(View.VISIBLE);
            tvDependentTicketId.setText("#" + tickets.getDependentTicket().getIndex());
            tvDependentTicketSummary.setText(tickets.getDependentTicket().getSummary());
            llDependsOnDetails.setVisibility(View.VISIBLE);
            tvAddDependentTicket.setVisibility(View.GONE);
            if (isEditable) {
                ivEditDependsOn.setVisibility(View.VISIBLE);
                ivDeleteDependsOn.setVisibility(View.VISIBLE);
            } else {
                ivEditDependsOn.setVisibility(View.GONE);
                ivDeleteDependsOn.setVisibility(View.GONE);
            }
        } else {
            ivEditDependsOn.setVisibility(View.GONE);
            ivDeleteDependsOn.setVisibility(View.GONE);
            if (isEditable) {
                rlDependsOn.setVisibility(View.VISIBLE);
                llDependsOnDetails.setVisibility(View.GONE);
                tvAddDependentTicket.setVisibility(View.VISIBLE);
            } else {
                rlDependsOn.setVisibility(View.GONE);
            }

        }
    }

    private void setRemainingTime() {
        StringBuilder estTime = new StringBuilder(tickets.getEstimatedTime());
        GlobalUtils.showLog(TAG, "est time st: " + tickets.getEstimatedTimeStamp());
        GlobalUtils.showLog(TAG, "currnet time st: " + System.currentTimeMillis());
        if (tickets.getEstimatedTimeStamp() > System.currentTimeMillis()) {
            String remainingTime = DateUtils.getRelativeTimeSpanString
                    (tickets.getEstimatedTimeStamp()).toString();
            boolean estTimeInDate = false;
            if (remainingTime.contains("In")) {
                remainingTime = remainingTime.replace("In", "");
            } else {
                estTimeInDate = true;
            }

            GlobalUtils.showLog(TAG, "time remain: " + remainingTime);
            estTime.append(" (");
            estTime.append(remainingTime);
            if (!estTimeInDate) estTime.append(" remaining");
            estTime.append(" )");
        } else if (tickets.getEstimatedTimeStamp() == 0) {

        } else {
            estTime.append(" (");
            estTime.append("time exceeded");
            estTime.append(")");
        }


        tvEstimatedTimeValue.setText(estTime);
    }


    private void makeViewsEditable() {
        tagsList = TagRepo.getInstance().getAllTags();
        labelList = LabelRepo.getInstance().getAllLabels();
        createTicketTypeSheet();
        createPriorityBottomSheet();
        createTeamBottomSheet();
        createLabelBottomSheet();
        tvTicketType.setOnClickListener(v -> {
            ticketTypeSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            ticketTypeSheet.show();
        });

        tvTicketTitle.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), EditTicketActivity.class);
            i.putExtra("type", "title");
            i.putExtra("text", tvTicketTitle.getText().toString().trim());
            i.putExtra("ticket_id", String.valueOf(ticketId));
            startActivityForResult(i, EDIT_RESULT);
        });

        tvTicketDesc.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), EditTicketActivity.class);
            i.putExtra("type", "desc");
            i.putExtra("text", tvTicketDesc.getText().toString().trim());
            i.putExtra("ticket_id", String.valueOf(ticketId));
            startActivityForResult(i, EDIT_RESULT);
        });

        tvEstimatedTimeValue.setOnClickListener(v -> {
            String estTime = tvEstimatedTimeValue.getText().toString().trim();
            if (estTime.contains("(")) {
                int bracketIndex = estTime.indexOf("(");
                estTime = estTime.substring(0, bracketIndex);
            }
            Intent i = new Intent(getActivity(), EditTicketActivity.class);
            i.putExtra("type", "estimated_time");
            i.putExtra("text", estTime);
            i.putExtra("ticket_id", String.valueOf(ticketId));
            startActivityForResult(i, EDIT_RESULT);
        });

        tvPriority.setOnClickListener(v -> prioritySheet.show());

        llTags.setOnClickListener(v -> teamSheet.show());

        llLabels.setOnClickListener(v -> labelSheet.show());

    }


    private void createTicketTypeSheet() {
        ticketTypeSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View llBottomSheet = getLayoutInflater()
                .inflate(R.layout.bottomsheet_select_service, null);

        ticketTypeSheet.setContentView(llBottomSheet);
        ticketTypeSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        ticketTypeSheet.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
       /*     if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);*/
            setupSheetHeight(d, BottomSheetBehavior.STATE_HALF_EXPANDED);
        });


        EditText searchTicketType = llBottomSheet.findViewById(R.id.et_search_service);
        searchTicketType.setHint("Search ticket type");
        rvTicketTypes = llBottomSheet.findViewById(R.id.rv_services);

        searchTicketType.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setupSheetHeight(ticketTypeSheet, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        ticketTypeSheet.setOnDismissListener(dialog -> searchTicketType.clearFocus());

        List<TicketCategory> ticketTypeList = TicketCategoryRepo.getInstance().getAllTicketCategories();
        if (!CollectionUtils.isEmpty(ticketTypeList)) {
            setUpTicketTypeRecyclerView(ticketTypeList);
        }

        searchTicketType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void setLabels() {
        GlobalUtils.showLog(TAG, "label list checK: " + tickets.getLabelRealmList().size());
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
                tvLabel.setTextSize(13);
                llLabels.addView(tvLabel);
            }
        } else {
            tvAddLabel.setVisibility(View.VISIBLE);
            if (isEditable) {
                tvAddLabel.setOnClickListener(v -> labelSheet.show());
            } else {
                tvAddLabel.setText("N/A");
                tvAddLabel.setTextColor(getResources().getColor(R.color.black));
            }
        }

    }

    private void createLabelBottomSheet() {
        labelSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.bottom_sheeet_label_alternate, null);

        labelSheet.setContentView(view);
        labelSheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        TextView tvLabelDone = view.findViewById(R.id.tv_done);
        etSearchLabel = view.findViewById(R.id.et_search_label);
        rvLabels = view.findViewById(R.id.rv_labels);
        ImageView ivBack = view.findViewById(R.id.iv_back);
        RelativeLayout rlNewLabel = view.findViewById(R.id.rl_new_label);
        TextView tvNewLabel = view.findViewById(R.id.tv_new_label);

        ivBack.setOnClickListener(v -> labelSheet.dismiss());
        setUpLabelRecyclerView(labelList, rvLabels, rlNewLabel, tvNewLabel);

        labelSheet.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById
                    (com.google.android.material.R.id.design_bottom_sheet);
      /*      if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);*/
            setupFullHeight(d);
            etSearchLabel.requestFocus();
            UiUtils.showKeyboardForced(getContext());

            //check mark selected teams
            labelAdapter.setData(labels);

            rlRoot.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                int heightDiff = rlRoot.getRootView().getHeight() - rlRoot.getHeight();
                ViewGroup.LayoutParams params = rvLabels.getLayoutParams();
                params.height = getWindowHeight() - heightDiff + 100;
            });
        });


        etSearchLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getActivity().runOnUiThread(() -> labelAdapter.getFilter().filter(s));
                if (s.length() == 0) {
                    rlNewLabel.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvLabelDone.setOnClickListener(v -> {
            labelSheet.dismiss();
            presenter.editLabel(String.valueOf(ticketId), labels);
        });

        labelSheet.setOnDismissListener(dialog -> {
            GlobalUtils.showLog(TAG, "label dismissed");

            setLabels();
            etSearchLabel.setText("");
            UiUtils.hideKeyboardForced(getContext());
        });
    }

    private void handleSubscriberCase(Tickets tickets, Account userAccount) {
        if (!tickets.getCreatedById().equalsIgnoreCase(userAccount.getAccountId()) &&
                !tickets.getAssignedEmployee().getAccountId().equalsIgnoreCase
                        (userAccount.getAccountId())) {
//            rlBotReplyHolder.setVisibility(View.GONE);
            tvContributor.setVisibility(View.GONE);
            ivAssignEmployee.setVisibility(View.GONE);
        }
    }

    private void handleAssignedCase(Tickets tickets, Account userAccount) {
        if (tickets.getTicketStatus().equalsIgnoreCase(
                TicketProto.TicketState.TICKET_STARTED.name())) {
            if (userAccount.getAccountId().equalsIgnoreCase
                    (tickets.getAssignedEmployee().getAccountId())) {
                rlSelectedStatus.setVisibility(View.VISIBLE);
                addScrollviewMargin();
            } else {
                removeScrollviewMargin();
            }
        }
    }

    private void createPriorityBottomSheet() {
        prioritySheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.layout_bottomsheet_priority, null);

        prioritySheet.setContentView(view);
        LinearLayout llLowestPriority = view.findViewById(R.id.ll_priority_lowest);
        LinearLayout llLowPriority = view.findViewById(R.id.ll_priority_low);
        LinearLayout llMediumPriority = view.findViewById(R.id.ll_priority_medium);
        LinearLayout llHighestPriority = view.findViewById(R.id.ll_priority_highest);
        LinearLayout llHighPriority = view.findViewById(R.id.ll_priority_high);

        llLowPriority.setOnClickListener(v -> {
            presenter.editTicketPriority(String.valueOf(ticketId), 2);
            prioritySheet.dismiss();
        });

        llLowestPriority.setOnClickListener(v -> {
            presenter.editTicketPriority(String.valueOf(ticketId), 1);
            prioritySheet.dismiss();
        });

        llMediumPriority.setOnClickListener(v -> {
            presenter.editTicketPriority(String.valueOf(ticketId), 3);
            prioritySheet.dismiss();
        });

        llHighPriority.setOnClickListener(v -> {
            presenter.editTicketPriority(String.valueOf(ticketId), 4);
            prioritySheet.dismiss();
        });

        llHighestPriority.setOnClickListener(v -> {
            presenter.editTicketPriority(String.valueOf(ticketId), 5);
            prioritySheet.dismiss();
        });
    }

    private void setUpLabelRecyclerView(List<Label> labelList, RecyclerView rvLabels,
                                        RelativeLayout rlNewLabel, TextView tvNewLabel) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvLabels.setLayoutManager(mLayoutManager);

        labelAdapter = new SearchLabelAdapter(labelList, getContext());
        rvLabels.setAdapter(labelAdapter);

        labelAdapter.setOnFilterListEmptyListener(() -> {
            tvNewLabel.setText(etSearchLabel.getText().toString().trim());
            rlNewLabel.setVisibility(View.VISIBLE);

            rlNewLabel.setOnClickListener(v -> {
                addNewLabel(labelList,
                        tvNewLabel.getText().toString().trim());
                rlNewLabel.setVisibility(View.GONE);
                etSearchLabel.setText("");
            });
        });

        labelAdapter.setOnItemClickListener(new SearchLabelAdapter.OnItemClickListener() {
            @Override
            public void onItemAdd(Label label) {
                GlobalUtils.showLog(TAG, "item add");
                if (!labels.contains(label)) {
                    labels.add(label);
                }
                GlobalUtils.showLog(TAG, " added labels: " + label);
            }

            @Override
            public void onItemRemove(Label label) {
                GlobalUtils.showLog(TAG, "item remove");
                labels.remove(label);
                GlobalUtils.showLog(TAG, "removed labels: " + label);
            }
        });
    }

    private void addNewLabel(List<Label> labelList, String labelName) {
        Label newLabel = new Label();
        newLabel.setName(labelName);
        newLabel.setLabelId("");

        labelList.add(newLabel);
        labelAdapter.setNewData(labelList);
        labels.add(newLabel);
    }

    private void setupSheetHeight(BottomSheetDialog bottomSheetDialog, int state) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
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
            Toast.makeText(getActivity(), "bottom sheet null", Toast.LENGTH_SHORT).show();
        }
    }


    private void setBotReplyChangeListener() {
        botReply.setOnCheckedChangeListener((buttonView, isChecked) -> {
            botReply.setClickable(false);
            if (isChecked) {
                presenter.enableBot(String.valueOf(ticketId));
            } else {
                presenter.disableBot(String.valueOf(ticketId));
            }
        });
    }

    private void setUpTicketTypeRecyclerView(List<TicketCategory> ticketTypeList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvTicketTypes.setLayoutManager(mLayoutManager);

        adapter = new SearchTicketTypeAdapter(ticketTypeList, getActivity());
        rvTicketTypes.setAdapter(adapter);

        adapter.setOnItemClickListener(ticketType -> {
            hideKeyBoard();
            ticketTypeSheet.dismiss();
            presenter.editTicketType(String.valueOf(ticketId), ticketType.getCategoryId(),
                    ticketType.getName());
        });
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

    public void onTicketStarted() {
        tvReopen.setVisibility(View.GONE);
        vSeparator1.setVisibility(View.GONE);

        tvResolve.setVisibility(View.VISIBLE);
        vSeparator.setVisibility(View.VISIBLE);

        setRemainingTime();
        addScrollviewMargin();

        tvStatusSelected.setText("STARTED");
        tvStatusSelected.setVisibility(View.VISIBLE);
        rlSelectedStatus.setVisibility(View.VISIBLE);
        llStatusHolder.setVisibility(View.VISIBLE);
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

        layoutParams.setMargins(0, 0, 0,
                GlobalUtils.convertDpToPixel(Objects.requireNonNull(getContext()), 77));
        scrollView.setLayoutParams(layoutParams);
    }

    private void hideActions() {
        rlSelectedStatus.setVisibility(View.GONE);
//        rlBotReplyHolder.setVisibility(View.GONE);
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
                        .getWindow().getDecorView().getRootView(), Constants.SERVER_ERROR);
    }

    private void inflateContributorLayout(RealmList<AssignEmployee> contributors,
                                          LinearLayout parent) {
        parent.removeAllViews();
        GlobalUtils.showLog(TAG, "contributors size check: " + contributors.size());
        for (AssignEmployee contributor : contributors
        ) {
            @SuppressLint("InflateParams") View viewAssignedEmployee = getLayoutInflater()
                    .inflate(R.layout.layout_contributor_row, null, false);
            TextView employeeName = viewAssignedEmployee.findViewById(R.id.tv_field);
            CircleImageView employeePic = viewAssignedEmployee.findViewById(R.id.civ_field);
            ImageView deleteEmployee = viewAssignedEmployee.findViewById(R.id.iv_delete);

            if (subscribed || contributed) {
                deleteEmployee.setVisibility(View.GONE);
            }

            deleteEmployee.setOnClickListener(v -> {
                GlobalUtils.showLog(TAG, "employee id: " + contributor.getEmployeeId());
                int pos = parent.indexOfChild(v);
                showDeleteDialog(contributor.getEmployeeId());
            });

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

    private void showConfirmationDialog(String employeeId, String employeeName, boolean isSelf) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        if (isSelf)
            builder1.setMessage("Assign this ticket to yourself?");
        else
            builder1.setMessage("Assign this ticket to " + employeeName + "?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    employeeBottomSheet.dismiss();
                    presenter.assignTicket(ticketId, employeeId);
                    dialog.dismiss();
                });

        builder1.setNegativeButton(
                "No",
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

    private void createTeamBottomSheet() {
        teamSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.bottom_sheet_team_alternate, null);

        teamSheet.setContentView(view);
        teamSheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        TextView tvTeamDone = view.findViewById(R.id.tv_done);
        etSearchTeam = view.findViewById(R.id.et_search_employee);
        rvTeams = view.findViewById(R.id.rv_teams);
        ImageView ivBack = view.findViewById(R.id.iv_back);

        ivBack.setOnClickListener(v -> teamSheet.dismiss());

        setUpTeamRecyclerView(tagsList, rvTeams);

        teamSheet.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
      /*      if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);*/
            setupFullHeight(d);
            etSearchTeam.requestFocus();
            UiUtils.showKeyboardForced(getContext());

            //check mark selected teams
            teamAdapter.setData(tags);

            rlRoot.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                int heightDiff = rlRoot.getRootView().getHeight() - rlRoot.getHeight();
                ViewGroup.LayoutParams params = rvTeams.getLayoutParams();
                params.height = getWindowHeight() - heightDiff + 100;
            });
        });


        etSearchTeam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Objects.requireNonNull(getActivity()).runOnUiThread(() ->
                        teamAdapter.getFilter().filter(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvTeamDone.setOnClickListener(v -> {
            presenter.editTeam(String.valueOf(ticketId), tags);
            teamSheet.dismiss();
        });

        teamSheet.setOnDismissListener(dialog -> {
            GlobalUtils.showLog(TAG, "team dismissed");
            addTeamsToLayout();

            etSearchTeam.setText("");
            UiUtils.hideKeyboardForced(getContext());
        });
    }

    private void setCheckedTags(Tickets tickets) {
        for (Tags tag : tickets.getTagsRealmList()
        ) {
            tags.add(tag.getTagId());
        }
    }

    private void addTeamsToLayout() {
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
                tvTag.setTextSize(13);
                llTags.addView(tvTag);
                llTags.setVisibility(View.VISIBLE);
            }
        } else {
            llTags.removeAllViews();
            llTags.setVisibility(View.GONE);
            tvAddTeam.setVisibility(View.VISIBLE);
            if (isEditable) {
                tvAddTeam.setOnClickListener(v -> teamSheet.show());
            } else {
                tvAddTeam.setText("N/A");
                tvAddTeam.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }


    private void showDeleteDialog(String employeeId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to delete?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> {
                    presenter.unAssignContributor(ticketId, employeeId);
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
  /*      int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (getResources().getDisplayMetrics().heightPixels);

        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_dialog);

        TextView text = dialog.findViewById(R.id.tv_message);
        text.setText(title);

        RelativeLayout holder = dialog.findViewById(R.id.rl_dialog_holder);
        Objects.requireNonNull(dialog.getWindow()).setLayout
                (width, holder.getMeasuredHeight());

        Button cancel = dialog.findViewById(R.id.btn_cancel);
        Button ok = dialog.findViewById(R.id.btn_ok);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();*/

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


 /*   @Override
    public void onOutsideClick(MotionEvent event) {
        GlobalUtils.showLog(TAG, "on outside click second");
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            Rect outRect = new Rect();
            mBottomSheet.getGlobalVisibleRect(outRect);

            if (!outRect.contains((int) event.getRawX(), (int) event.getRawY()))
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }*/

    private void setUpTeamRecyclerView(List<Tags> tagsList, RecyclerView rvTeams) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvTeams.setLayoutManager(mLayoutManager);

        teamAdapter = new SearchTeamAdapter(tagsList, getContext());
        rvTeams.setAdapter(teamAdapter);

        teamAdapter.setOnItemClickListener(new SearchTeamAdapter.OnItemClickListener() {
            @Override
            public void onItemAdd(Tags tag) {
                GlobalUtils.showLog(TAG, "item add");
                if (!tags.contains(tag.getTagId())) {
                    tags.add(tag.getTagId());
                }
                GlobalUtils.showLog(TAG, "tags: " + tags);
            }

            @Override
            public void onItemRemove(Tags tag) {
                GlobalUtils.showLog(TAG, "item remove");
                tags.remove(tag.getTagId());
                GlobalUtils.showLog(TAG, "tags: " + tags);
            }
        });
    }

    @Override
    public void getTicketTimelineSuccess(AssignEmployee assignedEmployee) {
        GlobalUtils.showLog(TAG, "get timeline success()");
        if (assignedEmployee.getEmployeeId().isEmpty()) {
            ivAssignEmployee.setImageDrawable
                    (getResources().getDrawable(R.drawable.ic_assign_employee));
        } else {
            ivAssignEmployee.setImageDrawable
                    (getResources().getDrawable(R.drawable.ic_switch_employee));
            tvAssignedEmployee.setText(assignedEmployee.getName());

            String employeeImage = assignedEmployee.getEmployeeImageUrl();
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(this).load(employeeImage).apply(options).into(civAssignedEmployee);


            GlobalUtils.showLog(TAG, "emp id check: " + assignedEmployee.getEmployeeId());
            employeeSearchAdapter.setChecked(assignedEmployee.getEmployeeId());
            Account userAccount = AccountRepo.getInstance().getAccount();
            if (userAccount.getAccountId().equalsIgnoreCase(assignedEmployee.getAccountId())) {
                ivTick.setVisibility(View.VISIBLE);
                llEmployeeAsSelf.setClickable(false);
                employeeSearchAdapter.removeCheckMark();
            } else {
                ivTick.setVisibility(View.GONE);
                llEmployeeAsSelf.setClickable(true);
            }
        }

        ivAssignEmployee.setOnClickListener(v -> {
            employeeBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            employeeBottomSheet.show();
        });
    }

    private void createEmployeeBottomSheet() {
        employeeBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View llBottomSheet = getLayoutInflater()
                .inflate(R.layout.bottomsheet_select_employee, null);

        employeeBottomSheet.setContentView(llBottomSheet);
        employeeBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        employeeBottomSheet.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
       /*     if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);*/
            setupSheetHeight(d, BottomSheetBehavior.STATE_HALF_EXPANDED);
        });

        EditText searchEmployee = llBottomSheet.findViewById(R.id.et_search_employee);
        llEmployeeAsSelf = llBottomSheet.findViewById(R.id.ll_self);
        CircleImageView civEmployeeAsSelf = llBottomSheet.findViewById(R.id.civ_image_self);
        TextView tvEmployeeAsSelf = llBottomSheet.findViewById(R.id.tv_name_self);
        TextView tvEmployeeAllUsers = llBottomSheet.findViewById(R.id.tv_all_users);
        TextView tvSuggestions = llBottomSheet.findViewById(R.id.tv_suggestions);
        rvEmployee = llBottomSheet.findViewById(R.id.rv_all_users);
        ivTick = llBottomSheet.findViewById(R.id.iv_tick);

        setSelfDetails(llEmployeeAsSelf, tvEmployeeAsSelf, civEmployeeAsSelf, tvSuggestions);

        llEmployeeAsSelf.setOnClickListener(v -> {
            Employee self = EmployeeRepo.getInstance().getEmployee();
            if (self != null) {
                AssignEmployee selfEmployee = new AssignEmployee();
                selfEmployee.setPhone(self.getPhone());
                selfEmployee.setName(self.getName());
                selfEmployee.setEmployeeImageUrl(self.getEmployeeImageUrl());
                selfEmployee.setEmployeeId(self.getEmployeeId());
                selfEmployee.setEmail(self.getEmail());
                selfEmployee.setCreatedAt(self.getCreatedAt());
                selfEmployee.setAccountId(self.getAccountId());

                selectedEmployee = selfEmployee;
                selectedEmployeeId = self.getEmployeeId();
                showConfirmationDialog(selectedEmployeeId, self.getName(), true);
            }
        });

        searchEmployee.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setupSheetHeight(employeeBottomSheet, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        employeeBottomSheet.setOnDismissListener(dialog -> {
            searchEmployee.clearFocus();
            searchEmployee.getText().clear();
            if (rvEmployee.getChildCount() > 0) rvEmployee.scrollToPosition(0);
        });

        setUpEmployeeRecyclerView(rvEmployee);
        searchEmployee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                employeeSearchAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(this).load(profilePicUrl).apply(options).into(civCustomer);
        }
    }

    @Override
    public void setAssignedEmployee(AssignEmployee assignedEmployee) {
        if (!assignedEmployee.getName().isEmpty()) {
            tvAssignedEmployee.setText(assignedEmployee.getName());
        } else {
            tvAssignedEmployee.setText(R.string.unassigned);
        }

        employeeSearchAdapter.setChecked(assignedEmployee.getEmployeeId());
        Account userAccount = AccountRepo.getInstance().getAccount();
        if (userAccount.getAccountId().equalsIgnoreCase(assignedEmployee.getAccountId())) {
            ivTick.setVisibility(View.VISIBLE);
            llEmployeeAsSelf.setClickable(false);
            employeeSearchAdapter.removeCheckMark();
        } else {
            ivTick.setVisibility(View.GONE);
            llEmployeeAsSelf.setClickable(true);
        }

        String employeeImage = assignedEmployee.getEmployeeImageUrl();
        if (employeeImage != null && !employeeImage.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(Objects.requireNonNull(getActivity()))
                    .load(employeeImage)
                    .apply(options)
                    .into(civAssignedEmployee);
        }
    }

    @Override
    public void onContributorUnAssignSuccess(String empId) {
        setContributors();
        Hawk.put(Constants.TICKET_ASSIGNED, true);
    }

    @Override
    public void onContributorUnAssignFail(String msg) {
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
        listener.onTaskClosed();
        TicketRepo.getInstance().changeTicketStatusToClosed(ticketId);
        setTicketDetails();
        Hawk.put(Constants.REFETCH_TICKET, true);
        Hawk.put(Constants.TICKET_RESOLVED, true);
        Hawk.put(Constants.TICKET_IN_PROGRESS, true);
        Hawk.put(Constants.TICKET_STARTED, false);
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
        listener.onTaskReopened();
        TicketRepo.getInstance().changeTicketStatusToReopened(ticketId);
        setTicketDetails();
        Hawk.put(Constants.REFETCH_TICKET, true);
        Hawk.put(Constants.TICKET_PENDING, true);
        Hawk.put(Constants.TICKET_RESOLVED, true);
        Hawk.put(Constants.TICKET_STARTED, false);
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
        listener.onTaskResolved();
        TicketRepo.getInstance().changeTicketStatusToResolved(ticketId);
        setTicketDetails();
        Hawk.put(Constants.REFETCH_TICKET, true);
        Hawk.put(Constants.TICKET_RESOLVED, true);
        Hawk.put(Constants.TICKET_IN_PROGRESS, true);
        Hawk.put(Constants.TICKET_STARTED, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpEmployeeRecyclerView(RecyclerView rvEmployee) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvEmployee.setLayoutManager(mLayoutManager);

        GlobalUtils.showLog(TAG, "employee list: " + employeeList);
        employeeSearchAdapter = new EmployeeSearchAdapter(employeeList, getContext(), false);
        rvEmployee.setAdapter(employeeSearchAdapter);

        rvEmployee.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return false;
        });

        if (employeeSearchAdapter != null) {
            employeeSearchAdapter.setOnItemClickListener((employee) -> {
                UiUtils.hideKeyboardForced(getContext());
                selectedEmployee = employee;
                selectedEmployeeId = employee.getEmployeeId();

                showConfirmationDialog(selectedEmployeeId, employee.getName(), false);
            });
        }

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

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

            int windowHeight = getWindowHeight();
            if (layoutParams != null) {
                layoutParams.height = windowHeight;
            }
            bottomSheet.setLayoutParams(layoutParams);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            Toast.makeText(getActivity(), "bottom sheet null", Toast.LENGTH_SHORT).show();
        }
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager()
                .getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    @Override
    public void getEmployeeSuccess() {
        employeeList = AssignEmployeeRepo.getInstance().getAllAssignEmployees();
        setUpEmployeeRecyclerView(rvEmployee);
    }

    @Override
    public void getEmployeeFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }
        UiUtils.showSnackBar(getActivity(),
                Objects.requireNonNull
                        (getActivity()).getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void assignSuccess(String empId) {
        Account userAccount = AccountRepo.getInstance().getAccount();
        Employee userEmployee = EmployeeRepo.getInstance()
                .getEmployeeByAccountId(userAccount.getAccountId());

        //tick mark if assigned to self
        if (empId.equalsIgnoreCase(userEmployee.getEmployeeId())) {
            ivTick.setVisibility(View.VISIBLE);
            llEmployeeAsSelf.setClickable(false);
            employeeSearchAdapter.removeCheckMark();

            //allow to start task if current status is pending
            if (tickets.getTicketStatus().equalsIgnoreCase("TICKET_CREATED")) {
                addScrollviewMargin();
                btnStartTask.setVisibility(View.VISIBLE);
            }
        } else {
            ivTick.setVisibility(View.GONE);
            llEmployeeAsSelf.setClickable(true);
        }

        //ticket mark if assigned to other employee
        employeeSearchAdapter.setChecked(empId);

        Hawk.put(Constants.FETCH_PENDING_LIST, true);
        Hawk.put(Constants.FETCH_IN_PROGRESS_LIST, true);
        tvAssignedEmployee.setText(selectedEmployee.getName());
        String employeeImage = selectedEmployee.getEmployeeImageUrl();
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                .error(R.drawable.ic_empty_profile_holder_icon);

        Glide.with(Objects.requireNonNull(getActivity()))
                .load(employeeImage)
                .apply(options)
                .into(civAssignedEmployee);

        ivAssignEmployee.setImageDrawable
                (getResources().getDrawable(R.drawable.ic_switch_employee));

        employeeBottomSheet.dismiss();
        UiUtils.hideKeyboardForced(getActivity());
    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager)
                Objects.requireNonNull
                        (getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
    }

    @Override
    public void assignFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }
        UiUtils.showSnackBar(getActivity(),
                getActivity().getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void showProgressEmployee() {
        if (pbEmployee != null) {
            pbEmployee.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressEmployee() {
        if (pbEmployee != null) {
            pbEmployee.setVisibility(View.GONE);
        }
    }

    @Override
    public void enableBotSuccess() {
        botReply.setClickable(true);
        botReply.setChecked(true);
        TicketRepo.getInstance().enableBotReply(String.valueOf(ticketId));
    }

    @Override
    public void enableBotFail(String msg) {
        botReply.setClickable(true);
        botReply.setOnCheckedChangeListener(null);
        botReply.setChecked(false);
        setBotReplyChangeListener();
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }
        UiUtils.showSnackBar(getActivity(), Objects.requireNonNull
                (getActivity()).getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void disableBotFail(String msg) {
        botReply.setClickable(true);
        botReply.setOnCheckedChangeListener(null);
        botReply.setChecked(true);
        setBotReplyChangeListener();
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }
        UiUtils.showSnackBar(getActivity(),
                Objects.requireNonNull
                        (getActivity()).getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void disableBotSuccess() {
        botReply.setClickable(true);
        botReply.setChecked(false);
        TicketRepo.getInstance().disableBotReply(String.valueOf(ticketId));
    }

    @Override
    public void onPriorityEditSuccess(int priority) {
        setPriority(priority);
    }

    @Override
    public void onPriorityEditFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        Banner.make(getActivity().getWindow().getDecorView().getRootView(),
                getActivity(), Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void onEditTeamSuccess() {
        tvAddTeam.setVisibility(View.GONE);
        addTeamsLocally();
    }

    private void addTeamsLocally() {
        RealmList<Tags> teamList = new RealmList<>();
        for (String teamId : tags
        ) {
            Tags tags = TagRepo.getInstance()
                    .getTagById(teamId);
            if (tags != null) GlobalUtils.showLog(TAG, "Tag: " +
                    tags.getLabel());
            teamList.add(tags);
        }

        TicketRepo.getInstance().editTeams(ticketId, teamList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "teams added");
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> addTeamsToLayout());
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "error while adding teams");
            }
        });

    }

    @Override
    public void onEditTeamFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        Banner.make(getActivity().getWindow().getDecorView().getRootView(),
                getActivity(), Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void onEditLabelSuccess() {
        tvAddLabel.setVisibility(View.GONE);
        addLabelsLocally(labels);
    }

    private void addLabelsLocally(List<Label> labels) {
        RealmList<Label> labelRealmList = new RealmList<>();
        labelRealmList.addAll(labels);

        TicketRepo.getInstance().editLabels(ticketId, labelRealmList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "labels added");
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> setLabels());
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "error while adding contributors");
            }
        });
    }

    @Override
    public void onEditLabelFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                getActivity(), Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void onTicketTypeEditSuccess() {
        tvTicketType.setText(tickets.getTicketCategory());
    }

    @Override
    public void onTicketTypeEditFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        Banner.make(getActivity().getWindow().getDecorView().getRootView(),
                getActivity(), Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void getTypeSuccess() {
        createTicketTypeSheet();
    }

    @Override
    public void getTypeFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        Banner.make(getActivity().getWindow().getDecorView().getRootView(),
                getActivity(), Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void onTaskStartSuccess(long estTime) {
//        btnStartTask.setVisibility(View.GONE);
//        onTicketStarted();
        GlobalUtils.showLog(TAG, "start button set to gone");
        listener.onTaskStarted();
        TicketRepo.getInstance().changeTicketStatusToStart(ticketId, estTime);
        onTicketStarted();
        Hawk.put(Constants.TICKET_STARTED, false);
//        Hawk.put(Constants.TICKET_STARTED, true);
        Hawk.put(Constants.TICKET_PENDING, true);
        Hawk.put(Constants.TICKET_IN_PROGRESS, true);

//        TicketRepo.getInstance().setTicketEstTime(ticketId, estTime);
        setRemainingTime();
    }

    @Override
    public void onTaskStartFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                getActivity(), Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void getTicketByIdSuccess(Tickets ticket) {
        tickets = ticket;
        GlobalUtils.showLog(TAG, "ticket dependency check: " + tickets.getDependentTicket());
//        setTicketDetails();
    }

    @Override
    public void getTicketByIdFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
        }
    }

    @Override
    public void getDependencyTicketsListSuccess() {
        dependentTicketList = DependentTicketRepo.getInstance().getAllDependentTickets();
        setUpDependentTicketRecyclerView(dependentTicketList, rvTicket);
    }


    private void createTicketDependencyBottomSheet() {
        ticketDependencySheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.layout_bottom_sheet_ticket_dependency, null);

        ticketDependencySheet.setContentView(view);
        ticketDependencySheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        etSearchTicket = view.findViewById(R.id.et_search_ticket);
        rvTicket = view.findViewById(R.id.rv_ticket);

        setUpDependentTicketRecyclerView(dependentTicketList, rvTicket);

        ticketDependencySheet.setOnShowListener(dialog -> {
            etSearchTicket.requestFocus();
            UiUtils.showKeyboardForced(getActivity());
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            setupSheetHeight(d, BottomSheetBehavior.STATE_EXPANDED);
        });


        etSearchTicket.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
//                    presenter.searchTickets(s.toString());
                    dependentTicketAdapter.getFilter().filter(s);
                } else dependentTicketAdapter.setData(dependentTicketList);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ticketDependencySheet.setOnDismissListener(dialog -> {
            etSearchTicket.clearFocus();
            etSearchTicket.getText().clear();
            UiUtils.hideKeyboardForced(getActivity());
        });
    }


    @Override
    public void getDependencyTicketsListFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
        }
    }

    @Override
    public void searchDependentTicketSuccess(List<DependentTicket> ticketList) {
        dependentTicketAdapter.setData(ticketList);
    }

    @Override
    public void searchDependentTicketFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void updateTicketSuccess(DependentTicket ticket, boolean isDelete) {
        if (!isDelete) {
            tvDependentTicketId.setText("#" + ticket.getIndex());
            tvDependentTicketSummary.setText(ticket.getSummary());

            llDependsOnDetails.setVisibility(View.VISIBLE);
            tvAddDependentTicket.setVisibility(View.GONE);
            ivEditDependsOn.setVisibility(View.VISIBLE);
            ivDeleteDependsOn.setVisibility(View.VISIBLE);
        } else {
            llDependsOnDetails.setVisibility(View.GONE);
            tvAddDependentTicket.setVisibility(View.VISIBLE);

            ivEditDependsOn.setVisibility(View.GONE);
            ivDeleteDependsOn.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateTicketFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
        }

        Toast.makeText(getActivity(), "Failed to update ticket", Toast.LENGTH_SHORT).show();
    }


    private void setSelfDetails(LinearLayout llEmployeeAsSelf, TextView tvEmployeeAsSelf,
                                CircleImageView civEmployeeAsSelf, TextView tvSuggestions) {
        Employee employee = EmployeeRepo.getInstance().getEmployee();
        GlobalUtils.showLog(TAG, "employee check: " + employee);
        if (employee != null) {
            tvSuggestions.setVisibility(View.VISIBLE);
            llEmployeeAsSelf.setVisibility(View.VISIBLE);

            StringBuilder selfEmployeeText = new StringBuilder(employee.getName());
            selfEmployeeText.append(" (Me)");
            tvEmployeeAsSelf.setText(selfEmployeeText);

            String profilePicUrl = employee.getEmployeeImageUrl();
            if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_empty_profile_holder_icon)
                        .error(R.drawable.ic_empty_profile_holder_icon);

                Glide.with(this).load(profilePicUrl).apply(options).into(civEmployeeAsSelf);
            }
        } else {
            tvSuggestions.setVisibility(View.GONE);
            llEmployeeAsSelf.setVisibility(View.GONE);
        }
    }

    public void setOnTicketStartListener(OnStatusChangeListener listener) {
        this.listener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpDependentTicketRecyclerView(List<DependentTicket> ticketList,
                                                  RecyclerView rvDependentTickets) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvDependentTickets.setLayoutManager(mLayoutManager);

        dependentTicketAdapter = new DependentTicketSearchAdapter(
                Objects.requireNonNull(getActivity()), ticketList);
        rvDependentTickets.setAdapter(dependentTicketAdapter);

        rvDependentTickets.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return false;
        });

        dependentTicketAdapter.setOnItemClickListener(ticket -> {
            dependentTicket = ticket;
            ticketDependencySheet.dismiss();
            presenter.updateTicket(ticketId, dependentTicket);
        });

    }


}
