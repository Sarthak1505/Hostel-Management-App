package practicing.development.hostelmanagment.hostelmanagment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import practicing.development.hostelmanagment.R;
import practicing.development.hostelmanagment.hostelmanagment.model.chatMessage;

// chat adapter used for group chat in this used for sending notification
public class grpMessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<chatMessage> messages;
    // assign values to differentiate between sender and reciever
    private int SEND_MESSAGE = 1;
    private int RECIEVE_MESSAGE = 2;

    // constructor
    public grpMessagesAdapter(Context context, ArrayList<chatMessage> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // if sender is using
        if (viewType == SEND_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.send, parent, false);
            return new SendViewHolder(view);
        }
        // if reciver is using
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.recieve, parent, false);
            return new RecieveViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        chatMessage chatMessage = messages.get(position);
        // if the firebase id which i currently active in the device is equal to sender id then message is send
        if (FirebaseAuth.getInstance().getUid().equals(chatMessage.getSenderId())) {
            return SEND_MESSAGE;
        } else {
            return RECIEVE_MESSAGE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        chatMessage message = messages.get(position);
        if (holder.getClass() == SendViewHolder.class) {
            SendViewHolder viewHolder = (SendViewHolder) holder;
            viewHolder.sendView.setText(message.getMessage());
        } else {
            RecieveViewHolder viewHolder = (RecieveViewHolder) holder;
            viewHolder.recieveView.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {

        return (messages == null) ? 0 : messages.size();
    }

    // view holder for sender
    public class SendViewHolder extends RecyclerView.ViewHolder {
        TextView sendView;

        public SendViewHolder(@NonNull View itemView) {
            super(itemView);
            sendView = itemView.findViewById(R.id.txtMessg);
        }
    }

    // view holder for reciever
    public class RecieveViewHolder extends RecyclerView.ViewHolder {
        TextView recieveView;

        public RecieveViewHolder(@NonNull View itemView) {
            super(itemView);
            recieveView = itemView.findViewById(R.id.txtMessg);
        }
    }
}