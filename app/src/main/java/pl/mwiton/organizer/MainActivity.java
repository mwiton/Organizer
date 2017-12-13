package pl.mwiton.organizer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import event.Event;
import event.EventDatabaseHandler;

import static event.Event.dateFormat;
import static event.Event.timeFormat;

public class MainActivity extends AppCompatActivity {
    private TextView currentActTextView;
    private TextView previousActTextView;
    private TextView nextActTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        currentActTextView = (TextView) findViewById(R.id.currentActTextView);
        previousActTextView = (TextView) findViewById(R.id.previousActTextView);
        nextActTextView = (TextView) findViewById(R.id.nextActTextView);

        showTodayEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearTextViews();
        showTodayEvents();
    }

    private void clearTextViews() {
        currentActTextView.setText("");
        previousActTextView.setText("");
        nextActTextView.setText("");
    }

    private void addLine(String line, TextView textView){
        if(textView.getText().equals("")) {
            textView.setText(line);
        }
        else {
            textView.setText(textView.getText() + "\n" + line);
        }
    }

    private void showTodayEvents() {
        EventDatabaseHandler databaseHandler = new EventDatabaseHandler(this);
        Calendar currentDate = Calendar.getInstance();
        List<Event> events = databaseHandler.getEventsForDay(dateFormat.format(currentDate.getTime()));
        String currentTime = timeFormat.format(currentDate.getTime());
        for(Event event : events){
            if (event.getStartTime().compareTo(currentTime) <=0 && event.getEndTime().compareTo(currentTime) >= 0)
            {
                addLine(event.getShortDescription(), currentActTextView);
            }
            else if(event.getStartTime().compareTo(currentTime) <=0)
            {
                addLine(event.getShortDescription(), previousActTextView);
            }
            else
            {
                addLine(event.getShortDescription(), nextActTextView);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_item:
                Intent intent = new Intent(this, AddItemActivity.class);
                startActivity(intent);
                break;
            case R.id.action_show_calendar:
                runCalendarWidget();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void runCalendarWidget() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear,
                                  int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                date.set(year, monthOfYear, dayOfMonth);
                Intent intent = new Intent(MainActivity.this, EventsList.class);
                intent.putExtra("date", dateFormat.format(date.getTime()));
                MainActivity.super.startActivity(intent);
            }
        };
        Calendar setCalendarDate = Calendar.getInstance();
        new DatePickerDialog(this, dateSetListener,
                setCalendarDate.get(Calendar.YEAR), setCalendarDate.get(Calendar.MONTH),
                setCalendarDate.get(Calendar.DAY_OF_MONTH)).show();
    }

}
