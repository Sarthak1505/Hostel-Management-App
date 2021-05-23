package practicing.development.hostelmanagment.hostelmanagment.admin.warden;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import practicing.development.hostelmanagment.R;

public class attendanceAdapter  extends FirestoreRecyclerAdapter<attendanceRoom,attendanceAdapter.attendaceHolder> {
 Context context;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public attendanceAdapter(@NonNull FirestoreRecyclerOptions<attendanceRoom> options, Context context) {
        super(options);
    this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull attendaceHolder attendaceHolder, int i, @NonNull attendanceRoom attendanceRoom) {
        attendaceHolder.entry.setText(attendanceRoom.getEntry());
        attendaceHolder.exit.setText(attendanceRoom.getExit());
    }

    @NonNull
    @Override
    public attendaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendace_list_item, parent, false);
        return new attendanceAdapter.attendaceHolder(v);
    }

    class attendaceHolder extends RecyclerView.ViewHolder {
     TextView entry;
     TextView exit;
    public attendaceHolder(@NonNull View itemView) {
        super(itemView);
   entry = itemView.findViewById(R.id.arrive);
    exit = itemView.findViewById(R.id.depart);
    }
}

}
