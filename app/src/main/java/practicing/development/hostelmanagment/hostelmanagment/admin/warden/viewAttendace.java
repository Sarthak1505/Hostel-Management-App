package practicing.development.hostelmanagment.hostelmanagment.admin.warden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import practicing.development.hostelmanagment.R;

public class viewAttendace extends AppCompatActivity {
    private FirebaseFirestore db;
    attendanceAdapter adapter;
    String fireId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendace);
      Intent intent = getIntent();
      fireId = intent.getStringExtra("activityName");
        db = FirebaseFirestore.getInstance();
        setUpRecyclerView();
    }
    private void setUpRecyclerView() {
        Query query = db.collection("Attendance").document(fireId).collection("data");
        FirestoreRecyclerOptions<attendanceRoom> options = new FirestoreRecyclerOptions.Builder<attendanceRoom>()
                .setQuery(query, attendanceRoom.class)
                .build();
        adapter = new attendanceAdapter(options, viewAttendace.this);
        RecyclerView recyclerView = findViewById(R.id.attendaceRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}