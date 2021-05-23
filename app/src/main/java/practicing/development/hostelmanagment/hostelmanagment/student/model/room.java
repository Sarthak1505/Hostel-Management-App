package practicing.development.hostelmanagment.hostelmanagment.student.model;

import com.google.firebase.firestore.PropertyName;
 // model class for rooms of hostel
public class room {
    private String room_no;
    private Boolean available;

    public room() {

    }

    public room(String room_no, Boolean available) {
        this.room_no = room_no;
        this.available = available;
    }

    @PropertyName("room_no")
    public String getRoom_no() {
        return room_no;
    }

    @PropertyName("available")
    public Boolean getAvailable() {
        return available;
    }
}
