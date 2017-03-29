package com.skoovy.android;

/**
 * Author:  Rudi Wever
 * Date:    3/27/2017
 */

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable; //import is used
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;


public class AvatarFragment extends DialogFragment{

    //DECLARATIONS
    private OnFragmentInterationListener mListener;
    Button close;
    GridView gridview;
    int[] images = {R.drawable.c1,
                    R.drawable.c2,
                    R.drawable.c3,
                    R.drawable.c4,
                    R.drawable.c5,
                    R.drawable.c6,
                    R.drawable.c7,
                    R.drawable.c8,
                    R.drawable.c9,
                    R.drawable.c10,
                    R.drawable.c11,
                    R.drawable.c12,
                    R.drawable.c13,
                    R.drawable.c14,
                    R.drawable.c15,
                    R.drawable.c16,
                    R.drawable.c17,
                    R.drawable.c18,
                    R.drawable.c19,
                    R.drawable.c20,
                    R.drawable.c21,
                    R.drawable.c22,
                    R.drawable.c23,
                    R.drawable.c24};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.avatar_list_dialog, container, false);

        //INITIALIZE LIST VIEW
        gridview = (GridView) rootview.findViewById(R.id.avatarList);
        close = (Button) rootview.findViewById(R.id.exitButton);
        //SET DIALOG TITLE
        getDialog().setTitle("Select your Skoovy Avatar");

        //CREATE ADAPTER OBJECT AND SET LISTVIEW TO IT
        AvatarListAdapter adapter = new AvatarListAdapter(getActivity(), images);

        //SET LISTVIEW TO THE ADAPTER
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //Toast.makeText(getActivity(), images[position], Toast.LENGTH_LONG).show();
                if (mListener != null){
                    //Log.d("User", "Fragment Log ->>> Item: " + position + "selected");
                    mListener.onFragmentInteraction(position);
                }
                dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();

            }
        });

        return rootview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInterationListener) {
            mListener = (OnFragmentInterationListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInterationListener");
        }
    }

    public interface OnFragmentInterationListener {
        void onFragmentInteraction(int position);
    }

}
