package cmput301f17t09.goalsandhabits.Main_Habits;

import android.app.DialogFragment;
import android.location.Location;

/**
 * Created by Andrew on 12/4/2017.
 */

public class NewHabitEventDialog extends DialogFragment {

    private Location currentloc;

    public interface NewHabitEventDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog, Location newloc);
        public void onDialogNegativeClick(DialogFragment dialog);
        public Location onLocButtonClick(DialogFragment dialog);
    }


}
