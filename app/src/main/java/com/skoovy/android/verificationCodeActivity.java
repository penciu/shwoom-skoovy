package com.skoovy.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nexmo.sdk.NexmoClient;
import com.nexmo.sdk.core.client.ClientBuilderException;
import com.nexmo.sdk.verify.client.VerifyClient;
import com.nexmo.sdk.verify.event.Command;
import com.nexmo.sdk.verify.event.CommandListener;
import com.nexmo.sdk.verify.event.SearchListener;
import com.nexmo.sdk.verify.event.UserObject;
import com.nexmo.sdk.verify.event.UserStatus;
import com.nexmo.sdk.verify.event.VerifyClientListener;
import com.nexmo.sdk.verify.event.VerifyError;

import java.io.IOException;

import static com.skoovy.android.R.string.nexmo_application_id;
import static com.skoovy.android.R.string.username_or_email;

public class verificationCodeActivity extends AppCompatActivity {

    //TextView3 in activity_verification_code.xml is reserved in case we need to dynamically update
    //text to the user.  If not needed then adjust layout after deleting.

    TextView appStatus; //status text from app to user.  Default is currently 'Starting verification process ...'
    TextView placeHolder; //Described in comment above; TextView3 in case we need to dynamically update.

    PinEntryEditText pinNumber;

    public static NexmoClient nexmoClient = null;
    public static VerifyClient verifyClient = null;
    private String TAG = "vCodeActivity";

    User user;

    Button button1; //main action button for this activity
    ImageButton button2; //backbutton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);

        try {
            nexmoClient = new NexmoClient.NexmoClientBuilder()
                    .context(getApplicationContext())
                    .applicationId(getString(R.string.application_id)) //your App key
                    .sharedSecretKey(getString(R.string.shared_secret)) //your App secret
                    .build();
        } catch (ClientBuilderException e) {
            e.printStackTrace();
        }

        verifyClient = new VerifyClient(nexmoClient);

        verifyClient.addVerifyListener(new VerifyClientListener() {
            @Override
            public void onVerifyInProgress(final VerifyClient verifyClient, UserObject user) {
                Log.d(TAG, "onVerifyInProgress for number: " + user.getPhoneNumber());
            }

            @Override
            public void onUserVerified(final VerifyClient verifyClient, UserObject user) {
                Log.d(TAG, "onUserVerified for number: " + user.getPhoneNumber());
            }

            @Override
            public void onError(final VerifyClient verifyClient, final com.nexmo.sdk.verify.event.VerifyError errorCode, UserObject user) {
                Log.d(TAG, "onError: " + errorCode + " for number: " + user.getPhoneNumber());
            }

            @Override
            public void onException(final IOException exception) {}
        });

        Intent intent6 = getIntent();
        user = (User)intent6.getSerializableExtra("user");

        //The verification text is sent at this point.
        verifyClient.getVerifiedUser(user.getPhoneCountryCode(), user.getNexmoPhoneNumber());

        //get font asset
        Typeface centuryGothic = Typeface.createFromAsset(getAssets(), "fonts/Century Gothic.ttf");

        //Find elements for this activity
        appStatus = (TextView) findViewById(R.id.textView2);
        placeHolder = (TextView) findViewById(R.id.textView3);

        button1 = (Button) findViewById(R.id.signupButton);
        button2 = (ImageButton) findViewById(R.id.backButton);

        //set font on button
        button1.setTypeface(centuryGothic);

        final PinEntryEditText txtPinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
        txtPinEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 4) {
                    button1.setBackgroundResource(R.drawable.roundedgreybutton);
                }
                if (s.length() == 4){
                    button1.setBackgroundResource(R.drawable.roundedorangebutton);
                    View view = findViewById(R.id.activity_verification_code);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }
        });

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
                //get PIN entered


                //Detect empty field before allowing user to continue to next activity
//                if (TextUtils.isEmpty(firstName)) {
                    //firstName is empty
//                    Toast.makeText(getApplicationContext(), "Please enter firstName", Toast.LENGTH_SHORT).show();
                    //stopping the function from executing further
//                    return;
//                }


                //Text fields was filled, so we now need to verify pin before allowing user to continue to next activity
                //place logic here to do verification action
                pinNumber = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
                if (pinNumber.length() < 4){
                    Toast.makeText(verificationCodeActivity.this, "I'm afraid that's not a valid pin number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //declare where you intend to go
                    Intent intent5 = new Intent(verificationCodeActivity.this, setUpPasswordActivity.class);
                    //now make it happen
                    intent5.putExtra("user", user);
                    Log.d("PIN ENTERED", pinNumber.getText().toString().trim());
                    verifyUser(user,intent5,pinNumber.getText().toString().trim());
                }


            }
        });

        //listens for back arrow button
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //place logic here to do back button action
                finish();
            }
        });
    }


    public void verifyUser(User user, final Intent intent, String PIN) {
        String countryCode = user.getPhoneCountryCode();
        String phoneNumber = user.getNexmoPhoneNumber();
        verifyClient.checkPinCode(PIN); //This verifies the PIN.
        //checkPinCode has no return value, thus in order to determine if the user has been verified,
        //we must check the user's status manually to determine success.
        verifyClient.getUserStatus(countryCode, phoneNumber, new SearchListener() {
            @Override
            public void onException(IOException exception) {
                //TODO: Add Exception Code
            }

            @Override
            public void onUserStatus(UserStatus userStatus) {
                if (userStatus == UserStatus.USER_VERIFIED)
                    startActivity(intent);
                else {
                    Toast.makeText(getApplicationContext(),"Wrong PIN Entered", Toast.LENGTH_SHORT);
                    return;
                }
            }

            @Override
            public void onError(VerifyError errorCode, String errorMessage) {}
        });
    }

}
