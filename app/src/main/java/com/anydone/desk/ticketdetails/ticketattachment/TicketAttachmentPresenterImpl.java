package com.anydone.desk.ticketdetails.ticketattachment;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.model.Attachment;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.UriUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
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

public class TicketAttachmentPresenterImpl extends BasePresenter<TicketAttachmentContract.TicketAttachmentView>
        implements TicketAttachmentContract.TicketAttachmentPresenter {
    private TicketAttachmentRepository ticketAttachmentRepository;
    private static final String TAG = "TicketAttachmentPresent";

    @Inject
    public TicketAttachmentPresenterImpl(TicketAttachmentRepository ticketAttachmentRepository) {
        this.ticketAttachmentRepository = ticketAttachmentRepository;
    }

    @Override
    public void uploadImageAttachment(Uri uri, Activity activity, String title) {
        getView().showProgressBar("");
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(uri, "Uri cannot be null");

        Observable<UserRpcProto.UserBaseResponse> imageUploadObservable;

        try {
            Bitmap decodedBitmap = MediaStore.Images.Media
                    .getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), uri);

            Bitmap bitmap = GlobalUtils.fixBitmapRotation(uri, decodedBitmap, activity);
            byte[] byteArray = getByteArrayFromBitmap(bitmap);
            String mimeType = getMimeType(uri);

            RequestBody imageReqBody = RequestBody.create(MediaType.parse(mimeType), byteArray);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("image",
                            "image.jpg", imageReqBody);

            Retrofit retrofit = getRetrofitInstance();

            AnyDoneService service = retrofit.create(AnyDoneService.class);

            imageUploadObservable = service.imageUploadConversation(Hawk.get(Constants.TOKEN), body);

            addSubscription(imageUploadObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                        @Override
                        public void onNext(@NonNull UserRpcProto.UserBaseResponse uploadPicResponse) {
                            GlobalUtils.showLog(TAG, "upload pic attachment response: "
                                    + uploadPicResponse);

                            getView().hideProgressBar();
                            if (uploadPicResponse == null) {
                                getView().onUploadImageAttachmentFail("Failed to upload file");
                                return;
                            }

                            if (uploadPicResponse.getError()) {
                                getView().onUploadImageAttachmentFail("Failed to upload file");
                                return;
                            }

                            getView().onUploadImageAttachmentSuccess(uploadPicResponse.getRefId(), title);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            getView().hideProgressBar();
                            getView().onUploadImageAttachmentFail("Failed to upload file");
                        }

                        @Override
                        public void onComplete() {
                            getView().hideProgressBar();
                        }
                    })
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadFileAttachment(Uri uri, String title) {
        getView().showProgressBar("");
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(uri, "Uri cannot be null");

        Observable<UserRpcProto.UserBaseResponse> docUploadObservable;

        String fullFilePath = UriUtils.getPathFromUri(getContext(), uri);
        assert fullFilePath != null;
        File file = new File(fullFilePath);
        String mimeType = getMimeType(uri);

        GlobalUtils.showLog(TAG, "file check: " + file.getName());
        RequestBody docReqBody = RequestBody.create(MediaType.parse(mimeType), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("doc", file.getName(), docReqBody);

        Retrofit retrofit = getRetrofitInstance();

        AnyDoneService service = retrofit.create(AnyDoneService.class);

        docUploadObservable = service.docUploadConversation(Hawk.get(Constants.TOKEN), body);

        addSubscription(docUploadObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(@NonNull UserRpcProto.UserBaseResponse uploadDocResponse) {
                        GlobalUtils.showLog(TAG, "upload doc attachment response: " + uploadDocResponse);

                        getView().hideProgressBar();

                        if (uploadDocResponse.getError()) {
                            getView().onUploadFileAttachmentFail("Failed to upload file");
                            return;
                        }

                        getView().onUploadFileAttachmentSuccess(uploadDocResponse.getRefId(), title);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().onUploadFileAttachmentFail("Failed to upload file");
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );
    }

    @Override
    public void addAttachment(long ticketId, Attachment attachment) {
        TicketProto.TicketAttachment.TicketAttachmentType attachmentType;

        if (attachment.getType() == 1) {
            attachmentType = TicketProto.TicketAttachment.TicketAttachmentType.IMAGE_TYPE;
        } else {
            attachmentType = TicketProto.TicketAttachment.TicketAttachmentType.DOC_TYPE;
        }

        TicketProto.TicketAttachment ticketAttachment = TicketProto.TicketAttachment.newBuilder()
                .setCreatedAt(System.currentTimeMillis())
                .setId(attachment.getId())
                .setTitle(attachment.getTitle())
                .setType(attachmentType)
                .setUrl(attachment.getUrl())
                .build();

        TicketProto.TicketAttachmentRequest ticketAttachmentRequest = TicketProto
                .TicketAttachmentRequest.newBuilder()
                .setTicketId(ticketId)
                .addTicketAttachments(ticketAttachment)
                .build();

        getView().showProgressBar("Please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String token = Hawk.get(Constants.TOKEN);
        GlobalUtils.showLog(TAG, "sent attachment id: " + attachment.getId());

        ticketObservable = service.addAttachment(token, ticketAttachmentRequest);
        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(@NonNull TicketServiceRpcProto.TicketBaseResponse
                                               response) {
                        GlobalUtils.showLog(TAG, "add attachment response: " +
                                response);

                        getView().hideProgressBar();

                        if (response.getError()) {
                            getView().addAttachmentFail(response.getMsg());
                            return;
                        }

                        attachment.setId(response.getAttachmentsList().get(0).getId());
                        getView().addAttachmentSuccess(attachment);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().addAttachmentFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );
    }

    @Override
    public void removeAttachment(long ticketId, Attachment attachment) {
        getView().showProgressBar("Please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String token = Hawk.get(Constants.TOKEN);

        TicketProto.TicketAttachment ticketAttachment = TicketProto.TicketAttachment.newBuilder()
                .setId(attachment.getId())
                .build();

        TicketProto.TicketAttachmentRequest ticketAttachmentRequest = TicketProto.TicketAttachmentRequest.newBuilder()
                .setTicketId(ticketId)
                .addTicketAttachments(ticketAttachment)
                .build();

        ticketObservable = service.removeAttachment(token,
                ticketAttachmentRequest);

        GlobalUtils.showLog(TAG, "send attachment proto: " + ticketAttachmentRequest);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                               response) {
                        GlobalUtils.showLog(TAG, "remove attachment response: " +
                                response);

                        getView().hideProgressBar();
                        if (response == null) {
                            getView().removeAttachmentFail("Failed to remove attachment");
                            return;
                        }

                        if (response.getError()) {
                            getView().removeAttachmentFail(response.getMsg());
                            return;
                        }

                        getView().removeAttachmentSuccess(attachment);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().removeAttachmentFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );
    }

    private byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 50, stream);
        return stream.toByteArray();
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

}
