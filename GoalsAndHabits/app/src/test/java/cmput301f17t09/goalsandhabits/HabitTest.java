package cmput301f17t09.goalsandhabits;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.Date;

import cmput301f17t09.goalsandhabits.Main_Habits.Habit;
import cmput301f17t09.goalsandhabits.Main_Habits.MainActivity;

/**
 * Created by chias on 2017-11-13.
 * This tests the Habit class and its ability to be added to the ArrayList for habits
 */

public class HabitTest extends ActivityInstrumentationTestCase2 {

    public HabitTest() {
        super(MainActivity.class);
    }

    public void testAddHabit() {
        ArrayList<Habit> habits = new ArrayList<Habit>();
        Habit habit = new Habit("TestTitle","TestReason", new Date());
        habits.add(habit);
        assertTrue(habits.contains(habit));
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
}
