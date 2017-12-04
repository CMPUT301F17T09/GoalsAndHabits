package cmput301f17t09.goalsandhabits.Follow_Activity;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cmput301f17t09.goalsandhabits.ElasticSearch.ElasticSearchController;
import cmput301f17t09.goalsandhabits.Main_Habits.HabitEvent;
import cmput301f17t09.goalsandhabits.Main_Habits.ImageController;
import cmput301f17t09.goalsandhabits.Profiles.Profile;
import cmput301f17t09.goalsandhabits.R;

import static cmput301f17t09.goalsandhabits.Follow_Activity.FollowActivity.EXTRA_EVENT_SERIAL;
import static cmput301f17t09.goalsandhabits.Main_Habits.MainActivity.MY_PREFERENCES;

public class FollowedEventActivity extends AppCompatActivity implements AddCommentDialog.AddCommentDialogListener {

    private Profile me;
    private HabitEvent event;
    private TextView comment;
    private TextView eventdate;
    private TextView numOfLikes;
    private Button likeButton;
    private Button commentButton;
    private CommentsArrayAdapter commentsArrayAdapter;
    private ArrayList<Comment> eventComments = new ArrayList<Comment>();
    private ListView commentsList;
    private Toolbar toolbar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd");
    private ImageView image;
    private Bitmap imageDisplay;

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
        getProfile();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followed_event);

        likeButton = (Button) findViewById(R.id.like);
        commentButton = (Button) findViewById(R.id.addComment);
        commentsList = (ListView) findViewById(R.id.commentsList);
        image = (ImageView) findViewById(R.id.eventPhoto);
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

        if (event.getComments()==null) {
            event.setComments(new ArrayList<Comment>());
        }
        if (event.getEncodedPhoto()!=null){
            imageDisplay = ImageController.base64ToImage(event.getEncodedPhoto());
            image.setImageBitmap(imageDisplay);
        }else{
            imageDisplay = null;
        }

        eventComments = event.getComments();
        commentsArrayAdapter = new CommentsArrayAdapter(this,eventComments);
        commentsList.setAdapter(commentsArrayAdapter);

        commentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                showCommentDialog();
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

    public void showCommentDialog() {
        DialogFragment dialog = AddCommentDialog.newInstance();
        dialog.show(getFragmentManager(), "AddCommentDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String commentText) {
        event.addComment(new Comment(me.getUsername(),commentText));
        commentsArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Exits out of filter dialog. Makes no changes to activity
     * @param dialog Filter Dialog Fragment
     */
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    private void getProfile(){
        Context context = FollowedEventActivity.this;
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

//    @Override
//    public void finish() {
//        //Pass back the habit and position
//        Intent data = new Intent();
//        if (deleted){
//            data.putExtra(HabitEvent.EXTRA_EVENT_DELETED,true);
//        }
//        data.putExtra(FollowActivity.EXTRA_EVENT_SERIAL, event);
//        setResult(RESULT_OK, data);
//        super.finish();
//    }
}
