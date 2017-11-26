package cmput301f17t09.goalsandhabits.Main_Habits;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;


import java.util.Calendar;
import java.util.Date;

import cmput301f17t09.goalsandhabits.R;

/**
 * Created by Simone on 2017/11/20.
 */

public class EditHabitEventDialog extends DialogFragment {


    public interface EditHabitEventDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog, String s, String newComment, Date newDate);
        public void onDialogNegativeƒClick(DialogFragment dialog);
    }
    EditHabitEventDialog.EditHabitEventDialogListener mListener;

    public static EditHabitEventDialog newInstance(String comment, String photoPath ) {
        EditHabitEventDialog dialog= new EditHabitEventDialog();
        Bundle args = new Bundle();
        args.putString("Comments", comment);
        args.putString("photoPath", photoPath);
        dialog.setArguments(args);
        return dialog;
    }
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
            mListener = (EditHabitEventDialog.EditHabitEventDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NewHabitEventDialogListener");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String comment = getArguments().getString("Comments");
        String photoPath = getArguments().getString("Photo Path");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View diaView = inflater.inflate(R.layout.dialog_edit_habit_events, null);

        final EditText comment_field = (EditText) diaView.findViewById(R.id.editComment);
        comment_field.setText(comment);
        final EditText photo_field = (EditText) diaView.findViewById(R.id.editDiaName);
        photo_field.setText(photoPath);
        final DatePicker datePicker = (DatePicker)diaView.findViewById(R.id.datePicker);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);

        builder.setTitle("Edit habit event info")
                .setView(diaView)
                .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String newComment= comment_field.getText().toString();
                        String newPhoto = photo_field.getText().toString();
                        Date newDate = calendar.getTime();
                        mListener.onDialogPositiveClick(EditHabitEventDialog.this, newComment,newPhoto, newDate);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeƒClick(EditHabitEventDialog.this);
                    }
                });
        return builder.create();
}
}
