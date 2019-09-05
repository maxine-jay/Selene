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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.selene.database.DailyInputRepository;
import com.example.selene.models.DailyInput;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
DailyInputActivity is linked with activity_daily_input and contains the logic for inputting a new
daily input or viewing a previously inputted daily input depending on interaction with
MainActivity
 */

public class DailyInputActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    //a tag to be used when logging tests
    private static final String TAG = "DailyInputActivity";
    private static final String IS_BLEEDING = "Bleeding";
    private static final String NOT_BLEEDING = "Not Bleeding";
    private static final String NO_NOTE_ENTERED = "No note entered for today";
    private static final String BLANK_STRING = "";
    //declare UI features
    private TextView output, dateView, enterEmotionTextView, enterPhysicalTextView;
    private EditText enterNote;
    private Spinner emotionSpinner, physicalFeelingSpinner;
    private ImageButton selectDateButton, finishNoteButton;
    private FloatingActionButton saveButton, editButton;
    private CheckBox bleedingCheckBox;
    //declare variables
    private String mBleeding, mEmotion, mPhysical, mNote;
    private Date mDate;
    private List<DailyInput> mDailyInputs;
    private DailyInput newInput, mIncomingDailyInput;
    private DailyInputRepository mDailyInputRepository;
    private int mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_input);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //instantiate ArrayList and Repository
        mDailyInputs = new ArrayList<>();
        mDailyInputRepository = new DailyInputRepository(this);

        //instantiate TextViews
        dateView = findViewById(R.id.dateView);
        enterEmotionTextView = findViewById(R.id.enterEmotion);
        enterPhysicalTextView = findViewById(R.id.enterPhysical);
        output = findViewById(R.id.testOutput);


        //instantiate Buttons
        selectDateButton = findViewById(R.id.btn_selectDate);
        saveButton = findViewById(R.id.btn_save);
        editButton = findViewById(R.id.btn_edit);
        enterNote = findViewById(R.id.enterNote);
        finishNoteButton = findViewById(R.id.btn_finishNote);

        //instantiate checkbox
        bleedingCheckBox = findViewById(R.id.cb_bleeding);


        //set onClickListeners for various buttons
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
                    mNote = NO_NOTE_ENTERED;
                    //hides keyboard and cursor
                    enterNote.setEnabled(false);
                }
                //must set enabled to account for likelihood that user may edit after clicking finishNoteButton
                enterNote.setEnabled(true);

            }
        });


        bleedingCheckBox.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean checked = ((CheckBox) view).isChecked();
                        if (checked) {
                            mBleeding = IS_BLEEDING;
                        } else {
                            mBleeding = NOT_BLEEDING;
                        }

                    }
                });

        //initialises the spinners for inputting emotional and physical feelings
        initSpinners();

        /*
        If the incoming intent is a new input (the user selected addNewFAB), then the views for inputting a new daily input
        are displayed. If the incoming intent is an already existing input, the views for viewing that daily input are displayed.
         */
        if (isNewDailyInput()) {
            //new daily input incoming
            setNewDailyInputViews();
            enableEditMode();

        } else {
            //already existing input incoming
            setExistingDailyInputViews();
            disableEditMode();
        }


    }

    /*
    showDatePickerDialog() displays the date picker which the user can use to select a date
    The maximum date is set to today's date because it should not be possible for users to input
    data relating to future dates which they have not experienced yet
     */
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

    /*
    onDateSet() gets the user selected date, instantiates mDate with the value, formats the date
    to a string for display, sets the title in the action bar and displays the date in the dateView text view
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //time of day needs to be set to avoid duplicate entries
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        mDate = calendar.getTime();


        String formattedDate = DailyInput.formatDateToString(mDate);
        //for testing
        Log.d(TAG, formattedDate);

        getSupportActionBar().setTitle(formattedDate);
        dateView.setText(formattedDate);

    }

    /*
    initSpinners() uses the emotion_spinner.xml layout file and the emotions_array and the physical_feelings_array to populate
    Storing the arrays in strings.xml file means that if the app was to be utilised in different countries, it would be simple to
    utilise arrays containing values written in different languages
     */
    public void initSpinners() {
        emotionSpinner = findViewById(R.id.emotion_spinner);
        emotionSpinner.setOnItemSelectedListener(this);

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

    //stores the selected value from the spinner as either mEmotion or mPhysical
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
        //do nothing
    }


    //checks if the intent from main activity is a new input or previously inputted
    private boolean isNewDailyInput() {
        if (getIntent().hasExtra("selected_input")) {
            mIncomingDailyInput = getIntent().getParcelableExtra("selected_input");
            newInput = new DailyInput();
            newInput.setDate(mIncomingDailyInput.getDate());
            newInput.setBleeding(mIncomingDailyInput.getBleeding());
            newInput.setEmotion(mIncomingDailyInput.getEmotion());
            newInput.setPhysicalFeeling(mIncomingDailyInput.getPhysicalFeeling());
            return false;
        }
        return true;
    }

    //sets views which enable the user to edit a daily input
    private void enableEditMode() {
        //checkbox
        bleedingCheckBox.setVisibility(View.VISIBLE);
        //text views
        enterEmotionTextView.setVisibility(View.VISIBLE);
        enterPhysicalTextView.setVisibility(View.VISIBLE);
        output.setVisibility(View.GONE);
        dateView.setVisibility(View.VISIBLE);
        //edit text
        enterNote.setVisibility(View.VISIBLE);
        //spinners
        emotionSpinner.setVisibility(View.VISIBLE);
        physicalFeelingSpinner.setVisibility(View.VISIBLE);
        //buttons
        finishNoteButton.setVisibility(View.VISIBLE);
        saveButton.show();
        editButton.hide();

        mode = 0;

    }

    //sets views which disable edit mode but allow the user to view the input item they selected on the MainActivity
    private void disableEditMode() {
        //checkbox
        bleedingCheckBox.setVisibility(View.GONE);
        //text views
        output.setVisibility(View.VISIBLE);
        enterEmotionTextView.setVisibility(View.GONE);
        enterPhysicalTextView.setVisibility(View.GONE);
        dateView.setVisibility(View.GONE);
        //edit text
        enterNote.setVisibility(View.GONE);
        //spinners
        emotionSpinner.setVisibility(View.GONE);
        physicalFeelingSpinner.setVisibility(View.GONE);
        //buttons
        editButton.show();
        saveButton.hide();
        selectDateButton.setVisibility(View.GONE);
        finishNoteButton.setVisibility(View.GONE);

        mode = 1;
    }

    private void setNewDailyInputViews() {
        //sets specified fields for a new input

        getSupportActionBar().setTitle(getString(R.string.add_new));
        String selectDate = "Please Select a Date";

        dateView.setText(selectDate);
        //if user does not check bleeding checkbox it can be assumed that they mean they are not bleeding,
        //this can be changed by editing the input
        if (mBleeding == null) {
            mBleeding = NOT_BLEEDING;
        } else {
            mBleeding = IS_BLEEDING;
        }


    }

    //sets specified fields for an already existing input. does not allow date change - can assume date is correct.
    //this means that the user cannot enter duplicate entries for one date...
    private void setExistingDailyInputViews() {


        Date dateForTitle = mIncomingDailyInput.getDate();
        String formattedDate = DailyInput.formatDateToString(dateForTitle);
        String titleForEdit = formattedDate;
        getSupportActionBar().setTitle(titleForEdit);

        dateView.setHint("Edit data for " + formattedDate);

        mDate = mIncomingDailyInput.getDate();

        //creates a string from the incoming daily input
        String viewModeDailyInputData = DailyInput.toString(mIncomingDailyInput);
        output.setText(viewModeDailyInputData);


        //sets spinners to previously selected values
        setSpinnersToPreviouslySelectedValues();

        //sets checkbox to previously selected value
        setBleedingCheckBoxToPreviouslySelectedValue();

        if (mIncomingDailyInput.getNote().equals(NO_NOTE_ENTERED) || mIncomingDailyInput.getNote() == null) {
            enterNote.setText(BLANK_STRING);
        } else {
            enterNote.setText(mIncomingDailyInput.getNote());
        }

    }

    /*
    setSpinnersToPreviouslySelectedValues() retrieves the emotion or physical feeling from the incoming input,
    then uses the String to find the position in the array, then it sets the selection to that position
     */
    private void setSpinnersToPreviouslySelectedValues() {
        String incomingEmotion = mIncomingDailyInput.getEmotion();
        ArrayAdapter<CharSequence> emotionAdapter = ArrayAdapter.createFromResource(this, R.array.emotions_array, android.R.layout.simple_spinner_dropdown_item);
        emotionSpinner.setAdapter(emotionAdapter);
        if (incomingEmotion != null) {
            int emotionSpinnerPosition = emotionAdapter.getPosition(incomingEmotion);
            emotionSpinner.setSelection(emotionSpinnerPosition);
        }


        String incomingPhysicalFeeling = mIncomingDailyInput.getPhysicalFeeling();
        ArrayAdapter<CharSequence> physicalAdapter = ArrayAdapter.createFromResource(this, R.array.physical_feelings_array, android.R.layout.simple_spinner_dropdown_item);
        physicalFeelingSpinner.setAdapter(physicalAdapter);
        if (incomingPhysicalFeeling != null) {
            int physicalSpinnerPosition = physicalAdapter.getPosition(incomingPhysicalFeeling);
            physicalFeelingSpinner.setSelection(physicalSpinnerPosition);
        }
    }

    /*
    setBleedingCheckBoxToPreviouslySelectedValue() checks what the bleeding value is in the incoming input,
    then used the .setChecked() method to either set it as checked or unchecked.
    Then it instantiates mBleeding to either NOT_BLEEDING or IS_BLEEDING incase the user does not make any changes to it...
    the value will remain the same
     */
    private void setBleedingCheckBoxToPreviouslySelectedValue() {
        String checkBleeding = mIncomingDailyInput.getBleeding();
        if (checkBleeding == null || checkBleeding.equals(NOT_BLEEDING)) {
            bleedingCheckBox.setChecked(false);
            mBleeding = NOT_BLEEDING;
        } else if (checkBleeding.equals(IS_BLEEDING)) {
            bleedingCheckBox.setChecked(true);
            mBleeding = IS_BLEEDING;
        }
    }


    /*
    checkDateAndSave() checks whether mDate is null and if it is, a toast is displayed asking
    the user to select a date. Date is the primary key in the database and cannot be null.
     */
    private void checkDateAndSave() {
        Context context = getApplicationContext();
        CharSequence text = (CharSequence) getString(R.string.pick_date);
        int duration = Toast.LENGTH_SHORT;

        if (mDate == null) {
            Toast pickDate = Toast.makeText(context, text, duration);
            pickDate.show();
        } else {

            if (mNote == null || mNote.length() == 0) {
                mNote = NO_NOTE_ENTERED;
            }
            newInput = new DailyInput(mDate, mBleeding, mEmotion, mPhysical, mNote);
            mDailyInputs.add(newInput);

            saveNewDailyInput();
            showToastAndGoBackToMainPage();

        }
    }

    //saves and inserts input into database
    private void saveNewDailyInput() {
        mDailyInputRepository.insertDailyInputTask(newInput);
    }

    //displays a toast which says "saved!" and navigates back to the MainActivity
    private void showToastAndGoBackToMainPage() {
        Context context = getApplicationContext();
        CharSequence text = (CharSequence) getString(R.string.saved);
        int duration = Toast.LENGTH_SHORT;

        Toast save = Toast.makeText(context, text, duration);
        save.show();
        Intent backToMain = new Intent(DailyInputActivity.this, MainActivity.class);
        startActivity(backToMain);

    }


    /*
   this method overrides the animated transitions which have been set for the whole
   program. If the user selects the home button (the back arrow at the top left of the
   toolbar, the new activity slides in from the left rather than the right
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        return false;
    }
}
