package cmput301f17t09.goalsandhabits.Main_Habits;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
    private float statThreshold = 50;

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
        TextView textStats = (TextView) convertView.findViewById(R.id.habitStats);
        title.setText(h.getTitle());
        reason.setText(h.getReason());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd", Locale.CANADA);
        startDate.setText(dateFormat.format(h.getStartDate()));
        ImageView imageView = (ImageView) convertView.findViewById(R.id.habitImage);
        float possibleEvents = h.getPossibleEvents();
        float stats = ((h.getEventsCompleted()/possibleEvents)*100);
        if (possibleEvents==0) stats=0;
        textStats.setText(String.format("%.0f",stats) + "%");
        if (stats<=statThreshold){
            textStats.setTextColor(Color.parseColor("#D12121"));
        }else{
            textStats.setTextColor(Color.parseColor("#41C61F"));
        }
        Calendar c = Calendar.getInstance();
        Calendar latest = getLatestHabitEventDate(h);
        if ((h.getSchedule()!=null && h.getSchedule().contains(c.get(Calendar.DAY_OF_WEEK)) && latest!=null) && (h.getEvents()==null || !sameDay(c,latest))){
            //Set alarm icon if the habit is schedule for today and
            //if we either have no events, or the latest event wasn't today:
            imageView.setImageResource(R.drawable.ic_alarm);
        }else {
            //If over half of the possible events have been completed:
            if (stats>statThreshold) {
                imageView.setImageResource(R.drawable.ic_checkmark);
            } else {
                imageView.setImageResource(R.drawable.ic_offtrack);
            }
        }



        return convertView;
    }

    private Calendar getLatestHabitEventDate(Habit h){
        ArrayList<HabitEvent> events = h.getEvents();
        if (events==null) return null;
        if (events.isEmpty()) return null;
        Calendar latest = Calendar.getInstance();
        latest.setTime(events.get(0).getDate());
        if (events.size()==1) return latest;
        Calendar test = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        for (HabitEvent event : events){
            if (event.getDate()==null) continue;
            test.setTime(event.getDate());
            if (test.after(latest)){
                if (!test.after(now)){
                    latest.setTime(test.getTime());
                }
            }
        }
        return latest;
    }

    private boolean sameDay(Calendar c1, Calendar c2){
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR));
    }
}
