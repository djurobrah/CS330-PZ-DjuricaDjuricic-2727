package metropolitan.com.sporters;

import java.util.HashMap;
import java.util.Map;

public class User
{
    public String username;
    public int gamesPlayed;
    public int bailedGames;

    public User() {}

    public User(String username, int gamesPlayed, int bailedGames)
    {
        this.username = username;
        this.gamesPlayed = gamesPlayed;
        this.bailedGames = bailedGames;
    }

    public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("gamesPlayed", gamesPlayed);
        result.put("bailedGames", bailedGames);
        return result;
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
