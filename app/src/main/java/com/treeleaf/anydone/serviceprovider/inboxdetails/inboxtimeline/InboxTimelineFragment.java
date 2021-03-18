package com.treeleaf.anydone.serviceprovider.inboxdetails.inboxtimeline;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.treeleaf.anydone.entities.InboxProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.ParticipantAdapter;
import com.treeleaf.anydone.serviceprovider.addparticipant.AddParticipantActivity;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.editInbox.EditInboxActivity;
import com.treeleaf.anydone.serviceprovider.inboxdetails.InboxDetailActivity;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;
import com.treeleaf.anydone.serviceprovider.realm.model.Participant;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.InboxRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ParticipantRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation.OnInboxEditListener;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmList;

public class InboxTimelineFragment extends BaseFragment<InboxTimelinePresenterImpl> implements
        InboxTimelineContract.InboxTimelineView,
        InboxDetailActivity.OnOutsideClickListener {
    private static final String TAG = "InboxTimelineFragment";
    public static final int ADD_PARTICIPANT = 6781;
    public static final int EDIT_RESULT = 8098;
    public static final int ASSIGN_EMPLOYEE_REQUEST = 8789;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    /*   @BindView(R.id.bottom_sheet_profile)
       LinearLayout mBottomSheet;*/
    @BindView(R.id.tv_participant_dropdown)
    TextView tvParticipantDropdown;
    @BindView(R.id.iv_dropdown_participant)
    ImageView ivDropdownParticipant;
    @BindView(R.id.expandable_layout_participant)
    ExpandableLayout elParticipant;
    @BindView(R.id.tv_inbox_created_date)
    TextView tvConversationCreatedDate;
    @BindView(R.id.tv_inbox_created_time)
    TextView tvConversationCreatedTime;
    @BindView(R.id.v_separator)
    View vSeparator;
    @BindView(R.id.v_separator1)
    View vSeparator1;
    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;
    @BindView(R.id.iv_created_by)
    CircleImageView ivCreatedBy;
    @BindView(R.id.tv_created_by_name)
    TextView tvCreatedBy;
    @BindView(R.id.tv_add_participants)
    TextView tvAddParticipants;
    @BindView(R.id.tv_subject)
    TextView tvSubject;
    @BindView(R.id.tv_add_subject)
    TextView tvAddSubject;
    @BindView(R.id.rv_participants)
    RecyclerView rvParticipantName;
    @BindView(R.id.sw_mute)
    Switch swMute;
    @BindView(R.id.tv_leave_and_del)
    TextView tvLeaveAndDel;
    @BindView(R.id.tv_mute_settings)
    TextView tvMuteSettings;
    @BindView(R.id.tv_participant_count)
    TextView tvParticipantsCount;
    @BindView(R.id.tv_convert_to_group)
    TextView tvConvertToGroup;

    private boolean expandParticipants = true;
    private int viewHeight = 0;
    private String inboxId;
    private BottomSheetBehavior sheetBehavior;
    private String status;
    private Animation rotation;
    private Inbox inbox;
    private ProgressBar pbEmployee;
    private ParticipantAdapter adapter;
    private OnInboxEditListener listener;
    private BottomSheetDialog muteSheet;
    private CheckBox cbMuteAll, cbMuteMentions;
    private BottomSheetDialog convertDialog;

    public InboxTimelineFragment() {
        // Required empty public constructor
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent i = Objects.requireNonNull(getActivity()).getIntent();

        inboxId = i.getStringExtra("inbox_id");
        GlobalUtils.showLog(TAG, "inbox id check on timeline:" + inboxId);

        if (inboxId == null && i.getExtras() != null) {
            inboxId = i.getExtras().getString("inboxId");
        }
        createMuteBottomSheet();
        if (inboxId != null) {
            inbox = InboxRepo.getInstance().getInboxById(inboxId);
//            presenter.getThreadById(threadId);
            setInboxDetails();

            createConvertToGroupSheet(inbox);
            if (inbox.getInboxType().equalsIgnoreCase(InboxProto.Inbox.InboxType.DIRECT_MESSAGE.name())) {
                tvLeaveAndDel.setText("Delete conversation");
                tvAddParticipants.setVisibility(View.GONE);
                tvConvertToGroup.setVisibility(View.VISIBLE);
            } else {
                tvConvertToGroup.setVisibility(View.GONE);
            }
        }

        tvAddParticipants.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddParticipantActivity.class);
            intent.putExtra("inbox_id", inboxId);
            startActivityForResult(intent,
                    ADD_PARTICIPANT);
        });


        rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        InboxDetailActivity mActivity = (InboxDetailActivity) getActivity();
        assert mActivity != null;
        mActivity.setOutSideTouchListener(this);

        tvParticipantDropdown.setOnClickListener(v -> {
            ivDropdownParticipant.setImageTintList(AppCompatResources.getColorStateList
                    (Objects.requireNonNull(getContext()), R.color.colorPrimary));
            expandParticipants = !expandParticipants;
            ivDropdownParticipant.startAnimation(rotation);
            if (expandParticipants) {
                ivDropdownParticipant.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropup));
            } else {
                ivDropdownParticipant.setImageDrawable(getActivity().getResources()
                        .getDrawable(R.drawable.ic_dropdown_toggle));
            }
            elParticipant.toggle();
        });

        tvLeaveAndDel.setOnClickListener(v -> showLeaveConfirmation());

        swMute.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                presenter.muteInboxNotification(inboxId, false);
            } else {
                presenter.unMuteNotification(inboxId);
            }
        });

        tvMuteSettings.setOnClickListener(v -> muteSheet.show());

        tvConvertToGroup.setOnClickListener(v -> convertDialog.show());
    }

    private void createConvertToGroupSheet(Inbox inbox) {
        convertDialog = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View llBottomSheet = getLayoutInflater()
                .inflate(R.layout.layout_convert_to_grp_dialog, null);

        convertDialog.setContentView(llBottomSheet);
//        convertDialog.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

 /*       convertDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
         *//*   if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);*//*
            setupSheetHeight(d, BottomSheetBehavior.STATE_HALF_EXPANDED);
        });*/

        TextInputEditText grpName = llBottomSheet.findViewById(R.id.et_group_name);
        TextView tvConvert = llBottomSheet.findViewById(R.id.btn_ok);
        TextView tvCancel = llBottomSheet.findViewById(R.id.btn_cancel);

        if (!inbox.getSubject().isEmpty()) {
            grpName.setText(inbox.getSubject());
        }
        grpName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setupSheetHeight(convertDialog, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        convertDialog.setOnDismissListener(dialog -> grpName.clearFocus());

        tvCancel.setOnClickListener(v -> convertDialog.dismiss());

        tvConvert.setOnClickListener(v -> {
            convertDialog.dismiss();
            String group = Objects.requireNonNull(grpName.getText()).toString();
            if (group.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter group", Toast.LENGTH_SHORT).show();
                return;
            }

            presenter.convertToGroup(inbox, group);
        });
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

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void setInboxDetails() {
        tvConversationCreatedDate.setText(GlobalUtils.getDateLong(inbox.getCreatedAt()));
        tvConversationCreatedTime.setText(GlobalUtils.getTimeExcludeMillis(inbox.getCreatedAt()));

        if (inbox.getParticipantList() != null && !inbox.getParticipantList().isEmpty()) {
            tvParticipantsCount.setVisibility(View.VISIBLE);
            tvParticipantsCount.setText("(" + inbox.getParticipantList().size() + ")");
        }

        if (inbox.getSubject() != null && !inbox.getSubject().isEmpty()) {
            tvSubject.setText(inbox.getSubject());
        } else {
            tvSubject.setVisibility(View.GONE);
            tvAddSubject.setVisibility(View.VISIBLE);
        }

        for (Participant participant : inbox.getParticipantList()
        ) {
            GlobalUtils.showLog(TAG, "participants check: " + participant.getEmployee().getName());
        }

        Glide.with(getContext())
                .load(inbox.getCreatedByUserProfilePic())
                .error(R.drawable.ic_empty_profile_holder_icon)
                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                .into(ivCreatedBy);

        tvCreatedBy.setText(inbox.getCreatedByUserFullName());
        setUpRecyclerView(inbox.getParticipantList());

        tvSubject.setOnClickListener(v -> startEdit());
        tvAddSubject.setOnClickListener(v -> {
            tvSubject.setText("");
            startEdit();
        });

        setNotificationSettings();

        if (inbox.isLeftGroup()) {
            tvAddParticipants.setVisibility(View.GONE);
            tvLeaveAndDel.setVisibility(View.GONE);
            swMute.setVisibility(View.GONE);
        }

    }

    private void setNotificationSettings() {
        if (inbox.getNotificationType().equalsIgnoreCase
                (InboxProto.InboxNotificationType.EVERY_NEW_MESSAGE_INBOX_NOTIFICATION.name())) {
            swMute.setChecked(false);
            tvMuteSettings.setVisibility(View.GONE);
        } else if (inbox.getNotificationType().equalsIgnoreCase(InboxProto.InboxNotificationType.MUTED_INBOX_NOTIFICATION.name())) {
            swMute.setChecked(true);
            tvMuteSettings.setVisibility(View.VISIBLE);
            cbMuteMentions.setChecked(false);
        } else {
            swMute.setChecked(true);
            cbMuteMentions.setChecked(true);
            tvMuteSettings.setVisibility(View.VISIBLE);
        }

    }

    private void startEdit() {
        Intent i = new Intent(getActivity(), EditInboxActivity.class);
        i.putExtra("subject", tvSubject.getText().toString().trim());
        i.putExtra("inbox_id", String.valueOf(inboxId));
        startActivityForResult(i, EDIT_RESULT);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_inbox_timeline;
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

    private void createMuteBottomSheet() {
        muteSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View llBottomSheet = getLayoutInflater()
                .inflate(R.layout.bottomsheet_mute, null);

        muteSheet.setContentView(llBottomSheet);

        cbMuteAll = llBottomSheet.findViewById(R.id.cb_mute_all);
        cbMuteMentions = llBottomSheet.findViewById(R.id.cb_mute_mentions);
        cbMuteAll.setClickable(false);

    /*    cbMuteAll.setOnClickListener(v -> {
            muteSheet.dismiss();
            presenter.muteInboxNotification(inboxId, false);
        });

        cbMuteMentions.setOnClickListener(v -> {
            muteSheet.dismiss();
            presenter.muteInboxNotification(inboxId, true);
        });*/

        cbMuteMentions.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                presenter.muteInboxNotification(inboxId, true);
            } else {
                presenter.muteInboxNotification(inboxId, false);
            }
        });


    }

    private void setUpRecyclerView(List<Participant> participantList) {
        rvParticipantName.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(participantList)) {
            adapter = new ParticipantAdapter(participantList, getContext(), inbox.getInboxType(),
                    inbox.isLeftGroup());
            adapter.setOnMuteClickListener((participant, pos) ->
                    presenter.updateParticipantNotification(inboxId, participant.getParticipantId(),
                            participantList, true));

            adapter.setOnUnMuteClickListener((participant, pos) ->
                    presenter.updateParticipantNotification(inboxId, participant.getParticipantId(),
                            participantList, false));

            adapter.setOnDeleteClickListener(this::showDeleteConfirmation);
            rvParticipantName.setAdapter(adapter);
        }
    }

    public void toggleMuteBottomSheet() {
        if (muteSheet.isShowing()) {
            muteSheet.dismiss();
        } else {
            muteSheet.show();
        }
    }

    private void showDeleteConfirmation(Participant participant, int pos) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to remove this participant?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    dialog.dismiss();
                    List<Participant> participantIds = getParticipantIds(participant);
                    presenter.deleteParticipant(inboxId, participantIds);
