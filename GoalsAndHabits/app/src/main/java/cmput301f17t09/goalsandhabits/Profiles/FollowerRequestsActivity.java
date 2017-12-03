package cmput301f17t09.goalsandhabits.Profiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cmput301f17t09.goalsandhabits.ElasticSearch.ElasticSearchController;
import cmput301f17t09.goalsandhabits.Follow_Activity.FollowActivity;
import cmput301f17t09.goalsandhabits.Follow_Activity.UsersArrayAdapter;
import cmput301f17t09.goalsandhabits.R;

import static cmput301f17t09.goalsandhabits.Main_Habits.MainActivity.MY_PREFERENCES;


/**
 * This activity allows user to view their follower requests and accept or decline them.
 * Note: This can only be implemented after elasticsearch is implemented
 */
public class FollowerRequestsActivity extends AppCompatActivity {

    private ArrayList<Profile> followReqs;
    private ListView followerList;
    private UsersArrayAdapter usersArrayAdapter;
    private Profile me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_requests);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("My Follower Requests");
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);
        followerList = (ListView) findViewById(R.id.followerReqs);
        getProfile();
        if (me.getFollowRequests()==null) {
            me.setFollowRequests(new ArrayList<Profile>());
        }
        followReqs = me.getFollowRequests();
        if (followReqs.isEmpty()) {
            Toast.makeText(FollowerRequestsActivity.this,"No follow requests", Toast.LENGTH_SHORT).show();
        }
        else {
        usersArrayAdapter = new UsersArrayAdapter(this,followReqs);
        followerList.setAdapter(usersArrayAdapter);
        followerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(RESULT_OK);

            }
        });

        }

    }
    private void getProfile(){
        Context context = FollowerRequestsActivity.this;
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES,Context.MODE_PRIVATE);
        final String userId = reader.getString("userId","");
        ElasticSearchController.GetProfileTask getProfileTask
                = new ElasticSearchController.GetProfileTask();
        getProfileTask.execute(userId);
        try {
            me = getProfileTask.get();
            Log.i("Info","success!");
        } catch (Exception e) {
            Log.i("Error", "Failed to get profiles with id " + userId + " from async object");
        }

    }
}
