package practicing.development.hostelmanagment.hostelmanagment.student.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Image;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import practicing.development.hostelmanagment.R;

public class receiptActivity extends AppCompatActivity {
    Button reciept;
    ImageView photo;
    TextView name, phn, roll, room, year, mess, roomType, amount;
    Image image = null;
    Uri uri;
    final int REQUEST_CODE = 1000;
    PdfDocument pdfDoc = null;
    Document doc;
    FirebaseFirestore firebaseFirestore;
    Bitmap bitmap;
    String dest = "";
    ConstraintLayout constraintLayout;
    // read and write permission to save pdf from app in the device
    public String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        reciept = findViewById(R.id.PDF);
        firebaseFirestore = FirebaseFirestore.getInstance();
        photo = findViewById(R.id.imageView3);
        name = findViewById(R.id.recName);
        phn = findViewById(R.id.recPhn);
        roll = findViewById(R.id.recEnroll);
        room = findViewById(R.id.recRoom);
        year = findViewById(R.id.recYear);
        mess = findViewById(R.id.recMess);
        roomType = findViewById(R.id.recRoomType);
        amount = findViewById(R.id.recAmount);
        requestPermissions(permission, REQUEST_CODE);
        reciept.setVisibility(View.VISIBLE);
        name.setText(studentProfile.name);

        dest = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + name.getText().toString() + ".pdf";

        fetch();
        constraintLayout = (ConstraintLayout) findViewById(R.id.layout);
        studentProfile.profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(receiptActivity.this).load(uri).into(photo);
            }
        });

    }

    public void Generate(View view) {
        // asking permissions
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            if (photo.getDrawable() == null) {
                Toast.makeText(receiptActivity.this, "INSERT A PHOTO!!", Toast.LENGTH_SHORT).show();
            } else {

                reciept.setVisibility(View.INVISIBLE);
                bitmap = createBitmapFromView(this, constraintLayout);
                createScreen();
                Toast.makeText(receiptActivity.this, "PDF DOWNLOADED AT " + dest, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(receiptActivity.this, RoomAndFees.class);
                startActivity(intent);
            }

        }
        // if not granted
        else {
            Toast.makeText(this, "PERMISSION NOT GRANTED", Toast.LENGTH_SHORT).show();
            requestPermissions(permission, REQUEST_CODE);
        }

    }
// creating the screen which is shown to user to bitmap
    private Bitmap createBitmapFromView(Context context, View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }
// now saving this bitmap in form of pdf using iText 7
    public void createScreen() {
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            image = new Image(ImageDataFactory.create(imageBytes));
            pdfDoc = new PdfDocument(new PdfWriter(dest));
            doc = new Document(pdfDoc, new PageSize(image.getImageWidth(), image.getImageHeight()));


            pdfDoc.addNewPage(new PageSize(image.getImageWidth(), image.getImageHeight()));
            doc.setBottomMargin(200);
            image.setFixedPosition(1, 0, 0);

            doc.add(image);
            Log.d("destinationOffile", dest);
            doc.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
// fetching the details of student
    public void fetch() {
        DocumentReference documentReference = firebaseFirestore.collection("Students").document(studentProfile.userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                year.setText("Year: " + value.getString("studentYear"));
                mess.setText("Mess Type: " + value.getString("mess"));
                roomType.setText("Room Type: " + value.getString("room"));
                amount.setText("Amount: " + value.getString("amount"));
                phn.setText(value.getString("amount"));
                roll.setText("ROll No: " + value.getString("studentRollNo"));
                room.setText("Room No :- " + value.getString("roomNo"));
            }
        });
    }

}