package com.skoovy.android;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nexmo.sdk.NexmoClient;
import com.nexmo.sdk.core.client.ClientBuilderException;
import com.nexmo.sdk.verify.client.VerifyClient;
import com.nexmo.sdk.verify.event.Command;
import com.nexmo.sdk.verify.event.CommandListener;
import com.nexmo.sdk.verify.event.UserObject;
import com.nexmo.sdk.verify.event.VerifyClientListener;

import java.io.IOException;

public class StartScreen extends AppCompatActivity {

    Button button1;
    Button button2;
    String mPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int REQUEST_CODE_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        if (ActivityCompat.checkSelfPermission(this, mPermission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{mPermission},
                    REQUEST_CODE_PERMISSION);
        }

        //get font asset
        Typeface centuryGothic = Typeface.createFromAsset(getAssets(), "fonts/Century Gothic.ttf");
        button1 = (Button) findViewById(R.id.loginButton);
        button2 = (Button) findViewById(R.id.signupButton);

        button1.setTypeface(centuryGothic);
        button2.setTypeface(centuryGothic);

        addListenerOnButton();
    }

    public void addListenerOnButton() {
        //programatically link button to ImageButton & setup listening
        button1 = (Button) findViewById(R.id.loginButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //place logic here to do login action
                //Toast.makeText(getApplicationContext(), "login button clicked", Toast.LENGTH_SHORT).show();

                //declare where you intend to go
                Intent intent1 = new Intent(StartScreen.this, loginActivity.class);
                //now make it happen
                startActivity(intent1);
            }
        });

        //programatically link button to ImageButton & setup listening
        button2 = (Button) findViewById(R.id.signupButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //declare where you intend to go
                Intent intent2 = new Intent(StartScreen.this, signupActivity.class);
                //now make it happen
                startActivity(intent2);
            }
        });
    }
}


