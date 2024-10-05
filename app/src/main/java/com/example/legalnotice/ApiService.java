package com.example.legalnotice;

import com.example.legalnotice.LegalNoticeData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/legal-notice")
    Call<Void> sendLegalNotice(@Body LegalNoticeData legalNoticeData);
}
