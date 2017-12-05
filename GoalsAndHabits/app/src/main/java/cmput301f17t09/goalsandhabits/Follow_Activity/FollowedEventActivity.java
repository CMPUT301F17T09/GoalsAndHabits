package cmput301f17t09.goalsandhabits.Follow_Activity;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cmput301f17t09.goalsandhabits.ElasticSearch.ElasticSearchController;
import cmput301f17t09.goalsandhabits.Main_Habits.HabitEvent;
import cmput301f17t09.goalsandhabits.Main_Habits.ImageController;
import cmput301f17t09.goalsandhabits.Profiles.Profile;
import cmput301f17t09.goalsandhabits.R;

import static cmput301f17t09.goalsandhabits.Follow_Activity.FollowActivity.EXTRA_EVENT_SERIAL;
import static cmput301f17t09.goalsandhabits.Main_Habits.MainActivity.MY_PREFERENCES;


/**
 * Created by chiasson.
 * This activity allows the user to view the latest habit event for a selected habit belonging to a followed user.
 * Users can also like these events or add comments.
 */
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

    /**
     * Displays the comment dialog
     */
    public void showCommentDialog() {
        DialogFragment dialog = AddCommentDialog.newInstance();
        dialog.show(getFragmentManager(), "AddCommentDialog");
    }

    /**
     * Exits out of add comment dialog and add comment to event
     * @param dialog Add Comment Fragment
     * @param commentText Comment text
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String commentText) {
        event.addComment(new Comment(me.getUsername(),commentText));
        commentsArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Exits out of add comment dialog. Makes no changes to activity
     * @param dialog Add Comment Fragment
     */
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

    /**
     * Gets an instance of the user's own profile in order to assign username to comments added
     */
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
}
