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
        events = new ArrayList<>();
    }

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

    public void setEvents(ArrayList<HabitEvent> events){
        this.events = events;
    }

    public ArrayList<HabitEvent> getEvents(){
        return events;
    }

    public void addHabitEvent(HabitEvent event){
        events.add(event);
    }

    public void deleteHabitEvent(int index){
        events.remove(index);
    }

    public void deleteHabitEvent(HabitEvent event){
        events.remove(event);
    }

    public String getId() { return this.id; }

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


}
