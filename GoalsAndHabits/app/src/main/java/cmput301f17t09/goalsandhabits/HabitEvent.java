package cmput301f17t09.goalsandhabits;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by atsmith on 10/31/17.
 */

public class HabitEvent implements Serializable{
    private String comment;
    private String photoPath;
    private Date date;

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

}
