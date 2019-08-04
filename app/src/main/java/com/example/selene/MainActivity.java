package com.example.selene;

import android.content.Intent;
import android.os.Bundle;

import com.example.selene.Adapters.DailyInputRecyclerAdapter;
import com.example.selene.Models.DailyInput;
import com.example.selene.Room.DailyInputRepository;
import com.example.selene.Util.VerticalSpacingItemDecorator;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DailyInputRecyclerAdapter.OnDailyInputListener {

    private static final String TAG = "MainActivity";

    //UI components
    private RecyclerView mRecyclerView;
    Button addNewButton;

    //vars
    private TextView mTextMessage;
    private ArrayList<DailyInput> mDailyInputs = new ArrayList<>();
    private DailyInputRecyclerAdapter mDailyInputRecyclerAdapter;
    private DailyInputRepository mDailyInputRepository;



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
        addNewButton = findViewById(R.id.button_addNew);

        mDailyInputRepository = new DailyInputRepository(this);

        addNewButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this, DailyInputActivity.class);
                startActivity(i);
            }
        });

        //Set up RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView_dailyItems);
        initRecyclerView();

        //get daily inputs from database
        retrieveDailyInputs();

        //Insert fake data to test
//        insertFakeDailyData();

    }

    private void retrieveDailyInputs(){
        mDailyInputRepository.retrieveDailyInputTask().observe(this, new Observer<List<DailyInput>>() {
            @Override
            public void onChanged(List<DailyInput> dailyInputs) {

                if(mDailyInputs.size()>0){
                    mDailyInputs.clear();
                }
                if(dailyInputs != null){
                    mDailyInputs.addAll(dailyInputs);
                }
                mDailyInputRecyclerAdapter.notifyDataSetChanged();

            }
        });
    }

    private void insertFakeDailyData(){

        for (int i =0; i<100;i++){
            DailyInput dailyInput = new DailyInput();
            dailyInput.setDate("Date " + i + "/2019");
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
        mDailyInputRecyclerAdapter = new DailyInputRecyclerAdapter(mDailyInputs, this);
        mRecyclerView.setAdapter(mDailyInputRecyclerAdapter);
    }


    public void onDailyInputClick (int position){

        Log.d(TAG, "onDailyInputClick: clicked: " + position);

        Intent intent = new Intent(this, DailyInputActivity.class);
        intent.putExtra("selected_input", mDailyInputs.get(position));
        startActivity(intent);
    }

}
