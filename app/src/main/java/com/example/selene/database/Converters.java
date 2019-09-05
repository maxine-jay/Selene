package com.example.selene.database;

import androidx.room.TypeConverter;

import java.util.Date;

/*
 * Code referenced from: https://developer.android.com/training/data-storage/room/referencing-data
 *
 * Converters to be used to convert Date to long and vice versa for use with Room database
 */

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
