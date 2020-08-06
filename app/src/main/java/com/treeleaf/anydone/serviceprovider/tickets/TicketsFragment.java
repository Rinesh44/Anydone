package com.treeleaf.anydone.serviceprovider.tickets;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.treeleaf.anydone.entities.OrderServiceProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.servicerequests.OnSwipeListener;
import com.treeleaf.anydone.serviceprovider.tickets.alltickets.AllTicketsFragment;
import com.treeleaf.anydone.serviceprovider.tickets.alltickets.OnAllTicketsListener;
import com.treeleaf.anydone.serviceprovider.tickets.subscribetickets.OnSubscribeTicketsListener;
import com.treeleaf.anydone.serviceprovider.tickets.subscribetickets.SubscribeTicketsFragment;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class TicketsFragment extends BaseFragment<TicketsPresenterImpl>
        implements TicketsContract.TicketsView, OnSwipeListener,
        OnSubscribeTicketsListener, OnAllTicketsListener {
    private static final String TAG = "ServiceRequestFragment";
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.bottom_sheet)
    LinearLayout llBottomSheet;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.pb_search)
    ProgressBar pbSearch;

    private List<Tickets> ticketsList;
    private String statusValue = "null";
    private SubscribeTicketListListener subscribeTicketListListener;
    private AllTicketsListListener allTicketsListListener;
    private List<Tickets> subscribeTickets;
    private List<Tickets> allTickets;
    private RadioGroup rgStatus;
    private boolean filter = false;
    private BottomSheetDialog filterBottomSheet;
    private HorizontalScrollView hsvStatusContainer;
    private EditText etFromDate, etTillDate;
    private MaterialButton btnSearch;
    private AutoCompleteTextView etServiceName;
    private TextView tvReset;

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

        ticketsList = TicketRepo.getInstance().getAllTickets();
        createFilterBottomSheet();
        if (CollectionUtils.isEmpty(ticketsList)) {
            presenter.getTickets(true);
        } else {
            ivFilter.setVisibility(View.VISIBLE);
            int fragmentIndex = mViewpager.getCurrentItem();
            presenter.separateSubscribeAndAllTickets(ticketsList, fragmentIndex, filter);
        }

        setupViewPager(mViewpager);
        mTabs.setupWithViewPager(mViewpager);
    }

    private void createFilterBottomSheet() {
        filterBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.filter_bottom_sheet_layout, null);
        filterBottomSheet.setContentView(view);
        btnSearch = view.findViewById(R.id.btn_search);
        etServiceName = view.findViewById(R.id.et_service_name);
        etFromDate = view.findViewById(R.id.et_from_date);
        etTillDate = view.findViewById(R.id.et_till_date);
        tvReset = view.findViewById(R.id.tv_reset);
        hsvStatusContainer = view.findViewById(R.id.hsv_status_container);

        tvReset.setOnClickListener(v -> {
            toggleBottomSheet();
            etServiceName.setText("");
            etFromDate.setText("");
            etTillDate.setText("");
            resetStatus();
            hideKeyBoard();

            Hawk.put(Constants.SELECTED_FILTER_STATUS, -1);
            presenter.getTickets(true);
        });

        etServiceName.setOnItemClickListener((parent, v, position, id) -> hideKeyBoard());

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

            Hawk.put(Constants.SELECTED_FILTER_STATUS, rgStatus.getCheckedRadioButtonId());
       /*     presenter.filterServiceRequests(etServiceName.getText().toString(), from, to,
                    getOrderState(statusValue.toLowerCase()));*/
            toggleBottomSheet();
        });


        etServiceName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyBoard();
            }
            return false;
        });
    }


    @OnClick(R.id.iv_filter)
    void filterRequests() {
        if (!CollectionUtils.isEmpty(ticketsList)) {
            int fragmentIndex = mViewpager.getCurrentItem();
            if (fragmentIndex == 0) {
                @SuppressLint("InflateParams") View statusView = getLayoutInflater()
                        .inflate(R.layout.layout_status_buttons_ongoing, null);
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
            } else {
                @SuppressLint("InflateParams") View statusView = getLayoutInflater()
                        .inflate(R.layout.layout_status_button_closed, null);
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
            GlobalUtils.showLog(TAG, "fragment index: " + fragmentIndex);
        } else {
            UiUtils.showSnackBar(getContext(), Objects.requireNonNull(getActivity()).getWindow()
                            .getDecorView().getRootView(),
                    "No service requests found to filter");
        }
//        toggleBottomSheet();
    }


    private String getOrderState(String statusValue) {
        switch (statusValue) {
            case "pending":
                return OrderServiceProto.ServiceOrderState.PENDING_SERVICE_ORDER.name();

            case "accepted":
                return OrderServiceProto.ServiceOrderState.ACCEPTED_SERVICE_ORDER.name();

            case "completed":
                return OrderServiceProto.ServiceOrderState.COMPLETED_SERVICE_ORDER.name();

            case "cancelled":
                return OrderServiceProto.ServiceOrderState.CANCELLED_SERVICE_ORDER.name();

            case "started":
                return OrderServiceProto.ServiceOrderState.STARTED_SERVICE_ORDER.name();

            case "closed":
                return OrderServiceProto.ServiceOrderState.CLOSED_SERVICE_ORDER.name();

            default:
                break;
        }
        return "";
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
            int selectedRadioBtn = Hawk.get(Constants.SELECTED_FILTER_STATUS, -1);
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
        viewPagerAdapter.addFragment(new SubscribeTicketsFragment(), "Subscribe");
        viewPagerAdapter.addFragment(new AllTicketsFragment(), "All");
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onSwipeRefresh() {
        GlobalUtils.showLog(TAG, "swipe implemented");
        presenter.getTickets(false);
    }


    @Override
    public void getTicketsSuccess() {

    }

    @Override
    public void getTicketsFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        hideKeyBoard();
        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
    }

    @Override
    public void onSubscribeTicketsSeparated(List<Tickets> subscribeTicketList) {
        GlobalUtils.showLog(TAG, "subsribe tickets count: " + subscribeTicketList.size());
        subscribeTickets = subscribeTicketList;
        if (subscribeTicketListListener != null)
            subscribeTicketListListener.showSubscribeTickets(subscribeTickets);
        else GlobalUtils.showLog(TAG, "subscribe listener is null");
    }

    @Override
    public void onAllTicketsSeparated(List<Tickets> allTicketList) {
        GlobalUtils.showLog(TAG, "all tickets count: " + allTicketList.size());
        allTickets = allTicketList;
        if (allTicketsListListener != null)
            allTicketsListListener.showAllTickets(allTickets);
        else GlobalUtils.showLog(TAG, "all ticket listener is null");
    }

    @Override
    public void onAllTicketsCreated() {
        GlobalUtils.showLog(TAG, "on all ticket fragment created called");
        if (allTicketsListListener != null)
            allTicketsListListener.showAllTickets(allTickets);
        else GlobalUtils.showLog(TAG, "all ticket listener is null");
    }

    @Override
    public void onSubscribeTicketFragmentCreated() {
        GlobalUtils.showLog(TAG, "on subscribe ticket fragment created called");
        if (subscribeTicketListListener != null)
            subscribeTicketListListener.showSubscribeTickets(subscribeTickets);
        else GlobalUtils.showLog(TAG, "subscribe ticket listener is null");
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

    public interface SubscribeTicketListListener {
        void showSubscribeTickets(List<Tickets> subscribeTicketList);
    }

    public void setSubscribeTicketListListener(SubscribeTicketListListener listener) {
        subscribeTicketListListener = listener;
    }

    public interface AllTicketsListListener {
        void showAllTickets(List<Tickets> allTicketsList);
    }

    public void setAllTicketListListener(AllTicketsListListener listener) {
        allTicketsListListener = listener;
    }


}

