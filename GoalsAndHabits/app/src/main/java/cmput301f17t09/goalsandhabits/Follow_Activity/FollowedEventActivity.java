package cmput301f17t09.goalsandhabits.Follow_Activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cmput301f17t09.goalsandhabits.Main_Habits.HabitEvent;
import cmput301f17t09.goalsandhabits.R;

import static cmput301f17t09.goalsandhabits.Follow_Activity.FollowActivity.EXTRA_EVENT_SERIAL;

public class FollowedEventActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private HabitEvent event;
    private TextView comment;
    private TextView eventdate;
    private TextView numOfLikes;
    private Button likeButton;
    private Button commentButton;
    private CommentsArrayAdapter commentsArrayAdapter;
    private ArrayList<Comment> eventComments = new ArrayList<Comment>();
    private ListView commentsList;
    private Context context;
    private int position;
    private Toolbar toolbar;
    private boolean deleted = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd");
    private Location currentLoc;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        if (extras.containsKey(EXTRA_EVENT_SERIAL)) {
            event = (HabitEvent) extras.getSerializable(EXTRA_EVENT_SERIAL);
        }
        if (event==null) {
            Log.i("Info","Null event");
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followed_event);

        likeButton = (Button) findViewById(R.id.like);
        commentButton = (Button) findViewById(R.id.addComment);
        commentsList = (ListView) findViewById(R.id.commentsList);

        comment = (TextView) findViewById(R.id.eventComment);
        numOfLikes = (TextView) findViewById(R.id.numOfLikes);
        numOfLikes.setText(String.format("Likes: %d",event.getLikes()));

        if (comment == null){return;}
        else{comment.setText(event.getComment());}
        eventdate = (TextView) findViewById(R.id.eventDate);
        eventdate.setText(dateFormat.format(event.getDate()));
        toolbar = (Toolbar) findViewById(R.id.actionbar);
        toolbar.setTitle("Latest Habit Event");
        toolbar.setNavigationIcon(R.drawable.ic_close_button);
        setSupportActionBar(toolbar);

        eventComments = event.getComments();
        commentsArrayAdapter = new CommentsArrayAdapter(this,eventComments);
        commentsList.setAdapter(commentsArrayAdapter);

        commentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                //showCommentDialog();
            }
        });
        likeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                event.addLike();
                numOfLikes.setText(String.format("Likes: %d",event.getLikes()));
            }
        });
    }
}
