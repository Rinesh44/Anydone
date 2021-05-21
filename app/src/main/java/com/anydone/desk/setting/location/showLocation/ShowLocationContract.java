package com.anydone.desk.setting.location.showLocation;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class ShowLocationContract {
    public interface ShowLocationView extends BaseView {
        void onMakeLocationDefualtSuccess(String locationId);

        void onMakeLocationDefaultFail(String msg);

        void onDeleteLocationSuccess(String locationId, int pos);

        void onDeleteLocationFail(String msg);
    }

    public interface ShowLocationPresenter extends Presenter<ShowLocationContract.ShowLocationView> {
        void makeLocationDefault(String locationId);

        void deleteLocation(String locationId, int pos);
    }
}
