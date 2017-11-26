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
import java.util.Date;

import cmput301f17t09.goalsandhabits.R;

/**
 * Created by Ken on 07/11/2017.
 */

public class EditHabitDialog extends DialogFragment{

    public interface EditHabitDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog, String newreason, String newtitle);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    EditHabitDialogListener mListener;

    public static EditHabitDialog newInstance(String name, String reason) {
        EditHabitDialog frag = new EditHabitDialog();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("reason", reason);
        frag.setArguments(args);
        return frag;
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
            mListener = (EditHabitDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement EditHabitDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String name = getArguments().getString("name");
        String reason = getArguments().getString("reason");
        //String date = getArguments().getString("Start date");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View diaView = inflater.inflate(R.layout.dialog_edit_habit, null);

        final EditText reason_field = (EditText) diaView.findViewById(R.id.editDiaReason);
        reason_field.setText(reason);
        final EditText name_field = (EditText) diaView.findViewById(R.id.editDiaName);
        name_field.setText(name);

        ArrayList<CheckBox> days = new ArrayList<CheckBox>();
        days.add((CheckBox) diaView.findViewById(R.id.sundayBox));
        days.add((CheckBox) diaView.findViewById(R.id.mondayBox));
        days.add((CheckBox) diaView.findViewById(R.id.tuesdayBox));
        days.add((CheckBox) diaView.findViewById(R.id.wednesdayBox));
        days.add((CheckBox) diaView.findViewById(R.id.thursdayBox));
        days.add((CheckBox) diaView.findViewById(R.id.fridayBox));
        days.add((CheckBox) diaView.findViewById(R.id.saturdayBox));
        String schedule = "";
        for (int i=0; i<days.size(); i++){
            CheckBox b = days.get(i);
            if (b != null){
                if (b.isChecked()){
                    schedule += "1";
                }else{
                    schedule += "0";
                }
            }
        }

        builder.setTitle("Edit habit info")
                .setView(diaView)
                .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String newreason = reason_field.getText().toString();
                        String newtitle = name_field.getText().toString();
                        //TODO:
                        mListener.onDialogPositiveClick(EditHabitDialog.this, newreason, newtitle);
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
