package metropolitan.com.sporters;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private GoogleMap mMap;
    LocationManager lm;
    private DatabaseReference mDatabaseRef;
    HashMap<String, Marker> markerHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActionBar ab = this.getSupportActionBar();
        if(ab != null)
        {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        lm = (LocationManager) getSystemService (Context.LOCATION_SERVICE);
    }

//    @Override
//    protected void onPostResume()
//    {
//        super.onPostResume();
//        onMapReady(mMap);
//    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        markerHashMap = new HashMap<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("games");
        mDatabaseRef.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                addToMarkers(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
                removeFromMarkers(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            finish();
            return;
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(), lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude())));

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private void addToMarkers(DataSnapshot dataSnapshot)
    {
        Game game = dataSnapshot.getValue(Game.class);
        String gameName = dataSnapshot.getRef().getKey();
        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(game.getLatitude(),game.getLongitude())));
        markerHashMap.put(gameName, marker);
    }

    private void removeFromMarkers(DataSnapshot dataSnapshot)
    {
        String gameName = dataSnapshot.getRef().getKey();
        markerHashMap.get(gameName).remove();
        markerHashMap.remove(gameName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.btn_hostGame:
            {
                Intent i = new Intent(this, HostGameActivity.class);
                startActivity(i);
                return true;
            }
            case android.R.id.home:
            {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            case DialogInterface.BUTTON_POSITIVE:
                            {
                                    FirebaseAuth.getInstance().signOut();
                                    dialog.dismiss();
                                    NavUtils.navigateUpFromSameTask(MapsActivity.this);
                                    return;
                            }
                            case DialogInterface.BUTTON_NEGATIVE:
                            {
                                    dialog.dismiss();
                                    return;
                            }
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setMessage("Are you sure you want to log out?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

            }
        }
        return true;
    }
}
