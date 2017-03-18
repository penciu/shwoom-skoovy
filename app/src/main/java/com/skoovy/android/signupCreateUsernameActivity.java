package com.skoovy.android;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

public class signupCreateUsernameActivity extends Activity{



    private EditText editTextUserName;
    public static String userName;

    Button button1;
    ImageButton button2;
    ImageButton undobutton1;

    TextView userTaken;

    FrameLayout animationContainer;
    ImageView mySpinner;

    Boolean isEditText1Empty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_create_username);

        //get font asset
        Typeface centuryGothic = Typeface.createFromAsset(getAssets(), "fonts/Century Gothic.ttf");

        //find widgets on this activity
        button1 = (Button) findViewById(R.id.signupButton);
        button2 = (ImageButton) findViewById(R.id.backToBirthdateButton);
        undobutton1 = (ImageButton) findViewById(R.id.undoButton1);
        mySpinner = (ImageView) findViewById(R.id.rotate_image);
        userTaken = (TextView) findViewById(R.id.userTaken);

        //set font on button
        button1.setTypeface(centuryGothic);

        //hide undo buttons at activty startup
        undobutton1.setVisibility(View.INVISIBLE);

        //hide spinner at activity startup
        mySpinner.setVisibility(View.INVISIBLE);

        //Retrieve text values entered for first name and last name
        editTextUserName =  (EditText) findViewById(R.id.registerUsername);

        //Listen for text on editTextFirstName input field
        editTextUserName.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){

                userName = editTextUserName.getText().toString().trim();

                if (userName.length() > 0){
                    //there is text in the first name text field
                    //so we display the undo button
                    undobutton1.setVisibility(View.VISIBLE);
                    isEditText1Empty = false;
                    isFieldsSet();
                }
                if (userName.length() == 0){
                    //there is text in the first name text field
                    undobutton1.setVisibility(View.INVISIBLE);
                    isEditText1Empty = true;
                    isFieldsSet();
                }
            }
        });

        //Check to see if 'Done' button was pressed on the softkeyboard
        //remove focus from current last name text field (focus actually passes back to LinearLayout)
        //hide undo text button for last name text field
        editTextUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // When 'done' button on softkeyboar is pressed, remove undo button on last name input field
                    undobutton1.setVisibility(View.INVISIBLE);
                    editTextUserName.clearFocus();

                    View view = findViewById(R.id.activity_signup_create_username);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });


        /*
        // Check for focus coming back to the editTextUserName text field
        // make sure that field is in focus and still has text
        // display the undo text button
         */
        editTextUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //Toast.makeText(getBaseContext(), ((EditText) v).getId() + " has focus - " + hasFocus, Toast.LENGTH_LONG).show();
                userName = editTextUserName.getText().toString().trim();
                if (editTextUserName.hasFocus() && userName.length() > 0){
                    //there is text in the username text field
                    //so we display the undo button
                    undobutton1.setVisibility(View.VISIBLE);
                    userTaken.setText("");
                    isEditText1Empty = false;
                    isFieldsSet();
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

        //undobutton1 is the undo text in text field button
        undobutton1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextUserName.setText("");
                undobutton1.setVisibility(View.INVISIBLE);
            }
        });


        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //hide undobutton
                undobutton1.setVisibility(View.GONE);

                //find spinnerView
                final View spinnerView = findViewById(R.id.spinnerView);

                //get text from values entered and trim whitespace
                userName = editTextUserName.getText().toString().trim();

                //Detect empty fields before allowing user to continue to next activity
                if (TextUtils.isEmpty(userName)) {
                    //userName is empty
                    Toast.makeText(getApplicationContext(), "Please enter userName", Toast.LENGTH_SHORT).show();
                    //stopping the function from executing further
                    return;
                }

                //at this point, text field was NOT empty.
                //so we display the spinner and start spin
                animationContainer = (FrameLayout)findViewById(R.id.animationHoldingFrame);
                animationContainer.setVisibility(View.VISIBLE);
                startRotatingImage(spinnerView);

                //CHECK DATABASE IF REQUESTED USERNAME IS TAKEN
                // Get an instance to our database
                FirebaseDatabase skoovyDatabase = FirebaseDatabase.getInstance();
                // Get a  reference to our userInfo node
                DatabaseReference currentSkoovyUsers = skoovyDatabase.getReference("userInfo");

                currentSkoovyUsers.orderByChild("username").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                    // do some stuff once
                        //database has returned dataSnapshot, so we can stop mySpinner
                        animationContainer.setVisibility(View.INVISIBLE);

                        if(dataSnapshot.exists()){
                            //Toast.makeText(getApplicationContext(), "FIREBASE WAS CHECKED: Selected Username is ALREADY in use", Toast.LENGTH_SHORT).show();
                            updateTextView(" " + userName + " is already taken. Try again.");
                        }
                        else{
                            //Toast.makeText(getApplicationContext(), "FIREBASE WAS CHECKED: Username selection is new", Toast.LENGTH_SHORT).show();
                            updateTextView("");
                            Toast.makeText(getApplicationContext(), "USER FIRST NAME: " + signupActivity.firstName + "\nUSER LAST NAME: " + signupActivity.lastName + "\nUSER BIRTHDATE: " + whatsYourBirthdayActivity.birthdate + "\nUSER USERNAME: " + userName, Toast.LENGTH_LONG).show();

                            //User entered an un-used username requirement
                            Intent intent2 = getIntent();
                            User user = (User)intent2.getSerializableExtra("user");
                            //User meets the username requirement
                            user.setUsername(userName);
                            //declare where you intend to go
                            Intent intent3 = new Intent(signupCreateUsernameActivity.this, whatsYourEmailActivity.class);
                            //now make it happen
                            intent3.putExtra("user", user);
                            startActivity(intent3);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //database has returned dataSnapshot, so we can stop mySpinner
                        mySpinner.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        //Back arrow button listener
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //place logic here to do back button action
                finish();
            }
        });
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
     * areBothFieldsSet
     * Switches signup button background if username field is set
     */
    public void isFieldsSet(){
        if (!(isEditText1Empty)) {
            //switch image on signup button
            button1.setBackgroundResource(R.drawable.roundedorangebutton);  //field is NOT empty
        }
        else {
            button1.setBackgroundResource(R.drawable.roundedgreybutton);  //field IS empty
        }
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
