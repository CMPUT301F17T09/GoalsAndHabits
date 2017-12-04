package cmput301f17t09.goalsandhabits.Main_Habits;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Date;

import cmput301f17t09.goalsandhabits.R;

/**
 * Created by Andrew on 11/12/2017.
 *
 * This activity is created when the user chooses to create a habit event
 * for a specific habit.
 */
public class NewHabitEventActivity extends AppCompatActivity {
    private static int REQUEST_IMAGE_CAPTURE = 2;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private boolean save = false;
    private ImageView editPhoto;
    private Bitmap imageDisplay;
    private EditText editComment;
    private DatePicker editDate;
    private String photoEncoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("New Habit Event");
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);

        editPhoto = (ImageView) findViewById(R.id.editPhoto);
        editComment = (EditText) findViewById(R.id.editComment);
        editDate = (DatePicker) findViewById(R.id.editDate);
        photoEncoded = "";

        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });


    }
    /**
     * Inflates the action bar buttons
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_new_habit_event, menu);
        return true;
    }

    /**
     * Handles interacting with the action bar buttons
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.saveButton:{
                save = true;
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when the activity is finished.
     * This method returns the details of the new habit event to
     * the calling activity in order to create the habit event.
     */
    @Override
    public void finish() {
        Intent data = new Intent();
        if (save){
            int day = editDate.getDayOfMonth();
            int month = editDate.getMonth();
            int year = editDate.getYear();

            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,day);

            Date date = calendar.getTime();
            if (!editComment.getText().toString().isEmpty()) {
                data.putExtra(MainActivity.EXTRA_HABIT_NAME, editComment.getText().toString());
            }
            data.putExtra(MainActivity.EXTRA_HABIT_STARTDATE,date);
            if (photoEncoded!=null && !photoEncoded.isEmpty()) {
                data.putExtra(MainActivity.EXTRA_HABIT_EVENT_PHOTO, photoEncoded);
            }
        }
        setResult(RESULT_OK, data);
        super.finish();
    }

    //Adapted from https://developer.android.com/training/camera/photobasics.html
    //Dec 4 2017
    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            imageDisplay = (Bitmap) extras.get("data");
            editPhoto.setImageBitmap(imageDisplay);
            photoEncoded = (ImageController.imageToBase64(imageDisplay));
        }
    }



}
