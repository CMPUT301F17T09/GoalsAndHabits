package cmput301f17t09.goalsandhabits.Main_Habits;


import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cmput301f17t09.goalsandhabits.ElasticSearch.ElasticSearchController;
import cmput301f17t09.goalsandhabits.R;


public class HabitHistoryActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_VIEW_EVENT = 7;
    public static final String EXTRA_EVENT_SERIAL = "cmput301f17t09.goalsandhabits.EVENT_SERIAL";
    public static final String EXTRA_EVENT_POSITION = "cmput301f17t09.goalsandhabits.EVENT_POSITION";
    public static final String EXTRA_EVENT_DELETED = "cmput301f17t09.goalsandhabits.EVENT_DELETED";
    private Habit habit;
    Context context;

    private HabitEventArrayAdapter habitEventArrayAdapter;
    private ListView habitEventsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_history);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            if (extras.containsKey(MainActivity.EXTRA_HABIT_SERIAL)){
                habit = (Habit) extras.getSerializable(MainActivity.EXTRA_HABIT_SERIAL);
            }
        }
        if (habit==null) finish();

        context = this;

        habitEventsList = (ListView) findViewById(R.id.habitEventList);
        habitEventArrayAdapter = new HabitEventArrayAdapter(this, habit.getEvents());
        habitEventsList.setAdapter(habitEventArrayAdapter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("Habit History of " + habit.getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addHabitEventButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewHabitEventActivity.class);
                startActivityForResult(intent, MainActivity.REQUEST_CODE_NEW_HABIT_EVENT);
            }
        });
        habitEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(RESULT_OK);
                HabitEvent he = habit.getEvents().get(position);
                if (he!=null) {
                    Intent intent = new Intent(HabitHistoryActivity.this, ViewEventActivity.class);
                    intent.putExtra(EXTRA_EVENT_SERIAL, he);
                    intent.putExtra(EXTRA_EVENT_POSITION, position);
                    startActivityForResult(intent, REQUEST_CODE_VIEW_EVENT);
                }
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case MainActivity.REQUEST_CODE_NEW_HABIT_EVENT:{
                    Date date;
                    if (!data.hasExtra(MainActivity.EXTRA_HABIT_STARTDATE)) return;
                    date = (Date) data.getSerializableExtra(MainActivity.EXTRA_HABIT_STARTDATE);
                    HabitEvent habitEvent = new HabitEvent(date);
                    if (data.hasExtra(MainActivity.EXTRA_HABIT_NAME)){
                        habitEvent.setComment(data.getStringExtra(MainActivity.EXTRA_HABIT_NAME));
                    }
                    habit.addHabitEvent(habitEvent);
                    habitEventArrayAdapter.notifyDataSetChanged();
                }
                case REQUEST_CODE_VIEW_EVENT:{
                    if (data.hasExtra(EXTRA_EVENT_POSITION) && data.hasExtra(EXTRA_EVENT_SERIAL)){
                        int pos = (int) data.getSerializableExtra(EXTRA_EVENT_POSITION);
                        HabitEvent habitevent = (HabitEvent) data.getSerializableExtra(EXTRA_EVENT_SERIAL);
                        if (data.hasExtra(EXTRA_EVENT_DELETED)){
                            habit.deleteHabitEvent(pos);
                            habitEventArrayAdapter.notifyDataSetChanged();
                        }else {
                            ArrayList<HabitEvent> habitEvents = habit.getEvents();
                            habitEvents.set(pos,habitevent);
                            habit.setEvents(habitEvents);
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra(MainActivity.EXTRA_HABIT_SERIAL, habit);
        setResult(RESULT_OK, data);
        super.finish();
    }

}
