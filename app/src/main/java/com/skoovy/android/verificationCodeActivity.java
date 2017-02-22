package com.skoovy.android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class verificationCodeActivity extends AppCompatActivity {

    //TextView3 in activity_verification_code.xml is reserved in case we need to dynamically update
    //text to the user.  If not needed then adjust layout after deleting.

    TextView appStatus; //status text from app to user.  Default is currently 'Starting verification process ...'
    TextView placeHolder; //Described in comment above; TextView3 in case we need to dynamically update.

    ImageButton button1; //main action button for this activity
    ImageButton button2; //backbutton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);

        //Find elements for this activity
        appStatus = (TextView) findViewById(R.id.textView2);
        placeHolder = (TextView) findViewById(R.id.textView3);

        button1 = (ImageButton) findViewById(R.id.signupButton);
        button2 = (ImageButton) findViewById(R.id.backButton);

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
                if (s.length() == 4){
                    View view = findViewById(R.id.activity_verification_code);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (s.toString().equals("1234")) {
                    Toast.makeText(verificationCodeActivity.this, "Success", Toast.LENGTH_SHORT).show();
                } else if (s.length() == "1234".length()) {
                    Toast.makeText(verificationCodeActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
                    // txtPinEntry.setText(null);  //THIS WILL CLEAR THE PASSCODE ENTERED IF SO DESIRED
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


                //declare where you intend to go
                Intent intent1 = new Intent(verificationCodeActivity.this, userIsRegisteredActivity.class);
                //now make it happen
                startActivity(intent1);
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
}
