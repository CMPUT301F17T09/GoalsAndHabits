package cmput301f17t09.goalsandhabits.Profiles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import cmput301f17t09.goalsandhabits.R;


/**
 * This activity allows the user to view their own habit history of all habits they have created,
 * sorted by date.
 * Note: have yet to determine how to use HabitEventArrayAdapter to access all habit events and sort
 * them.
 *
 */
public class MyHabitHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_habit_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("My Habit History");
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);
    }
}
