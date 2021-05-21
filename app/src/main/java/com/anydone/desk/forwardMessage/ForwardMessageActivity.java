package com.anydone.desk.forwardMessage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shasin.notificationbanner.Banner;
import com.anydone.desk.R;
import com.anydone.desk.adapters.ParticipantSelectionAdapter;
import com.anydone.desk.adapters.SearchContributorAdapter;
import com.anydone.desk.base.activity.MvpBaseActivity;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Inbox;
import com.anydone.desk.realm.repo.InboxRepo;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ForwardMessageActivity extends MvpBaseActivity<ForwardMessagePresenterImpl> implements
        ForwardMessageContract.ForwardMessageView {
    private static final String TAG = "ForwardMessageActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_to)
    EditText etSearchEmployee;
    @BindView(R.id.rv_employees)
    RecyclerView rvEmployees;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.fbl_participants)
    FlexboxLayout fblParticipants;
    @BindView(R.id.rv_selected_participants)
    RecyclerView rvSelectedParticipants;
    @BindView(R.id.fab_send)
    FloatingActionButton fabSend;

    private ProgressDialog progress;
    List<String> employeeIds = new ArrayList<>();
    private SearchContributorAdapter adapter;
    private String inboxId;
    private ParticipantSelectionAdapter selectedParticipantAdapter;
    private String forwardMessage;

    @Override
    protected int getLayout() {
        return R.layout.activity_forward_message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        inboxId = i.getStringExtra("inbox_id");
        forwardMessage = i.getStringExtra("msg_to_forward");
        Inbox inbox = InboxRepo.getInstance().getInboxById(inboxId);


        presenter.findParticipants();
        ivBack.setOnClickListener(v -> onBackPressed());

        setUpSelectedParticipantAdapter();

        etSearchEmployee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                runOnUiThread(() -> adapter.getFilter().filter(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fabSend.setOnClickListener(v -> presenter.forwardMessage(employeeIds, forwardMessage, inbox.getInboxType()));


     /*   rvEmployees.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fabSend.hide();
                else if (dy < 0)
                    fabSend.show();
            }
        });*/

        UiUtils.showKeyboard(this, etSearchEmployee);
    }

    private void setUpSelectedParticipantAdapter() {
        FlexboxLayoutManager mLayoutManager = new
                FlexboxLayoutManager(this);
        mLayoutManager.setFlexWrap(FlexWrap.WRAP);
        mLayoutManager.setFlexDirection(FlexDirection.ROW);
        mLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        mLayoutManager.setAlignItems(AlignItems.FLEX_START);

        rvSelectedParticipants.setLayoutManager(mLayoutManager);

        List<String> participantList = new ArrayList<>();
        selectedParticipantAdapter = new ParticipantSelectionAdapter(participantList, this);
        rvSelectedParticipants.setAdapter(selectedParticipantAdapter);

        selectedParticipantAdapter.setOnItemClickListener(participantId -> {
            GlobalUtils.showLog(TAG, "item remove listen");
            employeeIds.remove(participantId);
            GlobalUtils.showLog(TAG, "employee list size: " + employeeIds.size());
            if (employeeIds.size() == 0) {
                disableSending();
            }
            addParticipantToRecyclerView();
            adapter.setData(employeeIds);
        });
    }

    private void setUpRecyclerView(List<AssignEmployee> assignEmployeeList) {
     /*   Account user = AccountRepo.getInstance().getAccount();
        AssignEmployee userEmployee = AssignEmployeeRepo.getInstance().getAssignedEmployeeByAccountId(user.getAccountId());
        assignEmployeeList.remove(userEmployee);*/
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvEmployees.setLayoutManager(mLayoutManager);

        adapter = new SearchContributorAdapter(assignEmployeeList, this);
        adapter.setData(employeeIds);
        rvEmployees.setAdapter(adapter);

        adapter.setOnItemClickListener(new SearchContributorAdapter.OnItemClickListener() {
            @Override
            public void onItemAdd(String employeeId) {
                GlobalUtils.showLog(TAG, "item add listen");
                employeeIds.add(employeeId);
                GlobalUtils.showLog(TAG, "employee list size: " + employeeIds.size());
                if (employeeIds.size() > 0) {
                    enableSending();
                }

                addParticipantToRecyclerView();
                etSearchEmployee.getText().clear();
            }

            @Override
            public void onItemRemove(String employeeId) {
                GlobalUtils.showLog(TAG, "item remove listen");
                employeeIds.remove(employeeId);
                GlobalUtils.showLog(TAG, "employee list size: " + employeeIds.size());
                if (employeeIds.size() == 0) {
                    disableSending();
                }

                addParticipantToRecyclerView();
                etSearchEmployee.getText().clear();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void forwardMessageSuccess() {
        Toast.makeText(this, "Sent", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void forwardMessageFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }

        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void getParticipantSuccess(List<AssignEmployee> assignEmployeeList) {
        setUpRecyclerView(assignEmployeeList);
    }

    @Override
    public void getParticipantFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();
    }


    @Override
    public void showProgressBar(String message) {
        pbProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        pbProgress.setVisibility(View.GONE);
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

/*    private void addParticipantToLayout() {
        fblParticipants.removeAllViews();
        //add selected teams
        for (String employeeId : employeeIds
        ) {
            AssignEmployee employee = AssignEmployeeRepo.getInstance().getAssignedEmployeeById(employeeId);

            @SuppressLint("InflateParams") View view1 = getLayoutInflater()
                    .inflate(R.layout.layout_participant_selected, null);

            TextView participantLabel = view1.findViewById(R.id.tv_participant);
            CircleImageView participantImg = view1.findViewById(R.id.civ_participant);
//            ImageView remove = view1.findViewById(R.id.iv_close);
            participantLabel.setText(employee.getName());

            Glide.with(this)
                    .load(employee.getEmployeeImageUrl())
                    .error(R.drawable.ic_empty_profile_holder_icon)
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .into(participantImg);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(15, 8, 0, 0);
            view1.setLayoutParams(params);
            fblParticipants.addView(view1);
*//*
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*//*
        }

        fblParticipants.addView(etSearchEmployee);
        etSearchEmployee.requestFocus();
    }*/

    private void addParticipantToRecyclerView() {
        fblParticipants.removeAllViews();
        fblParticipants.addView(rvSelectedParticipants);
        selectedParticipantAdapter.setData(employeeIds);
        fblParticipants.addView(etSearchEmployee);
        etSearchEmployee.requestFocus();
    }

    private void enableSending() {
        fabSend.setVisibility(View.VISIBLE);
    }

    private void disableSending() {
        fabSend.setVisibility(View.GONE);
    }
}