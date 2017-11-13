package cmput301f17t09.goalsandhabits.Profiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;
import cmput301f17t09.goalsandhabits.R;

/**
 * Created by Andrew on 10/22/2017.
 *
 * This activity should be called from the main activity upon first launching the app
 * and allows for creation of a new profile or for transfer to a login activity.
 * Note: Not yet able to check for uniqueness of ID or name, test ID is provided for the time being
 *
 */
public class NewProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int duration = Toast.LENGTH_SHORT;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        Button submitButton = (Button) findViewById(R.id.submitButton);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        final EditText userName = (EditText) findViewById(R.id.editUsername);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);

                if (userName.getText().toString().isEmpty()) {
                    Toast.makeText(NewProfileActivity.this,"Please enter a username!",duration).show();
                }
                else {
                    //TODO: Check the username is unique then create a Profile and save it to the online database.
                    Intent intent = new Intent(NewProfileActivity.this, MainActivity.class);
                    finish(); //sends user to MainActivity
                    startActivity(intent);

                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                Intent intent = new Intent(NewProfileActivity.this,LoginActivity.class);
                finish();
                startActivity(intent); //send user to the login activity
            }
        });
    }


}
