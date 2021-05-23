package practicing.development.hostelmanagment.hostelmanagment.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import practicing.development.hostelmanagment.R;
import practicing.development.hostelmanagment.hostelmanagment.adapter.grpMessagesAdapter;
import practicing.development.hostelmanagment.hostelmanagment.model.chatMessage;

public class notification extends AppCompatActivity {
    // notification activity for warden to send notification only warden can send messages
    practicing.development.hostelmanagment.hostelmanagment.model.chatMessage chatMessage;
    ArrayList<chatMessage> messages;
    grpMessagesAdapter adapter;
    ImageView sendBtn;
    EditText messageText;
    FirebaseFirestore firebaseFirestore;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        messages = new ArrayList<>();
        adapter = new grpMessagesAdapter(this, messages);
        String senderId = FirebaseAuth.getInstance().getUid();
        recyclerView = findViewById(R.id.grpChatView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        sendBtn = findViewById(R.id.grpSendBtn);
        messageText = findViewById(R.id.grpmessageText);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        readMessages();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageText.getText().toString().length() == 0) {
                    Toast.makeText(notification.this, "CAN'T SEND EMPTY MESSAGES", Toast.LENGTH_SHORT).show();
                } else {
                    // add new message to firesore
                    String mssgText = messageText.getText().toString();
                    Date currTime = Calendar.getInstance().getTime();
                    chatMessage = new chatMessage(mssgText, senderId, currTime.getTime());
                    messageText.setText("");
                    Task<DocumentReference> documentReference = firebaseFirestore.collection("group").
                            add(chatMessage);
                }
            }
        });


    }

    // fetch all the previous messages from firestore databse
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
                    // fetching all the messages and adding them to arraylist of our model class
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