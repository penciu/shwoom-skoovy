package com.skoovy.android;

/**
 * Author:  Rudi Wever
 * Date:    3/26/2017
 */
import java.io.File;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.io.File;
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
    ImageButton cameraIconButton;
    ImageButton mapMarkerIconButton;
    ImageButton profileAvatarButton;

    private TextView usersFollowers;

    private String skoovyUserName;
    private String skoovyUID;
    private String skoovyAvatar;

    private DatabaseReference fbDatabase;
    FirebaseDatabase skoovyDatabase = FirebaseDatabase.getInstance();

    private ArrayList imageArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //WE NEED user OBJECT HERE
        Intent intent3 = getIntent();
        User user = (User) intent3.getSerializableExtra("User");
        //Since the user is authenticated, we also need their profile stats
        skoovyAvatar = user.getAvatar();
        skoovyUserName = user.getUsername();
        skoovyUID = user.getUid();
//        Log.d("User", "AVATAR FETCHED: " + skoovyAvatar);
//        Log.d("User", "USERNAME FETCHED: " + skoovyUserName);
        //FETCH DASHBOARD DATA FOR THIS SKOOVY USER
        //fetchSkoovyUsersAvatar();
//        fetchSkoovyUsersFollower();
        imageArrayList = new ArrayList();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference sampleRef = storageRef.child("photos/sample_image-min.jpg");


//        File localFile = createTempFile("images", "jpg");
        final File localFile = new File(getFilesDir(), "image1.jpg");
        sampleRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                Log.d("User", "localFile created");
                Log.d("User", "localFilename: " + localFile.toString());
//                addToImageArrayList (localFile.toString());
//                addToImageArrayList (localFile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        imageArrayList.add(localFile);
//        StorageReference sampleRef = storage.getReferenceFromUrl("gs://skoovy-b4e40.appspot.com/photos").child("sample_image-min.jpg");

/*        final long ONE_MEGABYTE = 1024 * 1024;
        sampleRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Log.d("User", "BITMAP: " + bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });*/

        setContentView(R.layout.activity_user_profile);
        fetchSkoovyUsersFollower();
        fetchSkoovyUsersAvatar();

        final android.app.FragmentManager fragmentManager = getFragmentManager();
        final AvatarFragment avatarFragment = new AvatarFragment();

/*        Integer[] mProfilePics = {
                R.drawable.no_image_placeholder, R.drawable.no_image_placeholder,
                R.drawable.no_image_placeholder, null //R.drawable.no_image_placeholder,
                //R.drawable.no_image_placeholder, R.drawable.no_image_placeholder
        };
        // Step 2
        int count = 0;
        for (Integer i : mProfilePics) {
            if (i != null) {
                count++;
            }
        }

        // Step 3
        Integer[] newArray = new Integer[count];

        // Step 4
        int index = 0;
        for (Integer i : mProfilePics) {
            if (i != null) {
                newArray[index++] = i;
            }
        }*/
        //Create GridView container for ImageViews being populated by ProfileImageAdapter.java
        //Image sources are designated in ProfileImageAdapter.java
        GridView gridview = (GridView) findViewById(R.id.profile_images_table);
        Log.d("User", "imageArrayList: " + imageArrayList.size());
        if (imageArrayList.size() < 6){
            int missingElements = 6 - imageArrayList.size();
            for (int i = 0; i < missingElements; i++){
                imageArrayList.add(R.drawable.no_image_placeholder);
            }
        }
        gridview.setAdapter(new ProfileImageAdapter(this, imageArrayList));
        gridview.setPadding(1, 1, 1, 1);

        //Create GUI references
        cameraIconButton = (ImageButton) findViewById(R.id.cameraIconButton);
        mapMarkerIconButton = (ImageButton) findViewById(R.id.mapMarkerIconButton);
        profileAvatarButton = (ImageButton) findViewById(R.id.profile_avatar);
        usersFollowers = (TextView) findViewById(R.id.skoovyUsersFollowers);

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

//    public void addToImageArrayList (String file) {
    public void addToImageArrayList (File file) {
/*
        File imgFile = new File(file);

        if (imgFile.exists()) {
            Log.d("User", "AbsolutePath: " + imgFile.getAbsolutePath());
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.imageViewTEST);

//            myImage.setImageBitmap(myBitmap);
*/

            imageArrayList.add(file);


       // }
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

    private void fetchSkoovyUsersAvatar() {
        if (skoovyAvatar == null) {
            //user still has not set an avatar
            return;
        }
        //Show user's avatar for start of this activity
        TypedArray imgs = getResources().obtainTypedArray(R.array.avatar_imgs);
        profileAvatarButton = (ImageButton) findViewById(R.id.profile_avatar);
        int imgID = Integer.parseInt(skoovyAvatar);
        profileAvatarButton.setImageResource(imgs.getResourceId(imgID, -1));
    }

    private void fetchSkoovyUsersFollower() {
        // Get a reference to our Followers node
        final DatabaseReference currentSkoovyUsersFollowersReference = skoovyDatabase.getReference("Followers");
        currentSkoovyUsersFollowersReference.orderByKey().addChildEventListener(new ChildEventListener() {
            //Create new SkoovyUser to hold profile stats
            SkoovyUser skoovyuser = new SkoovyUser();

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(skoovyUserName)) {
//                    Log.d("User", "found a follower(s) for you");
                    currentSkoovyUsersFollowersReference.child(skoovyUserName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int followers = 0;
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
//                                Log.d("User", "FOLLOWER");
                                followers++;
                            }
//                            Log.d("User", "followers:" + followers);
                            skoovyuser.setSkoovyUserFollowers(followers);
                            //NUMBER OF FOLLOWERS HAS BEEN FETCHED AND SET IN THE SKOOVYUSER CLASS
                            //now update user profile dashboard
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

        fbDatabase = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("userInfo")
                .child(skoovyUID);//to send user's profile data to user's userInfo node

        //update avatar data for user
        Map<String, Object> userUpdates = new HashMap<String, Object>();
        userUpdates.put("avatar", Integer.toString(position));
        fbDatabase.updateChildren(userUpdates);
    }
}
