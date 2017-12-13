package pl.mwiton.organizer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import event.Event;
import event.EventDatabaseHandler;

import static event.Event.timeFormat;
import static event.Event.dateFormat;

public class AddItemActivity extends AppCompatActivity {
    private static final String START_TIME_INTENT = "start_time";
    private static final String END_TIME_INTENT = "end_time";
    private static final String TITLE_INTENT = "title";
    private static final String DESCRIPTION_INTENT = "description";
    private TextView dateTextView;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private EditText titleEditText;
    private EditText descriptionEditText;

    private final String DATE_INTENT = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        dateTextView = (TextView) findViewById(R.id.date_textView);
        startTimeTextView = (TextView) findViewById(R.id.start_time_text_view);
        endTimeTextView = (TextView) findViewById(R.id.end_time_text_view);
        titleEditText = (EditText) findViewById(R.id.title_editText);
        descriptionEditText = (EditText) findViewById(R.id.description_editText);
        String date = getIntent().getStringExtra("date");
        if(date == null){
            dateTextView.setText(String.format("%tF", new Date()));
        }
        else{
            dateTextView.setText(date);
        }
        if(savedInstanceState != null) {
            dateTextView.setText(savedInstanceState.getString(DATE_INTENT));
            startTimeTextView.setText(savedInstanceState.getString(START_TIME_INTENT));
            endTimeTextView.setText(savedInstanceState.getString(END_TIME_INTENT));
            titleEditText.setText(savedInstanceState.getString(TITLE_INTENT));
            descriptionEditText.setText(savedInstanceState.getString(DESCRIPTION_INTENT));
        }

        Button button = (Button) findViewById(R.id.change_date_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear,
                                          int dayOfMonth) {
                        Calendar date = Calendar.getInstance();
                        date.set(year, monthOfYear, dayOfMonth);
                        dateTextView.setText(dateFormat.format(date.getTime()));
                    }
                };
                Calendar setCalendarDate = Calendar.getInstance();
                try {
                    Date setDate = dateFormat.parse(dateTextView.getText().toString());
                    setCalendarDate.setTime(setDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AddItemActivity.this, dateSetListener,
                        setCalendarDate.get(Calendar.YEAR), setCalendarDate.get(Calendar.MONTH),
                        setCalendarDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        button = (Button) findViewById(R.id.change_start_time_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        Calendar time = Calendar.getInstance();
                        time.set(Calendar.HOUR_OF_DAY, hour);
                        time.set(Calendar.MINUTE, minute);
                        startTimeTextView.setText(timeFormat.format(time.getTime()));
                    }
                };
                Calendar setCalendarDate = Calendar.getInstance();
                try {
                    if(isInitializedTime(startTimeTextView)) {
                        Date setDate = timeFormat.parse(startTimeTextView.getText().toString());
                        setCalendarDate.setTime(setDate);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new TimePickerDialog(AddItemActivity.this, timeSetListener,
                        setCalendarDate.get(Calendar.HOUR_OF_DAY), setCalendarDate.get(Calendar.MINUTE),
                        true).show();
            }
        });

        button = (Button) findViewById(R.id.change_end_time_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        Calendar time = Calendar.getInstance();
                        time.set(Calendar.HOUR_OF_DAY, hour);
                        time.set(Calendar.MINUTE, minute);
                        endTimeTextView.setText(timeFormat.format(time.getTime()));
                    }
                };
                Calendar setCalendarDate = Calendar.getInstance();
                try {
                    if(isInitializedTime(endTimeTextView)) {
                        Date setDate = timeFormat.parse(endTimeTextView.getText().toString());
                        setCalendarDate.setTime(setDate);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new TimePickerDialog(AddItemActivity.this, timeSetListener,
                        setCalendarDate.get(Calendar.HOUR_OF_DAY), setCalendarDate.get(Calendar.MINUTE),
                        true).show();
            }
        });
    }

    private boolean isInitializedTime(TextView textView) {
        return !textView.getText().equals(getString(R.string.default_time));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_item_menu, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(DATE_INTENT, dateTextView.getText().toString());
        outState.putString(START_TIME_INTENT, startTimeTextView.getText().toString());
        outState.putString(END_TIME_INTENT, endTimeTextView.getText().toString());
        outState.putString(TITLE_INTENT, titleEditText.getText().toString());
        outState.putString(DESCRIPTION_INTENT, descriptionEditText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_confirm:
                if(checkValidity()){
                    Event event = new Event(System.currentTimeMillis());
                    event.setTitle(titleEditText.getText().toString());
                    event.setDescription(descriptionEditText.getText().toString());
                    event.setDate(dateTextView.getText().toString());
                    event.setEndTime(endTimeTextView.getText().toString());
                    event.setStartTime(startTimeTextView.getText().toString());
                    EventDatabaseHandler databaseHandler = new EventDatabaseHandler(this);
                    databaseHandler.addEvent(event);
                    super.onBackPressed();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private boolean checkValidity() {
        boolean result = true;
        if(!(isInitializedTime(startTimeTextView) && isInitializedTime(endTimeTextView))){
            Toast.makeText(this, "Choose time", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if(startTimeTextView.getText().toString().compareTo(endTimeTextView.getText().toString()) > 0)
        {
            Toast.makeText(this, "Time of begin is greater than time of end", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if(titleEditText.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Title is empty", Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
