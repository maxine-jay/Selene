package com.example.selene;

import android.os.Bundle;

import com.example.selene.Adapters.DailyInputRecyclerAdapter;
import com.example.selene.Models.DailyInput;
import com.example.selene.Util.VerticalSpacingItemDecorator;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //UI components
    private RecyclerView mRecyclerView;

    //vars
    private TextView mTextMessage;
    private ArrayList<DailyInput> mDailyInputs = new ArrayList<>();
    private DailyInputRecyclerAdapter mDailyInputRecyclerAdapter;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Set up RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView_dailyItems);
        initRecyclerView();

        //Insert fake data to test
        insertFakeDailyData();

    }

    private void insertFakeDailyData(){

        for (int i =0; i<100;i++){
            DailyInput dailyInput = new DailyInput();
            dailyInput.setDate("Date " + i + "2019");
            dailyInput.setBleeding("Bleeding " +i);
            dailyInput.setEmotion("Emotion " + i);
            dailyInput.setPhysicalFeeling("Physical " + i);
            mDailyInputs.add(dailyInput);
        }
        mDailyInputRecyclerAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        mDailyInputRecyclerAdapter = new DailyInputRecyclerAdapter(mDailyInputs);
        mRecyclerView.setAdapter(mDailyInputRecyclerAdapter);
    }

}
