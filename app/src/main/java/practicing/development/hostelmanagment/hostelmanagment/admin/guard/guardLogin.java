package practicing.development.hostelmanagment.hostelmanagment.admin.guard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import practicing.development.hostelmanagment.R;
import practicing.development.hostelmanagment.hostelmanagment.admin.warden.wardenActivity;
import practicing.development.hostelmanagment.hostelmanagment.admin.warden.wardenLogin;
// login activity for guard
public class guardLogin extends AppCompatActivity {
    EditText email, password;
    Button login;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_login);
        email = findViewById(R.id.guardId);
        password = findViewById(R.id.guardPassword);
        password.setTransformationMethod(new PasswordTransformationMethod());
        login = findViewById(R.id.loginguard);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBarGuard);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                login.setVisibility(View.INVISIBLE);
                String getEmail = email.getText().toString();
                String getPass = password.getText().toString();
                if (TextUtils.isEmpty(getEmail)) {
                    email.setError("ENTER EMAIL");
                    return;
                }
                if (TextUtils.isEmpty(getPass)) {
                    password.setError("ENTER PASSWORD");
                    return;
                }
// id is already stored in firebase no need to sign up
                firebaseAuth.signInWithEmailAndPassword(getEmail, getPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        login.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(guardLogin.this, Attendance.class);
                            intent.putExtra("result", "activity");
                            startActivity(intent);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            login.setVisibility(View.VISIBLE);
                            Toast.makeText(guardLogin.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
