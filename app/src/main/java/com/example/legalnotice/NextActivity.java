package com.example.legalnotice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        // 개인 정보 관리 화면 이동
        ImageView profileImageView = findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NextActivity.this, PersonalInfoActivity.class);
                startActivity(intent);
            }
        });

        // 복용 약 관리 화면 이동
        ImageView medicationImageView = findViewById(R.id.medicationImageView);
        medicationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NextActivity.this, MedicationManagementActivity.class);
                startActivity(intent);
            }
        });

        // 검색 버튼 클릭 시
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NextActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        // 홈 버튼 클릭 시
        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NextActivity.this, NextActivity.class);
                startActivity(intent);
            }
        });
    }
}
