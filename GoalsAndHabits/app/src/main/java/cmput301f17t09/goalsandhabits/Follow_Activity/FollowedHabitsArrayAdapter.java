package cmput301f17t09.goalsandhabits.Follow_Activity;

import android.content.Context;
import android.graphics.Color;
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
import cmput301f17t09.goalsandhabits.Main_Habits.HabitEvent;
import cmput301f17t09.goalsandhabits.R;

/**
 * Created by chiasson on 2017-12-03.
 * This ArrayAdapter allows the each habit of each user followed to be displayed in a ListView
 */
public class FollowedHabitsArrayAdapter extends ArrayAdapter<Habit> {
    private Context context;
    private float statThreshold = 50;

    public FollowedHabitsArrayAdapter(Context context, ArrayList<Habit> habits){
        super(context, 0, habits);
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Habit h = getItem(position);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.followed_habits_list_item, parent, false);
        }
        TextView owner = (TextView) convertView.findViewById(R.id.habitOwner);
        TextView title = (TextView) convertView.findViewById(R.id.habitTitle);
        TextView reason = (TextView) convertView.findViewById(R.id.habitReason);
        TextView startDate = (TextView) convertView.findViewById(R.id.habitStartDate);
        TextView textStats = (TextView) convertView.findViewById(R.id.habitStats);
        owner.setText(String.format("%s",h.getProfile())+"'s habit:");
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

    /**
     * Gets the most recent habit event date
     * @param h Habit
     * @return Most recent habit event date
     */
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

    /**
     * Checks if two dates are the same
     * @param c1 Calendar 1
     * @param c2 Calendar 2
     * @return Whether date is the same
     */
    private boolean sameDay(Calendar c1, Calendar c2){
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR));
    }
}
