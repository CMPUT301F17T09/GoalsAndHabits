package cmput301f17t09.goalsandhabits.Main_Habits;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.Date;

import cmput301f17t09.goalsandhabits.R;

import static cmput301f17t09.goalsandhabits.Main_Habits.MainActivity.FILENAME;

/**
 * Created by Andrew on 11/6/2017.
 *
 * This activity allows the user to view the details of
 * a specific habit. The user can also edit or delete the
 * habit in this activity, as well as add habit events.
 */
public class ViewHabitActivity extends AppCompatActivity implements EditHabitDialog.EditHabitDialogListener{

    private Habit habit;
    private TextView reason;
    private Context context;
    private int position;
    private Toolbar toolbar;
    private ArrayList<HabitEvent> habits = new ArrayList<HabitEvent>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);
        loadFromFile();

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            if (extras.containsKey(MainActivity.EXTRA_HABIT_SERIAL)){
                habit = (Habit) extras.getSerializable(MainActivity.EXTRA_HABIT_SERIAL);
                position = (int) extras.getSerializable(MainActivity.EXTRA_HABIT_POSITION);
            }
        }
        if (habit==null) finish();

        reason = (TextView) findViewById(R.id.textReason);
        reason.setText(habit.getReason());

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
            case R.id.deleteButton:{
                deleteHabit();
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
                    HabitEvent habitEvent = new HabitEvent(date);
                    if (data.hasExtra(MainActivity.EXTRA_HABIT_NAME)){
                        habitEvent.setComment(data.getStringExtra(MainActivity.EXTRA_HABIT_NAME));
                    }
                    habit.addHabitEvent(habitEvent);
                    break;
                }
                case MainActivity.REQUEST_CODE_VIEW_HABIT_HISTORY:{
                    if (data.hasExtra(MainActivity.EXTRA_HABIT_SERIAL)){
                        habit = (Habit) data.getSerializableExtra(MainActivity.EXTRA_HABIT_SERIAL);
                    }
                }
                }
            }
    }

    /**
     * Creates a new instance of the edit habit dialog and displays it.
     */
    public void showEditDialog() {
        DialogFragment dialog = EditHabitDialog.newInstance(habit.getTitle(),habit.getReason());
        dialog.show(getFragmentManager(), "EditHabitDialog");
    }
    /**
    * delete from file
    **/
    public void deleteHabit(){
        habits = habit.getEvents();
        setResult(RESULT_OK);
        habits.remove(position);
        saveToFile();
        Intent backToMain = new Intent(ViewHabitActivity.this, MainActivity.class);
        startActivity(backToMain);
    }
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();

            // Taken from https://stackoverflow.com/question/12384064/gson-convert-from-json-into java
            // 2017 01-26 17:53:59
            habits = gson.fromJson(in, new TypeToken<ArrayList<HabitEvent>>(){}.getType());

            fis.close();

        } catch (FileNotFoundException e) {
            habits = new ArrayList<HabitEvent>();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
    /**
    *commit changes of delete
    */
     public void saveToFile(){
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

    /**
     * Receives new information from edit habit dialog and makes appropriate updates to the habit.
     * Closes dialog.
     * @param dialog Edit Habit Dialog Fragment
     * @param newreason Updated habit reason string
     * @param newtitle Updated habit name string
     */

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String newreason, String newtitle) {
        reason.setText(newreason);
        toolbar.setTitle(newtitle);
        habit.setReason(newreason);
        habit.setTitle(newtitle);
        //habit.setStartDate(newdate); //Not updating, will have to make changes to main activity
    }

    /**
     * Exits out of edit habit dialog. Makes no changes to habit.
     * @param dialog Edit Habit Dialog Fragment
     */
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    public void finish() {
        //Pass back the habit and position
        Intent data = new Intent();
        data.putExtra(MainActivity.EXTRA_HABIT_SERIAL, habit);
        data.putExtra(MainActivity.EXTRA_HABIT_POSITION, position);
        setResult(RESULT_OK, data);
        super.finish();
    }

}
