package cmput301f17t09.goalsandhabits;

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
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Andrew on 11/6/2017.
 */

public class ViewHabitActivity extends AppCompatActivity implements EditHabitDialog.EditHabitDialogListener{

    private Habit habit;
    private TextView reason;
    private Context context;
    private TextView toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            if (extras.containsKey(MainActivity.EXTRA_HABIT_SERIAL)){
                habit = (Habit) extras.getSerializable(MainActivity.EXTRA_HABIT_SERIAL);
            }
        }
        if (habit==null) finish();

        reason = (TextView) findViewById(R.id.textReason);
        reason.setText(habit.getReason());

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle(habit.getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);

        Button habitEventsButton = (Button) findViewById(R.id.buttonHabitEvents);
        habitEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewHabitActivity.this, HabitHistoryActivity.class);
                i.putExtra(MainActivity.EXTRA_HABIT_SERIAL, habit);
                startActivityForResult(i,MainActivity.REQUEST_CODE_NEW_HABIT_EVENT);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Start activity for result (Create Habit Event activity)
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_view_habit, menu);
        return true;
    }

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
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showEditDialog() {
        DialogFragment dialog = EditHabitDialog.newInstance(habit.getTitle(),habit.getReason());
        dialog.show(getFragmentManager(), "EditHabitDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String newreason) {
        // User touched the dialog's positive button
        reason.setText(newreason);
        habit.setReason(newreason);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's positive button
    }
}
