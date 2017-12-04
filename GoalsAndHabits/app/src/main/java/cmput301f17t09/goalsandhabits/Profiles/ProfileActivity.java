package cmput301f17t09.goalsandhabits.Profiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import cmput301f17t09.goalsandhabits.ElasticSearch.ElasticSearchController;
import cmput301f17t09.goalsandhabits.Follow_Activity.FollowActivity;
import cmput301f17t09.goalsandhabits.Main_Habits.Habit;
import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;
import cmput301f17t09.goalsandhabits.Main_Habits.Util;
import cmput301f17t09.goalsandhabits.Maps.MapFiltersActivity;
import cmput301f17t09.goalsandhabits.R;

/**
 * Created by Andrew on 11/1/2017.
 *
 * This activity allows the user to view their profile, enable/disable online capabilities,
 * view their own habit history, and view follow requests.
 * Note: Online capabilities not yet possible
 */

public class ProfileActivity extends AppCompatActivity {



    public static final String FILENAME = "data.sav";
    public static final String MY_PREFERENCES = "my_preferences";
    Profile profile;
    private ArrayList<Habit> habits;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (!Util.isNetworkAvailable(ProfileActivity.this)){
            Toast.makeText(ProfileActivity.this,"You need an internet connection for that!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
            finish();
            startActivity(intent);
            return;
        }

        getProfile();
        loadData();


        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("My Profile");
        setSupportActionBar(toolbar);

        Toolbar nav_tb = (Toolbar) findViewById(R.id.toolbar_nav);
        nav_tb.setContentInsetsAbsolute(0,0);

        Button habitHistory = (Button) findViewById(R.id.habitHistory);
        Button followerReqs = (Button) findViewById(R.id.followerReqs);

        TextView profileName = (TextView) findViewById(R.id.profileUsername);
        TextView numHabitsText = (TextView) findViewById(R.id.habitNumText);
        TextView numOfHabits = (TextView) findViewById(R.id.numOfHabits);
        profileName.setText(profile.getUsername());
        numHabitsText.setText("Number of Habits:");
        numOfHabits.setText(Integer.toString(habits.size()));

        ImageButton myHabits = (ImageButton) findViewById(R.id.navigation_myhabits);
        ImageButton activityFeed = (ImageButton) findViewById(R.id.navigation_activityfeed);
        ImageButton maps = (ImageButton) findViewById(R.id.navigation_map);
        myHabits.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
        activityFeed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(ProfileActivity.this,FollowActivity.class);
                finish();
                startActivity(intent);
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(ProfileActivity.this,MapFiltersActivity.class);
                finish();
                startActivity(intent);
            }
        });
        habitHistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(ProfileActivity.this,MyHabitHistory.class);
                startActivity(intent);
            }
        });
        followerReqs.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(ProfileActivity.this, FollowerRequestsActivity.class);
                //TODO: Display all requests received and allow user to accept/decline.
                startActivity(intent);
            }
        });

    }

    private void getProfile(){
        Context context = ProfileActivity.this;
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES,Context.MODE_PRIVATE);
        final String userId = reader.getString("userId","");
        ElasticSearchController.GetProfileTask getProfileTask
                = new ElasticSearchController.GetProfileTask();
        getProfileTask.execute(userId);
        try {
            profile = getProfileTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get profiles with id " + userId + " from async object");
        }

    }
    private void loadData(){
        habits = new ArrayList<>();
        if (Util.isNetworkAvailable(ProfileActivity.this)) {
            if (profile == null) {
                Log.i("Error", "Failed to load habits: profile is null!");
                return;
            }
            if (profile.getHabitIds() == null) {
                Log.i("Error", "Failed to load habits: habit id list is null!");
                return;
            }
            Log.i("Info", "Fetching habits for profile id " + profile.getUserId());
            ElasticSearchController.GetHabitsTask getHabitsTask
                    = new ElasticSearchController.GetHabitsTask();
            ArrayList<String> ids = profile.getHabitIds();
            getHabitsTask.execute(ids.toArray(new String[ids.size()]));
            try {
                habits = getHabitsTask.get();
            } catch (Exception e) {
                Log.i("Error", "ElasticSearch failed to find habits for profile with id " + profile.getUserId());
            }
        }else{
            //Load from local storage
            try {
                FileInputStream fis = openFileInput(FILENAME);
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));

                Gson gson = new Gson();

                //Taken from https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
                //2017-09-28
                Type listType = new TypeToken<ArrayList<Habit>>(){}.getType();
                habits = gson.fromJson(in,listType);
                Type profileType = new TypeToken<Profile>(){}.getType();
                profile = gson.fromJson(in,profileType);
                in.close();
                fis.close();

            } catch (FileNotFoundException e) {
                //We either need internet connection or previously stored data for the app to work
                throw new RuntimeException();
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }
}
