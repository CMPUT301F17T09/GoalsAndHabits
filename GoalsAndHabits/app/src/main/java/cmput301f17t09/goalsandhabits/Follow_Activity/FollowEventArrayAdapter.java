package cmput301f17t09.goalsandhabits.Follow_Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cmput301f17t09.goalsandhabits.Main_Habits.HabitEvent;
import cmput301f17t09.goalsandhabits.R;

public class FollowEventArrayAdapter extends ArrayAdapter<HabitEvent> {

    private Context context;

    public FollowEventArrayAdapter(Context context, ArrayList<HabitEvent> habitEvents){
        super(context, 0, habitEvents);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        HabitEvent h = getItem(position);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.follow_event_list_item, parent, false);
        }
        TextView user = (TextView) convertView.findViewById(R.id.eventUser);
        TextView date = (TextView) convertView.findViewById(R.id.eventDate);
        TextView comment = (TextView) convertView.findViewById(R.id.eventHabit);
        TextView location = (TextView) convertView.findViewById(R.id.eventLocation);
        date.setText(h.getDate().toString());
        if (h.getComment()==null){
            comment.setVisibility(View.INVISIBLE);
        }else{
            comment.setText(h.getComment());
        }
        location.setText("<<Location Here>>");

        return convertView;
    }
}
