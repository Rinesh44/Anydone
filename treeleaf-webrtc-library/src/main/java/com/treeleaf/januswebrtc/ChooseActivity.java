package com.treeleaf.januswebrtc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class ChooseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {

    private static final String TAG = ChooseActivity.class.getSimpleName();
    private EditText etRoomNumber;
    private static final int RC_CALL = 111;
    private static final String[] perms = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

    public static void launch(Context context) {
        Intent intent = new Intent(context, ChooseActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_activity);
        etRoomNumber = (EditText) findViewById(R.id.et_room_number);
        findViewById(R.id.btn_start_client).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openClientActivity();
            }
        });
        findViewById(R.id.btn_start_server).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openServerActivity();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_CALL)
    public void openClientActivity() {
        if (EasyPermissions.hasPermissions(this, perms)) {
            startActivity(new Intent(ChooseActivity.this, ClientActivity.class));
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this, "Need some permissions", RC_CALL, perms);
        }
    }

    @AfterPermissionGranted(RC_CALL)
    public void openServerActivity() {
        if (EasyPermissions.hasPermissions(this, perms)) {
            String roomNumber = etRoomNumber.getText().toString().trim();
            if (!roomNumber.isEmpty()) {
                Intent intent = new Intent(ChooseActivity.this, ServerActivity.class);
                intent.putExtra("extra_room_number", roomNumber);
                startActivity(intent);
            } else {
                Toast.makeText(ChooseActivity.this, "Enter room number", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this, "Enable camera and microphone permissions", RC_CALL, perms);
        }

    }


    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        Log.d(TAG, "onRationaleAccepted:" + requestCode);
    }

    @Override
    public void onRationaleDenied(int requestCode) {

    }


}
