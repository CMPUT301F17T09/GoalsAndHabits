package cmput301f17t09.goalsandhabits.Follow_Activity;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cmput301f17t09.goalsandhabits.ElasticSearch.ElasticSearchController;
import cmput301f17t09.goalsandhabits.Main_Habits.Habit;
import cmput301f17t09.goalsandhabits.Main_Habits.HabitEvent;
import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;
import cmput301f17t09.goalsandhabits.Main_Habits.Util;
import cmput301f17t09.goalsandhabits.Maps.MapFiltersActivity;
import cmput301f17t09.goalsandhabits.Profiles.Profile;
import cmput301f17t09.goalsandhabits.Profiles.ProfileActivity;
import cmput301f17t09.goalsandhabits.R;

import static cmput301f17t09.goalsandhabits.Main_Habits.MainActivity.MY_PREFERENCES;

/**
 * Created by chiasson on 2017-10-30.
 * This activity displays all habits for each user followed, along with their statuses. The user may
 * click on a habit to see its most recent habit event, or may search for users to follow, opening
 * up a search dialog.
 */
public class FollowActivity extends AppCompatActivity implements UserSearchDialog.UserSearchDialogListener {

    public static final String EXTRA_EVENT_SERIAL = "cmput301f17t09.goalsandhabits.EVENT_SERIAL";

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

        if (!Util.isNetworkAvailable(FollowActivity.this)){
            Toast.makeText(FollowActivity.this,"You need an internet connection for that!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(FollowActivity.this,MainActivity.class);
            finish();
            startActivity(intent);
            return;
        }

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


        if (!Util.isNetworkAvailable(FollowActivity.this)) {
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
                    Intent showEvent = new Intent(FollowActivity.this, FollowedEventActivity.class);Habit habit = habitsFollowed.get(position);
                    ArrayList<HabitEvent> events = new ArrayList<HabitEvent>();
                    events.addAll(habit.getEvents());
                    Comparator<? super HabitEvent> dateCompare = new Comparator<HabitEvent>() {
                        @Override
                        public int compare(HabitEvent h1, HabitEvent h2) {
                            return -h1.getDate().compareTo(h2.getDate());
                        }
                    };
                    Collections.sort(events,dateCompare);
                    if (events.size()>0) {
                        HabitEvent mostRecent = events.get(0);

                        if (mostRecent != null) {
//                        Log.i("Info")
                            showEvent.putExtra(EXTRA_EVENT_SERIAL, mostRecent);
                            startActivity(showEvent);
                        }
                    }
                    else {
                        Toast.makeText(FollowActivity.this,"This habit has no events!",Toast.LENGTH_SHORT).show();
                    }

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

    /**
     * User clicked the positive option of the dialog, and the app searches for the requested user
     * @param dialog User Search Dialog Fragment
     * @param userSearch Username input
     */
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
     * Exits out of search dialog. Makes no changes to activity
     * @param dialog User Search Dialog Fragment
     */
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    /**
     * Gets an instance of the user to obtain those profiles followed
     */
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

    /**
     * Gets the habits of each profile followed
     * @param p Profile to find
     */
    private void loadData(Profile p){
        //habitsFollowed = new ArrayList<>();
        if (Util.isNetworkAvailable(FollowActivity.this) && p != null){
            ElasticSearchController.GetProfileTask getProfileTask
                    = new ElasticSearchController.GetProfileTask();
            getProfileTask.execute(p.getUserId());
            try {
                p = getProfileTask.get();
            } catch (Exception e) {
                Log.i("Error", "Failed to get profiles with id " + p.getUserId() + " from async object");
            }
            //Load the habits from the elasticsearch server:
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
