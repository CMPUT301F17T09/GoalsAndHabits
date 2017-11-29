package cmput301f17t09.goalsandhabits.Maps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import cmput301f17t09.goalsandhabits.Follow_Activity.FollowActivity;
import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;
import cmput301f17t09.goalsandhabits.Profiles.ProfileActivity;
import cmput301f17t09.goalsandhabits.R;


/**
 * This activity allows users to view habit events based on their locations, and can be filtered.
 */
public class MapFiltersActivity extends AppCompatActivity implements OnMapReadyCallback{

    private MapView map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("MapFiltersActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_filters);

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
    public void onMapReady(GoogleMap gmap) {
        gmap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
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

}
