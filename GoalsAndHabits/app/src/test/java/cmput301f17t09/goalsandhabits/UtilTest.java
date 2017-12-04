package cmput301f17t09.goalsandhabits;


import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;

import cmput301f17t09.goalsandhabits.Main_Habits.Util;

/**
 * Created by Andrew on 12/4/2017.
 */

public class UtilTest extends TestCase {
    public void testGetDaysBetweenDates(){
        Calendar c = Calendar.getInstance();
        int amount = (int) (Math.random()*31);
        c.add(Calendar.DAY_OF_YEAR,amount);
        Date today = new Date();
        assertEquals(amount, Util.getDaysBetweenDates(today,c.getTime()));
    }
}
