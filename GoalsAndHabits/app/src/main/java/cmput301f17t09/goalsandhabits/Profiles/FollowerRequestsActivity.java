package cmput301f17t09.goalsandhabits.Profiles;

import android.app.DialogFragment;
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

import cmput301f17t09.goalsandhabits.ElasticSearch.ElasticSearchController;
import cmput301f17t09.goalsandhabits.Follow_Activity.UsersArrayAdapter;
import cmput301f17t09.goalsandhabits.R;

import static cmput301f17t09.goalsandhabits.Main_Habits.MainActivity.MY_PREFERENCES;


/**
 * Created by chiasson on 17-11-10
 * This activity allows a user to view their follower requests in a ListView and accept or decline them.
 */
public class FollowerRequestsActivity extends AppCompatActivity implements AcceptFollowerDialog.AcceptFollowerDialogListener {

    private ArrayList<Profile> followReqs;
    private ArrayList<Profile> followers;
    private ListView followerList;
    private UsersArrayAdapter usersArrayAdapter;
    private Profile me;
    private Profile follower;
    private int pos;

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
                pos = position;
                Log.i("info","pos: "+String.format("%d",pos));
                Profile p = followReqs.get(position);
                showAcceptDialog(p);

            }
        });

        }

    }

    /**
     * Displays the dialog to accept or decline a follower request
     * @param follower Potential follower profile
     */
    public void showAcceptDialog(Profile follower) {
        DialogFragment dialog = AcceptFollowerDialog.newInstance(follower);
        dialog.show(getFragmentManager(), "AcceptFollowerDialog");
    }

    /**
     * User clicked the Accept Follower Dialog's positive option. The follow request is deleted, and
     * the follower adds the user to their users follower.
     * @param dialog Accept Follower Dialog Fragment
     * @param followerName Username of new follower profile
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String followerName) {
        Log.i("Info", "accepted follow from "+followerName);
        ElasticSearchController.GetProfilesTask acceptFollow = new ElasticSearchController.GetProfilesTask();
        acceptFollow.execute(followerName);
        try {
            followers = acceptFollow.get();
        }catch (Exception e){
            Log.i("Error", "Failed to get profiles from async object");
        }
        follower = followers.get(0);
        if (follower.getUsersFollowed()==null) {
            follower.setUsersFollowed(new ArrayList<Profile>());
        }
        follower.addFollowee(me);
        if (follower.getUsersFollowed().get(follower.getUsersFollowed().size()-1).getUsername().equals(me.getUsername())) {
            Log.i("Info", "Added " + me.getUsername() + " as user followed to " + follower.getUsername());
        }
        saveData();
        me.removeFollowReq(pos);
        saveProfile();
        usersArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Exits out of Accept Follower dialog. Deletes follow request
     * @param dialog Accept Follower Dialog Fragment
     */
    public void onDialogNegativeClick(DialogFragment dialog, String followerName) {
        // User touched the dialog's negative button
        Log.i("Info", "rejected follow from "+followerName);
        ElasticSearchController.GetProfilesTask rejectFollow = new ElasticSearchController.GetProfilesTask();
        rejectFollow.execute(followerName);
        try {
            followers = rejectFollow.get();
        }catch (Exception e){
            Log.i("Error", "Failed to get profiles from async object");
        }
        follower = followers.get(0);
        me.removeFollowReq(pos);
        usersArrayAdapter.notifyDataSetChanged();
        saveProfile();
    }

    /**
     * Saves the follower's ArrayList of users followed in the ElasticSearch server
     */
    private void saveData(){
        if (follower != null) {
            ElasticSearchController.UpdateProfileTask updateFollowerTask
                    = new ElasticSearchController.UpdateProfileTask();
            updateFollowerTask.execute(follower);
        }
    }

    /**
     * Saves user's follow requests ArrayList in the ElasticSearch server
     */
    private void saveProfile() {
        if (me!=null) {
            ElasticSearchController.UpdateProfileTask updateProfileTask
                    = new ElasticSearchController.UpdateProfileTask();
            updateProfileTask.execute(me);
        }
    }

    /**
     * Gets an instance of the user's profile
     */
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
