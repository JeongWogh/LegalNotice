package com.example.legalnotice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.legalnotice.models.LegalNoticeData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button agreeButton; // 법적 고지 수락 버튼
    private TextView proceedTextView; // 진행 메시지 텍스트뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 기기의 고유 ID를 가져옴
        String userId = DeviceUtil.getDeviceId(MainActivity.this);

        // 서버에 법적 고지 수락 여부를 확인하는 요청을 보냄
        checkLegalNotice(userId);
    }

    // 서버에 법적 고지 수락 여부를 확인하는 메서드
    private void checkLegalNotice(String userId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.checkLegalNotice(userId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 법적 고지가 이미 수락된 경우 빈 화면을 표시하고 터치 시 다음 액티비티로 이동
                    setContentView(R.layout.activity_touch_to_proceed);

                    proceedTextView = findViewById(R.id.touchMessageTextView);
                    proceedTextView.setOnClickListener(view -> {
                        // NextActivity로 이동
                        Intent intent = new Intent(MainActivity.this, NextActivity.class);
                        startActivity(intent);
                        finish(); // 현재 액티비티를 종료
                    });
                } else {
                    // 법적 고지가 수락되지 않은 경우 법적 고지 화면을 표시
                    setContentView(R.layout.activity_main);

                    agreeButton = findViewById(R.id.acceptButton);

                    agreeButton.setOnClickListener(view -> {
                        // 현재 날짜와 시간을 가져옴
                        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                        boolean accepted = true;

                        // 수락 데이터를 생성하고 서버로 전송
                        LegalNoticeData legalNoticeData = new LegalNoticeData(userId, currentDate, accepted);
                        sendLegalNoticeData(legalNoticeData);
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MainActivity", "네트워크 오류: " + t.getMessage());
                Toast.makeText(MainActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 법적 고지 데이터를 서버로 전송하는 메서드
    private void sendLegalNoticeData(LegalNoticeData legalNoticeData) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.sendLegalNotice(legalNoticeData);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Retrofit", "데이터 전송 성공");
                    // 성공적으로 전송되면 NextActivity로 이동
                    Intent intent = new Intent(MainActivity.this, NextActivity.class);
                    startActivity(intent);
                    finish(); // 현재 액티비티를 종료
                } else {
                    Log.e("Retrofit", "응답 오류: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Retrofit", "요청 실패: " + t.getMessage());
            }
        });
    }
}
