package practicing.development.hostelmanagment.hostelmanagment.student.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import practicing.development.hostelmanagment.R;

public class RoomAndFees extends AppCompatActivity {
    static Button book;
    Button recieptBtn, leaveBtn;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String isBooked = "no";
    static String roomId = "";
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_and_fees);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        book = findViewById(R.id.book);
        recieptBtn = findViewById(R.id.recieptbtn);
        leaveBtn = findViewById(R.id.leavebtn);
        documentReference = firebaseFirestore.collection("Students").document(studentProfile.userID);
        // getting data of student if room is booked
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                isBooked = value.getString("roomBooked");
                roomId = value.getString("roomNo");
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         // if already booked
                if (isBooked.equals("YES")) {
                    Toast.makeText(RoomAndFees.this, "ROOM ALREADY BOOKED", Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent intent = new Intent(RoomAndFees.this, AvailableRoom.class);
                    intent.putExtra("number", studentProfile.rollNumber);
                    startActivity(intent);
                }
            }
        });
        // for viewig and downloading the reciept
        recieptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if booked show reciept
                if (isBooked.equals("YES")) {
                    Intent intent = new Intent(RoomAndFees.this, receiptActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RoomAndFees.this, "NO ROOM IS BOOKED FROM YOUR ACOOUNT", Toast.LENGTH_LONG).show();
                }
            }
        });
        // option for leaving the room
        leaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 // if no room is booked cant leave
                if (isBooked.equals("NO")) {
                    Toast.makeText(RoomAndFees.this, "NO ROOM IS BOOKED FROM YOUR ACOOUNT", Toast.LENGTH_LONG).show();

                } else {
                    firebaseUpdate();

                }
            }
        });
    }
// updating data when room is leaved
    public void firebaseUpdate() {
        DocumentReference roomData = firebaseFirestore.collection("Hostels").document("1 Year")
                .collection("Room").document(roomId);
        roomData.update(
                "available", true,
                "enroll No", "NA"

        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RoomAndFees.this, "Room Vacated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RoomAndFees.this, "error " + e, Toast.LENGTH_SHORT).show();
            }
        });
        documentReference.update(
                "roomBooked", "NO"
                ,
                "roomNo", "NA"
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("roomandfeesUpdate", "DONE");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("roomandfeesUpdate", "error" + e.getMessage());
            }
        });

    }

}