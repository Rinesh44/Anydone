package com.treeleaf.anydone.serviceprovider.inbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.SearchServiceAdapter;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.repo.AvailableServicesRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class InboxFragment extends BaseFragment<InboxPresenterImpl> implements
        InboxContract.InboxView {
    private static final String TAG = "InboxFragment";

    @BindView(R.id.pb_search)
    ProgressBar pbSearch;
    @BindView(R.id.iv_inbox_not_found)
    ImageView ivInboxNotFound;
    @BindView(R.id.rv_inbox)
    RecyclerView rvInbox;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_service)
    ImageView ivService;
    @BindView(R.id.swipe_refresh_inbox)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_reload)
    MaterialButton btnReload;

    private RecyclerView rvServices;
    private SearchServiceAdapter adapter;
    private BottomSheetDialog serviceSheet;


    public static InboxFragment newInstance(String param1, String param2) {
        InboxFragment fragment = new InboxFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayout() {
        return R.layout.layout_inbox;
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager
                .LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
/*        List<Thread> threadList = ThreadRepo.getInstance().getThreadsByServiceId(selectedService);
        if (!CollectionUtils.isEmpty(threadList)) {
            setUpThreadRecyclerView(threadList);
            rvThreads.setVisibility(View.VISIBLE);
            ivThreadNotFound.setVisibility(View.GONE);
            btnReload.setVisibility(View.GONE);
            etSearch.setVisibility(View.VISIBLE);
        } else presenter.getConversationThreads(true);*/

        createServiceBottomSheet();
        tvToolbarTitle.setOnClickListener(v -> {
            serviceSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            toggleServiceBottomSheet();
        });

        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh inbox called");

//                    presenter.getConversationThreads(false);
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        //Do something after 1 sec
                        if (swipeRefreshLayout != null)
                            swipeRefreshLayout.setRefreshing(false);
                    }, 1000);
                }
        );

/*        try {
            listenConversationMessages();
        } catch (MqttException e) {
            e.printStackTrace();
        }*/

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            /*    List<Thread> searchResults = ThreadRepo.getInstance().searchThread(s.toString());
                if (searchResults.isEmpty()) {
                    ivThreadNotFound.setVisibility(View.VISIBLE);
                } else {
                    ivThreadNotFound.setVisibility(View.GONE);
                }
                threadAdapter.setData(searchResults);*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void toggleServiceBottomSheet() {
        if (serviceSheet.isShowing()) {
            serviceSheet.dismiss();
        } else {
            serviceSheet.show();
        }
    }

    @Override
    public void getServicesSuccess() {
        List<Service> serviceList = AvailableServicesRepo.getInstance().getAvailableServices();
        Service firstService = serviceList.get(0);
        Hawk.put(Constants.SELECTED_SERVICE, firstService.getServiceId());
        GlobalUtils.showLog(TAG, "first service id saved");

        tvToolbarTitle.setText(firstService.getName().replace("_", " "));

     /*   RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_browse_service)
                .error(R.drawable.ic_browse_service);*/

        Glide.with(Objects.requireNonNull(getContext()))
                .load(firstService.getServiceIconUrl())
                .placeholder(R.drawable.ic_service_ph)
                .error(R.drawable.ic_service_ph)
//                .apply(options)
                .into(ivService);

        setUpServiceRecyclerView(serviceList);
    }

    @Override
    public void getServicesFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                msg);
    }

    @Override
    public void getInboxMessageSuccess() {

    }

    @Override
    public void getInboxMessageFail(String msg) {

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
                Constants.SERVER_ERROR);

        ivInboxNotFound.setVisibility(View.VISIBLE);
        btnReload.setVisibility(View.VISIBLE);
    }

    private void createServiceBottomSheet() {
        serviceSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View llBottomSheet = getLayoutInflater()
                .inflate(R.layout.bottomsheet_select_service, null);

        serviceSheet.setContentView(llBottomSheet);
        serviceSheet.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        serviceSheet.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById
                    (com.google.android.material.R.id.design_bottom_sheet);
 /*           if (bottomSheet != null)
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);*/
            setupSheetHeight(d, BottomSheetBehavior.STATE_HALF_EXPANDED);
        });

        EditText searchService = llBottomSheet.findViewById(R.id.et_search_service);
        rvServices = llBottomSheet.findViewById(R.id.rv_services);

        searchService.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                setupSheetHeight(serviceSheet, BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        serviceSheet.setOnDismissListener(dialog -> {
            UiUtils.hideKeyboardForced(Objects.requireNonNull(getActivity()));
            searchService.clearFocus();
        });


        List<Service> serviceList = AvailableServicesRepo.getInstance().getAvailableServices();
        if (CollectionUtils.isEmpty(serviceList)) {
            presenter.getServices();
        } else {
            String selectedServiceId = Hawk.get(Constants.SELECTED_SERVICE);
            if (selectedServiceId == null) {
                Service firstService = serviceList.get(0);
                tvToolbarTitle.setText(firstService.getName().replace("_", " "));
                Glide.with(Objects.requireNonNull(getContext()))
                        .load(firstService.getServiceIconUrl())
                        .placeholder(R.drawable.ic_service_ph)
                        .error(R.drawable.ic_service_ph)
                        .into(ivService);
                Hawk.put(Constants.SELECTED_SERVICE, firstService.getServiceId());
            } else {
                Service selectedService = AvailableServicesRepo.getInstance()
                        .getAvailableServiceById(selectedServiceId);
                tvToolbarTitle.setText(selectedService.getName().replace("_", " "));
                Glide.with(Objects.requireNonNull(getContext()))
                        .load(selectedService.getServiceIconUrl())
                        .placeholder(R.drawable.ic_service_ph)
                        .error(R.drawable.ic_service_ph)
                        .into(ivService);
            }
            setUpServiceRecyclerView(serviceList);
        }


        searchService.addTextChangedListener(new TextWatcher() {
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


    private void setUpServiceRecyclerView(List<Service> serviceList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvServices.setLayoutManager(mLayoutManager);

        adapter = new SearchServiceAdapter(serviceList, getActivity());
        rvServices.setAdapter(adapter);

        adapter.setOnItemClickListener(service -> {
            hideKeyBoard();
            Hawk.put(Constants.SELECTED_SERVICE, service.getServiceId());
//            Hawk.put(Constants.SERVICE_CHANGED_DASHBOARD, true);
            tvToolbarTitle.setText(service.getName().replace("_", " "));

       /*     RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_browse_service)
                    .error(R.drawable.ic_browse_service);*/

            Glide.with(Objects.requireNonNull(getContext()))
                    .load(service.getServiceIconUrl())
                    .placeholder(R.drawable.ic_service_ph)
                    .error(R.drawable.ic_service_ph)
//                    .apply(options)
                    .into(ivService);

            serviceSheet.dismiss();

            ivInboxNotFound.setVisibility(View.GONE);
            btnReload.setVisibility(View.GONE);
//            presenter.getTicketSuggestions();
        });
    }

    private void hideKeyBoard() {
        final InputMethodManager imm = (InputMethodManager)
                Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
    }

    @OnClick(R.id.btn_reload)
    void reload() {
        btnReload.setVisibility(View.GONE);
        ivInboxNotFound.setVisibility(View.GONE);
//        presenter.getConversationThreads(true);
    }
}
