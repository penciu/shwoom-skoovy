package com.skoovy.android.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.skoovy.android.R;

import static android.R.id.content;

/**
 * Created by Keenan on 4/5/2017.
 */

public class EmailGateDialog extends DialogFragment {
    LayoutInflater inflater;
    View view;
    TextView textView;
    ImageView mImageViewFillingCheckmark;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.email_gate, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        textView = (TextView) view.findViewById(R.id.textView19);
        mImageViewFillingCheckmark = (ImageView) view.findViewById(R.id.checkbox);
       /* builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/

        return builder.create();

    }

    public void updateTextView(String text) {
        textView.setText(text);
    }

    public void animateImageView() {
        ((AnimationDrawable) mImageViewFillingCheckmark.getBackground()).start();
    }
}
