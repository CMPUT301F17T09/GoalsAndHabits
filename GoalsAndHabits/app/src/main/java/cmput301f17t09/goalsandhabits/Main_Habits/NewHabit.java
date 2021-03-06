package cmput301f17t09.goalsandhabits.Main_Habits;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;
import cmput301f17t09.goalsandhabits.R;

/**
 * This activity allows the user to create a new habit
 * by specifying the title, reason, start date, and schedule.
 */
public class NewHabit extends AppCompatActivity implements DatePickerFrag.DatePickerFragListener {

    private boolean save = false;
    private Date newdate = new Date();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd", Locale.CANADA);

    /**
     * Called on activity start. Generates layout and button functionality.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("New Habit");
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);
        Button datePicker = (Button) findViewById(R.id.newDateButton);
        TextView date_field = (TextView) findViewById(R.id.dateEditText);
        date_field.setText(dateFormat.format(newdate));

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    /**
     * Creates Menu Layout
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_new_habit, menu);
        return true;
    }

    /**
     * Creates menu button functionality
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
                EditText name = (EditText) findViewById(R.id.titleEditText);
                EditText reason = (EditText) findViewById(R.id.reasonEditText);

                if (name.getText().toString().isEmpty()){
                    Toast.makeText(this, "Enter a valid habit title!", Toast.LENGTH_SHORT).show();
                }else if (reason.getText().toString().isEmpty()){
                    Toast.makeText(this, "Enter a valid habit reason!", Toast.LENGTH_SHORT).show();
                }else {
                    save=true;
                    finish();
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Return the details of the new habit to the calling activity
     * so that the habit can be created and added to the habit list.
     */
    @Override
    public void finish(){
        Intent data = new Intent();
        if (save){
            EditText name = (EditText) findViewById(R.id.titleEditText);
            EditText reason = (EditText) findViewById(R.id.reasonEditText);

            //Change days into a sequence of 1's and 0's
            //ie 1001100 = Sunday, Wednesday, Thursday.
            ArrayList<CheckBox> days = new ArrayList<CheckBox>();
            days.add((CheckBox) findViewById(R.id.sundayBox));
            days.add((CheckBox) findViewById(R.id.mondayBox));
            days.add((CheckBox) findViewById(R.id.tuesdayBox));
            days.add((CheckBox) findViewById(R.id.wednesdayBox));
            days.add((CheckBox) findViewById(R.id.thursdayBox));
            days.add((CheckBox) findViewById(R.id.fridayBox));
            days.add((CheckBox) findViewById(R.id.saturdayBox));

            String schedule = "";
            for (int i=0; i<days.size(); i++){
                CheckBox b = days.get(i);
                if (b != null){
                    if (b.isChecked()){
                        schedule += "1";
                    }else{
                        schedule += "0";
                    }
                }
            }

            data.putExtra(MainActivity.EXTRA_HABIT_NAME, name.getText().toString());
            data.putExtra(MainActivity.EXTRA_HABIT_REASON, reason.getText().toString());
            data.putExtra(MainActivity.EXTRA_HABIT_STARTDATE, newdate.getTime());
            data.putExtra(MainActivity.EXTRA_HABIT_SCHEDULE, schedule);
        }
        setResult(RESULT_OK, data);
        super.finish();
    }

    /**
     * Creates a new date picker dialog fragment and displays it.
     */
    public void showDatePickerDialog(){
        DialogFragment newFragment = new DatePickerFrag();
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    /**
     * Handles callback from date picker dialog. Sets date attribute of habit object.
     * @param dialog DatePicker
     * @param date Habit Date
     */
    @Override
    public void onDatePicked(DialogFragment dialog, Date date) {
        TextView date_field = (TextView) findViewById(R.id.dateEditText);
        date_field.setText(dateFormat.format(date));
        newdate = date;
    }
}
