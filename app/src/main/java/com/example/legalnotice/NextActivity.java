package com.example.legalnotice;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        TextView textView = findViewById(R.id.textView);
        textView.setText("환영합니다! 법적 고지에 동의하셨습니다.");
    }
}
