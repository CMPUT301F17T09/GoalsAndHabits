package cmput301f17t09.goalsandhabits.Follow_Activity;

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
import cmput301f17t09.goalsandhabits.Profiles.Profile;
import cmput301f17t09.goalsandhabits.R;

import static cmput301f17t09.goalsandhabits.Main_Habits.MainActivity.MY_PREFERENCES;

/**
 * Created by chiasson on 2017-12-02.
 * This activity displays the user's profile search results in a ListView. The user may click on a
 * list item to open a dialog asking if the user wishes to send the selected profile a follow request.
 */
public class SearchResultsActivity extends AppCompatActivity implements SendRequestDialog.SendRequestDialogListener {

    private String search;
    private UsersArrayAdapter usersArrayAdapter;
    private ListView usersList;
    ArrayList<Profile> matches = new ArrayList<Profile>();
    private Profile me;
    private Profile followee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Bundle extras = getIntent().getExtras();
        if (extras!=null && extras.get("search")!=null){
            search = extras.get("search").toString();
        }
        if (search==null) finish();
        getProfile();


        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("Searching Users");
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);

        ElasticSearchController.GetProfilesTask getProfilesTask = new ElasticSearchController.GetProfilesTask();
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
            usersArrayAdapter = new UsersArrayAdapter(this,matches);
            usersList.setAdapter(usersArrayAdapter);
            usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setResult(RESULT_OK);
                    Profile p = matches.get(position);
                    if (p!=null) {

                        boolean alreadyFollowed = false;
                        if (p.getUsername().equals(me.getUsername())) {
                            Toast.makeText(SearchResultsActivity.this,"You cannot follow yourself!",Toast.LENGTH_SHORT).show();
                            alreadyFollowed=true;
                        }
                        else {
                            if (me.getUsersFollowed()!= null){
                                for (Profile name : me.getUsersFollowed()) {
                                    if (name.getUsername().equals(p.getUsername())) {
                                        Toast.makeText(SearchResultsActivity.this, "You have already followed that user!", Toast.LENGTH_SHORT).show();
                                        alreadyFollowed = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (!alreadyFollowed) {
                            showRequestDialog(p);
                        }

                    }
                }
            });
        }else {
            Toast.makeText(SearchResultsActivity.this,"No matches found!",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Displays the dialog asking if the user wishes to send a follow request to the selected profile
     * @param p Profile to be requested to follow
     */
    public void showRequestDialog(Profile p) {
        DialogFragment dialog = SendRequestDialog.newInstance(p.getUsername());
        dialog.show(getFragmentManager(), "SendRequestDialog");
    }

    /**
     * User clicked the request dialog's positive option
     * @param dialog Send Request Dialog Fragment
     * @param userName Username of profile to follow
     */
    public void onDialogPositiveClick(DialogFragment dialog, String userName) {
        Log.i("Info", "requested to follow "+userName);
        ElasticSearchController.GetProfilesTask sendARequest = new ElasticSearchController.GetProfilesTask();
        sendARequest.execute(userName);
        try {
            matches = sendARequest.get();
        }catch (Exception e){
            Log.i("Error", "Failed to get profiles from async object");
        }
        followee = matches.get(0);
        if (followee.getFollowRequests()==null) {
            followee.setFollowRequests(new ArrayList<Profile>());
        }
        followee.addFollowReq(me);
        if (!followee.getFollowRequests().isEmpty()) {
            Log.i("Info", "Added " + me.getUsername() + " to " + followee.getUsername());
        }
        saveData();
    }

    /**
     * Exits out of request dialog. Makes no changes to activity
     * @param dialog Send Request Dialog Fragment
     */
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

    /**
     * Saves the follow request to the requested profile in the ElasticSearch Server
     */
    private void saveData(){
        if (followee != null) {
            ElasticSearchController.UpdateProfileTask updateProfileTask
                    = new ElasticSearchController.UpdateProfileTask();
            updateProfileTask.execute(followee);
        }
    }

    /**
     * Gets an instance of the user's profile
     */
    private void getProfile(){
        Context context = SearchResultsActivity.this;
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES,Context.MODE_PRIVATE);
        final String userId = reader.getString("userId","");
        ElasticSearchController.GetProfileTask getProfileTask
                = new ElasticSearchController.GetProfileTask();
        getProfileTask.execute(userId);
        try {
            me = getProfileTask.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get profiles with id " + userId + " from async object");
        }

    }


}
