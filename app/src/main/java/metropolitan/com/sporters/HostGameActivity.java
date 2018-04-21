package metropolitan.com.sporters;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HostGameActivity extends AppCompatActivity implements View.OnClickListener
{
    EditText mGameName;
    TextView mPlayers;
    SeekBar mSeekBar;
    TextView mAdress;
    Button mBtnLocation;
    Button mBtnHost;

    Place mPlace;

    private DatabaseReference mDatabaseRef;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);

        mGameName = findViewById(R.id.hostName);
        mPlayers = findViewById(R.id.text_hostPlayersNumber);
        mSeekBar = findViewById(R.id.seekBar);
        mAdress = findViewById(R.id.text_hostLocationAddress);
        mBtnLocation = findViewById(R.id.btn_hostLocation);
        mBtnHost = findViewById(R.id.btn_hostGame);

        mBtnLocation.setOnClickListener(this);
        mBtnHost.setOnClickListener(this);

        mPlace = null;

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("games");

        mAuth = FirebaseAuth.getInstance();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                mPlayers.setText(String.valueOf(progress + 2));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        mSeekBar.setMax(8);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_hostLocation:
            {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try
                {
                    startActivityForResult(builder.build(this), 1);
                }
                catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e)
                {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.btn_hostGame:
            {
                if(validForm())
                {
                    createGame();
                }
                break;
            }
        }
    }

    private void createGame()
    {
        String gameName = mGameName.getText().toString();
        int maxPlayers = Integer.parseInt(mPlayers.getText().toString());
        double lat = mPlace.getLatLng().latitude;
        double lng = mPlace.getLatLng().longitude;

        Game game = new Game(gameName, maxPlayers, lat, lng);
        HashMap<String, Object> gameMap = game.toMap();
        HashMap<String, Object> usersMap = game.initializeUsers(mAuth.getCurrentUser().getDisplayName());

        DatabaseReference gameRef = mDatabaseRef.child(gameName);
        gameRef.updateChildren(gameMap);

        DatabaseReference usersRef = gameRef.child("users");
        usersRef.updateChildren(usersMap);

        createMessageGroup(gameName, mAuth.getCurrentUser().getDisplayName());

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("gameName", gameName);
        startActivity(intent);
    }

    private void createMessageGroup(String gameName, String hostName)
    {
        DatabaseReference groupMessageRef = FirebaseDatabase.getInstance().getReference().child("messages/" + gameName);

        Map<String, Object> map = new HashMap<>();
        String randomKey = groupMessageRef.push().getKey();
        groupMessageRef.updateChildren(map);

        DatabaseReference msgChild = groupMessageRef.child(randomKey);
        Map<String, Object> map1 = new Message(hostName,":has created the game!").toMap();
        msgChild.updateChildren(map1);
    }

    private boolean validForm()
    {
        String gameName = mGameName.getText().toString();
        String maxPlayers = mPlayers.getText().toString();
        Place place = mPlace;
        if(gameName.equals("") || maxPlayers.equals("") || place == null)
        {
            Toast.makeText(this, "Please input all necessary data.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                Place place = PlacePicker.getPlace(this, data);
                mAdress.setText(place.getAddress());
                mAdress.setVisibility(View.VISIBLE);

                mPlace = place;
            }
        }
    }
}
