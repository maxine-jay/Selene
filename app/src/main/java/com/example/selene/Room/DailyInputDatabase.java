package com.example.selene.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.selene.Models.DailyInput;

@Database(entities = {DailyInput.class}, version = 2)
public abstract class DailyInputDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "dailyInput_db";

    private static DailyInputDatabase instance;

    static DailyInputDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    DailyInputDatabase.class,
                    DATABASE_NAME
            ).addMigrations(MIGRATION_1_2)
                    .build();
        }
        return instance;
    }

    public abstract DailyInputDao getDailyInputDao();


    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE DailyInputTable"
                    + " ADD COLUMN note TEXT");
        }
    };

}
