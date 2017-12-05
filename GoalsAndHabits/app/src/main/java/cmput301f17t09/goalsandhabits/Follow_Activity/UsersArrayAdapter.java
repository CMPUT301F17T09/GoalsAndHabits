package cmput301f17t09.goalsandhabits.Follow_Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cmput301f17t09.goalsandhabits.Profiles.Profile;
import cmput301f17t09.goalsandhabits.R;

/**
 * Created by chiasson on 2017-12-02.
 * This activity allows an ArrayList of profiles to be displayed in a ListView with their username
 * and their total habit count
 */
public class UsersArrayAdapter extends ArrayAdapter<Profile> {

    private Context context;

    public UsersArrayAdapter(Context context, ArrayList<Profile> profiles){
        super(context, 0, profiles);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Profile p = getItem(position);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_profile_list_item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.searchResultName);
        TextView countText = (TextView) convertView.findViewById(R.id.userHabitCountText);
        TextView countNum = (TextView) convertView.findViewById(R.id.userHabitCount);
        name.setText(p.getUsername());
        countText.setText("Number of Habits:");
        if (p.getHabitIds()!=null){
            countNum.setText(Integer.toString(p.getHabitIds().size()));
        }else{
            countNum.setText("0");
        }

        return convertView;
    }
}
