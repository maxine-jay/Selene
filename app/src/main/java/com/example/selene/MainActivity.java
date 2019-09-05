package com.example.selene;

import android.content.Intent;
import android.os.Bundle;
import com.example.selene.adapters.DailyInputRecyclerAdapter;
import com.example.selene.models.DailyInput;
import com.example.selene.database.DailyInputRepository;
import com.example.selene.utils.VerticalSpacingItemDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/*
MainActivity is linked with the layout file activity_main.xml
It contains methods to set up the RecyclerVew and methods that are used to interact with the ReyclerView
 */

public class MainActivity extends AppCompatActivity implements DailyInputRecyclerAdapter.OnDailyInputListener {

    private static final String TAG = "MainActivity";

    //UI components
    private RecyclerView mRecyclerView;
    private Toolbar toolbar;
    private FloatingActionButton addNewInputFAB;


    //vars

    private ArrayList<DailyInput> mDailyInputs = new ArrayList<>();
    private DailyInputRecyclerAdapter mDailyInputRecyclerAdapter;
    private DailyInputRepository mDailyInputRepository;
    private DailyInput mRecentlyDeletedItem;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sets the toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        mTextMessage = findViewById(R.id.message);

        addNewInputFAB = findViewById(R.id.add_new_input_fab);
        addNewInputFAB.setOnClickListener(new View.OnClickListener(){
            //takes user to DailyInputActivity to add new input
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, DailyInputActivity.class);
                startActivity(i);
            }
        });

        mDailyInputRepository = new DailyInputRepository(this);


        //Set up RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView_dailyItems);
        initRecyclerView();

        //get daily inputs from database
        retrieveDailyInputs();


    }
    /*
    onCreateOptionsMenu() specifies the menu buttons to be used in the toolbar for this activity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_buttons, menu);
        return true;
    }

    /*
    onOptionsItemSelected is used to define what happens when the user presses on a button on the toolbar
    In this case, it deals only with the calendar button and takes the user to the CalendarActivity
    If more buttons were to be added, a switch statement/if else statement could be used
     */
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
    /*
    retrieveDailyInputs() retrieves all items from the database
    notifies the adapter when the data set has changed (new inputs/deleted inputs)
     */
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

    /*
    initRecyclerView() initialises the RecyclerView.
     */
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        VerticalSpacingItemDecorator verticalSpacingItemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(verticalSpacingItemDecorator);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        mDailyInputRecyclerAdapter = new DailyInputRecyclerAdapter(mDailyInputs, this);
        mRecyclerView.setAdapter(mDailyInputRecyclerAdapter);
    }

    /*
    onDailyInputClick() determines what happens when the user clicks on an item in the RecyclerView
    In this case it takes them to the DailyInputActivity which will display data relating to the DailyInput item which was clicked
     */
    public void onDailyInputClick(int position) {

        Log.d(TAG, "onDailyInputClick: clicked: " + position);

        Intent intent = new Intent(this, DailyInputActivity.class);
        intent.putExtra("selected_input", mDailyInputs.get(position));
        startActivity(intent);
    }
    /*
    ItemTouchHelper is a utility class which adds methods for moving or swiping items
    onMove() is not utilised here
    onSwiped is used to delete a DailyInput item
     */
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            //Do not want to support the ability to move items up or down in RecyclerView because they are ordered by date
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            //When user swipes to the left, daily input will be deleted
            int position = viewHolder.getAdapterPosition();
            deleteDailyInput(mDailyInputs.get(position));

        }

        /*
        deleteDailyInput() removes a DailyInput item from mDailyInputs array and deletes it from the database
         */
        private void deleteDailyInput(DailyInput dailyInput) {
            mRecentlyDeletedItem = dailyInput;
            //remove from array
            mDailyInputs.remove(dailyInput);
            mDailyInputRecyclerAdapter.notifyDataSetChanged();
            //remove from database
            mDailyInputRepository.deleteDailyInput(dailyInput);
            //show undo snackbar so user has the opportunity to undo action
            showUndoSnackbar();
        }

    };

    /*
    showUndoSnackbar() is called when an item is deleted
    the snackbar has the action to undo a deletion, if the user selects this the undoDelete() method is called
     */
    private void showUndoSnackbar(){
        View view = findViewById(R.id.main_activity_view);
        Snackbar snackbar = Snackbar.make(view, "Item Deleted", Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoDelete();
            }
        });
        snackbar.show();
    }

    /*
    undoDelete() adds the recently deleted item to the mDailyInputs array list and inserts it back into the database
     */
    private void undoDelete(){
        mDailyInputs.add(mRecentlyDeletedItem);
        mDailyInputRepository.insertDailyInputTask(mRecentlyDeletedItem);
        mDailyInputRecyclerAdapter.notifyDataSetChanged();
    }


}

