package com.example.selene.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class DailyInput implements Parcelable {


    private String date;
    private String bleeding;
    private String emotion;
    private String physicalFeeling;


    public DailyInput(String date, String bleeding, String emotion, String physicalFeeling) {

        this.date = date;
        this.bleeding = bleeding;
        this.emotion = emotion;
        this.physicalFeeling = physicalFeeling;

    }

    public DailyInput(){

    }


    protected DailyInput(Parcel in) {
        date = in.readString();
        bleeding = in.readString();
        emotion = in.readString();
        physicalFeeling = in.readString();
    }

    public static final Creator<DailyInput> CREATOR = new Creator<DailyInput>() {
        @Override
        public DailyInput createFromParcel(Parcel in) {
            return new DailyInput(in);
        }

        @Override
        public DailyInput[] newArray(int size) {
            return new DailyInput[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBleeding() {
        return bleeding;
    }

    public void setBleeding(String bleeding) {
        this.bleeding = bleeding;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getPhysicalFeeling() {
        return physicalFeeling;
    }

    public void setPhysicalFeeling(String physicalFeeling) {
        this.physicalFeeling = physicalFeeling;
    }

    public String toString(DailyInput dailyInput) {
        return dailyInput.getDate() + " " + dailyInput.getBleeding() + " " + dailyInput.getEmotion() + " " + dailyInput.getPhysicalFeeling() + "\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(bleeding);
        parcel.writeString(emotion);
        parcel.writeString(physicalFeeling);
    }
}
