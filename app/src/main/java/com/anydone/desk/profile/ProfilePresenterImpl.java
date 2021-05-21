package com.anydone.desk.profile;


import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.ValidationUtils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;


public class ProfilePresenterImpl extends BasePresenter<ProfileContract.ProfileView>
        implements ProfileContract.ProfilePresenter {
    private static final String TAG = "ProfilePresenterImpl";
    private ProfileRepository profileRepository;

    @Inject
    public ProfilePresenterImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }


    @Override
    public void uploadImage(Uri resultUri) {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(resultUri, "Uri cannot be null");
        getView().showProgressBar("Uploading image...");

        Observable<UserRpcProto.UserBaseResponse> profileObservable;

        Bitmap decodedBitmap = GlobalUtils.decodeSampledBitmapFromResource(resultUri.getPath(),
                150, 150);

        BitmapFactory.decodeFile(resultUri.getPath());
        byte[] byteArray = getByteArrayFromBitmap(decodedBitmap);
        String mimeType = getMimeType(resultUri);

        RequestBody imageReqBody = RequestBody.create(MediaType.parse(mimeType), byteArray);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image",
                        "image.jpg", imageReqBody);

        Retrofit retrofit = getRetrofitInstance();

        AnyDoneService service = retrofit.create(AnyDoneService.class);

        profileObservable = service.uploadImage(Hawk.get(Constants.TOKEN), body);

        addSubscription(profileObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse uploadPicResponse) {
                        GlobalUtils.showLog(TAG, "upload pic response: "
                                + uploadPicResponse);

                        getView().hideProgressBar();
                        if (uploadPicResponse == null) {
                            getView().onUploadImageFail("Upload image failed");
                            return;
                        }

                        if (uploadPicResponse.getError()) {
                            getView().onUploadImageFail(uploadPicResponse.getMsg());
                            return;
                        }

                        setProfilePicToUser(uploadPicResponse.getRefId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onUploadImageFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );

    }

    @Override
    public void resendCode(String emailPhone) {
        getView().showProgressBar("Please wait...");
        Observable<UserRpcProto.UserBaseResponse> resendCodeObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        resendCodeObservable = service.resendCode(emailPhone);
        GlobalUtils.showLog(TAG, "resend code check: " + emailPhone);

        addSubscription(resendCodeObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse resendCodeResponse) {
                        GlobalUtils.showLog(TAG, "resend code response: "
                                + resendCodeResponse);
                        if (resendCodeResponse == null) {
                            getView().hideProgressBar();
                            getView().onResendCodeFail("Resend code failed");
                            return;
                        }

                        if (resendCodeResponse.getError()) {
                            getView().hideProgressBar();
                            getView().onResendCodeFail(resendCodeResponse.getMsg());
                            return;
                        }

                        getView().onResendCodeSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onResendCodeFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );

    }

    @Override
    public void addPhone(String phone) {
        Preconditions.checkNotNull(phone, "Phone cannot be null");
        if (!validatePhone(phone)) {
            return;
        }
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<UserRpcProto.UserBaseResponse> addPhoneObservable = null;
        try {
            String token = Hawk.get(Constants.TOKEN, "");
            addPhoneObservable = service.addPhone(token, URLEncoder.encode(phone, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        assert addPhoneObservable != null;
        addSubscription(addPhoneObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse addPhoneResponse) {
                        GlobalUtils.showLog(TAG, "add phone response: " + addPhoneResponse);

                        getView().hideProgressBar();
                        if (addPhoneResponse == null) {
                            getView().onAddPhoneFail("Add phone failed");
                            return;
                        }

                        if (addPhoneResponse.getError()) {
                            getView().onAddPhoneFail(addPhoneResponse.getMsg());
                            return;
                        }

                        Hawk.put(Constants.EMAIL_PHONE, phone);
                        AccountRepo.getInstance().addPhone(phone, new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                GlobalUtils.showLog(TAG, "phone added to current user");
                                getView().onAddPhoneSuccess();
                            }

                            @Override
                            public void fail() {
                                GlobalUtils.showLog(TAG,
                                        "failed to add phone to current user");
                            }
                        });

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                }));
    }

    @Override
    public void addEmail(String email) {
        Preconditions.checkNotNull(email, "Email cannot be null");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        if (!validateEmail(email)) {
            return;
        }

        getView().showProgressBar("Please wait...");
        Observable<UserRpcProto.UserBaseResponse> addEmailObservable = null;
        try {
            String token = Hawk.get(Constants.TOKEN, "");
            addEmailObservable = service.addEmail(token, URLEncoder.encode(email, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        assert addEmailObservable != null;
        addSubscription(addEmailObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse addEmailResponse) {
                        GlobalUtils.showLog(TAG, "add email response: " + addEmailResponse);

                        getView().hideProgressBar();
                        if (addEmailResponse == null) {
                            getView().onAddEmailFail("Add email failed");
                            return;
                        }

                        if (addEmailResponse.getError()) {
                            getView().onAddEmailFail(addEmailResponse.getMsg());
                            return;
                        }

                        Hawk.put(Constants.EMAIL_PHONE, email);
                        AccountRepo.getInstance().addEmail(email, new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                GlobalUtils.showLog(TAG, "email added to current user");
                                getView().onAddEmailSuccess();
                            }

                            @Override
                            public void fail() {
                                GlobalUtils.showLog(TAG,
                                        "failed to add email to current user");
                            }
                        });

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                }));
    }

    @Override
    public void setPhoneVerified() {
        AccountRepo.getInstance().setPhoneVerified(new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "Set user phone verified");
                getView().onDataChange("Phone Verified");
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG,
                        "failed to set user phone verified");
            }
        });
    }

    @Override
    public void setEmailVerified() {
        AccountRepo.getInstance().setEmailVerified(new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "Set user email verified");
                getView().onDataChange("Email Verified");
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG,
                        "failed to set user email verified");
            }
        });
    }

    private void setProfilePicToUser(String refId) {
        AccountRepo.getInstance().addProfilePicUrl(refId, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "profile pic saved");
                getView().onUploadImageSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save profile pic");
            }
        });
    }

    private byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream);
        return stream.toByteArray();
    }

    private String getMimeType(Uri uri) {
        String mimeType;
        if (Objects.equals(uri.getScheme(), ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private Retrofit getRetrofitInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .build();

        String base_url = Hawk.get(Constants.BASE_URL);

        return new Retrofit.Builder()
                .baseUrl(base_url)
                .client(client)
                .addConverterFactory(ProtoConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    private boolean validateEmail(String email) {

        if (ValidationUtils.isEmpty(email)) {
            getView().showInvalidEmailError();
            return false;
        }

        if (!ValidationUtils.isEmailValid(email)) {
            getView().showInvalidEmailError();
            return false;
        }

        return true;
    }

    private boolean validatePhone(String phone) {

        if (ValidationUtils.isEmpty(phone)) {
            getView().showInvalidPhoneError();
            return false;
        }

        if (phone.length() < 12) {
            getView().showInvalidPhoneError();
            return false;
        }

        return true;
    }
}
