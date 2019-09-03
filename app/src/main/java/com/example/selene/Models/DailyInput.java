package com.example.selene.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
DailyInput is the model used for creating DailyInput objects
It is also an Entity in the DailyInputDatabase
It contains a custom toString() method and a method to format the date to a String for display purposes
It contains methods to implement Parcelable which allows DailyInput objects to be parsed by the database
 */
@Entity(tableName = "DailyInputTable")
public class DailyInput implements Parcelable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="date")
    private Date date;
    @ColumnInfo(name="bleeding")
    private String bleeding;
    @ColumnInfo(name="emotion")
    private String emotion;
    @ColumnInfo(name="physical_feeling")
    private String physicalFeeling;
    @ColumnInfo(name = "note")
    private String note;

    @Ignore
    private static final String DATE_FORMAT = "dd/MM/yyyy";


    public DailyInput(Date date, String bleeding, String emotion, String physicalFeeling, String note) {

        this.date = date;
        this.bleeding = bleeding;
        this.emotion = emotion;
        this.physicalFeeling = physicalFeeling;
        this.note = note;

    }

    @Ignore
    public DailyInput(){
        //this constructor can be used to set up using setters rather than parameters if needed

    }



    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public static String toString(DailyInput dailyInput) {
        return " Bleeding: " + dailyInput.getBleeding()
                + "\n Emotional feeling: " + dailyInput.getEmotion()
                + "\n Physical Feeling: " + dailyInput.getPhysicalFeeling()
                + "\n Note: " + dailyInput.getNote();
    }


    public static String formatDateToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String formattedDate = dateFormat.format(date);

        return formattedDate;
    }

    //read from parcel
    protected DailyInput(Parcel in) {
        date = new Date(in.readLong());
        bleeding = in.readString();
        emotion = in.readString();
        physicalFeeling = in.readString();
        note = in.readString();
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //convert date to long
        parcel.writeLong(date.getTime());
        parcel.writeString(bleeding);
        parcel.writeString(emotion);
        parcel.writeString(physicalFeeling);
        parcel.writeString(note);
    }
}
