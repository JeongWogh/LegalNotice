package com.example.legalnotice;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class TouchToProceedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_to_proceed);

        // 전체 레이아웃에 터치 이벤트 감지
        View mainLayout = findViewById(R.id.mainLayout);

        // 레이아웃 전체에 터치 이벤트 리스너 설정
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // NextActivity로 이동
                    Intent intent = new Intent(TouchToProceedActivity.this, NextActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }
}
