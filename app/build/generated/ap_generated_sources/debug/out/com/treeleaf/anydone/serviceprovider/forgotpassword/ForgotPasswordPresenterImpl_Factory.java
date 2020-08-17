// Generated by Dagger (https://google.github.io/dagger).
package com.treeleaf.anydone.serviceprovider.forgotpassword;

import dagger.internal.Factory;
import javax.inject.Provider;

public final class ForgotPasswordPresenterImpl_Factory
    implements Factory<ForgotPasswordPresenterImpl> {
  private final Provider<ForgotPasswordRepository> forgotPasswordRepositoryProvider;

  public ForgotPasswordPresenterImpl_Factory(
      Provider<ForgotPasswordRepository> forgotPasswordRepositoryProvider) {
    this.forgotPasswordRepositoryProvider = forgotPasswordRepositoryProvider;
  }

  @Override
  public ForgotPasswordPresenterImpl get() {
    return provideInstance(forgotPasswordRepositoryProvider);
  }

  public static ForgotPasswordPresenterImpl provideInstance(
      Provider<ForgotPasswordRepository> forgotPasswordRepositoryProvider) {
    return new ForgotPasswordPresenterImpl(forgotPasswordRepositoryProvider.get());
  }

  public static ForgotPasswordPresenterImpl_Factory create(
      Provider<ForgotPasswordRepository> forgotPasswordRepositoryProvider) {
    return new ForgotPasswordPresenterImpl_Factory(forgotPasswordRepositoryProvider);
  }

  public static ForgotPasswordPresenterImpl newForgotPasswordPresenterImpl(
      ForgotPasswordRepository forgotPasswordRepository) {
    return new ForgotPasswordPresenterImpl(forgotPasswordRepository);
  }
}