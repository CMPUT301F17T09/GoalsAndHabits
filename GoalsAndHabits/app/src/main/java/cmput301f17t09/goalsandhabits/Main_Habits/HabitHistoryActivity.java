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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import cmput301f17t09.goalsandhabits.R;


public class HabitHistoryActivity extends AppCompatActivity implements EditHabitEventDialog.EditHabitEventDialogListener {

    private Habit habit;
    Context context;

    private HabitEventArrayAdapter habitEventArrayAdapter;
    private ListView habitEventsList;
    private HabitEvent habitEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_history);

        Bundle extras = getIntent().getExtras();
        habitEvent = (HabitEvent) extras.getSerializable("EVENT");
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
    }
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
                    habitEventArrayAdapter.notifyDataSetChanged();
                }
            }
        }
    }
    public void showEditDialog() {
      DialogFragment dialog = EditHabitDialog.newInstance(habitEvent.getComment(), habitEvent.getPhotoPath());
      dialog.show(getFragmentManager(), "EditHabitEventDialog");
    }


    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra(MainActivity.EXTRA_HABIT_SERIAL, habit);
        setResult(RESULT_OK, data);
        super.finish();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String s, String newComment, Date newDate) {

    }

    @Override
    public void onDialogNegativeƒClick(DialogFragment dialog) {

    }
}
