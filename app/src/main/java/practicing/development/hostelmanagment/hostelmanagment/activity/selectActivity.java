package practicing.development.hostelmanagment.hostelmanagment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import practicing.development.hostelmanagment.R;
import practicing.development.hostelmanagment.hostelmanagment.admin.adminLogin;
import practicing.development.hostelmanagment.hostelmanagment.student.activity.signup;
import practicing.development.hostelmanagment.hostelmanagment.student.activity.studentLogin;

public class selectActivity extends AppCompatActivity {
// first activity to be displayed
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
    }
// onClicks
    public void student(View view) {
        Intent intent = new Intent(this, studentLogin.class);
        startActivity(intent);
    }

    public void admin(View view) {
        Intent intent = new Intent(this, adminLogin.class);
        intent.putExtra("result", "selectActivity");
        startActivity(intent);
    }

    public void signup(View view) {
        Intent intent = new Intent(this, signup.class);
        startActivity(intent);

    }
}