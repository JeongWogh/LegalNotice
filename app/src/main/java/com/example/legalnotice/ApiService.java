package com.example.legalnotice;

import com.example.legalnotice.models.LegalNoticeData; // 올바른 경로로 수정
import com.example.legalnotice.models.PersonalInfoData;
import com.example.legalnotice.models.Pill;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/api/pills/user")
    Call<List<Pill>> getUserMedications(@Query("user_id") String userId);

    @POST("/api/pills/add")
    Call<Void> addPill(@Body Pill pill);

    @GET("/api/pills/searchByName")
    Call<List<Pill>> searchByName(@Query("itemName") String itemName);

    // sendLegalNotice 메서드 수정
    @POST("/legal-notice")
    Call<Void> sendLegalNotice(@Body LegalNoticeData legalNoticeData);

    @POST("/api/pills/delete")
    Call<Void> deletePill(@Body Pill pill);

    @GET("/api/check-legal-notice")
    Call<Void> checkLegalNotice(@Query("userId") String userId);

    // 개인 정보 저장 엔드포인트
    @POST("/api/personal-info/save")
    Call<Void> savePersonalInfo(@Body PersonalInfoData personalInfo);

    // 개인 정보 초기화 엔드포인트
    @POST("/api/personal-info/reset")
    Call<Void> resetPersonalInfo(@Body PersonalInfoData personalInfo);
}
