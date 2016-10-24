package com.codepath.nytquest.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by rpraveen on 10/23/16.
 */
public class DatePickerFragment extends DialogFragment {

  public interface OnDateSetListener {
    public void onDateSet(DatePicker view, int year, int month, int date);
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstance) {
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    // Activity needs to implement this interface
    DatePickerDialog.OnDateSetListener listener =
      (DatePickerDialog.OnDateSetListener) getTargetFragment();

    // Create a new instance of TimePickerDialog and return it
    return new DatePickerDialog(getActivity(), listener, year, month, day);
  }
}
