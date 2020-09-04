package com.example.myfleetcall.ReceiverObserver;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class MyPhoneStateListener extends PhoneStateListener {

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch (state) {
            //Hangup
            case TelephonyManager.CALL_STATE_IDLE:
                System.out.println("hangup");
                break;
            //Outgoing
            case TelephonyManager.CALL_STATE_OFFHOOK:
                System.out.println("outgoing");
                break;
            //Incoming
            case TelephonyManager.CALL_STATE_RINGING:
                break;



        }
    }
}
