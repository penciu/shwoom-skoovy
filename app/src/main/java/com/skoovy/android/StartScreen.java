package com.skoovy.android;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartScreen extends AppCompatActivity {

    Button button1;
    Button button2;

    String mPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private boolean permission = false;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && permission) {
            //declare where you intend to go
            Intent intent1 = new Intent(StartScreen.this, MapsActivity.class);
            //now make it happen
            startActivity(intent1);
        }

        //get font asset
        Typeface centuryGothic = Typeface.createFromAsset(getAssets(), "fonts/Century Gothic.ttf");
        button1 = (Button) findViewById(R.id.loginButton);
        button2 = (Button) findViewById(R.id.signupButton);

        button1.setTypeface(centuryGothic);
        button2.setTypeface(centuryGothic);

        addListenerOnButton();
    }

    private void checkReady() {
        if (ActivityCompat.checkSelfPermission(this, mPermission) == PackageManager.PERMISSION_GRANTED){
            permission = true;
        }
    }

    public void addListenerOnButton() {
        //programatically link button to ImageButton & setup listening
        button1 = (Button) findViewById(R.id.loginButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //place logic here to do login action
                //Toast.makeText(getApplicationContext(), "login button clicked", Toast.LENGTH_SHORT).show();
                if(permission) {
                    //declare where you intend to go
                    Intent intent1 = new Intent(StartScreen.this, loginActivity.class);
                    //now make it happen
                    startActivity(intent1);
                } else {
                    getPermission();
                }
            }
        });

        //programatically link button to ImageButton & setup listening
        button2 = (Button) findViewById(R.id.signupButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(permission) {
                    //declare where you intend to go
                    Intent intent2 = new Intent(StartScreen.this, signupActivity.class);
                    //now make it happen
                    startActivity(intent2);
                } else {
                    getPermission();
                }
            }
        });
    };

    private void getPermission() {
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkReady();
    }
}


