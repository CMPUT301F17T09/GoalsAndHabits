package cmput301f17t09.goalsandhabits.Profiles;

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
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Comparator;

import cmput301f17t09.goalsandhabits.ElasticSearch.ElasticSearchController;
import cmput301f17t09.goalsandhabits.Main_Habits.Habit;
import cmput301f17t09.goalsandhabits.Main_Habits.HabitEvent;
import cmput301f17t09.goalsandhabits.Main_Habits.HabitEventArrayAdapter;
import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;
import cmput301f17t09.goalsandhabits.R;


/**
 * This activity allows the user to view their own habit history of all habits they have created,
 * sorted by date.
 * Note: have yet to sort habit events by date and add filter and map options
 *
 */
public class MyHabitHistory extends AppCompatActivity implements FilterDialog.FilterDialogListener {


    public static final int REQUEST_CODE_SIGNUP = 6;

    public static final String MY_PREFERENCES = "my_preferences";
    private MyEventsArrayAdapter myEventsArrayAdapter;
    private ListView habitEventsList;
    private ArrayList<Habit> habits;
    private ArrayList<HabitEvent> events;
    public static final String FILENAME = "data.sav";
    Context context;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_habit_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("My Habit History");
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);
        getProfile();

        context = this;

        habitEventsList = (ListView) findViewById(R.id.myHabitEventList);
        loadData();
        if (!habits.isEmpty()) {myEventsArrayAdapter = new MyEventsArrayAdapter(this,new ArrayList<HabitEvent>());}

        if (habits.size()>0) {
            for (int i=0;i<habits.size();i++) {
                myEventsArrayAdapter.addAll(habits.get(i).getEvents());
                Log.i("Info", "Adding events");
            }
            if (myEventsArrayAdapter.isEmpty()) {
                Log.i("Error", "Failed to load events: habit events list is null!");
            }
        }
        Comparator<? super HabitEvent> dateCompare = new Comparator<HabitEvent>() {
            @Override
            public int compare(HabitEvent h1, HabitEvent h2) {
                return -h1.getDate().compareTo(h2.getDate());
            }
        };
        if (!((myEventsArrayAdapter ==  null)) && !(myEventsArrayAdapter.isEmpty())) {
            Log.i("Info", "Sorting");
            myEventsArrayAdapter.sort(dateCompare);
            habitEventsList.setAdapter(myEventsArrayAdapter);
        }
        else {
            Toast.makeText(MyHabitHistory.this,"No habit events to display!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Inflates the action bar with buttons
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_view_myhabitevents, menu);
        return true;
    }

    /**
     * Handles the buttons in the action bar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            //TODO: Add filters
            case R.id.filter:{
                showFilterDialog();
                return true;
            }
            case R.id.viewOnMap:{
                Intent showmap = new Intent(MyHabitHistory.this,MyHabitsMapActivity.class);
                ArrayList<HabitEvent> elist = new ArrayList<>();
                //Workaround because there is no persistent event list
                for (int i=0; i<myEventsArrayAdapter.getCount(); i++){
                    elist.add(myEventsArrayAdapter.getItem(i));
                }
                showmap.putExtra("event list", elist);

                startActivity(showmap);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Creates a new instance of the filter dialog and displays it.
     */
    public void showFilterDialog() {
        DialogFragment dialog = FilterDialog.newInstance();
        dialog.show(getFragmentManager(), "FilterDialog");
    }

    /**
     * Exits out of Filter Dialog and updates list to match search parameters
     * @param dialog Filter Dialog Fragment
     * @param habitType Habit Type search term
     * @param commentSearch Habit Event Comment search term
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String habitType, String commentSearch) {
        //TODO: search habit events for habits matching search parameters, notify data set changed?
        //If there is something to search
        if (!habits.isEmpty() && (!(habitType.equals("")) || !(commentSearch.equals("")))) {
            if (habitType.equals("*")) {
                habitType=".*";
            }
            if (commentSearch.equals("*")) {
                commentSearch=".*";
            }
            myEventsArrayAdapter.clear();
            for (Habit h : habits) {
                if (!(habitType.equals("")) && h.getTitle().matches("(?i)(" + habitType + ")")) {
                    if (!(commentSearch.equals("")) && !(h.getEvents().isEmpty())) {
                        for (HabitEvent e: h.getEvents()) {
                            if((e.getComment()!=null) && e.getComment().matches("(?i)(.*?"+commentSearch+".*)")) {
                                myEventsArrayAdapter.add(e);
                            }
                        }
                    }
                    else {
                        myEventsArrayAdapter.addAll(h.getEvents());
                    }
                }
                else if(!(commentSearch.equals("")) && habitType.equals("")) {
                    for (HabitEvent e: h.getEvents()) {
                        if ((e.getComment()!=null) && e.getComment().matches("(?i)(.*?"+commentSearch+".*)")) {
                            myEventsArrayAdapter.add(e);
                        }
                    }
                }
            }
            Comparator<? super HabitEvent> dateCompare = new Comparator<HabitEvent>() {
                @Override
                public int compare(HabitEvent h1, HabitEvent h2) {
                    return -h1.getDate().compareTo(h2.getDate());
                }
            };
            if (!((myEventsArrayAdapter ==  null)) && !(myEventsArrayAdapter.isEmpty())) {
                myEventsArrayAdapter.sort(dateCompare);
                habitEventsList.setAdapter(myEventsArrayAdapter);
            }
            else {
                Toast.makeText(MyHabitHistory.this,"No habit events matched!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(MyHabitHistory.this,"Please enter search parameter(s)!", Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * Exits out of filter dialog. Makes no changes to list.
     * @param dialog Filter Dialog Fragment
     */
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    private void loadData(){
        habits = new ArrayList<>();
        if (isNetworkAvailable()) {
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

    private void getProfile(){
        Context context = MyHabitHistory.this;
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES,Context.MODE_PRIVATE);
        final String userId = reader.getString("userId","");
        ElasticSearchController.GetProfileTask getProfileTask
                    = new ElasticSearchController.GetProfileTask();
        getProfileTask.execute(userId);
        try {
            profile = getProfileTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get profiles with id " + userId + " from async object");
            Intent intent = new Intent(MyHabitHistory.this, NewProfileActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SIGNUP);
        }

    }


    //adapted from https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    //as of Nov 25, 2017
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
