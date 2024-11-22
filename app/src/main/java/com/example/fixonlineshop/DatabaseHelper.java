package com.example.fixonlineshop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "UserDatabase.db";
    public static final int DATABASE_VERSION = 2;

    public static final String USER_TABLE_NAME = "users";
    public static final String COL_2 = "username";
    public static final String COL_3 = "password";

    public static final String NOTE_TABLE_NAME = "notes";
    public static final String NOTE_COL_1 = "ID";       // ID catatan
    public static final String NOTE_COL_2 = "title";    // Judul catatan
    public static final String NOTE_COL_3 = "content";  // Konten catatan

    public static final String PRODUCT_TABLE_NAME = "products"; // Nama tabel produk
    public static final String PRODUCT_COL_1 = "ID";            // ID produk
    public static final String PRODUCT_COL_2 = "name";          // Nama produk
    public static final String PRODUCT_COL_3 = "price";         // Harga produk

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Membuat tabel users dengan id sebagai INTEGER PRIMARY KEY AUTOINCREMENT
        db.execSQL("CREATE TABLE " + USER_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");

        // Membuat tabel catatan
        db.execSQL("CREATE TABLE " + NOTE_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT)");

        // Membuat tabel produk
        db.execSQL("CREATE TABLE " + PRODUCT_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Menangani upgrade jika ada perubahan pada database
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
            onCreate(db);  // Membuat ulang tabel dengan struktur baru
        }
    }

    // Menambahkan user baru
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);  // username
        contentValues.put(COL_3, password);  // password

        long result = db.insert(USER_TABLE_NAME, null, contentValues);
        return result != -1;  // Kembalikan true jika berhasil
    }

    // Mengecek apakah login berhasil
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE_NAME + " WHERE username = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    // Menambahkan catatan baru
    public boolean insertNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COL_2, note.getTitle());
        contentValues.put(NOTE_COL_3, note.getContent());

        long result = db.insert(NOTE_TABLE_NAME, null, contentValues);
        return result != -1; // Kembalikan true jika berhasil
    }

    // Mengambil semua catatan
    public List<Note> getAllNotes() {
        List<Note> notesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NOTE_TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(NOTE_COL_1));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(NOTE_COL_2));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(NOTE_COL_3));
                Note note = new Note(id, title, content);
                notesList.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notesList; // Mengembalikan daftar catatan
    }

    // Menghapus catatan berdasarkan ID
    public boolean deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NOTE_TABLE_NAME, NOTE_COL_1 + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    // Mengupdate catatan
    public boolean updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COL_2, note.getTitle());
        contentValues.put(NOTE_COL_3, note.getContent());

        int result = db.update(NOTE_TABLE_NAME, contentValues, NOTE_COL_1 + "=?", new String[]{String.valueOf(note.getId())});
        return result > 0; // Return true if update was successful
    }

    // Menambahkan produk baru
    public boolean insertProduct(String name, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_COL_2, name);
        contentValues.put(PRODUCT_COL_3, price);

        long result = db.insert(PRODUCT_TABLE_NAME, null, contentValues);
        return result != -1; // Kembalikan true jika berhasil
    }

    // Mengambil semua produk
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + PRODUCT_TABLE_NAME, null);
    }
}
