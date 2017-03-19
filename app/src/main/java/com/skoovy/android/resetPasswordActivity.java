package com.skoovy.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class resetPasswordActivity extends Activity {

    ImageButton button1;
    ImageButton button2;
    Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        //Create GUI references for this activity
        button1 = (ImageButton)findViewById(R.id.resetPasswordViaEmail);
        button2 = (ImageButton)findViewById(R.id.resetPasswordViaMobile);
        button3 = (Button) findViewById(R.id.cancelBackToLogIn);

        addListenerOnButton();
    }

    public void addListenerOnButton()
    {
        //button1 is the email reset method button
        button1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(), "TODO ITEM", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(resetPasswordActivity.this, resetPasswordActivity.class); //CHANGE HERE TO GO TO NEXT ACTIVITY

                startActivity(intent);
            }
        });

        //button2 is the mobile reset method button
        button2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(), "TODO ITEM", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(resetPasswordActivity.this, resetPasswordActivity.class); //CHANGE HERE TO GO TO NEXT ACTIVITY

                startActivity(intent);
            }
        });

        //button3 is the cancel button
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //place logic here to do back button action
                finish();
            }
        });
    }
}

