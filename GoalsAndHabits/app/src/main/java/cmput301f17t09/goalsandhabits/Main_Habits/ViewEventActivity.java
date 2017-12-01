package cmput301f17t09.goalsandhabits.Main_Habits;

import android.Manifest;
import android.app.DialogFragment;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;

import cmput301f17t09.goalsandhabits.Maps.MapFiltersActivity;
import cmput301f17t09.goalsandhabits.R;

import static cmput301f17t09.goalsandhabits.Main_Habits.MainActivity.FILENAME;

/**
 * Created by Simone on 2017/11/22.
 */

public class ViewEventActivity extends AppCompatActivity implements EditHabitEventDialog.EditHabitEventDialogListener {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private HabitEvent event;
    private TextView comment;
    private Context context;
    private int position;
    private Toolbar toolbar;
    private boolean deleted = false;
    private Location currentLoc;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit_events);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            if (extras.containsKey(HabitHistoryActivity.EXTRA_EVENT_SERIAL)){
                event = (HabitEvent) extras.getSerializable(HabitHistoryActivity.EXTRA_EVENT_SERIAL);
                position = (int) extras.getSerializable(HabitHistoryActivity.EXTRA_EVENT_POSITION);
            }
        }
        if (event==null) finish();


        if (checkLocationPermission()){
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                currentLoc = location;
                            }
                        }
                    });
        }else{requestPermission();}

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        comment = (TextView) findViewById(R.id.eventComment);

        toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("Habit Event");
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);

        comment.setText(event.getComment());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_view_habit, menu);
        return true;
    }
    /**
     * Handles the buttons in the action bar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.editButton:{
                showEditDialog();
                //finish();
                return true;
            }
            case R.id.deleteButton:{;
                deleted=true;
                finish();
                return true;

            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showEditDialog() {
        DialogFragment dialog = EditHabitEventDialog.newInstance(event.getComment(),event.getPhotoPath(),event.getLocation());
        dialog.show(getFragmentManager(), "EditHabitEventDialog");
    }

    @Override
    public void finish() {
        //Pass back the habit and position
        Intent data = new Intent();
        if (deleted){
            data.putExtra(HabitHistoryActivity.EXTRA_EVENT_DELETED,true);
        }
        data.putExtra(HabitHistoryActivity.EXTRA_EVENT_SERIAL, event);
        data.putExtra(HabitHistoryActivity.EXTRA_EVENT_POSITION, position);
        setResult(RESULT_OK, data);
        super.finish();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String newcomment, Location newloc) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public Location onLocButtonClick(DialogFragment dialog) {
        if (checkLocationPermission()){
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                currentLoc = location;
                            }
                        }
                    });
        }else{requestPermission();}

        return currentLoc;
    }

    private Boolean checkLocationPermission(){
        int result = ContextCompat.checkSelfPermission(ViewEventActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ViewEventActivity.this,
                            "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ViewEventActivity.this,
                            "Permission required", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
