package com.treeleaf.anydone.serviceprovider.base.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.injection.ActivityComponentFactory;
import com.treeleaf.anydone.serviceprovider.injection.component.ActivityComponent;
import com.treeleaf.anydone.serviceprovider.login.LoginActivity;
import com.treeleaf.anydone.serviceprovider.utils.LocaleHelper;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.observers.DisposableObserver;
import io.realm.Realm;

/**
 * Base activity.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;

    private ActivityComponent activityComponent;

    private DisposableObserver locationSubscription;

    /**
     * abstract method which returns id of layout in the form of R.layout.layout_name.
     *
     * @return id of layout in the form of R.layout.layout_name
     */
    protected abstract int getLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        unbinder = ButterKnife.bind(this);
        injectDagger();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationSubscription != null && !locationSubscription.isDisposed()) {
            locationSubscription.dispose();
        }
    }

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = ActivityComponentFactory.create(this);
        }
        return activityComponent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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

    /**
     * Hide soft keyboard
     */
    public void hideKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Intercept touch event for hiding keyboard while pressing outside of edit text
     *
     * @param event {@link MotionEvent}
     * @return boolean
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideKeyBoard();
        return true;
    }

    protected abstract void injectDagger();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
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
