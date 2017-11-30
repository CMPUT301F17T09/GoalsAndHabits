package cmput301f17t09.goalsandhabits.Maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import cmput301f17t09.goalsandhabits.Follow_Activity.FollowActivity;
import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;
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
    Location currentLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("MapFiltersActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_filters);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkLocationPermission()){

            }else{
                requestPermission();
            }
        }
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
                    addMarkers(gmap);
                }
                else{
                    clearMap(gmap);
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
                            "Permission accepted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MapFiltersActivity.this,
                            "Permission required to continue", Toast.LENGTH_SHORT).show();
                    requestPermission();

                }
                break;
        }
    }

    //Temporary method for testing marker stuff
    private void clearMap(GoogleMap map) {
        map.clear();
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }

    //Temporary method for testing marker stuff
    private void addMarkers(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(currentLoc.getLatitude()+0.0005, currentLoc.getLongitude()+0.0005))
                .title("Super Marker"));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        gmap = map;
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkLocationPermission()){

            }else{
                requestPermission();
            }
        }
        gmap.setMyLocationEnabled(true);
        gmap.setOnMyLocationButtonClickListener(this);
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

            }else{
                requestPermission();
            }
        }
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
        return false;
    }
}
