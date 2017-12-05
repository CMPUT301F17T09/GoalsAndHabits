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

import cmput301f17t09.goalsandhabits.R;

/**
 * Created by chiasson on 2017-12-03.
 * This dialog asks the user if they wish to allow the selected potential follower to follow them
 */
public class AcceptFollowerDialog extends DialogFragment {

    public interface AcceptFollowerDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog, String request);
        public void onDialogNegativeClick(DialogFragment dialog, String request);
    }

    AcceptFollowerDialogListener mListener;

    public static AcceptFollowerDialog newInstance(Profile follower) {
        AcceptFollowerDialog frag = new AcceptFollowerDialog();
        Bundle args = new Bundle();
        args.putString("request",follower.getUsername());
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
            mListener = (AcceptFollowerDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AcceptFollowerListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View diaView = inflater.inflate(R.layout.dialog_accept_follower, null);
        final String request = getArguments().getString("request");

        builder.setTitle("Accept follow request?")
                .setView(diaView)
                .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(AcceptFollowerDialog.this, request);
                    }
                })
                .setNegativeButton("reject", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(AcceptFollowerDialog.this, request);
                    }
                });
        return builder.create();
    }

}
