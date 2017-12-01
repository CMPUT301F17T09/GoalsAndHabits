package cmput301f17t09.goalsandhabits.Follow_Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import cmput301f17t09.goalsandhabits.Profiles.Profile;
import cmput301f17t09.goalsandhabits.R;

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
        countNum.setText(Integer.toString(p.getHabitIds().size()));

        return convertView;
    }
}
