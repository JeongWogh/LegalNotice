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
import com.example.legalnotice.adapters.PillAdapter;
import com.example.legalnotice.models.Pill;
import com.example.legalnotice.ApiClient;
import com.example.legalnotice.ApiService;
import com.example.legalnotice.DeviceUtil;
import com.example.legalnotice.models.UserMedicationsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MedicationManagementActivity extends AppCompatActivity {
    // RecyclerView를 통해 약물 목록을 표시할 뷰
    private RecyclerView medicationRecyclerView;
    // RecyclerView에 데이터를 설정할 어댑터
    private PillAdapter pillAdapter;
    // 홈으로 돌아가는 버튼
    private ImageView homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_management);

        // 홈 버튼 선언 및 클릭 리스너 설정
        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> {
            // NextActivity로 이동
            Intent intent = new Intent(MedicationManagementActivity.this, NextActivity.class);
            startActivity(intent);
            finish(); // 현재 액티비티를 종료하여 메모리 누수를 방지
        });

        // RecyclerView 초기화
        medicationRecyclerView = findViewById(R.id.medicationRecyclerView);
        // RecyclerView가 수직으로 배치되도록 설정
        medicationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 약물 목록을 불러옴
        loadMedications();
    }

    // 서버에서 사용자의 약물 데이터를 불러오는 메소드
    private void loadMedications() {
        // 기기의 고유 ID를 가져와 사용자 ID로 사용
        String deviceId = DeviceUtil.getDeviceId(this);
        // ApiService 인스턴스를 생성
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        // 사용자의 약물 목록을 가져오는 API 호출
        Call<UserMedicationsResponse> call = apiService.getUserMedications(deviceId);


        // API 응답을 처리
        call.enqueue(new Callback<UserMedicationsResponse>() {
            @Override
            public void onResponse(Call<UserMedicationsResponse> call, Response<UserMedicationsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserMedicationsResponse userMedicationsResponse = response.body();
                    if (userMedicationsResponse.isSuccess()) {
                        List<Pill> medications = userMedicationsResponse.getData();  // 서버에서 가져온 약물 목록
                        setupRecyclerView(medications);  // RecyclerView 설정
                    } else {
                        Log.e("MedicationManagement", "응답 실패: " + userMedicationsResponse.getMessage());
                        Toast.makeText(MedicationManagementActivity.this, "약물 데이터를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("MedicationManagement", "서버 응답 오류: " + response.code());
                    Toast.makeText(MedicationManagementActivity.this, "약물 데이터를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserMedicationsResponse> call, Throwable t) {
                Log.e("MedicationManagement", "네트워크 오류: " + t.getMessage());
                Toast.makeText(MedicationManagementActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // RecyclerView를 설정하는 메서드
    private void setupRecyclerView(List<Pill> pills) {
        pillAdapter = new PillAdapter((pills != null) ? pills : new ArrayList<>(), new PillAdapter.OnPillDeleteListener() {
            @Override
            public void onPillDelete(Pill pill) {
                deletePillFromDatabase(pill);  // 삭제 메서드 호출
            }
        }, null, new PillAdapter.OnPillClickListener() {
            @Override
            public void onPillClick(Pill pill) {
                showPillDetails(pill);  // 상세 정보 보기 메서드 호출
            }
        }, true);
        medicationRecyclerView.setAdapter(pillAdapter);
    }


    // 사용자의 약물을 DB에서 삭제하는 메소드
    private void deletePillFromDatabase(Pill pill) {
        // 기기의 고유 ID를 userId로 설정
        String deviceId = DeviceUtil.getDeviceId(this);
        pill.setUserId(deviceId);

        // ApiService 인스턴스를 생성
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        // 약물 삭제 요청 API 호출
        Call<Void> call = apiService.deletePill(pill);

        // API 응답을 처리
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 삭제 성공 로그 및 사용자에게 성공 메시지 표시
                    Log.d("MedicationManagement", "약 정보가 성공적으로 삭제되었습니다.");
                    Toast.makeText(MedicationManagementActivity.this, "약물이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    // 삭제 후 목록을 다시 로드하여 갱신
                    loadMedications();
                } else {
                    // 삭제 실패 로그 출력 및 사용자에게 오류 메시지 표시
                    Log.e("MedicationManagement", "삭제 실패: " + response.code());
                    Toast.makeText(MedicationManagementActivity.this, "약물 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // 네트워크 오류 로그 출력 및 사용자에게 오류 메시지 표시
                Log.e("MedicationManagement", "네트워크 오류: " + t.getMessage());
                Toast.makeText(MedicationManagementActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 약물의 상세 정보를 표시하는 메소드
    private void showPillDetails(Pill pill) {
        // PillDetailActivity로 이동하며, Pill 객체를 전달
        Intent intent = new Intent(MedicationManagementActivity.this, PillDetailActivity.class);
        intent.putExtra("pill", pill); // Pill 객체를 Intent에 추가하여 전달
        startActivity(intent);
    }
}
