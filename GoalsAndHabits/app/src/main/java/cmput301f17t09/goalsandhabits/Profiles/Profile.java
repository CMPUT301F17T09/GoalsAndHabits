package cmput301f17t09.goalsandhabits.Profiles;

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

public class Profile {
    @JestId
    private String userId;
    private String username;

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
}
