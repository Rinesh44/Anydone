package com.treeleaf.anydone.serviceprovider.setting.timezone;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

import java.io.UnsupportedEncodingException;

public class TimezoneContract {
    public interface TimezoneView extends BaseView {
        void onTimezoneAddSuccess();

        void onTimezoneAddFail(String msg);
    }

    public interface TimezonePresenter extends Presenter<TimezoneContract.TimezoneView> {
        void addTimezone(String token, String timezone) throws UnsupportedEncodingException;
    }
}
