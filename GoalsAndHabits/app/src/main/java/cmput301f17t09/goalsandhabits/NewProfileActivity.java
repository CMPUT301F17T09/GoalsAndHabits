package cmput301f17t09.goalsandhabits;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        //Button submitButton = (Button) findViewById(R.id.submitButton);
        //submitButton.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {
                //setResult(RESULT_OK);
                //TODO: Check the username is unique then create a Profile and save it to the online database.
            //}
        //});
    }


}
