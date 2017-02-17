package com.skoovy.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Intent;

import java.util.Calendar;

public class whatsYourBirthdayActivity extends AppCompatActivity {
    public static String birthdate;
    EditText editTextBirthdate;
    ImageButton button1;
    ImageButton button2;
    ImageButton undoButton1;
    final Calendar c = Calendar.getInstance();
    private DatePicker myDatePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_your_birthday);

        myDatePicker = ((DatePicker)findViewById(R.id.datePicker));
        editTextBirthdate = ((EditText)findViewById(R.id.dob));
        undoButton1 = ((ImageButton)findViewById(R.id.undoButton1));

        undoButton1.setVisibility(View.INVISIBLE);

        myDatePicker.setVisibility(View.INVISIBLE);

        setCurrentDateOnView();

        myDatePicker.init(myDatePicker.getYear(), myDatePicker.getMonth(), myDatePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener()
        {
            public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3)
            {
                //Convert integer month to string month
                String [] monthArray = new String[] {"January ","February ","March ","April ","May ","June ","July ","August ","September ","October ","November ","December "};
                String month = "";
                //Array is 0 based, months are not
                switch (arg2) {
                    case 1: month = monthArray[0]; break;
                    case 2: month = monthArray[1]; break;
                    case 3: month = monthArray[2]; break;
                    case 4: month = monthArray[3]; break;
                    case 5: month = monthArray[4]; break;
                    case 6: month = monthArray[5]; break;
                    case 7: month = monthArray[6]; break;
                    case 8: month = monthArray[7]; break;
                    case 9: month = monthArray[8]; break;
                    case 10: month = monthArray[9]; break;
                    case 11: month = monthArray[10]; break;
                    case 12: month = monthArray[11]; break;
                }

                editTextBirthdate.setText(" " + month + arg3 + ", " + arg1);
                undoButton1.setVisibility(View.VISIBLE);
            }
        });

        //initialize input text field to empty
        editTextBirthdate.setText("");

        //listen for touch on edit text field
        editTextBirthdate.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                myDatePicker.setVisibility(View.VISIBLE);

                return false;
            }
        });

        //tell my buttons to listen up!
        addListenerOnButton();
    }

    public void addListenerOnButton()
    {
        //undo input text in edit text field
        undoButton1 = ((ImageButton)findViewById(R.id.undoButton1));
        undoButton1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                editTextBirthdate.setText("");
                undoButton1.setVisibility(View.INVISIBLE);
            }
        });

        //go on to next activity
        button1 = ((ImageButton)findViewById(R.id.next));
        button1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                birthdate = editTextBirthdate.getText().toString().trim();
                if (TextUtils.isEmpty(birthdate)) {
                    return;
                }
                Intent intent1 = new Intent(whatsYourBirthdayActivity.this, signupCreateUsernameActivity.class);

                startActivity(intent1);
            }
        });

        //go back to previous screen
        button2 = ((ImageButton)findViewById(R.id.backtoNameButton));
        button2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    /**
     *
     *
     */
    public void setCurrentDateOnView()
    {
        myDatePicker = ((DatePicker)findViewById(R.id.datePicker));

        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        editTextBirthdate = ((EditText)findViewById(R.id.dob));
        editTextBirthdate.append(new StringBuilder()

                .append(mMonth).append(mDay).append(",")
                .append(mYear).append(" "));

        myDatePicker.init(mYear, mMonth, mDay, null);
    }
}
