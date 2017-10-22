package cmput301f17t09.goalsandhabits;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.robotium.solo.Solo;

import junit.framework.TestCase;
/**
 * Created by chias on 2017-10-21.
 */

public class MyHabitsTest extends ActivityInstrumentationTestCase2<MyHabits>{

    private Solo solo;

    public MyHabitsTest() {
        super(cmput301f17t09.goalsandhabits.MyHabits.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void testaddButton() {
        solo.assertCurrentActivity("Wrong Activity", MyHabits.class);
        View Add = getActivity().findViewById(R.id.Add);
        solo.clickOnView(Add);
        solo.assertCurrentActivity("Wrong Activity", NewHabit.class);

    }

    public void testclickableList() {
        solo.assertCurrentActivity("Wrong Activity", MyHabits.class);
        View Add = getActivity().findViewById(R.id.Add);
        solo.clickOnView(Add);
        solo.assertCurrentActivity("Wrong Activity", NewHabit.class);
        //TODO: create new test habit and return to MyHabits

        //solo.assertCurrentActivity("Wrong Activity", MyHabits.class);
        //solo.clickInList(0);
    }


    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}