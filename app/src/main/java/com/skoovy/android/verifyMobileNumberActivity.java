package com.skoovy.android;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Application;
import android.content.Context;

import com.nexmo.sdk.NexmoClient;
import com.nexmo.sdk.core.client.ClientBuilderException;
import com.nexmo.sdk.verify.client.VerifyClient;

import com.nexmo.sdk.NexmoClient;
import com.nexmo.sdk.core.client.ClientBuilderException;
import com.nexmo.sdk.verify.client.VerifyClient;
import com.nexmo.sdk.verify.event.UserObject;
import com.nexmo.sdk.verify.event.VerifyClientListener;


public class verifyMobileNumberActivity extends AppCompatActivity {

    Button button1;
    Button button2;
    TextView phoneNumber;

    private VerifyClient verifyClient;
    private NexmoClient nexmoClient;
    private boolean verified;
    private static final String TAG = "verify";

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

                Context context = getApplicationContext();
                try {
                    nexmoClient = new NexmoClient.NexmoClientBuilder()
                            .context(context)
                            .applicationId(Constants.NEXMO_ID) //your App key
                            .sharedSecretKey(Constants.SHARED_SECRET) //your App secret
                            .build();
                } catch (ClientBuilderException e) {
                    e.printStackTrace();
                }

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

    public VerifyClient getVerifyClient(boolean verifiedValue) {
        verified = verifiedValue;
        return this.verifyClient;
    }
    //@Override
    public void onCreate() {
        //super.onCreate();
        acquireVerifyClient();
    }
    public void acquireVerifyClient() {
        Context context = getApplicationContext();
        try {
            this.nexmoClient = new NexmoClient.NexmoClientBuilder()
                    .context(context)
                    .applicationId(getResources().getString(R.string.nexmo_application_id))
                    .sharedSecretKey(getResources().getString(R.string.nexmo_shared_secret))
                    .build();
        } catch (ClientBuilderException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
            return;
        }
        this.verifyClient = new VerifyClient(nexmoClient);
    }


}
