package cmput301f17t09.goalsandhabits.Main_Habits;

import android.location.Location;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by atsmith on 10/31/17.
 *
 * This class implements a HabitEvent which contains
 * an optional comment, a path to a photo, as well as
 * a date on which the event occurred.
 */
public class HabitEvent implements Serializable{
    private String comment;
    private String photoPath;
    private Date date;
    private Location location;

    public HabitEvent(Date date){
        setDate(date);
    }

    public HabitEvent(Date date, String comment,Location location){
        setDate(date);
        setComment(comment);
        setLocation(location);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment(){
        return comment;
    }

    public String getPhotoPath(){
        return photoPath;
    }
    public Location getLocation(){return location;}

    public void setComment(String comment){
        this.comment = comment;
    }

    public void setPhotoPath(String photoPath){
        this.photoPath = photoPath;
    }
    public void setLocation(Location location){this.location = location;}

}
