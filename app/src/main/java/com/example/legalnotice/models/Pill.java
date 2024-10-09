package com.example.legalnotice.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Pill implements Parcelable {
    private String userId;
    private String itemSeq;
    private String itemName;
    private String efcyQesitm;
    private String atpnQesitm;
    private String seQesitm;
    private String etcotc;
    private String itemImage;

    // 생성자
    public Pill(String itemSeq, String itemName, String efcyQesitm, String atpnQesitm, String seQesitm, String etcotc, String itemImage) {
        this.itemSeq = itemSeq;
        this.itemName = itemName;
        this.efcyQesitm = efcyQesitm;
        this.atpnQesitm = atpnQesitm;
        this.seQesitm = seQesitm;
        this.etcotc = etcotc;
        this.itemImage = itemImage;
    }

    // Parcelable을 구현하여 객체를 전달할 수 있도록 함
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

    // Getter 및 Setter 메소드
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

    @Override
    public int describeContents() {
        return 0;
    }

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
