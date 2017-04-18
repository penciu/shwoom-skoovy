package com.skoovy.android;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

/**
 * Created by SuperBlah12 on 4/4/2017.
 */

public class SkoovyRequest {

    private String requestID;
    private String userID;
    private double latitude;
    private double longitude;
    private String requestText;

    public String getRequestText() {
        return requestText;
    }

    public void setRequestText(String requestText) {
        this.requestText = requestText;
    }

    //Emtpty Constructor for Firebase stuff.
    SkoovyRequest() {
    }

    //Latitude given.
    SkoovyRequest(String userID, String requestText, double latitude, double longitude) {
        generateRequestID();
        this.userID = userID;
        this.requestText = requestText;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRequestID() {
        return requestID;
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

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public MarkerOptions generateMarkerOptions() {
        MarkerOptions opts = new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title("Request")
                .snippet(requestText);
        return opts;
    }

    private void generateRequestID() {
        requestID = UUID.randomUUID().toString();
        requestID = requestID.replace("-", "");
    }

    public void sendToDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("request/" + requestID).setValue(this);
    }
}