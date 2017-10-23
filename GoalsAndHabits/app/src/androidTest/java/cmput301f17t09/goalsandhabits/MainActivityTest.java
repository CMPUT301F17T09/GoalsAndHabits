package cmput301f17t09.goalsandhabits;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.robotium.solo.Solo;

/**
 * Created by Ken on 22/10/2017.
 */

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    public MainActivityTest() {super(cmput301f17t09.goalsandhabits.MainActivity.class);}

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void testMyHabitsButton() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        View  myHabits = getActivity().findViewById(R.id.MyHabits);
        solo.clickOnView(myHabits);
        solo.assertCurrentActivity("Wrong Activity", MyHabits.class);
    }

    public void testFollowButton() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        View  follow = getActivity().findViewById(R.id.Follow);
        solo.clickOnView(follow);
        solo.assertCurrentActivity("Wrong Activity", FollowActivity.class);
    }

    public void testMapFiltersButton() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        View  mapFilters = getActivity().findViewById(R.id.MapFilters);
        solo.clickOnView(mapFilters);
        solo.assertCurrentActivity("Wrong Activity", MapFiltersActivity.class);
    }

    public void testHabitHistoryButton() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        View  habitHistory = getActivity().findViewById(R.id.HabitHistory);
        solo.clickOnView(habitHistory);
        solo.assertCurrentActivity("Wrong Activity", HabitHistoryActivity.class);
    }

    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
