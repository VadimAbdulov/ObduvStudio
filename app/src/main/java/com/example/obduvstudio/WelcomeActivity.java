package com.example.obduvstudio;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_welcome);

        Button createProjectButton = findViewById(R.id.createProjectButton);

        createProjectButton.setOnClickListener(v -> {
            // Переход к диалогу ввода названия проекта
            ProjectNameDialog dialog = new ProjectNameDialog();
            dialog.show(getSupportFragmentManager(), "ProjectNameDialog");
        });
    }
}