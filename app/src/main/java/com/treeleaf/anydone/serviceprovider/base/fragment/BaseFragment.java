package com.treeleaf.anydone.serviceprovider.base.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.AnyDoneServiceProviderApplication;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.login.LoginActivity;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

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
        final Realm realm = RealmUtils.getInstance().getRealm();
        realm.executeTransaction(realm1 -> realm1.deleteAll());
    }
}
