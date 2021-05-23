package practicing.development.hostelmanagment.hostelmanagment.student.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import practicing.development.hostelmanagment.R;

// signup activity for student
public class signup extends AppCompatActivity {
    public EditText name, enrollNo, emailId, password, contactNo, parentName, ParentNo, Age, Year;
    TextView register;


    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name = findViewById(R.id.fullname);
        enrollNo = findViewById(R.id.enrollmentNumber);
        emailId = findViewById(R.id.emailId);
        password = findViewById(R.id.password);
        contactNo = findViewById(R.id.PhoneNo);
        parentName = findViewById(R.id.ParentName);
        ParentNo = findViewById(R.id.ParentContact);
        Age = findViewById(R.id.Age);
        Year = findViewById(R.id.Year);
        register = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String getName = name.getText().toString().trim();
                String getEnroll = enrollNo.getText().toString().trim();
                String getId = emailId.getText().toString().trim();
                String getPass = password.getText().toString().trim();
                String getParentName = parentName.getText().toString().trim();
                String getParentNo = ParentNo.getText().toString().trim();
                String getPhn = contactNo.getText().toString().trim();
                String getAge = Age.getText().toString().trim();
                String getYear = Year.getText().toString().trim();
                if (TextUtils.isEmpty(getName)) {
                    name.setError("NAME IS REQUIRED!!");
                    return;
                }
                if (TextUtils.isEmpty(getEnroll)) {
                    enrollNo.setError("ROLLNO IS REQUIRED!!");
                    return;
                }
                if (TextUtils.isEmpty(getId)) {
                    emailId.setError("EMAIL ID IS REQUIRED!!");
                    return;
                }
                if (TextUtils.isEmpty(getPass)) {
                    password.setError("PASSWORD IS REQUIRED!!");
                    return;
                }
                if (getEnroll.length() < 4) {
                    enrollNo.setError("ROLLNO SHOULD BE MORE THAN 4 CHARACTER");
                }

                if (getPass.length() < 6) {
                    password.setError("PASSWORD SHOULD BE MORE THAN 6 CHARACTER");
                    return;
                }
                if (TextUtils.isEmpty(getAge)) {
                    Age.setError("ENTER AGE");
                    return;
                }
                if (TextUtils.isEmpty(getYear)) {
                    Year.setError("ENTER YOUR COLLEGE YEAR");
                    return;
                }
                if (TextUtils.isEmpty(getParentName)) {
                    parentName.setError("ENTER GUARDIAN NAME");
                    return;
                }
                if (TextUtils.isEmpty(getParentNo)) {
                    ParentNo.setError("ENTER GUARDIAN PHN NO");
                    return;
                }
                if (TextUtils.isEmpty(getPhn)) {
                    contactNo.setError("ENTER YOUR NO");
                    return;
                }
                if (getPhn.length() < 10) {
                    contactNo.setError("ENTER VALID NUMBER");
                    return;
                }
                if (getParentNo.length() < 10) {
                    ParentNo.setError("ENTER VALID NUMBER");
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);
                invisible();
              // creating username in firebase with email id
                firebaseAuth.createUserWithEmailAndPassword(getId, getPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(signup.this, "USER CREATED", Toast.LENGTH_SHORT).show();

                            userId = firebaseAuth.getCurrentUser().getUid();
                         // creating collection and document of student in firestore
                            DocumentReference documentReference = firebaseFirestore.collection("Students").document(userId);
                         // this is created for attendace which will be used by guard to store the attendance of particular student in firestore
                            DocumentReference reference = firebaseFirestore.collection("Attendance").document(userId);

                            Map<String, Object> attendance = new HashMap<>();
                            Map<String, Object> student = new HashMap<>();
                            student.put("studentName", getName);
                            student.put("studentRollNo", getEnroll);
                            student.put("studentEmail", getId);
                            student.put("amount", getPhn);
                            student.put("parentName", getParentName);
                            student.put("parentNo", getParentNo);
                            student.put("studentAge", getAge);
                            student.put("studentYear", getYear);
                            student.put("roomBooked", "NO");
                            student.put("roomNo", "NA");
                            student.put("firebaseId", userId);
                            student.put("image", "No Image");
                            student.put("mess", "NA");
                            student.put("room", "NA");
                            student.put("amount", "NA");
                            attendance.put("scanCount", false);
                            documentReference.set(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                  reference.set(attendance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                      @Override
                                      public void onSuccess(Void aVoid) {
                                          Log.d("onSuccess", "Success");
                                      }
                                  });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("onFailure", "Failed: " + e.toString());
                                }
                            });
                            progressBar.setVisibility(View.INVISIBLE);
                            visible();
                            Intent intent = new Intent(signup.this, studentProfile.class);
                            intent.putExtra("activityName", "signup");
                            startActivity(intent);
                        } else {
                            Toast.makeText(signup.this, "ERROR OCCURED", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            visible();
                        }
                    }
                });


            }
        });
    }

    public void invisible() {
        name.setVisibility(View.INVISIBLE);
        enrollNo.setVisibility(View.INVISIBLE);
        emailId.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        contactNo.setVisibility(View.INVISIBLE);
        parentName.setVisibility(View.INVISIBLE);
        ParentNo.setVisibility(View.INVISIBLE);
        Age.setVisibility(View.INVISIBLE);
        Year.setVisibility(View.INVISIBLE);
    }

    public void visible() {
        name.setVisibility(View.VISIBLE);
        enrollNo.setVisibility(View.VISIBLE);
        emailId.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        contactNo.setVisibility(View.VISIBLE);
        parentName.setVisibility(View.VISIBLE);
        ParentNo.setVisibility(View.VISIBLE);
        Age.setVisibility(View.VISIBLE);
        Year.setVisibility(View.VISIBLE);
    }

}