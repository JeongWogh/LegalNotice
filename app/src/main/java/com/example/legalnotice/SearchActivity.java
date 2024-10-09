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
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private ImageView homeButton;
    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView searchResultsRecyclerView;
    private PillAdapter pillAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        homeButton = findViewById(R.id.homeButton);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);

        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 홈 버튼 클릭 시 메인 화면으로 이동
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, NextActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 검색 버튼 클릭 시 검색 수행
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchPills(query);
                }
            }
        });
    }

    private void searchPills(String query) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Pill>> call = apiService.searchByName(query);

        call.enqueue(new Callback<List<Pill>>() {
            @Override
            public void onResponse(Call<List<Pill>> call, Response<List<Pill>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pill> pills = response.body();
                    setupRecyclerView(pills);
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

    private void setupRecyclerView(List<Pill> pills) {
        pillAdapter = new PillAdapter(pills, null, new PillAdapter.OnPillAddListener() {
            @Override
            public void onPillAdd(Pill pill) {
                savePillToDatabase(pill);
            }
        }, false); // 검색 화면에서는 삭제 버튼을 표시하지 않으므로 false로 설정

        searchResultsRecyclerView.setAdapter(pillAdapter);
    }

    private void savePillToDatabase(Pill pill) {
        String deviceId = DeviceUtil.getDeviceId(this);
        pill.setUserId(deviceId); // 기기 고유 ID를 사용자 ID로 설정

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.addPill(pill);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("SearchActivity", "약 정보가 DB에 성공적으로 저장되었습니다.");
                    // 토스트 메시지 표시
                    Toast.makeText(SearchActivity.this, "약이 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("SearchActivity", "DB 저장 실패: " + response.code());
                    // 실패 시 토스트 메시지 표시
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
}
