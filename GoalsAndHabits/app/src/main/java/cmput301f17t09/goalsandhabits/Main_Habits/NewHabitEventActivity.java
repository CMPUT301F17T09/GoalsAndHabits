package cmput301f17t09.goalsandhabits.Main_Habits;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import java.security.Permissions;
import java.util.Calendar;
import java.util.Date;

import cmput301f17t09.goalsandhabits.R;

import static cmput301f17t09.goalsandhabits.R.id.imageView4;

/**
 * Created by Andrew on 11/12/2017.
 *
 * This activity is created when the user chooses to create a habit event
 * for a specific habit.
 */
public class NewHabitEventActivity extends AppCompatActivity {
    protected static final int CHOOSE_IMAGE_REQUEST_CODE = 104;
    public static final int MAX_IMAGE_SIZE = 65535;
    private boolean save = false;
    private String photoPath;
    private Double Lat;
    private Double Long;
    private Location Loc;
    private Switch loc;
    private ImageView imageView;
    private Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("New Habit Event");
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);
        imageView = (ImageView) findViewById(imageView4);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permission = ActivityCompat.checkSelfPermission(NewHabitEventActivity. this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permission == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,CHOOSE_IMAGE_REQUEST_CODE);
                }else{
                    ;}
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode ==CHOOSE_IMAGE_REQUEST_CODE  && resultCode == Activity.RESULT_OK && data != null) {
            try {

                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap chosenImage = BitmapFactory.decodeStream(inputStream);

                // attempt to resize the image if necessary
                chosenImage = ImageController.compressImageToMax(chosenImage, MAX_IMAGE_SIZE);

                if (chosenImage == null) {
                    image = null;
                    updateImage();
                } else if (chosenImage.getByteCount() <= MAX_IMAGE_SIZE) {
                    image = chosenImage;
                    updateImage();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        }
    private void updateImage(){
        if (image != null) {
            imageView.clearColorFilter();
            imageView.setBackgroundColor(Color.rgb(255, 255, 255));
            imageView.setImageBitmap(image);
        }
        else {
            imageView.setColorFilter(Color.rgb(0, 0, 0));
            imageView.setBackgroundColor(Color.rgb(0, 0, 0));
        }
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
            EditText comment = (EditText) findViewById(R.id.editComment);
            DatePicker dp = (DatePicker) findViewById(R.id.editDate);
            TextView path = (TextView) findViewById(R.id.PhotoPath);
            int day = dp.getDayOfMonth();
            int month = dp.getMonth();
            int year = dp.getYear();

            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,day);

            Date date = calendar.getTime();
            if (!comment.getText().toString().isEmpty()) {
                data.putExtra(MainActivity.EXTRA_HABIT_NAME, comment.getText().toString());
            }
            data.putExtra(MainActivity.EXTRA_HABIT_STARTDATE,date);
            HabitEvent event = new HabitEvent(date);
            if (image != null){
                event.setEncodedPhoto(ImageController.imageToBase64(image));
            }

        }
        setResult(RESULT_OK, data);
        super.finish();
    }


}
