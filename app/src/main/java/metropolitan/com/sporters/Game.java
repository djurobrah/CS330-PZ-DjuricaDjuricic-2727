package metropolitan.com.sporters;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public class Game
{
    private double latitude;
    private double longitude;
    private int maxPlayers;
    private String name;

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

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public int getMaxPlayers()
    {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers)
    {
        this.maxPlayers = maxPlayers;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "Game{" +
                "name='" + name + '\'' +
                ", maxPlayers=" + maxPlayers +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
