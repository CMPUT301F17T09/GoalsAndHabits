package cmput301f17t09.goalsandhabits.Follow_Activity;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cmput301f17t09.goalsandhabits.ElasticSearch.ElasticSearchController;
import cmput301f17t09.goalsandhabits.Main_Habits.Habit;
import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;
import cmput301f17t09.goalsandhabits.Maps.MapFiltersActivity;
import cmput301f17t09.goalsandhabits.Profiles.FollowerRequestsActivity;
import cmput301f17t09.goalsandhabits.Profiles.Profile;
import cmput301f17t09.goalsandhabits.Profiles.ProfileActivity;
import cmput301f17t09.goalsandhabits.R;

import static cmput301f17t09.goalsandhabits.Main_Habits.MainActivity.MY_PREFERENCES;

public class FollowActivity extends AppCompatActivity implements UserSearchDialog.UserSearchDialogListener {

    private FollowedHabitsArrayAdapter usersFollowed;
    private ArrayList<Profile> followees;
    private ArrayList<Habit> habitsFollowed;
    private ListView followList;
    private Profile me;


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

        Button newRequest = (Button) findViewById(R.id.SendRequest);
        followList = (ListView) findViewById(R.id.FollowList);


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


        if (!isNetworkAvailable()) {
            Toast.makeText(FollowActivity.this,"Connect to see activity!", Toast.LENGTH_SHORT).show();
        }
        else {
            getProfile();
            if (me.getUsersFollowed()==null) {
                Log.i("Info", "usersfollowed is null");
                me.setUsersFollowed(new ArrayList<Profile>());
                Toast.makeText(FollowActivity.this,"You're not following anyone yet!", Toast.LENGTH_SHORT).show();
            }
            habitsFollowed = new ArrayList<Habit>();
            followees = me.getUsersFollowed();
            for (Profile p: followees) {
                loadData(p);
                Log.i("Info","Getting habits of "+p.getUsername());
            }
            usersFollowed = new FollowedHabitsArrayAdapter(this,habitsFollowed);
            followList.setAdapter(usersFollowed);

            newRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK);
                    showSearchDialog();
                }
            });

            followList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setResult(RESULT_OK);
                    //Intent showEvent = new Intent(FollowActivity.this, FollowedEventActivity.class)
                }
            });
        }

    }

    /**
     * Creates a new instance of the search dialog and displays it.
     */
    public void showSearchDialog() {
        DialogFragment dialog = UserSearchDialog.newInstance();
        dialog.show(getFragmentManager(), "UserSearchDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String userSearch) {
        if (userSearch.equals("")) {
            Toast.makeText(FollowActivity.this,"Please enter a user to search for!", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent results = new Intent(FollowActivity.this, SearchResultsActivity.class);
            results.putExtra("search", userSearch);
            startActivity(results);
        }

    }

    /**
     * Exits out of filter dialog. Makes no changes to activity
     * @param dialog Filter Dialog Fragment
     */
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }
    private void getProfile(){
        Context context = FollowActivity.this;
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES,Context.MODE_PRIVATE);
        final String userId = reader.getString("userId","");
        ElasticSearchController.GetProfileTask getProfileTask
                = new ElasticSearchController.GetProfileTask();
        getProfileTask.execute(userId);
        try {
            me = getProfileTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get profiles with id " + userId + " from async object");
        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void loadData(Profile p){
        habitsFollowed = new ArrayList<>();
        if (isNetworkAvailable() && p != null){
            //Now load the habits from the elasticsearch server:
            ArrayList<Habit> onlineHabits = new ArrayList<>();
            if (p.getHabitIds()!=null){
                Log.i("Info", "Fetching habits for profile id " + p.getUserId());
                ElasticSearchController.GetHabitsTask getHabitsTask
                        = new ElasticSearchController.GetHabitsTask();
                ArrayList<String> ids = p.getHabitIds();
                Log.i("Info",ids.toString());
                getHabitsTask.execute(ids.toArray(new String[ids.size()]));
                try {
                    onlineHabits = getHabitsTask.get();
                } catch (Exception e) {
                    Log.i("Error", "ElasticSearch failed to find habits for profile with id " + p.getUserId());
                }
            }
            habitsFollowed.addAll(onlineHabits);
            Log.i("Info","Total habits: " + habitsFollowed.size());
        }
    }
}
