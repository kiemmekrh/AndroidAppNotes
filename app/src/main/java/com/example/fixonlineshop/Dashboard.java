package com.example.fixonlineshop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private RecyclerView notesRecyclerView;
    private NotesAdapter notesAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);

        List<Note> allNotes = dbHelper.getAllNotes();
        notesAdapter = new NotesAdapter(allNotes, this);
        notesRecyclerView.setAdapter(notesAdapter);

        findViewById(R.id.addButton).setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, AddNoteActivity.class);
            startActivityForResult(intent, 1); // Start activity for result
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String noteTitle = data.getStringExtra("note_title");
            String noteContent = data.getStringExtra("note_content");

            Note newNote = new Note(0, noteTitle, noteContent);
            boolean isInserted = dbHelper.insertNote(newNote);

            if (isInserted) {
                Toast.makeText(this, "Catatan berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Gagal menambahkan catatan!", Toast.LENGTH_SHORT).show();
            }

            List<Note> updatedNotes = dbHelper.getAllNotes();
            notesAdapter.refreshData(updatedNotes);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Note> updatedNotes = dbHelper.getAllNotes();
        notesAdapter.refreshData(updatedNotes);
    }
}