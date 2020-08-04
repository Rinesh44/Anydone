package com.treeleaf.anydone.serviceprovider.base.presenter;

import android.content.Context;

import androidx.annotation.StringRes;

import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

import io.reactivex.observers.DisposableObserver;

/**
 * Interface for a basic presenter
 *
 * @param <T> {@link BaseView}
 */
public interface Presenter<T extends BaseView> {

  /**
   * Attach View to the presenter
   *
   * @param t {@link BaseView}
   */
  void attachView(T t);

  /**
   * De-attach view
   */
  void deAttachView();

  /**
   * Check if view is attached to the presenter
   *
   * @return {@code true} if view is attached to the presenter; else {@code false}
   */
  boolean isViewAttached();

  /**
   * Return the attached view
   *
   * @return {@link BaseView} attached to the presenter
   */
  T getView();

  /**
   * Add RxJava Subscription
   *
   * @param subscription {@link DisposableObserver}
   */
  void addSubscription(DisposableObserver subscription);

  /**
   * Return the context of the view
   *
   * @return {@link Context}
   */
  Context getContext();

  /**
   * Returns a localized formatted string from the application's package's
   * default string table, substituting the formatDistance arguments as defined in
   * {@link java.util.Formatter} and {@link String#format}.
   *
   * @param resourceId Resource id for the formatDistance string
   * @param formatArgs The formatDistance arguments that will be used for
   * substitution.
   * @return The string data associated newInstance the resource, formatted and
   * stripped of styled text information.
   */
  String getString(@StringRes int resourceId, Object... formatArgs);
}
