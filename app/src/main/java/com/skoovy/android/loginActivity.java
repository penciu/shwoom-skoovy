package com.skoovy.android;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class loginActivity extends Activity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText mEmailView;
    private EditText mPasswordView;

    public static String email;
    public static String password;

    ImageButton button1;
    ImageButton button2;
    ImageButton undoButton1;
    ImageButton undoButton2;
    Button passwordreset;

    boolean isEditText1Empty = true;
    boolean isEditText2Empty = true;

    Boolean wasEmailValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //find all buttons in this activity
        button1 = ((ImageButton)findViewById(R.id.loginImageButton));
        passwordreset = ((Button)findViewById(R.id.passwordHelp));
        undoButton1 = ((ImageButton)findViewById(R.id.undoButton1));
        undoButton2 = ((ImageButton)findViewById(R.id.undoButton2));
        //initialize undo text buttons to INVISIBLE
        undoButton1.setVisibility(View.INVISIBLE);
        undoButton2.setVisibility(View.INVISIBLE);

        //find all EditTexts in this activity
        mEmailView = ((EditText)findViewById(R.id.emailTextField));
        mPasswordView = ((EditText)findViewById(R.id.passwordTextField));


        //listen for changes on the email text field
        //HIDE undo text button when no field has no text
        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                email = mEmailView.getText().toString().trim();
                if (email.length() == 0) {
                    //there is NO text in the email text field
                    //so we HIDE the undo button
                    undoButton1.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });

        //Listen for text on email text field
        mEmailView.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View arg0, MotionEvent arg1) {
                email = mEmailView.getText().toString().trim();
                if (email.length() > 0) {
                    //there is text in the email text field
                    //so we display the undo button
                    undoButton1.setVisibility(View.VISIBLE);
                }
                undoButton2.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        //listen for changes on the password text field
        //HIDE undo text button when no field has no text
        mPasswordView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                password = mPasswordView.getText().toString().trim();
                if (password.length() > 0) {
                    //there is text in the password text field
                    //so we display the undo button
                    undoButton2.setVisibility(View.VISIBLE);
                }
                undoButton1.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        //Listen for text on email input field
        mEmailView.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                email = mEmailView.getText().toString().trim();

                if (email.length() > 0) {
                    //there is text in the email text field
                    //so we trip the empty flag, DISPLAY the undo button,
                    //and call a check to switch image on login button
                    isEditText1Empty = false;
                    undoButton1.setVisibility(View.VISIBLE);
                    areBothFieldsSet();
                }

                if (email.length() == 0)
                {
                    //there is NO text in the email text field
                    //so we trip the empty flag, HIDE the undo button,
                    //and call a check to switch image on login button
                    isEditText1Empty = true;
                    undoButton1.setVisibility(View.INVISIBLE);
                    areBothFieldsSet();
                }
            }
        });

        //Listen for text on password input field
        mPasswordView.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (!(mEmailView.hasFocus())) {
                    undoButton1.setVisibility(View.INVISIBLE);
                }
                password = mPasswordView.getText().toString().trim();
                if (password.length() > 0)
                {
                    //there is password in the email text field
                    //so we trip the empty flag, DISPLAY the undo button,
                    //and call a check to switch image on login button
                    isEditText2Empty = false;
                    undoButton2.setVisibility(View.VISIBLE);
                    areBothFieldsSet();
                }
                if (password.length() == 0)
                {
                    //there is NO password in the email text field
                    //so we trip the empty flag, HIDE the undo button,
                    //and call a check to switch image on login button
                    isEditText2Empty = true;
                    undoButton2.setVisibility(View.INVISIBLE);
                    areBothFieldsSet();
                }
            }
        });

                /*
        * Listen for focus changes to control presentation of undo buttons here.
        * Additional presentation control is also done when text changes (in code above)
         */

        //if EditText mEmailView is not focused, remove undo button
        mEmailView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    undoButton1.setVisibility(View.INVISIBLE);
                }
                if (hasFocus && (mEmailView.length() > 0 )) {
                    // code to execute when EditText loses focus
                    undoButton1.setVisibility(View.VISIBLE);
                }
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {}
        };
        mAuth.addAuthStateListener(mAuthListener);

        //Check to see if 'Done' button was pressed on the softkeyboard
        //remove focus from current password text field (focus actually passes back to LinearLayout)
        //hide undo text button for password text field
        mPasswordView.setOnEditorActionListener(new OnEditorActionListener() {

            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // When 'done' button on softkeyboar is pressed, remove undo button on password input field
                    undoButton2.setVisibility(View.INVISIBLE);
                    mPasswordView.clearFocus();

                    View view = findViewById(R.id.activity_login);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });


        //Tell my buttons to listen up!
        addListenerOnButton();
    }

    /**
     * areBothFieldsSet
     * Switches LOGIN button image if both fields are set
     */
    public boolean areBothFieldsSet()
    {
        if ((!(isEditText1Empty)) && (!(isEditText2Empty))) {
            this.button1.setImageResource(R.drawable.login);   //    THIS NEEDS DIFFERENT SRC
            return true;
        } else {
            this.button1.setImageResource(R.drawable.login);
            return false;
        }
    }

    /**
     * addListenerOnButton
     * Listens to the buttons of this activity
     */
    public void addListenerOnButton()
    {
        //undoButton1 is the undo text for email text field
        undoButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEmailView.setText(""); //clear text in text field
            }
        });

        //undoButton2 is the undo text for password text field
        undoButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mPasswordView.setText(""); //clear text in text field
            }
        });

        //button1 is the LOGIN button on this activity
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //we need to check that both fields are NOT empty before we call attemptLogin()
                //get text from values entered and trim whitespace
                email = mEmailView.getText().toString().trim();
                password = mPasswordView.getText().toString().trim();

                //Detect empty fields before allowing user to continue to next activity
                if(TextUtils.isEmpty(email)){
                    //firstName is empty
                    Toast.makeText(getApplicationContext(), "Please enter EMAIL", Toast.LENGTH_SHORT).show();
                    //stopping the function from executing further
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    //lastName is empty
                    Toast.makeText(getApplicationContext(), "Please enter PASSWORD", Toast.LENGTH_SHORT).show();
                    //stopping the function from executing further
                    return;
                }

                //User entered an text, but we need to check if text is valid email pattern or if it was a username
                //Text is an email, but not an @skoovy.com email (@skoovy.com emails are reserved for phone users.)
                if(email.contains("@") && !email.contains("@skoovy.com")) {
                    //Text could be an email. Checking to see if email is valid.
                    wasEmailValid = isValidEmail(email);
                     if (!(wasEmailValid)) {
                         //email text is INVALID email pattern
                         Toast.makeText(getApplicationContext(), "Please enter a valid EMAIL", Toast.LENGTH_SHORT).show();
                         //stopping the function from executing further
                         return;
                     } else {
                         //email text was valid email pattern
                         //Both text fields were filled, so we allow user to continue
                         //place logic here to do login action
                         attemptLogin(email,password);
                     }
                 } else {
                    //Text is a username.
                     attemptLogin(email,password);
                 }
            }
        });

        //button2 is the BACK ARROW button
        button2 = ((ImageButton)findViewById(R.id.backtToStartButton));
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        //passwordreset is the text 'Password help?'
        passwordreset = ((Button)findViewById(R.id.passwordHelp));
        passwordreset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //declare where you intend to go
                Intent intent = new Intent(loginActivity.this, resetPasswordActivity.class);
                //now make it happen
                startActivity(intent);
            }
        });
    }

    /**
     * Attempts to login the user with a given email/username and password.
     * Potential users can fit into three categories: Email Users, Phone Users, and Fake Users.
     * Email and Phone users can be in two states: Valid and Invalid.
     * Program performs different checks to only allow Valid users in.
     * @param emailString The email address or username of the user.
     * @param passwordString The given password for the account. If this doesn't match what's on file, the user is not allowed in.
     */
    private void attemptLogin(String emailString, String passwordString) {
        //Username was passed
        String countryCode = "";
        String phoneNumber = "";
        if(!emailString.contains("@")) {

        }
        mAuth.signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Login failed. Bad email or password given.
                        if (!task.isSuccessful()) {
                            Toast.makeText(loginActivity.this, "Username or password not found.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //User successfully logged into system. Checking for verification.
                        else if(email.contains("@skoovy.com")){
                            //User has no email on file. Checking for Verified Phone Number.
                            //TODO: PHONE VALIDATION
                        } else if (mAuth.getCurrentUser().isEmailVerified()) {
                            //User has entered a verified email. Sending to App.
                            //Set Up Intent
                            Intent intent = new Intent(loginActivity.this, userIsRegisteredActivity.class);
                            //Now make it happen
                            startActivity(intent);
                        } else {
                            //User entered an unverified Email.
                            Toast.makeText(loginActivity.this, "Verify your email first.",
                                    Toast.LENGTH_SHORT).show();
                            mAuth.getCurrentUser().sendEmailVerification();
                            mAuth.signOut();
                        }
                    }
                });
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