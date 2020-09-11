package com.treeleaf.anydone.serviceprovider.threaddetails.threadtimeline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputLayout;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.EmployeeSearchAdapter;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Thread;
import com.treeleaf.anydone.serviceprovider.realm.repo.AssignEmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.EmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ThreadRepo;
import com.treeleaf.anydone.serviceprovider.threaddetails.ThreadDetailActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ThreadTimelineFragment extends BaseFragment<ThreadTimelinePresenterImpl> implements
        ThreadTimelineContract.ThreadTimelineView,
        ThreadDetailActivity.OnOutsideClickListener {
    private static final String TAG = "ThreadTimelineFragment";

    public static final int ASSIGN_EMPLOYEE_REQUEST = 8789;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    @BindView(R.id.ll_assigned_employee)
    LinearLayout llAssignedEmployee;
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
    @BindView(R.id.tv_conversation_details_dropdown)
    TextView tvConversationDetailsDropdown;
    @BindView(R.id.iv_dropdown_conversation_details)
    ImageView ivDropdownConversationDetails;
    @BindView(R.id.expandable_layout_conversation_details)
    ExpandableLayout elConversationDetails;
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
    @BindView(R.id.il_select_employee)
    TextInputLayout ilSelectEmployee;
    @BindView(R.id.et_search_employee)
    AutoCompleteTextView etSearchEmployee;
    @BindView(R.id.ll_self)
    LinearLayout llSelf;
    @BindView(R.id.civ_image_self)
    CircleImageView civSelf;
    @BindView(R.id.tv_name_self)
    TextView tvSelf;
    @BindView(R.id.tv_all_users)
    TextView tvAllUsers;
    @BindView(R.id.rv_all_users)
    RecyclerView rvAllUsers;
    @BindView(R.id.search_employee)
    ScrollView svSearchEmployee;
    @BindView(R.id.iv_source)
    ImageView ivSource;
    @BindView(R.id.tv_source)
    TextView tvSource;
    @BindView(R.id.switch_bot_reply)
    Switch botReply;


    private boolean expandCustomer = true;
    private boolean expandTicketDetails = true;
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


    public ThreadTimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent i = Objects.requireNonNull(getActivity()).getIntent();
        threadId = i.getStringExtra("thread_id");
        if (threadId != null) {
            GlobalUtils.showLog(TAG, "thread id check:" + threadId);
            thread = ThreadRepo.getInstance().getThreadById(threadId);
            setThreadDetails();
        }

        presenter.getEmployees();

        rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        ThreadDetailActivity mActivity = (ThreadDetailActivity) getActivity();
        assert mActivity != null;
        mActivity.setOutSideTouchListener(this);

        selfEmployee = EmployeeRepo.getInstance().getEmployee();

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

        tvConversationDetailsDropdown.setOnClickListener(v -> {
            expandTicketDetails = !expandTicketDetails;
            ivDropdownConversationDetails.startAnimation(rotation);
            if (!expandTicketDetails) {
                ivDropdownConversationDetails.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropup));
            } else {
                ivDropdownConversationDetails.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropdown_toggle));
            }
            elConversationDetails.toggle();
        });

       /* btnMarkComplete.setOnClickListener(v -> startActivity(new Intent(getActivity(),
                PaymentSummary.class)));*/
//        sheetBehavior = BottomSheetBehavior.from(mBottomSheet);

        etSearchEmployee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {
                    GlobalUtils.showLog(TAG, "text changed");
                    employeeList = AssignEmployeeRepo.getInstance().searchEmployee(s.toString());
                    if (CollectionUtils.isEmpty(employeeList)) {
                        tvAllUsers.setVisibility(View.GONE);
                    } else {
                        tvAllUsers.setVisibility(View.VISIBLE);
                    }
                    GlobalUtils.showLog(TAG, "searched list size: " + employeeList.size());
                    if (svSearchEmployee.getVisibility() == View.GONE)
                        svSearchEmployee.setVisibility(View.VISIBLE);
                    if (employeeSearchAdapter != null) {
                        employeeSearchAdapter.setData(employeeList);
                        employeeSearchAdapter.notifyDataSetChanged();
                    }

                    scrollView.fullScroll(View.FOCUS_DOWN);
                    etSearchEmployee.requestFocus();
                } else {
                    svSearchEmployee.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
        tvConversationCreatedDate.setText(GlobalUtils.getDateAlternate(thread.getCreatedAt()));
        tvConversationCreatedTime.setText(GlobalUtils.getTime(thread.getCreatedAt()));
        tvTag.setText(thread.getDefaultLabel());
        setSource(thread);
        setCustomerDetails(thread);

        if (thread.isBotEnabled()) {
            botReply.setChecked(true);
        } else {
            botReply.setChecked(false);
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
                    .placeholder(R.drawable.ic_profile_icon)
                    .error(R.drawable.ic_profile_icon);

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
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        //set bot reply to false when replied from our end
        if (!thread.isBotEnabled()) {
            botReply.setChecked(false);
        }
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

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(getActivity(),
                Objects.requireNonNull(getActivity())
                        .getWindow().getDecorView().getRootView(), message);
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

    private void setUpEmployeeRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvAllUsers.setLayoutManager(mLayoutManager);

        employeeSearchAdapter = new EmployeeSearchAdapter(employeeList, getContext());
        rvAllUsers.setAdapter(employeeSearchAdapter);

        if (employeeSearchAdapter != null) {
            employeeSearchAdapter.setOnItemClickListener((employee) -> {
                selectedEmployeeId = employee.getEmployeeId();
                etSearchEmployee.setText(employee.getName());
                etSearchEmployee.setSelection(employee.getName().length());
                svSearchEmployee.setVisibility(View.GONE);
                hideKeyBoard();
            });
        }

        setSelfDetails();
        llSelf.setOnClickListener(v -> {
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

                selectedEmployeeId = self.getEmployeeId();
            }

            etSearchEmployee.setText(selfEmployee.getName());
            etSearchEmployee.setSelection(selfEmployee.getName().length());
            svSearchEmployee.setVisibility(View.GONE);
        });
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


    private void setSelfDetails() {
        Employee employee = EmployeeRepo.getInstance().getEmployee();
        if (employee != null) {
            tvSelf.setText(employee.getName() + " (Me)");

            String profilePicUrl = employee.getEmployeeImageUrl();
            if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_profile_icon)
                        .error(R.drawable.ic_profile_icon);

                Glide.with(Objects.requireNonNull(getActivity())).load(profilePicUrl).apply(options).into(civSelf);
            }
        }
    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager)
                Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
    }

}
