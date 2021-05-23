package practicing.development.hostelmanagment.hostelmanagment.student.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import practicing.development.hostelmanagment.R;
// login activity for student
public class studentLogin extends AppCompatActivity {
    EditText email, password;
    TextView forget;
    Button login;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        email = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);
        password.setTransformationMethod(new PasswordTransformationMethod());
        forget = findViewById(R.id.forgetPassword);
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar5);
        firebaseAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                login.setVisibility(View.INVISIBLE);
                forget.setVisibility(View.INVISIBLE);
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

                firebaseAuth.signInWithEmailAndPassword(getEmail, getPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            login.setVisibility(View.VISIBLE);
                            forget.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(studentLogin.this, studentProfile.class);
                            intent.putExtra("activityName", "studentLogin");
                            startActivity(intent);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            login.setVisibility(View.VISIBLE);
                            forget.setVisibility(View.VISIBLE);
                            Toast.makeText(studentLogin.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText reset = new EditText(view.getContext());
                final AlertDialog.Builder passwordReset = new AlertDialog.Builder(view.getContext());
                passwordReset.setTitle("Reset Password");
                passwordReset.setMessage("Enter Your Email To Recieve Reset Link");
                passwordReset.setView(reset);
                passwordReset.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract amail and send reset link
                        String resetEmail = reset.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(resetEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(studentLogin.this, "RESET LINK SEND", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(studentLogin.this, "Error " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                passwordReset.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // close dialog

                    }
                });
                passwordReset.create().show();
            }

        });
    }

}