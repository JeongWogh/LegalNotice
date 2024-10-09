package com.example.legalnotice.models;

public class PersonalInfoData {
    private String userId;
    private Integer age;
    private String gender;
    private Boolean pregnant;
    private Boolean nursing;
    private String allergy;

    public PersonalInfoData(String userId, Integer age, String gender, Boolean pregnant, Boolean nursing, String allergy) {
        this.userId = userId;
        this.age = age;
        this.gender = gender;
        this.pregnant = pregnant;
        this.nursing = nursing;
        this.allergy = allergy;
    }

    // Getter 및 Setter
    public String getUserId() {
        return userId;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public Boolean getPregnant() {
        return pregnant;
    }

    public Boolean getNursing() {
        return nursing;
    }

    public String getAllergy() {
        return allergy;
    }
}
