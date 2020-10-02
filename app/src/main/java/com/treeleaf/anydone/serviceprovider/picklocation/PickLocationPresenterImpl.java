package com.treeleaf.anydone.serviceprovider.picklocation;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.model.Location;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

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

public class PickLocationPresenterImpl extends BasePresenter<PickLocationContract.PickLocationView>
        implements PickLocationContract.PickLocationPresenter {
    private static final String TAG = "PickLocationPresenterIm";
    private PickLocationRepository pickLocationRepository;

    @Inject
    public PickLocationPresenterImpl(PickLocationRepository pickLocationRepository) {
        this.pickLocationRepository = pickLocationRepository;
    }

    @Override
    public void autocompleteLocation(String placeString) {
        GlobalUtils.showLog(TAG, "entered location: " + placeString);
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
                            List<Location> placeList =
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

    private List<Location> getAutocompleteList(ResponseBody autocompleteResponse)
            throws JSONException, IOException {
        List<Location> placesList = new ArrayList<>();

        JSONObject rootObject = new JSONObject(autocompleteResponse.string());
        JSONArray featureArray = rootObject.getJSONArray("features");
        for (int i = 0; i < featureArray.length(); i++) {
            JSONObject obj = featureArray.getJSONObject(i);
            Location autocompleteLocation = new Location();
            autocompleteLocation.setLocationType(obj.getString("text"));
            JSONArray locationArray = obj.getJSONArray("center");
            double lat = locationArray.getDouble(1);
            double lng = locationArray.getDouble(0);

            String[] separated = obj.getString("place_name").split(",");
            GlobalUtils.showLog(TAG, "place name" + obj.getString("place_name"));
            GlobalUtils.showLog(TAG, "seaprated 0 : " + separated[0]);
            if (separated.length > 1) {
                autocompleteLocation.setLocationName(separated[0].trim() + ", "
                        + separated[1].trim());
            } else {
                autocompleteLocation.setLocationName(separated[0].trim());
            }
            autocompleteLocation.setLat(lat);
            autocompleteLocation.setLng(lng);
            autocompleteLocation.setDefault(false);
            autocompleteLocation.setId(UUID.randomUUID().toString()
                    .replace("-", ""));
            placesList.add(autocompleteLocation);
        }
        return placesList;
    }

    private Retrofit getRetrofitInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .build();

        String base_url = Hawk.get(Constants.BASE_URL);
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private String getAutocompleteUrl(String placeString) {
        String countryCode = Hawk.get(Constants.COUNTRY_CODE);

        return "geocoding/v5/mapbox.places/" + placeString +
                ".json?" +
                "access_token=" +
                Constants.MAP_BOX_TOKEN +
                "&country=" +
                countryCode;
    }
}
