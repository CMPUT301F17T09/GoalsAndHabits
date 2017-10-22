package cmput301f17t09.goalsandhabits;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button myHabits = (Button) findViewById(R.id.MyHabits);
        Button follow = (Button) findViewById(R.id.Follow);
        Button maps = (Button) findViewById(R.id.MapFilters);
        Button history = (Button) findViewById(R.id.HabitHistory);
        myHabits.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MainActivity.this,MyHabits.class);
                startActivity(intent);
            }
        });
        follow.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MainActivity.this,FollowActivity.class);
                startActivity(intent);
            }
        });
        maps.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MainActivity.this,MapFiltersActivity.class);
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(MainActivity.this,HabitHistoryActivity.class);
                startActivity(intent);
            }
        });

    }
}
