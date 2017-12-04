package cmput301f17t09.goalsandhabits.Main_Habits;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cmput301f17t09.goalsandhabits.R;

/**
 * Created by Andrew on 12/3/2017.
 */

public class NoNetworkConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network_connection);

        Button button = (Button) findViewById(R.id.buttonExit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
