package practicing.development.hostelmanagment.hostelmanagment.admin.warden;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import practicing.development.hostelmanagment.R;
import practicing.development.hostelmanagment.hostelmanagment.activity.ChatActivity;
import practicing.development.hostelmanagment.hostelmanagment.student.activity.studentProfile;

public class studentListAdapter extends FirestoreRecyclerAdapter<studentRoom, studentListAdapter.studentViewHolder> {
    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public studentListAdapter(@NonNull FirestoreRecyclerOptions<studentRoom> options, Context context) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull studentViewHolder studentViewHolder, int i, @NonNull studentRoom studentRoom) {
          // setting details of student that will be displayed to warden in recycler view
        studentViewHolder.student_Name.setText("Student Name:- " + studentRoom.studentName);
        studentViewHolder.student_Roll_NO.setText("Student Roll No:- " + studentRoom.studentRollNo);
        studentViewHolder.studentRoomBooked.setText("Room Booked:- " + studentRoom.roomNo);
        String imgPath = studentRoom.image;
        Glide.with(context).load(imgPath).into(studentViewHolder.profile);
        // on clicking the card view or the member in recycler view
        studentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, wardenActivity.class);
                Bundle extras = new Bundle();
                extras.putString("name", studentRoom.studentName);
                extras.putString("fireId", studentRoom.firebaseId);
                extras.putString("roomBooked", studentRoom.roomNo);
                intent.putExtras(extras);
                view.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public studentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_item, parent, false);
        return new studentViewHolder(v);
    }


    class studentViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView student_Name;
        TextView student_Roll_NO;
        TextView studentRoomBooked;

        public studentViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.listProfile);
            student_Name = itemView.findViewById(R.id.listName);
            student_Roll_NO = itemView.findViewById(R.id.listRoll);
            studentRoomBooked = itemView.findViewById(R.id.listRoom);

        }
    }
}
