package metropolitan.com.sporters;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public class Game
{
    private String name;
    private int maxPlayers;
    private double latitude;
    private double longitude;

    public Game() {}

    public Game(String name, int maxPlayers, double latitude, double longitude)
    {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("maxPlayers", maxPlayers);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        return result;
    }

    public HashMap<String, Object> initializeUsers(String host)
    {
        HashMap<String, Object> result = new HashMap<>();
        for (int i = 1; i <= this.maxPlayers; i++)
        {
            if(i == 1)
            {
                result.put(String.valueOf(i), host);
            }
            else
            {
                result.put(String.valueOf(i), "z");
            }
        }
        return result;
    }
}
