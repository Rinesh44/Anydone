package com.anydone.desk.ticketdetails.ticketattachment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.CollectionUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.anydone.desk.R;
import com.anydone.desk.adapters.AttachmentAdapter;
import com.anydone.desk.base.fragment.BaseFragment;
import com.anydone.desk.injection.component.ApplicationComponent;
import com.anydone.desk.realm.model.Attachment;
import com.anydone.desk.realm.model.Tickets;
import com.anydone.desk.realm.repo.TicketRepo;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.ImagesFullScreen;
import com.anydone.desk.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmList;

import static android.app.Activity.RESULT_OK;

public class TicketAttachmentFragment extends BaseFragment<TicketAttachmentPresenterImpl> implements
        TicketAttachmentContract.TicketAttachmentView {
    public static final int ATTACH_FILE_REQUEST_CODE = 3730;
    private static final String TAG = "TicketAttachmentFragmen";
    /*    @BindView(R.id.tv_ticket_title)
        TextView tvTicketTitle;
        @BindView(R.id.tv_ticket_desc)
        TextView tvTicketDesc;*/
    private Tickets ticket;
    /*    @BindView(R.id.scv_label)
        HorizontalScrollView scvLabel;
        @BindView(R.id.ll_label_holder)
        LinearLayout llLabels;*/
    @BindView(R.id.rv_attachments)
    RecyclerView rvAttachments;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    private long ticketId;
    private AttachmentAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_attachments;
    }


    @Override
    public void onViewCreated(@NonNull @io.reactivex.annotations.NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent i = Objects.requireNonNull(getActivity()).getIntent();
        ticketId = i.getLongExtra("selected_ticket_id", -1);

        ticket = TicketRepo.getInstance().getTicketById(ticketId);
        setupAttachmentRecyclerView(rvAttachments, ticket);
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }


    @Override
    public void showProgressBar(String message) {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String message) {
        if (message.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), Constants.SERVER_ERROR);
            onAuthorizationFailed(getActivity());
            return;
        }
        UiUtils.showToast(getActivity(), Constants.SERVER_ERROR);
    }

    private void setupAttachmentRecyclerView(RecyclerView rvAttachments, Tickets ticket) {
        GlobalUtils.showLog(TAG, "attachment setup");
        List<Attachment> attachmentList = ticket.getAttachmentList();
        if (attachmentList == null || attachmentList.isEmpty()) {
            attachmentList = new ArrayList<>();
            Attachment addAttachment = new Attachment();
            addAttachment.setId(UUID.randomUUID().toString().replace("-", ""));
            addAttachment.setType(0);

            attachmentList.add(addAttachment);
            rvAttachments.setLayoutManager(new GridLayoutManager(getContext(), 3));

            adapter = new AttachmentAdapter(attachmentList, getContext());
            rvAttachments.setAdapter(adapter);

            adapter.setOnAddAttachmentClickListener(() -> {
                GlobalUtils.showLog(TAG, "comment adapter received add click");
                accessExternalStoragePermissions();
            });

            adapter.setOnAttachmentImageClickListener((pos, imagesList) -> {
                GlobalUtils.showLog(TAG, "image click listened on top level");
                GlobalUtils.showLog(TAG, "url list size: " + imagesList.size());
//            Collections.reverse(imagesList);
                if (!CollectionUtils.isEmpty(imagesList)) {
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("images", (ArrayList<String>) imagesList);
                    bundle.putInt("position", pos);

                    FragmentTransaction ft = Objects.requireNonNull(getActivity())
                            .getSupportFragmentManager().beginTransaction();
                    ImagesFullScreen newFragment = ImagesFullScreen.newInstance();
                    newFragment.setArguments(bundle);
                    newFragment.show(ft, "slideshow");
                }
            });

            adapter.setOnAttachmentRemoveListener(this::showRemoveAttachmentDialog);

            adapter.setOnDocClickListener(attachment -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(attachment.getUrl()));
                Objects.requireNonNull(getContext()).startActivity(browserIntent);
            });

        } else {
            rvAttachments.setLayoutManager(new GridLayoutManager(getContext(), 3));
            AttachmentAdapter adapter = new AttachmentAdapter(attachmentList, getContext());
            rvAttachments.setAdapter(adapter);
            adapter.setData(attachmentList);

            adapter.setOnAddAttachmentClickListener(this::accessExternalStoragePermissions);

            adapter.setOnAttachmentImageClickListener((pos, imagesList) -> {
                GlobalUtils.showLog(TAG, "image click listened on top level");
                GlobalUtils.showLog(TAG, "url list size: " + imagesList.size());
//            Collections.reverse(imagesList);
                if (!CollectionUtils.isEmpty(imagesList)) {
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("images", (ArrayList<String>) imagesList);
                    bundle.putInt("position", pos);

                    FragmentTransaction ft = Objects.requireNonNull(getActivity())
                            .getSupportFragmentManager().beginTransaction();
                    ImagesFullScreen newFragment = ImagesFullScreen.newInstance();
                    newFragment.setArguments(bundle);
                    newFragment.show(ft, "slideshow");
                }
            });

            adapter.setOnAttachmentRemoveListener(this::showRemoveAttachmentDialog);

            adapter.setOnDocClickListener(attachment -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(attachment.getUrl()));
                Objects.requireNonNull(getContext()).startActivity(browserIntent);
            });
        }
    }

    private void accessExternalStoragePermissions() {
        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport
                                                             multiplePermissionsReport) {
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getContext(),
                                    "Media/files access permissions are required",
                                    Toast.LENGTH_LONG).show();
                            openAppSettings();
                        }

                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            openAttachmentOptions();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void showRemoveAttachmentDialog(Attachment attachment) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Remove attachment?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Cancel",
                (dialog, id) -> dialog.cancel());

        builder1.setNegativeButton(
                "Remove",
                (dialog, id) -> {
                    presenter.removeAttachment(ticketId, attachment);
                    dialog.dismiss();
                });

        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(getResources().getColor(R.color.colorPrimary));

        });
        alert11.show();
    }

    private void openAttachmentOptions() {
        Uri selectedUri = Uri.parse(String.valueOf(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS)));
        GlobalUtils.showLog(TAG, "selectedUri: " + selectedUri);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setDataAndType(selectedUri, "*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, ATTACH_FILE_REQUEST_CODE);
    }


    private void openAppSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ATTACH_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
            ContentResolver cr = Objects.requireNonNull(getActivity()).getContentResolver();
            String mime = cr.getType(fileUri);

            GlobalUtils.showLog(TAG, "mime type check: " + mime);
            GlobalUtils.showLog(TAG, "filename check: " + fileUri.getLastPathSegment());
            String filePath = fileUri.getLastPathSegment();
