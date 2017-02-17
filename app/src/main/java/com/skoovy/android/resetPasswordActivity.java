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


        addListenerOnButton();
    }

    public void addListenerOnButton()
    {
        button1 = (ImageButton)findViewById(R.id.resetPasswordViaEmail);
        button1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(), "TODO ITEM", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(resetPasswordActivity.this, resetPasswordActivity.class); //CHANGE HERE TO GO TO NEXT ACTIVITY

                startActivity(intent);
            }
        });

        button2 = (ImageButton)findViewById(R.id.resetPasswordViaMobile);
        button2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(), "TODO ITEM", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(resetPasswordActivity.this, resetPasswordActivity.class); //CHANGE HERE TO GO TO NEXT ACTIVITY

                startActivity(intent);
            }
        });

        button3 = (Button) findViewById(R.id.cancelBackToLogIn);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //place logic here to do back button action
                finish();
            }
        });
    }
}

