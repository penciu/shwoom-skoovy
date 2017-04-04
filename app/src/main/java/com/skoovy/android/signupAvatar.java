package com.skoovy.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class signupAvatar extends Activity implements ViewSwitcher.ViewFactory, AdapterView.OnItemSelectedListener {
    //DECLARATIONS
    //GUI
    ImageSwitcher imgSwitcher;
    TextView tv;

    Button button1;  //next activity button
    ImageButton button2;  //back arrow button

    //refs
    private DatabaseReference fbDatabase;

    //vars
    private int avatarPosition;

    int imgs[] =
            {
                    R.drawable.c1,
                    R.drawable.c2,
                    R.drawable.c3,
                    R.drawable.c4,
                    R.drawable.c5,
                    R.drawable.c6,
                    R.drawable.c7,
                    R.drawable.c8,
                    R.drawable.c9,
                    R.drawable.c10,
                    R.drawable.c11,
                    R.drawable.c12,
                    R.drawable.c13,
                    R.drawable.c14,
                    R.drawable.c15,
                    R.drawable.c16,
                    R.drawable.c17,
                    R.drawable.c18,
                    R.drawable.c19,
                    R.drawable.c20,
                    R.drawable.c21,
                    R.drawable.c22,
                    R.drawable.c23,
                    R.drawable.c24
            };

    String descriptor[] =
            {
                    "running",
                    "grit-teeth",
                    "smirky",
                    "hmmf...",
                    "whaaa!",
                    "dancing",
                    "happy",
                    "Excited",
                    "mr travel",
                    "java",
                    "luv bug",
                    "Content Monster",
                    "brad pit",
                    "eeyore",
                    "kidney bean",
                    "bow tie",
                    "doper",
                    "singer",
                    "one-hand",
                    "speedy",
                    "i don't like that",
                    "?what?",
                    "lazy",
                    "loud"
            };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_avatar);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (ImageButton) findViewById(R.id.backButton);
        imgSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
        imgSwitcher.setFactory(this);
        imgSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        imgSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

        Gallery gallery = (Gallery) findViewById(R.id.gallery1);
        gallery.setAdapter(new ImageAdapter(this));

        //get font asset
        Typeface centuryGothic = Typeface.createFromAsset(getAssets(), "fonts/Century Gothic.ttf");

        //set font on button
        button1.setTypeface(centuryGothic);


        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                imgSwitcher.setImageResource(imgs[position]);
                Log.d("User", "position -->>" + position);
                position++;
                Toast.makeText(getApplicationContext(), "AVATAR SELECTED-->> #" + position, Toast.LENGTH_SHORT).show();
            }
        });

        gallery.setOnItemSelectedListener(this);

        //Tell my buttons to listen up!
        addListenerOnButton();

    }//end of onCreate

    public void addListenerOnButton() {
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NEED USER OBJECT HERE TO BE ABLE TO PUSH AVATAR DATA TO FIREBASE DB
                Intent intent6 = getIntent();
                User user = (User) intent6.getSerializableExtra("User");

                //UPDATE USER OBJECT WITH AVATAR SELECTION (for display by user profile)
                user.setAvatar(Integer.toString(avatarPosition));
                // ====================  AND  =========================== //
                //WE NEED TO UPDATE DB WITH USER'S AVATAR SELECTION THEN WE CAN PROCEED TO NEXT ACTIVITY
                fbDatabase = FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("userInfo")
                        .child(user.getUid());//pointer to user's profile data to user's userInfo node

                //update avatar data for user in Firebase DB
                Map<String, Object> userUpdates = new HashMap<>();
                userUpdates.put("avatar", Integer.toString(avatarPosition));
                fbDatabase.updateChildren(userUpdates);

                //PROCEED TO NEXT ACTIVITY
                //declare where you intend to go
                Intent intent3 = new Intent(signupAvatar.this, UserProfile.class);
                intent3.putExtra("User", user);
                //now make it happen
                startActivity(intent3);
            }
        });

        //button2 is the BACK ARROW button
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //place logic here to do back button action
                finish();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tv = (TextView) findViewById(R.id.textView);
        tv.setText(descriptor[position]);
        avatarPosition = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class ImageAdapter extends BaseAdapter {

        private Context mContext;

        public ImageAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            return imgs.length;
        }

        public Object getItem(int arg0) {

            return arg0;
        }

        public long getItemId(int arg0) {
            return arg0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d("User", "*******************************************");
            ImageView imageView;  //THIS IS THE IMAGE VIEW USED IN THE IMAGE ADAPTER

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            //initialize size for use in imageview params
            int thirdWidth = (int) ((size.x / 3.25) - 15);
            int scaledHeight = (int) (thirdWidth * .8);

            Log.d("User", "thirdWidth ===> " + thirdWidth);
            Log.d("User", "scaledHeight ===> " + scaledHeight);
            if (convertView == null) {
                Log.d("User", "position ===> " + position);
                Log.d("User", "convertView ===> null");
                imageView = new ImageView(mContext);
//                imageView.setImageResource(imgs[position]);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(20, 0, 20, 0);
                //     imageView.setLayoutParams(new Gallery.LayoutParams(thirdWidth, scaledHeight));
                imageView.setLayoutParams(new Gallery.LayoutParams(thirdWidth, scaledHeight));
            } else {
                Log.d("User", "position ===> " + position);
                Log.d("User", "convertView ===> !null");
                imageView = (ImageView) convertView;
//                imageView.setImageResource(imgs[position]);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(20, 0, 20, 0);
                imageView.setLayoutParams(new Gallery.LayoutParams(thirdWidth, scaledHeight));
            }

            imageView.setImageResource(imgs[position]);
            return imageView;
        }
    }

    public View makeView() {
        ImageView iView = new ImageView(this);
        iView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iView.setLayoutParams(new ImageSwitcher.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, 0));
        iView.setBackgroundColor(0xFF000000);
        return iView;
    }


}


