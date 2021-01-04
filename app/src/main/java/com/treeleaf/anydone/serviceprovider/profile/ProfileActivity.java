package com.treeleaf.anydone.serviceprovider.profile;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.editprofile.EditProfileActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.anydone.serviceprovider.verification.VerificationActivity;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends MvpBaseActivity<ProfilePresenterImpl>
        implements ProfileContract.ProfileView {

    public static final int ADD_EMAIL_REQUEST = 111;
    public static final int ADD_PHONE_REQUEST = 222;
    public static final int CAMERA_ACTION_PICK_REQUEST_CODE = 333;
    public static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 444;

    private static final String TAG = "ProfileActivity";
    @BindView(R.id.profile_image)
    CircleImageView civProfileImage;
    @BindView(R.id.rl_profile_email)
    RelativeLayout rlEmail;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.rl_profile_phone)
    RelativeLayout rlPhone;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.rl_profile_gender)
    RelativeLayout rlGender;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.bottom_sheet)
    MaterialCardView mBottomSheet;
    @BindView(R.id.pb_progress)
    ProgressBar progress;

    private Account account;
    private boolean isPhone;
    private Dialog dialog;
    private BottomSheetBehavior sheetBehavior;
    String currentPhotoPath = "";
    String phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Profile");
        setToolbar();

        sheetBehavior = BottomSheetBehavior.from(mBottomSheet);

        TextView tvCamera = mBottomSheet.findViewById(R.id.tv_camera);
        TextView tvGallery = mBottomSheet.findViewById(R.id.tv_gallery);

        setProfileData();

        tvName.setOnClickListener(v -> openProfileEditActivity());

        tvPhone.setOnClickListener(v -> {
            if (account.getPhone() == null || account.getPhone().isEmpty()) {
                showEmailPhoneDialog();
                return;
            }

            if (!account.isPhoneVerified()) {
                hideKeyBoard();
                presenter.resendCode(account.getPhone());
            }
        });

        tvEmail.setOnClickListener(v -> {
            if (account.getEmail() == null || account.getEmail().isEmpty()) {
                showEmailPhoneDialog();
                return;
            }

            if (!account.isEmailVerified()) {
                hideKeyBoard();
                presenter.resendCode(account.getEmail());
            }
        });

        tvGender.setOnClickListener(v -> openProfileEditActivity());

        civProfileImage.setOnClickListener(v -> toggleBottomSheet());
        tvCamera.setOnClickListener(v -> Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(ProfileActivity.this,
                                    "Camera and media/files access permissions are required",
                                    Toast.LENGTH_LONG).show();
                            openAppSettings();
                        }

                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            try {
                                openCamera();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list,
                                                                   PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check());

        tvGallery.setOnClickListener(v -> Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(ProfileActivity.this,
                                    "Media/files access permissions are required",
                                    Toast.LENGTH_LONG).show();
                            openAppSettings();
                        }

                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            openGallery();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list,
                                                                   PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check());
    }

    private void openAppSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void openGallery() {
        toggleBottomSheet();

        Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pictureIntent.setType("image/*");  // 1
        pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);  // 2
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String[] mimeTypes = new String[]{"image/jpeg", "image/png", "image/webp"};  // 3
            pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(Intent.createChooser(pictureIntent,
                "Select Picture"), PICK_IMAGE_GALLERY_REQUEST_CODE);  // 4

    }

    private void openCamera() throws IOException {
        toggleBottomSheet();

        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getImageFile(); // 1
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 2
            uri = FileProvider.getUriForFile(this,
                    "com.treeleaf.anydone.serviceprovider.provider", file);
        } else {
            uri = Uri.fromFile(file); // 3
        }
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
        startActivityForResult(pictureIntent, CAMERA_ACTION_PICK_REQUEST_CODE);
    }

    private void showEmailPhoneDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_email_phone);
        Objects.requireNonNull(dialog.getWindow())
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        RelativeLayout rlPhoneContainer = dialog.findViewById(R.id.rl_phone_container);
        TextView tvAreaCode = dialog.findViewById(R.id.tv_country_code);
        CountryCodePicker ccp = dialog.findViewById(R.id.country_picker);
        TextInputEditText etEmail = dialog.findViewById(R.id.et_email);
        TextInputEditText etPhone = dialog.findViewById(R.id.et_phone);
        TextInputLayout ilEmail = dialog.findViewById(R.id.il_email);
        MaterialButton btnVerify = dialog.findViewById(R.id.btn_verify);

        if (isPhone) {
            ilEmail.setVisibility(View.VISIBLE);
            ilEmail.requestFocus();
        } else {
            rlPhoneContainer.setVisibility(View.VISIBLE);
            String selectedCountry1 = ccp.getSelectedCountryNameCode();
            final PhoneNumberFormattingTextWatcher[] textWatcher =
                    {new PhoneNumberFormattingTextWatcher(selectedCountry1)};
            etPhone.addTextChangedListener(textWatcher[0]);

            tvAreaCode.setText(ccp.getSelectedCountryCodeWithPlus());
            ccp.setOnCountryChangeListener(() -> {
                String selectedCountry = ccp.getSelectedCountryNameCode();
                GlobalUtils.showLog(TAG, "country Code: " + selectedCountry);
                etPhone.requestFocus();
                etPhone.setText("");
                tvAreaCode.setText(ccp.getSelectedCountryCodeWithPlus());
                etPhone.removeTextChangedListener(textWatcher[0]);
                textWatcher[0] = new PhoneNumberFormattingTextWatcher(selectedCountry);
                etPhone.addTextChangedListener(textWatcher[0]);
            });

        }

        btnVerify.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(btnVerify.getWindowToken(), 0);
            if (isPhone) {
                email = UiUtils.getString(etEmail);
                presenter.addEmail(email);
            } else {
                String numberOnly = UiUtils.getString(etPhone).replaceAll("[^0-9]",
                        "");
                phone = tvAreaCode.getText().toString() + numberOnly;
                presenter.addPhone(phone);
            }
        });

        dialog.show();
    }

    private void setProfileData() {
        account = AccountRepo.getInstance().getAccount();
        GlobalUtils.showLog(TAG, "account details: " + account);
        tvName.setText(account.getFullName());
        tvName.setTextColor(getResources().getColor(R.color.black));
        isPhone = Hawk.get(Constants.IS_PHONE);

      /*  if (account.getAddress() != null && !account.getAddress().isEmpty()) {
            tvAddress.setText(account.getAddress());
        }*/


        if (account.getPhone() != null && !account.getPhone().isEmpty()) {
            formatPhone(account);
            if (!isPhone && !account.isPhoneVerified()) {
                tvPhone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_not_verified,
                        0, 0, 0);
            } else {
                tvPhone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_verified,
                        0, 0, 0);
                //                tvPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            tvPhone.setCompoundDrawablePadding(10);
            tvPhone.setGravity(Gravity.CENTER_VERTICAL);
        }

        if (account.getEmail() != null && !account.getEmail().isEmpty()) {
            tvEmail.setText(account.getEmail());
            tvEmail.setTextColor(getResources().getColor(R.color.black));

            if (!isPhone && !account.isEmailVerified()) {
                tvEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_not_verified,
                        0, 0, 0);
            } else {
                tvEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_verified,
                        0, 0, 0);
                //                tvEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            tvEmail.setCompoundDrawablePadding(10);
            tvEmail.setGravity(Gravity.CENTER_VERTICAL);
        }

        if (account.getProfilePic() != null) {
            if (!account.getProfilePic().isEmpty()) {
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_empty_profile_holder_icon)
                        .error(R.drawable.ic_empty_profile_holder_icon);

                Glide.with(this)
                        .load(account.getProfilePic())
                        .apply(options)
                        .into(civProfileImage);
            }

            if (account.getGender() != null && !account.getGender()
                    .equals(AnydoneProto.Gender.UNKNOWN_GENDER.name())) {
                tvGender.setText(account.getGender());
                tvGender.setTextColor(getResources().getColor(R.color.black));
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProfileData();
    }

    private void formatPhone(Account account) {
        String formattedNumber = PhoneNumberUtils.formatNumber(account.getPhone(),
                account.getCountryCode());
        tvPhone.setText(formattedNumber);
        tvPhone.setTextColor(getResources().getColor(R.color.black));
    }

    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Profile");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                Constants.SERVER_ERROR);

    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_edit_profile:
                openProfileEditActivity();
        }
        return false;
    }

    private void openProfileEditActivity() {
        startActivity(new Intent(this, EditProfileActivity.class));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onUploadImageSuccess() {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                "Profile picture uploaded");
        setProfileData();
    }

    @Override
    public void onUploadImageFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onResendCodeSuccess() {
        if (isPhone) {
            Hawk.put(Constants.EMAIL_PHONE, account.getPhone());
            Intent i = new Intent(ProfileActivity.this, VerificationActivity.class);
            i.putExtra("edit_profile", true);
            i.putExtra("phone_verification", true);
            i.putExtra("phone", phone);
            startActivityForResult(i, ADD_PHONE_REQUEST);
        } else {
            Hawk.put(Constants.EMAIL_PHONE, account.getEmail());
            Intent i = new Intent(ProfileActivity.this, VerificationActivity.class);
            i.putExtra("email_verification", true);
            i.putExtra("edit_profile", true);
            i.putExtra("email", email);
            startActivityForResult(i, ADD_EMAIL_REQUEST);
        }
    }

    @Override
    public void onResendCodeFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onDataChange(String msg) {
        setProfileData();
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void showInvalidPhoneError() {
        TextInputLayout ilPhone = dialog.findViewById(R.id.il_phone);
        TextInputEditText etPhone = dialog.findViewById(R.id.et_phone);
        TextView tvAreaCode = dialog.findViewById(R.id.tv_country_code);
        CountryCodePicker ccp = dialog.findViewById(R.id.country_picker);

        etPhone.requestFocus();
        ilPhone.setErrorEnabled(true);
        ilPhone.setError("Invalid Phone Number");

        ccp.setPadding(0, 0, 0,
                getResources().getDimensionPixelOffset(R.dimen.dimen_10x1));
        tvAreaCode.setPadding(0, 0, 0,
                getResources().getDimensionPixelOffset(R.dimen.dimen_10x1));
    }

    @Override
    public void showInvalidEmailError() {
        TextInputLayout ilEmail = dialog.findViewById(R.id.il_email);
        TextInputEditText etEmail = dialog.findViewById(R.id.et_email);
        etEmail.requestFocus();
        ilEmail.setErrorEnabled(true);
        ilEmail.setError("Invalid Email");
    }

    @Override
    public void onAddEmailFail(String msg) {
        dialog.dismiss();
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onAddPhoneFail(String msg) {
        dialog.dismiss();
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onAddEmailSuccess() {
        dialog.dismiss();
        hideKeyBoard();
        Intent i = new Intent(ProfileActivity.this, VerificationActivity.class);
        i.putExtra("edit_profile", true);
        startActivityForResult(i, ADD_EMAIL_REQUEST);
    }

    @Override
    public void onAddPhoneSuccess() {
        dialog.dismiss();
        hideKeyBoard();
        Intent i = new Intent(ProfileActivity.this, VerificationActivity.class);
        i.putExtra("edit_profile", true);
        startActivityForResult(i, ADD_PHONE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_ACTION_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = Uri.parse(currentPhotoPath);
            openCropActivity(uri, uri);
        }

        if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE &&
                resultCode == RESULT_OK && data != null) {
            Uri sourceUri = data.getData(); // 1
            File file = null; // 2
            try {
                file = getImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri destinationUri = Uri.fromFile(file);  // 3
            openCropActivity(sourceUri, destinationUri);  // 4
        }


        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri uri = UCrop.getOutput(data);
            presenter.uploadImage(uri);
        }

        if (requestCode == ADD_PHONE_REQUEST) {
            if (resultCode == 2) {
                if (data != null) {
                    boolean success = data.getBooleanExtra("success", false);
                    if (success) {
                        presenter.setPhoneVerified();
                    }
                }
            }
        }

        if (requestCode == ADD_EMAIL_REQUEST) {
            if (resultCode == 2) {
                if (data != null) {
                    boolean success = data.getBooleanExtra("success", false);
                    if (success) {
                        presenter.setEmailVerified();
                    }
                }
            }
        }
    }

    /**
     * manually opening / closing bottom sheet on button click
     */
    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

                Rect outRect = new Rect();
                mBottomSheet.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY()))
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        return super.dispatchTouchEvent(event);
    }


    private File getImageFile() throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        );
        File file = File.createTempFile(
                imageFileName, ".jpg", storageDir
        );
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;
    }

    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(5f, 5f)
                .start(ProfileActivity.this);
    }


}
