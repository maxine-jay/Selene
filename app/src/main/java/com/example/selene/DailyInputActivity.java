package com.example.selene;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.selene.models.DailyInput;
import com.example.selene.database.DailyInputRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DailyInputActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "DailyInputActivity";

    //UI
    private TextView output, dateView, enterEmotionTextView, enterPhysicalTextView;
    private EditText enterNote;
    private Spinner emotionSpinner, physicalFeelingSpinner;
//    private Button saveButton, editButton;
    private ImageButton selectDateButton, finishNoteButton;
    private FloatingActionButton saveButton, editButton;
    private CheckBox bleedingCheckBox;

    //Vars
    private String mBleeding, mEmotion, mPhysical, mNote;
    private Date mDate;

    private List<DailyInput> mDailyInputs;

    private String isBleeding = "Bleeding";
    private String isNotBleeding = "Not Bleeding";
    private String noNoteEntered = "No note entered for today";

    private DailyInput newInput, mIncomingDailyInput;
    private boolean isNewDailyInput;

    private DailyInputRepository mDailyInputRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_input);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mDailyInputs = new ArrayList<>();
        mDailyInputRepository = new DailyInputRepository(this);

        //TextViews
        dateView = findViewById(R.id.dateView);
        enterEmotionTextView = findViewById(R.id.enterEmotion);
        enterPhysicalTextView = findViewById(R.id.enterPhysical);
        output = findViewById(R.id.testOutput);


        //Buttons
        selectDateButton = findViewById(R.id.btn_selectDate);
        saveButton = findViewById(R.id.btn_save);
        editButton = findViewById(R.id.btn_edit);
        enterNote = findViewById(R.id.enterNote);
        finishNoteButton = findViewById(R.id.btn_finishNote);

        //Checkboxes
        bleedingCheckBox = findViewById(R.id.cb_bleeding);



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

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                checkDateAndSave();
            }
        });

        finishNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enterNote.length() > 0) {
                    mNote = enterNote.getText().toString();
                    //hides keyboard and cursor
                    enterNote.setEnabled(false);
                } else {
                    mNote = noNoteEntered;
                    //hides keyboard and cursor
                    enterNote.setEnabled(false);
                }
                //must set enabled to account for likelihood that user may edit after clicking finishNoteButton
                enterNote.setEnabled(true);

            }
        });


        //BLEEDING CHECKBOX ONCLICKLISTENER
        findViewById(R.id.cb_bleeding).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean checked = ((CheckBox) view).isChecked();
                        if (checked) {
                            mBleeding = isBleeding;
                        } else {
                            mBleeding = isNotBleeding;
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        return false;
    }


    private void saveNewDailyInput() {
        mDailyInputRepository.insertDailyInputTask(newInput);
    }
//
//    private void updateDailyInput() {
//
//        mDailyInputRepository.updateDailyInput(newInput);
//    }


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

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        mDate = calendar.getTime();


        Log.d(TAG, mDate.toString());
        String formattedDate = DailyInput.formatDateToString(mDate);
        getSupportActionBar().setTitle(formattedDate);
        dateView.setText(formattedDate);
