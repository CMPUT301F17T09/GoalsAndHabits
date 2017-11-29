package cmput301f17t09.goalsandhabits.Main_Habits;

import android.app.DialogFragment;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
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
 * Created by Simone on 2017/11/22.
 */

public class ViewEventActivity extends AppCompatActivity implements EditHabitEventDialog.EditHabitEventDialogListener {
    private HabitEvent event;
    private TextView comment;
    private Context context;
    private int position;
    private Toolbar toolbar;
    private ArrayList<HabitEvent> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit_events);
        loadFromFile();

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            if (extras.containsKey(MainActivity.EXTRA_HABIT_SERIAL)){
                event = (HabitEvent) extras.getSerializable(HabitHistoryActivity.EXTRA_EVENT_SERIAL);
                position = (int) extras.getSerializable(HabitHistoryActivity.EXTRA_EVENT_POSITION);
            }
        }
        if (event==null) finish();

        comment = (TextView) findViewById(R.id.eventComment);
        comment.setText(event.getComment());

        toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle(event.getComment());
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);

        Button habitEventsButton = (Button) findViewById(R.id.buttonHabitEvents);
        habitEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewEventActivity.this, HabitHistoryActivity.class);
                i.putExtra(MainActivity.EXTRA_HABIT_SERIAL, event);
                startActivityForResult(i,MainActivity.REQUEST_CODE_VIEW_HABIT_HISTORY);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_view_myhabitevents, menu);
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
                setResult(RESULT_OK);
                events.remove(position);
                saveInFile();
                Intent backToMain = new Intent(ViewEventActivity.this, HabitHistoryActivity.class);
                startActivity(backToMain);
                return true;

            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showEditDialog() {
        DialogFragment dialog = EditHabitEventDialog.newInstance(event.getComment(),event.getPhotoPath());
        dialog.show(getFragmentManager(), "EditHabitEventDialog");
    }



    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();

            // Taken from https://stackoverflow.com/question/12384064/gson-convert-from-json-into java
            // 2017 01-26 17:53:59
            events= gson.fromJson(in, new TypeToken<ArrayList<HabitEvent>>(){}.getType());

            fis.close();

        } catch (FileNotFoundException e) {
            events = new ArrayList<HabitEvent>();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
    /**
     *commit changes of delete
     */
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(events, out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }



    public void onDialogPositiveClick(DialogFragment dialog, String newComment, String newPhotoPath,
                                      Location newLocation) {
        comment.setText(newComment);
        event.setComment(newComment);
        event.setPhotoPath(newPhotoPath);
         //Not updating, will have to make changes to main activity
    }


    public void showEditEventDialog() {
        DialogFragment dialog = EditHabitDialog.newInstance(event.getComment(), event.getPhotoPath());
        dialog.show(getFragmentManager(), "EditHabitEventDialog");
    }
    public void finish() {
        //Pass back the habit and position
        Intent data = new Intent();
        data.putExtra(MainActivity.EXTRA_HABIT_SERIAL, event);
        data.putExtra(MainActivity.EXTRA_HABIT_POSITION, position);
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
