package com.skoovy.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class verifyMobileNumberActivity extends AppCompatActivity {

    ImageButton button1;
    Button button2;
    TextView phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mobile_number);

        //find widgets on this activity
        button1 = (ImageButton) findViewById(R.id.verifymobilenumber);
        button2 = (Button) findViewById(R.id.cancel);
        phoneNumber = (TextView) findViewById(R.id.selectedMobileNumber);

        //fill in phoneNumber text with user's phone number
        String modPhoneNumber = whatsYourMobileNumber.phoneNumber.trim().replace(")","-").substring(1,14);
        phoneNumber.setText(whatsYourMobileNumber.countryCode + " " + modPhoneNumber);

        //Tell my buttons to listen up!
        addListenerOnButton();
    }

    public void addListenerOnButton() {



        //button1 is the SEND SMS button
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






                Toast.makeText(getApplicationContext(), "THIS IS WHERE THE NEXMO API WOULD SEND PIN VIA SMS TO USER", Toast.LENGTH_LONG).show();

                //declare where you intend to go
                Intent intent1 = new Intent(verifyMobileNumberActivity.this, verificationCodeActivity.class);
                //now make it happen
                startActivity(intent1);

            }
        });

        //button2 is the CANCEL button
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //place logic here to do back button action
                finish();
            }
        });


    }




}
