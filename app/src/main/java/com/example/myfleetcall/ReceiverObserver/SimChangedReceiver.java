package com.example.myfleetcall.ReceiverObserver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myfleetcall.activity.AllowDeviceAccessActivity;
import com.example.myfleetcall.activity.MobileNumberActivity;
import com.example.myfleetcall.activity.VerifySimWithMobileNoActivity;

import java.util.List;
import java.util.Set;

public class SimChangedReceiver extends BroadcastReceiver {

    private static final String EXTRA_SIM_STATE = "android.intent.action.SERVICE_STATE";

    static long start_time, end_time;
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public static final String TAG = "SmsBroadCastReceiver";
    String msg, phone = "";
    public static int slot = -1;
    MobileNumberActivity mobileNumberActivity;
    public static final int RECEIVE_SMS_PERMISSION = 0;
    AllowDeviceAccessActivity allowDeviceAccessActivity = new AllowDeviceAccessActivity();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("SimChangedReceiver", "--> SIM state changed <--");

        navigateToMobileNumber(context,intent);

    }

    private void navigateToMobileNumber(Context context,Intent intent) {
        //Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), MobileNumberActivity.class.getName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        //finish;
    }



}
