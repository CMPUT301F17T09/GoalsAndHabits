package cmput301f17t09.goalsandhabits.Profiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cmput301f17t09.goalsandhabits.R;

/**
 * Created by chiasson on 2017-11-30.
 * This dialog prompts the user for habit type and/or comment filters for their total habit history
 *
 */
public class FilterDialog extends DialogFragment {

    public interface FilterDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog, String habitSearch, String commentSearch); //add filter parameters
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    FilterDialogListener mListener;

    public static FilterDialog newInstance() {
        FilterDialog frag = new FilterDialog();
        Bundle args = new Bundle();
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
            mListener = (FilterDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement FilterDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View diaView = inflater.inflate(R.layout.dialog_filter, null);

        final EditText habit_field = (EditText) diaView.findViewById(R.id.editHabitType);
        habit_field.setText("");
        final EditText comment_field = (EditText) diaView.findViewById(R.id.editCommentSearch);
        comment_field.setText("");
        final TextView hint = (TextView) diaView.findViewById(R.id.hint);
        hint.setText("Hint: search * to search all habits or all non-null comments");

        builder.setTitle("Filter Habit Events")
                .setView(diaView)
                .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String habitSearch = habit_field.getText().toString();
                        String commentSearch = comment_field.getText().toString();
                        mListener.onDialogPositiveClick(FilterDialog.this, habitSearch,commentSearch);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(FilterDialog.this);
                    }
                });
        return builder.create();
    }

}
