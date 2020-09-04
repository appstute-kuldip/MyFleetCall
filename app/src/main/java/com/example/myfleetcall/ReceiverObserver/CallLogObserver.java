package com.example.myfleetcall.ReceiverObserver;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.myfleetcall.Firebase.MyFirebaseMessagingService;
import com.example.myfleetcall.activity.HomeActivity;
import com.example.myfleetcall.services.ApiClient;
import com.example.myfleetcall.services.CallDetailsRequest;
import com.example.myfleetcall.services.CallDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class CallLogObserver extends ContentObserver {

    private Context context;
    HomeActivity homeActivity;
    MyFirebaseMessagingService myFirebaseMessagingService;
    public static String mNumber = null;
    public static long callDuration = 0;

    public CallLogObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;

    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.i(TAG, "CallLogs OnChange()");
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                //callDura = null;
            } else if (MyFirebaseMessagingService.flagCallDetails) {
                Cursor c = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                        null, null, CallLog.Calls.DATE + " DESC");
                if (c != null) {
                    if (c.moveToFirst()) {
                        int type = Integer.parseInt(c.getString(c
                                .getColumnIndex(CallLog.Calls.TYPE)));
                        /*
                         * increase call counter for outgoing call only
                         */
                        if (type == 2) {
                            mNumber = c.getString(c
                                    .getColumnIndex(CallLog.Calls.NUMBER));

                            callDuration = c.getLong(c
                                    .getColumnIndex(CallLog.Calls.DURATION));

                            Log.i(TAG, "number = " + mNumber + " type = " + type + " duration = " + callDuration);

                            saveCallDetails(createCallRequest(), context);
                            homeActivity.flagCallDetails = false;
                            registerListener(context);
                        }
                    }
                    c.close();
                } else
                    Log.e(TAG, "Call Logs Cursor is Empty");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error on onChange : " + e.toString());
        }


    }

    public CallDetailsRequest createCallRequest() {

        //String mob = PreferenceManager.getDefaultSharedPreferences(context).getString("mobileNumber","defaultStringIfNothingFound");
        SharedPreferences prefs = context.getSharedPreferences("MyFleetCall", MODE_PRIVATE);
        String mobileNumber = prefs.getString("mobileNumber", "no value");
        System.out.println("mob" + mobileNumber);
        CallDetailsRequest callDetailsRequest = new CallDetailsRequest();
        callDetailsRequest.setStartTime(myFirebaseMessagingService.currentTime); //start time
        callDetailsRequest.setEndTime(""); //end time
        callDetailsRequest.setCallDuration(callDuration);  //call duration
        callDetailsRequest.setCallTo(myFirebaseMessagingService.CallTo); //call to
        callDetailsRequest.setCallFrom(mobileNumber);//call from
        if (callDuration == 0) {
            callDetailsRequest.setStatus("Call not initialized");
        } else {
            callDetailsRequest.setStatus("Call completed");
        }


        return callDetailsRequest;
    }

    public void saveCallDetails(CallDetailsRequest callDetailsRequest, final Context context) {

        Call<CallDetailsResponse> callDetailsResponseCall = ApiClient.getUserService().saveCallDetails(callDetailsRequest);
        callDetailsResponseCall.enqueue(new Callback<CallDetailsResponse>() {
            @Override
            public void onResponse(Call<CallDetailsResponse> call, Response<CallDetailsResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Call details saved..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CallDetailsResponse> call, Throwable t) {
                Toast.makeText(context, "Fail to save call details..", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static void registerListener(Context context) {
        ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).listen(new MyPhoneStateListener(),
                PhoneStateListener.LISTEN_CALL_STATE);
    }
}
