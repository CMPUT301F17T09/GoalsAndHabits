package cmput301f17t09.goalsandhabits.Main_Habits;

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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import cmput301f17t09.goalsandhabits.Follow_Activity.FollowActivity;
import cmput301f17t09.goalsandhabits.Maps.MapFiltersActivity;
import cmput301f17t09.goalsandhabits.Profiles.NewProfileActivity;
import cmput301f17t09.goalsandhabits.Profiles.Profile;
import cmput301f17t09.goalsandhabits.Profiles.ProfileActivity;
import cmput301f17t09.goalsandhabits.R;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_HABIT_NAME = "cmput301f17t09.goalsandhabits.HABIT_NAME";
    public static final String EXTRA_HABIT_REASON = "cmput301f17t09.goalsandhabits.HABIT_REASON";
    public static final String EXTRA_HABIT_STARTDATE = "cmput301f17t09.goalsandhabits.HABIT_STARTDATE";
    public static final String EXTRA_HABIT_SCHEDULE = "cmput301f17t09.goalsandhabits.HABIT_SCHEDULE";
    public static final String EXTRA_HABIT_SERIAL = "cmput301f17t09.goalsandhabits.HABIT_SERIAL";
    public static final String EXTRA_HABIT_POSITION = "cmput301f17t09.goalsandhabits.HABIT_POSITION";

    public static final String FILENAME = "data.sav";

    public static final int REQUEST_CODE_NEW_HABIT = 1;
    public static final int REQUEST_CODE_NEW_HABIT_EVENT = 2;
    public static final int REQUEST_CODE_VIEW_HABIT_HISTORY = 3;
    public static final int REQUEST_CODE_VIEW_HABIT = 4;

    private static final String MY_PREFERENCES = "my_preferences";

    private ArrayList<Habit> habits;
    private HabitArrayAdapter habitArrayAdapter;
    private ListView habitsList;

    public Profile myProfile = new Profile();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = MainActivity.this;
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES,Context.MODE_PRIVATE);
        final boolean first = reader.getBoolean("is_first",true);
        Intent intent = new Intent(MainActivity.this, NewProfileActivity.class);


        //adapted from https://stackoverflow.com/questions/7238532/how-to-launch-activity-only-once-when-app-is-opened-for-first-time
        //as of Nov 13, 2017
        if (first) {
            startActivity(intent); //If this is the first startup of the app, run profile creation activity
        }
        final SharedPreferences.Editor editor = reader.edit();
        editor.putBoolean("is_first",false);
        editor.commit();


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
        habitsList = (ListView) findViewById(R.id.habitsList);
        loadData();
        habitArrayAdapter = new HabitArrayAdapter(this, habits);
        habitsList.setAdapter(habitArrayAdapter);
        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MainActivity.this, NewHabit.class);
                startActivityForResult(intent, REQUEST_CODE_NEW_HABIT);
            }
        });

        habitsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(RESULT_OK);
                Habit h = habits.get(position);
                if (h!=null) {
                    Intent intent = new Intent(MainActivity.this, ViewHabitActivity.class);
                    intent.putExtra(EXTRA_HABIT_SERIAL, h);
                    intent.putExtra(EXTRA_HABIT_POSITION, position);
                    startActivityForResult(intent, REQUEST_CODE_VIEW_HABIT);
                }
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

                    habits.add(habit);
                    habitArrayAdapter.notifyDataSetChanged();

                    Toast.makeText(this, "Habit " + name + " created!", Toast.LENGTH_SHORT).show();
                    break;
                }
                case REQUEST_CODE_VIEW_HABIT:{
                    if (data.hasExtra(EXTRA_HABIT_POSITION) && data.hasExtra(EXTRA_HABIT_SERIAL)){
                        int pos = (int) data.getSerializableExtra(EXTRA_HABIT_POSITION);
                        habits.set(pos, (Habit) data.getSerializableExtra(EXTRA_HABIT_SERIAL));
                        habitArrayAdapter.notifyDataSetChanged();
                    }
                    break;
                }
            }
        }
    }

    @Override
    protected void onStop(){
        saveData();
        super.onStop();
    }


    private void loadData(){
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            //Taken from https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            //2017-09-28
            Type listType = new TypeToken<ArrayList<Habit>>(){}.getType();
            habits = gson.fromJson(in,listType);
            in.close();
            fis.close();

        } catch (FileNotFoundException e) {
            habits = new ArrayList<Habit>();
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    private void saveData(){
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(habits, out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}