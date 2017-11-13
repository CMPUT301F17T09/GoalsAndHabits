package cmput301f17t09.goalsandhabits.Main_Habits;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;
import cmput301f17t09.goalsandhabits.R;

public class NewHabit extends AppCompatActivity {

    private boolean save = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("New Habit");
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_new_habit, menu);
        return true;
    }

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
                EditText date = (EditText) findViewById(R.id.dateEditText);

                //TODO: Better field checking.
                //TODO: Use a better date picker.
                //showDatePickerDialog();

                if (name.getText().toString().isEmpty()){
                    Toast.makeText(this, "Enter a valid habit title!", Toast.LENGTH_SHORT).show();
                }else if (reason.getText().toString().isEmpty()){
                    Toast.makeText(this, "Enter a valid habit reason!", Toast.LENGTH_SHORT).show();
                }else if (date.getText().toString().isEmpty()){
                    Toast.makeText(this, "Enter a valid start date!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void finish(){
        Intent data = new Intent();
        if (save){
            EditText name = (EditText) findViewById(R.id.titleEditText);
            EditText reason = (EditText) findViewById(R.id.reasonEditText);
            EditText date = (EditText) findViewById(R.id.dateEditText);
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
            data.putExtra(MainActivity.EXTRA_HABIT_STARTDATE, date.getText().toString());
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
}
