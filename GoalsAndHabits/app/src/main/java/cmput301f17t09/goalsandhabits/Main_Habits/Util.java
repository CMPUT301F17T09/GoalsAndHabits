package cmput301f17t09.goalsandhabits.Main_Habits;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Date;

/**
 * Created by Andrew on 11/30/2017.
 *
 * This class contains utility methods used in multiple places throughout the codebase.
 */
public class Util {
    /**
     * Get the number of days between two dates. Make sure 'before' is definitely before 'after'
     * @param before The earlier date in time
     * @param after The later date in time
     * @return The nuumber of days spanning before to after.
     */
    public static int getDaysBetweenDates(Date before, Date after){
        long dif = (after.getTime() - before.getTime());
        return (int) (dif / (1000*60*60*24));
    }

    //adapted from https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    //as of Nov 25, 2017
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
