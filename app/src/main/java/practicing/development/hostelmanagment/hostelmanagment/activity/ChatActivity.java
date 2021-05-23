package practicing.development.hostelmanagment.hostelmanagment.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
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
import practicing.development.hostelmanagment.hostelmanagment.adapter.messagesAdapter;
import practicing.development.hostelmanagment.hostelmanagment.model.chatMessage;

// activity navigated when messages button get pressed
public class ChatActivity extends AppCompatActivity {
    chatMessage chatMessage;
    ArrayList<chatMessage> messages;
    messagesAdapter adapter;
    String senderRoom;
    String recieverRoom;
    ImageView sendBtn;
    EditText messageText;
    FirebaseFirestore firebaseFirestore;
    String recieverId = "";
    RecyclerView recyclerView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messages = new ArrayList<>();

        adapter = new messagesAdapter(this, messages);
        String senderId = FirebaseAuth.getInstance().getUid();
        Log.d("senderId", senderId);
        intent = getIntent();
        Bundle extras = getIntent().getExtras();
        // name of student passed by warden
        String name = extras.getString("name");
        // fireId of student
        String fireId = extras.getString("fireId");
        // if activity is called from student
        if (name.equals("student")) {
            // reciever is warden as there is only one warden for copied its fireId
            recieverId = "0ITDS9FWBGXcikLFWuLUk7frqoZ2";
            // action bar will show warden
            setTitle("WARDEN");

        } // if activity is called by warden
        else {
            // reciever will be student
            recieverId = fireId;
            Log.d("recieverID", "ID:- " + recieverId);
            // action bar will show student
            setTitle(name);

        }
        // back button in action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.chatView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        senderRoom = senderId + recieverId;
        recieverRoom = recieverId + senderId;
        sendBtn = findViewById(R.id.sendBtn);
        messageText = findViewById(R.id.messageText);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        readMessages();
// send the message
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mssgText = messageText.getText().toString();
                Date currTime = Calendar.getInstance().getTime();
                chatMessage = new chatMessage(mssgText, senderId, currTime.getTime());
                messageText.setText("");
                // created chat room from combination of sender id and reciever id it will be unique for different student
                CollectionReference collectionReference = firebaseFirestore.collection("chats").document(senderRoom).collection("messages");
                collectionReference.add(chatMessage).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        // chat room which will store all the reciever message for reciever
                        CollectionReference collectionReference = firebaseFirestore.collection("chats").document(recieverRoom).collection("messages");
                        collectionReference.add(chatMessage).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {

                            }
                        });
                    }
                });
            }
        });
    }

    // fetch messages from firestore
    private void readMessages() {
        // fetching messages which were stored in reiever room
        CollectionReference collectionReference = firebaseFirestore.collection("chats").document(recieverRoom).collection("messages");
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