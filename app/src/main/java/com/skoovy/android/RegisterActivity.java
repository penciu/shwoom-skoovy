package com.skoovy.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

//TODO: Hey Guys,

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private ImageButton mEmailRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.emailTextField);

        mPasswordView = (EditText) findViewById(R.id.passwordTextField);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailRegisterButton = (ImageButton) findViewById(R.id.signUpImageButton);
    }

    public void attemptRegister(View view){
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        //TODO: Register User

    }
}