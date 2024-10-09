package com.example.legalnotice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.legalnotice.R;
import com.example.legalnotice.adapters.PillAdapter;
import com.example.legalnotice.models.Pill;
import com.example.legalnotice.ApiClient;
import com.example.legalnotice.ApiService;
import com.example.legalnotice.DeviceUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class MedicationManagementActivity extends AppCompatActivity {
    private RecyclerView medicationRecyclerView;
    private PillAdapter pillAdapter;
    private ImageView homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_management);

        // 홈 버튼 선언 및 클릭 리스너 설정
        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // NextActivity로 이동
                Intent intent = new Intent(MedicationManagementActivity.this, NextActivity.class);
                startActivity(intent);
                finish();
            }
        });

        medicationRecyclerView = findViewById(R.id.medicationRecyclerView);
        medicationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadMedications();
    }

    private void loadMedications() {
        String deviceId = DeviceUtil.getDeviceId(this);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Pill>> call = apiService.getUserMedications(deviceId);

        call.enqueue(new Callback<List<Pill>>() {
            @Override
            public void onResponse(Call<List<Pill>> call, Response<List<Pill>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pill> medications = response.body();
                    if (medications.isEmpty()) {
                        Toast.makeText(MedicationManagementActivity.this, "복용 중인 약물이 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        pillAdapter = new PillAdapter(medications, new PillAdapter.OnPillDeleteListener() {
                            @Override
                            public void onPillDelete(Pill pill) {
                                deletePillFromDatabase(pill);
                            }
                        }, null, true); // 삭제 버튼을 활성화하려면 true로 설정
                        medicationRecyclerView.setAdapter(pillAdapter);
                    }
                } else {
                    Log.e("MedicationManagement", "서버 응답 오류: " + response.code());
                    Toast.makeText(MedicationManagementActivity.this, "약물 데이터를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pill>> call, Throwable t) {
                Log.e("MedicationManagement", "네트워크 오류: " + t.getMessage());
                Toast.makeText(MedicationManagementActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void deletePillFromDatabase(Pill pill) {
        String deviceId = DeviceUtil.getDeviceId(this);
        pill.setUserId(deviceId); // 기기의 고유 ID를 userId로 설정

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.deletePill(pill);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("MedicationManagement", "약 정보가 성공적으로 삭제되었습니다.");
                    loadMedications(); // 삭제 후 목록을 다시 로드
                } else {
                    Log.e("MedicationManagement", "삭제 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MedicationManagement", "네트워크 오류: " + t.getMessage());
            }
        });
    }


}
