package practicing.development.hostelmanagment.hostelmanagment.student.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import practicing.development.hostelmanagment.R;

public class QRGenerator extends AppCompatActivity {
    Bitmap bitmap;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    String data = "";
    ImageView qrView;
    Button generate;
    ProgressBar progressBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_generator);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        data = bundle.getString("number");
        Log.d("dataOfQr", data);
        qrView = findViewById(R.id.qrView);
        generate = findViewById(R.id.button3);
        progressBar = findViewById(R.id.progressBar4);
        textView = findViewById(R.id.textView11);
        StorageReference profileRef = storageReference.child("users/" + firebaseAuth.getCurrentUser().getUid() + "/qr.jpg");
       // if alr3eaduy created fetch the qr from storage
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(QRGenerator.this).load(uri).into(qrView);
                progressBar.setVisibility(View.INVISIBLE);
                generate.setVisibility(View.INVISIBLE);
                generate.setEnabled(false);
                textView.setText("USE THIS FOR SCANNING");
            }
        });
    }
// generating the qr
    public void generate(View view) {
        progressBar.setVisibility(View.VISIBLE);
        // encoding the fireId of user
        QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 500);
        qrgEncoder.setColorBlack(Color.BLACK);
        qrgEncoder.setColorWhite(Color.WHITE);
        // Getting QR-Code as Bitmap
        bitmap = qrgEncoder.getBitmap();
        StorageReference profileRef = storageReference.child("users/" + firebaseAuth.getCurrentUser().getUid() + "/qr.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        UploadTask uploadTask = profileRef.putBytes(bytes);
        // saving qr in storage in form of byte array
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(QRGenerator.this).load(uri).into(qrView);
                        progressBar.setVisibility(View.INVISIBLE);
                        generate.setVisibility(View.INVISIBLE);
                        generate.setEnabled(false);
                        textView.setText("USE THIS FOR SCANNING");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QRGenerator.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QRGenerator.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}