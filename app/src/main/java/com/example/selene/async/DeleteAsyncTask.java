package com.example.selene.async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.selene.models.DailyInput;
import com.example.selene.database.DailyInputDao;

//does database stuff on background thread so wont crash app

public class DeleteAsyncTask extends AsyncTask<DailyInput, Void, Void> {

    private static final String TAG = "InsertAsyncTask";


    private DailyInputDao mDailyInputDao;
    public DeleteAsyncTask(DailyInputDao dao) {
        mDailyInputDao = dao;

    }

    @Override
    protected Void doInBackground(DailyInput... dailyInputs) {
        Log.d(TAG, "doInBackground: thread" + Thread.currentThread().getName());
        mDailyInputDao.delete(dailyInputs);
        return null;
    }
}
