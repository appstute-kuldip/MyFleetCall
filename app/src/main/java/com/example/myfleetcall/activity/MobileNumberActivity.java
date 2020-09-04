package com.example.myfleetcall.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myfleetcall.R;

import com.example.myfleetcall.services.ApiClientSMS;
import com.example.myfleetcall.services.RequestSMS;
import com.example.myfleetcall.services.ResponseSMS;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileNumberActivity extends AppCompatActivity {

    private EditText eMNumber;
    private Button btnNext;
    private String mNumber = "";
    public static ProgressBar mProgressBar;
    public static String simID, deviceID, deviceID_2, mobileNumber = "";
    public static final int RECEIVE_SMS_PERMISSION = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number);

        eMNumber = findViewById(R.id.field_phone_number);
        btnNext = findViewById(R.id.btn_next);
        mProgressBar = findViewById(R.id.progressbar);


        //getPermission();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) !=
                PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},
                        RECEIVE_SMS_PERMISSION);
            }
        }
        getSimDetails(0);
        //System.out.println("sim slot:"+SMSReceiver.slot);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getSMSFromApi(createRequestSMS("9890023065"));
                btnNext.setEnabled(false);
                mNumber = eMNumber.getText().toString();
                mobileNumber = eMNumber.getText().toString();
                Log.d("mNumber", mNumber);
                System.out.println("mn:" + mNumber);
                if (mNumber.isEmpty() || mNumber.length() < 10) {
                    eMNumber.setError("Enter a valid mobile number");
                    eMNumber.requestFocus();
                    return;
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    getSMSFromApi(createRequestSMS());
                    //getSimDetails(getApplicationContext());
                }
            }
        });

    }

    public RequestSMS createRequestSMS() {
        RequestSMS requestSMS = new RequestSMS();
        requestSMS.setMobileNumber(mNumber);
        requestSMS.setSmsBody("Registration successful. Welcome to MyFleetCall.");
        return requestSMS;
    }

    public void getSMSFromApi(final RequestSMS requestSMS) {

        Call<ResponseSMS> responseSMSCall = ApiClientSMS.getUserService().sendMobileNumber(requestSMS);
        responseSMSCall.enqueue(new Callback<ResponseSMS>() {
            @Override
            public void onResponse(Call<ResponseSMS> call, Response<ResponseSMS> response) {

                if (response.isSuccessful()) {
                    Log.e("success", response.body().getMessage());
                    if (response.body().getMessage().equals("Number not whitelisted")) {
                        //Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), "Number is not whitelisted", Toast.LENGTH_SHORT).show();
                        showAlertDialog("You are not authorized to register.");
                        btnNext.setEnabled(true);
                        mProgressBar.setVisibility(View.GONE);
                    } else {
                        //Toast.makeText(getApplicationContext(), "Number is whitelisted", Toast.LENGTH_SHORT).show();


                    }
                }
//
                //mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ResponseSMS> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Request failed, Please try again.", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);

            }
        });
    }

    private void showAlertDialog(String stringToShow) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(stringToShow);
        dlgAlert.setTitle("MyFleetCall");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

    }

    public void getSimDetails(int slot) {

        System.out.println("Slot inside:" + slot);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        TelephonyManager manager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);

        SubscriptionManager subscriptionManager = SubscriptionManager.from(this);
        final List<SubscriptionInfo> subsInfoList = subscriptionManager.getActiveSubscriptionInfoList();

        if (subsInfoList == null) {
            Toast.makeText(this, "No SIM card detected..", Toast.LENGTH_SHORT).show();
            eMNumber.setEnabled(false);
            btnNext.setEnabled(false);
        } else {
            eMNumber.setEnabled(true);
            btnNext.setEnabled(true);
            for (SubscriptionInfo subscriptionInfo : subsInfoList) {

                Integer index = subscriptionInfo.getSimSlotIndex();
                System.out.println(subscriptionInfo);
                if (slot == 1) {
                    //Log.d("Test", " Number is  " + subscriptionInfo.getNumber());
                    Log.d("Test sim2", " Number is  " + subscriptionInfo.getDisplayName() + subscriptionInfo.getIccId());
                    //Toast.makeText(context, subscriptionInfo.getIccId(), Toast.LENGTH_SHORT).show();
                    //sim1Info = subscriptionInfo.getIccId();
                    //radioButton_sim2.setText("   SIM 2 - " + subscriptionInfo.getDisplayName());//+subscriptionInfo.getIccId()
//                    simID = subscriptionInfo.getIccId();
//                    deviceID = manager.getDeviceId(0);
//                    deviceID_2 = manager.getDeviceId(1);
                } else {
                    //Log.d("Test", " Number is  " + subscriptionInfo.getNumber());
                    Log.d("Test sim1", " Number is  " + subscriptionInfo.getDisplayName() + subscriptionInfo.getIccId());
                    //Toast.makeText(context, subscriptionInfo.getIccId(), Toast.LENGTH_SHORT).show();
                    //sim2Info = subscriptionInfo.getIccId();
                    //radioButton_sim1.setText("   SIM 1 - " + subscriptionInfo.getDisplayName());//+subscriptionInfo.getIccId()
//                    simID = subscriptionInfo.getIccId();
//                    deviceID = manager.getDeviceId(0);
//                    deviceID_2 = manager.getDeviceId(1);
                }
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case RECEIVE_SMS_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "thanks..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Not Granted..", Toast.LENGTH_SHORT).show();

                }
        }
    }

}
