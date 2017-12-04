package cmput301f17t09.goalsandhabits.MapActivityTests;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import cmput301f17t09.goalsandhabits.Maps.MapFiltersActivity;


/**
 * Created by Tony on 2017/10/22.
 */

public class MapFiltersActivityTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;
    public MapFiltersActivityTest() {
        super(MapFiltersActivity.class);
    }
    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }


    //TODO:  test zoom level and click on map marker

    /**
    public void testZoomLevel() {
        solo.assertCurrentActivity("Wrong Activity", MapFiltersActivity.class);
        MapView mapView = (MapView)solo.getCurrentActivity().findViewById(R.id.mapView);
        solo.clickOnView(mapView);
        int zoom = mapView.getZoomLevel();        // not working right now
    }

    public void testClickMarker(){

    }
**/
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}

