package com.skoovy.android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.InfoWindowAdapter {

    private GoogleMap mMap;
    private GPSTracker gps;
    private ImageButton button1;
    private ImageButton button2;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);
                gps = new GPSTracker(MapsActivity.this);
            }
            else {
                gps = new GPSTracker(MapsActivity.this);
                getLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getFiles();
    }

    /**
     * addListenerOnButton
     * Listens to the buttons of this activity
     */
    public void addListenerOnButton()
    {
       //button1 is the PROFILE button on this activity
        button1 = ((ImageButton)findViewById(R.id.profileIconButton));
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //declare where you intend to go
                Intent intent = new Intent(MapsActivity.this, UserProfile.class);
                //now make it happen
                startActivity(intent);
            }
        });

        //button2 is the CAMERA button
        button2 = ((ImageButton)findViewById(R.id.cameraIconButton));
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //declare where you intend to go
                Intent intent = new Intent(MapsActivity.this, CameraActivity.class);
                //now make it happen
                startActivity(intent);
            }
        });
    }

    private void getLocation(){
        // check if GPS enabled
        if(!gps.canGetLocation()){
            gps.showSettingsAlert();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(this);
        getLocation();
        try {
            mMap.setMyLocationEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(gps.getLatitude(), gps.getLongitude())));
            // Read from the database.
            database.getReference("posts").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dS, String s) {
                    SkoovyPost post = dS.getValue(SkoovyPost.class);
                    mMap.addMarker(post.generateMarkerOptions());
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

            database.getReference("request").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dS, String s) {
                    SkoovyRequest request = dS.getValue(SkoovyRequest.class);
                    mMap.addMarker(request.generateMarkerOptions());
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        } catch (SecurityException ex) {
            onMapReady(mMap);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        if(marker.getSnippet() == null) {
            String path = getExternalCacheDir().getPath() + "/" + marker.getTitle();
            if (fileExists(path))
                return prepareInfoView(Drawable.createFromPath(path));
            else{
                getFiles();
                return getInfoContents(marker);
            }
        }
        else {
            //test
            Runnable run = new Runnable() {
                @Override
                public void run(){
                    //declare where you intend to go
                    Intent intent1 = new Intent(MapsActivity.this, CameraActivity.class);
                    //now make it happen
                    startActivity(intent1);
                }
            };
            Handler hand = new Handler();
            hand.postDelayed(run, 2000);
            return null;
        }
    }

    private View prepareInfoView(Drawable image){
        ImageView infoImageView = new ImageView(MapsActivity.this);
        infoImageView.setImageDrawable(image);
        infoImageView.setMinimumWidth(1000);
        infoImageView.setMinimumHeight(1000);
        return infoImageView;
    }

    boolean fileExists(String path){
        File file = new File(path);
        if(file.exists())
            return true;
        else
            return false;
    }

    public void getFiles() {
        //Create Firebase storage reference
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        DatabaseReference dr = database.getReference("posts");
        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dS, String s) {
                SkoovyPost post = dS.getValue(SkoovyPost.class);
                //Create pointer to file on cloud storage
                StorageReference sampleRef = storageRef.child(post.getImagePath());
                //Create localFile here ↴
                final File localFile = new File(getExternalCacheDir(), post.getImageID() + ".jpg");
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
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
