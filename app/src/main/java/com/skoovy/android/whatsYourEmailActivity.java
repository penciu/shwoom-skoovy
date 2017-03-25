package com.skoovy.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class whatsYourEmailActivity extends AppCompatActivity {

    EditText editTextEmail;
    public static String email;

    Button button1;
    ImageButton button2;
    Button button3;
    ImageButton undobutton1;

    TextView userTaken;

    FrameLayout animationContainer;
    ImageView mySpinner;

    Boolean isEditText1Empty = true;
    Boolean wasEmailValid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_your_email);

        //get font asset
        Typeface centuryGothic = Typeface.createFromAsset(getAssets(), "fonts/Century Gothic.ttf");

        //create widget references for this activity
        button1 = (Button) findViewById(R.id.signupButton);
        button2 = (ImageButton) findViewById(R.id.backToUsernameButton);
        button3 = (Button) findViewById(R.id.signUpWithMobile);
        undobutton1 = (ImageButton) findViewById(R.id.undoButton1);
        editTextEmail =  (EditText) findViewById(R.id.registerYourEmail);
        mySpinner = (ImageView) findViewById(R.id.rotate_image);
        userTaken = (TextView) findViewById(R.id.userTaken);

        //set font on button
        button1.setTypeface(centuryGothic);

        //hide undo buttons at activty startup
        undobutton1.setVisibility(View.INVISIBLE);

        //hide spinner at activity startup
        mySpinner.setVisibility(View.INVISIBLE);

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
                    isValidEmail(email);
                }
                if (email.length() == 0){
                    //there is text in the first name text field
                    undobutton1.setVisibility(View.INVISIBLE);
                    isEditText1Empty = true;
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
                button1.setBackgroundResource(R.drawable.roundedgreybutton);
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hide undo text field button
                undobutton1.setVisibility(View.INVISIBLE);
                updateTextView("");
                //get text from values entered and trim whitespace
                email = editTextEmail.getText().toString().trim();

                //find spinnerView
                final View spinnerView = findViewById(R.id.spinnerView);

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

                //at this point, text field was NOT empty.
                //so we display the spinner and start spin
                animationContainer = (FrameLayout)findViewById(R.id.animationHoldingFrame);
                animationContainer.setVisibility(View.VISIBLE);
             //   startRotatingImage(spinnerView);
                startRotatingImage(spinnerView);

                //CHECK DATABASE IF REQUESTED EMAIL IS TAKEN
                // Get an instance to our database
                FirebaseDatabase skoovyDatabase = FirebaseDatabase.getInstance();
                // Get a  reference to our userInfo node
                DatabaseReference currentSkoovyUsers = skoovyDatabase.getReference("userInfo");

                currentSkoovyUsers.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // do some stuff once
                        //database has returned dataSnapshot, so we can stop mySpinner
                        animationContainer.setVisibility(View.INVISIBLE);

                        if(dataSnapshot.exists()){
                            //Toast.makeText(getApplicationContext(), "FIREBASE WAS CHECKED: Selected Username is ALREADY in use", Toast.LENGTH_SHORT).show();
                            updateTextView(" " + email + " is already taken. Try again.");
                            undobutton1.setVisibility(View.VISIBLE);
                        }
                        else{
                            //Toast.makeText(getApplicationContext(), "FIREBASE WAS CHECKED: Username selection is new", Toast.LENGTH_SHORT).show();
                            updateTextView("");
                            //Toast.makeText(getApplicationContext(), "USER FIRST NAME: " + signupActivity.firstName + "\nUSER LAST NAME: " + signupActivity.lastName + "\nUSER BIRTHDATE: " + whatsYourBirthdayActivity.birthdate + "\nUSER USERNAME: " + signupCreateUsernameActivity.userName + "\nUSER EMAIL: " + email, Toast.LENGTH_LONG).show();

                            //User entered an un-used email requirement
                            Intent intent3 = getIntent();
                            User user = (User)intent3.getSerializableExtra("user");
                            //User meets the username requirement
                            user.setEmail(email);
                            //declare where you intend to go
                            Intent intent5 = new Intent(whatsYourEmailActivity.this, setUpPasswordActivity.class);
                            //now make it happen
                            intent5.putExtra("user", user);
                            startActivity(intent5);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //database has returned dataSnapshot, so we can stop mySpinner
                        mySpinner.setVisibility(View.INVISIBLE);
                    }
                });

//                Toast.makeText(getApplicationContext(), "USER FIRST NAME: " + signupActivity.firstName + "\nUSER LAST NAME: " + signupActivity.lastName + "\nUSER BIRTHDATE: " + whatsYourBirthdayActivity.birthdate + "\nUSER USERNAME: " + signupCreateUsernameActivity.userName + "\nUSER EMAIL: " + email, Toast.LENGTH_LONG).show();
//
//                //User satisfied email requirement
//                Intent intent3 = getIntent();
//                User user = (User)intent3.getSerializableExtra("user");
//                //User meets the username requirement
//                user.setEmail(email);
//
//                //declare where you intend to go
//                Intent intent5 = new Intent(whatsYourEmailActivity.this, setUpPasswordActivity.class);
//                //now make it happen
//                intent5.putExtra("user", user);
//                startActivity(intent5);

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
                Intent intent3 = getIntent();
                User user = (User)intent3.getSerializableExtra("user");
                //declare where you intend to go
                Intent intent5 = new Intent(whatsYourEmailActivity.this, whatsYourMobileNumber.class);
                //now make it happen
                intent5.putExtra("user", user);
                startActivity(intent5);
            }
        });
    }



    /**
     * isValidEmail
     * Checks for valid email pattern
     * @param target, String text from user input
     * @return boolean
     */
    public final  boolean isValidEmail(String target) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";


        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);

        if(matcher.matches()) {
            //Toast.makeText(getApplicationContext(), " VALID EMAIL", Toast.LENGTH_SHORT).show();
            button1.setBackgroundResource(R.drawable.roundedorangebutton);
            return true;
        }
        else {
            //Toast.makeText(getApplicationContext(), " BAD EMAIL", Toast.LENGTH_SHORT).show();
            button1.setBackgroundResource(R.drawable.roundedgreybutton);
            return false;
        }
    }


    /**
     * updateTextView
     * When user name exists in database, methos is called to update the text view
     * @param toThis string text to display in text view
     *
     */
    public void updateTextView(String toThis) {
        TextView textView = (TextView) findViewById(R.id.userTaken);
        textView.setText(toThis);
    }


    /**
     * startRotatingImage
     * @param view
     * Called to rotate spinner while database is checked for existing username
     */
    public void startRotatingImage(View view) {
        mySpinner = (ImageView) findViewById(R.id.rotate_image);
        Animation startRotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.android_rotate_animation);
        mySpinner.startAnimation(startRotateAnimation);
    }
}
