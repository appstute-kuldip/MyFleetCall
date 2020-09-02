package com.example.myfleetcall.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.myfleetcall.Firebase.CallMobile;
import com.example.myfleetcall.Firebase.MyFirebaseMessagingService;
import com.example.myfleetcall.R;
import com.firebase.client.Firebase;

public class HomeActivity extends AppCompatActivity {

//    Button btn_Call;
//    private Firebase mRef;
//    private String callingTo = null;
//    private String simIDFromServer = null;
//    private String simIDFromDevice = null;
    public static boolean flagCallDetails = false;
    //private NotificationManagerCompat notificationManager;
    private NotificationChannel mChannel;
    private SharedPreferences.Editor mPreference;
    //String CallTo;
    String NMobileNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Firebase.setAndroidContext(this);

        mPreference = getApplicationContext().getSharedPreferences("MyFleetCall", MODE_PRIVATE).edit();

        //btn_Call = findViewById(R.id.button_call);
        //checkValidity(createValRequest());


    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyFleetCall", MODE_PRIVATE);
        System.out.println("mob:" + prefs.getString("mobileNumber", "no value"));
        boolean FlagNoti = prefs.getBoolean("FlagNoti", false);
        NMobileNumber = prefs.getString("NMobileNumber",null);


        if (FlagNoti) {

            //Toast.makeText(getApplicationContext(), "Call", Toast.LENGTH_SHORT).show();
            //navigateToMakeACall();
            //makeACall();
            showAlertDialog("You have a new Call request : " , NMobileNumber);
            mPreference.putBoolean("FlagNoti", false);
            mPreference.apply();
            mPreference.commit();

        }

    }

    //
    @Override
    public void onPause() {
        super.onPause();

    }


    private void navigateToMakeACall() {
        Intent intent = new Intent(getApplicationContext(), CallMobile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        finish();
    }

    private void makeACall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    111);
        }
        //initCallStatus(createInitCallRequest());

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+NMobileNumber));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(callIntent);
        //finish();
    }

    private void showAlertDialog(String stringToShow,String NMobileNumber){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(stringToShow + NMobileNumber);
        dlgAlert.setTitle("MyFleetCall");
        dlgAlert.setPositiveButton("CALL", null);
        dlgAlert.setCancelable(true);


        dlgAlert.setPositiveButton(
                "CALL",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        makeACall();
                    }
                }
        );
        dlgAlert.create().show();

    }
}
