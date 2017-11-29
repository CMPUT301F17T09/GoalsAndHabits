package cmput301f17t09.goalsandhabits.Main_Habits;

import android.location.Location;
import android.util.Log;

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
    private Double Lat;
    private Double Long;

    public HabitEvent(Date date){
        setDate(date);
    }

    public HabitEvent(Date date, String comment){
        setDate(date);
        setComment(comment);
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

    public void setComment(String comment){
        this.comment = comment;
    }

    public void setPhotoPath(String photoPath){
        this.photoPath = photoPath;
    }
    public Double getLat() {return Lat;}
        public Double getLong() {        return Long;}
    public void setLat(Double lat){        this.Lat=lat;}
    public void setLong(Double Long){this.Long=Long;}
    public boolean voidLocation(){
        if( this.getLat()==null ){
            return true;
        }
        return false;
    }
    public void setNullLocation(){
        this.Long=null;
        this.Lat=null;
    }

}
