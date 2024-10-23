package com.example.legalnotice;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.legalnotice.models.DrugInteraction;
import com.example.legalnotice.models.DrugInteractionResponse;
import com.example.legalnotice.models.Pill;
import com.example.legalnotice.ApiClient;
import com.example.legalnotice.ApiService;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PillDetailActivity extends AppCompatActivity {

    // 뷰 요소 선언
    private ImageView pillImageView;
    private TextView pillNameTextView;
    private TextView pillEfcyTextView;
    private TextView pillAtpnTextView;
    private TextView pillSeTextView;
    private TextView pillEtcotcTextView;
    private Button interactionButton;
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
                    .error(R.drawable.ic_default_image) // 이미지 로딩 실패 시 기본 이미지 설정
                    .into(pillImageView);

            // 상호작용 버튼 클릭 시 이벤트 설정
            interactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fetchDrugInteractions(pill.getItemName()); // 상호작용 정보를 가져오는 메서드 호출
                }
            });

            // 홈 버튼 클릭 시 메인 화면(NextActivity)으로 이동
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PillDetailActivity.this, NextActivity.class);
                    startActivity(intent);
                    finish(); // 현재 액티비티 종료
                }
            });
        }
    }

    // 상호작용 정보를 가져오는 메서드
    private void fetchDrugInteractions(String drugName) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        String deviceId = DeviceUtil.getDeviceId(this);  // userId로 사용될 기기 ID를 가져옵니다.

        Call<DrugInteractionResponse> call = apiService.getDrugInteractions(drugName, deviceId);
        call.enqueue(new Callback<DrugInteractionResponse>() {
            @Override
            public void onResponse(Call<DrugInteractionResponse> call, Response<DrugInteractionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DrugInteractionResponse interactionResponse = response.body();

                    if (interactionResponse.isSuccess() && interactionResponse.getData() != null) {
                        List<DrugInteraction> interactions = interactionResponse.getData();  // 응답에서 data 필드를 추출
                        if (!interactions.isEmpty()) {
                            showInteractionDialog(interactions);  // 상호작용 정보를 다이얼로그에 표시
                        } else {
                            showNoInteractionDialog();  // 상호작용 정보가 없을 경우
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
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_interaction, null);
        LinearLayout interactionContainer = dialogView.findViewById(R.id.interactionContainer);

        // 상호작용 정보를 반복하면서 각 항목을 다이얼로그에 추가
        for (DrugInteraction interaction : interactions) {
            View itemView = getLayoutInflater().inflate(R.layout.item_interaction, null);
            ImageView interactionImageView = itemView.findViewById(R.id.interactionImageView);
            TextView interactionTextView = itemView.findViewById(R.id.interactionTextView);

            // Picasso를 사용해 이미지 로드
            Picasso.get()
                    .load(interaction.getNoneItemImage())
                    .error(R.drawable.ic_default_image) // 이미지 로딩 실패 시 기본 이미지
                    .into(interactionImageView);

            // 약물 이름과 성분 정보를 텍스트뷰에 설정
            interactionTextView.setText("약물: " + interaction.getNoneItemName() + "\n성분: " + interaction.getNoneIngrName());

            // 이미지 클릭 시 확대 화면으로 이동
            interactionImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PillDetailActivity.this, FullImageActivity.class);
                    intent.putExtra("imageUrl", interaction.getNoneItemImage());
                    startActivity(intent);
                }
            });

            // interactionContainer에 항목 추가
            interactionContainer.addView(itemView);
        }

        // AlertDialog를 사용해 상호작용 다이얼로그 표시
        new AlertDialog.Builder(this)
                .setTitle("상호 복용 금지 약물 정보")
                .setView(dialogView) // 커스텀 뷰 설정
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss(); // 다이얼로그 닫기
                    }
                })
                .show();
    }

    // 상호작용 정보가 없을 때 표시하는 다이얼로그
    private void showNoInteractionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("상호 복용 금지 약물 정보")
                .setMessage("상호작용 정보가 없습니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss(); // 다이얼로그 닫기
                    }
                })
                .show();
    }
}
