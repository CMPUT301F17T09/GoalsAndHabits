package cmput301f17t09.goalsandhabits.Main_Habits;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import cmput301f17t09.goalsandhabits.R;


public class HabitHistoryActivity extends AppCompatActivity {

    private Habit habit;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("Habit History of " + habit.getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);

        //TODO: pass back the habit when finishing this activity (in case it changed)
    }
}