//                    presenter.reopenTicket(Long.parseLong(ticketId));
                });

        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> dialog.dismiss());


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(R.color.colorPrimary));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                    .getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                    .getColor(android.R.color.holo_red_dark));

        });
        alert11.show();
    }


    private void showConvertConfirmation(Inbox inbox) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_convert_to_grp_dialog);

        TextInputEditText etGrpName = dialog.findViewById(R.id.et_group_name);
        TextView convert = dialog.findViewById(R.id.btn_ok);
        TextView cancel = dialog.findViewById(R.id.btn_cancel);

        UiUtils.showKeyboardForced(getActivity());
        etGrpName.requestFocus();
        etGrpName.setText(inbox.getSubject());

        if (!inbox.getSubject().isEmpty()) {
            etGrpName.setSelection(inbox.getSubject().length());
        }

        cancel.setOnClickListener(v -> {
            UiUtils.hideKeyboardForced(getContext());
            dialog.dismiss();
        });

        convert.setOnClickListener(v -> {
            String grpName = etGrpName.getText().toString().trim();
            if (grpName.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter group name", Toast.LENGTH_SHORT).show();
                return;
            }

            presenter.convertToGroup(inbox, etGrpName.getText().toString().trim());
        });

        dialog.show();


   /*     AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to convert to group?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Convert",
                (dialog, id) -> {
                    dialog.dismiss();
                    presenter.convertToGroup(inbox);
//                    presenter.reopenTicket(Long.parseLong(ticketId));
                });

        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> dialog.dismiss());


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(R.color.colorPrimary));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                    .getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                    .getColor(android.R.color.holo_red_dark));

        });
        alert11.show();*/
    }

    private void showLeaveConfirmation() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to leave this conversation?");
        builder1.setCancelable(true);

        if (inbox.isLeftGroup() || inbox.getInboxType().equalsIgnoreCase(InboxProto.Inbox.InboxType.DIRECT_MESSAGE.name())) {
            builder1.setPositiveButton(
                    "Delete",
                    (dialog, id) -> {
                        presenter.leaveAndDeleteConversation(inbox.getInboxId());
                        dialog.dismiss();
                    });

            builder1.setNegativeButton(
                    "Cancel",
                    (dialog, id) -> dialog.dismiss());
        } else {
            builder1.setNeutralButton(
                    "Cancel",
                    (dialog, id) -> dialog.dismiss());


            builder1.setPositiveButton(
                    "Leave & delete",
                    (dialog, id) -> {
                        presenter.leaveAndDeleteConversation(inbox.getInboxId());
                        dialog.dismiss();
                    });

            builder1.setNegativeButton(
                    "Leave",
                    (dialog, id) -> {
                        presenter.leaveConversation(inbox.getInboxId());
                        dialog.dismiss();
                    });

        }


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            if (inbox.isLeftGroup() ||
                    inbox.getInboxType().equalsIgnoreCase(InboxProto.Inbox.InboxType.DIRECT_MESSAGE.name())) {

                alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setBackgroundColor(getResources().getColor(R.color.transparent));
                alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(getResources().getColor(R.color.colorPrimary));

                alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                        .getColor(R.color.transparent));
                alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                        .getColor(android.R.color.holo_red_dark));

            } else {
                alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setBackgroundColor(getResources().getColor(R.color.transparent));
                alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(getResources().getColor(android.R.color.holo_red_dark));

                alert11.getButton(AlertDialog.BUTTON_NEUTRAL)
                        .setBackgroundColor(getResources().getColor(R.color.transparent));
                alert11.getButton(AlertDialog.BUTTON_NEUTRAL)
                        .setTextColor(getResources().getColor(R.color.colorPrimary));

                alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                        .getColor(R.color.transparent));
                alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                        .getColor(android.R.color.holo_red_dark));

            }

        });
        alert11.show();
    }

    private List<Participant> getParticipantIds(Participant participantToRemove) {
        List<Participant> participantList = inbox.getParticipantList();
        GlobalUtils.showLog(TAG, "participants before: " + participantList.size());
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> participantList.remove(participantToRemove));
        GlobalUtils.showLog(TAG, "participants after: " + participantList.size());

        return participantList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PARTICIPANT && resultCode == 2) {
            if (data != null) {
                boolean participantsAdded = data.getBooleanExtra("participant_added",
                        false);
//                List<String> participantIds = data.getStringArrayListExtra("participants");
                if (participantsAdded) {
//                    addParticipantsLocally(participantIds);
                    setParticipants();
                }
            }
        }

        if (requestCode == EDIT_RESULT && resultCode == 2) {
            if (data != null) {
                inbox = InboxRepo.getInstance().getInboxById(inboxId);
                tvAddSubject.setVisibility(View.GONE);
                tvSubject.setVisibility(View.VISIBLE);
                tvSubject.setText(inbox.getSubject());

                if (inbox.getSubject().isEmpty()) {
                    tvAddSubject.setVisibility(View.VISIBLE);
                    tvSubject.setVisibility(View.GONE);
                }

                if (listener != null) listener.onSubjectEdit(inboxId);
            }
        }
    }

    private void addParticipantsLocally(List<String> participantIds) {
        GlobalUtils.showLog(TAG, "add participants locally");
        GlobalUtils.showLog(TAG, "participant list size: " + participantIds.size());
        RealmList<Participant> participantList = new RealmList<>();
        for (String participantId : participantIds
        ) {
            Participant participant = ParticipantRepo.getInstance()
                    .getParticipantByEmployeeId(participantId);
            if (participant != null) {
                GlobalUtils.showLog(TAG, "Participant: " +
                        participant.getParticipantId());
                participantList.add(participant);
            }
        }

        Inbox inbox = InboxRepo.getInstance().getInboxById(inboxId);
        for (Participant participant : inbox.getParticipantList()
        ) {
            if (!participantList.contains(participant)) {
                participantList.add(participant);
            }
        }

        InboxRepo.getInstance().setParticipants(inboxId, participantList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "participants added");
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> setParticipants());
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "error while adding participants");
            }
        });
    }

    private void setParticipants() {
        inbox = InboxRepo.getInstance().getInboxById(inboxId);
        RealmList<Participant> participantList = inbox.getParticipantList();
        if (!CollectionUtils.isEmpty(participantList)) {
            GlobalUtils.showLog(TAG, "participants list not empty");
            rvParticipantName.setVisibility(View.VISIBLE);
            adapter.setData(participantList, inbox.getInboxType());
        } else {
            GlobalUtils.showLog(TAG, "participants empty");
            rvParticipantName.setVisibility(View.GONE);
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

/*    @Override
    public void getThreadByIdSuccess(TicketProto.EmployeeAssigned employeeAssigned) {
        tvAssignEmployee.setVisibility(View.GONE);
        rlAssignEmployee.setVisibility(View.VISIBLE);
        tvAssignEmpLabel.setVisibility(View.VISIBLE);

        tvAssignedEmployee.setText(employeeAssigned.getAssignedTo().getAccount().getFullName());
        String employeeImage = employeeAssigned.getAssignedTo().getAccount().getProfilePic();
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
    }*/

/*    @Override
    public void getThreadByIdFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(), getActivity()
                .getWindow().getDecorView().getRootView(), msg);
    }*/


    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager)
                Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
    }
