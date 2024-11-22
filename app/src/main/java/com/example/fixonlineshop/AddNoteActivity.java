package com.example.fixonlineshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity implements SensorEventListener {

    private EditText titleEditText;
    private EditText contentEditText;
    private ImageView photoImageView; // ImageView for photo display
    private static final int CAMERA_REQUEST_CODE = 100; // Request code for camera
    private static final int CAMERA_RESULT_CODE = 99; // Result code for camera activity
    private boolean isEditMode = false; // Flag to check if in edit mode
    private int noteId; // Store note ID for editing

    // Sensor-related variables
    private SensorManager sensorManager;
    private float acelVal; // Current acceleration value
    private float acelLast; // Last acceleration value
    private float shake; // Acceleration difference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Initialize UI components
        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        photoImageView = findViewById(R.id.photoImageView); // ImageView for photo
        ImageView saveButton = findViewById(R.id.saveButton);
        Button cameraButton = findViewById(R.id.cameraButton); // Camera button

        // Check if in edit mode
        Intent intent = getIntent();
        if (intent.hasExtra("note_id")) {
            isEditMode = true;
            noteId = intent.getIntExtra("note_id", -1);
            titleEditText.setText(intent.getStringExtra("note_title"));
            contentEditText.setText(intent.getStringExtra("note_content"));
        }

        // Initialize sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;

        // Click listener for save button
        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String content = contentEditText.getText().toString();

            if (!title.isEmpty() && !content.isEmpty()) {
                Note note = new Note(isEditMode ? noteId : 0, title, content);
                DatabaseHelper dbHelper = new DatabaseHelper(this);

                if (isEditMode) {
                    dbHelper.updateNote(note); // Update note
                    Toast.makeText(AddNoteActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.insertNote(note); // Insert new note
                    Toast.makeText(AddNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                }

                // After saving or updating, navigate to Dashboard
                Intent intent1 = new Intent(AddNoteActivity.this, Dashboard.class);
                startActivity(intent1);
                finish(); // Close activity after saving/updating
            } else {
                Toast.makeText(AddNoteActivity.this, "Please fill out both fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Click listener for camera button
        cameraButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data"); // Get bitmap from intent
            if (photo != null) {
                photoImageView.setImageBitmap(photo); // Display photo in ImageView
                contentEditText.setText("Photo taken!"); // Update content EditText
            }
        } else {
            Toast.makeText(this, "Camera Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    // Implement shake detection
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        acelLast = acelVal;
        acelVal = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = acelVal - acelLast;
        shake = shake * 0.9f + delta; // Smoothing

        // Detect shake with a certain threshold
        if (shake > 12) {
            Toast.makeText(this, "Shake detected! Returning to dashboard...", Toast.LENGTH_SHORT).show();
            // Return to Dashboard
            Intent intent = new Intent(AddNoteActivity.this, Dashboard.class);
            startActivity(intent);
            finish(); // Close current activity
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed in this case
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this); // Unregister sensor when activity is paused
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL); // Re-register sensor
    }
}
