package cmput301f17t09.goalsandhabits.MainHabitsTests;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.robotium.solo.Solo;

import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;
import cmput301f17t09.goalsandhabits.Main_Habits.NewHabit;
import cmput301f17t09.goalsandhabits.R;

/**
 * Created by chias on 2017-10-21.
 */

public class MyHabitsTest extends ActivityInstrumentationTestCase2<MainActivity>{

    private Solo solo;

    public MyHabitsTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void testaddButton() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        View Add = getActivity().findViewById(R.id.Add);
        solo.clickOnView(Add);
        solo.assertCurrentActivity("Wrong Activity", NewHabit.class);

    }

    public void testclickableList() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        View Add = getActivity().findViewById(R.id.Add);
        solo.clickOnView(Add);
        solo.assertCurrentActivity("Wrong Activity", NewHabit.class);
        //TODO: create new test habit and return to MainActivity

        //solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        //solo.clickInList(0);
    }


    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
