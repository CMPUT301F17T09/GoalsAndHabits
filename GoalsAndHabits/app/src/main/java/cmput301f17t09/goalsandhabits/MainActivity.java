package cmput301f17t09.goalsandhabits;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_HABIT_NAME = "cmput301f17t09.goalsandhabits.HABIT_NAME";
    public static final String EXTRA_HABIT_REASON = "cmput301f17t09.goalsandhabits.HABIT_REASON";
    public static final String EXTRA_HABIT_STARTDATE = "cmput301f17t09.goalsandhabits.HABIT_STARTDATE";
    public static final String EXTRA_HABIT_SCHEDULE = "cmput301f17t09.goalsandhabits.HABIT_SCHEDULE";

    public static final int REQUEST_CODE_NEW_HABIT = 1;

    private static final String MY_PREFERENCES = "my_preferences";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, NewProfileActivity.class);
        if (MainActivity.isFirst(MainActivity.this)) {
            startActivity(intent);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("My Habits");
        setSupportActionBar(toolbar);

        Toolbar nav_tb = (Toolbar) findViewById(R.id.toolbar_nav);
        nav_tb.setContentInsetsAbsolute(0,0);

        ImageButton activityFeed = (ImageButton) findViewById(R.id.navigation_activityfeed);
        ImageButton profile = (ImageButton) findViewById(R.id.navigation_profile);
        ImageButton maps = (ImageButton) findViewById(R.id.navigation_map);
        activityFeed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MainActivity.this,FollowActivity.class);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MainActivity.this,MapFiltersActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.Add);
        ListView habitsList = (ListView) findViewById(R.id.habitsList);
        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MainActivity.this, NewHabit.class);
                startActivityForResult(intent, 1);
            }
        });

        habitsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(RESULT_OK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_NEW_HABIT:{
                    if (!data.hasExtra(EXTRA_HABIT_NAME)) return;
                    if (!data.hasExtra(EXTRA_HABIT_REASON)) return;
                    if (!data.hasExtra(EXTRA_HABIT_STARTDATE)) return;
                    if (!data.hasExtra(EXTRA_HABIT_SCHEDULE)) return;
                    String name = data.getExtras().getString(EXTRA_HABIT_NAME);
                    String reason = data.getExtras().getString(EXTRA_HABIT_REASON);
                    String date_string = data.getExtras().getString(EXTRA_HABIT_STARTDATE);
                    String days = data.getExtras().getString(EXTRA_HABIT_SCHEDULE);
                    //TODO: Extract Date from date string.
                    Date startdate = new Date();

                    HashSet<Integer> schedule = new HashSet<Integer>();

                    for (int i = 0; i<days.length(); i++){
                        if (days.charAt(i) == '1'){
                            //Add the corresponding day (Sunday = 1, Monday = 2, Tuesday = 3, etc.)
                            schedule.add(i+1);
                        }
                    }

                    Habit habit = new Habit(name, reason, startdate);
                    habit.setSchedule(schedule);

                    //TODO: Add the new habit to the list of habits

                    Toast.makeText(this, "Habit " + name + " created!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static boolean isFirst(Context context) {
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES,Context.MODE_PRIVATE);
        final boolean first = reader.getBoolean("is_first",true);
        if(first) {
            final SharedPreferences.Editor editor = reader.edit();
            editor.putBoolean("is_first", false);
            editor.commit();
        }
        return first;
    }

}
