package com.skoovy.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.AdapterView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Rudi Wever on 3/26/2017.
 */

public class ProfileImageAdapter extends BaseAdapter {
    private Context mContext;
    int counter = 0;
    ArrayList filenames;
    //Constructor
    public ProfileImageAdapter (Context context, ArrayList  names) {
        mContext = context;
        filenames=names;
        //Log.d("User", "filenames: " + names.size());
        Log.d("User", "HERE -->> ProfileImageAdapter");
        Log.d("User", "ProfileImageAdapter -->>"+ names.size());

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
        int halfWidth = ((parent.getWidth()/2));
        int halfHeight = ((parent.getHeight()/2));
//        Log.d("User", "position:"+position);
//        Log.d("User", "halfWidth:"+halfWidth);
//        Log.d("User", "halfHeight:"+halfHeight);
        if (convertView == null) {
//            Log.d("User", "convertView == null");
            counter++;
            imageView = new ImageView(mContext);
            imageView.setId(counter);
            imageView.setLayoutParams(new GridView.LayoutParams(halfWidth, halfHeight));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else
        {
            imageView = (ImageView) convertView;
            imageView.setLayoutParams(new GridView.LayoutParams(halfWidth, halfHeight));  //Over-rides any un-correct settings.
        }
//        Uri imgURI = Uri.parse((String) filenames.get(position));
//        imageView.setImageURI((Uri) filenames.get(position));
       // imageView.setImageResource((Integer) filenames.get(position));
//imageView = (ImageView) filenames.get(position);
        Log.d("User", "filenames:"+filenames);
        Glide.with(mContext).load(filenames.get(position)).into(imageView);
        return imageView;
    }


    // Keep all Images in array
/*    public Integer[] mProfilePics = {
            R.drawable.no_image_placeholder, R.drawable.no_image_placeholder,
            R.drawable.no_image_placeholder, R.drawable.no_image_placeholder,
            R.drawable.no_image_placeholder, R.drawable.no_image_placeholder
    };*/



}