//            File file = new File(Objects.requireNonNull(GlobalUtils.getPath(uri, getContext())));
            assert filePath != null;
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            GlobalUtils.showLog(TAG, "file name check: " + fileName);
            assert mime != null;
            if (mime.equalsIgnoreCase("image/jpeg")
                    || mime.equalsIgnoreCase("image/png")
                    || mime.equalsIgnoreCase("image/webp")) {
                presenter.uploadImageAttachment(fileUri, getActivity(), fileName);
            } else {
                presenter.uploadFileAttachment(fileUri, fileName);
            }
        }

    }

    @Override
    public void onUploadImageAttachmentSuccess(String url, String title) {
        Attachment attachment = new Attachment();
        attachment.setId(UUID.randomUUID().toString().replace("-", ""));
        attachment.setType(1);
        attachment.setCreatedAt(System.currentTimeMillis());
        attachment.setTitle(title);
        attachment.setUpdatedAt(System.currentTimeMillis());
        attachment.setUrl(url);

        presenter.addAttachment(ticketId, attachment);
    }

    @Override
    public void onUploadImageAttachmentFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUploadFileAttachmentSuccess(String url, String title) {
        String extension = url.substring(url.lastIndexOf(".")).toLowerCase();
        GlobalUtils.showLog(TAG, "exe check: " + extension);
        Attachment attachment = new Attachment();
        switch (extension) {
            case ".pdf":
                GlobalUtils.showLog(TAG, "caught on pdf type");
                attachment.setType(2);
                break;

            case ".doc":
                attachment.setType(3);
                break;

            case ".xls":
                attachment.setType(4);
                break;

            case ".jpg":

            case ".png":

            case ".webp":
                attachment.setType(1);
                break;
        }
        attachment.setId(UUID.randomUUID().toString().replace("-", ""));
        attachment.setCreatedAt(System.currentTimeMillis());
        attachment.setTitle(title);
        attachment.setUpdatedAt(System.currentTimeMillis());
        attachment.setUrl(url);

        presenter.addAttachment(ticketId, attachment);
    }

    @Override
    public void onUploadFileAttachmentFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addAttachmentSuccess(Attachment attachment) {
        Tickets tickets = TicketRepo.getInstance().getTicketById(ticketId);
        RealmList<Attachment> existingAttachment = tickets.getAttachmentList();
        GlobalUtils.showLog(TAG, "existing attachment: " + existingAttachment.size());
        if (existingAttachment == null) existingAttachment = new RealmList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmList<Attachment> finalExistingAttachment = existingAttachment;
        realm.executeTransaction(realm1 -> finalExistingAttachment.add(attachment));
        GlobalUtils.showLog(TAG, "final existing attachment: " + finalExistingAttachment.size());
        TicketRepo.getInstance().addAttachments(ticketId, finalExistingAttachment);
        adapter.setData(finalExistingAttachment);
    }

    @Override
    public void addAttachmentFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), Constants.SERVER_ERROR);
            onAuthorizationFailed(getActivity());
            return;
        }
        UiUtils.showToast(getActivity(), msg);
    }

    @Override
    public void removeAttachmentSuccess(Attachment attachment) {
        adapter.removeAttachment(attachment);
    }

    @Override
    public void removeAttachmentFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getActivity(), Constants.SERVER_ERROR);
            onAuthorizationFailed(getActivity());
            return;
        }
        UiUtils.showToast(getActivity(), msg);
    }
}

