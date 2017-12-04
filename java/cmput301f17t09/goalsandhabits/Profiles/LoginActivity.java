package cmput301f17t09.goalsandhabits.Profiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import cmput301f17t09.goalsandhabits.ElasticSearch.ElasticSearchController;
import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;
import cmput301f17t09.goalsandhabits.R;


/**
 * This activity allows users to log in using a pre-existing profile with a unique user ID and
 * username. Users can also return to the NewProfileActivity.
 * Note:
 */
public class LoginActivity extends AppCompatActivity {

    private Profile profile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");
        setContentView(R.layout.activity_login);
        final int duration = Toast.LENGTH_SHORT;

        Button login = (Button) findViewById(R.id.login_button);
        Button signup = (Button) findViewById(R.id.signup_button);
        final EditText loginName = (EditText) findViewById(R.id.loginname);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginName.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this,"Please enter your username!",duration).show();
                }
                else {
                    String username = loginName.getText().toString();
                    ArrayList<Profile> matches = new ArrayList<Profile>();
                    ElasticSearchController.GetProfilesTask getProfilesTask
                            = new ElasticSearchController.GetProfilesTask();
                    getProfilesTask.execute(username);
                    try {
                        matches = getProfilesTask.get();
                    }catch (Exception e){
                        Log.i("Error", "Failed to get profiles from async object");
                    }
                    if (matches.size()>0){
                        //Grab the first profile (should only be 1 match anyways)
                        profile = matches.get(0);
                    }
                    if (profile!=null){
                        if (profile.getUserId()!=null) {
                            Context context = LoginActivity.this;
                            final SharedPreferences reader = context.getSharedPreferences(MainActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
                            final SharedPreferences.Editor editor = reader.edit();
                            editor.putString("userId", profile.getUserId());
                            editor.commit();
                        }
                    }
                    finish(); //sends user to MainActivity
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(LoginActivity.this,NewProfileActivity.class);
                finish();
                //startActivity(intent); //sends user back to NewProfileActivity
            }
        });
    }

    @Override
    public void finish(){
        Intent data = new Intent();
        if (profile!=null) {
            data.putExtra(MainActivity.EXTRA_PROFILE_SERIAL, profile);
        }
        setResult(RESULT_OK,data);
        super.finish();
    }
}
