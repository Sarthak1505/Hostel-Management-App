package practicing.development.hostelmanagment.hostelmanagment.student.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import practicing.development.hostelmanagment.R;
import practicing.development.hostelmanagment.hostelmanagment.activity.ChatActivity;
import practicing.development.hostelmanagment.hostelmanagment.activity.notification;
import practicing.development.hostelmanagment.hostelmanagment.activity.studentNotification;
 // it will show the profile of student and various options for students
public class studentProfile extends AppCompatActivity {
    TextView nameView, emailView, rollNoView, phnNo, ParentName, ParentNo, Year;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    static String userID;
    Button logout, QR, room, message, notify;
    ImageView edit, profile;
    static String email, mobile, rollNumber, name, year;


    StorageReference storageReference;
    static StorageReference profileRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        Intent intent = getIntent();
        room = findViewById(R.id.feesButton);
        message = findViewById(R.id.messageButton);
        notify = findViewById(R.id.notifyButton);
        nameView = findViewById(R.id.nameView);
        emailView = findViewById(R.id.emailView);
        rollNoView = findViewById(R.id.rollNoView);

        phnNo = findViewById(R.id.phnView);
        progressBar = findViewById(R.id.progressBar3);
        storageReference = FirebaseStorage.getInstance().getReference();
        ParentName = findViewById(R.id.parentView);
        ParentNo = findViewById(R.id.parentNoView);
        Year = findViewById(R.id.yearView);
        edit = findViewById(R.id.editView);
        profile = findViewById(R.id.profileView);
        QR = findViewById(R.id.qrButton);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
       // if the activity is called by signup activity or login activity of student
        if (intent.getStringExtra("activityName").equals("signup") || intent.getStringExtra("activityName").equals("studentLogin")) {
            userID = firebaseAuth.getCurrentUser().getUid();
        }
        // if warden calls this activity to view profile of student
        else {
            // user id will be passed by warden
            userID = intent.getStringExtra("activityName");
            message.setVisibility(View.INVISIBLE);
            room.setVisibility(View.INVISIBLE);
            notify.setVisibility(View.INVISIBLE);
            QR.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);
            profile.setClickable(false);
        }
        profileRef = storageReference.child("users/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");

// fetching image
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                Log.d("url", url);
                Glide.with(studentProfile.this).load(uri).into(profile);
            }
        });
        // fetching data from firestore
        DocumentReference documentReference = firebaseFirestore.collection("Students").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d("methodCalled", "called");
                nameView.setText(value.getString("studentName"));
                name = value.getString("studentName");
                email = value.getString("studentEmail");
                emailView.setText(value.getString("studentEmail"));
                rollNoView.setText("Roll No:" + value.getString("studentRollNo"));
                rollNumber = value.getString("studentRollNo");
                phnNo.setText("Phone No: " + value.getString("studentPhn"));

                mobile = value.getString("studentPhn");
                ParentName.setText("Parent's Name: " + value.getString("parentName"));
                ParentNo.setText("Parent's No: " + value.getString("parentNo"));
                Year.setText("Year: " + value.getString("studentYear"));
                year = value.getString("studentYear");
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickImage, 1000);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickImage, 1000);
            }
        });
        // chat activity with one to one chat with warden
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(studentProfile.this, ChatActivity.class);
                Bundle extras = new Bundle();
                extras.putString("name", "student");
                // intent.putExtra("name",nameView.getText().toString());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        // viewing any notice from warden
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(studentProfile.this, studentNotification.class);

                startActivity(intent);
            }
        });
    }
// opening camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageData = data.getData();
                uploadImage(imageData);
            }
        }
    }
// uploading image
    private void uploadImage(Uri imageData) {
        progressBar.setVisibility(View.VISIBLE);
        StorageReference reference = storageReference.child("users/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
        reference.putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String generatedFilePath = uri.toString();
                        Log.d("url", generatedFilePath);
                        DocumentReference documentReference = firebaseFirestore.collection("Students").document(firebaseAuth.getCurrentUser().getUid());
                        documentReference.update(
                                "image", generatedFilePath
                        );
                        Glide.with(studentProfile.this).load(uri).into(profile);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(studentProfile.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(studentProfile.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

    }



// move to qr activity
    public void qrCode(View view) {

        Intent intent = new Intent(studentProfile.this, QRGenerator.class);
        intent.putExtra("number", userID);
        startActivity(intent);
    }
// for booking of room and payment
    public void fees(View view) {
        Intent intent = new Intent(studentProfile.this, RoomAndFees.class);
        intent.putExtra("number", rollNoView.getText().toString());
        startActivity(intent);
    }
}