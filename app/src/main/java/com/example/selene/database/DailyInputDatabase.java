package com.example.selene.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.selene.models.DailyInput;

/*
DailyInputDatabase extends RoomDatabase
This class builds the database
Contains an abstract reference to the Dao
Contains two migrations which were used to make changes to the database tables
 */

@Database(entities = {DailyInput.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class DailyInputDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "dailyInput_db";

    private static DailyInputDatabase instance;

    static DailyInputDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    DailyInputDatabase.class,
                    DATABASE_NAME
            ).addMigrations(MIGRATION_1_2, MIGRATION_2_3)
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

    static final Migration MIGRATION_2_3 = new Migration (2,3) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE DailyInputTable");

            database.execSQL("CREATE TABLE DailyInputTable_New(date INTEGER NOT NULL, bleeding TEXT, emotion TEXT, physical_feeling TEXT, note TEXT, PRIMARY KEY(date))");


            database.execSQL("ALTER TABLE DailyInputTable_New RENAME TO DailyInputTable");
        }
    };

}
