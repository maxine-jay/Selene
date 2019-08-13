package com.example.selene.room;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimeStamp(Long value){
        if (value == null){
            return null;
        }
        else {
            return new Date(value);
        }
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date){
        if (date == null) {
            return null;
        }
        else {
            return date.getTime();
        }
    }
}
