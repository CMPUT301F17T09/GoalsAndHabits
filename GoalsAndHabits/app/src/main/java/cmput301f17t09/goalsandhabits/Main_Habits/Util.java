package cmput301f17t09.goalsandhabits.Main_Habits;

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
}
