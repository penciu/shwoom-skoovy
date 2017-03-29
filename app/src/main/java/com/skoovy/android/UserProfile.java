package com.skoovy.android;

/**
 * Author:  Rudi Wever
 * Date:    3/26/2017
 */

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * UserProfile
 * Creates the UserProfile page of the app.  UserProfile page is also made up of Profile_Content_Images &
 * AvatarFragment.java fragments.
 */
public class UserProfile extends AppCompatActivity implements AvatarFragment.OnFragmentInterationListener {

    //DECLARATIONS
    ImageButton cameraIconButton;
    ImageButton mapMarkerIconButton;
    ImageButton profileAvatarButton;

    private TextView usersFollowers;

    private String skoovyUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fetchSkoovyUsersFollower();
        setContentView(R.layout.activity_user_profile);

        final android.app.FragmentManager fragmentManager = getFragmentManager();
        final AvatarFragment avatarFragment = new AvatarFragment();

        //Create GridView container for ImageViews being populated by ProfileImageAdapter.java
        //Image sources are designated in ProfileImageAdapter.java
        GridView gridview = (GridView) findViewById(R.id.profile_images_table);
        gridview.setAdapter(new ProfileImageAdapter(this));
        gridview.setPadding(1, 1, 1, 1);

        //Create GUI references
        cameraIconButton = (ImageButton) findViewById(R.id.cameraIconButton);
        mapMarkerIconButton = (ImageButton) findViewById(R.id.mapMarkerIconButton);
        profileAvatarButton = (ImageButton) findViewById(R.id.profile_avatar);
        usersFollowers = (TextView) findViewById(R.id.skoovyUsersFollowers);


//        Intent intent3 = getIntent();
//        SkoovyUser skoovyuser = (SkoovyUser)intent3.getSerializableExtra("SkoovyUser");
        //now able to access SkoovyUser class with skoovyuser methods
        //THIS LINE SETS THE NUMBER OF FOLLOWERS ON THE PROFILE PAGE


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

    /**
     * addListenerOnButton
     * Container for OnClickListeners
     */
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

    private void fetchSkoovyUsersFollower() {
        //WE NEED user OBJECT HERE
        Intent intent3 = getIntent();
        User user = (User) intent3.getSerializableExtra("User");
        //Since the user is authenticated, we also need their profile stats
        skoovyUserName = user.getUsername();
        //Create new SkoovyUser to hold profile stats
        SkoovyUser skoovyuser = new SkoovyUser();

        FirebaseDatabase skoovyDatabase = FirebaseDatabase.getInstance();
        // Get a reference to our Followers node
        final DatabaseReference currentSkoovyUsersFollowersReference = skoovyDatabase.getReference("Followers");
        currentSkoovyUsersFollowersReference.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(skoovyUserName)) {
                    Log.d("User", "found a follower(s) for you");
                    currentSkoovyUsersFollowersReference.child(skoovyUserName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int followers = 0;
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                Log.d("User", "FOLLOWER");
                                followers++;
                            }
                            Log.d("User", "followers:" + followers);
                            SkoovyUser skoovyuser = new SkoovyUser();
                            skoovyuser.setSkoovyUserFollowers(followers);
                            //NUMBER OF FOLLOWERS HAS BEEN FETCHED AND SET IN THE SKOOVYUSER CLASS
                            usersFollowers.setText(String.valueOf(skoovyuser.getSkoovyUserFollowers()));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * onFragmentInteraction
     * This is the interface to receive data from the AvatarFragment
     *
     * @param position int value of the avatar's index in the avatar_imgs array
     */
    @Override
    public void onFragmentInteraction(int position) {
        //Log.d("User", "Activity Log ->>> Item: " + position + "selected");
        TypedArray imgs = getResources().obtainTypedArray(R.array.avatar_imgs);
        profileAvatarButton.setImageResource(imgs.getResourceId(position, -1));
    }
}
