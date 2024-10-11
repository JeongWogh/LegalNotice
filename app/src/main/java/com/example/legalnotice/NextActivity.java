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

        // 개인 정보 관리 화면으로 이동
        // profileImageView를 클릭하면 PersonalInfoActivity로 이동하는 Intent 설정
        ImageView profileImageView = findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NextActivity.this, PersonalInfoActivity.class);
                startActivity(intent); // PersonalInfoActivity를 실행
            }
        });

        // 복용 약 관리 화면으로 이동
        // medicationImageView를 클릭하면 MedicationManagementActivity로 이동하는 Intent 설정
        ImageView medicationImageView = findViewById(R.id.medicationImageView);
        medicationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NextActivity.this, MedicationManagementActivity.class);
                startActivity(intent); // MedicationManagementActivity를 실행
            }
        });

        // 검색 버튼 클릭 시 검색 화면으로 이동
        // searchButton을 클릭하면 SearchActivity로 이동하는 Intent 설정
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NextActivity.this, SearchActivity.class);
                startActivity(intent); // SearchActivity를 실행
            }
        });

        // 홈 버튼 클릭 시 현재 액티비티를 재실행하는 코드
        // homeButton을 클릭하면 NextActivity를 다시 실행하는 Intent 설정
        // (현재 액티비티와 동일한 액티비티로 이동하기 때문에 의미가 없을 수 있음)
        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // NextActivity를 다시 실행함
                Intent intent = new Intent(NextActivity.this, NextActivity.class);
                startActivity(intent);
                // 현재 액티비티를 종료하지 않으므로 새로운 인스턴스가 계속 생성될 수 있음
            }
        });
    }
}
