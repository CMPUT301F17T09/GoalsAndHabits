package cmput301f17t09.goalsandhabits.Follow_Activity;

/**
 * Created by chias on 2017-12-04.
 */

public class Comment {

    private String commenter;
    private String commentText;

    public Comment(String commenter, String commentText) {
        this.setCommenter(commenter);
        this.setCommentText(commentText);
    }
    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
