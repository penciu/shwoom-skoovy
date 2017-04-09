package com.skoovy.android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.InfoWindowAdapter {

    private enum contentType {FOOD,PLACE,EVENT};

    private GoogleMap mMap;
    private GPSTracker gps;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private LatLng MY_HOUSE = new LatLng(35.2659170,-113.9995680);
    private int IMAGE = R.drawable.user_image;


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

            }
            else {
                gps = new GPSTracker(MapsActivity.this);
                getLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLocation(){
        // check if GPS enabled
        if(!gps.canGetLocation()){
            gps.showSettingsAlert();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(this);
        getLocation();
        try {
            mMap.setMyLocationEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(gps.getLatitude(), gps.getLongitude())));
            mMap.addMarker(makeYourMark(MY_HOUSE, contentType.FOOD, IMAGE));
        } catch (SecurityException ex) {
            onMapReady(mMap);
        }
    }

    public MarkerOptions makeYourMark(LatLng place, contentType type, int image) {
        BitmapDescriptor a;
        switch (type) {
            case FOOD:
                a = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                break;
            case PLACE:
                a = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                break;
            case EVENT:
                a = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                break;
            default:
                a = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
        }
        MarkerOptions opts = new MarkerOptions()
                .position(place)
                .icon(a)
                .title("" + image);
        return opts;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return prepareInfoView(getResources().getDrawable(Integer.parseInt(marker.getTitle())));
    }

    private View prepareInfoView(Drawable image){
        /*LinearLayout infoView = new LinearLayout(MapsActivity.this);
        LinearLayout.LayoutParams infoViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        infoView.setOrientation(LinearLayout.HORIZONTAL);
        infoView.setLayoutParams(infoViewParams);*/

        ImageView infoImageView = new ImageView(MapsActivity.this);
        infoImageView.setImageDrawable(image);
        infoImageView.setMaxWidth(200);
        infoImageView.setMaxHeight(200);
        //infoView.addView(infoImageView);
        return infoImageView;
    }
}
