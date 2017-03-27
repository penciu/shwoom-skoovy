package com.skoovy.android;

/**
 * Author:  Rudi Wever
 * Date:    3/26/2017
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

public class UserProfile extends AppCompatActivity {

    private ImageButton cameraIconButton;
    private ImageButton mapMarkerIconButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Create GridView container for ImageViews being populated by ProfileImageAdapter.java
        //Image sources are designated in ProfileImageAdapter.java
        GridView gridview = (GridView) findViewById(R.id.profile_images_table);
        gridview.setAdapter(new ProfileImageAdapter(this));

        //Create GUI references
        cameraIconButton    = (ImageButton) findViewById(R.id.cameraIconButton);
        mapMarkerIconButton = (ImageButton) findViewById(R.id.mapMarkerIconButton);


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
            }
        });
    }
}
