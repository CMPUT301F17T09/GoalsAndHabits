package cmput301f17t09.goalsandhabits;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by atsmith on 10/31/17.
 */

public class Habit implements Serializable{

    private static final int titleLength = 20;
    private static final int reasonLength = 30;

    private String title;
    private String reason;
    private Date startDate;
    private HashSet<Integer> schedule;


    /**
     * Habit constructor.
     * If title or reason are longer than titleLength or reasonLength respectively they are truncated.
     * @param title
     * @param reason
     * @param startDate
     */
    public Habit(String title, String reason, Date startDate){
        setTitle(title);
        setReason(reason);
        setStartDate(startDate);
    }

    public Habit(String title, String reason, Date startDate, HashSet<Integer> schedule){
        setTitle(title);
        setReason(reason);
        setStartDate(startDate);
        setSchedule(schedule);
    }

    /**
     * Sets the schedule of the habit.
     * The schedule is a hash set of integers which are fields
     * from the Calendar class. IE Calendar.MONDAY, Calendar.TUESDAY, etc.
     * @param schedule
     */
    public void setSchedule(HashSet<Integer> schedule){
        this.schedule = schedule;
    }

    public String getTitle(){
        return title;
    }

    public String getReason() {
        return reason;
    }

    public Date getStartDate(){
        return startDate;
    }

    public void setTitle(String title){
        if (title.length()>titleLength){
            title = title.substring(0,titleLength);
        }
        this.title = title;
    }

    public void setReason(String reason){
        if (reason.length()>reasonLength){
            reason = reason.substring(0,reasonLength);
        }
        this.reason = reason;
    }

    public void setStartDate(Date startDate){
        this.startDate = startDate;
    }

    /**
     * Gets the schedule of the habit.
     * The schedule is a hash set of integers which are fields
     * from the Calendar class. IE Calendar.MONDAY, Calendar.TUESDAY, etc.
     */
    public HashSet<Integer> getSchedule(){
        return this.schedule;
    }
}
