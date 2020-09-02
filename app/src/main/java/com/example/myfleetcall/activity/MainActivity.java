package com.example.myfleetcall.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.myfleetcall.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText editTextMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextMobile = findViewById(R.id.field_phone_number);

        findViewById(R.id.button_start_verification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editTextMobile.getText().toString().trim();

                if (mobile.isEmpty() || mobile.length() < 10) {
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("phonenumber", mobile);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            Intent intent = new Intent(this,ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
