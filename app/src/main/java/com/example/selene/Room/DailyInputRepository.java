package com.example.selene.Room;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.selene.Models.DailyInput;

import java.util.List;

public class DailyInputRepository {

    private DailyInputDatabase mDailyInputDatabase;

    public DailyInputRepository(Context context) {

        mDailyInputDatabase = DailyInputDatabase.getInstance(context);
    }

    public void insertDailyInputTask(DailyInput dailyInput) {

    }

    public void updateDailyInput(DailyInput dailyInput) {

    }

    public LiveData<List<DailyInput>> retrieveDailyInputTask() {
        return mDailyInputDatabase.getDailyInputDao().getDailyInputs();
    }

    public void deleteDailyInput(DailyInput dailyInput) {

    }
}
