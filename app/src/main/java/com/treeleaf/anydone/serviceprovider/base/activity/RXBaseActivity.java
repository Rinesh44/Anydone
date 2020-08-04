package com.treeleaf.anydone.serviceprovider.base.activity;

import android.os.Bundle;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/*
* To be used in activities that require disposable but not a presenter
*
* */
public abstract class RXBaseActivity extends BaseActivity {

  private CompositeDisposable compositeDisposable;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    compositeDisposable = new CompositeDisposable();
  }

  public void addSubscription(DisposableObserver disposableObserver) {
    this.compositeDisposable.add(disposableObserver);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    compositeDisposable.dispose();
  }
}
