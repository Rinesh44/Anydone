package com.anydone.desk.setting.location;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.anydone.desk.base.presenter.BasePresenter;
import com.treeleaf.anydone.entities.UserProto;
import com.anydone.desk.model.AutocompleteLocation;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.Location;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.LocationRepo;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.ProtoMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddLocationPresenterImpl extends BasePresenter<AddLocationContract.AddLocationView>
        implements AddLocationContract.AddLocationPresenter {
    private static final String TAG = "AddLocationPresenterImp";
    private AddLocationRepository addLocationRepository;

    @Inject
    public AddLocationPresenterImpl(AddLocationRepository addLocationRepository) {
        this.addLocationRepository = addLocationRepository;
    }

    @Override
    public void autocompleteLocation(String placeString) {
        String autocompleteUrl = getAutocompleteUrl(placeString);
        Retrofit retrofit = getRetrofitInstance();

        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<ResponseBody> autocompleteObservable =
                service.getPlaceAutocomplete(autocompleteUrl);

        addSubscription(autocompleteObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody autocompleteResponse) {
                        if (autocompleteResponse == null) {
                            getView().onPlaceAutocompleteFail("Failed to get location");
                            return;
                        }

                        try {
                            List<AutocompleteLocation> placeList =
                                    getAutocompleteList(autocompleteResponse);
                            getView().onPlaceAutocompleteSuccess(placeList);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onPlaceAutocompleteFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    @Override
    public void saveLocation(List<Location> locationList) {
        LocationRepo.getInstance().saveLocationList(locationList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().onLocationSaveSuccess();
            }

            @Override
            public void fail() {
                getView().onLocationSaveFail("Failed to save location");
            }
        });
    }

    @Override
    public void addLocation(String token, String address, double lat, double lng, String type) {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(address, "address cannot be null");
        Preconditions.checkNotNull(lat, "lat cannot be null");
        Preconditions.checkNotNull(lng, "lng cannot be null");
        Preconditions.checkNotNull(type, "type cannot be null");

        getView().showProgressBar("Please wait...");
        Observable<UserRpcProto.UserBaseResponse> locationObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);


        Account userAccount = AccountRepo.getInstance().getAccount();

        UserProto.LocationType locationType = getLocationType(type);

        boolean isDefault = true;
        List<Location> allLocation = LocationRepo.getInstance().getAllLocation();
        if (!CollectionUtils.isEmpty(allLocation)) isDefault = false;

        UserProto.Location location = UserProto.Location.newBuilder()
                .setAddress(address)
                .setLatitude(Float.parseFloat(String.valueOf(lat)))
                .setLongitude(Float.parseFloat(String.valueOf(lng)))
                .setLocationType(locationType)
                .setIsDefault(isDefault)
                .build();

        GlobalUtils.showLog(TAG, "location proto: " + location);

        locationObservable = service.addLocation(token, location);

        addSubscription(locationObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse userBaseResponse) {
                        GlobalUtils.showLog(TAG, "add location response: " + userBaseResponse);
                        getView().hideProgressBar();
                        if (userBaseResponse == null) {
                            getView().onAddLocationFail("Failed to add location");
                            return;
                        }

                        if (userBaseResponse.getError()) {
                            getView().onAddLocationFail(userBaseResponse.getMsg());
                            return;
                        }


                        getView().onAddLocationSuccess(ProtoMapper.
                                transformLocations(userBaseResponse.getLocationsList()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onAddLocationFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                }));
    }

    private UserProto.LocationType getLocationType(String type) {
        if (type.equalsIgnoreCase("home")) {
            return UserProto.LocationType.HOME;
        } else if (type.equalsIgnoreCase("work")) {
            return UserProto.LocationType.OFFICE;
        }

        return null;
    }


    private List<AutocompleteLocation> getAutocompleteList(ResponseBody autocompleteResponse)
            throws JSONException, IOException {
        List<AutocompleteLocation> placesList = new ArrayList<>();

        JSONObject rootObject = new JSONObject(autocompleteResponse.string());
        JSONArray featureArray = rootObject.getJSONArray("features");
        for (int i = 0; i < featureArray.length(); i++) {
            JSONObject obj = featureArray.getJSONObject(i);
            AutocompleteLocation autocompleteLocation = new AutocompleteLocation();
            autocompleteLocation.setPrimary(obj.getString("text"));
            JSONArray locationArray = obj.getJSONArray("center");
            double lat = locationArray.getDouble(1);
            double lng = locationArray.getDouble(0);

            String placeName = (String) obj.get("place_name");
            if (placeName.contains(",")) {
                String[] separated = obj.getString("place_name").split(",");
                GlobalUtils.showLog(TAG, "location check: " + obj.getString("place_name"));
                if (separated[1] != null)
                    autocompleteLocation.setSecondary(separated[0].trim() + ", " + separated[1].trim());
            } else {
                autocompleteLocation.setSecondary(placeName);
            }
            autocompleteLocation.setLat(lat);
            autocompleteLocation.setLng(lng);
            placesList.add(autocompleteLocation);
        }
        return placesList;

    }

    private Retrofit getRetrofitInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .build();

//        String base_url = Hawk.get(Constants.BASE_URL);
        String base_url = "https://api.mapbox.com/";
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private String getAutocompleteUrl(String placeString) {
        String countryCode = Hawk.get(Constants.COUNTRY_CODE);
        if (countryCode.isEmpty()) {
            countryCode = "NP";
        }

        return "geocoding/v5/mapbox.places/" + placeString +
                ".json?" +
                "access_token=" +
                Constants.MAP_BOX_TOKEN +
                "&country=" +
                countryCode;
    }
}
