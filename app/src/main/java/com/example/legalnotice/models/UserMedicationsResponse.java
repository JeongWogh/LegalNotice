package com.example.legalnotice.models;

import java.util.List;

public class UserMedicationsResponse {
    private boolean success;
    private String message;
    private List<Pill> data;

    // Getter와 Setter
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Pill> getData() {
        return data;
    }

    public void setData(List<Pill> data) {
        this.data = data;
    }
}
