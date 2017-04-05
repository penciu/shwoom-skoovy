package com.skoovy.android;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.UUID;

/**
 * Created by SuperBlah12 on 4/4/2017.
 */

public class SkoovyPost {

    public enum Type {FOOD, PLACE, EVENT};
    private Type postType;
    private String imagePath;
    private String postID;
    private String ownerUID;
    private boolean visibility;
    private List<String> viewers;
    private double latitude;
    private double longitude;
    private LatLng location;

    //Emtpty Constructor for Firebase stuff.
    SkoovyPost(){}

    //Latitude given.
    SkoovyPost(Type postType, String image, String ownerUID, boolean visibility, double latitude, double longitude) {
        generatePostID();
        this.postType = postType;
        this.imagePath = image;
        this.ownerUID = ownerUID;
        this.visibility = visibility;
        this.latitude = latitude;
        this.longitude = longitude;
        recreateLocation();
    }

    SkoovyPost(Type postType, String image, String ownerUID, boolean visibility, LatLng location){
        generatePostID();
        this.postType = postType;
        this.imagePath = image;
        this.ownerUID = ownerUID;
        this.visibility = visibility;
        this.location = location;
        latitude = location.latitude;
        longitude = location.longitude;
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
        if(location != null)
            return location;
        else {
            location = new LatLng(latitude,longitude);
            return location;
        }
    }

    public void setPostType(Type postType) {
        this.postType = postType;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
        recreateLocation();
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        recreateLocation();
    }

    public void setLocation(LatLng location) {
        this.location = location;
        this.latitude = location.latitude;
        this.longitude = location.longitude;
    }

    public MarkerOptions generateMarkerOptions() {
        BitmapDescriptor a = BitmapDescriptorFactory.defaultMarker();
        switch(postType){
            case FOOD:{
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
                .position(new LatLng(latitude,longitude))
                .icon(a)
                .title("" + imagePath);
        return opts;
    }

    private void generatePostID(){
        postID = UUID.randomUUID().toString();
        postID = postID.replace("-","");
    }

    private void recreateLocation(){
        location = new LatLng(latitude,longitude);
    }
}
