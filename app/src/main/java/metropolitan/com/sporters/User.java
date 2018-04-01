package metropolitan.com.sporters;

import java.util.HashMap;
import java.util.Map;

public class User
{
    public String uid;
    public String username;

    public User() {}

    public User(String uid, String username)
    {
        this.uid = uid;
        this.username = username;
    }

    public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("username", username);
        return result;
    }
}
