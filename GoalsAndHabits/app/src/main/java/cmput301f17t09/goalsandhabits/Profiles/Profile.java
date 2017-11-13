package cmput301f17t09.goalsandhabits.Profiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Andrew on 10/22/2017.
 */

public class Profile {
    private Long userId;
    private String username;

    public Profile(){
        this.userId = 0L;
    }

    public Profile(String username){
        this.userId = 0L;
        setUsername(username);
    }

    public Long getUserId(){
        return this.userId;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public void setUsername(String username){
        this.username = username;
    }
}
