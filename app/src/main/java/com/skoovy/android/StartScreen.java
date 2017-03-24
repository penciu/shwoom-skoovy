package com.skoovy.android;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StartScreen extends AppCompatActivity {

    Button button1;
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

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


