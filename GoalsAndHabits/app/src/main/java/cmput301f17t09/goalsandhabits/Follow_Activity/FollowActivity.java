package cmput301f17t09.goalsandhabits.Follow_Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;
import cmput301f17t09.goalsandhabits.Maps.MapFiltersActivity;
import cmput301f17t09.goalsandhabits.Profiles.ProfileActivity;
import cmput301f17t09.goalsandhabits.R;

public class FollowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("Activity Feed");
        setSupportActionBar(toolbar);

        Toolbar nav_tb = (Toolbar) findViewById(R.id.toolbar_nav);
        nav_tb.setContentInsetsAbsolute(0,0);

        ImageButton myHabits = (ImageButton) findViewById(R.id.navigation_myhabits);
        ImageButton profile = (ImageButton) findViewById(R.id.navigation_profile);
        ImageButton maps = (ImageButton) findViewById(R.id.navigation_map);
        myHabits.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(FollowActivity.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(FollowActivity.this,ProfileActivity.class);
                finish();
                startActivity(intent);
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(FollowActivity.this,MapFiltersActivity.class);
                finish();
                startActivity(intent);
            }
        });

        Button newRequest = (Button) findViewById(R.id.SendRequest);
        ListView followList = (ListView) findViewById(R.id.FollowList);

        newRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                //TODO: Create a dialog and prompt user for id to add.
            }
        });

        followList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(RESULT_OK);
            }
        });
    }
}
