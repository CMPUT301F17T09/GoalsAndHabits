package cmput301f17t09.goalsandhabits.Main_Habits;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.DatePicker;

import java.util.Date;

/**
 * Created by Ken on 13/11/2017.
 * This class represents a basic date picker. Taken from Android Documentation.
 * Currently not used by the application.
 */

public class DatePickerFrag extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    public interface DatePickerFragListener{
        public void onDatePicked(DialogFragment dialog, Date date);
    }

    DatePickerFragListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //https://stackoverflow.com/questions/32083053/android-fragment-onattach-deprecated
        //Nov 7
        Activity activity=null;

        if (context instanceof Activity){
            activity =(Activity) context;
        }
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DatePickerFragListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DatePickerFragListener");
        }
    }

    /**
     * Instantiates a new date picker dialog fragment.
     * @param savedInstanceState
     * @return DatePickerDialog Fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
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
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        Date newdate = c.getTime();
        mListener.onDatePicked(DatePickerFrag.this, newdate);
    }
}
