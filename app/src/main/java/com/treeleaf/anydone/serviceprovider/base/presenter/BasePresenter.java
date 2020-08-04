package com.treeleaf.anydone.serviceprovider.base.presenter;

import android.content.Context;

import androidx.annotation.StringRes;

import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Base presenter to be extended by all presenters
 *
 * @param <T> {@link BaseView}
 */
public class BasePresenter<T extends BaseView> implements Presenter<T> {

  private T t;

  private CompositeDisposable compositeDisposable = new CompositeDisposable();

  @Override public void attachView(T t) {
    this.t = t;
  }

  @Override public void deAttachView() {
    compositeDisposable.dispose();
    this.t = null;
  }

  @Override public boolean isViewAttached() {
    return t != null;
  }

  @Override public T getView() {
    return t;
  }

  @Override public void addSubscription(DisposableObserver disposableObserver) {
    this.compositeDisposable.add(disposableObserver);
  }

  @Override public Context getContext() {
    return getView().getContext();
  }

  @Override public String getString(@StringRes int resourceId, Object... formatArgs) {
    return getContext().getString(resourceId, formatArgs);
  }
}
