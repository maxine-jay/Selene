package com.example.selene.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.selene.models.DailyInput;


import java.util.List;

@Dao
public interface DailyInputDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDailyInput(DailyInput... dailyInputs);

    @Query("SELECT * FROM DailyInputTable ORDER BY date DESC")
    LiveData<List<DailyInput>> getDailyInputs();

    @Update
    int update(DailyInput... dailyInputs);

    @Delete
    int delete(DailyInput... dailyInputs);

    @Query("SELECT * FROM DailyInputTable WHERE bleeding = 'Bleeding'")
    LiveData<List<DailyInput>> getBleedingInputs();


}
