package com.example.myfleetcall.ReceiverObserver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.myfleetcall.Firebase.MyFirebaseMessagingService;
import com.example.myfleetcall.activity.HomeActivity;
import com.example.myfleetcall.activity.MobileNumberActivity;
import com.example.myfleetcall.activity.MobileVerifiedActivity;
import com.example.myfleetcall.services.ApiClient;
import com.example.myfleetcall.services.UserRequest;
import com.example.myfleetcall.services.UserResponse;

import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class SMSReceiver extends BroadcastReceiver {

    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public static final String TAG = "SmsBroadCastReceiver";
    String msg, phone = "";
    MobileNumberActivity mobileNumberActivity;
    MyFirebaseMessagingService mFMS;
    private SharedPreferences.Editor mPreference;
    public static String simID, deviceID, deviceID_2;


    @Override
    public void onReceive(Context context, Intent intent) {

        mPreference = context.getSharedPreferences("MyFleetCall", MODE_PRIVATE).edit();
        Log.i(TAG, "Intent received" + intent.getAction());

        if (intent.getAction() == SMS_RECEIVED) {

            Bundle dataBundle = intent.getExtras();
            if (dataBundle != null) {
                Object[] mypdu = (Object[]) dataBundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[mypdu.length];

                for (int i = 0; i < mypdu.length; i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = dataBundle.getString("format");
                        messages[i] = SmsMessage.createFromPdu((byte[]) mypdu[i], format);
                    } else {
                        messages[i] = SmsMessage.createFromPdu((byte[]) mypdu[i]);
                    }


                    msg = messages[i].getMessageBody();
                    phone = messages[i].getOriginatingAddress();

                }
                //String reqMsg = "BP-Fleetm";
                //boolean res = reqMsg.equals(phone);
                //System.out.println("res"+res+"phone:"+phone);
//                String decData = getDecryptData(msg);
                System.out.println("msg data:" + msg.length());

//                System.out.println("match:"+MatchSMSString);
//                System.out.println("k:"+MobileNumberActivity.keyData);
                //getDecryptData(msg,MobileNumberActivity.keyData,MobileNumberActivity.ivData);
                if (msg.length() == 69) {//msg.equals("Registration successful. Welcome to MyFleetCall.")
                    //mobileNumberActivity.disableProgressBar();
                    //Toast.makeText(context,"Message :"+msg+" \nPhone:"+phone,Toast.LENGTH_SHORT).show();
                    String MatchSMSString = msg.substring(0, 48);
                    if (MatchSMSString.equals("Registration successful. Welcome to MyFleetCall.")) {
                        MobileNumberActivity.mProgressBar.setVisibility(View.GONE);
                        getSimSlot(context, intent);
                    }

                }
            }
        }

    }


    public void getSimSlot(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            int slot = -1;
            if (bundle != null) {
                Set<String> keySet = bundle.keySet();
                for (String key : keySet) {
                    switch (key) {
                        case "slot":
                            slot = bundle.getInt("slot", -1);
                            break;
                        case "simId":
                            slot = bundle.getInt("simId", -1);
                            break;
                        case "simSlot":
                            slot = bundle.getInt("simSlot", -1);
                            break;
                        case "slot_id":
                            slot = bundle.getInt("slot_id", -1);
                            break;
                        case "simnum":
                            slot = bundle.getInt("simnum", -1);
                            break;
                        case "slotId":
                            slot = bundle.getInt("slotId", -1);
                            break;
                        case "slotIdx":
                            slot = bundle.getInt("slotIdx", -1);
                            break;
                        default:
                            if (key.toLowerCase().contains("slot") | key.toLowerCase().contains("sim")) {
                                String value = bundle.getString(key, "-1");
                                if (value.equals("0") | value.equals("1") | value.equals("2")) {
                                    slot = bundle.getInt(key, -1);
                                }
                            }


                    }
                }

                Log.d("slot", "slot=>" + slot);
//                if (slot == 0) {
//                    Toast.makeText(context, "SIM - 1 j", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "SIM - 2 j", Toast.LENGTH_SHORT).show();
//                }
                //getSimDetails(context,slot);
                //mobileNumberActivity.getSimDetails(slot);
                getSimDetails(context, slot);
                saveUser(createRequest(), context);

            }

        } catch (Exception e) {
            Log.d(TAG, "Exception=>" + e);
        }

    }


    public UserRequest createRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setSim_Id(simID);
        userRequest.setDevice_Id(deviceID);
        userRequest.setDevice_Id_2(deviceID_2);
        userRequest.setMobileNumber(mobileNumberActivity.mobileNumber);
        userRequest.setFcmToken(mFMS.fcmToken);

        return userRequest;
    }

    public void saveUser(UserRequest userRequest, final Context context) {

        Call<UserResponse> userResponseCall = ApiClient.getUserService().saveUser(userRequest);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body().getMessage().equals("Success")) {

                        Toast.makeText(context, "Registered successfully.", Toast.LENGTH_SHORT).show();

                        mPreference.putString("simID", simID);
                        mPreference.putString("deviceID", deviceID);
                        mPreference.putString("deviceID_2", deviceID_2);
                        mPreference.putString("mobileNumber", mobileNumberActivity.mobileNumber);
                        mPreference.putString("id", response.body().getId());
                        mPreference.apply();
                        mPreference.commit();
                        SharedPreferences prefs = context.getSharedPreferences("MyFleetCall", MODE_PRIVATE);
                        System.out.println("mob:" + prefs.getString("deviceID", "no value"));
                        navigateToMobileVerifiedActivity(context);
                    } else {

                        Toast.makeText(context, "Welcome back to MyFleetCall.", Toast.LENGTH_SHORT).show();

                        mPreference.putString("simID", simID);
                        mPreference.putString("deviceID", deviceID);
                        mPreference.putString("deviceID_2", deviceID_2);
                        mPreference.putString("mobileNumber", mobileNumberActivity.mobileNumber);
                        mPreference.putString("id", response.body().getId());
                        mPreference.apply();
                        mPreference.commit();
                        navigateToHomeActivity(context);
                    }

                } else {
                    Toast.makeText(context, "Request failed.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(context, "Request failed." + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void navigateToMobileVerifiedActivity(Context context) {
        Intent intent = new Intent(context, MobileVerifiedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        //context.finish();
    }

    private void navigateToHomeActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        //context.finish();
    }


    public void getSimDetails(Context context, int slot) {

        System.out.println("Slot inside rec:" + slot);
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        TelephonyManager manager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);

        SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
        final List<SubscriptionInfo> subsInfoList = subscriptionManager.getActiveSubscriptionInfoList();

        if (subsInfoList == null) {
            //Toast.makeText(this,"No SIM card detected..",Toast.LENGTH_SHORT).show();
//            eMNumber.setEnabled(false);
//            btnNext.setEnabled(false);
        } else {
//            eMNumber.setEnabled(true);
//            btnNext.setEnabled(true);
            for (SubscriptionInfo subscriptionInfo : subsInfoList) {

                Integer index = subscriptionInfo.getSimSlotIndex();
                System.out.println(subscriptionInfo);
                if (slot == 1) {
                    //Log.d("Test", " Number is  " + subscriptionInfo.getNumber());
                    Log.d("Test sim2", " Number is  " + subscriptionInfo.getDisplayName() + subscriptionInfo.getIccId());
                    //Toast.makeText(context, subscriptionInfo.getIccId(), Toast.LENGTH_SHORT).show();
                    //sim1Info = subscriptionInfo.getIccId();
                    //radioButton_sim2.setText("   SIM 2 - " + subscriptionInfo.getDisplayName());//+subscriptionInfo.getIccId()
                    simID = subscriptionInfo.getIccId();
                    deviceID = manager.getDeviceId(0);
                    deviceID_2 = manager.getDeviceId(1);
                } else {
                    //Log.d("Test", " Number is  " + subscriptionInfo.getNumber());
                    Log.d("Test sim1", " Number is  " + subscriptionInfo.getDisplayName() + subscriptionInfo.getIccId());
                    //Toast.makeText(context, subscriptionInfo.getIccId(), Toast.LENGTH_SHORT).show();
                    //sim2Info = subscriptionInfo.getIccId();
                    //radioButton_sim1.setText("   SIM 1 - " + subscriptionInfo.getDisplayName());//+subscriptionInfo.getIccId()
                    simID = subscriptionInfo.getIccId();
                    deviceID = manager.getDeviceId(0);
                    deviceID_2 = manager.getDeviceId(1);
                }
            }
        }
    }
}
