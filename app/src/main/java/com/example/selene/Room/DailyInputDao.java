package com.example.selene.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.selene.Models.DailyInput;

import java.util.List;

@Dao
public interface DailyInputDao {

    @Insert
    void insertDailyInput(DailyInput... dailyInputs);

    @Query("SELECT * FROM DailyInputTable")
    LiveData<List<DailyInput>> getDailyInputs();

    @Update
    int update(DailyInput... dailyInputs);

    @Delete
    int delete(DailyInput... dailyInputs);
}
