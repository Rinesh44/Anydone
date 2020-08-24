package com.treeleaf.anydone.serviceprovider.tickets;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.addticket.AddTicketActivity;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.tickets.assignedtickets.AssignedTicketsFragment;
import com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets.ClosedTicketsFragment;
import com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets.OnTicketReopenListener;
import com.treeleaf.anydone.serviceprovider.tickets.subscribetickets.SubscribeTicketsFragment;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class TicketsFragment extends BaseFragment<TicketsPresenterImpl>
        implements TicketsContract.TicketsView, OnTicketReopenListener {
    private static final String TAG = "ServiceRequestFragment";
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    /*    @BindView(R.id.bottom_sheet)
        LinearLayout llBottomSheet;*/
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.pb_search)
    ProgressBar pbSearch;
    @BindView(R.id.btn_add_ticket)
    MaterialButton btnAddTicket;

    private List<Tickets> assignedTicketList;
    private List<Tickets> subscribedTicketList;
    private List<Tickets> closedTicketList;
    String statusValue = null;
    private RadioGroup rgStatus;
    private boolean filter = false;
    private BottomSheetDialog filterBottomSheet;
    private HorizontalScrollView hsvStatusContainer;
    private EditText etFromDate, etTillDate;
    private MaterialButton btnSearch;
    private AutoCompleteTextView etSearchText;
    private TextView tvReset;
    final Calendar myCalendar = Calendar.getInstance();
    private AssignedListListener assignedListListener;
    private SubscribedListListener subscribedListListener;
    private ClosedListListener closedListListener;


    @Override
    protected int getLayout() {
        return R.layout.fragment_tickets;
    }

    public static TicketsFragment newInstance(String param1, String param2) {
        TicketsFragment fragment = new TicketsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager
                .LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        assignedTicketList = TicketRepo.getInstance().getAssignedTickets();
        subscribedTicketList = TicketRepo.getInstance().getSubscribedTickets();
        closedTicketList = TicketRepo.getInstance().getClosedResolvedTickets();
        createFilterBottomSheet();

        setupViewPager(mViewpager);
        mTabs.setupWithViewPager(mViewpager);
    }

    private void createFilterBottomSheet() {
        filterBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.layout_bottomsheet_filter_tickets, null);
        filterBottomSheet.setContentView(view);
        btnSearch = view.findViewById(R.id.btn_search);
        etSearchText = view.findViewById(R.id.et_search);
        etFromDate = view.findViewById(R.id.et_from_date);
        etTillDate = view.findViewById(R.id.et_till_date);
        tvReset = view.findViewById(R.id.tv_reset);
        hsvStatusContainer = view.findViewById(R.id.hsv_status_container);


        DatePickerDialog.OnDateSetListener fromDateListener = (view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateFromDate();
        };

        DatePickerDialog.OnDateSetListener tillDateListener = (view1, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateToDate();
        };

        etFromDate.setOnClickListener(v -> new DatePickerDialog(getActivity(), fromDateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        etTillDate.setOnClickListener(v -> new DatePickerDialog(getActivity(), tillDateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        tvReset.setOnClickListener(v -> {
            toggleBottomSheet();
            etSearchText.setText("");
            etFromDate.setText("");
            etTillDate.setText("");
            resetStatus();
            hideKeyBoard();

            Hawk.put(Constants.SELECTED_TICKET_FILTER_STATUS, -1);

            if (mViewpager.getCurrentItem() == 0) {
                if (assignedListListener != null) {
                    assignedListListener.updateAssignedList(assignedTicketList);
                }
            } else if (mViewpager.getCurrentItem() == 1) {
                if (subscribedListListener != null) {
                    subscribedListListener.updateSubscribedList(subscribedTicketList);
                }
            } else {
                if (closedListListener != null) {
                    closedListListener.updateClosedList(closedTicketList);
                }
            }
        });

        etSearchText.setOnItemClickListener((parent, v, position, id) -> hideKeyBoard());

        btnSearch.setOnClickListener(v -> {
            filter = true;
            String fromDate = etFromDate.getText().toString().trim();
            String tillDate = etTillDate.getText().toString().trim();

            long from = 0;
            long to = 0;

            if (!fromDate.isEmpty() && !tillDate.isEmpty()) {
                Calendar calendarFromDate = Calendar.getInstance();
                Calendar calendarTillDate = Calendar.getInstance();
                String[] fromDateSeparated = fromDate.split("/");
                String[] tillDateSeparated = tillDate.split("/");

                calendarFromDate.set(Integer.parseInt(fromDateSeparated[0]),
                        Integer.parseInt(fromDateSeparated[1]) - 1,
                        Integer.parseInt(fromDateSeparated[2]));
                calendarTillDate.set(Integer.parseInt(tillDateSeparated[0]),
                        Integer.parseInt(tillDateSeparated[1]) - 1,
                        Integer.parseInt(tillDateSeparated[2]));
                from = calendarFromDate.getTime().getTime();
                to = calendarTillDate.getTime().getTime();
            }

            Hawk.put(Constants.SELECTED_TICKET_FILTER_STATUS, rgStatus.getCheckedRadioButtonId());
            if (mViewpager.getCurrentItem() == 0) {
                presenter.filterAssignedTickets(etSearchText.getText().toString(), from, to,
                        getTicketState(statusValue.toLowerCase()));
            } else if (mViewpager.getCurrentItem() == 1) {
                presenter.filterSubscribedTickets(etSearchText.getText().toString(), from, to,
                        getTicketState(statusValue.toLowerCase()));
            } else {
                presenter.filterClosedTickets(etSearchText.getText().toString(), from, to,
                        getTicketState(statusValue.toLowerCase()));
            }

            toggleBottomSheet();
        });


        etSearchText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyBoard();
            }
            return false;
        });
    }

    private void updateFromDate() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etFromDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateToDate() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etTillDate.setText(sdf.format(myCalendar.getTime()));
    }

    @OnClick(R.id.btn_add_ticket)
    void addTicket() {
        Intent i = new Intent(getActivity(), AddTicketActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.iv_filter)
    void filterRequests() {
        int fragmentIndex = mViewpager.getCurrentItem();
        if (fragmentIndex == 0) {
            if (!CollectionUtils.isEmpty(assignedTicketList)) {
                @SuppressLint("InflateParams") View statusView = getLayoutInflater()
                        .inflate(R.layout.layout_status_buttons_assigned, null);
                rgStatus = statusView.findViewById(R.id.rg_status);

                rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
                    RadioButton selectedRadioButton = group.findViewById(checkedId);
                    //highlight selected button and disable unselected
                    int count = group.getChildCount();
                    for (int i = 0; i < count; i++) {
                        RadioButton rb = (RadioButton) group.getChildAt(i);
                        rb.setBackground(getResources().getDrawable(R.drawable.round_line_inactive));
                        rb.setTextColor(getResources().getColor(R.color.grey));
                    }

                    selectedRadioButton.setBackground(getResources()
                            .getDrawable(R.drawable.round_line_active));
                    selectedRadioButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                    statusValue = selectedRadioButton.getText().toString().trim();
                });

                hsvStatusContainer.removeAllViews();
                hsvStatusContainer.addView(rgStatus);
            }
        } else if (fragmentIndex == 1) {
            if (!CollectionUtils.isEmpty(subscribedTicketList)) {
                @SuppressLint("InflateParams") View statusView = getLayoutInflater()
                        .inflate(R.layout.layout_status_buttons_alternate, null);
                rgStatus = statusView.findViewById(R.id.rg_status);

                rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
                    RadioButton selectedRadioButton = group.findViewById(checkedId);

                    //highlight selected button and disable unselected
                    int count = group.getChildCount();
                    for (int i = 0; i < count; i++) {
                        RadioButton rb = (RadioButton) group.getChildAt(i);
                        rb.setBackground(getResources().getDrawable(R.drawable.round_line_inactive));
                        rb.setTextColor(getResources().getColor(R.color.grey));
                    }

                    selectedRadioButton.setBackground(getResources()
                            .getDrawable(R.drawable.round_line_active));
                    selectedRadioButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                    statusValue = selectedRadioButton.getText().toString().trim();
                });

                hsvStatusContainer.removeAllViews();
                hsvStatusContainer.addView(rgStatus);
            }
        } else {
            if (!CollectionUtils.isEmpty(closedTicketList)) {
                @SuppressLint("InflateParams") View statusView = getLayoutInflater()
                        .inflate(R.layout.layout_status_buttons_closed, null);
                rgStatus = statusView.findViewById(R.id.rg_status);

                rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
                    RadioButton selectedRadioButton = group.findViewById(checkedId);

                    //highlight selected button and disable unselected
                    int count = group.getChildCount();
                    for (int i = 0; i < count; i++) {
                        RadioButton rb = (RadioButton) group.getChildAt(i);
                        rb.setBackground(getResources().getDrawable(R.drawable.round_line_inactive));
                        rb.setTextColor(getResources().getColor(R.color.grey));
                    }

                    selectedRadioButton.setBackground(getResources()
                            .getDrawable(R.drawable.round_line_active));
                    selectedRadioButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                    statusValue = selectedRadioButton.getText().toString().trim();
                });

                hsvStatusContainer.removeAllViews();
                hsvStatusContainer.addView(rgStatus);
            }
        }
        GlobalUtils.showLog(TAG, "fragment index: " + fragmentIndex);
        toggleBottomSheet();
    }


    private int getTicketState(String statusValue) {
        switch (statusValue.toLowerCase()) {
            case "started":
                return TicketProto.TicketState.TICKET_STARTED.getNumber();

            case "todo":
                return TicketProto.TicketState.TICKET_CREATED.getNumber();

            case "reopened":
                return TicketProto.TicketState.TICKET_REOPENED.getNumber();

            case "resolved":
                return TicketProto.TicketState.TICKET_RESOLVED.getNumber();

            case "closed":
                return TicketProto.TicketState.TICKET_CLOSED.getNumber();

            default:
                break;
        }
        return -1;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_filter, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        UiUtils.hideKeyboardForced(getContext());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }


    /**
     * manually opening / closing bottom sheet on button click
     */
    public void toggleBottomSheet() {
        if (filterBottomSheet.isShowing()) filterBottomSheet.hide();
        else {
            int selectedRadioBtn = Hawk.get(Constants.SELECTED_TICKET_FILTER_STATUS, -1);
            if (selectedRadioBtn != -1) {
                int rgCount = rgStatus.getChildCount();
                for (int i = 0; i < rgCount; i++) {
                    RadioButton button = (RadioButton) rgStatus.getChildAt(i);
                    if (button.getId() == selectedRadioBtn) {
                        button.setChecked(true);
                        button.setBackground(getResources().getDrawable(R.drawable.round_line_active));
                        button.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
            }
            filterBottomSheet.show();
        }
    }

    private void resetStatus() {
        int rgCount = rgStatus.getChildCount();
        for (int i = 0; i < rgCount; i++) {
            statusValue = "null";
            RadioButton button = (RadioButton) rgStatus.getChildAt(i);
            button.setChecked(false);
            button.setBackground(getResources().getDrawable(R.drawable.round_line_inactive));
            button.setTextColor(getResources().getColor(R.color.grey));
        }
    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager)
                Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
    }

    @Override
    public void showProgressBar(String message) {
        pbSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (pbSearch != null) {
            pbSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                message);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new AssignedTicketsFragment(), "Assigned");
        viewPagerAdapter.addFragment(new SubscribeTicketsFragment(), "Subscribed");
        viewPagerAdapter.addFragment(new ClosedTicketsFragment(), "Closed/Resolved");
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void updateAssignedTicketList(List<Tickets> ticketsList) {
        if (assignedListListener != null) {
            assignedListListener.updateAssignedList(ticketsList);
        } else {
            GlobalUtils.showLog(TAG, "assigned list listener null");
        }
    }

    @Override
    public void filterAssignedTicketsFailed(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
        List<Tickets> emptyList = new ArrayList<>();
        assignedListListener.updateAssignedList(emptyList);
    }

    @Override
    public void updateSubscribedTicketList(List<Tickets> ticketsList) {
        if (subscribedListListener != null) {
            subscribedListListener.updateSubscribedList(ticketsList);
        } else {
            GlobalUtils.showLog(TAG, "subscribed list listener null");
        }
    }

    @Override
    public void filterSubscribedTicketFailed(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
        List<Tickets> emptyList = new ArrayList<>();
        subscribedListListener.updateSubscribedList(emptyList);
    }

    @Override
    public void updateClosedTicketList(List<Tickets> ticketsList) {
        if (closedListListener != null) {
            closedListListener.updateClosedList(ticketsList);
        } else {
            GlobalUtils.showLog(TAG, "closed list listener null");
        }
    }

    @Override
    public void filterClosedTicketFailed(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
        List<Tickets> emptyList = new ArrayList<>();
        closedListListener.updateClosedList(emptyList);
    }

    @Override
    public void ticketReopened() {
        GlobalUtils.showLog(TAG, "on ticket reopened interface implemented");
        if (assignedListListener != null) {
            assignedListListener.updateAssignedList(assignedTicketList);
        } else {
            GlobalUtils.showLog(TAG, "assinged list listnner is null");
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public interface AssignedListListener {
        void updateAssignedList(List<Tickets> ticketsList);
    }

    public void setAssignedListListener(AssignedListListener listener) {
        assignedListListener = listener;
    }

    public interface SubscribedListListener {
        void updateSubscribedList(List<Tickets> ticketsList);
    }

    public void setSubscribedListListener(SubscribedListListener listener) {
        subscribedListListener = listener;
    }

    public interface ClosedListListener {
        void updateClosedList(List<Tickets> ticketsList);
    }

    public void setClosedListListener(ClosedListListener listener) {
        closedListListener = listener;
    }
}

