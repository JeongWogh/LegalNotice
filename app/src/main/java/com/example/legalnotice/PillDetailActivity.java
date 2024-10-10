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
import com.example.legalnotice.models.Pill;
import com.example.legalnotice.ApiClient;
import com.example.legalnotice.ApiService;
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

        // Intent에서 Pill 객체 가져오기
        Pill pill = getIntent().getParcelableExtra("pill");

        if (pill != null) {
            // 데이터를 뷰에 설정
            pillNameTextView.setText(pill.getItemName());
            pillEfcyTextView.setText(pill.getEfcyQesitm());
            pillAtpnTextView.setText(pill.getAtpnQesitm());
            pillSeTextView.setText(pill.getSeQesitm());
            pillEtcotcTextView.setText(pill.getEtcotc());

            // 이미지 로딩
            Picasso.get()
                    .load(pill.getItemImage())
                    .error(R.drawable.ic_default_image)
                    .into(pillImageView);

            // 상호작용 버튼 클릭 이벤트 설정
            interactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fetchDrugInteractions(pill.getItemName());
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

    private void fetchDrugInteractions(String drugName) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<DrugInteraction>> call = apiService.getDrugInteractions(drugName);

        call.enqueue(new Callback<List<DrugInteraction>>() {
            @Override
            public void onResponse(Call<List<DrugInteraction>> call, Response<List<DrugInteraction>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DrugInteraction> interactions = response.body();
                    if (!interactions.isEmpty()) {
                        showInteractionDialog(interactions);
                    } else {
                        showNoInteractionDialog();
                    }
                } else {
                    Log.e("PillDetailActivity", "상호작용 정보를 불러오는데 실패했습니다: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<DrugInteraction>> call, Throwable t) {
                Log.e("PillDetailActivity", "네트워크 오류: " + t.getMessage());
            }
        });
    }

    private void showInteractionDialog(List<DrugInteraction> interactions) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_interaction, null);
        LinearLayout interactionContainer = dialogView.findViewById(R.id.interactionContainer);

        for (DrugInteraction interaction : interactions) {
            View itemView = getLayoutInflater().inflate(R.layout.item_interaction, null);
            ImageView interactionImageView = itemView.findViewById(R.id.interactionImageView);
            TextView interactionTextView = itemView.findViewById(R.id.interactionTextView);

            Picasso.get()
                    .load(interaction.getNoneItemImage())
                    .error(R.drawable.ic_default_image)
                    .into(interactionImageView);

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

            interactionContainer.addView(itemView);
        }

        new AlertDialog.Builder(this)
                .setTitle("상호 복용 금지 약물 정보")
                .setView(dialogView)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

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
