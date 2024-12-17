package com.example.fixonlineshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    DatabaseHelper db;
    EditText usernameInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

                db = new DatabaseHelper(this);
                usernameInput = findViewById(R.id.username_input);
                passwordInput = findViewById(R.id.password_input);
                Button loginButton = findViewById(R.id.login_btn);
                TextView clickableText = findViewById(R.id.clickable_text);

                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username = usernameInput.getText().toString();
                        String password = passwordInput.getText().toString();

                        boolean isValidLogin = db.checkLogin(username, password);
                        if (isValidLogin) {
                            Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, Dashboard.class); //
                            startActivity(intent);
                            finish(); // Menutup aktivitas login agar tidak bisa kembali ke halaman login
                        } else {
                            Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                        }
                    }
        });

        clickableText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }
}
