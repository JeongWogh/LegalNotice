package com.example.legalnotice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.legalnotice.adapters.PillAdapter;
import com.example.legalnotice.models.Pill;
import com.example.legalnotice.ApiClient;
import com.example.legalnotice.ApiService;
import com.example.legalnotice.DeviceUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    // 화면 요소 선언
    private ImageView homeButton;
    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView searchResultsRecyclerView;
    private PillAdapter pillAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 뷰 초기화
        homeButton = findViewById(R.id.homeButton);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);

        // RecyclerView에 레이아웃 매니저 설정
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 홈 버튼 클릭 시 메인 화면(NextActivity)으로 이동
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, NextActivity.class);
                startActivity(intent);
                finish(); // 현재 액티비티 종료
            }
        });

        // 검색 버튼 클릭 시 검색 수행
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchPills(query); // 검색 메서드 호출
                }
            }
        });
    }

    // API를 호출하여 약물 정보를 검색하는 메서드
    private void searchPills(String query) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Pill>> call = apiService.searchByName(query);

        call.enqueue(new Callback<List<Pill>>() {
            @Override
            public void onResponse(Call<List<Pill>> call, Response<List<Pill>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pill> pills = response.body();
                    setupRecyclerView(pills); // 검색 결과를 RecyclerView에 표시
                } else {
                    Log.e("SearchActivity", "검색 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Pill>> call, Throwable t) {
                Log.e("SearchActivity", "네트워크 오류: " + t.getMessage());
            }
        });
    }

    // RecyclerView를 설정하는 메서드
    private void setupRecyclerView(List<Pill> pills) {
        // pills가 null일 경우 빈 리스트로 설정하여 RecyclerView에 표시
        pillAdapter = new PillAdapter((pills != null) ? pills : new ArrayList<>(), new PillAdapter.OnPillDeleteListener() {
            @Override
            public void onPillDelete(Pill pill) {
                // 검색 화면에서는 삭제 기능이 필요 없으므로 빈 구현으로 남겨둠
            }
        }, new PillAdapter.OnPillAddListener() {
            @Override
            public void onPillAdd(Pill pill) {
                savePillToDatabase(pill); // 약물 추가 메서드 호출
            }
        }, new PillAdapter.OnPillClickListener() {
            @Override
            public void onPillClick(Pill pill) {
                showPillDetails(pill); // 약물 상세 정보 화면으로 이동
            }
        }, false); // 검색 화면에서는 삭제 버튼을 표시하지 않으므로 false로 설정

        searchResultsRecyclerView.setAdapter(pillAdapter);
    }

    // 검색된 약물을 데이터베이스에 저장하는 메서드
    private void savePillToDatabase(Pill pill) {
        String deviceId = DeviceUtil.getDeviceId(this);
        pill.setUserId(deviceId); // 기기 고유 ID를 사용자 ID로 설정

        // JSON 변환 로그 추가 (디버깅용)
        Gson gson = new Gson();
        String json = gson.toJson(pill);
        Log.d("SearchActivity", "전송할 JSON: " + json);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.addPill(pill);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("SearchActivity", "약 정보가 DB에 성공적으로 저장되었습니다.");
                    Toast.makeText(SearchActivity.this, "약이 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("SearchActivity", "DB 저장 실패: " + response.code());
                    Toast.makeText(SearchActivity.this, "약 추가에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SearchActivity", "네트워크 오류: " + t.getMessage());
                Toast.makeText(SearchActivity.this, "네트워크 오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 약물의 상세 정보를 표시하는 메서드
    private void showPillDetails(Pill pill) {
        Intent intent = new Intent(SearchActivity.this, PillDetailActivity.class);
        intent.putExtra("pill", pill); // Pill 객체를 Intent로 전달
        startActivity(intent);
    }
}
