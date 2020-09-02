package com.example.myfleetcall.Firebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.myfleetcall.R;
import com.example.myfleetcall.activity.HomeActivity;
import com.example.myfleetcall.services.ApiClient;
import com.example.myfleetcall.services.InitCallRequest;
import com.example.myfleetcall.services.InitCallResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallMobile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_mobile);

        //navigateToHome();


        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1251);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    111);
        }
        initCallStatus(createInitCallRequest());

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+MyFirebaseMessagingService.CallTo));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(callIntent);
        finish();
    }


    public InitCallRequest createInitCallRequest(){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyFleetCall", MODE_PRIVATE);
        String id = prefs.getString("id","no value");

        InitCallRequest initCallRequest = new InitCallRequest();
        initCallRequest.setId(id);
        return initCallRequest;
    }

    public void initCallStatus(InitCallRequest initCallRequest) {

        Call<InitCallResponse> initCallResponseCall = ApiClient.getUserService().initCallStatus(initCallRequest);
        initCallResponseCall.enqueue(new Callback<InitCallResponse>() {
            @Override
            public void onResponse(Call<InitCallResponse> call, Response<InitCallResponse> response) {

                if (response.isSuccessful()) {
                    Log.d("initCallStatus","initCall success");
                }
            }

            @Override
            public void onFailure(Call<InitCallResponse> call, Throwable t) {
                Log.d("initCallStatusError","initCall error");
            }
        });
    }

    private void navigateToHome() {
        Intent callIntent = new Intent(this, HomeActivity.class);
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(callIntent);
        finish();
    }
}
