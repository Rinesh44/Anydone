package com.treeleaf.anydone.serviceprovider.creategroup;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.entities.InboxProto;
import com.treeleaf.anydone.rpc.InboxRpcProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.ParticipantSelectionAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.SearchContributorAdapter;
import com.treeleaf.anydone.serviceprovider.adapters.SubjectSearchAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.inboxdetails.InboxDetailActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;
import com.treeleaf.anydone.serviceprovider.realm.repo.InboxRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ProtoMapper;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Retrofit;

public class CreateGroupActivity extends MvpBaseActivity<CreateGroupPresenterImpl> implements
        CreateGroupContract.CreateGroupView {

    private static final String TAG = "CreateGroupActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    /*    @BindView(R.id.tv_create_group)
        TextView tvCreateGroup;*/
    @BindView(R.id.et_to)
    EditText etSearchEmployee;
    @BindView(R.id.rv_employees)
    RecyclerView rvEmployees;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.et_subject)
    EditText etSubject;
    @BindView(R.id.et_message)
    TextInputEditText etMessage;
    @BindView(R.id.iv_send)
    ImageView ivSend;
    @BindView(R.id.fbl_participants)
    FlexboxLayout fblParticipants;
    @BindView(R.id.rv_selected_participants)
    RecyclerView rvSelectedParticipants;
    @BindView(R.id.rl_bottom_bar)
    RelativeLayout rlBottomBar;
    @BindView(R.id.btn_create_group)
    MaterialButton btnCreateGroup;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @BindView(R.id.sw_private)
    Switch swMakePrivate;
    @BindView(R.id.ll_search_container)
    LinearLayout llSearchContainer;
    @BindView(R.id.rv_subjects)
    RecyclerView rvSubjects;


    private ProgressDialog progress;
    List<String> employeeIds = new ArrayList<>();
    private SearchContributorAdapter adapter;
    private SubjectSearchAdapter subjectSearchAdapter;
    private String inboxId;
    private ParticipantSelectionAdapter selectedParticipantAdapter;
    private boolean isGroup = false;
    private boolean isPrivate = false;
    private List<String> searchedInboxIds = new ArrayList<>();
    Disposable disposable = new CompositeDisposable();
    private String searchedText;

    @Override
    protected int getLayout() {
        return R.layout.activity_create_group;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        inboxId = i.getStringExtra("inbox_id");
        isGroup = i.getBooleanExtra("group", false);
        Inbox inbox = InboxRepo.getInstance().getInboxById(inboxId);

        if (isGroup) {
//            tvCreateGroup.setText("Create Group");
            etSubject.setHint("Group name");
            tvToolbarTitle.setText("New Group");
            rlBottomBar.setVisibility(View.VISIBLE);
            llSearchContainer.setVisibility(View.GONE);
        } else {
//            tvCreateGroup.setText("Create Thread");
            rlBottomBar.setVisibility(View.GONE);
            llSearchContainer.setVisibility(View.VISIBLE);
        }

        presenter.findParticipants();
        ivBack.setOnClickListener(v -> onBackPressed());
        ivSend.setEnabled(false);

/*        tvCreateGroup.setOnClickListener(v -> {
            if (employeeIds.isEmpty()) {
                Toast.makeText(this,
                        "Please select participant to add", Toast.LENGTH_SHORT).show();
            } else {
                presenter.createGroup(employeeIds, Objects.requireNonNull(etMessage.getText()).toString(),
                        etSubject.getText().toString(), isGroup);
            }
        });*/

        btnCreateGroup.setOnClickListener(v -> {
            if (employeeIds.isEmpty()) {
                Toast.makeText(this,
                        "Please select participant to add", Toast.LENGTH_SHORT).show();
            } else {
                if (isGroup && etSubject.getText().toString().isEmpty()) {
                    Toast.makeText(this,
                            "Please enter group name", Toast.LENGTH_SHORT).show();
                    return;
                }
                presenter.createGroup(employeeIds, Objects.requireNonNull(etMessage.getText()).toString(),
                        etSubject.getText().toString(), isGroup, isPrivate);
            }
        });

        ivSend.setOnClickListener(v ->
        {
            if (employeeIds.isEmpty()) {
                Toast.makeText(this,
                        "Please select participant to add", Toast.LENGTH_SHORT).show();
            } else {
                presenter.createGroup(employeeIds, Objects.requireNonNull(etMessage.getText()).toString(),
                        etSubject.getText().toString(), isGroup, isPrivate);
            }
        });

        setUpSelectedParticipantAdapter();
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ivSend.setImageTintList(AppCompatResources.getColorStateList
                            (Objects.requireNonNull(getContext()), R.color.colorPrimary));
                    ivSend.setEnabled(true);
                } else {
                    ivSend.setImageTintList(AppCompatResources.getColorStateList
                            (Objects.requireNonNull(getContext()), R.color.selector_disabled));
                    ivSend.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        rvSubjects.setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager)
                    Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return false;
        });

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

        swMakePrivate.setOnCheckedChangeListener((buttonView, isChecked) -> isPrivate = isChecked);

        UiUtils.showKeyboard(this, etSearchEmployee);
        etSubject.requestFocus();

