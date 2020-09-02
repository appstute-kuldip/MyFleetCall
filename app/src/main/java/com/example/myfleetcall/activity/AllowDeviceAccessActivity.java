package com.example.myfleetcall.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.myfleetcall.R;

public class AllowDeviceAccessActivity extends AppCompatActivity {

    int PERMISSION_ALL = 1;
    ProgressBar progressBar_allow_device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allow_device_access);


        String[] PERMISSIONS = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.SEND_SMS,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

        }

        progressBar_allow_device = findViewById(R.id.progressbar_allow_device);
        progressBar_allow_device.setVisibility(View.VISIBLE);


    }

    private boolean hasPermissions(AllowDeviceAccessActivity allowDeviceAccess, String[] permissions) {
        return false;
    }

    private void navigateToSimVerify() {
        Intent intent = new Intent(AllowDeviceAccessActivity.this, VerifySimWithMobileNoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void navigateToMobileNumber() {
        Intent intent = new Intent(getApplicationContext(), MobileNumberActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getApplicationContext().startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_ALL) {
            //navigateToSimVerify();
            navigateToMobileNumber();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
