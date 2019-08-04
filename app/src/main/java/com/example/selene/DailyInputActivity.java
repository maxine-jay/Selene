package com.example.selene;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selene.Models.DailyInput;
import com.example.selene.Room.DailyInputRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DailyInputActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "DailyInputActivity";

    //UI elements
    TextView output, review, dateView;
    Spinner emotionSpinner, physicalFeelingSpinner;
    Button selectDateButton, backButton;

    //Vars
    String mBleeding;
    String mDate;
    String mEmotion;
    String mPhysical;

    ArrayList<DailyInput> mDailyInputs;
    DailyInput newInput;

    private DailyInput mInitialDailyInput;
    private boolean isNewDailyInput;

    private DailyInputRepository mDailyInputRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_input);

        mDailyInputs = new ArrayList<>();

        mDailyInputRepository = new DailyInputRepository(this);

        dateView = findViewById(R.id.dateView);
        review = findViewById(R.id.review);
        output = findViewById(R.id.testOutput);
        selectDateButton = findViewById(R.id.btn_selectDate);
        backButton = findViewById(R.id.backButton);

        //destroys activity on selection of back button
        //needs to be clear to user that any input will be destroyed
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //SELECT DATE ONCLICKLISTENER
        selectDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePickerDialog();
                    }
                }
        );

        //BLEEDING CHECKBOX ONCLICKLISTENER
        findViewById(R.id.cb_bleeding).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean checked = ((CheckBox) view).isChecked();
                        String toast = "";
                        if (checked) {
                            mBleeding = "Bleeding";
                            Log.d("IS_BLEEDING", mBleeding);
                        } else if (!checked) {
                            mBleeding = "Not Bleeding";
                        } else {
                            mBleeding = "No input for bleeding";

                        }

                    }
                });

        setUpSpinners();

        if (getIncomingIntent()) {
            //new daily input incoming (should allow for NEW INPUT)
            //currently there is nothing here...but it's working. should add in some code???
            setNewDailyInputProperties();

        } else {
            //already existing input incoming (should allow for EDIT)
            setExistingDailyInputProperties();
        }

    }

//    private void saveChanges(){
//        if(isNewDailyInput){
//            saveNewDailyInput();
//        }else{
//
//        }
//    }
//
//    private void saveNewDailyInput(){
//        mDailyInputRepository.insertDailyInputTask(mInitialDailyInput);
//    }


    //DATEPICKERDIALOG
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

        mDate = dayOfMonth + "/" + (month + 1) + "/" + year;
        dateView.setText(mDate);

    }

    public void setUpSpinners() {
        emotionSpinner = findViewById(R.id.emotion_spinner);
        emotionSpinner.setOnItemSelectedListener(this);


        ArrayAdapter<CharSequence> emotionsAdapter = ArrayAdapter.createFromResource(this,
                R.array.emotions_array, android.R.layout.simple_spinner_item);
        emotionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emotionSpinner.setAdapter(emotionsAdapter);

        physicalFeelingSpinner = findViewById(R.id.physical_spinner);
        physicalFeelingSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> physicalAdapter = ArrayAdapter.createFromResource(this,
                R.array.physical_feelings_array, android.R.layout.simple_spinner_item);
        emotionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        physicalFeelingSpinner.setAdapter(physicalAdapter);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()) {
            case R.id.emotion_spinner:
                Log.d("EMOTION_SPINNER_SLCT", adapterView.getSelectedItem().toString());
//                review.setText("Emotion " + adapterView.getSelectedItem().toString());
                mEmotion = adapterView.getSelectedItem().toString();
                break;
            case R.id.physical_spinner:
                Log.d("PHYSICAL_SPINNER_SLCT", adapterView.getSelectedItem().toString());
//                review.setText("Physical " + adapterView.getSelectedItem().toString());
                mPhysical = adapterView.getSelectedItem().toString();
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void onSaveButtonClicked(View view) {

        newInput = new DailyInput(mDate, mBleeding, mEmotion, mPhysical);
        mDailyInputs.add(newInput);
        review.setText(newInput.toString(newInput));
        mDailyInputRepository.insertDailyInputTask(newInput);

//        saveChanges();

        String final_selection;
        final_selection = "";

        for (DailyInput di : mDailyInputs) {
            final_selection += (di.toString(di)) + "\n";


        }

        if (mDailyInputs.size() > 0) {

            output.append(final_selection);

        } else {
            output.append("No data selected");
        }


    }


    private boolean getIncomingIntent() {
        if (getIntent().hasExtra("selected_input")) {
            mInitialDailyInput = getIntent().getParcelableExtra("selected_input");

            isNewDailyInput = false;
            return false;
        }

        isNewDailyInput = true;
        return true;
    }

    private void setNewDailyInputProperties() {
        //sets specified fields for a new input
        dateView.setText("Please select a date");
    }

    private void setExistingDailyInputProperties() {
        //sets specified fields for an already existing input. does not allow date change - can assume date is correct.
        //this means that the user cannot enter duplicate entries for one date...

        dateView.setText(mInitialDailyInput.getDate());
        selectDateButton.setVisibility(View.GONE);

    }
}
