package umn.ac.id;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class AwalActivity extends AppCompatActivity {
    Button button;
    private Button btnProfile;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awal);
        btnLogin = findViewById(R.id.button_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AwalActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(v -> startActivity(new Intent(AwalActivity.this, MainActivity.class)));
        btnProfile = findViewById(R.id.button_profile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AwalActivity.this, ProfileActvity.class);
                startActivity(intent);
            }
        });
        btnProfile.setOnClickListener(v -> startActivity(new Intent(AwalActivity.this, ProfileActvity.class)));

    }

}