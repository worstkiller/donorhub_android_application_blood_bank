package com.vikaskumar.materialdesign.model;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.vikaskumar.materialdesign.activity.RegisterActivity;

import java.util.Calendar;

/**
 * Created by vikas kumar on 11/12/2015.
 */
public  class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        StringBuilder sb = new StringBuilder();
        sb.append(year);
        sb.append("-");
        sb.append(month);
        sb.append("-");
        sb.append(day);
        String strI = sb.toString();
        RegisterActivity.ageData=strI;

        final Calendar c = Calendar.getInstance();
        int yearCurrent = c.get(Calendar.YEAR);
        int age=yearCurrent-year;
        RegisterActivity.inputAgeText.setText(Integer.toString(age));
    }
}