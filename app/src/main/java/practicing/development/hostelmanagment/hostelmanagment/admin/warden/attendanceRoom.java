package practicing.development.hostelmanagment.hostelmanagment.admin.warden;

import com.google.firebase.firestore.PropertyName;

public class attendanceRoom {
    String entry;
    String exit;

    public attendanceRoom() {
    }

    public attendanceRoom(String entry, String exit) {
        this.entry = entry;
        this.exit = exit;
    }
 @PropertyName("entry")
    public String getEntry() {
        return entry;
    }


@PropertyName("exit")
    public String getExit() {
        return exit;
    }


}
