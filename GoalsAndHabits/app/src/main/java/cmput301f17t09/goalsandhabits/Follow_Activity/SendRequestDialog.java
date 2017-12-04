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
import android.widget.TextView;

import org.w3c.dom.Text;

import cmput301f17t09.goalsandhabits.R;

public class SendRequestDialog extends DialogFragment {

    public interface SendRequestDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog, String request);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    SendRequestDialogListener mListener;

    public static SendRequestDialog newInstance(String request) {
        SendRequestDialog frag = new SendRequestDialog();
        Bundle args = new Bundle();
        args.putString("request",request);
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
            mListener = (SendRequestDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FilterDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View diaView = inflater.inflate(R.layout.dialog_send_request, null);
        final String request = getArguments().getString("request");

        builder.setTitle("Request to follow?")
                .setView(diaView)
                .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(SendRequestDialog.this, request);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(SendRequestDialog.this);
                    }
                });
        return builder.create();
    }

}
