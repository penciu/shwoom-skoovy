package com.skoovy.android;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class setUpPasswordActivity extends AppCompatActivity {

    private EditText editTextPassword;
    private static String password;

    ImageButton button1;        //go on to next activity
    ImageButton button2;        //go back to previous screen
    ImageButton undobutton1;    //undo input text in edit text field

    Boolean isEditText1Empty = true;
    Boolean wasPasswordValid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_password);

        //find widgets on this activity
        button1 = (ImageButton) findViewById(R.id.signupButton);
        button2 = (ImageButton) findViewById(R.id.backButton);
        undobutton1 = (ImageButton) findViewById(R.id.undoButton1);

        //hide undo buttons at activty startup
        undobutton1.setVisibility(View.INVISIBLE);

        //Retrieve text values entered for password
        editTextPassword = (EditText) findViewById(R.id.setapasswordinput);

        //Listen for text on editTextFirstName input field
        editTextPassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                password = editTextPassword.getText().toString().trim();

                if (password.length() > 0) {
                    //there is text in the first name text field
                    //so we display the undo button
                    undobutton1.setVisibility(View.VISIBLE);
                    isEditText1Empty = false;
                    isFieldsSet();
                }
                if (password.length() == 0) {
                    //there is text in the first name text field
                    undobutton1.setVisibility(View.INVISIBLE);
                    isEditText1Empty = true;
                    isFieldsSet();
                }
            }
        });

/*
        /*
        * Listen for focus changes to control presentation of undo buttons here.
        * Additional presentation control is also done when text changes (in code above)
         */

        //if EditText editTextFirstName is not focused, remove undo button
        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    undobutton1.setVisibility(View.INVISIBLE);
                }
                if (hasFocus && (editTextPassword.length() > 0)) {
                    // code to execute when EditText loses focus
                    undobutton1.setVisibility(View.VISIBLE);
                }
            }
        });

        //Check to see if 'Done' button was pressed on the softkeyboard
        //remove focus from current last name text field (focus actually passes back to LinearLayout)
        //hide undo text button for last name text field
        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // When 'done' button on softkeyboar is pressed, remove undo button on last name input field
                    undobutton1.setVisibility(View.INVISIBLE);
                    editTextPassword.clearFocus();

                    View view = findViewById(R.id.activity_set_up_password);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });

        //Tell my buttons to listen up!
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        //undobutton1 is the undo text in text field button
        undobutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextPassword.setText("");
                undobutton1.setVisibility(View.INVISIBLE);
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get text from values entered and trim whitespace
                password = editTextPassword.getText().toString().trim();

                //Detect empty fields before allowing user to continue to next activity
                if (TextUtils.isEmpty(password)) {
                    //password text is empty
                    Toast.makeText(getApplicationContext(), "Please enter PASSWORD", Toast.LENGTH_SHORT).show();
                    //stopping the function from executing further
                    return;
                }
                if (password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Please enter at LEAST 8 characters", Toast.LENGTH_SHORT).show();
                    //stopping the function from executing further
                    return;
                }
                Toast.makeText(getApplicationContext(), " TODO ITEM", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "USER FIRST NAME: " + signupActivity.firstName + "\nUSER LAST NAME: " + signupActivity.lastName + "\nUSER BIRTHDATE: " + whatsYourBirthdayActivity.birthdate + "\nUSER USERNAME: " + signupCreateUsernameActivity.userName + "\nUSER EMAIL: " + whatsYourEmailActivity.email + "USER PASSWORD: " + password, Toast.LENGTH_LONG).show();

                //declare where you intend to go
                //Intent intent1 = new Intent(whatsYourEmailActivity.this, whatsYourValidationPIN.class);
                //now make it happen
                //startActivity(intent1);

            }
        });

        //button2 is the BACK ARROW button
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //place logic here to do back button action
                finish();
            }
        });


        //STILL NEED AN ADDITIONAL BUTTON TO GO TO whatsYourMobileNumber.class
    }

    /**
     * areBothFieldsSet
     * Switches signup button image if both fields are set
     */
    public void isFieldsSet() {
        if (!(isEditText1Empty)) {
            //switch image on signup button
            button1.setImageResource(R.drawable.next);
        } else {
            button1.setImageResource(R.drawable.signupgrey);
        }
    }

}
