package com.example.legalnotice;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // 서버의 기본 URL 설정: 로컬 네트워크에 있는 Flask 서버의 IP와 포트 번호
    // 주석 처리된 부분은 에뮬레이터 환경에서 사용할 로컬 서버 IP
    //    private static final String BASE_URL = "http://10.0.2.2:5000"; // 에뮬레이터에서 로컬 서버 접속 시 사용
    private static final String BASE_URL = "http://10.100.1.76:5001"; // 실제 네트워크에서 사용되는 서버 주소

    private static Retrofit retrofit;

    // Retrofit 클라이언트를 생성하고 반환하는 메소드
    public static Retrofit getClient() {
        // Retrofit 인스턴스가 null인 경우에만 새로 생성하여 초기화
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // 기본 URL 설정
                    .addConverterFactory(GsonConverterFactory.create()) // Gson을 사용해 JSON을 객체로 변환
                    .build(); // Retrofit 인스턴스 생성
        }
        return retrofit; // 생성된 Retrofit 인스턴스 반환
    }
}
