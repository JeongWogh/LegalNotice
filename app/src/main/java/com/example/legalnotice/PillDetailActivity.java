package com.example.legalnotice;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.legalnotice.models.Pill;
import com.squareup.picasso.Picasso;

public class PillDetailActivity extends AppCompatActivity {

    private ImageView pillImageView;
    private TextView pillNameTextView;
    private TextView pillEfcyTextView;
    private TextView pillAtpnTextView;
    private TextView pillSeTextView;
    private TextView pillEtcotcTextView;

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
        }
    }
}
