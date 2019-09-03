package com.example.selene.async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.selene.models.DailyInput;
import com.example.selene.database.DailyInputDao;

//inserts a daily input item into the database on a background thread to avoid the database crashing

public class InsertAsyncTask extends AsyncTask<DailyInput, Void, Void> {

    private static final String TAG = "InsertAsyncTask";


    private DailyInputDao mDailyInputDao;
    public InsertAsyncTask(DailyInputDao dao) {
        mDailyInputDao = dao;

    }

    @Override
    protected Void doInBackground(DailyInput... dailyInputs) {
        Log.d(TAG, "doInBackground: thread" + Thread.currentThread().getName());
        mDailyInputDao.insertDailyInput(dailyInputs);
        return null;
    }
}
