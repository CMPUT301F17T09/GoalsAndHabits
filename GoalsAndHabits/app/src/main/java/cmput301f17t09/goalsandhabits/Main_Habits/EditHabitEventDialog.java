package cmput301f17t09.goalsandhabits.Main_Habits;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.Date;

import cmput301f17t09.goalsandhabits.R;

/**
 * Created by Simone on 2017/11/20.
 */

public class EditHabitEventDialog extends DialogFragment {

    private Location currentloc;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    private ImageView imgPicture;
    private String encodedImage = "";
    Bitmap imageToDisplay;


    public interface EditHabitEventDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog, String newcomment, Location newloc);
        public void onDialogNegativeClick(DialogFragment dialog);
        public Location onLocButtonClick(DialogFragment dialog);
    }
    EditHabitEventDialog.EditHabitEventDialogListener mListener;

    public static EditHabitEventDialog newInstance(String comment, String photoPath, Location location ) {
        EditHabitEventDialog dialog= new EditHabitEventDialog();
        Bundle args = new Bundle();
        args.putString("Comments", comment);
        args.putString("photoPath", photoPath);
        if (location != null){
            args.putDouble("latitude", location.getLatitude());
            args.putDouble("longitude", location.getLongitude());
        }
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
        Image image;
        Double lat = getArguments().getDouble("latitude");
        Double lon = getArguments().getDouble("longitude");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View diaView = inflater.inflate(R.layout.dialog_edit_habit_events, null);

        final EditText comment_field = (EditText) diaView.findViewById(R.id.editComment);
        comment_field.setText(comment);

        final TextView location_field = (TextView) diaView.findViewById(R.id.textLocation);
        if (lat != null && lon != null){
            currentloc = new Location("");
            currentloc.setLatitude(lat);
            currentloc.setLongitude(lon);
            location_field.setText("("+Location.convert(lat,Location.FORMAT_DEGREES)+","+
                    Location.convert(lon,Location.FORMAT_DEGREES)+")");
        }
        final ImageView imageField = (ImageView) diaView.findViewById(R.id.imageView2) ;
        //imageField.setImageBitmap();
        //TODO: Photo stuff

        Button loc_button = (Button) diaView.findViewById(R.id.locButton);
        loc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentloc = mListener.onLocButtonClick(EditHabitEventDialog.this);
                if (currentloc != null){
                    location_field.setText("(" + Location.convert(currentloc.getLatitude(), Location.FORMAT_DEGREES)
                            +","+Location.convert(currentloc.getLongitude(), Location.FORMAT_DEGREES)+")");
                }
            }
        });
        Button imageButton = (Button) diaView.findViewById(R.id.imageView2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                loadImagefromGallery();
            }
        });

        builder .setView(diaView)
                .setPositiveButton("accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String newComment= comment_field.getText().toString();
                       // String newPhoto = photo_field.getText().toString();
                        mListener.onDialogPositiveClick(EditHabitEventDialog.this, newComment, currentloc);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(EditHabitEventDialog.this);
                    }
                });
        return builder.create();
    }
    public void loadImagefromGallery(View view) {

        // Create intent to Open Image applications like Gallery, Google Photos

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,

                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Start the Intent

        startActivityForResult(galleryIntent, IMAGE_GALLERY_REQUEST);

    }

}
