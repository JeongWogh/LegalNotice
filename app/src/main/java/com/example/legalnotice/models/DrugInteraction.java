package com.example.legalnotice.models;

// DrugInteraction 클래스는 약물 상호작용 정보를 저장하는 모델 클래스입니다.
public class DrugInteraction {
    // 상호작용 약물의 이름
    private String noneItemName;
    // 상호작용 약물의 성분명
    private String noneIngrName;
    // 상호작용 약물의 이미지 URL
    private String noneItemImage;

    // Getter and Setter methods (각 필드에 접근하고 수정하기 위한 메소드들)

    // 상호작용 약물의 이름을 반환하는 getter 메소드
    public String getNoneItemName() {
        return noneItemName;
    }

    // 상호작용 약물의 이름을 설정하는 setter 메소드
    public void setNoneItemName(String noneItemName) {
        this.noneItemName = noneItemName;
    }

    // 상호작용 약물의 성분명을 반환하는 getter 메소드
    public String getNoneIngrName() {
        return noneIngrName;
    }

    // 상호작용 약물의 성분명을 설정하는 setter 메소드
    public void setNoneIngrName(String noneIngrName) {
        this.noneIngrName = noneIngrName;
    }

    // 상호작용 약물의 이미지 URL을 반환하는 getter 메소드
    public String getNoneItemImage() {
        return noneItemImage;
    }

    // 상호작용 약물의 이미지 URL을 설정하는 setter 메소드
    public void setNoneItemImage(String noneItemImage) {
        this.noneItemImage = noneItemImage;
    }
}
