package com.anydone.desk.base.view;

import android.content.Context;

/**
 * Needs to be implemented by all views
 */
public interface BaseView {

  void showProgressBar(String message);

  void showToastMessage(String message);

  void hideProgressBar();

  void onFailure(String message);

  Context getContext();
}
