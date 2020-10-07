package com.treeleaf.anydone.serviceprovider.setting.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.AutocompleteLocationAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.model.AutocompleteLocation;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class AddLocationActivity extends MvpBaseActivity<AddLocationPresenterImpl> implements
        AddLocationContract.AddLocationView {
    private static final int PLACE_SELECTION_REQUEST_CODE = 5679;
    private static final String TAG = "AddLocationActivity";
    String[] locationType = {"Home", "Work"};

    private Location userLocation;
    private FusedLocationProviderClient fusedLocationClient;
    @BindView(R.id.et_search_location)
    AutoCompleteTextView etSearchLocation;
    @BindView(R.id.sp_location_type)
    AppCompatSpinner spLocationType;
    @BindView(R.id.et_location)
    TextInputEditText etLocation;
    @BindView(R.id.btn_add_location)
    MaterialButton btnAddLocation;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private AutocompleteLocation selectedLocation;

    @Override
    protected int getLayout() {
        return R.layout.activity_add_location;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastKnownLocation();
        setToolbar();
        etLocation.setShowSoftInputOnFocus(false);

        spLocationType.setOnTouchListener((v, event) -> {
            etLocation.requestFocus();
            setUpLocationTypeDropdown();
            return false;
        });

        etLocation.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etLocation.setText("a");
                etLocation.setTextColor(getResources().getColor(R.color.transparent));
            }
        });

        etSearchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    presenter.autocompleteLocation(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etSearchLocation.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (etSearchLocation.getRight() -
                        etSearchLocation.getCompoundDrawables()
                                [DRAWABLE_RIGHT].getBounds().width())) {
                    setUpPlacePicker();
                    return true;
                }
            }
            return false;
        });

        btnAddLocation.setOnClickListener(v ->
        {

            if (selectedLocation == null) {
                Banner.make(getWindow().getDecorView().getRootView(),
                        this, Banner.INFO, "Please enter location",
                        Banner.TOP, 2000).show();

                return;
            }

            String selectedLocationType = (String) spLocationType.getSelectedItem();
            GlobalUtils.showLog(TAG, "selected locatin type: " + selectedLocationType);
            if (selectedLocationType == null) {
                Banner.make(getWindow().getDecorView().getRootView(),
                        this, Banner.INFO, "Please select location type",
                        Banner.TOP, 2000).show();
                return;
            }

            String token = Hawk.get(Constants.TOKEN);
            presenter.addLocation(token, selectedLocation.getSecondary(), selectedLocation.getLat(),
                    selectedLocation.getLng(), (String) spLocationType.getSelectedItem());

        });
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
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
            Banner.make(getWindow().getDecorView().getRootView(),
                    this, Banner.INFO, "Please turn on your GPS",
                    Banner.TOP, 2000).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_SELECTION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the information from the selected location's CarmenFeature
            CarmenFeature carmenFeature = PlacePicker.getPlace(data);
            GlobalUtils.showLog(TAG, "carmen feature: " + carmenFeature);
            if (carmenFeature != null) {
                String locationText = carmenFeature.text() +
                        ", " +
                        Objects.requireNonNull(carmenFeature.context()).get(0).text() +
                        ", " +
                        carmenFeature.context().get(1).text();

                selectedLocation = new AutocompleteLocation();
                selectedLocation.setPrimary(carmenFeature.context().get(0).text());
                selectedLocation.setSecondary(carmenFeature.text());
                selectedLocation.setLat(Objects.requireNonNull(carmenFeature.center()).latitude());
                selectedLocation.setLng(carmenFeature.center().longitude());
                etSearchLocation.setText(locationText);
            }
        }
    }

    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Add Location");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setUpLocationTypeDropdown() {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                locationType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLocationType.setAdapter(adapter);
    }


    @Override
    public void onPlaceAutocompleteSuccess(List<AutocompleteLocation> places) {
        setUpLocationSearchAdapter(places);
    }

    @Override
    public void onPlaceAutocompleteFail(String msg) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onLocationSaveSuccess() {
        finish();
    }

    @Override
    public void onLocationSaveFail(String msg) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onAddLocationSuccess(List<com.treeleaf.anydone.serviceprovider.realm.model.Location> locationList) {
        presenter.saveLocation(locationList);
    }

    @Override
    public void onAddLocationFail(String msg) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void showProgressBar(String message) {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), message);
    }

    @Override
    public Context getContext() {
        return this;
    }


    private void setUpLocationSearchAdapter(List<AutocompleteLocation> locations) {
        if (!CollectionUtils.isEmpty(locations)) {
            AutocompleteLocationAdapter adapter = new AutocompleteLocationAdapter(this,
                    locations);
            etSearchLocation.setAdapter(adapter);
        }

        etSearchLocation.setOnItemClickListener((parent, view, position, id) -> {
            UiUtils.hideKeyboard(AddLocationActivity.this);
            if (!CollectionUtils.isEmpty(locations)) {
                selectedLocation = locations.get(position);
                GlobalUtils.showLog(TAG, "lat: " + selectedLocation.getLat());
                GlobalUtils.showLog(TAG, "lng: " + selectedLocation.getLng());
            }
//            etSearchLocation.setText(locations.get(position).getPrimary());
        });
    }
}