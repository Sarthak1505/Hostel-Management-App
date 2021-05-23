package practicing.development.hostelmanagment.hostelmanagment.admin.warden;

import com.google.firebase.firestore.PropertyName;
// model class
// it will be used for fetching student detials
public class studentRoom {
    String studentName;
    String firebaseId;
    String studentRollNo;
    String roomNo;
    String image;

    public studentRoom() {
    }

    public studentRoom(String studentName, String firebaseId, String studentRollNo, String roomNo, String image) {
        this.studentName = studentName;
        this.firebaseId = firebaseId;
        this.studentRollNo = studentRollNo;
        this.roomNo = roomNo;
        this.image = image;
    }
// address of image stored in firestore
    @PropertyName("image")

    public String getImage() {
        return image;
    }

// name
    @PropertyName("studentName")
    public String getStudentName() {
        return studentName;
    }
// student fireID
    @PropertyName("firebaseId")
    public String getFirebaseId() {
        return firebaseId;
    }
// student roll no
    @PropertyName("studentRollNo")
    public String getStudentRollNo() {
        return studentRollNo;
    }
// room bokked by student
    @PropertyName("roomNo")
    public String getRoomNo() {
        return roomNo;
    }
}
