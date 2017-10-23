package cmput301f17t09.goalsandhabits;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.robotium.solo.Solo;

/**
 * Created by Andrew on 10/22/2017.
 */

public class NewProfileActivityTest extends ActivityInstrumentationTestCase2<NewProfileActivity>{
    private Solo solo;

    public NewProfileActivityTest() {super(cmput301f17t09.goalsandhabits.NewProfileActivity.class);}

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void testSubmitButton() {
        solo.assertCurrentActivity("Wrong Activity", NewProfileActivity.class);
        View submitButton = getActivity().findViewById(R.id.submitButton);
        solo.clickOnView(submitButton);
        //TODO: Enter a username and save the profile to the database
    }

    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
