package com.treeleaf.anydone.serviceprovider.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.forgotpassword.ForgotPasswordActivity;
import com.treeleaf.anydone.serviceprovider.landing.LandingActivity;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.anydone.serviceprovider.verification.VerificationActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends MvpBaseActivity<LoginPresenterImpl> implements
        LoginContract.LoginView, View.OnClickListener {
    public static final int GOOGLE_SIGN_IN = 111;
    public static final int PERMISSIONS_CODE = 999;
    private static final String TAG = "LoginActivity";
    /*   @BindView(R.id.btnLoginWithGoogle)
       MaterialButton btnGoogleSignIn;*/
    @BindView(R.id.tv_forgot_password)
    TextView tvForgotPassword;
    @BindView(R.id.et_email)
    TextInputEditText etEmail;
    @BindView(R.id.et_password)
    TextInputEditText etPassword;
    @BindView(R.id.il_email_phone)
    TextInputLayout ilEmailPhone;
    @BindView(R.id.il_password)
    TextInputLayout ilPassword;

    private GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog progress;

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        btnGoogleSignIn.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);

        checkRequiredPermissions();
        configureGoogleSignIn();
        addTextInputListeners();

    }

    private void addTextInputListeners() {
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ilEmailPhone.setError(null);
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ilPassword.setError(null);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // user is already signed in

        }
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    private void configureGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @OnClick(R.id.loginButton)
    public void login() {
        hideKeyBoard();
        presenter.loginWithEmailPhone(UiUtils.getString(etEmail), UiUtils.getString(etPassword));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
      /*      case R.id.btnLoginWithGoogle:
                googleSignIn();
                break;*/

            case R.id.tv_forgot_password:
                startActivity(new Intent(LoginActivity.this,
                        ForgotPasswordActivity.class));
                break;
        }
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                        "Logged in as " + account.getEmail());
            }
            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                    "Sign in failed with error code: " + e.getStatusCode());
        }
    }

    @Override
    public void showInvalidEmailPhoneError() {
        ilEmailPhone.setError("Invalid email or phone");
    }

    @Override
    public void showInvalidPasswordError() {
        ilPassword.setError("Invalid password");
    }

    @Override
    public void showEmptyPhoneOrEmailFieldError(String message) {
        ilEmailPhone.setError(message);
    }

    @Override
    public void showEmptyPasswordFieldError(String message) {
        ilPassword.setError(message);
    }

    @Override
    public void onAccountNotVerified() {
        Intent i = new Intent(LoginActivity.this, VerificationActivity.class);
        i.putExtra("email_phone", UiUtils.getString(etEmail));
        i.putExtra("password", UiUtils.getString(etPassword));
        startActivity(i);
    }

    @Override
    public void onLoginSuccess() {
        //go to next activity
        GlobalUtils.showLog(TAG, "login Success");
        startActivity(new Intent(LoginActivity.this, LandingActivity.class));
        finish();
    }

    @Override
    public void onLoginFail(String msg) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void showProgressBar(String message) {
        progress = ProgressDialog.show(this, null, message, true);
    }

    @Override
    public void showToastMessage(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), message);
    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    @Override
    public void onFailure(String message) {
        showToastMessage(message);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @SuppressLint("InlinedApi")
    private void checkRequiredPermissions() {
        if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                && hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                && hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                && hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && hasPermission(Manifest.permission.ACCESS_MEDIA_LOCATION)
                && hasPermission(Manifest.permission.CAMERA)
                && hasPermission(Manifest.permission.RECORD_AUDIO)) {
            return;
        }

        requestPermissionsSafely(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_MEDIA_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.RECORD_AUDIO}, PERMISSIONS_CODE);
    }
}
