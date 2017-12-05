package cmput301f17t09.goalsandhabits.Follow_Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cmput301f17t09.goalsandhabits.R;

/**
 * Created by chiasson on 2017-12-04.
 * This ArrayAdapter allows comments on a habit event to be displayed in a ListView
 */
public class CommentsArrayAdapter extends ArrayAdapter<Comment> {
    private Context context;

    public CommentsArrayAdapter(Context context, ArrayList<Comment> comments){
        super(context, 0, comments);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Comment c = getItem(position);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comments_list_item, parent, false);
        }
        TextView commenter = (TextView) convertView.findViewById(R.id.commenter);
        TextView commentText = (TextView) convertView.findViewById(R.id.comment);
        commenter.setText(c.getCommenter());
        commentText.setText(c.getCommentText());
        return convertView;
    }
}
