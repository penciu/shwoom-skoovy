package com.skoovy.android;

/**
 * Author:  Rudi Wever
 * Date:    3/26/2017
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Dialog;
import android.content.Context;


public class UserProfile extends AppCompatActivity implements  AvatarFragment.OnFragmentInterationListener{

    //DECLARATIONS
    ImageButton cameraIconButton;
    ImageButton mapMarkerIconButton;
    ImageButton profileAvatarButton;

    private TextView usersFollowers;

    AlertDialog alertDialog;
    ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);

        final android.app.FragmentManager fragmentManager = getFragmentManager();
        final AvatarFragment avatarFragment = new AvatarFragment();

        //Create GridView container for ImageViews being populated by ProfileImageAdapter.java
        //Image sources are designated in ProfileImageAdapter.java
        GridView gridview = (GridView) findViewById(R.id.profile_images_table);
        gridview.setAdapter(new ProfileImageAdapter(this));
        gridview.setPadding(1,1,1,1);

        //Create GUI references
        cameraIconButton    = (ImageButton) findViewById(R.id.cameraIconButton);
        mapMarkerIconButton = (ImageButton) findViewById(R.id.mapMarkerIconButton);
        profileAvatarButton = (ImageButton) findViewById(R.id.profile_avatar);
        usersFollowers = (TextView) findViewById(R.id.skoovyUsersFollowers);


        Intent intent3 = getIntent();
        SkoovyUser skoovyuser = (SkoovyUser)intent3.getSerializableExtra("SkoovyUser");
        //now able to access SkoovyUser class with skoovyuser methods
        //THIS LINE SETS THE NUMBER OF FOLLOWERS ON THE PROFILE PAGE
        usersFollowers.setText(String.valueOf(skoovyuser.getSkoovyUserFollowers()));



        //AVATAR CLICKED, NOW OPEN AVATAR SELECTION FRAGMENT
        profileAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatarFragment.show(fragmentManager, "Avatar Fragment");
            }
        });


        //Tell my buttons to listen up!
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        cameraIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //declare where you intend to go
                Intent intent1 = new Intent(UserProfile.this, CameraActivity.class);
                //now make it happen
                startActivity(intent1);
            }
        });

        mapMarkerIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //declare where you intend to go
//                Intent intent2 = new Intent(UserProfile.this, SOMEMAPACTIVITY.class);
                //now make it happen
//                startActivity(intent2);
//                Log.d("User", "AVATAR"+images[position]);
            }
        });


    }


    @Override
    public void onFragmentInteraction(int position) {
        Log.d("User", "Activity Log ->>> Item: " + position + "selected");

        TypedArray imgs = getResources().obtainTypedArray(R.array.avatar_imgs);

        profileAvatarButton.setImageResource(imgs.getResourceId(position, -1));
    }
}
