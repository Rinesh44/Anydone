package com.treeleaf.anydone.serviceprovider.setting;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class SettingsContract {
    public interface SettingsView extends BaseView {

    }

    public interface SettingsPresenter extends Presenter<SettingsContract.SettingsView> {

    }
}
