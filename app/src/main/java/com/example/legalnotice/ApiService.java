package com.example.legalnotice;

import com.example.legalnotice.models.DrugInteraction;
import com.example.legalnotice.models.DrugInteractionResponse;
import com.example.legalnotice.models.LegalNoticeData;
import com.example.legalnotice.models.PersonalInfoData;
import com.example.legalnotice.models.Pill;
import com.example.legalnotice.models.SearchResponse;
import com.example.legalnotice.models.UserMedicationsResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // 사용자 약물 정보를 가져오는 메서드
    @GET("/api/v1/pills/user")
    Call<UserMedicationsResponse> getUserMedications(@Query("user_id") String userId);


    // 약물 정보를 추가하는 메서드
    @POST("/api/v1/pills/add")  // '/api/v1/' 추가
    Call<Void> addPill(@Body Pill pill);

    // 약물 이름으로 검색하는 메서드
    @GET("/api/v1/pills/searchByName")
    Call<SearchResponse> searchByName(@Query("itemName") String itemName);

    // 법적 고지 정보를 서버에 전송하는 메서드
    @POST("/api/v1/legal-notice")  // '/api/v1/' 추가
    Call<Void> sendLegalNotice(@Body LegalNoticeData legalNoticeData);

    // 약물 정보를 삭제하는 메서드
    @POST("/api/v1/pills/delete")  // '/api/v1/' 추가
    Call<Void> deletePill(@Body Pill pill);

    // 법적 고지 수락 여부를 확인하는 메서드
    @GET("/api/v1/check-legal-notice")  // '/api/v1/' 추가
    Call<Void> checkLegalNotice(@Query("userId") String userId);

    // 상호작용 정보를 가져오는 API 호출 정의
    @GET("/api/v1/getDrugInteractions")
    Call<DrugInteractionResponse> getDrugInteractions(@Query("drugItemName") String drugName, @Query("userId") String userId);

    // 사용자 개인 정보를 가져오는 메서드
    @GET("/api/v1/personal-info")  // '/api/v1/' 추가
    Call<PersonalInfoData> getPersonalInfo(@Query("userId") String userId);

    // 사용자 개인 정보를 저장하는 메서드
    @Headers("Content-Type: application/json")
    @POST("/api/v1/personal-info/save")  // '/api/v1/' 추가
    Call<Void> savePersonalInfo(@Body PersonalInfoData personalInfo);

    // 사용자 개인 정보를 초기화하는 메서드
    @Headers("Content-Type: application/json")
    @POST("/api/v1/personal-info/reset")  // '/api/v1/' 추가
    Call<Void> resetPersonalInfo(@Body PersonalInfoData personalInfo);
}
