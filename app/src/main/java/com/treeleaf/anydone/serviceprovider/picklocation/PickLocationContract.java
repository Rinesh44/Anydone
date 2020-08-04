package com.treeleaf.anydone.serviceprovider.picklocation;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.Location;

import java.util.List;

public class PickLocationContract {
    public interface PickLocationView extends BaseView {
        void onPlaceAutocompleteSuccess(List<Location> places);

        void onPlaceAutocompleteFail(String msg);
    }

    public interface PickLocationPresenter extends
            Presenter<PickLocationContract.PickLocationView> {
        void autocompleteLocation(String placeString);
    }
}
