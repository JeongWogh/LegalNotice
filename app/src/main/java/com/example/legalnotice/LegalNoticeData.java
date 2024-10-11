package com.example.legalnotice;

// LegalNoticeData 클래스는 법적 고지에 대한 정보를 저장하는 모델 클래스입니다.
public class LegalNoticeData {
    // 필드 정의
    private String userId;  // 사용자 ID
    private String date;    // 동의한 날짜
    private boolean accepted; // 동의 여부 (true/false)

    // 생성자
    public LegalNoticeData(String userId, String date, boolean accepted) {
        this.userId = userId;
        this.date = date;
        this.accepted = accepted;
    }

    // Getter 메서드: 각 필드에 대한 값을 반환하는 메서드들
    public String getUserId() {
        return userId; // 사용자 ID 반환
    }

    public String getDate() {
        return date; // 날짜 반환
    }

    public boolean isAccepted() {
        return accepted; // 동의 여부 반환 (true/false)
    }
}
