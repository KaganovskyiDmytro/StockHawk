package com.sam_chordas.android.stockhawk.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by dmytrokaganovskyi on 8/21/16.
 */
public class DatePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.enter_start_date)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}
