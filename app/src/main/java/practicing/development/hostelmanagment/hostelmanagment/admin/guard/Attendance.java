package practicing.development.hostelmanagment.hostelmanagment.admin.guard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import practicing.development.hostelmanagment.R;
// this activity will show the result if data gets uploaded in firebase or not scanned is showed when it is succesfull

public class Attendance extends AppCompatActivity {
    Button button;

    boolean entered;
    String resultOfQr;
    FirebaseFirestore firebaseFirestore;
    String currTime;
    public static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        button = findViewById(R.id.scanButton);
        textView = findViewById(R.id.resultOfQr);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
// when called from guardLogin
        if (intent.getStringExtra("result").equals("activity")) {
            textView.setText("");
        }
        // when called from scanQr
        else {
            // result of qr which is fireId of student
            resultOfQr = intent.getStringExtra("result");

            markAttendance();
           
        }
    }

    // onClick
    public void scan(View view) {
        startActivity(new Intent(this, scanQr.class));

    }

    // updating data in firestore
    public void markAttendance() {
        // it goes in document in collection "Attendance"
        DocumentReference documentReference = firebaseFirestore.collection("Attendance").document(resultOfQr);
        Log.d("result", resultOfQr);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // checks the scan count which will be used to determine if the user arrived or departed
                    entered = (boolean) documentSnapshot.get("scanCount");
                    Log.d("countFire", "entered :-"+entered);
                    // if not entered the campus
                    if (!entered) {


                        // we store the data in format
                        // entry:date time
                        Map<String, Object> time = new LinkedHashMap<>();
                        Calendar calander = Calendar.getInstance();
                        int cDay = calander.get(Calendar.DAY_OF_MONTH);
                        int cMonth = calander.get(Calendar.MONTH) + 1;
                        int cYear = calander.get(Calendar.YEAR);
                        String currDate = cDay + ":" + cMonth + ":" + cYear;
                        int cHour = calander.get(Calendar.HOUR);
                        int cMinute = calander.get(Calendar.MINUTE);
                        int cSecond = calander.get(Calendar.SECOND);
                        if(cHour> 12) {
                            cHour = cHour - 12;
                            currTime =cHour + ":" + cMinute + ":" + cSecond+"pm";
                        }
                        else{
                            currTime =cHour + ":" + cMinute + ":" + cSecond+"am";
                        }
                        CollectionReference collectionReference = firebaseFirestore.collection("Attendance").document(resultOfQr)
                                .collection("data");
                        time.put("entry", currDate+" "+currTime);
                        time.put("exit","NA");
                        collectionReference.add(time).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                firebaseUpdate();
                                Log.d("attendance", "success");
                                textView.setText("Scanned");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("attendance", "failure");
                                textView.setText("Error Try Again");

                            }
                        });
                    }
                    // when  user was outside the hostel
                    else {


                        Map<String, Object> time = new LinkedHashMap<>();
                        Calendar calander = Calendar.getInstance();
                        int cDay = calander.get(Calendar.DAY_OF_MONTH);
                        int cMonth = calander.get(Calendar.MONTH) + 1;
                        int cYear = calander.get(Calendar.YEAR);
                        String currDate = cDay + ":" + cMonth + ":" + cYear;
                        int cHour = calander.get(Calendar.HOUR);
                        int cMinute = calander.get(Calendar.MINUTE);
                        int cSecond = calander.get(Calendar.SECOND);
                        if(cHour> 12) {
                            cHour = cHour - 12;
                            currTime =cHour + ":" + cMinute + ":" + cSecond+"pm";
                        }
                        else{
                            currTime =cHour + ":" + cMinute + ":" + cSecond+"am";
                        }
                        CollectionReference collectionReference = firebaseFirestore.collection("Attendance").document(resultOfQr)
                                .collection("data");
                        time.put("exit", currDate +" "+currTime);
                        collectionReference.add(time).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                firebaseUpdate();
                                textView.setText("Scanned");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                textView.setText("Error Try Again");
                            }
                        });

                    }
                } else {
                    Log.d("nullDocument", "null");
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("fireError", e.getMessage());
                    }
                });

    }
// when the collections are successfully created we update the scan count for future use
    public void firebaseUpdate() {
        DocumentReference documentReference = firebaseFirestore.collection("Attendance").document(resultOfQr);
       if (entered){
           entered = false;
       }
       else{
           entered = true;
       }
        documentReference.update(
                "scanCount", entered
        );
        Log.d("countFire","entered :-" +entered);
    }

}