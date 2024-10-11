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

        // 메인 레이아웃을 찾아 변수에 저장
        View mainLayout = findViewById(R.id.mainLayout);

        // 메인 레이아웃에 터치 이벤트 리스너를 설정하여 사용자가 화면을 터치하면 이벤트가 발생하도록 함
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 터치가 발생했을 때 (ACTION_DOWN)
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // NextActivity로 이동
                    Intent intent = new Intent(TouchToProceedActivity.this, NextActivity.class);
                    startActivity(intent);
                    // 현재 액티비티를 종료하여 사용자가 뒤로 가기를 눌러도 이전 화면으로 돌아가지 않도록 설정
                    finish();
                }
                // true를 반환하여 이벤트 처리가 완료되었음을 알림
                return true;
            }
        });
    }
}
