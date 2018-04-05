package metropolitan.com.sporters;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karan.churi.PermissionManager.PermissionManager;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity implements OnClickListener
{
    EditText m_email;
    EditText m_password;
    Button m_login;
    Button m_register;

    private FirebaseAuth mAuth;

    PermissionManager permission;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        m_email = findViewById(R.id.loginEmail);
        m_password = findViewById(R.id.loginPass);
        m_login = findViewById(R.id.btn_login);
        m_register = findViewById(R.id.btn_register);

        m_login.setOnClickListener(this);
        m_register.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        permission = new PermissionManager(){};
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_login:
            {
                if(validLoginForm())
                {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        login(m_email.getText().toString(), m_password.getText().toString());
                        return;
                    }

                    ArrayList<String> granted = permission.getStatus().get(0).granted;
                    if(!granted.contains("android.permission.ACCESS_FINE_LOCATION"))
                    {
                        permission.checkAndRequestPermissions(this);
                    }
                }
                break;
            }
            case R.id.btn_register:
            {
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, 1);
                break;
            }
        }
    }

    private boolean validLoginForm()
    {
        String email = m_email.getText().toString();
        String password = m_password.getText().toString();
        if(email.equals("") || password.equals(""))
        {
            Toast.makeText(this, "Please fill out all the fields.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void login(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Login failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                String result = data.getStringExtra("result");
                m_email.setText(result);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults)
    {
        permission.checkResult(requestCode,permissions, grantResults);
        ArrayList<String> granted = permission.getStatus().get(0).granted;
        if(granted.contains("android.permission.ACCESS_FINE_LOCATION"))
        {
            login(m_email.getText().toString(), m_password.getText().toString());
        }
    }
}
