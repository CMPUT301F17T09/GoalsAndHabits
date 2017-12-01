package cmput301f17t09.goalsandhabits.Follow_Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import cmput301f17t09.goalsandhabits.R;

public class SearchResultsActivity extends AppCompatActivity {

    private String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            search = extras.get("search").toString();
        }
        if (search==null) finish();

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("Searching Users");
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);
    }


}
