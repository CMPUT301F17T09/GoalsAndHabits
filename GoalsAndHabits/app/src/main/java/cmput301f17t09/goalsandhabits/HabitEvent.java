package cmput301f17t09.goalsandhabits;

/**
 * Created by atsmith on 10/31/17.
 */

public class HabitEvent {
    private Habit habit;
    private String comment;
    private String photoPath;

    public HabitEvent(Habit habit){
        this.habit = habit;
    }

    public Habit getHabit(){
        return habit;
    }

    public String getComment(){
        return comment;
    }

    public String getPhotoPath(){
        return photoPath;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public void setPhotoPath(String photoPath){
        this.photoPath = photoPath;
    }

}
