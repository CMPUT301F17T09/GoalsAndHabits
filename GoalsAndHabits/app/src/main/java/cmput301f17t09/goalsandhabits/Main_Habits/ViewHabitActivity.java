package cmput301f17t09.goalsandhabits.Main_Habits;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import cmput301f17t09.goalsandhabits.Follow_Activity.Comment;
import cmput301f17t09.goalsandhabits.R;

import static cmput301f17t09.goalsandhabits.Main_Habits.MainActivity.FILENAME;

/**
 * Created by Andrew on 11/6/2017.
 *
 * This activity allows the user to view the details of
 * a specific habit. The user can also edit or delete the
 * habit in this activity, as well as add habit events.
 */
public class ViewHabitActivity extends AppCompatActivity implements EditHabitDialog.EditHabitDialogListener,
                                                            DatePickerFrag.DatePickerFragListener{

    private Habit habit;
    private TextView reason;
    private TextView startdate;
    private TextView schedule;
    private ImageView imageView;
    private TextView statusText;
    private Context context;
    private int position;
    private Toolbar toolbar;
    private boolean deleted = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd");
    private float statThreshold = 50;

    /**
     * Called on activity start. Generates layout and button functionality.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            if (extras.containsKey(MainActivity.EXTRA_HABIT_SERIAL)){
                habit = (Habit) extras.getSerializable(MainActivity.EXTRA_HABIT_SERIAL);
                position = (int) extras.getSerializable(MainActivity.EXTRA_HABIT_POSITION);
            }
        }
        if (habit==null) finish();

        refreshData();

        toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle(habit.getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);

        Button habitEventsButton = (Button) findViewById(R.id.buttonHabitEvents);
        habitEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewHabitActivity.this, HabitHistoryActivity.class);
                i.putExtra(MainActivity.EXTRA_HABIT_SERIAL, habit);
                startActivityForResult(i,MainActivity.REQUEST_CODE_VIEW_HABIT_HISTORY);
            }
        });

        Button habitDateButton = (Button) findViewById(R.id.changeDate);
        habitDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDatePickerDialog();
            }
        });

        context = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewHabitEventActivity.class);
                startActivityForResult(intent, MainActivity.REQUEST_CODE_NEW_HABIT_EVENT);
            }
        });
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
            case R.id.editButton:{
                showEditDialog();
                //finish();
                return true;
            }
            case R.id.deleteButton:{;
                deleted=true;
                finish();
                return true;

            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when an activity that was called for a result returns.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case MainActivity.REQUEST_CODE_NEW_HABIT_EVENT:{
                    Date date;
                    if (!data.hasExtra(MainActivity.EXTRA_HABIT_STARTDATE)) return;
                    date = (Date) data.getSerializableExtra(MainActivity.EXTRA_HABIT_STARTDATE);
                    if (habit.checkEventExistsOnDate(date)){
                        Toast.makeText(ViewHabitActivity.this,"You already have an event for that day!",Toast.LENGTH_LONG).show();
                        break;
                    }
                    HabitEvent habitEvent = new HabitEvent(date);
                    if (data.hasExtra(MainActivity.EXTRA_HABIT_NAME)){
                        habitEvent.setComment(data.getStringExtra(MainActivity.EXTRA_HABIT_NAME));
                    }
                    if (data.hasExtra(MainActivity.EXTRA_HABIT_EVENT_PHOTO)){
                        habitEvent.setEncodedPhoto(data.getStringExtra(MainActivity.EXTRA_HABIT_EVENT_PHOTO));
                        Log.i("Info","Added photo!");
                    }
                    habitEvent.setLikes(0);
                    habitEvent.setComments(new ArrayList<Comment>());
                    habit.addHabitEvent(habitEvent);
                    habitEvent.setHabitType(habit.getTitle());
                    refreshData();
                    break;
                }
                case MainActivity.REQUEST_CODE_VIEW_HABIT_HISTORY:{
                    if (data.hasExtra(MainActivity.EXTRA_HABIT_SERIAL)){
                        habit = (Habit) data.getSerializableExtra(MainActivity.EXTRA_HABIT_SERIAL);
                        refreshData();
                    }
                    break;
                }
                }
            }
    }

    /**
     * Creates a new instance of the edit habit dialog and displays it.
     */
    public void showEditDialog() {
        DialogFragment dialog = EditHabitDialog.newInstance(habit.getTitle(),habit.getReason(),habit.getSchedule());
        dialog.show(getFragmentManager(), "EditHabitDialog");
    }


    /**
     * Receives new information from edit habit dialog and makes appropriate updates to the habit.
     * Closes dialog.
     * @param dialog Edit Habit Dialog Fragment
     * @param newreason Updated habit reason string
     * @param newtitle Updated habit name string
     */

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String newreason, String newtitle, HashSet<Integer> newschedule) {
        reason.setText(newreason);
        toolbar.setTitle(newtitle);
        schedule.setText(getScheduleString(newschedule));
        habit.setReason(newreason);
        habit.setTitle(newtitle);
        habit.setSchedule(newschedule);
    }

    /**
     * Exits out of edit habit dialog. Makes no changes to habit.
     * @param dialog Edit Habit Dialog Fragment
     */
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    /**
     * Called on activity finish. Passes any changes back to host activity.
     */
    public void finish() {
        //Pass back the habit and position
        Intent data = new Intent();
        if (deleted){
            data.putExtra(MainActivity.EXTRA_HABIT_DELETED,true);
        }
        data.putExtra(MainActivity.EXTRA_HABIT_SERIAL, habit);
        data.putExtra(MainActivity.EXTRA_HABIT_POSITION, position);
        setResult(RESULT_OK, data);
        super.finish();
    }

    /**
     * Calls a new instance of the date picker dialog
     */
    public void showDatePickerDialog(){
        DialogFragment newFragment = new DatePickerFrag();
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    /**
     * Handles the call back from the date picker
     * @param dialog DatePicker
     * @param date Habit Date
     */
    @Override
    public void onDatePicked(DialogFragment dialog, Date date) {
        boolean allowed=true;
        ArrayList<HabitEvent> events = habit.getEvents();
        if (events!=null){
            for (HabitEvent event : events){
                if (Util.isDateAfter(event.getDate(),date)){
                    allowed=false;
                    break;
                }
            }
        }
        if (allowed) {
            TextView habitDate = (TextView) findViewById(R.id.textHabitDate);
            habitDate.setText(dateFormat.format(date));
            habit.setStartDate(date);
        }else{
            Toast.makeText(ViewHabitActivity.this,"An event already exists prior to " + dateFormat.format(date) + "!",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Counts how many days it's been since the last event.
     * @return Number of days since last event
     */
    private int getDaysFromLastEvent(){
        //TODO: Can this be more efficient?
        if (habit.getEvents() != null){
            ArrayList<HabitEvent> events = habit.getEvents();
            if (!events.isEmpty()) {
                Calendar c = Calendar.getInstance();
                Calendar d = Calendar.getInstance();
                c.setTime(events.get(0).getDate());
                for (int i=1;i<events.size();i++){
                    d.setTime(events.get(i).getDate());
                    if (c.before(d)){
                        c.setTime(d.getTime());
                    }
                }
                return Util.getDaysBetweenDates(c.getTime(),new Date());
            }
        }
        return 0;
    }

    /**
     * Returns the habit schedule as a string.
     * @param schedule
     * @return String of habit schedule
     */
    private String getScheduleString(HashSet<Integer> schedule){
        String s = "";
        if (schedule.size()==7) return "Daily";
        if (schedule.contains(Calendar.SUNDAY)) s += "Sunday, ";
        if (schedule.contains(Calendar.MONDAY)) s += "Monday, ";
        if (schedule.contains(Calendar.TUESDAY)) s += "Tuesday, ";
        if (schedule.contains(Calendar.WEDNESDAY)) s += "Wednesday, ";
        if (schedule.contains(Calendar.THURSDAY)) s += "Thursday, ";
        if (schedule.contains(Calendar.FRIDAY)) s += "Friday, ";
        if (schedule.contains(Calendar.SATURDAY)) s += "Saturday, ";
        if (s.length()>2) {
            s = s.substring(0, s.length() - 2);
        }
        return s;
    }

    /**
     * Reloads layout with habit data
     */
    private void refreshData(){
        reason = (TextView) findViewById(R.id.textReason);
        reason.setText(habit.getReason());
        startdate = (TextView) findViewById(R.id.textHabitDate);
        startdate.setText(dateFormat.format(habit.getStartDate()));
        schedule = (TextView) findViewById(R.id.textSchedule);
        schedule.setText(getScheduleString(habit.getSchedule()));
        imageView = (ImageView) findViewById(R.id.imageView);
        statusText = (TextView) findViewById(R.id.imageText);
        float possibleEvents = habit.getPossibleEvents();
        float stats = ((habit.getEventsCompleted()/possibleEvents)*100);
        if (possibleEvents==0) stats=0;
        statusText.setText("You are " + String.format("%.0f",stats) + "% consistent!");
        if (stats>statThreshold){
            imageView.setImageResource(R.drawable.ic_checkmark);
            statusText.setTextColor(Color.parseColor("#41C61F"));
        }else{
            imageView.setImageResource(R.drawable.ic_offtrack);
            statusText.setTextColor(Color.parseColor("#D12121"));
        }

        TextView eventsMissed = (TextView) findViewById(R.id.textMissedEvents);
        eventsMissed.setText(String.format("%.0f",possibleEvents-habit.getEventsCompleted()));
        TextView daysSinceLastEvent = (TextView) findViewById(R.id.textDaysSinceLastEvent);
        daysSinceLastEvent.setText(Integer.toString(getDaysFromLastEvent()));
    }


}
