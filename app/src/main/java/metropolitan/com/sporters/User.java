package metropolitan.com.sporters;

import java.util.HashMap;
import java.util.Map;

public class User
{
    private String username;
    public User() {}

    public User(String username)
    {
        this.username = username;
    }

//    public HashMap<String, Object> toMap()
//    {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("gamesPlayed", gamesPlayed);
//        return result;
//    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

}
