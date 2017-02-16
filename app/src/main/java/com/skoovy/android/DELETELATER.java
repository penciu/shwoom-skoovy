package com.skoovy.android;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DELETELATER extends AppCompatActivity {

    //Firebase Auth Stuff
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletelater);

        //More Firebase Authentication stuff
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent nextScreen = new Intent(DELETELATER.this, StartScreen.class);
                    startActivity(nextScreen);
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void logOut(View view) {
        mAuth.signOut();
    }
}
