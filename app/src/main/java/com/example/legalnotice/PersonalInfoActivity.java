package com.example.legalnotice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class PersonalInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        ImageButton homeButton = findViewById(R.id.homeButton);
        EditText ageEditText = findViewById(R.id.ageEditText);
        RadioGroup genderRadioGroup = findViewById(R.id.genderRadioGroup);
        Switch pregnantSwitch = findViewById(R.id.pregnantSwitch);
        Switch nursingSwitch = findViewById(R.id.nursingSwitch);
        EditText allergyEditText = findViewById(R.id.allergyEditText);
        Button resetButton = findViewById(R.id.resetButton);

        // 홈 버튼 클릭 시 첫 화면으로 돌아가기
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalInfoActivity.this, NextActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 초기화 버튼 클릭 시 모든 입력 초기화
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ageEditText.setText("");
                genderRadioGroup.clearCheck();
                pregnantSwitch.setChecked(false);
                nursingSwitch.setChecked(false);
                allergyEditText.setText("");
            }
        });
    }
}
