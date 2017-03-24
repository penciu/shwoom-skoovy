package com.skoovy.android;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class setUpPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;

    private EditText editTextPassword;
    private static String password;

    Button button1;        //go on to next activity
    ImageButton button2;        //go back to previous screen
    ImageButton undobutton1;    //undo input text in edit text field

    Boolean isEditText1Empty = true;
    Boolean wasPasswordValid = false;

    Boolean isRegistered = false;
    Boolean amIRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_password);

        //get font asset
        Typeface centuryGothic = Typeface.createFromAsset(getAssets(), "fonts/Century Gothic.ttf");

        //Set up database reference, and reference the location we write to
        mDatabase = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("userInfo");//to send data to correct child

        mAuth = FirebaseAuth.getInstance();

        //Create widget references on this activity
        button1 = (Button) findViewById(R.id.signupButton);
        button2 = (ImageButton) findViewById(R.id.backButton);
        undobutton1 = (ImageButton) findViewById(R.id.undoButton1);
        editTextPassword = (EditText) findViewById(R.id.setapasswordinput);

        //set font on button
        button1.setTypeface(centuryGothic);

        //hide undo buttons at activty startup
        undobutton1.setVisibility(View.INVISIBLE);

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
                    //isFieldsSet();
                    if (password.length() > 7){
                        button1.setBackgroundResource(R.drawable.roundedorangebutton);
                    }
                    else {
                        button1.setBackgroundResource(R.drawable.roundedgreybutton);
                    }
                }
                if (password.length() == 0) {
                    //there is text in the first name text field
                    undobutton1.setVisibility(View.INVISIBLE);
                    isEditText1Empty = true;
                   // isFieldsSet();
                    button1.setBackgroundResource(R.drawable.roundedgreybutton);
                }
            }
        });

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
                    // When 'done' button on softkeyboard is pressed, remove undo button on last name input field
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





                //CODE BELOW IS TO SEND DATA TO FIREBASE ABOUT USER //MAYBE NEEDED ELSEWHERE


                //this is where you want to send first name, last name, birthdate, unique user name, and either email or mobile phone number, along with password to firebase database

                //Call method to write to database
                //             registerUserToDatabase(personsFirstLastUserName);
                //    String key = mDatabase.push().child(signupActivity.firstName).child(signupActivity.lastName).child(userName).setValue();
                //   Toast.makeText(getApplicationContext(), "Your UID is: " + key, Toast.LENGTH_SHORT).show();
                //declare where you intend to go
                //Intent intent4 = new Intent(signupCreateUsernameActivity.this, signupCreateUsernameActivity.class);
                //now make it happen
                //startActivity(intent4);
                //    registerUserToDatabase();

                Intent intent5 = getIntent();
                User user = (User)intent5.getSerializableExtra("user");
                user.setPassword(password);
                Log.d("User", user.toString());
 //               registerUserToDatabase();
                mDatabase.push().setValue(user);  //User registration data is now pushed to Firebase DB in node 'userInfo'
                isUserRegistered();

                String email = user.getEmail();
                createAccount(email, password);

                //declare where you intend to go
                Intent intent6 = new Intent(setUpPasswordActivity.this, userIsRegisteredActivity.class);
                //now make it happen
// *******************************************************************************
//                PROBABLY WANT TO PASS THE USER OBJECT TO THE NEXT INTENT HERE
// *******************************************************************************
                startActivity(intent6);

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

    }

    /**
     * isFieldsSet
     * Switches signup button image if field are set
     */
//    public void isFieldsSet() {
//        if (!(isEditText1Empty)) {
//            //switch image on signup button
//            button1.setImageResource(R.drawable.next);
//        } else {
//            button1.setImageResource(R.drawable.signupgrey);
//        }
//    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("User", "AUTHcreateUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(setUpPasswordActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    /**
     * isUserRegistered
     * Confirms if user push to DB was successful
     */
    public void isUserRegistered () {
        Log.d("User", "REG STATUS IS GETTING CHECKED");
        mDatabase.orderByChild("username").equalTo(signupCreateUsernameActivity.userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("User", "DB DATA CHANGED");
                //database has returned a changed dataSnapshot, so do some stuff once

//                if(dataSnapshot.exists()){
//                }
//                else{
//                }
                Toast.makeText(getApplicationContext(), "USER REGISTERED", Toast.LENGTH_LONG).show();
                isRegistered = true;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "REGISTERATION FAILED", Toast.LENGTH_LONG).show();
                isRegistered = false;

            }
        });
    }

}
