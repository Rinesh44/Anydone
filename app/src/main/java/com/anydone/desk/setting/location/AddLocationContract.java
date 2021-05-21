package com.anydone.desk.setting.location;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.model.AutocompleteLocation;
import com.anydone.desk.realm.model.Location;

import java.util.List;

public class AddLocationContract {
    public interface AddLocationView extends BaseView {
        void onPlaceAutocompleteSuccess(List<AutocompleteLocation> places);

        void onPlaceAutocompleteFail(String msg);

        void onLocationSaveSuccess();

        void onLocationSaveFail(String msg);

        void onAddLocationSuccess(List<Location> locationList);

        void onAddLocationFail(String msg);
    }

    public interface AddLocationPresenter extends Presenter<AddLocationContract.AddLocationView> {
        void autocompleteLocation(String placeString);

        void saveLocation(List<Location> locationList);

        void addLocation(String token, String address, double lat, double lng, String type);
    }
}
