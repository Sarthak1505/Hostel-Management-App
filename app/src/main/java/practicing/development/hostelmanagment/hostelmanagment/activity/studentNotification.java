package practicing.development.hostelmanagment.hostelmanagment.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import practicing.development.hostelmanagment.R;
import practicing.development.hostelmanagment.hostelmanagment.adapter.grpMessagesAdapter;
import practicing.development.hostelmanagment.hostelmanagment.model.chatMessage;
// activity used by students to recieve notice notifiaction activity is for warden to send messages ans this is for recieving the messages
public class studentNotification extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    ArrayList<chatMessage> messages = new ArrayList<>();
    grpMessagesAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notification);
        adapter = new grpMessagesAdapter(this, messages);
        recyclerView = findViewById(R.id.studentNotify);
        firebaseFirestore = FirebaseFirestore.getInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        readMessages();
        recyclerView.setAdapter(adapter);

    }
// fetching messages send by warden
    private void readMessages() {

        CollectionReference collectionReference = firebaseFirestore.collection("group");
        collectionReference.orderBy("time", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("chatFail", "onEvent: Listen failed.", e);
                    return;
                }
                messages.clear();
                if (documentSnapshots != null) {
                    for (QueryDocumentSnapshot queryDocumentSnapshots : documentSnapshots) {
                        chatMessage chat = queryDocumentSnapshots.toObject(chatMessage.class);
                        messages.add(chat);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

        });
    }
}