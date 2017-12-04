package cmput301f17t09.goalsandhabits.ProfileTests;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

import com.robotium.solo.Solo;

import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;
import cmput301f17t09.goalsandhabits.Profiles.LoginActivity;
import cmput301f17t09.goalsandhabits.Profiles.NewProfileActivity;
import cmput301f17t09.goalsandhabits.R;

/**
 * Created by Andrew on 10/22/2017.
 *
 * This test will runs the screens which are used only on the very first startup of the app
 */

public class NewProfileActivityTest extends ActivityInstrumentationTestCase2<NewProfileActivity>{
    private Solo solo;

    public NewProfileActivityTest() {super(NewProfileActivity.class);}

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception{
        Activity activity = getActivity();
    }

    /**
     * This tests the submit button on the NewProfileActivity and checks for uniqueness of the username
     * before assigning a userID and saving the profile to teh database
     */
    public void testSubmitButton() {
        solo.assertCurrentActivity("Wrong Activity", NewProfileActivity.class);
        View submitButton = getActivity().findViewById(R.id.submitButton);
        solo.clickOnView(submitButton);
        solo.assertCurrentActivity("Wrong Activity", NewProfileActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editUsername),"Test");
        //TODO: Check for uniqueness of username, assign userID, save the profile to the database
        solo.clickOnView(submitButton);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * This tests the login button's ability to take the user to a screen where they can enter a
     * pre-existing username and user ID
     */
    public void testLoginButton() {
        solo.assertCurrentActivity("Wrong Activity", NewProfileActivity.class);
        View loginButton = getActivity().findViewById(R.id.loginButton);
        solo.clickOnView(loginButton);
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        View signupButton = getActivity().findViewById(R.id.signup_button);
    }

    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
