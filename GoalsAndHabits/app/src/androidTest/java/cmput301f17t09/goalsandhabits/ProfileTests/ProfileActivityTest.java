package cmput301f17t09.goalsandhabits.ProfileTests;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

import com.robotium.solo.Solo;

import cmput301f17t09.goalsandhabits.Follow_Activity.FollowActivity;
import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;
import cmput301f17t09.goalsandhabits.Maps.MapFiltersActivity;
import cmput301f17t09.goalsandhabits.Profiles.FollowerRequestsActivity;
import cmput301f17t09.goalsandhabits.Profiles.LoginActivity;
import cmput301f17t09.goalsandhabits.Profiles.MyHabitHistory;
import cmput301f17t09.goalsandhabits.Profiles.ProfileActivity;
import cmput301f17t09.goalsandhabits.R;

/**
 * This tests the Profile Activity and all its functions, from using the tab to go between activities
 * to enabling/disabling online capabilities and viewing habit history and follower requests
 */
public class ProfileActivityTest extends ActivityInstrumentationTestCase2<ProfileActivity>{
    private Solo solo;

    public ProfileActivityTest() {super(ProfileActivity.class);}

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    /**
     * Tests the tab button for MapFiltersActivity
     */
    public void testMapFiltersButton() {
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        View mapfiltersbutton = getActivity().findViewById(R.id.navigation_map);
        solo.clickOnView(mapfiltersbutton);
        solo.assertCurrentActivity("Wrong Activity", MapFiltersActivity.class);
    }

    /**
     * Tests the tab button for MainActivity
     */
    public void testMainActivityButton() {
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        View mainactivitybutton = getActivity().findViewById(R.id.navigation_myhabits);
        solo.clickOnView(mainactivitybutton);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Tests the tab button for FollowActivity
     */
    public void testFollowActivityButton() {
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        View activityfeedbutton = getActivity().findViewById(R.id.navigation_activityfeed);
        solo.clickOnView(activityfeedbutton);
        solo.assertCurrentActivity("Wrong Activity", FollowActivity.class);
    }


    /**
     * Tests the MyHabitHistory button
     */
    public void testmyHabitHistory() {
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        View habithistorybutton = getActivity().findViewById(R.id.habitHistory);
        solo.clickOnView(habithistorybutton);
        solo.assertCurrentActivity("Wrong Activity", MyHabitHistory.class);
    }

    /**
     * Tests the FollowerRequests button
     */
    public void testfollowerRequests() {
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        View followreqsbutton = getActivity().findViewById(R.id.followerReqs);
        solo.clickOnView(followreqsbutton);
        solo.assertCurrentActivity("Wrong Activity", FollowerRequestsActivity.class);
    }


    //TODO: test online capabilities


    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
