package practicing.development.hostelmanagment.hostelmanagment.admin.warden;

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
import practicing.development.hostelmanagment.hostelmanagment.student.activity.studentLogin;
import practicing.development.hostelmanagment.hostelmanagment.student.activity.studentProfile;

public class wardenLogin extends AppCompatActivity {
    EditText email, password;
    Button login;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
     // login activity of warden
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warden_login);
        email = findViewById(R.id.userId);
        password = findViewById(R.id.userPassword);
        password.setTransformationMethod(new PasswordTransformationMethod());
        login = findViewById(R.id.loginWarden);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar6);
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
// this password will be already saved in firebase as warden
                firebaseAuth.signInWithEmailAndPassword(getEmail, getPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        login.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(wardenLogin.this, studentList.class);
                            intent.putExtra("activityName", "studentLogin");
                            startActivity(intent);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            login.setVisibility(View.VISIBLE);
                            Toast.makeText(wardenLogin.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}