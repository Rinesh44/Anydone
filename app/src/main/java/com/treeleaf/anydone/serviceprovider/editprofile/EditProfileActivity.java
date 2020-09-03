package com.treeleaf.anydone.serviceprovider.editprofile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.EmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ServiceProviderRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.Objects;

import butterknife.BindView;

public class EditProfileActivity extends MvpBaseActivity<EditProfilePresenterImpl>
        implements EditProfileContract.EditProfileView {
    private static final String TAG = "EditProfileActivity";
    String[] gender = {"Male", "Female", "Other"};
    @BindView(R.id.il_full_name)
    TextInputLayout ilFullName;
    @BindView(R.id.et_full_name)
    TextInputEditText etFullName;
    @BindView(R.id.btn_save)
    MaterialButton btnSave;
    @BindView(R.id.sp_gender)
    Spinner spnGender;
    @BindView(R.id.il_gender)
    TextInputLayout ilGender;
    @BindView(R.id.et_gender)
    TextInputEditText etGender;
    @BindView(R.id.pb_progress)
    ProgressBar progress;

    private Account userAccount;
    private Employee employee;
    private ServiceProvider serviceProvider;

    @Override
    protected int getLayout() {
        return R.layout.activity_edit_profile;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar();
        userAccount = AccountRepo.getInstance().getAccount();
        etGender.setShowSoftInputOnFocus(false);
        setDataToFields();

        spnGender.setOnTouchListener((v, event) -> {
            etGender.requestFocus();
            setUpGenderDropDown();
            return false;
        });

        etGender.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etGender.setText("a");
                etGender.setTextColor(getResources().getColor(R.color.transparent));
            }
        });

        btnSave.setOnClickListener(v -> {
            hideKeyBoard();
            if (userAccount.getAccountType().equalsIgnoreCase(AnydoneProto.AccountType.EMPLOYEE.name())) {
                employee = EmployeeRepo.getInstance().getEmployeeByAccountId
                        (userAccount.getAccountId());
                presenter.editEmployeeProfile(employee.getEmployeeId(), userAccount.getAccountId(),
                        UiUtils.getString(etFullName),
                        (String) spnGender.getSelectedItem());
            } else {
                serviceProvider = ServiceProviderRepo.getInstance().getServiceProviderByAccountId(
                        (userAccount.getAccountId()));
                presenter.editServiceProviderProfile(serviceProvider.getServiceProviderId(), userAccount.getAccountId(),
                        UiUtils.getString(etFullName),
                        (String) spnGender.getSelectedItem());
            }

        });
    }

    private void setDataToFields() {

        if (userAccount.getFullName() != null && !userAccount.getFullName().isEmpty()) {
            etFullName.setText(userAccount.getFullName());
        }

        if (userAccount.getGender() != null && !userAccount.getGender()
                .equals(AnydoneProto.Gender.UNKNOWN_GENDER.name())) {
            setUpGenderDropDown();
            GlobalUtils.showLog(TAG, "check gender: " + userAccount.getGender());
            etGender.setText("a");
            etGender.setTextColor(getResources().getColor(R.color.white));
            switch (userAccount.getGender()) {
                case "MALE":
                    GlobalUtils.showLog(TAG, "gender male");
                    spnGender.setSelection(0);
                    break;

                case "FEMALE":
                    spnGender.setSelection(1);
                    break;

                case "OTHER":
                    spnGender.setSelection(2);
                    break;

                default:
                    break;
            }
        }

    }

    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable
                (R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Edit Profile");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    private void setUpGenderDropDown() {
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, gender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnGender.setAdapter(adapter);
    }

    @Override
    protected void injectDagger() {
        getActivityComponent()
                .inject(EditProfileActivity.this);
    }

    @Override
    public void showInvalidFullNameError() {
        etFullName.requestFocus();
        ilFullName.setErrorEnabled(true);
        ilFullName.setError("Invalid Full Name");

        onInvalidFullName();
    }

    @Override
    public void showInvalidGenderError() {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                "Please " + "select your Gender");
    }

    @Override
    public void onEditProfileSuccess() {
        //goto verification activity
        ilFullName.setErrorEnabled(false);
        finish();
    }

    @Override
    public void onEditProfileFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onInvalidFullName() {
        ilGender.setErrorEnabled(false);
    }

    @Override
    public void onInvalidGender() {
        ilFullName.setErrorEnabled(false);
    }

    @Override
    public void onPlaceAutocompleteFail(String msg) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }


    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {
        UiUtils.showToast(this, message);
    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), message);
    }

    @Override
    public Context getContext() {
        return EditProfileActivity.this;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
