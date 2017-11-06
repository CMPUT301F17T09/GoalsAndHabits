package cmput301f17t09.goalsandhabits;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class EditHabit extends AppCompatActivity{
	private boolean save = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_habit);
		Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
		toolbar.setTitle("Edit Habit");
		toolbar.setNavigationIcon(R.drawable.ic_close_button);
		setSupportActionBar(toolbar);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_edit_habit, menu);
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
	}


public class MoreEdit extends AppCompatActivity {

	private ArrayList<EachActivity> activities;
	private ArrayAdapter<EachActivity> adapter;

	private EditText newName;
	private EditText newComment;
	private EditText newCount;

	private int position;
	@Override
	/**
	 * Called when the activity is first created.
	 * @param savedInstanceState
	 */
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_edit);
		loadFromFile();
		Intent intent = getIntent();
		position = intent.getExtras().getInt("pos");

		newName = (EditText) findViewById(R.id.newName);
		newComment = (EditText) findViewById(R.id.newComment);
		newCount = (EditText) findViewById(R.id.newCount);

		Button saveButton = (Button) findViewById(R.id.save);
		Button backButton = (Button) findViewById(R.id.back);
		saveButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String nameStr = new String();
				nameStr="";
				String CommentStr = new String();
				CommentStr="";
				String CountStr = new String();
				CountStr="";
				int newValue = -1;

				nameStr = newName.getText().toString();
				nameStr.trim();
				CountStr = newCount.getText().toString();
				CommentStr = newComment.getText().toString();
				if (CountStr.equals("")==false ){
					try {
						newValue = Integer.valueOf(CountStr).intValue();
					} catch (Exception e) {
						Intent intent = new Intent(MoreEdit.this, Exception2.class);
						intent.putExtra("pos", position);
						startActivity(intent);
					}
				}


				if (CommentStr.equals("")==false  & CommentStr.length()<30) {
					(activities.get(position)).setComment(CommentStr);
					(activities.get(position)).setDate();
					saveInFile();
				}
				if (nameStr.equals("")==false & nameStr.length()<30) {
					(activities.get(position)).setName(nameStr);
					(activities.get(position)).setDate();
					saveInFile();
				}
				if (CountStr.equals("")==false  & CountStr.length()<30 & newValue!=-1) {
					(activities.get(position)).setCount(newValue);
					(activities.get(position)).setInitValue(newValue);
					(activities.get(position)).setDate();
					saveInFile();
				}



			}

		});



		backButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent backToMain = new Intent(MoreEdit.this, MainActivity.class);
				startActivity(backToMain);
			}

		});




	}





	/**
	 * https://github.com/ta301fall2017/lonelyTwitter/tree/f17TueLab3
	 * Loads data from file.
	 * @throws RuntimeException if IOException e happens
	 * @exception FileNotFoundException if the file is not created
	 */



	private void loadFromFile() {
		try {
			FileInputStream fis = openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();

			// Taken from https://stackoverflow.com/question/12384064/gson-convert-from-json-into java
			// 2017 01-26 17:53:59
			activities = gson.fromJson(in, new TypeToken<ArrayList<EachActivity>>(){}.getType());

			fis.close();

		} catch (FileNotFoundException e) {
			activities = new ArrayList<EachActivity>();
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

	/**
	 * Saves tweets in file in JSON format.
	 * @throws FileNotFoundException if folder not exists
	 */
	private void saveInFile() {
		try {
			FileOutputStream fos = openFileOutput(FILENAME,
					Context.MODE_PRIVATE);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
			Gson gson = new Gson();
			gson.toJson(activities, out);
			out.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException();
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

}
