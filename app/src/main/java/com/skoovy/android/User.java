package com.skoovy.android;


/*
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
*/
public class User {

    public String firstname;
    public String lastname;
    public String birthday;
    public String username;
    public String email;
    public String phoneCountryCode;
    public String phoneNumber;
    public String password;



    public User() {
        //Default constructor
    }

    public User(String firstname, String lastname, String birthday, String username, String email, String phoneCountryCode, String phoneNumber, String password){
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday= birthday;
        this.username = username;
        this.email= email;
        this.phoneCountryCode= phoneCountryCode;
        this.phoneNumber= phoneNumber;
        this.password= password;
    }


    //DatabaseReference currentSkoovyUsers = database.getReference("server/saving-data/skoovy/userInfo");
//    var currentSkoovyUsers = firebase.database().ref("server/saving-data/skoovy/userInfo");

    // currentSkoovyUsers.orderByChild("username").addChildEventListener(new ChildEventListener()){
    //}
    // database.orderByChild("username").addChildEventListener(new ChildEventListener()){

    // }

    //currentSkoovyUsers.orderByChild("username").equalTo(username).addChildEventListener(new ChildEventListener() {
    //  @Override
    //public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
    //  System.out.println(dataSnapshot.getKey());
    // }

    // ...
    // });


}
