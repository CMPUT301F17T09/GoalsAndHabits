package cmput301f17t09.goalsandhabits.Main_Habits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cmput301f17t09.goalsandhabits.Main_Habits.Habit;
import cmput301f17t09.goalsandhabits.R;

/**
 * Created by Andrew on 11/6/2017.
 *
 * This class extends ArrayAdapter in order to properly display the details
 * of a habit in a ListView
 */
public class HabitArrayAdapter extends ArrayAdapter<Habit> {
    private Context context;

    public HabitArrayAdapter(Context context, ArrayList<Habit> habits){
        super(context, 0, habits);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Habit h = getItem(position);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.habit_list_item, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.habitTitle);
        TextView reason = (TextView) convertView.findViewById(R.id.habitReason);
        TextView startDate = (TextView) convertView.findViewById(R.id.habitStartDate);
        title.setText(h.getTitle());
        reason.setText(h.getReason());
        startDate.setText(h.getStartDate().toString());
        ImageView imageView = (ImageView) convertView.findViewById(R.id.habitImage);
        int missed = h.getEventsMissed();
        if (missed==0){
            imageView.setImageResource(R.drawable.ic_checkmark);
        }else{
            imageView.setImageResource(R.drawable.ic_offtrack);
        }


        return convertView;
    }
}
