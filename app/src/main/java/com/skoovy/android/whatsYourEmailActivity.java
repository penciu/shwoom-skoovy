package com.skoovy.android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class whatsYourEmailActivity extends AppCompatActivity {

    private EditText editTextEmail;
    public static String email;

    ImageButton button1;
    ImageButton button2;
    Button button3;
    ImageButton undobutton1;

    Boolean isEditText1Empty = true;
    Boolean wasEmailValid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_your_email);

        //find widgets on this activity
        button1 = (ImageButton) findViewById(R.id.signupButton);
        button2 = (ImageButton) findViewById(R.id.backToUsernameButton);
        button3 = (Button) findViewById(R.id.signUpWithMobile);
        undobutton1 = (ImageButton) findViewById(R.id.undoButton1);

        //hide undo buttons at activty startup
        undobutton1.setVisibility(View.INVISIBLE);

        //Retrieve text values entered for first name and last name
        editTextEmail =  (EditText) findViewById(R.id.registerYourEmail);

        //Listen for text on editTextFirstName input field
        editTextEmail.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){

                email = editTextEmail.getText().toString().trim();

                if (email.length() > 0){
                    //there is text in the first name text field
                    //so we display the undo button
                    undobutton1.setVisibility(View.VISIBLE);
                    isEditText1Empty = false;
                    isFieldsSet();
                }
                if (email.length() == 0){
                    //there is text in the first name text field
                    undobutton1.setVisibility(View.INVISIBLE);
                    isEditText1Empty = true;
                    isFieldsSet();
                }
            }
        });


        /*
        * Listen for focus changes to control presentation of undo buttons here.
        * Additional presentation control is also done when text changes (in code above)
         */

        //if EditText editTextFirstName is not focused, remove undo button
        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    undobutton1.setVisibility(View.INVISIBLE);
                }
                if (hasFocus && (editTextEmail.length() > 0 )) {
                    // code to execute when EditText loses focus
                    undobutton1.setVisibility(View.VISIBLE);
                }
            }
        });

        //Check to see if 'Done' button was pressed on the softkeyboard
        //remove focus from current last name text field (focus actually passes back to LinearLayout)
        //hide undo text button for last name text field
        editTextEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // When 'done' button on softkeyboar is pressed, remove undo button on last name input field
                    undobutton1.setVisibility(View.INVISIBLE);
                    editTextEmail.clearFocus();

                    View view = findViewById(R.id.activity_whats_your_email);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
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
                editTextEmail.setText("");
                undobutton1.setVisibility(View.INVISIBLE);
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get text from values entered and trim whitespace
                email = editTextEmail.getText().toString().trim();

                //Detect empty fields before allowing user to continue to next activity
                if (TextUtils.isEmpty(email)) {
                    //email text is empty
                    Toast.makeText(getApplicationContext(), "Please enter EMAIL", Toast.LENGTH_SHORT).show();
                    //stopping the function from executing further
                    return;
                }

                //User entered an text, but we need to check if text is valid email pattern
                wasEmailValid = isValidEmail(email);
                if (!(wasEmailValid)){
                    //email text is INVALID email pattern
                    Toast.makeText(getApplicationContext(), "Please enter a valid EMAIL", Toast.LENGTH_SHORT).show();
                    //stopping the function from executing further
                    return;
                }
                
                Toast.makeText(getApplicationContext(), "USER FIRST NAME: " + signupActivity.firstName + "\nUSER LAST NAME: " + signupActivity.lastName + "\nUSER BIRTHDATE: " + whatsYourBirthdayActivity.birthdate + "\nUSER USERNAME: " + signupCreateUsernameActivity.userName + "\nUSER EMAIL: " + email, Toast.LENGTH_LONG).show();

                //declare where you intend to go
                Intent intent1 = new Intent(whatsYourEmailActivity.this, setUpPasswordActivity.class);
                //now make it happen
                startActivity(intent1);

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

        //button3 is the SIGNUP WITH MOBILE NUMBER button
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //declare where you intend to go
                Intent intent2 = new Intent(whatsYourEmailActivity.this, whatsYourMobileNumber.class);
                //now make it happen
                startActivity(intent2);
            }
        });
        //STILL NEED AN ADDITIONAL BUTTON TO GO TO whatsYourMobileNumber.class
    }

    /**
     * areBothFieldsSet
     * Switches signup button image if both fields are set
     */
    public void isFieldsSet(){
        if (!(isEditText1Empty)) {
            //switch image on signup button
            button1.setImageResource(R.drawable.next);
        }
        else {
            button1.setImageResource(R.drawable.signupgrey);
        }
    }

    /**
     * isValidEmail
     * Checks for valid email pattern
     * @param target
     * @return boolean
     */
    public final  boolean isValidEmail(String target) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = target;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches()) {
            //Toast.makeText(getApplicationContext(), " VALID EMAIL", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            //Toast.makeText(getApplicationContext(), " BAD EMAIL", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
