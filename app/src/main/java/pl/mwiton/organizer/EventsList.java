package pl.mwiton.organizer;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import event.Event;
import event.EventDatabaseHandler;

public class EventsList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        String date = getIntent().getStringExtra("date");
        ab.setTitle(date);
        final EventDatabaseHandler databaseHandler = new EventDatabaseHandler(this);
        final EventAdapter adapter = new EventAdapter(databaseHandler.getEventsForDay(date), this);

        final ListView eventsListView = (ListView) findViewById(R.id.eventsListView);
        eventsListView.setAdapter(adapter);

        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                long eventId = ((Event)eventsListView.getItemAtPosition(position)).getTimeOfCreate();
                Intent intent = new Intent(EventsList.this, EventDescription.class);
                intent.putExtra("id", eventId);
                EventsList.super.startActivity(intent);
            }
        });

        eventsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                final Event event = (Event)eventsListView.getItemAtPosition(position);
                PopupMenu popup = new PopupMenu(EventsList.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.item_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.action_delete:
                                databaseHandler.removeEvent(event.getTimeOfCreate());
                                adapter.remove(event);
                                break;
                        }
                        return true;
                    }
                });
                popup.show();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_events_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_item:
                Intent intent = new Intent(this, AddItemActivity.class);
                intent.putExtra("date", getIntent().getStringExtra("date"));
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
