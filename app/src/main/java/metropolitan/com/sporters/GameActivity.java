package metropolitan.com.sporters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
}
