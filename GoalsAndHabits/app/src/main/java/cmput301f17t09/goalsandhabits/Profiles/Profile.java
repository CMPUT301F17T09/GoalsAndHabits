package cmput301f17t09.goalsandhabits.Profiles;

import java.io.Serializable;
import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 * Created by Andrew on 10/22/2017.
 *
 * This class implements the user's profile and maps their user ID and username to their habits,
 * their followers requests, and the users they follow.
 */

public class Profile implements Serializable{

    @JestId
    private String userId;
    private String username;
    private ArrayList<String> habitIds;
    private ArrayList<Profile> usersFollowed;
    private ArrayList<Profile> followRequests;

    /**
     * Profile Constructor
     * @param username
     */
    public Profile(String username){
        setUsername(username);
    }

    /**
     * Gets userID
     * @return userId
     */
    public String getUserId(){
        return this.userId;
    }

    /**
     * Gets username
     * @return username
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Gets users followed
     * @return Users followed
     */
    public ArrayList<Profile> getUsersFollowed() {
        return usersFollowed;
    }

    /**
     * Adds a user to those followed
     * @param followee New user followed
     */
    public void addFollowee(Profile followee) {
        usersFollowed.add(followee);
    }

    /**
     * Sets users followed
     * @param usersFollowed Users Followed
     */
    public void setUsersFollowed(ArrayList<Profile> usersFollowed) {
        this.usersFollowed = usersFollowed;
    }

    /**
     * Sets follow requests
     * @param followRequests Users requesting to follow
     */
    public void setFollowRequests(ArrayList<Profile> followRequests) {
        this.followRequests = followRequests;
    }

    /**
     * Gets users requesting to follow
     * @return Follow requests
     */
    public ArrayList<Profile> getFollowRequests() {
        return followRequests;
    }

    /**
     * Adds a user requesting to folow
     * @param follower Potential following user
     */
    public void addFollowReq(Profile follower) {
        followRequests.add(follower);
    }

    /**
     * Removes follow request
     * @param follower User requesting to follow
     */
    public void removeFollowReq(Profile follower) {
        followRequests.remove(follower);
    }

    /**
     * Removes follow request
     * @param position User position
     */
    public void removeFollowReq(int position) {
        followRequests.remove(position);
    }

    /**
     * Sets userID
     * @param userId
     */
    public void setUserId(String userId){
        this.userId = userId;
    }

    /**
     * Sets username
     * @param username
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Gets Habit IDs
     * @return Habit IDs
     */
    public ArrayList<String> getHabitIds() { return this.habitIds; }

    /**
     * Sets Habit IDs
     * @param habitIds Habit IDs
     */
    public void setHabitIds(ArrayList<String> habitIds) { this.habitIds = habitIds; }

    /**
     * Adds new habit ID
     * @param habitId Habit ID
     */
    public void addHabitId(String habitId) {
        if (habitIds==null){
            habitIds = new ArrayList<>();
        }
        if (habitId!=null) {
            if (!habitId.isEmpty()) {
                habitIds.add(habitId);
            }
        }
    }

    /**
     * Removes a habit ID
     * @param habitId Habit ID
     */
    public void removeHabitId(String habitId){
        if (habitIds==null) return;
        habitIds.remove(habitId);
    }
}
