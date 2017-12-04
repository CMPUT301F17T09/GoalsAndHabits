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
import android.widget.TextView;

import cmput301f17t09.goalsandhabits.R;

public class AddCommentDialog extends DialogFragment {

    public interface AddCommentDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog,String comment);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    EditText enteredComment;
    AddCommentDialogListener mListener;

    public static AddCommentDialog newInstance() {
        AddCommentDialog frag = new AddCommentDialog();
        Bundle args = new Bundle();
//        args.putString("request",request);
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
            mListener = (AddCommentDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddCommentListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View diaView = inflater.inflate(R.layout.dialog_add_comment, null);
        //final String request = getArguments().getString("request");
        enteredComment = (EditText) diaView.findViewById(R.id.commentText);



        builder.setTitle("Add a Comment")
                .setView(diaView)
                .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String comment = enteredComment.getText().toString();
                        mListener.onDialogPositiveClick(AddCommentDialog.this, comment);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(AddCommentDialog.this);
                    }
                });
        return builder.create();
    }

}
