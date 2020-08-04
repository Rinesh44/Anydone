package com.treeleaf.anydone.serviceprovider.picklocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.LocationAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Location;
import com.treeleaf.anydone.serviceprovider.realm.repo.LocationRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class PickLocationActivity extends MvpBaseActivity<PickLocationPresenterImpl> implements
        PickLocationContract.PickLocationView {
    private static final int PLACE_SELECTION_REQUEST_CODE = 45672;
    private static final String TAG = "PickLocationActivity";
    @BindView(R.id.rv_select_location)
    RecyclerView rvPickLocation;
    @BindView(R.id.et_search_location)
    EditText etSearchLocation;
    @BindView(R.id.iv_pick_location)
    ImageView ivPickLocation;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    LocationAdapter adapter;
    private android.location.Location userLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private List<Location> locationList;


    @Override
    protected int getLayout() {
        return R.layout.activity_pick_location;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastKnownLocation();

        locationList = LocationRepo.getInstance().getAllLocation();
        setUpRecyclerView(locationList);

        etSearchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    presenter.autocompleteLocation(s.toString());
                    ivPickLocation.setVisibility(View.GONE);
                    ivClear.setVisibility(View.VISIBLE);
                } else {
                    ivPickLocation.setVisibility(View.VISIBLE);
                    ivClear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0) {
                    adapter.setData(locationList);
                }
            }
        });

        ivPickLocation.setOnClickListener(v -> setUpPlacePicker());
        ivClear.setOnClickListener(v -> {
            etSearchLocation.setText("");
            ivClear.setVisibility(View.GONE);
            ivPickLocation.setVisibility(View.VISIBLE);
            adapter.setData(locationList);
        });
    }

    public void setUpPlacePicker() {
        Mapbox.getInstance(this, Constants.MAP_BOX_TOKEN);
        if (userLocation != null) {
            Intent intent = new PlacePicker.IntentBuilder()
                    .accessToken(Constants.MAP_BOX_TOKEN)
                    .placeOptions(
                            PlacePickerOptions.builder()
                                    .statingCameraPosition(
                                            new CameraPosition.Builder()
                                                    .target(new LatLng(userLocation))
                                                    .zoom(16)
                                                    .build())
                                    .toolbarColor(getResources().getColor(R.color.colorPrimary))
                                    .build())
                    .build(this);
            startActivityForResult(intent, PLACE_SELECTION_REQUEST_CODE);
        } else {
            Banner.make(getWindow().getDecorView().getRootView(), this, Banner.INFO,
                    "Please turn on your GPS", Banner.TOP, 2000).show();
        }
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        userLocation = location;
                    }
                });
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    private void setUpRecyclerView(List<Location> locationList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvPickLocation.setLayoutManager(mLayoutManager);

        adapter = new LocationAdapter(locationList);
        rvPickLocation.setAdapter(adapter);

        if (adapter != null) {
            adapter.setOnClickedListener((location) -> {
                hideKeyBoard();
                Intent i = new Intent();
                i.putExtra("selected_location", location);
                setResult(22, i);
                finish();
            });
        }
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

    @Override
    public void onPlaceAutocompleteSuccess(List<Location> places) {
        GlobalUtils.showLog(TAG, "autocomplete success");
        adapter.setData(places);
    }

    @Override
    public void onPlaceAutocompleteFail(String msg) {
        GlobalUtils.showLog(TAG, "autocomplete fail: " + msg);
    }

    @Override
    public void showProgressBar(String message) {

    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void onFailure(String message) {
        GlobalUtils.showLog(TAG, "error: " + message);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_SELECTION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the information from the selected location's CarmenFeature
            assert data != null;
            CarmenFeature carmenFeature = PlacePicker.getPlace(data);
            if (carmenFeature != null) {
                String locationText = carmenFeature.text() +
                        ", " +
                        Objects.requireNonNull(carmenFeature.context()).get(0).text() +
                        ", " +
                        carmenFeature.context().get(1).text();

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                Intent i = new Intent();
                i.putExtra("selected_location", locationText);
                setResult(22, i);
                finish();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}