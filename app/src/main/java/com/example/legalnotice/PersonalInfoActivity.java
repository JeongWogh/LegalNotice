package com.example.legalnotice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.legalnotice.ApiClient;
import com.example.legalnotice.ApiService;
import com.example.legalnotice.DeviceUtil;
import com.example.legalnotice.models.PersonalInfoData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalInfoActivity extends AppCompatActivity {
    private EditText ageEditText;
    private RadioGroup genderRadioGroup;
    private Switch pregnantSwitch, nursingSwitch;
    private EditText allergyEditText;
    private Button saveButton, resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        ageEditText = findViewById(R.id.ageEditText);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        pregnantSwitch = findViewById(R.id.pregnantSwitch);
        nursingSwitch = findViewById(R.id.nursingSwitch);
        allergyEditText = findViewById(R.id.allergyEditText);
        saveButton = findViewById(R.id.saveButton);
        resetButton = findViewById(R.id.resetButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePersonalInfo();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPersonalInfo();
            }
        });
    }

    private void savePersonalInfo() {
        String deviceId = DeviceUtil.getDeviceId(this);
        int age = Integer.parseInt(ageEditText.getText().toString());
        String gender = ((RadioButton) findViewById(genderRadioGroup.getCheckedRadioButtonId())).getText().toString();
        boolean pregnant = pregnantSwitch.isChecked();
        boolean nursing = nursingSwitch.isChecked();
        String allergy = allergyEditText.getText().toString();

        PersonalInfoData personalInfo = new PersonalInfoData(deviceId, age, gender, pregnant, nursing, allergy);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.savePersonalInfo(personalInfo);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PersonalInfoActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PersonalInfoActivity.this, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PersonalInfoActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetPersonalInfo() {
        String deviceId = DeviceUtil.getDeviceId(this);
        PersonalInfoData personalInfo = new PersonalInfoData(deviceId, null, null, null, null, null);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.resetPersonalInfo(personalInfo);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PersonalInfoActivity.this, "초기화되었습니다.", Toast.LENGTH_SHORT).show();
                    ageEditText.setText("");
                    genderRadioGroup.clearCheck();
                    pregnantSwitch.setChecked(false);
                    nursingSwitch.setChecked(false);
                    allergyEditText.setText("");
                } else {
                    Toast.makeText(PersonalInfoActivity.this, "초기화에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PersonalInfoActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
