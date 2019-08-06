package com.example.selene.async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.selene.Models.DailyInput;
import com.example.selene.Room.DailyInputDao;

//does database stuff on background thread so wont crash app

public class UpdateAsyncTask extends AsyncTask<DailyInput, Void, Void> {

    private static final String TAG = "InsertAsyncTask";


    private DailyInputDao mDailyInputDao;
    public UpdateAsyncTask(DailyInputDao dao) {
        mDailyInputDao = dao;

    }

    @Override
    protected Void doInBackground(DailyInput... dailyInputs) {
        Log.d(TAG, "doInBackground: thread" + Thread.currentThread().getName());
        mDailyInputDao.update(dailyInputs);
        return null;
    }
}