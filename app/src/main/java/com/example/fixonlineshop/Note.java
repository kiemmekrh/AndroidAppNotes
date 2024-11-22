package com.example.fixonlineshop;

public class Note {
    private int id;         // ID untuk catatan
    private String title;   // Judul catatan
    private String content; // Konten catatan

    // Constructor
    public Note(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // Getter untuk ID
    public int getId() {
        return id;
    }

    // Setter untuk ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter untuk Judul
    public String getTitle() {
        return title;
    }

    // Setter untuk Judul
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter untuk Konten
    public String getContent() {
        return content;
    }

    // Setter untuk Konten
    public void setContent(String content) {
        this.content = content;
    }
}
