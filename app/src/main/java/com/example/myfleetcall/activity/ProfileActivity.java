package com.example.myfleetcall.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.View;

import com.example.myfleetcall.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager subs = (SubscriptionManager) getSystemService(getApplicationContext().TELEPHONY_SUBSCRIPTION_SERVICE);
            if (subs != null) {
                Log.d("sim_spalsh", "num sims = " + subs.getActiveSubscriptionInfoCountMax());


                if (subs.getActiveSubscriptionInfoCountMax() > 1) {

//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage("123",null,"hi",null,null);
                    //SendSMS From SIM One
                    SmsManager.getSmsManagerForSubscriptionId(0)
                            .sendTextMessage("1234567890", null, "sim1", null, null);

                    //SendSMS From SIM Two
                    SmsManager.getSmsManagerForSubscriptionId(1)
                            .sendTextMessage("7768948518", null, "sim2", null, null);

                }
            }
        }

        findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }



}
