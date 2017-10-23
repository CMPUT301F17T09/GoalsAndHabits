package cmput301f17t09.goalsandhabits;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.robotium.solo.Solo;

import junit.framework.TestCase;
/**
 * Created by Ken on 22/10/2017.
 */

public class FollowActivityTest extends ActivityInstrumentationTestCase2<FollowActivity> {
    private Solo solo;

    public FollowActivityTest() {super(cmput301f17t09.goalsandhabits.FollowActivity.class);}

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    public void testRequestButton() {
        solo.assertCurrentActivity("Wrong Activity", FollowActivity.class);
        View sendRequest = getActivity().findViewById(R.id.SendRequest);
        solo.clickOnView(sendRequest);
        //TODO: Enter in a valid user id and send a request
    }

    /**
     * This test requires that a different user has sent a follow request to the user being tested.
     */
    public void testRequestAcceptance() {
        solo.assertCurrentActivity("Wrong Activity", FollowActivity.class);
        View viewPending = getActivity().findViewById(R.id.ViewRequests);
        solo.clickOnView(viewPending);
        //TODO: Click on accept of a follow request
    }

    /**
     * This test requires that the user is following another user and that the user they are
     * following has some habits added to their list.
     */
    public void testClickableList() {
        solo.assertCurrentActivity("Wrong Activity", FollowActivity.class);
        //TODO: Click on an item in the list and ensure its details are correct.
    }


    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
