package com.treeleaf.anydone.serviceprovider.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.forgotpassword.ForgotPasswordActivity;
import com.treeleaf.anydone.serviceprovider.forgotpassword.resetpassword.ResetPasswordActivity;
import com.treeleaf.anydone.serviceprovider.landing.LandingActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.HostSelectionInterceptor;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.anydone.serviceprovider.verification.VerificationActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends MvpBaseActivity<LoginPresenterImpl> implements
        LoginContract.LoginView, View.OnClickListener {
    public static final int GOOGLE_SIGN_IN = 111;
    public static final int PERMISSIONS_CODE = 999;
    private static final String TAG = "LoginActivity";
    String[] branches = {"Dev", "Production"};

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
    @BindView(R.id.sp_branch)
    AppCompatSpinner spBranch;

    private GoogleSignInClient mGoogleSignInClient;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit = null;

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

        setupBranchSpinner();
    }

    private void setupBranchSpinner() {
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, branches);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spBranch.setAdapter(aa);

        spBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    GlobalUtils.showLog(TAG, "dev");
                    Hawk.put(Constants.BASE_URL, "https://api.anydone.com/");

                    String url = Hawk.get(Constants.BASE_URL);
                    HostSelectionInterceptor interceptor = new HostSelectionInterceptor();
                    interceptor.setHost(url);

                } else {
                    GlobalUtils.showLog(TAG, "prod");
                    Hawk.put(Constants.BASE_URL, "https://api.anydone.com/");

                    String url = Hawk.get(Constants.BASE_URL);
                    HostSelectionInterceptor interceptor = new HostSelectionInterceptor();
                    interceptor.setHost(url);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Hawk.put(Constants.BASE_URL, "https://api.anydone.net/");
            }
        });
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
    public void onEmployeeFirstLogin(String oldPassword) {
        Intent i = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        i.putExtra("employee_first_login", true);
        i.putExtra("old_password", oldPassword);
        startActivity(i);
    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), message);
    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
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

    public static Retrofit getClient(Context context) {

        if (okHttpClient == null)
            initOkHttp(context);

        String baseUrl = Hawk.get(Constants.BASE_URL);
        GlobalUtils.showLog(TAG, "base url: " + baseUrl);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static void initOkHttp(final Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json");

                // Adding Authorization token (API Key)
                // Requests will be denied without API key
                /*if (!TextUtils.isEmpty(PrefUtils.getApiKey(context))) {
                    requestBuilder.addHeader("Authorization", PrefUtils.getApiKey(context));
                }*/

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        okHttpClient = httpClient.build();
    }
}
