package com.example.selene;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import com.example.selene.adapters.DailyInputRecyclerAdapter;
import com.example.selene.models.DailyInput;
import com.example.selene.room.DailyInputRepository;
import com.example.selene.Util.VerticalSpacingItemDecorator;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DailyInputRecyclerAdapter.OnDailyInputListener {

    private static final String TAG = "MainActivity";

    //UI components
    private RecyclerView mRecyclerView;
    private Button addNewButton;


    //vars
    private TextView mTextMessage;
    private ArrayList<DailyInput> mDailyInputs = new ArrayList<>();
    private DailyInputRecyclerAdapter mDailyInputRecyclerAdapter;
    private DailyInputRepository mDailyInputRepository;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextMessage = findViewById(R.id.message);
        addNewButton = findViewById(R.id.button_addNew);

        mDailyInputRepository = new DailyInputRepository(this);

        addNewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DailyInputActivity.class);
                startActivity(i);
            }
        });

        //Set up RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView_dailyItems);
        initRecyclerView();

        //get daily inputs from database
        retrieveDailyInputs();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){

        int id = item.getItemId();

        if(id == R.id.action_calendar){
            Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void retrieveDailyInputs() {
        mDailyInputRepository.retrieveDailyInputTask().observe(this, new Observer<List<DailyInput>>() {
            @Override
            public void onChanged(List<DailyInput> dailyInputs) {

                if (mDailyInputs.size() > 0) {
                    mDailyInputs.clear();
                }
                if (dailyInputs != null) {
                    mDailyInputs.addAll(dailyInputs);
                }
                mDailyInputRecyclerAdapter.notifyDataSetChanged();

            }
        });
    }


    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        mDailyInputRecyclerAdapter = new DailyInputRecyclerAdapter(mDailyInputs, this);
        mRecyclerView.setAdapter(mDailyInputRecyclerAdapter);
    }


    public void onDailyInputClick(int position) {

        Log.d(TAG, "onDailyInputClick: clicked: " + position);

        Intent intent = new Intent(this, DailyInputActivity.class);
        intent.putExtra("selected_input", mDailyInputs.get(position));
        startActivity(intent);
    }

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            //Do not want to support the ability to move items up or down in RecyclerView because they are ordered by date
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            //When user swipes to the left, daily input will be deleted
            deleteDailyInput(mDailyInputs.get(viewHolder.getAdapterPosition()));

        }


        private void deleteDailyInput(DailyInput dailyInput) {
            //remove from array
            mDailyInputs.remove(dailyInput);
            mDailyInputRecyclerAdapter.notifyDataSetChanged();
            //remove from database
            mDailyInputRepository.deleteDailyInput(dailyInput);
        }


        public void goToCalendar(View view) {
            Intent i = new Intent(MainActivity.this, CalendarActivity.class);
            startActivity(i);

        }
    };
}

