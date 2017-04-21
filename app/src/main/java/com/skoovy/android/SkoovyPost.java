package com.skoovy.android;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.UUID;

/**
 * Created by SuperBlah12 on 4/4/2017.
 */

public class SkoovyPost {

    public enum Type {FOOD, PLACE, EVENT}

    ;
    private Type postType;
    private String imagePath = "";
    private String imageID = "";
    private String postID;
    private String userID;
    private double latitude;
    private double longitude;

    //Emtpty Constructor for Firebase stuff.
    SkoovyPost() {
    }

    //Latitude given.
    SkoovyPost(Type postType, String imageID, String userID, double latitude, double longitude) {
        generatePostID();
        this.postType = postType;
        this.imagePath = "photos/" + imageID + ".jpg";
        this.imageID = imageID;
        this.userID = userID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
        this.imagePath = "photos/" + imageID + ".jpg";
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Type getPostType() {
        return postType;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getPostID() {
        return postID;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public LatLng getLocation() {
        return new LatLng(latitude, longitude);
    }

    public void setPostType(Type postType) {
        this.postType = postType;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public MarkerOptions generateMarkerOptions() {
        BitmapDescriptor a = BitmapDescriptorFactory.defaultMarker();
        switch (postType) {
            case FOOD: {
                a = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                break;
            }
            case PLACE: {
                a = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                break;
            }
            case EVENT: {
                a = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                break;
            }
            default: {
                a = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                break;
            }
        }
        MarkerOptions opts = new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(a)
                .title("" + imageID + ".jpg");
        return opts;
    }

    private void generatePostID() {
        postID = UUID.randomUUID().toString();
        postID = postID.replace("-", "");
    }

    public void sendToDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("posts/" + postID).setValue(this);
    }
}
