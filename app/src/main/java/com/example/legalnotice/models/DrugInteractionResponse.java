package com.example.legalnotice.models;

import java.util.List;

public class DrugInteractionResponse {
    private boolean success;
    private String message;
    private List<DrugInteraction> data;

    // Getter 및 Setter
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

    public List<DrugInteraction> getData() {
        return data;
    }

    public void setData(List<DrugInteraction> data) {
        this.data = data;
    }
}
