package metropolitan.com.sporters;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity implements View.OnClickListener
{
    EditText mSendText;
    Button mBtnSendText;
    String mGameName;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseRef;
    ArrayList<Message> mMessages;
    RecyclerView mRecyclerView;
    ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mSendText = findViewById(R.id.sendText);
        mBtnSendText = findViewById(R.id.btn_sendText);
        mRecyclerView = findViewById(R.id.rView);
        mBtnSendText.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMessages = new ArrayList<>();
        mAdapter = new ListAdapter(this, mMessages);
        mRecyclerView.setAdapter(mAdapter);

        mGameName = getIntent().getStringExtra("gameName");
        Toast.makeText(this, mGameName, Toast.LENGTH_LONG).show();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("messages/" + mGameName);
        mDatabaseRef.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                addMessage(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        ActionBar ab = this.getSupportActionBar();
        if(ab != null)
        {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void addMessage(DataSnapshot dataSnapshot)
    {
        Message message = dataSnapshot.getValue(Message.class);
        mMessages.add(message);
        mAdapter.notifyItemInserted(mAdapter.getItemCount()-1);
        mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_sendText:
            {
                if(!mSendText.getText().toString().equals(""))
                {
                    Map<String, Object> map = new HashMap<>();
                    String randomKey = mDatabaseRef.push().getKey();
                    mDatabaseRef.updateChildren(map);

                    DatabaseReference msgChild = mDatabaseRef.child(randomKey);
                    Map<String, Object> map1 = new Message(mAuth.getCurrentUser().getDisplayName(), mSendText.getText().toString()).toMap();
                    msgChild.updateChildren(map1);

                    mSendText.getText().clear();
                }
                else
                {
                    Toast.makeText(this, "You can't send an empty message", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                openDialog();
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            openDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void openDialog()
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
                        dialog.dismiss();
                        exitGame();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setMessage("Are you sure you want to leave the game?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }

    private void exitGame()
    {
        DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference().child("games/" + mGameName + "/users");
        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                int i = 0;
                for (DataSnapshot child : snapshot.getChildren())
                {
                    if(child.getValue().toString().equals(mAuth.getCurrentUser().getDisplayName()))
                    {
                        if(i == 0)
                        {
                            child.getRef().getParent().getParent().removeValue();
                            mDatabaseRef.removeValue();
                        }
                        else
                        {
                            child.getRef().setValue("z");
                        }

                        Intent intent = new Intent(GameActivity.this, MapsActivity.class);
                        startActivity(intent);
                        return;
                    }
                    i++;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        NavUtils.navigateUpFromSameTask(GameActivity.this);
    }
}
