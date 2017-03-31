package com.skoovy.android;

/**
 * Author:  Rudi Wever, rwever@asu.edu
 * Date:    3/26/2017
 */

import java.io.File;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * UserProfile
 * Creates the UserProfile page of the app.  UserProfile page is also made up of Profile_Content_Images &
 * AvatarFragment.java fragments.
 */
public class UserProfile extends AppCompatActivity implements AvatarFragment.OnFragmentInterationListener {

    //DECLARATIONS
    //GUI
    ImageButton cameraIconButton;
    ImageButton mapMarkerIconButton;
    ImageButton profileAvatarButton;
    private TextView usersPosts;
    private TextView usersPoints;
    private TextView usersFollowers;
    private TextView usersFollowing;

    //vars
    private int skoovyUserPoints;
    private int pointsAtGivenUser;
    private String skoovyUserName;
    private String skoovyUID;
    private String skoovyAvatar;
    private ArrayList imageArrayList = new ArrayList();
    SkoovyUser skoovyuser = new SkoovyUser();
    private int maxCount = 6; //max number of imageViews for gridview

    //refs
    private DatabaseReference fbDatabase;
    FirebaseDatabase skoovyDatabase = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        //CODE TO MAKE NEW NODE IN FIREBASE DB!!!
        //(not needed for activity, but needed until nodes are created for db access)
        DatabaseReference fbdbRef = skoovyDatabase.getReference().child("Following").child("foo");
        Map<String, Object> childUpdates = new HashMap<String, Object>();
        childUpdates.put("0", "Farmer Brown");
        fbdbRef.updateChildren(childUpdates);

*/

/*
        //CODE TO ADD NEW FOLLOWER TO A SPECIFIC USER (in this example user=rewever, .put are the followers of rwever)
        DatabaseReference fbdbRef = skoovyDatabase.getReference().child("Following").child("rwever");
        Map<String, Object> childUpdates = new HashMap<String, Object>();
        childUpdates.put("0", "kowal");
        childUpdates.put("1", "lilyfair");
        childUpdates.put("2", "super_blah12");
        childUpdates.put("3", "rick8");
        childUpdates.put("4", "Samuel L. Jackson");
        childUpdates.put("5", "Gabriel Macht");
        childUpdates.put("6", "Steve Carell");
        childUpdates.put("7", "Ben Stiller");
        childUpdates.put("8", "Brittany Snow");
        childUpdates.put("9", "Michelle Monaghan");
        childUpdates.put("10", "Chris Pratt");
        childUpdates.put("11", "Vanessa Hudgens");
        fbdbRef.updateChildren(childUpdates);
*/


        //WE NEED user OBJECT HERE
        Intent intent3 = getIntent();
        User user = (User) intent3.getSerializableExtra("User");
        //Since the user is authenticated, we also need their profile stats
        skoovyAvatar = user.getAvatar();
        skoovyUserName = user.getUsername();
        skoovyUserPoints = user.getPoints();
        skoovyUID = user.getUid();
        Log.d("User", "skoovyUserPoints" + skoovyUserPoints);

/*

        DatabaseReference fbdbRef = skoovyDatabase.getReference().child("userInfo").child(skoovyUID);
        Map<String, Object> childUpdates = new HashMap<String, Object>();
        childUpdates.put("points", 5900);
        fbdbRef.updateChildren(childUpdates);

*/


        //Create Firebase storage reference
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


//============>>> CURRENTLY ONLY ONE IMAGE IS BEING FETCHED.
//                    NEED TO CONSIDER HOW TO FETCH MULTIPLE IMAGES.
//                      PROBABLY IMPLEMENT WITH A WHILE LOOP THAT FETCHES UNTIL NULL VALUE
//                          COMES BACK FROM STORAGE OR UNTIL MAXCOUNT IS REACHED.
//
//                 QUESTION:  WHAT HAPPENS WHEN NON-JPG IS FETCHED?
//
//                 QUESTION:  INSTEAD OF FETCHING AND HOLDING THE RESOURCE IN DEVICE MEMORY,
//                            CAN WE JUST LOAD IMAGE DIRECTLY TO IMAGE VIEW (RAM)?

        //Create pointer to file on cloud storage
        StorageReference sampleRef = storageRef.child("photos/sample_image-min.jpg");


