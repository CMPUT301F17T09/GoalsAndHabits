package cmput301f17t09.goalsandhabits;

import java.util.Date;

/**
 * Created by atsmith on 10/31/17.
 */

public class Habit {
    private String title;
    private String reason;
    private Date startDate;
    private static final int titleLength = 20;
    private static final int reasonLength = 30;

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
}
