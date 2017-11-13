package cmput301f17t09.goalsandhabits;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Andrew on 10/22/2017.
 */

/**
 * This activity should be called from the main activity upon first launching the app
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

                    finish();
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                Intent intent = new Intent(NewProfileActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }


}
