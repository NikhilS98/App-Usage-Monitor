package com.android.ang.seprocessmonitor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private Calendar calendar;

    public DatePickerFragment () {
        super();
        //initialize with today's date
        calendar = Calendar.getInstance();
        DateSelected.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        DateSelected.setMonth(calendar.get(Calendar.MONTH));
        DateSelected.setYear(calendar.get(Calendar.YEAR));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, DateSelected.getYear(),
                DateSelected.getMonth(), DateSelected.getDay());
    }

    //For calling the onDismiss in MainActivity whenever the date dialog is closed
    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

    //Called whenever a date is selected in the dialog by the user
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DateSelected.setDay(dayOfMonth);
        DateSelected.setMonth(month);
        DateSelected.setYear(year);

        if (dayOfMonth == calendar.get(Calendar.DAY_OF_MONTH) && month == calendar.get(Calendar.MONTH) &&
                year == calendar.get(Calendar.YEAR))
            DateSelected.setToday(true);
        else
            DateSelected.setToday(false);
    }
}
