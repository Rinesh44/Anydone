package com.treeleaf.anydone.serviceprovider.setting.location.showLocation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.button.MaterialButton;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.LocationAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Location;
import com.treeleaf.anydone.serviceprovider.realm.repo.LocationRepo;
import com.treeleaf.anydone.serviceprovider.setting.location.AddLocationActivity;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class ShowLocationActivity extends MvpBaseActivity<ShowLocationPresenterImpl>
        implements ShowLocationContract.ShowLocationView {
    @BindView(R.id.btn_add_location)
    MaterialButton btnAddLocation;
    @BindView(R.id.rv_locations)
    RecyclerView rvLocations;
    @BindView(R.id.rl_location_view)
    RelativeLayout rlLocationView;
    @BindView(R.id.rl_empty_view)
    RelativeLayout rlEmptyView;
    @BindView(R.id.tv_add_location)
    TextView tvAddLocation;

    private LocationAdapter adapter;
    private ProgressDialog progressBar;

    @Override
    protected int getLayout() {
        return R.layout.activity_location;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbar();
        List<Location> locationList = LocationRepo.getInstance().getAllLocation();
        setUpRecyclerView(locationList);

        btnAddLocation.setOnClickListener(v ->
                startActivity(new Intent(
                        ShowLocationActivity.this, AddLocationActivity.class)));

        tvAddLocation.setOnClickListener(v -> startActivity(new Intent(
                ShowLocationActivity.this, AddLocationActivity.class)));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Location");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    private void setUpRecyclerView(List<Location> locationList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvLocations.setLayoutManager(mLayoutManager);

        if (!CollectionUtils.isEmpty(locationList)) {
            rlLocationView.setVisibility(View.VISIBLE);
            btnAddLocation.setVisibility(View.GONE);
            rlEmptyView.setVisibility(View.GONE);
            adapter = new LocationAdapter(locationList);
            rvLocations.setAdapter(adapter);
        } else {
            rlLocationView.setVisibility(View.GONE);
            rlEmptyView.setVisibility(View.VISIBLE);
            btnAddLocation.setVisibility(View.VISIBLE);
        }


        if (adapter != null) {
            adapter.setOnPrimaryListener((id, pos) -> presenter.makeLocationDefault(id));

            adapter.setOnDeleteListener(this::showDeleteDialog);
        }
    }

    private void showDeleteDialog(String locationId, int pos) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure you want to delete?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> {
                    presenter.deleteLocation(locationId, pos);
                    dialog.dismiss();
                });

        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> {
                    adapter.closeSwipeLayout(locationId);
                    dialog.dismiss();
                });


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

    @Override
    protected void onResume() {
        super.onResume();
        List<Location> locationList = LocationRepo.getInstance().getAllLocation();
        setUpRecyclerView(locationList);
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void onMakeLocationDefualtSuccess(String id) {
        adapter.closeSwipeLayout(id);
        LocationRepo.getInstance().removeLocationAsPrimary();
        LocationRepo.getInstance().setLocationAsPrimary(id);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMakeLocationDefaultFail(String msg) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onDeleteLocationSuccess(String locationId, int pos) {
        adapter.deleteItem(locationId, pos);
        setUpRecyclerView(LocationRepo.getInstance().getAllLocation());
    }

    @Override
    public void onDeleteLocationFail(String msg) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void showProgressBar(String message) {
        progressBar = ProgressDialog.show(this, null, message, true);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (progressBar != null) {
            progressBar.cancel();
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                message);
    }

    @Override
    public Context getContext() {
        return this;
    }
}