/*        etSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    rvSubjects.setVisibility(View.GONE);
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvSubjects.setVisibility(View.VISIBLE);
                        }
                    }, 1000);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        setupSubjectSearchRecyclerView();
        observeSearchView();
    }


    private void setupSubjectSearchRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvSubjects.setLayoutManager(mLayoutManager);

        List<Inbox> inboxList = new ArrayList<>();
        subjectSearchAdapter = new SubjectSearchAdapter(inboxList, this);
        rvSubjects.setAdapter(subjectSearchAdapter);

        subjectSearchAdapter.setOnItemClickListener(inbox -> {
            Intent i = new Intent(CreateGroupActivity.this, InboxDetailActivity.class);
            i.putExtra("inbox_id", inbox.getInboxId());
            startActivity(i);
            finish();
        });
    }

    public Observable<String> fromView(EditText searchView) {
        final PublishSubject<String> subject = PublishSubject.create();

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                GlobalUtils.showLog(TAG, "from view: " + s.toString());

//                subject.onNext(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
//                subject.onComplete();
                subject.onNext(s.toString());
            }
        });

        return subject;
    }

    private void observeSearchView() {
        disposable = fromView(etSubject)
                .map(s -> s.toLowerCase().trim())
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .flatMap((Function<String, ObservableSource<InboxRpcProto.InboxBaseResponse>>)
                        query -> {
                            searchedText = query;

                     /*       if (query.isEmpty()) {
                                return Observable.error(new Throwable("empty query"));
                            }*/

                            runOnUiThread(() -> {
                                if (query.length() > 0) {
                                    rvSubjects.setVisibility(View.VISIBLE);
                                } else {
                                    rvSubjects.setVisibility(View.GONE);
                                }
                            });

                            return findExistingSubjects(query);
                        })
                .onErrorResumeNext(Observable.empty())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<InboxRpcProto.InboxBaseResponse>() {
                    @Override
                    public void onNext(@NonNull InboxRpcProto.InboxBaseResponse o) {
                        GlobalUtils.showLog("TAG", "search subject: " + o);
                        GlobalUtils.showLog("TAG", "search list size: " + o.getInboxResponse().getInboxList().size());
                        List<Inbox> searchedList = ProtoMapper.transformInbox(o.getInboxResponse().getInboxList());
                        GlobalUtils.showLog(TAG, "converted list size: " + searchedList.size());
                        searchedInboxIds.clear();
                        for (Inbox inbox : searchedList
                        ) {
                            searchedInboxIds.add(inbox.getInboxId());
                        }

                /*        if (searchedText.length() > 0) {
                            rvSubjects.setVisibility(View.VISIBLE);
                        } else {
                            rvSubjects.setVisibility(View.GONE);
                        }*/

                        GlobalUtils.showLog(TAG, "inbox ids " + searchedInboxIds.size());

                        saveInboxList(o.getInboxResponse().getInboxList());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        GlobalUtils.showLog(TAG, "on error: " + e.getLocalizedMessage());

                     /*   if (e.getLocalizedMessage().equalsIgnoreCase("empty query")) {
                            Toast.makeText(CreateGroupActivity.this, "Not found", Toast.LENGTH_SHORT).show();
                        }*/
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public Observable<InboxRpcProto.InboxBaseResponse> findExistingSubjects(String query) {
        GlobalUtils.showLog(TAG, "search inbox called()");
//        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<InboxRpcProto.InboxBaseResponse> inboxObservable;
        String token = Hawk.get(Constants.TOKEN);

        inboxObservable = service.getExistingSubject(token, query);

        return inboxObservable;
    }

    private void saveInboxList(List<InboxProto.Inbox> inboxList) {
        InboxRepo.getInstance().saveInboxes(inboxList, true, new Repo.Callback() {
            @Override
            public void success(Object o) {
                fetchSearchedListFromDb();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG,
                        "error on saving inbox list");
            }
        });
    }

    private void fetchSearchedListFromDb() {
        List<Inbox> searchedInbox = new ArrayList<>();
        for (String inboxId : searchedInboxIds
        ) {
            Inbox inbox = InboxRepo.getInstance().getInboxById(inboxId);
            searchedInbox.add(inbox);
        }

        Inbox selfInbox = InboxRepo.getInstance().getSelfInbox();
        searchedInbox.remove(selfInbox);
        GlobalUtils.showLog(TAG, "searched list from db: " + searchedInbox.size());
        GlobalUtils.showLog(TAG, "searched text check: " + searchedText);
        subjectSearchAdapter.setData(searchedInbox, searchedText);
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
                disableCreateGroup();
            }

            addParticipantToRecyclerView();
            adapter.setData(employeeIds);
        });
    }

    private void setUpRecyclerView(List<AssignEmployee> assignEmployeeList) {
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
                    enableCreateGroup();
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
                    disableCreateGroup();
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
    public void createGroupSuccess() {
        finish();
    }

    @Override
    public void createGroupFail(String msg) {
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
   /*     Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();*/

    }

    @Override
    public void getSubjectSuccess(List<Inbox> subjectResults) {
//        subjectSearchAdapter.setData(subjectResults);
    }

    @Override
    public void getSubjectFail(String msg) {
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

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private void enableCreateGroup() {
  /*      tvCreateGroup.setClickable(true);
        tvCreateGroup.setTextColor(tvCreateGroup.getContext()
                .getResources().getColor(R.color.colorPrimary));*/

        btnCreateGroup.setEnabled(true);
        btnCreateGroup.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));

    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private void disableCreateGroup() {
   /*     tvCreateGroup.setClickable(false);
        tvCreateGroup.setTextColor(tvCreateGroup.getContext()
                .getResources().getColor(R.color.btn_disabled));*/

        btnCreateGroup.setEnabled(false);
        btnCreateGroup.setBackgroundTintList(getResources().getColorStateList(R.color.btn_disabled));

    }
}