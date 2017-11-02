package cmput301f17t09.goalsandhabits;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

public class MapFiltersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("MapFiltersActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_filters);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("Map");
        setSupportActionBar(toolbar);

        Toolbar nav_tb = (Toolbar) findViewById(R.id.toolbar_nav);
        nav_tb.setContentInsetsAbsolute(0,0);

        ImageButton myHabits = (ImageButton) findViewById(R.id.navigation_myhabits);
        ImageButton activityFeed = (ImageButton) findViewById(R.id.navigation_activityfeed);
        ImageButton profile = (ImageButton) findViewById(R.id.navigation_profile);
        myHabits.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MapFiltersActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        activityFeed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MapFiltersActivity.this,FollowActivity.class);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MapFiltersActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