        //Create localFile here ↴
        final File localFile = new File(getFilesDir(), "image1.jpg");
        sampleRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                Log.d("User", "LOCALFILE CREATED.  FILENAME: " + localFile.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        imageArrayList.add(localFile);


        setContentView(R.layout.activity_user_profile);
        //Create GUI references
        cameraIconButton = (ImageButton) findViewById(R.id.cameraIconButton);
        mapMarkerIconButton = (ImageButton) findViewById(R.id.mapMarkerIconButton);
        profileAvatarButton = (ImageButton) findViewById(R.id.profile_avatar);
        usersPosts = (TextView) findViewById(R.id.skoovyUsersPosts);
        usersPoints = (TextView) findViewById(R.id.skoovyUsersPoints);
        usersFollowers = (TextView) findViewById(R.id.skoovyUsersFollowers);
        usersFollowing = (TextView) findViewById(R.id.skoovyUsersFollowing);

        //set user profile dashboard stats
        setSkoovyUsersFollower();
        setSkoovyUsersFollowing();
        setSkoovyUsersAvatar();
        setSkoovyUsersPoints();

        //Create fragment for popup avatar selection
        final android.app.FragmentManager fragmentManager = getFragmentManager();
        final AvatarFragment avatarFragment = new AvatarFragment();


        //THIS FILLING IN OF EMPTY ELEMENTS MIGHT NOT BE NEEDED..............
        //Fill in any empty elements in imageArrayList with placehoder
        Log.d("User", "imageArrayList: " + imageArrayList.size());
        //if the imageArrayList only has 1 image then we want to show at full size
        //however if the imageArrayList has more than 2 or more, but less than maxCount,
        //then we want to fill in the rest of the elements with a blank placeholder.
        if (imageArrayList.size() != 1) {
            if (imageArrayList.size() < maxCount) {
                int missingElements = maxCount - imageArrayList.size();
                for (int i = 0; i < missingElements; i++) {
                    imageArrayList.add(R.drawable.no_image_placeholder);
                }
            }
        }


        //Create GridView container for ImageViews being populated by ProfileImageAdapter.java
        //Image sources are designated in ProfileImageAdapter.java
        GridView gridview = (GridView) findViewById(R.id.profile_images_table);
        if (imageArrayList.size() == 1) {
            gridview.setNumColumns(1);
        }
        gridview.setAdapter(new ProfileImageAdapter(this, imageArrayList));
        //if you want to hava a border around the gridview, use this...
        //gridview.setPadding(1, 1, 1, 1);


        //AVATAR CLICKED, NOW OPEN AVATAR SELECTION FRAGMENT
        profileAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatarFragment.show(fragmentManager, "Avatar Fragment");
            }
        });

        //Tell my buttons to listen up!
        addListenerOnButton();

    } //END OF onCreate


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

    private void setSkoovyUsersAvatar() {
        if (skoovyAvatar == null) {
            //user still has not set an avatar
            return;
        }
        //Show user's avatar for start of this activity
        TypedArray imgs = getResources().obtainTypedArray(R.array.avatar_imgs);
        profileAvatarButton = (ImageButton) findViewById(R.id.profile_avatar);
        int imgID = Integer.parseInt(skoovyAvatar);
        profileAvatarButton.setImageResource(imgs.getResourceId(imgID, -1));
        //garbage collection: return TypedArray object to cache
        imgs.recycle();
    }


    private void setSkoovyUsersPoints() {
        // Get a reference to our userInfo node
        final DatabaseReference currentSkoovyUsersPointsReference = skoovyDatabase.getReference("userInfo");
        currentSkoovyUsersPointsReference.orderByKey().addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(skoovyUID)) {
                    currentSkoovyUsersPointsReference.child(skoovyUID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                pointsAtGivenUser = (int) (long) dataSnapshot.child("points").getValue();

                                //NUMBER OF POINTS HAS BEEN FETCHED
                                //now update user profile dashboard
                                StringBuilder pointValue = new StringBuilder(String.valueOf(pointsAtGivenUser));
                                if (pointsAtGivenUser > 9999) {
////            usersPosts.setTextSize(14);
////            usersPoints.setTextSize(14);
////            usersFollowers.setTextSize(14);
////            usersFollowing.setTextSize(14);
////            usersPoints.setText("9,999+");
                                    usersPoints.setText("10k+");
                                    return;
                                }
                                if (pointsAtGivenUser > 999) {
                                    pointValue.insert(1, ",");
                                    usersPoints.setText(pointValue);
                                    return;
                                }
                                usersPoints.setText(pointValue);
                            }
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


    private void setSkoovyUsersFollower() {
        // Get a reference to our Followers node
        final DatabaseReference currentSkoovyUsersFollowersReference = skoovyDatabase.getReference("Followers");
        currentSkoovyUsersFollowersReference.orderByKey().addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(skoovyUserName)) {
                    currentSkoovyUsersFollowersReference.child(skoovyUserName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //NUMBER OF FOLLOWERS HAS BEEN FETCHED AND SET IN THE SKOOVYUSER CLASS
                            //now update user profile dashboard
                            usersFollowers.setText(String.valueOf((int) dataSnapshot.getChildrenCount()));
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


    private void setSkoovyUsersFollowing() {
        // Get a reference to our Followers node
        final DatabaseReference currentSkoovyUsersFollowersReference = skoovyDatabase.getReference("Following");
        currentSkoovyUsersFollowersReference.orderByKey().addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(skoovyUserName)) {
                    currentSkoovyUsersFollowersReference.child(skoovyUserName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //NUMBER OF FOLLOWING HAS BEEN FETCHED
                            //now update user profile dashboard
                            usersFollowing.setText(String.valueOf((int) dataSnapshot.getChildrenCount()));
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
     * This is the interface to receive interaction data from the AvatarFragment
     * In this interaction we also save the users avatar choice in the Firebase DB ->> 'userInfo' node
     *
     * @param position int value of the avatar's index in the avatar_imgs array
     */
    @Override
    public void onFragmentInteraction(int position) {
//        Log.d("User", "Activity Log ->>> Item: " + position + "selected");
        TypedArray imgs = getResources().obtainTypedArray(R.array.avatar_imgs);
        //now update user profile dashboard
        profileAvatarButton.setImageResource(imgs.getResourceId(position, -1));
        //garbage collection: return TypedArray object to cache
        imgs.recycle();

        fbDatabase = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("userInfo")
                .child(skoovyUID);//pointer to user's profile data to user's userInfo node

        //update avatar data for user in Firebase DB
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("avatar", Integer.toString(position));
        fbDatabase.updateChildren(userUpdates);
    }
}
