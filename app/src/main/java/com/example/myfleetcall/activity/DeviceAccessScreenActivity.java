package com.example.myfleetcall.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.myfleetcall.R;
import com.example.myfleetcall.services.ApiClient;
import com.example.myfleetcall.services.CheckValidityRequest;
import com.example.myfleetcall.services.CheckValidityResponse;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceAccessScreenActivity extends AppCompatActivity {

    Button button_lets_stated;
    private SharedPreferences mPreference;
    private RelativeLayout mainlayout;
    private LinearLayout titleLayout,contentLayout,btnLayout;
    private ProgressBar pLoading;
    String simID,deviceID,deviceID_2,mobileNumber,id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_access_screen);
        mainlayout = (RelativeLayout) this.findViewById(R.id.main_layout);
        pLoading = findViewById(R.id.prgLoading);
        titleLayout = findViewById(R.id.title_layout);
        contentLayout = findViewById(R.id.content_layout);
        btnLayout = findViewById(R.id.button_layout);

        //check validity sharedPref


        System.out.println(simID + " mob:" + mobileNumber);


        //check first time app install
        mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = mPreference.getBoolean("FIRSTRUN", true);
        if (isFirstRun) {
            // Code to run once
            //Toast.makeText(getApplicationContext(),"First time ",Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean("FIRSTRUN", false);
            //editor.commit();
            editor.apply();
        } else {
            //navigateToHomeActivity();
            //mainlayout.setVisibility(LinearLayout.GONE);
            titleLayout.setVisibility(LinearLayout.GONE);
            contentLayout.setVisibility(LinearLayout.GONE);
            btnLayout.setVisibility(LinearLayout.GONE);
            pLoading.setVisibility(View.VISIBLE);
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyFleetCall", MODE_PRIVATE);
            System.out.println("mob:"+prefs.getString("mobileNumber","no value"));
            simID = prefs.getString("simID", null);
            deviceID = prefs.getString("deviceID", null);
            deviceID_2 = prefs.getString("deviceID_2", null);
            mobileNumber = prefs.getString("mobileNumber", null);
            id = prefs.getString("id",null);
            System.out.println(simID+" "+deviceID+" "+deviceID_2+" "+mobileNumber+" "+id);
            checkValidity(createValRequest());

        }




        //prog color
        pLoading.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN );

//        SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyFleetCall", MODE_PRIVATE);
//        System.out.println("mob:"+prefs.getString("mobileNumber","no value"));

        button_lets_stated = findViewById(R.id.button_lets_started);

        button_lets_stated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigateToDeviceAccess();
                if (checkIfAlreadyhavePermission()) {
                    //navigateToVerifySimWithMob();
                    navigateToMobileNumber();
                } else {
                    navigateToDeviceAccess();
                }
            }
        });
    }



    private void navigateToDeviceAccess() {
        Intent intent = new Intent(getApplicationContext(), AllowDeviceAccessActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        finish();
    }

//    private void navigateToVerifySimWithMob() {
//        Intent intent = new Intent(getApplicationContext(), VerifySimWithMobileNoActivity.class);
      //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//        getApplicationContext().startActivity(intent);
//        finish();
//    }

    private void navigateToMobileNumber() {
        Intent intent = new Intent(getApplicationContext(), MobileNumberActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        finish();
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        finish();
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);

        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    public CheckValidityRequest createValRequest() {

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyFleetCall", MODE_PRIVATE);
        simID = prefs.getString("simID", null);
        deviceID = prefs.getString("deviceID", null);
        deviceID_2 = prefs.getString("deviceID_2", null);
        mobileNumber = prefs.getString("mobileNumber", null);
        id = prefs.getString("id",null);

        CheckValidityRequest checkValidityRequest = new CheckValidityRequest();

        System.out.println("mob:"+mobileNumber);
        System.out.println("id:"+id);
        System.out.println("simid:"+simID);
        System.out.println("deviceid:"+deviceID);
        System.out.println("device2:"+deviceID_2);
        checkValidityRequest.setMobileNumber(mobileNumber);
        checkValidityRequest.setId(id);
        checkValidityRequest.setSimId(simID);
        checkValidityRequest.setDeviceId(deviceID);
        checkValidityRequest.setDeviceId_2(deviceID_2);
        return checkValidityRequest;
    }

    public void checkValidity(CheckValidityRequest checkValidityRequest) {

        Call<CheckValidityResponse> checkValidityResponseCall = ApiClient.getUserService().checkValidity(checkValidityRequest);
        //System.out.println(simID+ " "+deviceID +" mob"+mobileNumber);
        checkValidityResponseCall.enqueue(new Callback<CheckValidityResponse>() {
            @Override
            public void onResponse(Call<CheckValidityResponse> call, Response<CheckValidityResponse> response) {

                if (response.isSuccessful()) {
                    Log.e("success", response.body().getMessage());
                    if (response.body().getMessage().equals("Success")) {
                        pLoading.setVisibility(View.GONE);
                        navigateToHomeActivity();

                    } else {
                        pLoading.setVisibility(View.GONE);
                        navigateToMobileNumberActivity();
                    }
                    //Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<CheckValidityResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Request failed, Please try again.", Toast.LENGTH_SHORT).show();
            }
        });


    }

//    private void navigateToHomeActivity() {
//        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//        getApplicationContext().startActivity(intent);
//        finish();
//    }

    private void navigateToMobileNumberActivity() {
        Intent intent = new Intent(getApplicationContext(), MobileNumberActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getApplicationContext().startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //checkValidity(createValRequest());
    }

}
