package com.treeleaf.anydone.serviceprovider.setting;

import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;

import javax.inject.Inject;

public class SettingsPresenterImpl extends BasePresenter<SettingsContract.SettingsView>
        implements SettingsContract.SettingsPresenter {

    @Inject
    public SettingsPresenterImpl() {
    }

}
