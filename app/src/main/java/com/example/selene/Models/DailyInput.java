package com.example.selene.Models;


public class DailyInput {


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
}
