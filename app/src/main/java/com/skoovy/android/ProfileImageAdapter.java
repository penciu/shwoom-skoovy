package com.skoovy.android;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Rudi Wever on 3/26/2017.
 */

public class ProfileImageAdapter extends BaseAdapter {
    private Context mContext;

    //Constructor
    public ProfileImageAdapter (Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mProfilePics.length;
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
        int halfWidth = ((parent.getWidth()/2));
        int halfHeight = ((parent.getHeight()/2));
//        Log.d("User", "position:"+position);
//        Log.d("User", "halfWidth:"+halfWidth);
//        Log.d("User", "halfHeight:"+halfHeight);
        if (convertView == null) {
//            Log.d("User", "convertView == null");
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(halfWidth, halfHeight));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else
        {
            imageView = (ImageView) convertView;
            imageView.setLayoutParams(new GridView.LayoutParams(halfWidth, halfHeight));  //Over-rides any un-correct settings.
        }
        imageView.setImageResource(mProfilePics[position]);

        return imageView;
    }


    // Keep all Images in array
    public Integer[] mProfilePics = {
            R.drawable.no_image_placeholder, R.drawable.no_image_placeholder,
            R.drawable.no_image_placeholder, R.drawable.no_image_placeholder,
            R.drawable.no_image_placeholder, R.drawable.no_image_placeholder
    };
}
