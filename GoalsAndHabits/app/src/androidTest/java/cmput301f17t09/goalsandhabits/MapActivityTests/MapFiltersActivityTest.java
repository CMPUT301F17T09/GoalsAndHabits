package cmput301f17t09.goalsandhabits.MapActivityTests;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.google.android.gms.maps.MapView;
import com.robotium.solo.Solo;

import cmput301f17t09.goalsandhabits.Maps.MapFiltersActivity;
import cmput301f17t09.goalsandhabits.R;


/**
 * Created by Tony on 2017/10/22.
 * Contains some tests for the Main map activity
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

    /**
     * Checks to see if map starts and markers are being placed
     */
    public void testMapStart(){
        solo.assertCurrentActivity("Wrong Activity", MapFiltersActivity.class);
        solo.waitForText("Displaying nearby events");
    }

    /**
     * Checks to see if map starts and nearby event toggle is working
     */
    public void testNearbyToggle() {
        solo.assertCurrentActivity("Wrong Activity", MapFiltersActivity.class);
        View toggle = getActivity().findViewById(R.id.nearbyEventSwitch);
        solo.clickOnView(toggle);
        solo.waitForText("Displaying nearby events");
    }

    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}

