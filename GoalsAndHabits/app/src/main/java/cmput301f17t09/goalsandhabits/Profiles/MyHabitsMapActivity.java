package cmput301f17t09.goalsandhabits.Profiles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import cmput301f17t09.goalsandhabits.Main_Habits.HabitEvent;
import cmput301f17t09.goalsandhabits.R;

/**
 * This activity displays a map showing locations of the user's own habit events that are passed to
 * this activity
 */
public class MyHabitsMapActivity extends AppCompatActivity implements OnMapReadyCallback{
    private MapView map;
    private GoogleMap gmap;
    private ArrayList<HabitEvent> events;


    /**
     * Called on activity start. Generates layout and button functionality.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_habits_map);

        events = (ArrayList<HabitEvent>) getIntent().getSerializableExtra("event list");
        if (events==null || events.size()<1){finish();}


        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("Map of My Habit History");
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);

        map = (MapView) findViewById(R.id.myHabitsMap);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);
    }

    /**
     * Adds a marker for each event with a location.
     * @param map
     */
    private void addAllMarkers(GoogleMap map){
        for (HabitEvent e:events){
            if ((e.getLat()!=null && e.getLong()!=null) && (e.getLat()!=0 && e.getLong()!=0)){
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(e.getLat(), e.getLong()))
                        .title(e.getHabitType()));
            }
        }
    }

    /**
     * Retrieves map object once synced
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        addAllMarkers(gmap);
    }

    /**
     * Lifecycle method for mapview
     */
    @Override
    public final void onDestroy()
    {
        map.onDestroy();
        super.onDestroy();
    }

    /**
     * Lifecycle method for mapview
     */
    @Override
    public final void onLowMemory()
    {
        map.onLowMemory();
        super.onLowMemory();
    }

    /**
     * Lifecycle method for mapview
     */
    @Override
    public final void onPause()
    {
        map.onPause();
        super.onPause();
    }

    /**
     * Lifecycle method for mapview
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        map.onResume();
    }
}
