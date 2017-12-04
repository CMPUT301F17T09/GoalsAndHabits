package cmput301f17t09.goalsandhabits.Follow_Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cmput301f17t09.goalsandhabits.R;

public class CommentsArrayAdapter extends ArrayAdapter<Comment> {
    private Context context;
    private float statThreshold = 50;

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
