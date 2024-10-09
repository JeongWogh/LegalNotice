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
    private Button agreeButton;
    private TextView proceedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 기기의 고유 ID를 가져오기
        String userId = DeviceUtil.getDeviceId(MainActivity.this);

        // 서버에 법적 고지 수락 여부 확인 요청
        checkLegalNotice(userId);
    }

    private void checkLegalNotice(String userId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.checkLegalNotice(userId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 법적 고지가 이미 수락된 경우 빈 화면을 표시
                    setContentView(R.layout.activity_touch_to_proceed);

                    proceedTextView = findViewById(R.id.touchMessageTextView);
                    proceedTextView.setOnClickListener(view -> {
                        // 다음 액티비티로 이동
                        Intent intent = new Intent(MainActivity.this, NextActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    // 법적 고지가 수락되지 않은 경우
                    setContentView(R.layout.activity_main);

                    agreeButton = findViewById(R.id.acceptButton);

                    agreeButton.setOnClickListener(view -> {
                        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                        boolean accepted = true;

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

    private void sendLegalNoticeData(LegalNoticeData legalNoticeData) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.sendLegalNotice(legalNoticeData);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Retrofit", "데이터 전송 성공");
                    Intent intent = new Intent(MainActivity.this, NextActivity.class);
                    startActivity(intent);
                    finish();
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
