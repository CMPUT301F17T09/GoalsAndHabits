package cmput301f17t09.goalsandhabits.Profiles;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cmput301f17t09.goalsandhabits.Main_Habits.HabitEvent;
import cmput301f17t09.goalsandhabits.R;

public class MyEventsArrayAdapter extends ArrayAdapter<HabitEvent> {
    private Context context;

    public MyEventsArrayAdapter(Context context, ArrayList<HabitEvent> habitEvents){
        super(context, 0, habitEvents);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        HabitEvent h = getItem(position);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_events_list_item, parent, false);
        }
        TextView habit = (TextView) convertView.findViewById(R.id.eventType);
        TextView comment = (TextView) convertView.findViewById(R.id.eventComment);
        TextView date = (TextView) convertView.findViewById(R.id.eventDate);
        habit.setText(h.getHabitType());
        date.setText(h.getDate().toString());
        if (h.getComment()==null){
            comment.setVisibility(View.INVISIBLE);
        }else{
            comment.setText(h.getComment());
        }


        return convertView;
    }
}
