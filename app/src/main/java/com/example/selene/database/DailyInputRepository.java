package com.example.selene.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.selene.models.DailyInput;
import com.example.selene.async.DeleteAsyncTask;
import com.example.selene.async.InsertAsyncTask;
import com.example.selene.async.UpdateAsyncTask;

import java.util.List;

/**
DailyInputRepository acts as an intermediary between the DailyInputActivity and the
database. It helps to keep the code clean, rather than calling AsyncTask methods straight
from the DailyInputActivity.

This isn't absolutely necessary in this program, but if the program were to grow,
it would be invaluable for the clarity of the program
 */
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

    public LiveData<List<DailyInput>> retrieveBleedingInputTask(){
        return mDailyInputDatabase.getDailyInputDao().getBleedingInputs();

    }





}
