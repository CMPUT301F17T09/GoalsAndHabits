package cmput301f17t09.goalsandhabits.Main_Habits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import cmput301f17t09.goalsandhabits.R;

/**
 * Created by Andrew on 11/12/2017.
 *
 * This activity is created when the user chooses to create a habit event
 * for a specific habit.
 */
public class NewHabitEventActivity extends AppCompatActivity {

    private boolean save = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("New Habit Event");
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);


    }

    /**
     * Inflates the action bar buttons
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_new_habit_event, menu);
        return true;
    }

    /**
     * Handles interacting with the action bar buttons
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.saveButton:{
                save = true;
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when the activity is finished.
     * This method returns the details of the new habit event to
     * the calling activity in order to create the habit event.
     */
    @Override
    public void finish() {
        Intent data = new Intent();
        if (save){
            EditText comment = (EditText) findViewById(R.id.editComment);
            DatePicker dp = (DatePicker) findViewById(R.id.editDate);

            int day = dp.getDayOfMonth();
            int month = dp.getMonth();
            int year = dp.getYear();

            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,day);

            Date date = calendar.getTime();
            if (!comment.getText().toString().isEmpty()) {
                data.putExtra(MainActivity.EXTRA_HABIT_NAME, comment.getText().toString());
            }
            data.putExtra(MainActivity.EXTRA_HABIT_STARTDATE,date);
        }
        setResult(RESULT_OK, data);
        super.finish();
    }

}
