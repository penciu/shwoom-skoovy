package com.skoovy.android.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;

import com.skoovy.android.R;


public class SingleChoiceDialogFragment extends DialogFragment {
    private final String TAG = "AUC_COMPLEX";
    private final String[] countries = {"Afghanstan (+93)", "Albania (+355)", "Algeria (+213)", "American Samoa (+1)",
                                        "Andora (+376)", "Angola (+244)", "Anguililla (+1)", "Antarctica (+0)",
                                        "Antigua & Barbuda (+1)", "Argentina (+54)", "Armenia(+374)", "Aruba (+297)",
                                        "Ascension Island (+247)", "Australia (+61)"};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // NOTE: setMessage doesn't work here because the list takes up the content
        // area. Use the setTitle method to set a descriptive prompt
        builder.setTitle("<   Select Your Country Code");

        // The setItems function is used to create a list of content
        builder.setItems(countries, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, String.format("countries chosen: %s",countries[which]));
            }
        });

        // Single-choice dialogs don't need buttons because they
        // auto-dismiss when the user makes a choice

        return builder.create();
    };
}
