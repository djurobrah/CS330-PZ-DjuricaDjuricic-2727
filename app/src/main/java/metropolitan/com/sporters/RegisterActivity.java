package metropolitan.com.sporters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener
{
    EditText m_email;
    EditText m_username;
    EditText m_password;
    EditText m_passwordConfirm;
    Button m_btn;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        m_email = findViewById(R.id.registerEmail);
        m_username = findViewById(R.id.registerUsername);
        m_password = findViewById(R.id.registerPass);
        m_passwordConfirm = findViewById(R.id.registerPassConfirm);
        m_btn = findViewById(R.id.btn_confirmRegistration);

        m_btn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users");
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_confirmRegistration:
                if(validRegistrationForm())
                {
                    register(m_email.getText().toString(), m_username.getText().toString(), m_password.getText().toString());
                }
                break;
        }
    }

    private void register(final String email, final String username, final String password)
    {
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                if (!snapshot.hasChild(username))
                {
                    registerUser(email, username, password);
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Username already exists.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void registerUser(String email, final String username, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                            user.updateProfile(profileUpdates);

                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result", mAuth.getCurrentUser().getEmail());
                            setResult(Activity.RESULT_OK, returnIntent);

                            //databaseAddUser(new User(username, 0, 0));

                            FirebaseAuth.getInstance().signOut();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "Authentication failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_CANCELED, returnIntent);
                        }
                    }

                });
    }

//    private void databaseAddUser(User user)
//    {
//        HashMap<String, Object> userMap = user.toMap();
//        DatabaseReference userRef = mDatabaseRef.child(user.getUsername());
//        userRef.updateChildren(userMap);
//    }

    private boolean validRegistrationForm()
    {
        String email = m_email.getText().toString();
        String username = m_username.getText().toString();
        String password = m_password.getText().toString();
        String passwordConfirm = m_passwordConfirm.getText().toString();

        if(email.equals("") || username.equals("") || password.equals("") || passwordConfirm.equals(""))
        {
            Toast.makeText(this, "Please fill out all the fields.", Toast.LENGTH_LONG).show();
            return false;
        }

        Pattern pattern = Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches())
        {
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(!password.equals(passwordConfirm))
        {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(password.length() < 6)
        {
            Toast.makeText(this, "Password must be at least 6 characters long.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


}
