package com.example.legalnotice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.legalnotice.ApiClient;
import com.example.legalnotice.ApiService;
import com.example.legalnotice.models.PersonalInfoData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalInfoActivity extends AppCompatActivity {

    private EditText ageEditText;
    private RadioGroup genderRadioGroup;
    private Switch pregnantSwitch;
    private Switch nursingSwitch;
    private EditText allergyEditText;
    private ImageButton homeButton;
    private Button saveButton;
    private Button resetButton;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        ageEditText = findViewById(R.id.ageEditText);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        pregnantSwitch = findViewById(R.id.pregnantSwitch);
        nursingSwitch = findViewById(R.id.nursingSwitch);
        allergyEditText = findViewById(R.id.allergyEditText);
        homeButton = findViewById(R.id.homeButton);
        saveButton = findViewById(R.id.saveButton);
        resetButton = findViewById(R.id.resetButton);

        // 기기 고유 ID를 사용자 ID로 사용
        userId = DeviceUtil.getDeviceId(this);

        // 홈 버튼 클릭 시 메인 화면으로 이동
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalInfoActivity.this, NextActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 저장 버튼 클릭 이벤트
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePersonalInfo();
            }
        });

        // 초기화 버튼 클릭 이벤트
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPersonalInfo();
            }
        });

        // DB에서 사용자 정보 불러오기
        loadPersonalInfo();
    }

    private void loadPersonalInfo() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<PersonalInfoData> call = apiService.getPersonalInfo(userId);

        call.enqueue(new Callback<PersonalInfoData>() {
            @Override
            public void onResponse(Call<PersonalInfoData> call, Response<PersonalInfoData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PersonalInfoData personalInfo = response.body();
                    Log.d("PersonalInfoActivity", "불러온 데이터: " + personalInfo.toString());

                    // 불러온 데이터를 화면에 설정
                    if (personalInfo.getAge() != null) {
                        ageEditText.setText(String.valueOf(personalInfo.getAge()));
                    }

                    if (personalInfo.getGender() != null) {
                        if (personalInfo.getGender().equals("남자")) {
                            genderRadioGroup.check(R.id.maleRadioButton);
                        } else if (personalInfo.getGender().equals("여자")) {
                            genderRadioGroup.check(R.id.femaleRadioButton);
                        }
                    }

                    pregnantSwitch.setChecked(personalInfo.getPregnant() != null && personalInfo.getPregnant());
                    nursingSwitch.setChecked(personalInfo.getNursing() != null && personalInfo.getNursing());
                    if (personalInfo.getAllergy() != null) {
                        allergyEditText.setText(personalInfo.getAllergy());
                    }
                } else {
                    Log.e("PersonalInfoActivity", "정보 불러오기 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PersonalInfoData> call, Throwable t) {
                Log.e("PersonalInfoActivity", "네트워크 오류: " + t.getMessage());
            }
        });
    }




    private void savePersonalInfo() {
        Integer age = ageEditText.getText().toString().trim().isEmpty() ? null : Integer.parseInt(ageEditText.getText().toString().trim());
        String gender = genderRadioGroup.getCheckedRadioButtonId() == R.id.maleRadioButton ? "남자" : "여자";
        boolean pregnant = pregnantSwitch.isChecked();
        boolean nursing = nursingSwitch.isChecked();
        String allergy = allergyEditText.getText().toString().trim();

        PersonalInfoData personalInfo = new PersonalInfoData(userId, age, gender, pregnant, nursing, allergy);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.savePersonalInfo(personalInfo);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PersonalInfoActivity.this, "정보가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("PersonalInfoActivity", "저장 실패: " + response.code());
                    Toast.makeText(PersonalInfoActivity.this, "저장에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("PersonalInfoActivity", "네트워크 오류: " + t.getMessage());
                Toast.makeText(PersonalInfoActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetPersonalInfo() {
        PersonalInfoData personalInfo = new PersonalInfoData(userId, null, null, false, false, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.resetPersonalInfo(personalInfo);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PersonalInfoActivity.this, "정보가 초기화되었습니다.", Toast.LENGTH_SHORT).show();
                    // 화면 초기화
                    ageEditText.setText("");
                    genderRadioGroup.clearCheck();
                    pregnantSwitch.setChecked(false);
                    nursingSwitch.setChecked(false);
                    allergyEditText.setText("");
                } else {
                    Log.e("PersonalInfoActivity", "초기화 실패: " + response.code());
                    Toast.makeText(PersonalInfoActivity.this, "초기화에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("PersonalInfoActivity", "네트워크 오류: " + t.getMessage());
                Toast.makeText(PersonalInfoActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
