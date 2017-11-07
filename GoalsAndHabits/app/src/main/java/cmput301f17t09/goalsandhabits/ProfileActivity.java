package cmput301f17t09.goalsandhabits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Andrew on 11/1/2017.
 */

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("My Profile");
        setSupportActionBar(toolbar);

        Toolbar nav_tb = (Toolbar) findViewById(R.id.toolbar_nav);
        nav_tb.setContentInsetsAbsolute(0,0);

        Button habitHistory = (Button) findViewById(R.id.habitHistory);
        Button followerReqs = (Button) findViewById(R.id.followerReqs);

        Switch online = (Switch) findViewById(R.id.online);
        boolean isOnline = online.isChecked();

        ImageButton myHabits = (ImageButton) findViewById(R.id.navigation_myhabits);
        ImageButton activityFeed = (ImageButton) findViewById(R.id.navigation_activityfeed);
        ImageButton maps = (ImageButton) findViewById(R.id.navigation_map);
        myHabits.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        activityFeed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(ProfileActivity.this,FollowActivity.class);
                startActivity(intent);
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(ProfileActivity.this,MapFiltersActivity.class);
                startActivity(intent);
            }
        });
        habitHistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(ProfileActivity.this,HabitHistoryActivity.class);
                startActivity(intent);
            }
        });
        followerReqs.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                //Intent intent = new Intent(ProfileActivity.this, FollowRequests.class);
                //startActivity(intent);
            }
        });

    }
}
