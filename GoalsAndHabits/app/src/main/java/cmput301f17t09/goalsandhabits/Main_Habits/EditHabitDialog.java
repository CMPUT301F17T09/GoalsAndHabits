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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import cmput301f17t09.goalsandhabits.R;

/**
 * Created by Ken on 07/11/2017.
 * Creates a dialog to be used when editing habits
 */
public class EditHabitDialog extends DialogFragment{

    /**
     * Allows passing of dialog results back to original caller
     */
    public interface EditHabitDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog, String newreason, String newtitle, HashSet<Integer> schedule);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    EditHabitDialogListener mListener;

    /**
     * Creates a new instance of the Edit Habit Dialog with the given parameters
     * @param name Habit Name
     * @param reason Habit Reason
     * @param schedule Habit Schedule
     * @return
     */
    public static EditHabitDialog newInstance(String name, String reason, HashSet<Integer> schedule) {
        EditHabitDialog frag = new EditHabitDialog();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("reason", reason);
        args.putSerializable("schedule",schedule);
        frag.setArguments(args);
        return frag;
    }

    /**
     * Verifies that the host activity implements the callback interface
     * @param context Host Activity
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
                    + " must implement EditHabitDialogListener");
        }
    }

    /**
     * Called on creation of dialog. Creates layout and functionality for dialog window.
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String name = getArguments().getString("name");
        String reason = getArguments().getString("reason");
        HashSet<Integer> schedule = (HashSet<Integer>) getArguments().getSerializable("schedule");
        //String date = getArguments().getString("Start date");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View diaView = inflater.inflate(R.layout.dialog_edit_habit, null);

        final EditText reason_field = (EditText) diaView.findViewById(R.id.editDiaReason);
        reason_field.setText(reason);
        final EditText name_field = (EditText) diaView.findViewById(R.id.editDiaName);
        name_field.setText(name);

        final ArrayList<CheckBox> days = new ArrayList<CheckBox>();
        days.add((CheckBox) diaView.findViewById(R.id.sundayBox));
        days.add((CheckBox) diaView.findViewById(R.id.mondayBox));
        days.add((CheckBox) diaView.findViewById(R.id.tuesdayBox));
        days.add((CheckBox) diaView.findViewById(R.id.wednesdayBox));
        days.add((CheckBox) diaView.findViewById(R.id.thursdayBox));
        days.add((CheckBox) diaView.findViewById(R.id.fridayBox));
        days.add((CheckBox) diaView.findViewById(R.id.saturdayBox));
        for (int i = 0; i<days.size(); i++){
            if (schedule.contains(i+1)){
                days.get(i).setChecked(true);
            }
        }

        builder.setTitle("Edit habit info")
                .setView(diaView)
                .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String newreason = reason_field.getText().toString();
                        String newtitle = name_field.getText().toString();
                        HashSet<Integer> newschedule = new HashSet<>();
                        for (int i=0;i<7;i++){
                            if (days.get(i).isChecked()){
                                newschedule.add(i+1);
                            }
                        }
                        mListener.onDialogPositiveClick(EditHabitDialog.this, newreason, newtitle, newschedule);
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
