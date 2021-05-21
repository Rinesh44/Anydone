package com.anydone.desk.threaddetails.threadtimeline;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.anydone.desk.R;
import com.anydone.desk.adapters.EmployeeSearchAdapter;
import com.anydone.desk.adapters.LinkedTicketAdapter;
import com.anydone.desk.base.fragment.BaseFragment;
import com.anydone.desk.injection.component.ApplicationComponent;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Customer;
import com.anydone.desk.realm.model.Employee;
import com.anydone.desk.realm.model.Label;
import com.anydone.desk.realm.model.Service;
import com.anydone.desk.realm.model.Tags;
import com.anydone.desk.realm.model.Thread;
import com.anydone.desk.realm.model.Tickets;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.AssignEmployeeRepo;
import com.anydone.desk.realm.repo.AvailableServicesRepo;
import com.anydone.desk.realm.repo.EmployeeRepo;
import com.anydone.desk.realm.repo.ThreadRepo;
import com.anydone.desk.realm.repo.TicketRepo;
import com.anydone.desk.threaddetails.ThreadDetailActivity;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.UiUtils;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmList;

public class ThreadTimelineFragment extends BaseFragment<ThreadTimelinePresenterImpl> implements
        ThreadTimelineContract.ThreadTimelineView,
        ThreadDetailActivity.OnOutsideClickListener {
    private static final String TAG = "ThreadTimelineFragment";

    public static final int ASSIGN_EMPLOYEE_REQUEST = 8789;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    /*   @BindView(R.id.bottom_sheet_profile)
       LinearLayout mBottomSheet;*/
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
    @BindView(R.id.tv_conversation_created_date)
    TextView tvConversationCreatedDate;
    @BindView(R.id.tv_conversation_created_time)
    TextView tvConversationCreatedTime;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.v_separator)
    View vSeparator;
    @BindView(R.id.v_separator1)
    View vSeparator1;
    @BindView(R.id.rl_bot_reply_holder)
    RelativeLayout rlBotReplyHolder;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.iv_source)
    ImageView ivSource;
    @BindView(R.id.tv_source)
    TextView tvSource;
    @BindView(R.id.switch_bot_reply)
    Switch botReply;
    @BindView(R.id.tv_assign_employee)
    TextView tvAssignEmployee;
    @BindView(R.id.tv_assigned_employee)
    TextView tvAssignedEmployee;
    @BindView(R.id.civ_assigned_employee)
    CircleImageView civAssignedEmployee;
    @BindView(R.id.iv_assign_employee)
    ImageView ivAssignEmployee;
    @BindView(R.id.tv_assign_employee_label)
    TextView tvAssignEmpLabel;
    @BindView(R.id.rl_assign_employee)
    RelativeLayout rlAssignEmployee;
    @BindView(R.id.ll_linked_tickets)
    LinearLayout llLinkedTickets;
    @BindView(R.id.tv_linked_ticket_dropdown)
    TextView tvLinkedTicketDropdown;
    @BindView(R.id.iv_dropdown_linked_tickets)
    ImageView ivLinkedTicketDropdown;
    @BindView(R.id.expandable_layout_linked_tickets)
    ExpandableLayout elLinkedTickets;
    @BindView(R.id.rv_linked_tickets)
    RecyclerView rvLinkedTickets;


    private boolean expandCustomer = true;
    private boolean expandLinkedTickets = true;
    private int viewHeight = 0;
    private String threadId;
    private BottomSheetBehavior sheetBehavior;
    private String status;
    private Animation rotation;
    private List<AssignEmployee> employeeList;
    private EmployeeSearchAdapter employeeSearchAdapter;
    private String selectedEmployeeId;
    private Employee selfEmployee;
    private Thread thread;
    private ProgressBar pbEmployee;
    private AssignEmployee selectedEmployee;
    private LinkedTicketAdapter adapter;
    private CircleImageView civSelf;
    private TextView tvSelf;
    private TextView tvAllUsers;
    private RecyclerView rvAllUsers;

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

    public ThreadTimelineFragment() {
        // Required empty public constructor
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent i = Objects.requireNonNull(getActivity()).getIntent();
        threadId = i.getStringExtra("thread_id");
        if (threadId != null) {
            GlobalUtils.showLog(TAG, "thread id check:" + threadId);
            thread = ThreadRepo.getInstance().getThreadById(threadId);
            presenter.getEmployees();
            presenter.getLinkedTickets(threadId);
            presenter.getThreadById(threadId);
            setThreadDetails();
            createLinkedTicketBottomSheet();
        }

        createEmployeeBottomSheet();
        rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        ThreadDetailActivity mActivity = (ThreadDetailActivity) getActivity();
        assert mActivity != null;
        mActivity.setOutSideTouchListener(this);

        selfEmployee = EmployeeRepo.getInstance().getEmployee();

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

        tvLinkedTicketDropdown.setOnClickListener(v -> {
            ivDropdownCustomer.setImageTintList(AppCompatResources.getColorStateList
                    (Objects.requireNonNull(getContext()), R.color.colorPrimary));
            expandLinkedTickets = !expandLinkedTickets;
            ivLinkedTicketDropdown.startAnimation(rotation);
            if (expandLinkedTickets) {
                ivLinkedTicketDropdown.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropup));
            } else {
                ivLinkedTicketDropdown.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropdown_toggle));
            }
            elLinkedTickets.toggle();
        });


       /* btnMarkComplete.setOnClickListener(v -> startActivity(new Intent(getActivity(),
                PaymentSummary.class)));*/
