package com.example.legalnotice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

public class FullImageActivity extends AppCompatActivity {

    private ImageView fullImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        fullImageView = findViewById(R.id.fullImageView);

        // Intent에서 이미지 URL 가져오기
        String imageUrl = getIntent().getStringExtra("imageUrl");
        if (imageUrl != null) {
            Picasso.get()
                    .load(imageUrl)
                    .error(R.drawable.ic_default_image)
                    .into(fullImageView);
        }
    }
}
