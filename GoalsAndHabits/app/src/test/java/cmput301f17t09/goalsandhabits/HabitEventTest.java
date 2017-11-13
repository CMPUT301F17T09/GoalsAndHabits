package cmput301f17t09.goalsandhabits;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;

import cmput301f17t09.goalsandhabits.Main_Habits.Habit;
import cmput301f17t09.goalsandhabits.Main_Habits.HabitEvent;

/**
 * Created by Ken on 13/11/2017.
 * This class represents a few tests for the habit event model
 */

public class HabitEventTest extends TestCase{
    protected Habit testHabit;

    /**
     * Test to see if creation of a habit event works as intended.
     */
    public void testHabitEventCreation(){
        HabitEvent testEvent = new HabitEvent(new Date(), "testComment");
        assertTrue(testEvent.getComment().equals("testComment"));
    }

    /**
     * Add a habit event to a habit's event list and assert that the addition is correct.
     */
    public void testAddHabitEvent(){
        HabitEvent testEvent = new HabitEvent(new Date(), "testComment");
        testHabit = new Habit("TestTitle", "TestReason", new Date());
        testHabit.addHabitEvent(testEvent);
        assertTrue(testHabit.getEvents().contains(testEvent));
    }

    /**
     * Add a habit event to a habit's event list, then clear the list.
     * Assert that the event no longer exists in the event list.
     */
    public void testSetHabitEvents(){
        HabitEvent testEvent = new HabitEvent(new Date(), "testComment");
        testHabit = new Habit("TestTitle", "TestReason", new Date());
        testHabit.addHabitEvent(testEvent);
        assertTrue(testHabit.getEvents().contains(testEvent));
        ArrayList<HabitEvent> emptyList = new ArrayList<HabitEvent>();
        testHabit.setEvents(emptyList);
        assertFalse(testHabit.getEvents().contains(testEvent));
    }
}
