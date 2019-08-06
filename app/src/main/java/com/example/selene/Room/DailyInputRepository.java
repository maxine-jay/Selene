package com.example.selene.Room;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.selene.Models.DailyInput;
import com.example.selene.async.DeleteAsyncTask;
import com.example.selene.async.InsertAsyncTask;
import com.example.selene.async.UpdateAsyncTask;

import java.util.List;

public class DailyInputRepository {

    private DailyInputDatabase mDailyInputDatabase;

    public DailyInputRepository(Context context) {

        mDailyInputDatabase = DailyInputDatabase.getInstance(context);
    }

    public void insertDailyInputTask(DailyInput dailyInput) {
        new InsertAsyncTask(mDailyInputDatabase.getDailyInputDao()).execute(dailyInput);

    }

    public void updateDailyInput(DailyInput dailyInput) {
        new UpdateAsyncTask(mDailyInputDatabase.getDailyInputDao()).execute(dailyInput);
    }

    public LiveData<List<DailyInput>> retrieveDailyInputTask() {
        return mDailyInputDatabase.getDailyInputDao().getDailyInputs();
    }

    public void deleteDailyInput(DailyInput dailyInput) {
        new DeleteAsyncTask(mDailyInputDatabase.getDailyInputDao()).execute(dailyInput);

    }
}
