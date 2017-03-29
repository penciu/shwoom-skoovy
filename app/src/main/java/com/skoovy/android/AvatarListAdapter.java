package com.skoovy.android;

/**
 * Author:  Rudi Wever
 * Date:    3/27/2017
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class AvatarListAdapter extends BaseAdapter {

    //DECLARATIONS
    Context context;
    private int [] images;

    public AvatarListAdapter(Context context, int[]images){
        this.context=context;
        this.images=images;
    }


    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.avatar_rowmodel, null);
        }
        //GET VIEW
        ImageView img = (ImageView) convertView.findViewById(R.id.profile_avatar);

        //ASSIGN DATA
        img.setImageResource(images[position]);

        return convertView;
    }
}
