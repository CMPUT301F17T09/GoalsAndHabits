package cmput301f17t09.goalsandhabits.Follow_Activity;

/**
 * Created by chiasson on 2017-12-04.
 * This class implements the comment objects following users may leave on a habit's most recent habit event.
 */

public class Comment {

    private String commenter;
    private String commentText;

    /**
     * Comment constructor
     * @param commenter User commenting
     * @param commentText Text commented
     */
    public Comment(String commenter, String commentText) {
        this.setCommenter(commenter);
        this.setCommentText(commentText);
    }

    /**
     * Gets username of user who commented
     * @return User who commented
     */
    public String getCommenter() {
        return commenter;
    }

    /**
     * Sets username of user who commented
     * @param commenter User who commented
     */
    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    /**
     * Gets comment text
     * @return Comment text
     */
    public String getCommentText() {
        return commentText;
    }

    /**
     * Sets comment text
     * @param commentText Comment text
     */
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
