package cmput301f17t09.goalsandhabits;

import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import cmput301f17t09.goalsandhabits.Main_Habits.Habit;
import cmput301f17t09.goalsandhabits.Main_Habits.HabitEvent;
import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;

/**
 * Created by chias on 2017-11-13.
 * This tests the Habit class and its ability to be added to the ArrayList for habits
 */

public class HabitTest extends TestCase {

    public void testAddHabit() {
        ArrayList<Habit> habits = new ArrayList<Habit>();
        Habit habit = new Habit("TestTitle","TestReason", new Date());
        habits.add(habit);
        assertTrue(habits.contains(habit));
        assertTrue(habit.getTitle().equals("TestTitle"));
        assertTrue(habit.getReason().equals("TestReason"));
    }

    public void testDeleteHabit() {
        //TODO: test delete habit when implemented
    }

    public void testContains() {
        ArrayList<Habit> habits = new ArrayList<Habit>();
        Habit habit = new Habit("TestTitle","TestReason", new Date());
        assertFalse(habits.contains(habit));
        habits.add(habit);
        assertTrue(habits.contains(habit));
    }

    public void testGetHabit() {
        ArrayList<Habit> habits = new ArrayList<Habit>();
        Habit habit = new Habit("TestTitle","TestReason", new Date());
        habits.add(habit);
        Habit returnedHabit = habits.get(0);
        assertEquals(returnedHabit.getReason(),habit.getReason());
        assertEquals(returnedHabit.getTitle(),habit.getTitle());
        assertEquals(returnedHabit.getStartDate(),habit.getStartDate());
    }

    public void testCheckEventExistsOnDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR,1);
        Date today = new Date();
        Habit habit = new Habit("TestTitle","TestReason",today);
        HabitEvent event = new HabitEvent(today);
        habit.addHabitEvent(event);
        assertTrue(habit.checkEventExistsOnDate(today));
        assertFalse(habit.checkEventExistsOnDate(c.getTime()));
    }

    public void testGetEventsCompleted() {
        Habit habit = new Habit("TestTitle","TestReason",new Date());
        HashSet<Integer> schedule = new HashSet<>();
        schedule.add(Calendar.MONDAY);
        schedule.add(Calendar.WEDNESDAY);
        schedule.add(Calendar.FRIDAY);
        habit.setSchedule(schedule);
        Calendar c = Calendar.getInstance();
        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY){
            c.add(Calendar.DAY_OF_YEAR,1);
        }
        c.add(Calendar.DAY_OF_YEAR,7);
        HabitEvent eventOnMonday = new HabitEvent(c.getTime());
        c.add(Calendar.DAY_OF_YEAR,1);
        HabitEvent eventOnTuesday = new HabitEvent(c.getTime());
        c.add(Calendar.DAY_OF_YEAR,-5);
        HabitEvent eventBeforeStartDate = new HabitEvent(c.getTime());
        habit.addHabitEvent(eventOnMonday);
        habit.addHabitEvent(eventOnTuesday);
        habit.addHabitEvent(eventBeforeStartDate);
        assertEquals(1,habit.getEventsCompleted());
    }

    public void testGetPossibleEvents() {
        Habit habit = new Habit("TestTitle","TestReason",new Date());
        HashSet<Integer> schedule = new HashSet<>();
        schedule.add(Calendar.MONDAY);
        schedule.add(Calendar.TUESDAY);
        habit.setSchedule(schedule);
        int back = 27;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR,-back);
        habit.setStartDate(c.getTime());
        int expected = 0;
        for (int i = 0; i<=back;i++){
            if (habit.getSchedule().contains(c.get(Calendar.DAY_OF_WEEK))){
                expected++;
            }
            c.add(Calendar.DAY_OF_YEAR,1);
        }
        assertEquals(expected,habit.getPossibleEvents());
    }
}
