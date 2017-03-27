package com.skoovy.android;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class userIsRegisteredActivity extends AppCompatActivity {

    Button button1;
    Button button2;
    Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_is_registered);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.skipButton);

        //get font asset
        Typeface centuryGothic = Typeface.createFromAsset(getAssets(), "fonts/Century Gothic.ttf");


        button1.setTypeface(centuryGothic);
        button2.setTypeface(centuryGothic);

        //Tell my buttons to listen up!
        addListenerOnButton();
    }

    /**
     * addListenerOnButton
     * Listens to the buttons of this activity
     */
    public void addListenerOnButton() {
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //declare where you intend to go
                Intent intent1 = new Intent(userIsRegisteredActivity.this, StartScreen.class);
                //now make it happen
                startActivity(intent1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //declare where you intend to go
                Intent intent2 = new Intent(userIsRegisteredActivity.this, CameraActivity.class);
                //now make it happen
                startActivity(intent2);
            }
        });

        //button3 is the 'skip' button
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //declare where you intend to go
                Intent intent3 = new Intent(userIsRegisteredActivity.this, UserProfile.class);
                //now make it happen
                startActivity(intent3);
            }
        });
    }
}
