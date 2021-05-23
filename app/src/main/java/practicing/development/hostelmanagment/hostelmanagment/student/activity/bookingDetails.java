package practicing.development.hostelmanagment.hostelmanagment.student.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import practicing.development.hostelmanagment.R;
import practicing.development.hostelmanagment.hostelmanagment.student.adapter.roomAdapter;

import static android.widget.AdapterView.*;

public class bookingDetails extends AppCompatActivity implements PaymentResultListener {

    Spinner spinner;
    Spinner spinner1;
    Button book;
    static String mess = "";
    static String room = "";
    TextView studentName, phnNo, enrollNo, guardianName, year, hostelPrice, feesDisplay;
    static String amount = "";
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    static String roomId;
    static String booked = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        Log.i("idOfUser", studentProfile.userID);
        spinner = (Spinner) findViewById(R.id.spinner);
        studentName = findViewById(R.id.stud_name);
        phnNo = findViewById(R.id.phn_no);
        enrollNo = findViewById(R.id.enroll_no);
        guardianName = findViewById(R.id.guardian_name);
        year = findViewById(R.id.curr_year);
        feesDisplay = findViewById(R.id.feesDisplay);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();
        hostelPrice = findViewById(R.id.text2);
        book = findViewById(R.id.bookButton);
        book.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertBox();
            }
        });
        fetch();
        spinnerDetails();

    }

    // spinner for mess type
    public void spinnerDetails() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(bookingDetails.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.mess));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(arrayAdapter);

        String mess = spinner.getSelectedItem().toString();
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(bookingDetails.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.roomType));
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner1.setAdapter(arrayAdapter1);
        String room = spinner1.getSelectedItem().toString();

        onChange();
    }

    // fetching the data of student from firebase
    public void fetch() {
        DocumentReference documentReference = firebaseFirestore.collection("Students").document(studentProfile.userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d("methodCalled", "called");
                studentName.setText("Name:- " + value.getString("studentName"));

                enrollNo.setText("Roll No:- " + value.getString("studentRollNo"));
                phnNo.setText("Phone No:- " + value.getString("studentPhn"));
                guardianName.setText("Parent Name:- " + value.getString("parentName"));
                year.setText("Year:-" + value.getString("studentYear"));
            }
        });
    }

    // when spinner changed
    public void onChange() {
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // getting amount to be paid
                amount = feesDisplay.getText().toString();
                // messs type
                mess = spinner.getSelectedItem().toString();
                // room type
                room = spinner1.getSelectedItem().toString();
                hostelPrice.setText("Mess" + " " + mess + " " + "Room" + " " + room);
                if (mess.equals("Veg")) {
                    if (room.equals("AC")) {
                        feesDisplay.setText("Rs 70,000");
                    } else {
                        feesDisplay.setText("Rs 50,000");
                    }
                } else {
                    if (room.equals("AC")) {
                        feesDisplay.setText("Rs 90,000");
                    } else {
                        feesDisplay.setText("Rs 70,000");
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // for another spinner
        spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                amount = feesDisplay.getText().toString();
                mess = spinner.getSelectedItem().toString();
                room = spinner1.getSelectedItem().toString();
                hostelPrice.setText("Mess" + " " + mess + " " + "Room" + " " + room);
                if (mess.equals("Veg")) {
                    if (room.equals("AC")) {
                        feesDisplay.setText("Rs 70,000");
                    } else {
                        feesDisplay.setText("Rs 50,000");
                    }
                } else {
                    if (room.equals("AC")) {
                        feesDisplay.setText("Rs 90,000");
                    } else {
                        feesDisplay.setText("Rs 70,000");
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void alertBox() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(bookingDetails.this);
        builder1.setMessage("Room will be booked for whole year");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        makePayment();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
// doing payment using "RAZORPAY PAYMENT GATEWAY"
    private void makePayment() {

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_QzPqw3iecRsWHe");
        /**
         * Set your logo here
         */
        // checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();
            Log.i("size", Integer.toString(feesDisplay.getText().toString().length()));
            int price = Integer.parseInt(feesDisplay.getText().toString().substring(3, 5) +
                    feesDisplay.getText().toString().substring(6)) * 100;
            options.put("name", "Hostel");
            options.put("description", "Reference No. #" + studentProfile.rollNumber);
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", Integer.toString(price));//pass amount in currency subunits
            options.put("prefill.email", studentProfile.email);
            options.put("prefill.contact", studentProfile.mobile);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
        firebaseUpdate();
        studentUpdate();
        RoomAndFees.book.setEnabled(false);
        Intent intent = new Intent(bookingDetails.this, RoomAndFees.class);
        startActivity(intent);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(bookingDetails.this, "error" + s, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(bookingDetails.this, RoomAndFees.class);
        booked = "error";
        startActivity(intent);
    }
// updating data when room get booked in coleection Hostels
    public void firebaseUpdate() {
        DocumentReference roomData = firebaseFirestore.collection("Hostels").document("1 Year")
                .collection("Room").document(roomAdapter.id);
        roomData.update(
                "available", false,
                "enroll No", studentProfile.rollNumber.toString()
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                roomId = roomAdapter.id;
                Toast.makeText(bookingDetails.this, "Room Booked", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(bookingDetails.this, "error " + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
// updating data of student
    public void studentUpdate() {
        DocumentReference documentReference = firebaseFirestore.collection("Students").document(studentProfile.userID);
        documentReference.update(
                "roomBooked", "YES"
                , "roomNo", roomAdapter.id.toString(),
                "mess", mess,
                "room", room,
                "amount", amount
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("roomUpdate", "success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("roomUpdate", "failed");
            }
        });
    }
}
