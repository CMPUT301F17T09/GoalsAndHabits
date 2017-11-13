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
import android.widget.CalendarView;
import android.widget.EditText;

import java.util.Date;

import cmput301f17t09.goalsandhabits.R;

/**
 * Created by Ken on 07/11/2017.
 * This class represents an interactive dialog that allows a user to change the different fields
 * of a selected habit.
 */

public class EditHabitDialog extends DialogFragment{

    /**
     * An interface that must be implemented by the activity that creates this dialog.
     * Allows the activity to handle the changes made by the dialog.
     */
    public interface EditHabitDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog, String newreason,
                                          String newtitle, Date newdate);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    EditHabitDialogListener mListener;

    /**
     * Creates a new instance of the edit habit dialog with the passed parameters.
     * @param name The name of the habit to be changed
     * @param reason The reason of the habit to be changed
     * @return A new dialog fragment
     */
    public static EditHabitDialog newInstance(String name, String reason) {
        EditHabitDialog frag = new EditHabitDialog();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("reason", reason);
        frag.setArguments(args);
        return frag;
    }

    /**
     * Ensures that the dialog was created by an activity implementing it's interface.
     * @param context Activity creating the dialog
     */
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
            mListener = (EditHabitDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NewHabitDialogListener");
        }
    }

    /**
     * Builds an alert dialog fragment
     * @param savedInstanceState
     * @return Dialog Fragment
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String name = getArguments().getString("name");
        String reason = getArguments().getString("reason");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View diaView = inflater.inflate(R.layout.dialog_edit_habit, null);

        final EditText reason_field = (EditText) diaView.findViewById(R.id.editDiaReason);
        reason_field.setText(reason);
        final EditText name_field = (EditText) diaView.findViewById(R.id.editDiaName);
        name_field.setText(name);
        final CalendarView date_select = (CalendarView) diaView.findViewById(R.id.editDiaDate);
        //TODO: set calendar to start date of habit

        builder.setTitle("Edit habit info")
                .setView(diaView)
                .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String newreason = reason_field.getText().toString();
                        String newtitle = name_field.getText().toString();
                        Date newdate = new Date(date_select.getDate());
                        mListener.onDialogPositiveClick(EditHabitDialog.this, newreason, newtitle,
                                newdate);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(EditHabitDialog.this);
                    }
                });
        return builder.create();
    }

}