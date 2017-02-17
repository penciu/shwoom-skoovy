package com.skoovy.android;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/*
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.*;
*/
public class signupCreateUsernameActivity extends Activity {

    // [START declare_database_ref]
//    private DatabaseReference mDatabase;

    private EditText editTextUserName;
    private String userName;

    ImageButton button3;
    ImageButton button4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set up database reference, and reference the location we write to
//        mDatabase = FirebaseDatabase
//                .getInstance()
//                .getReference()
//                .child("userInfo");//to send data to correct child

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_create_username);

        //Retrieve text values entered for first name and last name
        editTextUserName =  (EditText) findViewById(R.id.registerUsername);

        //Tell my buttons to listen up!
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        button3 = (ImageButton) findViewById(R.id.activityCsignupButton);
        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //get text from values entered and trim whitespace
                userName = editTextUserName.getText().toString().trim();

                //Detect empty fields before allowing user to continue to next activity
                if (TextUtils.isEmpty(userName)) {
                    //userName is empty
                    Toast.makeText(getApplicationContext(), "Please enter userName", Toast.LENGTH_SHORT).show();
                    //stopping the function from executing further
                    return;
                }

                // Get an instance to our database
//                FirebaseDatabase skoovyDatabase = FirebaseDatabase.getInstance();
                // Get a  reference to our userInfo node
//                DatabaseReference currentSkoovyUsers = skoovyDatabase.getReference("userInfo");

//                currentSkoovyUsers.orderByChild("username").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
                // do some stuff once
//                        if(dataSnapshot.exists()){
//                            System.out.println("Already in use"+dataSnapshot.getChildren());
//                            Toast.makeText(getApplicationContext(), "Already in use", Toast.LENGTH_SHORT).show();
//                            updateTextView(userName + " is already taken. Try again.");
//                        }
//                        else{
//                            System.out.println("not found");
//                            Toast.makeText(getApplicationContext(), "not found", Toast.LENGTH_SHORT).show();
//                            updateTextView("");
//                        }

//                    }

//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {

//                    }
//                });
                Toast.makeText(getApplicationContext(), "TODO ITEM", Toast.LENGTH_SHORT).show();

            }
        });






        //CODE BELOW IS TO SEND DATA TO FIREBASE ABOUT USER //MAYBE NEEDED ELSEWHERE
        //Text field was filled, so we allow user to continue to next activity
        //place logic here to do login action
        //Toast.makeText(getApplicationContext(), "activityC signup button clicked", Toast.LENGTH_SHORT).show();

        //this is where you want to send first name, last name, and unique user name to firebase database

        //Call method to build json object in order to send to databasesave firstName, lastName, and userName to database
        //passing the userName is not neccessary here, as we have it for further use here as a class variable
        //             String personsFirstLastUserName = objectBuilder(signupActivity.firstName,signupActivity.lastName);

        //Call method to write to database
        //             registerUserToDatabase(personsFirstLastUserName);
        //    String key = mDatabase.push().child(signupActivity.firstName).child(signupActivity.lastName).child(userName).setValue();
        //   Toast.makeText(getApplicationContext(), "Your UID is: " + key, Toast.LENGTH_SHORT).show();
        //declare where you intend to go
        //Intent intent4 = new Intent(signupCreateUsernameActivity.this, signupCreateUsernameActivity.class);
        //now make it happen
        //startActivity(intent4);
        //    registerUserToDatabase();



        button4 = (ImageButton) findViewById(R.id.backtoNameButton);
        button4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //place logic here to do back button action
                finish();
            }
        });
    }

    //Object builder for writing to database
    //   public String objectBuilder( String firstName, String lastName){
    //       String objectToSend = "{\"firstName\":" + firstName + ",\"lastName\":" + lastName + ",\"username\":" +
    //               userName + "}";
    //       return objectToSend;
    //   }

    // Write a name to the database
    //   public void registerUserToDatabase(String names) {
    //       mDatabase.push().setValue(names);
    //   }
    public void updateTextView(String toThis) {
        TextView textView = (TextView) findViewById(R.id.userTaken);
        textView.setText(toThis);
    }

//    private void registerUserToDatabase() {
//        User user = new User(signupActivity.firstName,signupActivity.lastName, userName);

//        mDatabase.push().setValue(user);
//    }

}