//        dateView.setText(DailyInput.formatDateToString(mDate));

    }

    public void setUpSpinners() {
        emotionSpinner = findViewById(R.id.emotion_spinner);
        emotionSpinner.setOnItemSelectedListener(this);

        //uses array and spinner item layout to fill spinner
       ArrayAdapter<CharSequence> emotionsAdapter = ArrayAdapter.createFromResource(this,
                R.array.emotions_array, R.layout.spinner_row_item);



        emotionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emotionSpinner.setAdapter(emotionsAdapter);




        physicalFeelingSpinner = findViewById(R.id.physical_spinner);
        physicalFeelingSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> physicalAdapter = ArrayAdapter.createFromResource(this,
                R.array.physical_feelings_array, R.layout.spinner_row_item);
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

        getSupportActionBar().setTitle(getString(R.string.add_new));
        String selectDate = "Please Select a Date";

        dateView.setText(selectDate);
        //if user does not check bleeding checkbox it can be assumed that they mean they are not bleeding,
        //this can be changed by editing the input
        if (mBleeding == null) {
            mBleeding = isNotBleeding;
        } else {
            mBleeding = isBleeding;
        }


    }

    private void setExistingDailyInputFields() {
        //sets specified fields for an already existing input. does not allow date change - can assume date is correct.
        //this means that the user cannot enter duplicate entries for one date...

        Date dateForTitle = mIncomingDailyInput.getDate();
        String formattedDate = DailyInput.formatDateToString(dateForTitle);
        String titleForEdit = formattedDate;
        getSupportActionBar().setTitle(titleForEdit);

        String viewModeDailyInputData = mIncomingDailyInput.getBleeding() + "\n"
                + "Emotional feeling: " + mIncomingDailyInput.getEmotion() + "\n"
                + "Physical feeling: " + mIncomingDailyInput.getPhysicalFeeling() + "\n"
                + "Note: " + mIncomingDailyInput.getNote();


        mDate = mIncomingDailyInput.getDate();

//        enterNote.setText(mIncomingDailyInput.getNote());
//        output.setBackgroundColor(Color.parseColor("#4CAF50"));

        output.setText(viewModeDailyInputData);

        //sets spinners to previously selected values
        setSpinnersToPreviouslySelectedValues();

        //sets checkbox to previously selected value
        setBleedingCheckBoxToPreviouslySelectedValue();

        if (mIncomingDailyInput.getNote().equals(noNoteEntered) || mIncomingDailyInput.getNote() == null) {
            enterNote.setText("");
        } else {
            enterNote.setText(mIncomingDailyInput.getNote());
        }

    }

    private void enableEditMode() {
        bleedingCheckBox.setVisibility(View.VISIBLE);
        enterEmotionTextView.setVisibility(View.VISIBLE);
        enterPhysicalTextView.setVisibility(View.VISIBLE);
        emotionSpinner.setVisibility(View.VISIBLE);
        physicalFeelingSpinner.setVisibility(View.VISIBLE);
        editButton.hide();
        output.setVisibility(View.GONE);
        saveButton.show();
        enterNote.setVisibility(View.VISIBLE);
        finishNoteButton.setVisibility(View.VISIBLE);

    }

    private void disableEditMode() {
        dateView.setVisibility(View.GONE);
        selectDateButton.setVisibility(View.GONE);
        bleedingCheckBox.setVisibility(View.GONE);
        enterEmotionTextView.setVisibility(View.GONE);
        enterPhysicalTextView.setVisibility(View.GONE);
        emotionSpinner.setVisibility(View.GONE);
        physicalFeelingSpinner.setVisibility(View.GONE);
        saveButton.hide();
        editButton.show();
        enterNote.setVisibility(View.GONE);
        finishNoteButton.setVisibility(View.GONE);
    }

    private void setSpinnersToPreviouslySelectedValues() {
        String emotionCompare = mIncomingDailyInput.getEmotion();
        ArrayAdapter<CharSequence> emotionAdapter = ArrayAdapter.createFromResource(this, R.array.emotions_array, android.R.layout.simple_spinner_dropdown_item);
        emotionSpinner.setAdapter(emotionAdapter);
        if (emotionCompare != null) {
            int emotionSpinnerPosition = emotionAdapter.getPosition(emotionCompare);
            emotionSpinner.setSelection(emotionSpinnerPosition);
        }

        String physicalCompare = mIncomingDailyInput.getPhysicalFeeling();
        ArrayAdapter<CharSequence> physicalAdapter = ArrayAdapter.createFromResource(this, R.array.physical_feelings_array, android.R.layout.simple_spinner_dropdown_item);
        physicalFeelingSpinner.setAdapter(physicalAdapter);
        if (physicalCompare != null) {
            int physicalSpinnerPosition = physicalAdapter.getPosition(physicalCompare);
            physicalFeelingSpinner.setSelection(physicalSpinnerPosition);
        }
    }

    private void setBleedingCheckBoxToPreviouslySelectedValue() {
        String checkBleeding = mIncomingDailyInput.getBleeding();
        if (checkBleeding == null || checkBleeding.equals(isNotBleeding)) {
            bleedingCheckBox.setChecked(false);
            mBleeding = isNotBleeding;
        } else if (checkBleeding.equals(isBleeding)) {
            bleedingCheckBox.setChecked(true);
            mBleeding = isBleeding;
        }
    }

    private void checkDateAndSave() {
        Context context = getApplicationContext();
        CharSequence text = "You must pick a date!";
        int duration = Toast.LENGTH_SHORT;

        if (mDate == null) {
            Toast pickDate = Toast.makeText(context, text, duration);
            pickDate.show();
        } else {

            if (mNote == null || mNote.length() == 0) {
                mNote = noNoteEntered;
            }
            newInput = new DailyInput(mDate, mBleeding, mEmotion, mPhysical, mNote);
            mDailyInputs.add(newInput);

            saveNewDailyInput();
            showToastAndGoBackToMainPage();

        }
    }

    private void showToastAndGoBackToMainPage() {
        Context context = getApplicationContext();
        CharSequence text = "Saved!";
        int duration = Toast.LENGTH_SHORT;

        Toast save = Toast.makeText(context, text, duration);
        save.show();
        Intent backToMain = new Intent(DailyInputActivity.this, MainActivity.class);
        startActivity(backToMain);

    }
}
