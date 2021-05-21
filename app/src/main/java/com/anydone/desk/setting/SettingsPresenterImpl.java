package com.anydone.desk.setting;

import com.anydone.desk.base.presenter.BasePresenter;

import javax.inject.Inject;

public class SettingsPresenterImpl extends BasePresenter<SettingsContract.SettingsView>
        implements SettingsContract.SettingsPresenter {

    @Inject
    public SettingsPresenterImpl() {
    }

}
