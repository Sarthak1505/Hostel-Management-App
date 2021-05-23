package practicing.development.hostelmanagment.hostelmanagment.student.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import practicing.development.hostelmanagment.R;
import practicing.development.hostelmanagment.hostelmanagment.student.activity.bookingDetails;
import practicing.development.hostelmanagment.hostelmanagment.student.model.room;
// adapter which will display the data from firestore
public class roomAdapter extends FirestoreRecyclerAdapter<room, roomAdapter.roomHolder> {
    //private OnItemClickListener listener;
    public static String id;
    public static String availability = "NA";
    public static String roomNO;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public roomAdapter(@NonNull FirestoreRecyclerOptions<room> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull roomHolder roomHolder, int i, @NonNull room room) {
       // get data using model class
        roomHolder.room_no.setText(room.getRoom_no());
        roomNO = room.getRoom_no().toString();
      // if room is not booked by anyone
        if (room.getAvailable()) {
            roomHolder.vacancy.setText("Available");
            availability = "Available";
            roomHolder.book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(roomHolder.getAdapterPosition());
                    String path = documentSnapshot.getReference().getPath();
                    id = getSnapshots().getSnapshot(i).getId();
                    Log.i("idFirebase", id);
                    Intent intent = new Intent(view.getContext(), bookingDetails.class);
                    view.getContext().startActivity(intent);
                }
            });
        }
        // if it is already occupied
        else {
            roomHolder.vacancy.setText("Not Available");
            roomHolder.book.setVisibility(View.INVISIBLE);
        }
    }

    @NonNull
    @Override
    public roomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
        return new roomHolder(v);
    }
 // view holder
    class roomHolder extends RecyclerView.ViewHolder {
        TextView room_no, vacancy;

        Button book;

        public roomHolder(@NonNull View itemView) {
            super(itemView);
            room_no = itemView.findViewById(R.id.room_no);
            vacancy = itemView.findViewById(R.id.vacancy);
            book = itemView.findViewById(R.id.button4);

        }
    }


}
