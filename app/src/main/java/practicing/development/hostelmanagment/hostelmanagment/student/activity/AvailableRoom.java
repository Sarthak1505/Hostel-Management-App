package practicing.development.hostelmanagment.hostelmanagment.student.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import practicing.development.hostelmanagment.R;
import practicing.development.hostelmanagment.hostelmanagment.SpacesItemDecoration;
import practicing.development.hostelmanagment.hostelmanagment.student.model.room;
import practicing.development.hostelmanagment.hostelmanagment.student.adapter.roomAdapter;
// showing list of all hostels whether booked or not
public class AvailableRoom extends AppCompatActivity {
    private FirebaseFirestore db;
    private roomAdapter roomAdapter;
    String data = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_room);
        Intent intent = getIntent();
        data = intent.getExtras().toString();
        db = FirebaseFirestore.getInstance();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = db.collection("Hostels").document("1 Year").collection("Room");

        FirestoreRecyclerOptions<room> options = new FirestoreRecyclerOptions.Builder<room>()
                .setQuery(query, room.class)
                .build();
        roomAdapter = new roomAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(roomAdapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(5));


    }

    @Override
    protected void onStart() {
        super.onStart();
        roomAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        roomAdapter.stopListening();
    }
}