package cmput301f17t09.goalsandhabits.Follow_Activity;

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

import cmput301f17t09.goalsandhabits.R;

/**
 * Created by chiasson on 2017-12-02.
 * This dialog prompts the user to search for profiles to follow
 *
 * OUSTANDING ISSUE: search only finds exact match of username
 */
public class UserSearchDialog extends DialogFragment {

    public interface UserSearchDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog, String userSearch);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    UserSearchDialogListener mListener;

    public static UserSearchDialog newInstance() {
        UserSearchDialog frag = new UserSearchDialog();
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
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (UserSearchDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement UserSearchListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View diaView = inflater.inflate(R.layout.dialog_user_search, null);

        final EditText user_field = (EditText) diaView.findViewById(R.id.editUserSearch);
        user_field.setText("");

        builder.setTitle("Search for a user")
                .setView(diaView)
                .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String userSearch = user_field.getText().toString();
                        mListener.onDialogPositiveClick(UserSearchDialog.this, userSearch);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(UserSearchDialog.this);
                    }
                });
        return builder.create();
    }

}
