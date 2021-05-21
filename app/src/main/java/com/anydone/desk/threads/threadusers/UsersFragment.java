package com.anydone.desk.threads.threadusers;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.button.MaterialButton;
import com.orhanobut.hawk.Hawk;
import com.anydone.desk.R;
import com.anydone.desk.adapters.UsersAdapter;
import com.anydone.desk.base.fragment.BaseFragment;
import com.anydone.desk.injection.component.ApplicationComponent;
import com.anydone.desk.realm.model.Customer;
import com.anydone.desk.realm.repo.CustomerRepo;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.UiUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class UsersFragment extends BaseFragment<UsersPresenterImpl>
        implements UsersContract.UsersView {
    private static final String TAG = "UsersFragment";

    @BindView(R.id.rv_users)
    RecyclerView rvUsers;
    @BindView(R.id.pb_search)
    ProgressBar pbSearch;
    @BindView(R.id.iv_user_not_found)
    ImageView ivUsersNotFound;
    @BindView(R.id.tv_user_not_found)
    TextView tvUserNotFound;
    @BindView(R.id.root)
    CoordinatorLayout root;
    @BindView(R.id.swipe_refresh_users)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_reload)
    MaterialButton btnReload;

    private UsersAdapter usersAdapter;

    @Override
    protected int getLayout() {
        return R.layout.layout_users;
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
        List<Customer> customerList = CustomerRepo.getInstance().getAllCustomers();
        if (!CollectionUtils.isEmpty(customerList)) {
            setUpCustomerRecyclerView(customerList);
            rvUsers.setVisibility(View.VISIBLE);
            tvUserNotFound.setVisibility(View.GONE);
            ivUsersNotFound.setVisibility(View.GONE);
            btnReload.setVisibility(View.GONE);
            etSearch.setVisibility(View.VISIBLE);
        } else presenter.findCustomers(true);

        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh threads called");

                    presenter.findCustomers(false);
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        //Do something after 1 sec
                        if (swipeRefreshLayout != null)
                            swipeRefreshLayout.setRefreshing(false);
                    }, 1000);
                }
        );

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Customer> searchResults = CustomerRepo.getInstance().searchCustomers(s.toString());
                if (searchResults.isEmpty()) {
                    ivUsersNotFound.setVisibility(View.VISIBLE);
                    tvUserNotFound.setVisibility(View.VISIBLE);
                } else {
                    ivUsersNotFound.setVisibility(View.GONE);
                    tvUserNotFound.setVisibility(View.GONE);
                }
                usersAdapter.setData(searchResults);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void findCustomersSuccess() {
        List<Customer> customerList = CustomerRepo.getInstance().getAllCustomers();
        usersAdapter.setData(customerList);
    }

    @Override
    public void findCustomersFail(String msg) {
        if (msg != null && msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

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
        pbSearch.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                Constants.SERVER_ERROR);
    }

    private void setUpCustomerRecyclerView(List<Customer> customerList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvUsers.setLayoutManager(mLayoutManager);

        usersAdapter = new UsersAdapter(customerList, getActivity());
        rvUsers.setAdapter(usersAdapter);

    }
}
