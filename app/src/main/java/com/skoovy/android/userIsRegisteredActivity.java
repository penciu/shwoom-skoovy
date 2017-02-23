package com.skoovy.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class userIsRegisteredActivity extends AppCompatActivity {

    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_is_registered);
        button1 = (Button) findViewById(R.id.button1);

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
    }
}