//        sheetBehavior = BottomSheetBehavior.from(mBottomSheet);

        setBotReplyChangeListener();

    }

    private void setBotReplyChangeListener() {
        botReply.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                presenter.enableBot(threadId);
            } else {
                presenter.disableBot(threadId);
            }
        });
    }

    private void setThreadDetails() {
        tvConversationCreatedDate.setText(GlobalUtils.getDateLong(thread.getCreatedAt()));
        tvConversationCreatedTime.setText(GlobalUtils.getTimeExcludeMillis(thread.getCreatedAt()));
        tvTag.setText(thread.getDefaultLabel());
        setSource(thread);
        setCustomerDetails(thread);
        setAssignedEmployee(thread);
        botReply.setChecked(thread.isBotEnabled());
    }

    private void setAssignedEmployee(Thread thread) {
        if (thread.getAssignedEmployee() == null) {
            tvAssignEmployee.setVisibility(View.VISIBLE);
            tvAssignEmployee.setOnClickListener(v -> {
                employeeBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                employeeBottomSheet.show();
            });
            GlobalUtils.showLog(TAG, "assigned emp null");
        } else {
            tvAssignEmployee.setVisibility(View.GONE);
            rlAssignEmployee.setVisibility(View.VISIBLE);
            tvAssignEmpLabel.setVisibility(View.VISIBLE);

            tvAssignedEmployee.setText(thread.getAssignedEmployee().getName());
            String employeeImage = thread.getAssignedEmployee().getEmployeeImageUrl();
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(Objects.requireNonNull(getActivity()))
                    .load(employeeImage)
                    .apply(options)
                    .into(civAssignedEmployee);

            ivAssignEmployee.setImageDrawable(getResources().getDrawable(R.drawable.ic_switch_employee));
            ivAssignEmployee.setOnClickListener(v -> employeeBottomSheet.show());

            if (thread.getAssignedEmployee() != null) {
                employeeSearchAdapter.setChecked(thread.getAssignedEmployee().getEmployeeId());
                Account userAccount = AccountRepo.getInstance().getAccount();
                if (userAccount.getAccountId().equalsIgnoreCase(thread.getAssignedEmployee().getAccountId())) {
                    ivTick.setVisibility(View.VISIBLE);
                    llEmployeeAsSelf.setClickable(false);
                    employeeSearchAdapter.removeCheckMark();
                } else {
                    ivTick.setVisibility(View.GONE);
                    llEmployeeAsSelf.setClickable(true);
                }
            }

        }
    }

    private void setCustomerDetails(Thread thread) {
        tvCustomerName.setText(thread.getCustomerName());
        if (thread.getCustomerPhone() == null || thread.getCustomerPhone().isEmpty()) {
            llCustomerPhone.setVisibility(View.GONE);
        } else {
            tvCustomerPhone.setText(thread.getCustomerPhone());
        }

        if (thread.getCustomerEmail() == null || thread.getCustomerEmail().isEmpty()) {
            llCustomerEmail.setVisibility(View.GONE);
        } else {
            tvCustomerEmail.setText(thread.getCustomerEmail());
        }

        String profilePicUrl = thread.getCustomerImageUrl();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(this).load(profilePicUrl).apply(options).into(civCustomer);
        }
    }

    private void setSource(Thread thread) {
        switch (thread.getSource()) {
            case "FACEBOOK_THIRD_PARTY_SOURCE":
                tvSource.setText(R.string.messenger);
                ivSource.setImageDrawable(getResources().getDrawable(R.drawable.ic_messenger));
                break;

            case "VIBER_THIRD_PARTY_SOURCE":
                tvSource.setText(R.string.viber);
                ivSource.setImageDrawable(getResources().getDrawable(R.drawable.ic_viber));
                break;

            case "SLACK_THIRD_PARTY_SOURCE":
                tvSource.setText("Slack");
                ivSource.setImageDrawable(getResources().getDrawable(R.drawable.ic_slack));
                break;

            case "MAIL_THIRD_PARTY_SOURCE":
                tvSource.setText("Mail");
                ivSource.setImageDrawable(getResources().getDrawable(R.drawable.ic_link_email));
                break;

        }
    }


    @Override
    public void onResume() {
        super.onResume();

//        boolean botEnabled = Hawk.get(Constants.BOT_REPLY, true);

//        GlobalUtils.showLog(TAG, "check bot boolean:" + botEnabled);
        //set bot reply to false when replied from our end
        if (!thread.isBotEnabled()) {
            botReply.setChecked(false);
        }

        presenter.getLinkedTickets(threadId);

      /*  if (!botEnabled) {
            botReply.setChecked(false);
        }*/
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
           /* if (bottomSheet != null)
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
                showConfirmationDialog(selectedEmployeeId);
            }
        });

        searchEmployee.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setupSheetHeight(employeeBottomSheet, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        employeeBottomSheet.setOnDismissListener(dialog -> {
            setSheetHalfExpanded(dialog);
            searchEmployee.clearFocus();
            searchEmployee.getText().clear();
            if (rvEmployee.getChildCount() > 0) rvEmployee.scrollToPosition(0);
        });

        setUpEmployeeRecyclerView();
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


/*    @SuppressLint("SetTextI18n")
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
    }*/

    @Override
    protected int getLayout() {
        return R.layout.fragment_thread_timeline;
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


    private void setSheetHalfExpanded(DialogInterface dialog) {
        BottomSheetDialog d = (BottomSheetDialog) dialog;
        FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null)
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }


    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(getActivity(),
                Objects.requireNonNull(getActivity())
                        .getWindow().getDecorView().getRootView(), Constants.SERVER_ERROR);
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
     /*   if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            Rect outRect = new Rect();
//            mBottomSheet.getGlobalVisibleRect(outRect);

            if (!outRect.contains((int) event.getRawX(), (int) event.getRawY()))
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }*/
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpEmployeeRecyclerView() {
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

                showConfirmationDialog(selectedEmployeeId);
            });
        }
    }

    @Override
    public void getEmployeeSuccess() {
        employeeList = AssignEmployeeRepo.getInstance().getAllAssignEmployees();
        setUpEmployeeRecyclerView();
    }

    @Override
    public void getEmployeeFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.hideKeyboardForced(Objects.requireNonNull(getActivity()));
        UiUtils.showSnackBar(getActivity(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void enableBotSuccess() {
        botReply.setChecked(true);
        ThreadRepo.getInstance().enableBotReply(threadId);
    }

    @Override
    public void enableBotFail(String msg) {
        botReply.setOnCheckedChangeListener(null);
        botReply.setChecked(false);
        setBotReplyChangeListener();
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }
        UiUtils.showSnackBar(getActivity(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void disableBotFail(String msg) {
        botReply.setOnCheckedChangeListener(null);
        botReply.setChecked(true);
        setBotReplyChangeListener();
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }
        UiUtils.showSnackBar(getActivity(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void disableBotSuccess() {
        botReply.setChecked(false);
        ThreadRepo.getInstance().disableBotReply(threadId);
    }

    @Override
    public void showProgressEmployee() {
        pbEmployee.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressEmployee() {
        pbEmployee.setVisibility(View.GONE);
    }

    @Override
    public void assignSuccess(String empId) {
        setAssignedEmployeeDetails();
        UiUtils.hideKeyboardForced(getActivity());

    }

    private void setAssignedEmployeeDetails() {
        tvAssignedEmployee.setVisibility(View.VISIBLE);
        rlAssignEmployee.setVisibility(View.VISIBLE);
        tvAssignEmpLabel.setVisibility(View.VISIBLE);
        tvAssignEmployee.setVisibility(View.GONE);

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

        ivAssignEmployee.setImageDrawable(getResources().getDrawable(R.drawable.ic_switch_employee));
        ivAssignEmployee.setOnClickListener(v -> employeeBottomSheet.show());

        employeeSearchAdapter.setChecked(selectedEmployee.getEmployeeId());
        Account userAccount = AccountRepo.getInstance().getAccount();
        if (userAccount.getAccountId().equalsIgnoreCase(selectedEmployee.getAccountId())) {
            ivTick.setVisibility(View.VISIBLE);
            llEmployeeAsSelf.setClickable(false);
            employeeSearchAdapter.removeCheckMark();
        } else {
            ivTick.setVisibility(View.GONE);
            llEmployeeAsSelf.setClickable(true);
        }
    }

    @Override
    public void assignFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(), getActivity()
                .getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void getThreadByIdSuccess() {
        tvAssignEmployee.setVisibility(View.GONE);
        rlAssignEmployee.setVisibility(View.VISIBLE);
        tvAssignEmpLabel.setVisibility(View.VISIBLE);

        Thread thread = ThreadRepo.getInstance().getThreadById(threadId);
        tvAssignedEmployee.setText(thread.getAssignedEmployee().getName());
        String employeeImage = thread.getAssignedEmployee().getEmployeeImageUrl();
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                .error(R.drawable.ic_empty_profile_holder_icon);

        Glide.with(Objects.requireNonNull(getActivity()))
                .load(employeeImage)
                .apply(options)
                .into(civAssignedEmployee);

        ivAssignEmployee.setImageDrawable(getResources().getDrawable(R.drawable.ic_switch_employee));

        ivAssignEmployee.setOnClickListener(v -> employeeBottomSheet.show());
    }

    @Override
    public void getThreadByIdFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(), getActivity()
                .getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void getLinkedTicketSuccess() {
        List<Tickets> linkedTicketList = TicketRepo.getInstance().getTicketByThreadId(threadId);
        if (!linkedTicketList.isEmpty()) {
            setUpRecyclerView(linkedTicketList);
            llLinkedTickets.setVisibility(View.VISIBLE);
        } else llLinkedTickets.setVisibility(View.GONE);
    }

    @Override
    public void getLinkedTicketFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

/*        UiUtils.showSnackBar(getActivity(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(), msg);*/
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

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager)
                Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
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
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void showConfirmationDialog(String employeeId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Assign to employee?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> {
                    employeeBottomSheet.dismiss();
                    presenter.assignEmployee(threadId, employeeId);
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

    private void setUpRecyclerView(List<Tickets> ticketsList) {
        rvLinkedTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvLinkedTickets.setVisibility(View.VISIBLE);
            adapter = new LinkedTicketAdapter(ticketsList, getContext());
            adapter.setOnItemClickListener(ticket -> setTicketDetails(ticket.getTicketId()));
            rvLinkedTickets.setAdapter(adapter);
        } else {
            rvLinkedTickets.setVisibility(View.GONE);
        }
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

    private void createLinkedTicketBottomSheet() {
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
            Toast.makeText(getActivity(), "bottom sheet null", Toast.LENGTH_SHORT).show();
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
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(this)
                    .load(employeeImage)
                    .apply(options)
                    .into(civAssignedEmployee);
        }
    }
}
