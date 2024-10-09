package com.example.legalnotice.models;

public class LegalNoticeData {
    private String userId;
    private String date;
    private boolean accepted;

    public LegalNoticeData(String userId, String date, boolean accepted) {
        this.userId = userId;
        this.date = date;
        this.accepted = accepted;
    }

    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public boolean isAccepted() {
        return accepted;
    }
}
