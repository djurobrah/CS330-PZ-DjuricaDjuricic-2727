package metropolitan.com.sporters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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
                registerUser(m_email.getText().toString(), m_password.getText().toString());
                break;
        }
    }

    private void registerUser(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result", mAuth.getCurrentUser().getEmail());
                            setResult(Activity.RESULT_OK, returnIntent);

                            String uuid = mAuth.getCurrentUser().getUid();
                            String username = m_username.getText().toString();
                            databaseAddUser(new User(uuid, username));

                            FirebaseAuth.getInstance().signOut();
                            finish();
                        }
                        else
                            {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }

                });
    }

    private void databaseAddUser(User user)
    {
        HashMap<String, Object> userMap = user.toMap();
        mDatabaseRef.updateChildren(userMap);
    }
}
