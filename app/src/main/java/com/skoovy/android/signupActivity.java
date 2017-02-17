package com.skoovy.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;



//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;


public class signupActivity extends Activity {

    private EditText editTextFirstName;
    private EditText editTextLastName;

    public static String firstName;
    public static String lastName;

    ImageButton button1;
    ImageButton button2;

    ImageButton undobutton1;
    ImageButton undobutton2;

    Boolean isEditText1Empty = true;
    Boolean isEditText2Empty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //find undo text buttons
        undobutton1 = (ImageButton) findViewById(R.id.undoButton1);
        undobutton2 = (ImageButton) findViewById(R.id.undoButton2);

        //hide undo buttons at activty startup
        undobutton1.setVisibility(View.INVISIBLE);
        undobutton2.setVisibility(View.INVISIBLE);

        //Retrieve text values entered for first name and last name
        editTextFirstName =  (EditText) findViewById(R.id.firstName);
        editTextLastName = (EditText) findViewById(R.id.lastName);

        //Listen for text on editTextFirstName input field
        editTextFirstName.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){

                //Toast.makeText(signupActivity.this, "text changed", Toast.LENGTH_SHORT).show();
                undobutton1.setVisibility(View.VISIBLE);

                firstName = editTextFirstName.getText().toString().trim();

                if (firstName.length() > 0){
                    //there is text in the first name text field
                    //so we display the undo button
                    undobutton1.setVisibility(View.VISIBLE);
                    isEditText1Empty = false;
                    areBothFieldsSet();
                }
                if (firstName.length() == 0){
                    //there is text in the first name text field
                    undobutton1.setVisibility(View.INVISIBLE);
                    isEditText1Empty = true;
                    areBothFieldsSet();
                }
            }
        });

        //Listen for text on editTextFirstName input field
        editTextLastName.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){

                //Toast.makeText(signupActivity.this, "text changed", Toast.LENGTH_SHORT).show();
                if (!(editTextFirstName.hasFocus())) {
                    undobutton1.setVisibility(View.INVISIBLE);
                }
                lastName = editTextFirstName.getText().toString().trim();

                if (lastName.length() > 0){
                    //there is text in the first name text field
                    //so we display the undo button
                    undobutton2.setVisibility(View.VISIBLE);
                    isEditText2Empty = false;
                    areBothFieldsSet();
                }
                if (lastName.length() == 0){
                    //there is text in the first name text field
                    undobutton2.setVisibility(View.INVISIBLE);
                    isEditText2Empty = true;
                    areBothFieldsSet();
                }
            }
        });

        //Tell my buttons to listen up!
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        button1 = (ImageButton) findViewById(R.id.activityBsignupButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get text from values entered and trim whitespace
                firstName = editTextFirstName.getText().toString().trim();
                lastName = editTextLastName.getText().toString().trim();

                //Detect empty fields before allowing user to continue to next activity
                if(TextUtils.isEmpty(firstName)){
                    //firstName is empty
                    Toast.makeText(getApplicationContext(), "Please enter firstName", Toast.LENGTH_SHORT).show();
                    //stopping the function from executing further
                    return;
                }
                if(TextUtils.isEmpty(lastName)){
                    //lastName is empty
                    Toast.makeText(getApplicationContext(), "Please enter lastName", Toast.LENGTH_SHORT).show();
                    //stopping the function from executing further
                    return;
                }

                //Both text fields were filled, so we allow user to continue to next activity
                //place logic here to do login action
                //declare where you intend to go
                Intent intent1 = new Intent(signupActivity.this, whatsYourBirthdayActivity.class);
                //now make it happen
                startActivity(intent1);
            }
        });

        button2 = (ImageButton) findViewById(R.id.backButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //place logic here to do back button action
                finish();
            }
        });

        undobutton1 = (ImageButton) findViewById(R.id.undoButton1);
        undobutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextFirstName.setText("");
                undobutton1.setVisibility(View.INVISIBLE);
            }
        });

        undobutton2 = (ImageButton) findViewById(R.id.undoButton2);
        undobutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextLastName.setText("");
                undobutton2.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void areBothFieldsSet(){
        if ((!(isEditText1Empty))&&(!(isEditText2Empty))) {
            //switch image on signup button
            button1.setImageResource(R.drawable.signup);
        }
    }
}