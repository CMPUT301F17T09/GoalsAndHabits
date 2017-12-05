package cmput301f17t09.goalsandhabits.Main_Habits;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import io.searchbox.annotations.JestId;

/**
 * Created by atsmith on 10/31/17.
 * This class implements the Habit objects
 */

public class Habit implements Serializable{

    private static final int titleLength = 20;
    private static final int reasonLength = 30;

    @JestId
    private String id;
    private String title;
    private String reason;
    private Date startDate;
    private HashSet<Integer> schedule;
    private ArrayList<HabitEvent> events;
    private String profile;




    /**
     * Habit constructor.
     * @param title Habit title
     * @param reason Habit reason
     * @param startDate Habit start date
     */
    public Habit(String title, String reason, Date startDate){
        setTitle(title);
        setReason(reason);
        setStartDate(startDate);
        events = new ArrayList<>();
    }

    /**
     * Habit constructor.
     * @param title Habit title
     * @param reason Habit reason
     * @param startDate Habit start date
     * @param schedule Habit Schedule
     */
    public Habit(String title, String reason, Date startDate, HashSet<Integer> schedule){
        setTitle(title);
        setReason(reason);
        setStartDate(startDate);
        setSchedule(schedule);
        events = new ArrayList<>();
    }

    /**
     * Sets the schedule of the habit.
     * The schedule is a hash set of integers which are fields
     * from the Calendar class. IE Calendar.MONDAY, Calendar.TUESDAY, etc.
     * @param schedule Habit schedule
     */
    public void setSchedule(HashSet<Integer> schedule){
        this.schedule = schedule;
    }

    /**
     * Returns the name of the habit object
     * @return Habit title
     */
    public String getTitle(){
        return title;
    }

    /**
     * Returns the username of the owner of the habit
     * @return Profile username
     */
    public String getProfile() {
        return profile;
    }

    /**
     * Sets the profile username of the owner of the habit
     * @param profile Profile username
     */
    public void setProfile(String profile) {
        this.profile = profile;
    }

    /**
     * Returns the reason of the habit object
     * @return Habit reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Returns the start date of the habit object
     * @return Habit startDate
     */
    public Date getStartDate(){
        return startDate;
    }

    /**
     * Sets the name of the habit object. If greater than required length it is truncated.
     * @param title Habit name
     */
    public void setTitle(String title){
        if (title.length()>titleLength){
            title = title.substring(0,titleLength);
        }
        this.title = title;
    }

    /**
     * Set the reason of the habit object. If greater than required length it is truncated.
     * @param reason Habit reason
     */
    public void setReason(String reason){
        if (reason.length()>reasonLength){
            reason = reason.substring(0,reasonLength);
        }
        this.reason = reason;
    }

    /**
     * Sets the the starting date of the habit object.
     * @param startDate Habit startDate
     */
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

    /**
     * Sets the habit events for this habit object
     * @param events Habit Events
     */
    public void setEvents(ArrayList<HabitEvent> events){
        this.events = events;
    }

    /**
     * Returns a list of the habit events for this habit object
     * @return Habit Events
     */
    public ArrayList<HabitEvent> getEvents(){
        return events;
    }

    /**
     * Adds a habit event to this object's list of habit events
     * @param event Habit Event
     */
    public void addHabitEvent(HabitEvent event){
        events.add(event);
    }

    /**
     * Deletes a habit event from this object's list at a given index
     * @param index Index to Remove
     */
    public void deleteHabitEvent(int index){
        events.remove(index);
    }

    /**
     * Deletes a given habit event from this object's list.
     * @param event Habit Event
     */
    public void deleteHabitEvent(HabitEvent event){
        events.remove(event);
    }

    /**
     * Returns this habit object's id number
     * @return Habit ID
     */
    public String getId() { return this.id; }

    /**
     * Sets this habit object's id number
     * @param id Habit ID
     */
    public void setId(String id) { this.id = id; }

    /**
     * Gets the total number of schedule days that have passed since this habit's start date.
     * @return int representing above.
     */
    public int getPossibleEvents(){
        if (startDate==null) return 0;
        if (schedule==null || schedule.isEmpty()) return 0;
        Calendar now = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        start.setTime(this.startDate);
        if (start.after(now)) return 0;
        int span = Util.getDaysBetweenDates(start.getTime(),now.getTime());
        int startDay = start.get(Calendar.DAY_OF_WEEK);
        int events = 0;
        if (span>=7){
            for (int day : schedule){
                if (day>=startDay) events++;
            }
            span-=7;
            events += (span/7)*schedule.size();
        }
        for (int day : schedule){
            if (day<startDay) day+=7;
            if (day >= startDay && day<=(startDay + (span % 7))) events++;
        }
        return events;
    }

    /**
     * Gets the number of events that have been completed.
     * That is, the number of events corresponding to the schedule that
     * have occurred after the habit start date.
     * Ex: Schedule = Mon,Tue,Wed. Events: Mon,Wed,Fri.
     * Would return 2
     * @return number of completed events.
     */
    public int getEventsCompleted(){
        if (events==null || events.isEmpty()) return 0;
        if (schedule==null || schedule.isEmpty()) return 0;
        if (startDate==null) return 0;
        int total = 0;
        Calendar cal = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        for (HabitEvent event : events){
            Date d = event.getDate();
            if (d==null) continue;
            cal.setTime(d);
            if(cal.after(start) && schedule.contains(cal.get(Calendar.DAY_OF_WEEK))){
                total++;
            }
        }
        return total;
    }

    /**
     * Checks to see if any habit events in the object's list appear on the given date.
     * @param date Event Date
     * @return True if any event lands on the given date, false otherwise
     */
    public boolean checkEventExistsOnDate(Date date){
        if (date==null) return true;
        Calendar check = Calendar.getInstance();
        check.setTime(date);
        int day = check.get(Calendar.DAY_OF_YEAR);
        int year = check.get(Calendar.YEAR);
        if (events==null || events.isEmpty()) return false;
        for (HabitEvent event : events){
            Date d = event.getDate();
            if (d==null) continue;
            check.setTime(d);
            if (day==check.get(Calendar.DAY_OF_YEAR) && year==check.get(Calendar.YEAR)){
                return true;
            }
        }
        return false;
    }


}
