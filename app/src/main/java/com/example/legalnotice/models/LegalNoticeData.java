package com.example.legalnotice.models;

// LegalNoticeData 클래스는 법적 고지 정보에 대한 데이터를 저장하는 모델 클래스입니다.
public class LegalNoticeData {
    // 사용자 ID (기기의 고유 ID로 설정)
    private String userId;
    // 법적 고지를 수락한 날짜
    private String date;
    // 법적 고지에 대한 동의 여부
    private boolean accepted;

    // 생성자: LegalNoticeData 객체를 생성할 때 userId, date, accepted 값을 초기화합니다.
    public LegalNoticeData(String userId, String date, boolean accepted) {
        this.userId = userId;
        this.date = date;
        this.accepted = accepted;
    }

    // Getter 메소드들

    // 사용자 ID를 반환하는 getter 메소드
    public String getUserId() {
        return userId;
    }

    // 법적 고지 수락 날짜를 반환하는 getter 메소드
    public String getDate() {
        return date;
    }

    // 법적 고지 동의 여부를 반환하는 getter 메소드
    public boolean isAccepted() {
        return accepted;
    }
}
