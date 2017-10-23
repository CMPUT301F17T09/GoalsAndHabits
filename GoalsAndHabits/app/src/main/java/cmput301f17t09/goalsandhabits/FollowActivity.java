package cmput301f17t09.goalsandhabits;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class FollowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        Button newRequest = (Button) findViewById(R.id.SendRequest);
        Button viewPending = (Button) findViewById(R.id.ViewRequests);
        ListView followList = (ListView) findViewById(R.id.FollowList);

        newRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                //TODO: Create a dialog and prompt user for id to add.
            }
        });

        viewPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                //TODO: Display all requests received and allow user to accept/decline.
            }
        });

        followList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(RESULT_OK);
            }
        });
    }
}
