package metropolitan.com.sporters;

import java.util.HashMap;
import java.util.Map;

public class User
{
    private String uid;
    private String username;
    private int gamesPlayed;
    private int bailedGames;

    public User() {}

    public User(String uid, String username, int gamesPlayed, int bailedGames)
    {
        this.uid = uid;
        this.username = username;
        this.gamesPlayed = gamesPlayed;
        this.bailedGames = bailedGames;
    }

    public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("gamesPlayed", gamesPlayed);
        result.put("bailedGames", bailedGames);
        return result;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public int getGamesPlayed()
    {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed)
    {
        this.gamesPlayed = gamesPlayed;
    }

    public int getBailedGames()
    {
        return bailedGames;
    }

    public void setBailedGames(int bailedGames)
    {
        this.bailedGames = bailedGames;
    }
}
