package com.example.selene;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.selene.database.DailyInputDao;
import com.example.selene.database.DailyInputDatabase;

import org.junit.After;
import org.junit.Before;
/*
JUnit4 is being used here as opposed to JUnit5 because JUnit5 requires API 26+
 */
public abstract class DatabaseTest {

    //the database that needs to be tested
    private DailyInputDatabase dailyInputDatabase;

    //most things that need to be tested are in the Dao, so necessary to have getter
    public DailyInputDao getDailyInputDao(){
        return dailyInputDatabase.getDailyInputDao();
    }

    // creates a temporary database used for testing, constructs database for as long as app is live then destroys it
    @Before
    public void init(){
        dailyInputDatabase = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                DailyInputDatabase.class
        ).build();
    }

    @After
    public void finish(){
        dailyInputDatabase.close();
    }
}
