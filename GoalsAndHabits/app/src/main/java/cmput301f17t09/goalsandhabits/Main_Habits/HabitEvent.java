package cmput301f17t09.goalsandhabits.Main_Habits;

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import cmput301f17t09.goalsandhabits.Follow_Activity.Comment;

/**
 * Created by atsmith on 10/31/17.
 *
 * This class implements a HabitEvent which contains
 * an optional comment, an encoded photo string, as well as
 * a date on which the event occurred. It also contains
 * a latitude, longitude, habit type, comments, and the
 * number of likes received.
 */
public class HabitEvent implements Serializable{
    private String comment;
    private String photoPath;
    private Date date;
    private String encodedPhoto;                // compressed string representation of the photo
    private Double Lat;
    private Double Long;
    private String habitType;
    private ArrayList<Comment> comments;
    private int likes;

    /**
     * Constructs the habit event object with no comment
     * @param date Event Date
     */
    public HabitEvent(Date date){
        setDate(date);
    }

    /**
     * Constructs the habit event object with a comment
     * @param date Event Date
     * @param comment Event Comment
     */
    public HabitEvent(Date date, String comment){
        setDate(date);
        setComment(comment);
    }

    /**
     * Returns the habit type of this event
     * @return Habit Type
     */
    public String getHabitType() {
        return habitType;
    }

    /**
     * Sets the habit type of this event
     * @param habitType Habit Type
     */
    public void setHabitType(String habitType) {
        this.habitType = habitType;
    }

    /**
     * Returns the date of this event
     * @return Event Date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date of this event
     * @param date Event Date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Returns the comment of this event
     * @return Event Comment
     */
    public String getComment(){
        return comment;
    }

    public String getPhotoPath(){
        return photoPath;
    }

    /**
     * Returns the encoded photo string of this event
     * @return Encoded Photo String
     */
    public String getEncodedPhoto() {
        return encodedPhoto;
    }

    /**
     * Sets the encoded photo string of this event
     * @param encodedPhoto Encoded Photo String
     */
    public void setEncodedPhoto(String encodedPhoto) {
        this.encodedPhoto = encodedPhoto;
    }

    /**
     * Sets the comment for this event
     * @param comment Event Comment
     */
    public void setComment(String comment){
        this.comment = comment;
    }

    public void setPhotoPath(String photoPath){
        this.photoPath = photoPath;
    }

    /**
     * Gets the latitude for this event
     * @return Event Latitude
     */
    public Double getLat() {return Lat;}

    /**
     * Gets the longitude for this event
     * @return Event Longitude
     */
    public Double getLong() {return Long;}

    public void setLat(Double lat){this.Lat=lat;}

    public void setLong(Double Long){this.Long=Long;}

    /**
     * Returns a new Location object based on the event's latitude and longitude
     * @return Event Location
     */
    public Location getLocation() {
        if (this.Lat == null || this.Long == null){
            return null;
        }else{
            Location loc = new Location("");
            loc.setLatitude(this.Lat);
            loc.setLongitude(this.Long);
            return loc;
        }
    }

    /**
     * Sets the event's latitude and longitude based on a given Location object
     * @param location Location
     */
    public void setLocation(Location location) {
        this.Lat = location.getLatitude();
        this.Long = location.getLongitude();
    }
    public String getPhoto() {
        return this.encodedPhoto;
    }

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

    /**
     * Returns a list of the comments associated with this event
     * @return Comments List
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }

    /**
     * Sets a list of comments to be associated with this event
     * @param comments Comments List
     */
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Returns the number of likes this event has received
     * @return Number of Likes
     */
    public int getLikes() {
        return likes;
    }

    /**
     * Sets the number of likes for this event
     * @param likes Number of Likes
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }

    /**
     * Increments like count by one
     */
    public void addLike() {
        this.likes+=1;
    }

    /**
     * Adds a comment to the event's comment list
     * @param comment Comment to be added
     */
    public void addComment(Comment comment) {
        comments.add(comment);
    }

}
