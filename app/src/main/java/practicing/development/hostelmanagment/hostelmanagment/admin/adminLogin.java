package practicing.development.hostelmanagment.hostelmanagment.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import practicing.development.hostelmanagment.R;
import practicing.development.hostelmanagment.hostelmanagment.admin.guard.Attendance;
import practicing.development.hostelmanagment.hostelmanagment.admin.guard.guardLogin;
import practicing.development.hostelmanagment.hostelmanagment.admin.guard.scanQr;
import practicing.development.hostelmanagment.hostelmanagment.admin.warden.wardenActivity;
import practicing.development.hostelmanagment.hostelmanagment.admin.warden.wardenLogin;

public class adminLogin extends AppCompatActivity {
    ImageView guard, warden;
    TextView guardT, wardenT;
    // permission for camera
    public String[] permission = {Manifest.permission.CAMERA};
    // permission request code
    int REQUEST_CODE = 1000;
// camera permission required for qr scanning by guard
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        guard = findViewById(R.id.guardView);
        guardT = findViewById(R.id.guardText);
        warden = findViewById(R.id.wardenView);
        wardenT = findViewById(R.id.wardenText);
        requestPermissions(permission, REQUEST_CODE);
        guard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
                Intent intent = new Intent(adminLogin.this, guardLogin.class);

                startActivity(intent);
            }
        });
        guardT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
                Intent intent = new Intent(adminLogin.this,
                        Attendance.class);
                intent.putExtra("result", "activity");
                startActivity(intent);
            }
        });
        warden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminLogin.this, wardenLogin.class);
                startActivity(intent);
            }
        });
        wardenT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminLogin.this, wardenLogin.class);
                startActivity(intent);
            }
        });
    }

    public void requestPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permission, REQUEST_CODE);
        }

    }
}
