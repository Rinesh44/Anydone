package com.anydone.desk.base.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.orhanobut.hawk.Hawk;
import com.anydone.desk.AnyDoneServiceProviderApplication;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.injection.component.ApplicationComponent;
import com.anydone.desk.login.LoginActivity;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;

/**
 * Injects dagger and butter knife to make code reusable
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {

    @Inject
    protected T presenter;

    private Unbinder unbinder;
    private boolean isFragmentVisible = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        unbinder = ButterKnife.bind(this, view);
        injectDagger(provideAnyDoneDeliveryComponent());
        presenter.attachView(this);
        isFragmentVisible = true;
        return view;
    }

    protected abstract int getLayout();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.deAttachView();
    }

    @Override
    public void onResume() {
        super.onResume();
        isFragmentVisible = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentVisible = false;
    }

    public boolean isFragmentVisible() {
        return isFragmentVisible;
    }

    /**
     * Provides instance of ApplicationComponent{@link ApplicationComponent}
     *
     * @return instance of {@link ApplicationComponent}
     */
    public ApplicationComponent provideAnyDoneDeliveryComponent() {
        return AnyDoneServiceProviderApplication.get(getContext()).getApplicationComponent();
    }

    protected abstract void injectDagger(ApplicationComponent applicationComponent);

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onAuthorizationFailed(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

        Hawk.deleteAll();
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.deleteAll());
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;

    }

    public int checkSelfPermission(String permission) {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                permission)
                == PackageManager.PERMISSION_GRANTED) {
            return PackageManager.PERMISSION_GRANTED;
        } else return -1;
    }
}
