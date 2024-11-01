package com.example.legalnotice;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.legalnotice.models.DrugInteraction;
import com.example.legalnotice.models.DrugInteractionResponse;
import com.example.legalnotice.models.Pill;
import com.example.legalnotice.ApiClient;
import com.example.legalnotice.ApiService;
import com.example.legalnotice.DeviceUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PillDetailActivity extends AppCompatActivity {

    private ImageView pillImageView;
    private TextView pillNameTextView;
    private TextView pillEfcyTextView;
    private TextView pillAtpnTextView;
    private TextView pillSeTextView;
    private TextView pillEtcotcTextView;
    private Button interactionButton;
    private Button addMedicationButton;
    private ImageView homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_detail);

        // 뷰 초기화
        pillImageView = findViewById(R.id.itemImageView);
        pillNameTextView = findViewById(R.id.itemNameTextView);
        pillEfcyTextView = findViewById(R.id.efcyTextView);
        pillAtpnTextView = findViewById(R.id.atpnTextView);
        pillSeTextView = findViewById(R.id.seTextView);
        pillEtcotcTextView = findViewById(R.id.etcotcTextView);
        interactionButton = findViewById(R.id.interactionButton);
        addMedicationButton = findViewById(R.id.addMedicationButton);
        homeButton = findViewById(R.id.homeButton);

        // Intent에서 전달된 Pill 객체 가져오기
        Pill pill = getIntent().getParcelableExtra("pill");

        // 약물 데이터가 존재하면 화면에 표시
        if (pill != null) {
            pillNameTextView.setText(pill.getItemName());
            pillEfcyTextView.setText(pill.getEfcyQesitm());
            pillAtpnTextView.setText(pill.getAtpnQesitm());
            pillSeTextView.setText(pill.getSeQesitm());
            pillEtcotcTextView.setText(pill.getEtcotc());

            // Picasso 라이브러리를 사용하여 이미지 로딩
            Picasso.get()
                    .load(pill.getItemImage())
                    .error(R.drawable.ic_default_image)
                    .into(pillImageView);

            // 상호작용 버튼 클릭 시 상호작용 정보를 가져오는 메서드 호출
            interactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fetchDrugInteractions(pill.getItemName());
                }
            });

            // 추가 버튼 클릭 이벤트 설정
            addMedicationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    savePillToDatabase(pill); // 약물 추가 메서드 호출
                }
            });

            // 홈 버튼 클릭 시 메인 화면으로 이동
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PillDetailActivity.this, NextActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    // 약물 추가 메서드
    private void savePillToDatabase(Pill pill) {
        String deviceId = DeviceUtil.getDeviceId(this);
        pill.setUserId(deviceId); // 기기 고유 ID를 사용자 ID로 설정

        // JSON 변환 로그 추가 (디버깅용)
        Gson gson = new Gson();
        String json = gson.toJson(pill);
        Log.d("PillDetailActivity", "전송할 JSON: " + json);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.addPill(pill);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("PillDetailActivity", "약 정보가 DB에 성공적으로 저장되었습니다.");
                    Toast.makeText(PillDetailActivity.this, "약이 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("PillDetailActivity", "DB 저장 실패: " + response.code());
                    Toast.makeText(PillDetailActivity.this, "약 추가에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("PillDetailActivity", "네트워크 오류: " + t.getMessage());
                Toast.makeText(PillDetailActivity.this, "네트워크 오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 상호작용 정보를 가져오는 메서드
    private void fetchDrugInteractions(String drugName) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        String deviceId = DeviceUtil.getDeviceId(this); // userId로 사용될 기기 ID를 가져옵니다.

        Call<DrugInteractionResponse> call = apiService.getDrugInteractions(drugName, deviceId);
        call.enqueue(new Callback<DrugInteractionResponse>() {
            @Override
            public void onResponse(Call<DrugInteractionResponse> call, Response<DrugInteractionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DrugInteractionResponse interactionResponse = response.body();

                    if (interactionResponse.isSuccess() && interactionResponse.getData() != null) {
                        List<DrugInteraction> interactions = interactionResponse.getData();
                        if (!interactions.isEmpty()) {
                            showInteractionDialog(interactions); // 상호작용 정보를 다이얼로그에 표시
                        } else {
                            showNoInteractionDialog(); // 상호작용 정보가 없을 경우
                        }
                    } else {
                        Log.e("PillDetailActivity", "상호작용 정보를 불러오는데 실패했습니다: " + interactionResponse.getMessage());
                    }
                } else {
                    Log.e("PillDetailActivity", "상호작용 정보를 불러오는데 실패했습니다: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<DrugInteractionResponse> call, Throwable t) {
                Log.e("PillDetailActivity", "네트워크 오류: " + t.getMessage());
            }
        });
    }

    // 상호작용 정보를 표시하는 다이얼로그
    private void showInteractionDialog(List<DrugInteraction> interactions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("상호 복용 금지 약물 정보");

        StringBuilder message = new StringBuilder();
        for (DrugInteraction interaction : interactions) {
            message.append("약물: ").append(interaction.getNoneItemName())
                    .append("\n성분: ").append(interaction.getNoneIngrName())
                    .append("\n\n");
        }

        builder.setMessage(message.toString());
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    // 상호작용 정보가 없을 때 표시하는 다이얼로그
    private void showNoInteractionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("상호 복용 금지 약물 정보")
                .setMessage("상호작용 정보가 없습니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}
