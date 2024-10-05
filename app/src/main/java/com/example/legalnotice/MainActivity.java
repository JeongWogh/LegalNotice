package com.example.legalnotice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.legalnotice.LegalNoticeData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button agreeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        agreeButton = findViewById(R.id.acceptButton);

        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자 ID와 날짜를 설정
                String userId = "user123"; // 실제 앱에서는 사용자 ID를 가져오는 로직을 구현해야 함
                String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                boolean accepted = true; // '동의' 버튼을 눌렀으므로 true

                // 데이터를 생성
                LegalNoticeData legalNoticeData = new LegalNoticeData(userId, currentDate, accepted);

                // Retrofit을 통해 서버로 데이터 전송
                sendLegalNoticeData(legalNoticeData);
            }
        });
    }

    private void sendLegalNoticeData(LegalNoticeData legalNoticeData) {
        // ApiClient를 통해 Retrofit 인스턴스 가져오기
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // POST 요청
        Call<Void> call = apiService.sendLegalNotice(legalNoticeData);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 성공적으로 전송된 경우
                    Log.d("Retrofit", "데이터 전송 성공");

                    // 다음 화면으로 이동
                    Intent intent = new Intent(MainActivity.this, NextActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // 서버 응답 실패 시 로그
                    Log.e("Retrofit", "응답 오류: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // 요청 자체가 실패한 경우 로그
                Log.e("Retrofit", "요청 실패: " + t.getMessage());
            }
        });
    }
}
