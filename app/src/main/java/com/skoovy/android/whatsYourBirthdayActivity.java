package com.skoovy.android;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Intent;

import java.util.Calendar;

public class whatsYourBirthdayActivity extends AppCompatActivity {



    public static String birthdate;
    EditText editTextBirthdate;

    Button button1;        //go on to next activity
    ImageButton button2;        //go back to previous screen with BACK ARROW
    ImageButton undoButton1;    //undo input text in edit text field

    final Calendar c = Calendar.getInstance();
    private DatePicker myDatePicker;
    final int minAge = 13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_your_birthday);



        //get font asset
        Typeface centuryGothic = Typeface.createFromAsset(getAssets(), "fonts/Century Gothic.ttf");
        button1 = (Button) findViewById(R.id.next);
        button1.setTypeface(centuryGothic);

        //find elements in this activity
        myDatePicker = ((DatePicker)findViewById(R.id.datePicker));
        editTextBirthdate = ((EditText)findViewById(R.id.dob));
        undoButton1 = ((ImageButton)findViewById(R.id.undoButton1));

        //upon start of activity hide the undo text for editText
        undoButton1.setVisibility(View.INVISIBLE);

        //upon start of activity hide the DatePicker
        myDatePicker.setVisibility(View.INVISIBLE);

        //upon start of activity place current date in Datepicker
        setCurrentDateOnView();


        //this gets the values from the DatePicker and places the text in the editText
        myDatePicker.init(myDatePicker.getYear(), myDatePicker.getMonth(), myDatePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener()
        {
            public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3)
            {
                //Convert integer month to string month
                String [] monthArray = new String[] {"January ","February ","March ","April ","May ","June ","July ","August ","September ","October ","November ","December "};
                String month = "";
                //switch on the month integer
                switch (arg2) {
                    case 0: month = monthArray[0]; break;
                    case 1: month = monthArray[1]; break;
                    case 2: month = monthArray[2]; break;
                    case 3: month = monthArray[3]; break;
                    case 4: month = monthArray[4]; break;
                    case 5: month = monthArray[5]; break;
                    case 6: month = monthArray[6]; break;
                    case 7: month = monthArray[7]; break;
                    case 8: month = monthArray[8]; break;
                    case 9: month = monthArray[9]; break;
                    case 10: month = monthArray[10]; break;
                    case 11: month = monthArray[11]; break;
                }

                //Now we set the date from the DatePicker into the text field
                editTextBirthdate.setText(" " + month + arg3 + ",  " + arg1);
                //and present the undo button
                undoButton1.setVisibility(View.VISIBLE);

                //THIS IS WHERE THE LOGIC TO CHANGE BUTTON BACKGROUND SHOULD GO
                //NEED TO EVALUATE IF DATEPICKER DATE IS LESS THAN 13 YEARS THAN TODAY
                //IF TRUE, THEN SET BUTTON BACKGROUND TO ORANGE
                birthdate = editTextBirthdate.getText().toString().trim();
                if (TextUtils.isEmpty(birthdate)) {
                    button1.setBackgroundResource(R.drawable.roundedgreybutton);
                    return;
                }
                //check that age is at least 13 years old
                if (myDatePicker.getYear() > c.get(Calendar.YEAR) - minAge ) {
                    //Toast.makeText(getApplicationContext(), "SORRY, YOU MUST BE AT LEAST 13 YEARS", Toast.LENGTH_LONG).show();
                    button1.setBackgroundResource(R.drawable.roundedgreybutton);
                    //we exit from method
                    return;
                }
                if ((myDatePicker.getYear() == (c.get(Calendar.YEAR) - minAge)) && ((myDatePicker.getMonth() > c.get(Calendar.MONTH)))){
                    //Toast.makeText(getApplicationContext(), "SORRY, YOU MUST BE AT LEAST 13 YEARS", Toast.LENGTH_LONG).show();
                    button1.setBackgroundResource(R.drawable.roundedgreybutton);
                    //we exit from method
                    return;
                }
                if ((myDatePicker.getYear() == (c.get(Calendar.YEAR) - minAge)) && ((myDatePicker.getMonth() == c.get(Calendar.MONTH) + 0)) && (myDatePicker.getDayOfMonth() > c.get(Calendar.DAY_OF_MONTH))){
                    //Toast.makeText(getApplicationContext(), "SORRY, YOU MUST BE AT LEAST 13 YEARS", Toast.LENGTH_LONG).show();
                    button1.setBackgroundResource(R.drawable.roundedgreybutton);
                    //we exit from method
                    return;
                }
                button1.setBackgroundResource(R.drawable.roundedorangebutton);
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

    /**
     * addListenerOnButton
     * Listens to the buttons of this activity
     */
    public void addListenerOnButton()
    {
        //undo input text in edit text field
        undoButton1 = ((ImageButton)findViewById(R.id.undoButton1));
        undoButton1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                editTextBirthdate.setText("");
                button1.setBackgroundResource(R.drawable.roundedgreybutton);
                undoButton1.setVisibility(View.INVISIBLE);
            }
        });

        //go on to next activity
        button1 = (Button)findViewById(R.id.next);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                //place logic here to do signup action
                birthdate = editTextBirthdate.getText().toString().trim();
                if (TextUtils.isEmpty(birthdate)) {
                    return;
                }
                //check that age is at least 13 years old
                if (myDatePicker.getYear() > c.get(Calendar.YEAR) - minAge ) {
                    Toast.makeText(getApplicationContext(), "SORRY, YOU MUST BE AT LEAST 13 YEARS OLD", Toast.LENGTH_LONG).show();
                    //we exit from method
                    return;
                }
                if ((myDatePicker.getYear() == (c.get(Calendar.YEAR) - minAge)) && ((myDatePicker.getMonth() > c.get(Calendar.MONTH)))){
                    Toast.makeText(getApplicationContext(), "SORRY, YOU MUST BE AT LEAST 13 YEARS OLD", Toast.LENGTH_LONG).show();
                    //we exit from method
                    return;
                }
                if ((myDatePicker.getYear() == (c.get(Calendar.YEAR) - minAge)) && ((myDatePicker.getMonth() == c.get(Calendar.MONTH) + 0)) && (myDatePicker.getDayOfMonth() > c.get(Calendar.DAY_OF_MONTH))){
                    Toast.makeText(getApplicationContext(), "SORRY, YOU MUST BE AT LEAST 13 YEARS OLD", Toast.LENGTH_LONG).show();
                    //we exit from method
                    return;
                }

                Intent intent1 = getIntent();
                User user = (User)intent1.getSerializableExtra("user");
                //User meets the minAge requirement
                user.setBirthday(birthdate);

                //Toast.makeText(getApplicationContext(), "YEAH! YOU ARE OF AGE", Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "USER FIRST NAME: " + signupActivity.firstName + "\nUSER LAST NAME: " + signupActivity.lastName + "\nUSER BIRTHDATE: " + birthdate, Toast.LENGTH_LONG).show();

                //declare where you intend to go
                Intent intent2 = new Intent(whatsYourBirthdayActivity.this, signupCreateUsernameActivity.class);
                //now make it happen
                intent2.putExtra("user", user);
                startActivity(intent2);
            }

        });

        //go back to previous screen
        button2 = ((ImageButton)findViewById(R.id.backButton));
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * setCurrentDateOnView
     * Initializes the DatePicker to today's date
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
