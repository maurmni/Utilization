package com.example.utilization;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthRepository repo = new AuthRepository(this);

        if (repo.isUserLoggedIn()) {
            openHomeFragment();
        } else {
            openAuthFragment();
        }
    }

    private void openAuthFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new AuthFragment())
                .commit();
    }

    public void openHomeFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new home())
                .commit();
    }
}
