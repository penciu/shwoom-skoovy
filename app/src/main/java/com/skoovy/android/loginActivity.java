package com.skoovy.android;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull; //import is used
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ChildEventListener;
import com.nexmo.sdk.NexmoClient;
import com.nexmo.sdk.core.client.ClientBuilderException;
import com.nexmo.sdk.verify.client.VerifyClient;
import com.nexmo.sdk.verify.event.SearchListener;
import com.nexmo.sdk.verify.event.UserObject;
import com.nexmo.sdk.verify.event.UserStatus;
import com.nexmo.sdk.verify.event.VerifyClientListener;
import com.nexmo.sdk.verify.event.VerifyError;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class loginActivity extends Activity {


    public static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText mEmailView;
    private EditText mPasswordView;

    public static String email;
    public static String password;

    //Database content
    private String firstnameAtGivenUser;
    private String lastnameAtGivenUser;
    private String birthdayAtGivenUser;
    private String emailAtGivenUser;
    private String countrycodeAtGivenUser;
    private String prefixAtGivenUser;
    private String phonenumberAtGivenUser;
    private String uidAtGivenUser;
    private String nexmoPhoneNumberAtGivenUser;

    private String skoovyUserName;

    String TAG = "loginActivity";

    private int newUserFreePoints = 100; //new user default points

    Button button1;
    ImageButton button2;
    ImageButton undoButton1;
    ImageButton undoButton2;
    Button passwordReset;

    boolean isEditText1Empty = true;
    boolean isEditText2Empty = true;

    Boolean wasEmailValid = false;

    NexmoClient nexmoClient = null;
    VerifyClient verifyClient = null;

    Intent intent6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         intent6 = new Intent(loginActivity.this, userIsRegisteredActivity.class);

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

        //get font asset
        Typeface centuryGothic = Typeface.createFromAsset(getAssets(), "fonts/Century Gothic.ttf");

        //Create GUI references for this activity
        button1 = (Button)findViewById(R.id.loginButton);
        passwordReset = ((Button)findViewById(R.id.passwordHelp));
        undoButton1 = ((ImageButton)findViewById(R.id.undoButton1));
        undoButton2 = ((ImageButton)findViewById(R.id.undoButton2));
        mEmailView = ((EditText)findViewById(R.id.emailTextField));
        mPasswordView = ((EditText)findViewById(R.id.passwordTextField));

        //set font on button and initial background
        button1.setTypeface(centuryGothic);

        //initialize undo text buttons to INVISIBLE
        undoButton1.setVisibility(View.INVISIBLE);
        undoButton2.setVisibility(View.INVISIBLE);

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

                if(TextUtils.isEmpty(password)) {
                    return;
                }
                if (email.length() > 0) {
                    //there is text in the email text field
                    //so we trip the empty flag, DISPLAY the undo button,
                    //and call a check to switch image on login button
                    isEditText1Empty = false;
                    undoButton1.setVisibility(View.VISIBLE);
                    areBothFieldsSet();
                }
                if (email.length() > 0 && password.length() >= 8){
                    button1.setBackgroundResource(R.drawable.roundedorangebutton);   //fields are NOT empty
                    button1.setTextColor(0xFFFFFFFF);
                }
                if (email.length() == 0)
                {
                    //there is NO text in the email text field
                    //so we trip the empty flag, HIDE the undo button,
                    //and call a check to switch image on login button
                    isEditText1Empty = true;
                    undoButton1.setVisibility(View.INVISIBLE);
                    button1.setBackgroundResource(R.drawable.roundedwhitebuttonblackborder);
                    button1.setTextColor(0xFF000000);
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
                   // button1.setTextColor(0xFF000000);
                    undoButton2.setVisibility(View.INVISIBLE);
                    areBothFieldsSet();
                }
                if (password.length() >= 8 && email.length() > 0) {
                    button1.setBackgroundResource(R.drawable.roundedorangebutton);   //fields are NOT empty
                    button1.setTextColor(0xFFFFFFFF);
                }
                if (password.length() < 8){
                    button1.setBackgroundResource(R.drawable.roundedwhitebuttonblackborder);
                    button1.setTextColor(0xFF000000);
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
                    // code to execute when EditText has focus
                    undoButton1.setVisibility(View.VISIBLE);
                }
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {}
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

        //Check to see if 'Done' button was pressed on the softkeyboard
        //remove focus from current password text field (focus actually passes back to LinearLayout)
        //hide undo text button for password text field
        mPasswordView.setOnEditorActionListener(new OnEditorActionListener() {

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
    } // END OF ONCREATE FOR LOGIN ACTIVITY


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

                //HIDE THE SOFT KEYBOARD
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);

                //Both text fields were filled, so we allow attempt to login user
                attemptLogin();
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
        passwordReset = ((Button)findViewById(R.id.passwordHelp));
        passwordReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //declare where you intend to go
                Intent intent = new Intent(loginActivity.this, resetPasswordActivity.class);
                //now make it happen
                startActivity(intent);
            }
        });
    }


    /**
     * areBothFieldsSet
     * Switches LOGIN button image if both fields are set
     */
    public boolean areBothFieldsSet()
    {
        if ((!(isEditText1Empty)) && (!(isEditText2Empty))) {

            return true;
        } else {
            if(TextUtils.isEmpty(password)) {
                button1.setBackgroundResource(R.drawable.roundedwhitebuttonblackborder);  //fields ARE empty

            }
            else if (password.length() < 8) {
                button1.setBackgroundResource(R.drawable.roundedwhitebuttonblackborder);  //fields ARE empty
            }
            return false;
        }
    }

    /**
     * attemptLogin
     * Fetches strings from input fields and checks if email string was a username (instead of having email-syntax)
     * and aquires corresponding email from user's profile in DB if username exists.
     * Else the email string is validated for proper email-syntax.
     * Once proper email is aquired for this user (either from DB-fetching or from validated direct input), the string
     * is used for userLogin method call to authenticate this user.
     */
    private void attemptLogin()
    {
        String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        Boolean wasInputEmail = email.contains("@");
        if (!wasInputEmail){
            //CHECK DATABASE TO GET EMAIL VALUE FOR THIS USERNAME
            // Get an instance to our database
            FirebaseDatabase skoovyDatabase = FirebaseDatabase.getInstance();
            // Get a reference to our userInfo node
            final DatabaseReference currentSkoovyUsers = skoovyDatabase.getReference("userInfo");
            currentSkoovyUsers.orderByChild("username").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // do some stuff once
                    //database has returned dataSnapshot, so we can
                    if(dataSnapshot.exists()){
                        //Toast.makeText(getApplicationContext(), "FIREBASE WAS CHECKED: Email  in Firebase DB", Toast.LENGTH_SHORT).show();
                        //HERE WE GET THE EMAIL FROM THE USER'S PROFILE FOR PURPOSE OF AUTH
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            emailAtGivenUser = (String) snap.child("email").getValue();
                            firstnameAtGivenUser = (String) snap.child("firstname").getValue();
                            lastnameAtGivenUser = (String) snap.child("lastname").getValue();
                            birthdayAtGivenUser = (String) snap.child("birthday").getValue();
                            countrycodeAtGivenUser = (String) snap.child("phoneCountryCode").getValue();
                            prefixAtGivenUser = (String) snap.child("phonePrefixCode").getValue();
                            phonenumberAtGivenUser = (String) snap.child("phoneNumber").getValue();
                            nexmoPhoneNumberAtGivenUser = (String) snap.child("nexmoPhoneNumber").getValue();
                            uidAtGivenUser = (String) snap.child("uid").getValue();
                            Log.d("User", " emailAtGivenUser= "+  emailAtGivenUser);
                        }
                        userLogin(emailAtGivenUser,password);
                    }
                    else{
                        Log.d("User", "USER DOES NOT EXIST" );
                        Toast.makeText(getApplicationContext(), "User not found.  PLEASE SIGN-UP.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        else {
            wasEmailValid = isValidEmail(email);
            if (wasEmailValid) {
                userLogin(email,password);
            }
            else {
                Toast.makeText(getApplicationContext(), "PLEASE ENTER A VALID USERNAME OR EMAIL", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * userLogin
     * Method checks email and password for authentication
     * @param loginString Either username or email strings.  (At this point username has fetched the corresponding email for this user)
     * @param password password string entered by user
     */
    private void userLogin(String loginString, final String password) {
        mAuth.signInWithEmailAndPassword(loginString, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d("User", "signInWithEmail:onComplete:" + task.isSuccessful());
                    //USER IS NOW AUTHENTICATED!!!
                    //User signed up with email and is not verified
                    if(!emailAtGivenUser.contains("@skoovy.com") && !mAuth.getCurrentUser().isEmailVerified()) {
                        Toast.makeText(getApplicationContext(), "Please check your email for a verification link", Toast.LENGTH_LONG);
                        mAuth.getCurrentUser().sendEmailVerification();
                        mAuth.signOut();
                        return;
                    } else if (emailAtGivenUser.contains("@skoovy.com")) {
                        verifyClient.getUserStatus(countrycodeAtGivenUser, nexmoPhoneNumberAtGivenUser, new SearchListener() {
                                    @Override
                                    public void onException(IOException exception) {}
                                    @Override
                                    public void onUserStatus(UserStatus userStatus) {
                                        switch (userStatus) {
                                            case USER_VERIFIED: {
                                                //Create User instance for this user
                                                final User user = new User();
                                                populateUser(user);
                                                Log.d("User", "Current Skoovy " + user.toString());
                                                continueLogin(user); //Anonymous function cannot return values outside scope
                                                break;
                                            }
                                            default: { //User is not verified
                                                break;
                                            }
                                        }
                                    }
                                    @Override
                                    public void onError(VerifyError errorCode, String errorMessage) {}
                                }
                        );
                    }
                } else {
                    Log.d("User", "signInWithEmail:onComplete: USER NOT AUTHENTICATED" );
                    Log.w("ContentValues", "signInWithEmail", task.getException());
                    Toast.makeText(loginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT)
                            .show();
                }
                }
            }
        );
    }

    private void continueLogin(final User user){
        //Since the user is authenticated, we also need their profile stats
        skoovyUserName = user.getUsername();
//                        findSkoovyUserFollowers();
        FirebaseDatabase skoovyDatabase = FirebaseDatabase.getInstance();
        // Get a reference to our Followers node
        final DatabaseReference currentSkoovyUsersFollowersReference = skoovyDatabase.getReference("Followers");
        currentSkoovyUsersFollowersReference.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(skoovyUserName)){
                    Log.d("User", "found a follower(s) for you");
                    currentSkoovyUsersFollowersReference.child(skoovyUserName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int followers = 0;
                            for(DataSnapshot child : dataSnapshot.getChildren() ){
                                Log.d("User", "FOLLOWER");
                                followers++;
                            }
                            Log.d("User", "followers:"+followers);
                            //WELCOME TO SKOOVY
                            //declare where you intend to go
                            Intent intent6 = new Intent(loginActivity.this, userIsRegisteredActivity.class);
                            //now make it happen
                            user.setPoints(newUserFreePoints);  //starter points for the user
                            intent6.putExtra("User", user);
                            startActivity(intent6);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void populateUser(User newUser) {
        newUser.setFirstname(firstnameAtGivenUser);
        newUser.setLastname(lastnameAtGivenUser);
        newUser.setBirthday(birthdayAtGivenUser);
        newUser.setUsername(email);
        if (emailAtGivenUser.contains("@skoovy.com")){ //user registered via mobile number instead of via email
            //user has not updated email in their Skoovy profile, so we continue to set their email to null.
            newUser.setEmail(null);
        } else {
            newUser.setEmail(emailAtGivenUser);
        }
        newUser.setNexmoPhoneNumber(nexmoPhoneNumberAtGivenUser);
        newUser.setPhoneCountryCode(countrycodeAtGivenUser);
        newUser.setPhonePrefixCode(prefixAtGivenUser);
        newUser.setPhoneNumber(phonenumberAtGivenUser);
        newUser.setPassword(password);
        newUser.setUid(uidAtGivenUser);
    }

    /**
     * isValidEmail
     * Checks for valid email pattern
     * @param target email string entered by user
     * @return boolean true if syntax matches email syntax, else false
     */
    public final  boolean isValidEmail(String target) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
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

    private void findSkoovyUserFollowers() {
        /* Get an instance to our database
//        FirebaseDatabase skoovyDatabase = FirebaseDatabase.getInstance();
//        // Get a reference to our Followers node
//        final DatabaseReference currentSkoovyUsersFollowersReference = skoovyDatabase.getReference("Followers");
//        currentSkoovyUsersFollowersReference.orderByKey().addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                if (dataSnapshot.getKey().equals(skoovyUserName)){
//                    Log.d("User", "found a follower(s) for you");
//                    currentSkoovyUsersFollowersReference.child(skoovyUserName).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            int followers = 0;
//                            for(DataSnapshot child : dataSnapshot.getChildren() ){
//                                Log.d("User", "FOLLOWER");
//                                followers++;
//                            }
//                            Log.d("User", "followers:"+followers);
//                            skoovyuser.setSkoovyUserFollowers(followers);
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
       });*/

    }
}