package com.skoovy.android;

import android.app.Activity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;


public class loginActivity extends Activity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmailView;
    private EditText mPasswordView;
    private ImageButton mEmailSignInButton;
    public static String email;
    public static String password;
    ImageButton button1;
    ImageButton button2;
    ImageButton undoButton1;
    ImageButton undoButton2;
    Button passwordreset;
    boolean isEditText1Empty = true;
    boolean isEditText2Empty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        undoButton1 = ((ImageButton)findViewById(R.id.undoButton1));
        undoButton2 = ((ImageButton)findViewById(R.id.undoButton2));

        undoButton1.setVisibility(View.INVISIBLE);
        undoButton2.setVisibility(View.INVISIBLE);

        button1 = ((ImageButton)findViewById(R.id.loginImageButton));

        passwordreset = ((Button)findViewById(R.id.passwordHelp));

        mEmailView = ((EditText)findViewById(R.id.emailTextField));
        mPasswordView = ((EditText)findViewById(R.id.passwordTextField));


        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                email = mEmailView.getText().toString().trim();
                if (email.length() == 0) {
                    undoButton1.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        mEmailView.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                email = mEmailView.getText().toString().trim();
                if (email.length() > 0) {
                    undoButton1.setVisibility(View.VISIBLE);
                }
                undoButton2.setVisibility(View.INVISIBLE);
                return false;
            }
        });
        mPasswordView.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                password = mPasswordView.getText().toString().trim();
                if (password.length() > 0) {
                    undoButton2.setVisibility(View.VISIBLE);
                }
                undoButton1.setVisibility(View.INVISIBLE);
                return false;
            }
        });
        this.mEmailView.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                email = mEmailView.getText().toString().trim();
                if (email.length() > 0)
                {
                    isEditText1Empty = false;

                    undoButton1.setVisibility(View.VISIBLE);

                    canIChangeLogInButton();
                }
                if (email.length() == 0)
                {
                    isEditText1Empty = true;

                    undoButton1.setVisibility(View.INVISIBLE);

                    canIChangeLogInButton();
                }
            }
        });
        mPasswordView.addTextChangedListener(new TextWatcher()
        {
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
                    isEditText2Empty = false;

                    undoButton2.setVisibility(View.VISIBLE);

                    canIChangeLogInButton();
                }
                if (password.length() == 0)
                {
                    isEditText2Empty = true;

                    undoButton2.setVisibility(View.INVISIBLE);

                    canIChangeLogInButton();
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

        mEmailSignInButton = ((ImageButton)findViewById(R.id.loginImageButton));
        mEmailSignInButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                attemptLogin();
            }
        });
        addListenerOnButton();
    }

    public void canIChangeLogInButton()
    {
        if ((!this.isEditText1Empty) && (!this.isEditText2Empty)) {
            this.button1.setImageResource(R.drawable.login);   //    THIS NEEDS DIFFERENT SRC
        } else {
            this.button1.setImageResource(R.drawable.login);
        }
    }

    private void attemptLogin()
    {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful())
                        {
                            Log.w("ContentValues", "signInWithEmail", task.getException());
                            Toast.makeText(loginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

    public void addListenerOnButton()
    {
        undoButton1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                mEmailView.setText("");
            }
        });
        undoButton2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                mPasswordView.setText("");
            }
        });
        button1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                email = mEmailView.getText().toString().trim();
                password = mPasswordView.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    return;
                }
                if (TextUtils.isEmpty(password)) {}
            }
        });
        button2 = ((ImageButton)findViewById(R.id.backtToStartButton));
        button2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                finish();
            }
        });
        passwordreset = ((Button)findViewById(R.id.passwordHelp));
        passwordreset.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent intent = new Intent(loginActivity.this, resetPasswordActivity.class);

                startActivity(intent);
            }
        });
    }
}

