package com.treeleaf.anydone.serviceprovider.base.activity;

import android.os.Bundle;

import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

import javax.inject.Inject;

/**
 * Should be implemented by activity which makes use of {@link BasePresenter} and {@link BaseView}
 *
 * @param <T> {@link BasePresenter}
 */
public abstract class MvpBaseActivity<T extends BasePresenter> extends BaseActivity
        implements BaseView {

    @Inject
    protected T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (presenter != null) {
            presenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.deAttachView();
        }
    }

}
