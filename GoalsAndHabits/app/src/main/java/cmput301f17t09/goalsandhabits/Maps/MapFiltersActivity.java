package cmput301f17t09.goalsandhabits.Maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import cmput301f17t09.goalsandhabits.ElasticSearch.ElasticSearchController;
import cmput301f17t09.goalsandhabits.Follow_Activity.FollowActivity;
import cmput301f17t09.goalsandhabits.Main_Habits.Habit;
import cmput301f17t09.goalsandhabits.Main_Habits.HabitEvent;
import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;
import cmput301f17t09.goalsandhabits.Profiles.MyHabitHistory;
import cmput301f17t09.goalsandhabits.Profiles.NewProfileActivity;
import cmput301f17t09.goalsandhabits.Profiles.Profile;
import cmput301f17t09.goalsandhabits.Profiles.ProfileActivity;
import cmput301f17t09.goalsandhabits.R;


/**
 * This activity allows users to view habit events based on their locations, and can be filtered.
 */
public class MapFiltersActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private MapView map;
    private GoogleMap gmap;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location currentLoc;
    private ArrayList<Habit> habits;
    private ArrayList<HabitEvent> events;
    private Profile profile;
    public static final int REQUEST_CODE_SIGNUP = 6;
    public static final String FILENAME = "data.sav";
    public static final String MY_PREFERENCES = "my_preferences";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("MapFiltersActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_filters);
        getProfile();
        loadData();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (Build.VERSION.SDK_INT >= 23) {
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
            }else{
                requestPermission();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("Map");
        setSupportActionBar(toolbar);

        Toolbar nav_tb = (Toolbar) findViewById(R.id.toolbar_nav);
        nav_tb.setContentInsetsAbsolute(0,0);

        map = (MapView) findViewById(R.id.mapView);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);

        ImageButton myHabits = (ImageButton) findViewById(R.id.navigation_myhabits);
        ImageButton activityFeed = (ImageButton) findViewById(R.id.navigation_activityfeed);
        ImageButton profile = (ImageButton) findViewById(R.id.navigation_profile);
        myHabits.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MapFiltersActivity.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
        activityFeed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MapFiltersActivity.this,FollowActivity.class);
                finish();
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MapFiltersActivity.this,ProfileActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_nearby_events, menu);
        final MenuItem nearbyToggle = menu.findItem(R.id.nearbyEventSwitch);
        final Switch actionView = (Switch) nearbyToggle.getActionView().findViewById(R.id.mapSwitch);
        actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){//Needs to call some method otherwise doesn't work
                    addNearbyMarkers(gmap);
                }
                else{
                    addAllMarkers(gmap);
                }

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private Boolean checkLocationPermission(){
        int result = ContextCompat.checkSelfPermission(MapFiltersActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
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
                    Toast.makeText(MapFiltersActivity.this,
                            "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MapFiltersActivity.this,
                            "Permission required", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //Add markers for events within 5km
    private void addNearbyMarkers(GoogleMap map) {
        if (currentLoc!=null) {
            map.clear();
            loadData();
            for (Habit h : habits) {
                events = h.getEvents();
                for (HabitEvent e : events) {
                    if ((e.getLat() != null && e.getLong() != null) && (e.getLat() != 0 && e.getLong() != 0)) {
                        if (currentLoc.distanceTo(e.getLocation()) <=5000) {
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(e.getLat(), e.getLong()))
                                    .title(h.getTitle()));
                        }
                    }
                }
            }
        }
    }

    //Add markers for all locations
    private void addAllMarkers(GoogleMap map) {
        map.clear();
        loadData();
        for (Habit h:habits){
            events=h.getEvents();
            for (HabitEvent e:events){
                if ((e.getLat()!=null && e.getLong()!=null) && (e.getLat()!=0 && e.getLong()!=0)){
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(e.getLat(), e.getLong()))
                            .title(h.getTitle()));
                }
            }
        }
        //TODO: add markers for followed events
    }

    @Override
    public void onMapReady(GoogleMap map) {
        gmap = map;
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkLocationPermission()){
                gmap.setMyLocationEnabled(true);
                gmap.setOnMyLocationButtonClickListener(this);
            }else{
                requestPermission();
            }
        }
        addAllMarkers(gmap);
    }

    /**
     * May or may need to include the following methods
     * Apparently you need to use these if using a map view instead of a fragment
     * But some are optional
     * See: https://developers.google.com/maps/documentation/android-api/map
     * and https://developers.google.com/maps/documentation/android-api/lite#lifecycle
     * and https://stackoverflow.com/questions/28227127/cant-get-map-with-google-maps-v2-mapview
     */

    @Override
    public final void onDestroy()
    {
        map.onDestroy();
        super.onDestroy();
    }

    @Override
    public final void onLowMemory()
    {
        map.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public final void onPause()
    {
        map.onPause();
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        map.onResume();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //shouldn't be necessary but following method is complaining
        if (Build.VERSION.SDK_INT >= 23) {
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
            }else{
                requestPermission();
            }
        }
        return false;
    }


    //adapted from https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    //as of Nov 25, 2017
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void loadData(){
        habits = new ArrayList<>();
        if (isNetworkAvailable()) {
            if (profile == null) {
                Log.i("Error", "Failed to load habits: profile is null!");
                return;
            }
            if (profile.getHabitIds() == null) {
                Log.i("Error", "Failed to load habits: habit id list is null!");
                return;
            }
            Log.i("Info", "Fetching habits for profile id " + profile.getUserId());
            ElasticSearchController.GetHabitsTask getHabitsTask
                    = new ElasticSearchController.GetHabitsTask();
            ArrayList<String> ids = profile.getHabitIds();
            getHabitsTask.execute(ids.toArray(new String[ids.size()]));
            try {
                habits = getHabitsTask.get();
            } catch (Exception e) {
                Log.i("Error", "ElasticSearch failed to find habits for profile with id " + profile.getUserId());
            }
        }else{
            //Load from local storage
            try {
                FileInputStream fis = openFileInput(FILENAME);
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));

                Gson gson = new Gson();

                //Taken from https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
                //2017-09-28
                Type listType = new TypeToken<ArrayList<Habit>>(){}.getType();
                habits = gson.fromJson(in,listType);
                Type profileType = new TypeToken<Profile>(){}.getType();
                profile = gson.fromJson(in,profileType);
                in.close();
                fis.close();

            } catch (FileNotFoundException e) {
                //We either need internet connection or previously stored data for the app to work
                throw new RuntimeException();
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }

    private void getProfile(){
        Context context = MapFiltersActivity.this;
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES,Context.MODE_PRIVATE);
        final String userId = reader.getString("userId","");
        ElasticSearchController.GetProfileTask getProfileTask
                = new ElasticSearchController.GetProfileTask();
        getProfileTask.execute(userId);
        try {
            profile = getProfileTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get profiles with id " + userId + " from async object");
            Intent intent = new Intent(MapFiltersActivity.this, NewProfileActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SIGNUP);
        }

    }
}