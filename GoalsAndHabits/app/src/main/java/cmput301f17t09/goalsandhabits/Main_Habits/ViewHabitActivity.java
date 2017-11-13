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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import cmput301f17t09.goalsandhabits.R;

/**
 * Created by Andrew on 11/6/2017.
 */

public class ViewHabitActivity extends AppCompatActivity implements EditHabitDialog.EditHabitDialogListener{

    private Habit habit;
    private TextView reason;
    private Context context;
    private int position;
    private TextView toolbar;

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

    public void finish() {
        //Pass back the habit and position
        Intent data = new Intent();
        data.putExtra(MainActivity.EXTRA_HABIT_SERIAL, habit);
        data.putExtra(MainActivity.EXTRA_HABIT_POSITION, position);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
