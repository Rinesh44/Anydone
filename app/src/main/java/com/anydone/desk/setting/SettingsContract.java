package com.anydone.desk.setting;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class SettingsContract {
    public interface SettingsView extends BaseView {

    }

    public interface SettingsPresenter extends Presenter<SettingsContract.SettingsView> {

    }
}
