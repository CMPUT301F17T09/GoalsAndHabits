package cmput301f17t09.goalsandhabits.Profiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Andrew on 10/22/2017.
 *
 * This class implements the user's profile and maps their user ID and username to their habits and
 * habit events, followers, and the users they follow.
 * Note: as elasticSearch has not been implemented, Profile mapping is not yet possible.
 */

public class Profile {
    private Long userId;
    private String username;

    /**
     * Profile constructor used for testing
     */

    public Profile(){
        this.userId = 0L;
    }

    /**
     * Profile Constructor
     * @param username
     */

    public Profile(String username){
        this.userId = 0L;
        setUsername(username);
        //TODO: check for uniqueness of username, assign unique userID
    }

    /**
     * Gets userID
     * @return userId
     */
    public Long getUserId(){
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
    public void setUserId(Long userId){
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
