package com.example.myfleetcall.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.myfleetcall.Firebase.Members;
import com.example.myfleetcall.R;
import com.example.myfleetcall.services.ApiClient;
import com.example.myfleetcall.services.RequestVerifyMobile;
import com.example.myfleetcall.services.ResponseVerifyMobile;
import com.example.myfleetcall.services.UserRequest;
import com.example.myfleetcall.services.UserResponse;
import com.firebase.client.Firebase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifySimWithMobileNoActivity extends AppCompatActivity {

    RadioButton radioButton_sim1, radioButton_sim2;
    private RadioGroup radioGroup;
    private TableRow tableRow1, tableRow2;
    private Button button_next;
    private ProgressBar progressBar_nextButton;
    Members members;
    String getRadioBtnText = "";
    String getSimID, getDeviceID, getDeviceID_2;
    String serverID;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_sim_with_mobile_no);

        radioButton_sim1 = findViewById(R.id.radioButton_sim1);
        radioButton_sim2 = findViewById(R.id.radioButton_sim2);
        radioGroup = findViewById(R.id.radiogrp_sims);
        tableRow1 = findViewById(R.id.hr);
        tableRow2 = findViewById(R.id.hr2);
        button_next = findViewById(R.id.button_next);
        progressBar_nextButton = findViewById(R.id.progressbar_nextButton);
        radioButton_sim1.setText("   SIM 1 - ");
        radioButton_sim2.setText("   SIM 2 - ");

        radioGroup.clearCheck();

        members = new Members();
        Firebase.setAndroidContext(this);


        if (ActivityCompat.checkSelfPermission(VerifySimWithMobileNoActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        TelephonyManager manager = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        Log.i("OmSai ", "Single or Dula Sim " + manager.getPhoneCount());
        Log.i("OmSai ", "Defualt device ID " + manager.getDeviceId());
        Log.i("OmSai ", "Single 1 " + manager.getDeviceId(0));
        Log.i("OmSai ", "Single 2 " + manager.getDeviceId(1));
        Log.i("OmSai ", "sim 2 " + manager.getSimSerialNumber());


        SubscriptionManager subscriptionManager = SubscriptionManager.from(getApplicationContext());
        final List<SubscriptionInfo> subsInfoList = subscriptionManager.getActiveSubscriptionInfoList();


        for (SubscriptionInfo subscriptionInfo : subsInfoList) {

            Integer index = subscriptionInfo.getSimSlotIndex();
            System.out.println(subscriptionInfo);
            if (index == 1) {
                //Log.d("Test", " Number is  " + subscriptionInfo.getNumber());
                Log.d("Test sim2", " Number is  " + subscriptionInfo.getDisplayName() + subscriptionInfo.getIccId());
                radioButton_sim2.setText("   SIM 2 - " + subscriptionInfo.getDisplayName());//+subscriptionInfo.getIccId()
                getSimID = subscriptionInfo.getIccId();
                getDeviceID = manager.getDeviceId(0);
                getDeviceID_2 = manager.getDeviceId(1);
            } else {
                //Log.d("Test", " Number is  " + subscriptionInfo.getNumber());
                Log.d("Test sim1", " Number is  " + subscriptionInfo.getDisplayName());
                radioButton_sim1.setText("   SIM 1 - " + subscriptionInfo.getDisplayName());//+subscriptionInfo.getIccId()
                getSimID = subscriptionInfo.getIccId();
                getDeviceID = manager.getDeviceId(0);
                getDeviceID_2 = manager.getDeviceId(1);
            }
        }


        SubscriptionManager sManager = (SubscriptionManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SUBSCRIPTION_SERVICE);
        SubscriptionInfo infoSim1 = sManager.getActiveSubscriptionInfoForSimSlotIndex(0);
        SubscriptionInfo infoSim2 = sManager.getActiveSubscriptionInfoForSimSlotIndex(1);
        if (infoSim1 == null) {
            //count++;
            radioButton_sim1.setVisibility(View.GONE);
            tableRow1.setVisibility(View.GONE);
        }
        if (infoSim2 == null) {
            //count++;
            radioButton_sim2.setVisibility(View.GONE);
            tableRow2.setVisibility(View.GONE);
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    //Toast.makeText(getApplicationContext(), rb.getText().toString().substring(0,8), Toast.LENGTH_SHORT).show();
                    getRadioBtnText = rb.getText().toString().substring(0, 8);

                }
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar_nextButton.setVisibility(View.VISIBLE);

                v.getBackground().setAlpha(100);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(getApplicationContext(), "Nothing selected..", Toast.LENGTH_SHORT).show();

                } else {
                    //Toast.makeText(getApplicationContext(), getRadioBtnText, Toast.LENGTH_SHORT).show();
                    if (getRadioBtnText.equals("   SIM 2")) {
                        //Toast.makeText(getApplicationContext(), "matched", Toast.LENGTH_SHORT).show();
                        saveUser(createRequest());
                        //verifyMobileNumber(createVerifyRequest());

                    } else {
                        saveUser(createRequest());
                        //verifyMobileNumber(createVerifyRequest());
                    }
                }


            }
        });

    }


    private void navigateToMobileVerifiedActivity() {
        Intent intent = new Intent(getApplicationContext(), MobileVerifiedActivity.class);
        getApplicationContext().startActivity(intent);
        finish();
    }


    public UserRequest createRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setSim_Id(getSimID);
        userRequest.setDevice_Id(getDeviceID);
        userRequest.setDevice_Id_2(getDeviceID_2);

        return userRequest;
    }

    public RequestVerifyMobile createVerifyRequest() {
        RequestVerifyMobile requestVerifyMobile = new RequestVerifyMobile();
        requestVerifyMobile.setBody("<"+serverID+">");
        requestVerifyMobile.setFrom("9890023065");

        return requestVerifyMobile;
    }

    public void saveUser(UserRequest userRequest) {

        Call<UserResponse> userResponseCall = ApiClient.getUserService().saveUser(userRequest);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if (response.isSuccessful()) {
                    Log.e("success", response.body().getMessage() + " " + response.body().getId());
                    if (response.body().getMessage().equals("Already present")) {

                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        serverID = response.body().getId();
                        Toast.makeText(getApplicationContext(), "Verifying mobile number..", Toast.LENGTH_SHORT).show();
                        verifyMobileNumber(createVerifyRequest());
                        navigateToMobileVerifiedActivity();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "request failed.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "request failed." + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void verifyMobileNumber(RequestVerifyMobile requestVerifyMobile) {

        Call<ResponseVerifyMobile> verifyMobileCall = ApiClient.getUserService().verifyPhoneNumber(requestVerifyMobile);

        verifyMobileCall.enqueue(new Callback<ResponseVerifyMobile>() {
            @Override
            public void onResponse(Call<ResponseVerifyMobile> call, Response<ResponseVerifyMobile> response) {
                //Toast.makeText(getApplicationContext(), "success mobile"+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "success mobile", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "request failed."+response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseVerifyMobile> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "request failed." + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
