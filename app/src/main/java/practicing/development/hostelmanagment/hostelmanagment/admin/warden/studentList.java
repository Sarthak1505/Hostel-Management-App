package practicing.development.hostelmanagment.hostelmanagment.admin.warden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import practicing.development.hostelmanagment.R;
import practicing.development.hostelmanagment.hostelmanagment.SpacesItemDecoration;
// activity6 which display all the students in hostel to warden
public class studentList extends AppCompatActivity {
    private FirebaseFirestore db;
    studentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        db = FirebaseFirestore.getInstance();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = db.collection("Students");
        FirestoreRecyclerOptions<studentRoom> options = new FirestoreRecyclerOptions.Builder<studentRoom>()
                .setQuery(query, studentRoom.class)
                .build();
        adapter = new studentListAdapter(options, studentList.this);
        RecyclerView recyclerView = findViewById(R.id.studentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}