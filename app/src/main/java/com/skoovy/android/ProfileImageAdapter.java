package com.skoovy.android;

/**
 * Author:  Rudi Wever, rwever@asu.edu
 * Date:    3/26/2017
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class ProfileImageAdapter extends BaseAdapter {

    //DECLARATIONS
    //vars
    private Context mContext;
    private int counter = 0;
    private ArrayList filenames;

    //Constructor
    public ProfileImageAdapter(Context context, ArrayList names) {
        mContext = context;
        filenames = names;
        //Log.d("User", "ProfileImageAdapter -->>"+ names.size());
    }

    @Override
    public int getCount() {
        return filenames.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    // Create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        //initialize size for use in imageview params
        int halfWidth = ((parent.getWidth() / 2));
        int halfHeight = ((parent.getHeight() / 2));

        //however if one image is shown, then use entire space
        //gridview was already set to 1 column by the userprofile activity
        if (getCount() == 1) {
            halfWidth = halfWidth * 2;
            halfHeight = halfHeight * 2;
        }

        if (convertView == null) {
            counter++;
            imageView = new ImageView(mContext);
            imageView.setId(counter);
            imageView.setLayoutParams(new GridView.LayoutParams(halfWidth, halfHeight));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
            imageView.setLayoutParams(new GridView.LayoutParams(halfWidth, halfHeight));  //Over-rides any un-correct settings.

        }

        //load imageView with url
        Glide.with(mContext).load(filenames.get(position)).into(imageView);
        return imageView;
    }

}
