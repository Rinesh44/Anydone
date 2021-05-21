package com.anydone.desk.picklocation;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.realm.model.Location;

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
