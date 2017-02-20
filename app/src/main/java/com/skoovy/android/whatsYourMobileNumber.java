package com.skoovy.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.skoovy.android.Dialogs.SingleChoiceDialogFragment;

public class whatsYourMobileNumber extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, SingleChoiceDialogFragment.MyDialogFragmentListener {

    TextView cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_your_mobile_number);


        //find my activity's widgets and setup listener
        cc = (TextView) findViewById(R.id.countrycode);
        cc.setOnTouchListener(MyOnTouchListener);
    }

    View.OnTouchListener MyOnTouchListener = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            // TODO Auto-generated method stub

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

    @Override
    public void onReturnValue(String foo) {
        Toast.makeText(getApplicationContext(), "USER choice: " + SingleChoiceDialogFragment.choice, Toast.LENGTH_LONG).show();
        String choiceString = SingleChoiceDialogFragment.choice;
        String countryAbrv = choiceString.substring(choiceString.length()-3);
        //locate index of '('
        Integer indexOpenParen = choiceString.indexOf("(");
        Integer indexCloseParen = choiceString.indexOf(")");
        String countryCode = choiceString.substring(indexOpenParen,indexCloseParen + 1);
        cc.setText(countryAbrv + " " + countryCode);
    }
/*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.countrycode:
                // showChoiceDialog();


                break;

        }
    }
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
    public void onClick(View v) {

    }
}

