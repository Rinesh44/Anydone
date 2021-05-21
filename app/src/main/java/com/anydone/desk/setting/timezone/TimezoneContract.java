package com.anydone.desk.setting.timezone;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

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
