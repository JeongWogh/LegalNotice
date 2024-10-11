package com.example.legalnotice.models;

// PersonalInfoData 클래스는 개인 정보에 대한 데이터를 저장하는 모델 클래스입니다.
public class PersonalInfoData {
    // 사용자 ID (기기의 고유 ID로 설정)
    private String userId;
    // 나이
    private Integer age;
    // 성별 (남자 또는 여자)
    private String gender;
    // 임신 여부 (true: 임신 중, false: 임신 아님)
    private Boolean pregnant;
    // 수유 여부 (true: 수유 중, false: 수유 아님)
    private Boolean nursing;
    // 알레르기 정보
    private String allergy;

    // 생성자: PersonalInfoData 객체를 생성할 때 userId, age, gender, pregnant, nursing, allergy 값을 초기화합니다.
    public PersonalInfoData(String userId, Integer age, String gender, Boolean pregnant, Boolean nursing, String allergy) {
        this.userId = userId;
        this.age = age;
        this.gender = gender;
        this.pregnant = pregnant;
        this.nursing = nursing;
        this.allergy = allergy;
    }

    // Getter 메소드들

    // 사용자 ID를 반환하는 getter 메소드
    public String getUserId() {
        return userId;
    }

    // 나이를 반환하는 getter 메소드
    public Integer getAge() {
        return age;
    }

    // 성별을 반환하는 getter 메소드
    public String getGender() {
        return gender;
    }

    // 임신 여부를 반환하는 getter 메소드
    public Boolean getPregnant() {
        return pregnant;
    }

    // 수유 여부를 반환하는 getter 메소드
    public Boolean getNursing() {
        return nursing;
    }

    // 알레르기 정보를 반환하는 getter 메소드
    public String getAllergy() {
        return allergy;
    }
}
