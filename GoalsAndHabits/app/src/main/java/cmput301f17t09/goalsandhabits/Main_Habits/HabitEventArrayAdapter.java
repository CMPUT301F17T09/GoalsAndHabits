package cmput301f17t09.goalsandhabits.Main_Habits;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import cmput301f17t09.goalsandhabits.R;

/**
 * Created by Andrew on 11/12/2017.
 *
 * This class extends ArrayAdapter in order to display
 * details of a HabitEvent in a ListView
 */
public class HabitEventArrayAdapter extends ArrayAdapter<HabitEvent> {
    private Context context;

    public HabitEventArrayAdapter(Context context, ArrayList<HabitEvent> habitEvents){
        super(context, 0, habitEvents);
        this.context = context;
    }

    /**
     * Values to deplay in list
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        HabitEvent h = getItem(position);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.habit_event_list_item, parent, false);
        }
        TextView date = (TextView) convertView.findViewById(R.id.eventDate);
        TextView comment = (TextView) convertView.findViewById(R.id.eventComment);
        TextView location = (TextView) convertView.findViewById(R.id.eventLocation);
        ImageView image = (ImageView) convertView.findViewById(R.id.eventPhoto);
        if (h.getEncodedPhoto()!=null && !h.getEncodedPhoto().isEmpty()){
            image.setImageBitmap(ImageController.base64ToImage(h.getEncodedPhoto()));
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd", Locale.CANADA);
        date.setText(dateFormat.format(h.getDate()));
        if (h.getComment()==null){
            comment.setVisibility(View.INVISIBLE);
        }else{
            comment.setText(h.getComment());
        }
        if(h.getLat()!=null && h.getLong() != null){
            location.setText("("+ Location.convert(h.getLat(), Location.FORMAT_DEGREES)+","+Location.convert(h.getLong(), Location.FORMAT_DEGREES)+")");
        }else{
            location.setText("No Location Added");
        }

        return convertView;
    }
}
