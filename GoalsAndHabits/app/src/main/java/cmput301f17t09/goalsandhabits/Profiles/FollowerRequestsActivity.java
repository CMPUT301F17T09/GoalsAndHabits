package cmput301f17t09.goalsandhabits.Profiles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import cmput301f17t09.goalsandhabits.R;


/**
 * This activity allows user to view their follower requests and accept or decline them.
 * Note: This can only be implemented after elasticsearch is implemented
 */
public class FollowerRequestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_requests);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("My Follower Requests");
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);
    }
}
