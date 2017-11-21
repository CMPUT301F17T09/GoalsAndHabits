package cmput301f17t09.goalsandhabits.Profiles;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import cmput301f17t09.goalsandhabits.Main_Habits.Habit;
import cmput301f17t09.goalsandhabits.Main_Habits.HabitEventArrayAdapter;
import cmput301f17t09.goalsandhabits.R;


/**
 * This activity allows the user to view their own habit history of all habits they have created,
 * sorted by date.
 * Note: have yet to determine how to use HabitEventArrayAdapter to access all habit events and sort
 * them.
 *
 */
public class MyHabitHistory extends AppCompatActivity {



    private HabitEventArrayAdapter habitEventArrayAdapter;
    private ListView habitEventsList;
    private ArrayList<Habit> habits;
    public static final String FILENAME = "data.sav";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_habit_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("My Habit History");
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);

        context = this;

        habitEventsList = (ListView) findViewById(R.id.myHabitEventList);
        loadData();
        //TODO: For each habit, add all events to array adapter
        habitEventArrayAdapter = new HabitEventArrayAdapter(this,habits.get(0).getEvents());
        if (habits.size()>1) {
            for (int i=1;i<habits.size();i++) {
                habitEventArrayAdapter.addAll(habits.get(i).getEvents());
            }
        }
        habitEventsList.setAdapter(habitEventArrayAdapter);

    }

    /**
     * Inflates the action bar with buttons
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_view_habit, menu);
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
            //TODO: Add filter by Habit
            case R.id.editButton:{
                //
                return true;
            }
            //TODO: Add filter by comment
            case R.id.deleteButton:{
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
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
}
