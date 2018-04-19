package metropolitan.com.sporters;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public class Game
{
    private String name;
    private int maxPlayers;
    //private ArrayList<String> users;
    private double latitude;
    private double longitude;

    public Game() {}

    public Game(String name, int maxPlayers, double latitude, double longitude)
    {
        this.name = name;
        this.maxPlayers = maxPlayers;
        //this.users = users;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("maxPlayers", maxPlayers);
        //result.put("users", users);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        return result;
    }
}