/*
    private void showConfirmationDialog(String employeeId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Assign to employee?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> {
                    employeeBottomSheet.dismiss();
                    presenter.assignEmployee(inboxId, employeeId);
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
    }*/


    @Override
    public void addParticipantSuccess(String empId) {

    }

    @Override
    public void addParticipantFail(String msg) {

    }

    @Override
    public void getInboxByIdSuccess(TicketProto.EmployeeAssigned employeeAssigned) {

    }

    @Override
    public void getInboxByIdFail(String msg) {

    }

    @Override
    public void deleteParticipantSuccess() {
        inbox = InboxRepo.getInstance().getInboxById(inboxId);
        adapter.setData(inbox.getParticipantList(), inbox.getInboxType());
    }

    @Override
    public void deleteParticipantFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(), Objects.requireNonNull(getActivity())
                .getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onConversationLeaveSuccess() {
//        getActivity().finish();
        Participant toRemove = null;
        Account user = AccountRepo.getInstance().getAccount();
        for (Participant participant : inbox.getParticipantList()
        ) {
            if (participant.getEmployee().getAccountId().equals(user.getAccountId())) {
                toRemove = participant;
            }
        }

        List<Participant> participantList = inbox.getParticipantList();
        participantList.remove(toRemove);
        adapter.setData(participantList, inbox.getInboxType());

    }

    @Override
    public void onConversationLeaveFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(), getActivity()
                .getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onConversationDeleteSuccess() {
        Objects.requireNonNull(getActivity()).finish();
    }

    @Override
    public void onConversationDeleteFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(), Objects.requireNonNull(getActivity())
                .getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onMuteNotificationSuccess() {
        tvMuteSettings.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "Notifications are muted", Toast.LENGTH_SHORT).show();
        muteSheet.dismiss();
    }

    @Override
    public void onMuteNotificationFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(), getActivity()
                .getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onUnMuteSuccess() {
        tvMuteSettings.setVisibility(View.GONE);
    }

    @Override
    public void onUnMuteFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(), getActivity()
                .getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void updateParticipantNotificationSuccess(String participantId, String notificationId) {
        ParticipantRepo.getInstance().changeParticipantNotification(participantId, notificationId);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateParticipantNotificationFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(), getActivity()
                .getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void convertToGroupSuccess() {
        tvAddParticipants.setVisibility(View.VISIBLE);
        adapter.setData(inbox.getParticipantList(), inbox.getInboxType());
        swMute.setVisibility(View.VISIBLE);
        tvLeaveAndDel.setVisibility(View.VISIBLE);
        tvConvertToGroup.setVisibility(View.GONE);
    }

    @Override
    public void convertToGroupFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), msg);
            onAuthorizationFailed(getActivity());
            return;
        }

        UiUtils.showSnackBar(getActivity(), Objects.requireNonNull(getActivity())
                .getWindow().getDecorView().getRootView(), msg);
    }

    public void setOnSubjectChangeListener(OnInboxEditListener listener) {
        this.listener = listener;
    }

}

