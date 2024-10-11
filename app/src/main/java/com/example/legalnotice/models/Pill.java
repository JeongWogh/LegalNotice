package com.example.legalnotice.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

// Pill 클래스는 약물 정보를 저장하는 모델 클래스이며, Parcelable을 구현하여 객체를 다른 액티비티로 전달할 수 있도록 함
public class Pill implements Parcelable {
    // JSON 필드와 클래스 필드를 매핑
    @SerializedName("user_id")
    private String userId;

    @SerializedName("itemSeq")
    private String itemSeq;

    @SerializedName("itemName")
    private String itemName;

    @SerializedName("efcyQesitm")
    private String efcyQesitm;

    @SerializedName("atpnQesitm")
    private String atpnQesitm;

    @SerializedName("seQesitm")
    private String seQesitm;

    @SerializedName("etcotc")
    private String etcotc;

    @SerializedName("itemImage")
    private String itemImage;

    // 생성자: 필드 초기화를 위해 매개변수를 받아 객체를 생성
    public Pill(String itemSeq, String itemName, String efcyQesitm, String atpnQesitm, String seQesitm, String etcotc, String itemImage) {
        this.itemSeq = itemSeq;
        this.itemName = itemName;
        this.efcyQesitm = efcyQesitm;
        this.atpnQesitm = atpnQesitm;
        this.seQesitm = seQesitm;
        this.etcotc = etcotc;
        this.itemImage = itemImage;
    }

    // Parcelable을 구현하여 Pill 객체를 전달할 수 있도록 함
    protected Pill(Parcel in) {
        userId = in.readString();
        itemSeq = in.readString();
        itemName = in.readString();
        efcyQesitm = in.readString();
        atpnQesitm = in.readString();
        seQesitm = in.readString();
        etcotc = in.readString();
        itemImage = in.readString();
    }

    // CREATOR 객체를 통해 Parcel에서 객체를 생성할 수 있도록 설정
    public static final Creator<Pill> CREATOR = new Creator<Pill>() {
        @Override
        public Pill createFromParcel(Parcel in) {
            return new Pill(in);
        }

        @Override
        public Pill[] newArray(int size) {
            return new Pill[size];
        }
    };

    // Getter 및 Setter 메소드들
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemSeq() {
        return itemSeq;
    }

    public String getItemName() {
        return itemName;
    }

    public String getEfcyQesitm() {
        return efcyQesitm;
    }

    public String getAtpnQesitm() {
        return atpnQesitm;
    }

    public String getSeQesitm() {
        return seQesitm;
    }

    public String getEtcotc() {
        return etcotc;
    }

    public String getItemImage() {
        return itemImage;
    }

    // Parcelable 구현: 객체의 내용 설명 (일반적으로 0을 반환)
    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable 구현: 객체의 데이터를 Parcel에 쓰는 메소드
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(itemSeq);
        parcel.writeString(itemName);
        parcel.writeString(efcyQesitm);
        parcel.writeString(atpnQesitm);
        parcel.writeString(seQesitm);
        parcel.writeString(etcotc);
        parcel.writeString(itemImage);
    }
}
