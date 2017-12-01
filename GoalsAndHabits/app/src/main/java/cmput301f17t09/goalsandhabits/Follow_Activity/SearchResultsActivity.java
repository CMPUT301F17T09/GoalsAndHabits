package cmput301f17t09.goalsandhabits.Follow_Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import cmput301f17t09.goalsandhabits.ElasticSearch.ElasticSearchController;
import cmput301f17t09.goalsandhabits.Profiles.Profile;
import cmput301f17t09.goalsandhabits.R;

public class SearchResultsActivity extends AppCompatActivity {

    private String search;
    private UsersArrayAdapter usersArrayAdapterdapter;
    private ListView usersList;
    ArrayList<Profile> matches = new ArrayList<Profile>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Bundle extras = getIntent().getExtras();
        if (extras!=null && extras.get("search")!=null){
            search = extras.get("search").toString();
        }
        if (search==null) finish();

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("Searching Users");
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);

        ElasticSearchController.GetProfilesTask getProfilesTask
                = new ElasticSearchController.GetProfilesTask();
        getProfilesTask.execute(search);
        try {
            matches = getProfilesTask.get();
        }catch (Exception e){
            Log.i("Error", "Failed to get profiles from async object");
        }
        if (matches.size()>0){
            //TODO: create profiles array adapter and display in list
            Log.i("Info","Match found: "+matches.get(0).getUsername());
            usersList = (ListView) findViewById(R.id.usersList);
            usersArrayAdapterdapter = new UsersArrayAdapter(this,matches);
            usersList.setAdapter(usersArrayAdapterdapter);
            usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setResult(RESULT_OK);
                    Profile p = matches.get(position);
                    if (p!=null) {
//                        Intent intent = new Intent(MainActivity.this, ViewHabitActivity.class);
//                        intent.putExtra(EXTRA_HABIT_SERIAL, h);
//                        intent.putExtra(EXTRA_HABIT_POSITION, position);
//                        startActivityForResult(intent, REQUEST_CODE_VIEW_HABIT);
                    }
                }
            });
        }else {
            Toast.makeText(SearchResultsActivity.this,"No matches found!",Toast.LENGTH_SHORT).show();
            finish();
        }
    }


}
