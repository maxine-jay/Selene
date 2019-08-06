package com.example.selene;

import android.app.DatePickerDialog;
import android.graphics.Color;
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

    //UI
    TextView output, review, dateView, enterEmotionTextView, enterPhysicalTextView;
    Spinner emotionSpinner, physicalFeelingSpinner;
    Button selectDateButton, backButton, saveButton, editButton;
    CheckBox bleedingCheckBox;

    //Vars
    String mBleeding;
    String mDate;
    String mEmotion;
    String mPhysical;
    ArrayList<DailyInput> mDailyInputs;

    private DailyInput newInput;
    private DailyInput mIncomingDailyInput;
    private boolean isNewDailyInput;

    private DailyInputRepository mDailyInputRepository;

    private int mode;

    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED =0;




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
        backButton = findViewById(R.id.btn_back);
        enterEmotionTextView = findViewById(R.id.enterEmotion);
        enterPhysicalTextView = findViewById(R.id.enterPhysical);
        bleedingCheckBox = findViewById(R.id.cb_bleeding);
        saveButton = findViewById(R.id.btn_save);
        editButton = findViewById(R.id.btn_edit);


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

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEditMode();
            }
        });


        //BLEEDING CHECKBOX ONCLICKLISTENER
        findViewById(R.id.cb_bleeding).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean checked = ((CheckBox) view).isChecked();

                        if (checked) {
                            mBleeding = "Bleeding";
                        } else {
                            mBleeding = "Not Bleeding";
                        }

                    }
                });

        setUpSpinners();

        if (getIncomingIntent()) {
            //new daily input incoming (should allow for NEW INPUT)

            setNewDailyInputFields();
            enableEditMode();

        } else {
            //already existing input incoming (should allow for EDIT)
            setExistingDailyInputFields();
            disableEditMode();
        }



    }

    private void saveChanges(){
        if(isNewDailyInput){
            saveNewDailyInput();
        }else{
            newInput.setDate(mIncomingDailyInput.getDate());

            updateDailyInput();
        }
    }

    private void saveNewDailyInput(){
        mDailyInputRepository.insertDailyInputTask(newInput);
    }

    private void updateDailyInput(){

        mDailyInputRepository.updateDailyInput(newInput);
    }


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

        mDate = String.format("%d-%02d-%02d", year, (month +1), dayOfMonth);
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
                mEmotion = adapterView.getSelectedItem().toString();
                break;
            case R.id.physical_spinner:
                Log.d("PHYSICAL_SPINNER_SLCT", adapterView.getSelectedItem().toString());
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

        //this is where we save the note
//        mDailyInputRepository.insertDailyInputTask(newInput);

        saveChanges();

        // this was used to check whether inputs were going into array
//        String final_selection;
//        final_selection = "";

//        for (DailyInput di : mDailyInputs) {
//            final_selection += (di.toString(di)) + "\n";
//
//
//        }
//
//        if (mDailyInputs.size() > 0) {
//
//            output.append(final_selection);
//
//        } else {
//            output.append("No data selected");
//        }


    }

    //checks if the intent from main activity is a new input or previously inputted
    private boolean getIncomingIntent() {
        if (getIntent().hasExtra("selected_input")) {
            mIncomingDailyInput = getIntent().getParcelableExtra("selected_input");

            newInput = new DailyInput();
            newInput.setDate(mIncomingDailyInput.getDate());
            newInput.setBleeding(mIncomingDailyInput.getBleeding());
            newInput.setEmotion(mIncomingDailyInput.getEmotion());
            newInput.setPhysicalFeeling(mIncomingDailyInput.getPhysicalFeeling());

            isNewDailyInput = false;
            return false;
        }

        isNewDailyInput = true;
        return true;
    }

    private void setNewDailyInputFields() {
        //sets specified fields for a new input

        dateView.setText("Please select a date");


    }

    private void setExistingDailyInputFields() {
        //sets specified fields for an already existing input. does not allow date change - can assume date is correct.
        //this means that the user cannot enter duplicate entries for one date...

        String viewModeDailyInputData = mIncomingDailyInput.getBleeding() + "\n"
                + "Emotional feeling: " + mIncomingDailyInput.getEmotion() + "\n"
                + "Physical feeling: " + mIncomingDailyInput.getPhysicalFeeling();

        dateView.setText(mIncomingDailyInput.getDate());
        output.setText(viewModeDailyInputData);
        output.setBackgroundColor(Color.parseColor("#4CAF50"));

        //sets spinner to selected value
        String emotionCompare = mIncomingDailyInput.getEmotion();
        ArrayAdapter<CharSequence> emotionAdapter = ArrayAdapter.createFromResource(this, R.array.emotions_array, android.R.layout.simple_spinner_item);
        emotionSpinner.setAdapter(emotionAdapter);
        if(emotionCompare != null){
            int emotionSpinnerPosition = emotionAdapter.getPosition(emotionCompare);
            emotionSpinner.setSelection(emotionSpinnerPosition);
        }

        String physicalCompare = mIncomingDailyInput.getPhysicalFeeling();
        ArrayAdapter<CharSequence> physicalAdapter = ArrayAdapter.createFromResource(this, R.array.physical_feelings_array, android.R.layout.simple_spinner_item);
        physicalFeelingSpinner.setAdapter(physicalAdapter);
        if(physicalCompare != null){
            int physicalSpinnerPosition = physicalAdapter.getPosition(physicalCompare);
            physicalFeelingSpinner.setSelection(physicalSpinnerPosition);
        }









    }

    private void enableEditMode(){
        bleedingCheckBox.setVisibility(View.VISIBLE);
        enterEmotionTextView.setVisibility(View.VISIBLE);
        enterPhysicalTextView.setVisibility(View.VISIBLE);
        emotionSpinner.setVisibility(View.VISIBLE);
        physicalFeelingSpinner.setVisibility(View.VISIBLE);
        review.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.GONE);
        saveButton.setVisibility(View.VISIBLE);

    }
//
//    private void enableInitialEditMode(){
//        selectDateButton.setVisibility(View.VISIBLE);
//        bleedingCheckBox.setVisibility(View.VISIBLE);
//        enterEmotionTextView.setVisibility(View.VISIBLE);
//        enterPhysicalTextView.setVisibility(View.VISIBLE);
//        emotionSpinner.setVisibility(View.VISIBLE);
//        physicalFeelingSpinner.setVisibility(View.VISIBLE);
//        review.setVisibility(View.VISIBLE);
//        editButton.setVisibility(View.GONE);
//        saveButton.setVisibility(View.VISIBLE);

//
//        mode = EDIT_MODE_ENABLED;
//
//    }

    private void disableEditMode(){
        selectDateButton.setVisibility(View.GONE);
        bleedingCheckBox.setVisibility(View.GONE);
        enterEmotionTextView.setVisibility(View.GONE);
        enterPhysicalTextView.setVisibility(View.GONE);
        emotionSpinner.setVisibility(View.GONE);
        physicalFeelingSpinner.setVisibility(View.GONE);
        review.setVisibility(View.GONE);
        output.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.GONE);
        editButton.setVisibility(View.VISIBLE);


        mode = EDIT_MODE_DISABLED;

    }


}
