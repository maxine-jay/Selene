package com.example.selene.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.selene.Models.DailyInput;

@Database(entities = {DailyInput.class}, version = 1)
public abstract class DailyInputDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "dailyInput_db";

    private static DailyInputDatabase instance;

    static DailyInputDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    DailyInputDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract DailyInputDao getDailyInputDao();
}
