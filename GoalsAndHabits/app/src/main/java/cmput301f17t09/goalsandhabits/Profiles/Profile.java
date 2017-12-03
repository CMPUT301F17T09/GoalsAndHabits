package cmput301f17t09.goalsandhabits.Profiles;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import io.searchbox.annotations.JestId;

/**
 * Created by Andrew on 10/22/2017.
 *
 * This class implements the user's profile and maps their user ID and username to their habits and
 * habit events, followers, and the users they follow.
 * Note: as elasticSearch has not been implemented, Profile mapping is not yet possible.
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
        //TODO: check for uniqueness of username, assign unique userID
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

    public ArrayList<Profile> getUsersFollowed() {
        return usersFollowed;
    }

    public void addFollowee(Profile followee) {
        usersFollowed.add(followee);
    }

    public void setUsersFollowed(ArrayList<Profile> usersFollowed) {
        this.usersFollowed = usersFollowed;
    }

    public void setFollowRequests(ArrayList<Profile> followRequests) {
        this.followRequests = followRequests;
    }

    public ArrayList<Profile> getFollowRequests() {
        return followRequests;
    }

    public void addFollowReq(Profile follower) {
        followRequests.add(follower);
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

    public ArrayList<String> getHabitIds() { return this.habitIds; }

    public void setHabitIds(ArrayList<String> habitIds) { this.habitIds = habitIds; }

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

    public void removeHabitId(String habitId){
        if (habitIds==null) return;
        habitIds.remove(habitId);
    }
}
