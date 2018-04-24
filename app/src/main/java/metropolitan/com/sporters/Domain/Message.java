package metropolitan.com.sporters.Domain;

import java.util.HashMap;

public class Message
{
    private String user;
    private String body;

    public Message() {}

    public Message(String user, String body)
    {
        this.user = user;
        this.body = body;
    }

    public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("body", body);
        return result;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }
}
