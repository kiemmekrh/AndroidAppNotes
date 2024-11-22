package com.example.fixonlineshop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.graphics.Bitmap;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Camera extends AppCompatActivity {
    int REQUEST_CODE = 99;
    Button btnSnap;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btnSnap = findViewById(R.id.btncamera);
        imageView = findViewById(R.id.imageview1);

        // Meminta izin kamera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        }

        btnSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, REQUEST_CODE);
                } else {
                    Toast.makeText(Camera.this, "No camera app found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bitmap picTaken = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(picTaken);

            // Kembalikan gambar ke AddNoteActivity
            Intent returnIntent = new Intent();
            returnIntent.putExtra("image", picTaken); // Mengembalikan bitmap
            setResult(RESULT_OK, returnIntent);
            finish(); // Tutup Camera activity
        } else {
            Toast.makeText(this, "Camera Canceled", Toast.LENGTH_SHORT).show();
        }
    }
}
