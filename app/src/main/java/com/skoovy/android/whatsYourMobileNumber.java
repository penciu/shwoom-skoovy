package com.skoovy.android;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.skoovy.android.Dialogs.SingleChoiceDialogFragment;


public class whatsYourMobileNumber extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, SingleChoiceDialogFragment.MyDialogFragmentListener {

    TextView cc;
    EditText mobilePhoneNumber;

    public static String phoneNumber;
    public static String countryAbrv = "US";
    public static String countryCode = "(+1)"; //Set to default

    Button button1;
    ImageButton button2;
    Button button3;
    ImageButton undobutton1;

    Boolean isEditText1Empty = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_your_mobile_number);

        //get font asset
        Typeface centuryGothic = Typeface.createFromAsset(getAssets(), "fonts/Century Gothic.ttf");

        //find my activity's widgets and setup listeners
        cc = (TextView) findViewById(R.id.countrycode);
        cc.setOnTouchListener(MyOnTouchListener);
        button1 = (Button) findViewById(R.id.signupButton);
        button3 = (Button) findViewById(R.id.signUpWithEmailInsteadButton);

        //set font on button
        button1.setTypeface(centuryGothic);

        //find undo text buttons
        undobutton1 = (ImageButton) findViewById(R.id.undoButton1);
            //hide undo buttons at activty startup
            undobutton1.setVisibility(View.INVISIBLE);

        mobilePhoneNumber = (EditText) findViewById(R.id.registerYourMobileNuber);

        //Listen for text on editTextFirstName input field
        mobilePhoneNumber.addTextChangedListener(new TextWatcher(){

            //we need to know if the user is erasing or inputing some new character
            private boolean backspacingFlag = false;
            //we need to block the :afterTextChanges method to be called again after we just replaced the EditText text
            private boolean editedFlag = false;
            //we need to mark the cursor position and restore it after the edition
            private int cursorComplement;

            public void beforeTextChanged(CharSequence s, int start, int count, int after){
                //we store the cursor local relative to the end of the string in the EditText before the edition
                cursorComplement = s.length()-mobilePhoneNumber.getSelectionStart();
                //we check if the user ir inputing or erasing a character
                if (count > after) {
                    backspacingFlag = true;
                } else {
                    backspacingFlag = false;
                }
            }
            public void onTextChanged(CharSequence s, int start, int before, int count){
                phoneNumber = mobilePhoneNumber.getText().toString().trim();

                if (phoneNumber.length() > 0){
                    undobutton1.setVisibility(View.VISIBLE);
                    isEditText1Empty = false;
                }
                if (phoneNumber.length() == 0){
                    undobutton1.setVisibility(View.INVISIBLE);
                    isEditText1Empty = true;
                }
                if (phoneNumber.length() == 14){
                    button1.setBackgroundResource(R.drawable.roundedorangebutton);
                }
            }
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                //what matters are the phone digits beneath the mask, so we always work with a raw string with only digits
                String phone = string.replaceAll("[^\\d]", "");

                //if the text was just edited, :afterTextChanged is called another time... so we need to verify the flag of edition
                //if the flag is false, this is a original user-typed entry. so we go on and do some magic
                if (!editedFlag) {

                    //we start verifying the worst case, many characters mask need to be added
                    //example: 999999999 <- 6+ digits already typed
                    // masked: (999) 999-999
                    if (phone.length() >= 6 && !backspacingFlag) {
                        //we will edit. next call on this textWatcher will be ignored
                        editedFlag = true;
                        //here is the core. we substring the raw digits and add the mask as convenient
                        String ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3,6) + "-" + phone.substring(6);
                        mobilePhoneNumber.setText(ans);
                        //we deliver the cursor to its original position relative to the end of the string
                        mobilePhoneNumber.setSelection(mobilePhoneNumber.getText().length()-cursorComplement);

                        //we end at the most simple case, when just one character mask is needed
                        //example: 99999 <- 3+ digits already typed
                        // masked: (999) 99
                    } else if (phone.length() >= 3 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = "(" +phone.substring(0, 3) + ") " + phone.substring(3);
                        mobilePhoneNumber.setText(ans);
                        mobilePhoneNumber.setSelection(mobilePhoneNumber.getText().length()-cursorComplement);
                    }
                    // We just edited the field, ignoring this circle of the watcher and getting ready for the next
                } else {
                    editedFlag = false;
                }
            }
        });


        //Tell my buttons to listen up!
        addListenerOnButton();
    }

    View.OnTouchListener MyOnTouchListener = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View view, MotionEvent event) {

            switch(event.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:
                    //A pressed gesture has started, the motion contains the initial starting location.
                    // cc.setText("ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    //A non-primary pointer has gone down.
                    //cc.setText("ACTION_POINTER_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    //A change has happened during a press gesture (between ACTION_DOWN and ACTION_UP).
                    //cc.setText("ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    //A pressed gesture has finished.
                    //cc.setText("ACTION_UP");
                    showChoiceDialog();

                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    //A non-primary pointer has gone up.
                    //cc.setText("ACTION_POINTER_UP");
                    break;
            }

            return true;
        }

    };

    //This onReturnValue returns the dialog choice for countryAbrv and countryCode
    @Override
    public void onReturnValue(String foo) {
        //Toast.makeText(getApplicationContext(), "USER choice: " + SingleChoiceDialogFragment.choice, Toast.LENGTH_LONG).show();
        String choiceString = SingleChoiceDialogFragment.choice;

        //locate index of '(' and ')'
        Integer indexOpenParen = choiceString.indexOf("(");
        Integer indexCloseParen = choiceString.indexOf(")");

        //build strings to place in country code textView
        countryAbrv = choiceString.substring(indexCloseParen+2);
        countryCode = choiceString.substring(indexOpenParen,indexCloseParen + 1);

        //update text in country code textView
        cc.setText(countryAbrv + " " + countryCode);
    }

    /**
     * showChoiceDialog
     * Display country code single choice selection dialog
     */
    private void showChoiceDialog() {
        SingleChoiceDialogFragment complexDialog = new SingleChoiceDialogFragment();
        complexDialog.show(getSupportFragmentManager(),"SingleChoiceDialogFragment");

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) { }


    /**
     * addListenerOnButton
     * Listens to the buttons of this activity
     */
    public void addListenerOnButton() {

        //Listens for button to go to next activity clicked
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get text from values entered and trim whitespace
                phoneNumber = mobilePhoneNumber.getText().toString().trim();

                //Detect empty field before allowing user to continue to next activity
                if(TextUtils.isEmpty(phoneNumber)){
                    //mobile number is empty
                    Toast.makeText(getApplicationContext(), "Please enter your mobile phone number", Toast.LENGTH_SHORT).show();
                    //stopping the function from executing further
                    return;
                }

                //Detect if phone number field has enough numbers before allowing user to continue to next activity
                if(phoneNumber.length() < 14){
                    //mobile number is not long enough
                    Toast.makeText(getApplicationContext(), "Please enter a valid mobile phone number", Toast.LENGTH_SHORT).show();
                    //stopping the function from executing further
                    return;
                }

                Toast.makeText(getApplicationContext(), "countryCode:"+countryCode, Toast.LENGTH_SHORT).show();
                //Phone number text field was filled, so we allow user to continue to next activity
                //User satisfied phone number requirement
                Intent intent5 = getIntent();
                User user = (User)intent5.getSerializableExtra("user");
//                Log.d("User", user.toString());
//                Log.d("User", "user's country: "+ countryAbrv);
//                Log.d("User", "user's phoneprefix: "+ phoneNumber);
//                Log.d("User", "user's countryCode: "+ countryCode);

                //User meets the username requirement

                user.setPhoneCountryCode(countryAbrv);
                user.setPhonePrefixCode(countryCode);
                user.setPhoneNumber(phoneNumber.substring(1,4) + phoneNumber.substring(6,9) + phoneNumber.substring(10,14));

                user.getNexmoPhoneNumber();
//                Log.d("User", user.toString());

                //place logic here to do login action
                //declare where you intend to go
                Intent intent6 = new Intent(whatsYourMobileNumber.this, verifyMobileNumberActivity.class);
                //now make it happen
                intent6.putExtra("user", user);
                startActivity(intent6);
            }
        });

        //listens for back arrow button
        button2 = (ImageButton) findViewById(R.id.backToEmailButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //place logic here to do button action
                Intent intent4 = getIntent();
                User user = (User)intent4.getSerializableExtra("user");
                Intent intent5 = new Intent(whatsYourMobileNumber.this, whatsYourEmailActivity.class);
                //now make it happen
                intent5.putExtra("user", user);
                startActivity(intent5);
            }
        });

        //listens for 'Sign up with email instead' button
        button3 = (Button) findViewById(R.id.signUpWithEmailInsteadButton);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //place logic here to do button action
                Intent intent4 = getIntent();
                User user = (User)intent4.getSerializableExtra("user");
                Intent intent5 = new Intent(whatsYourMobileNumber.this, whatsYourEmailActivity.class);
                //now make it happen
                intent5.putExtra("user", user);
                startActivity(intent5);

            }
        });

        //listens for undo text button for first name field
        undobutton1 = (ImageButton) findViewById(R.id.undoButton1);
        undobutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobilePhoneNumber.setText("");
                button1.setBackgroundResource(R.drawable.roundedgreybutton);
                undobutton1.setVisibility(View.INVISIBLE);
            }
        });


    }

}

