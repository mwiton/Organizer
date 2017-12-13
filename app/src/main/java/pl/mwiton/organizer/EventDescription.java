package pl.mwiton.organizer;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import event.Event;
import event.EventDatabaseHandler;

public class EventDescription extends AppCompatActivity {
    private TextView dateTextView;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView createTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        dateTextView = (TextView) findViewById(R.id.date_text_view);
        startTimeTextView = (TextView) findViewById(R.id.start_time_text_view);
        endTimeTextView = (TextView) findViewById(R.id.end_time_text_view);
        titleTextView = (TextView) findViewById(R.id.title_text_view);
        descriptionTextView = (TextView) findViewById(R.id.description_text_view);
        createTimeTextView = (TextView) findViewById(R.id.create_time_text_view);

        long id = getIntent().getLongExtra("id", 0);
        EventDatabaseHandler databaseHandler = new EventDatabaseHandler(this);
        Event event = databaseHandler.getSingleEvent(id);

        dateTextView.setText(event.getDate());
        startTimeTextView.setText(event.getStartTime());
        endTimeTextView.setText(event.getEndTime());
        titleTextView.setText(event.getTitle());
        descriptionTextView.setText(event.getDescription());
        Date createDate = new Date(event.getTimeOfCreate());
        createTimeTextView.setText(String.format("%s, %s", Event.dateFormat.format(createDate),
                Event.timeFormat.format(createDate)));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
