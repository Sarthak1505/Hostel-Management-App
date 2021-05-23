package practicing.development.hostelmanagment.hostelmanagment.admin.warden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import practicing.development.hostelmanagment.R;
import practicing.development.hostelmanagment.hostelmanagment.activity.ChatActivity;
import practicing.development.hostelmanagment.hostelmanagment.activity.notification;
import practicing.development.hostelmanagment.hostelmanagment.student.activity.studentProfile;

// warden activity
public class wardenActivity extends AppCompatActivity {

    Button mssg, notify;
    String name;
    String fireID;
    String room;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warden);

        mssg = findViewById(R.id.messageStudent);
        notify = findViewById(R.id.notifyButton);
        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        fireID = extras.getString("fireId");
        room = extras.getString("roomBooked");
        firebaseFirestore = FirebaseFirestore.getInstance();
        Log.d("fireId", "Id:- " + fireID);
    }
// message option for warden for one to one chat with the selected student
    public void messageStudent(View view) {
        Intent intent = new Intent(wardenActivity.this, ChatActivity.class);
        Bundle extras = new Bundle();
        extras.putString("name", name);
        extras.putString("fireId", fireID);
        intent.putExtras(extras);
        startActivity(intent);
    }
//  grp chat with students in which only warden can send message thus warden can send important notice
    public void notify(View view) {
        Intent intent = new Intent(wardenActivity.this, notification.class);
        intent.putExtra("name", "warden");
        startActivity(intent);
    }
//warden can remove student from alloted room
    public void remove(View view) {
        // alert box
        AlertDialog.Builder builder = new AlertDialog.Builder(wardenActivity.this);
        builder.setTitle("Are You Sure?");
        builder.setMessage("This will remove the student");
        builder.setPositiveButton(
                "Remove",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    // it will update the data of firestore thus user will be shown that student is removed
                        DocumentReference documentReference = firebaseFirestore.collection("Hostels").document(
                                "1 Year")
                                .collection("Room").document(room);
                        documentReference.update(
                                "available", false,
                                "enroll No", "NA"
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(wardenActivity.this, "STUDENT REMOVE FROM THE BOOKED ROOM", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(wardenActivity.this, "ERROR TRY AGAIN", Toast.LENGTH_SHORT).show();
                            }
                        });

                        dialog.cancel();
                    }
                }
        );
        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void viewProfile(View view) {
        Intent intent  = new Intent(wardenActivity.this, studentProfile.class);
    intent.putExtra("activityName",fireID);
    startActivity(intent);
    }

    public void viewAttendance(View view) {
        Intent intent  = new Intent(wardenActivity.this, viewAttendace.class);
        intent.putExtra("activityName",fireID);
 startActivity(intent);
    }
}