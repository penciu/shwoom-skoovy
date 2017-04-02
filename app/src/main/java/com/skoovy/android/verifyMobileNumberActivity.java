package com.skoovy.android;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nexmo.sdk.verify.event.SearchListener;
import com.nexmo.sdk.verify.event.UserStatus;
import com.nexmo.sdk.verify.event.VerifyError;

import java.io.IOException;


public class verifyMobileNumberActivity extends AppCompatActivity {

    Button button1;
    Button button2;
    TextView phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mobile_number);

        //get font asset
        Typeface centuryGothic = Typeface.createFromAsset(getAssets(), "fonts/Century Gothic.ttf");

        //find widgets on this activity
        button1 = (Button) findViewById(R.id.verifymobilenumber);
        button2 = (Button) findViewById(R.id.cancel);
        phoneNumber = (TextView) findViewById(R.id.selectedMobileNumber);

        //set font on button
        button1.setTypeface(centuryGothic);

        //fill in phoneNumber text with user's phone number
        Toast.makeText(getApplicationContext(), whatsYourMobileNumber.phoneNumber, Toast.LENGTH_LONG).show();
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

                Intent intent6 = getIntent();
                final User user = (User)intent6.getSerializableExtra("user");

                //declare where you intend to go
                Intent intent5 = new Intent(verifyMobileNumberActivity.this, verificationCodeActivity.class);
                //now make it happen
                intent5.putExtra("user", user);
                startActivity(intent5);
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
