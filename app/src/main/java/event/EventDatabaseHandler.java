package event;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz Witoń on 08.11.2017.
 */

public class EventDatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database.db";
    private final String TABLE_NAME = "events";

    private final String COLUMN_ID = "id";
    private final String COLUMN_DATE = "date";
    private final String COLUMN_START_TIME = "start_time";
    private final String COLUMN_END_TIME = "end_time";
    private final String COLUMN_TITLE = "title";
    private final String COLUMN_DESCRIPTION = "description";

    public EventDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER, " + COLUMN_DATE
                + " VARCHAR(11), " + COLUMN_START_TIME + " VARCHAR(6), " + COLUMN_END_TIME
                + " VARCHAR(6), " + COLUMN_TITLE + " TEXT, " + COLUMN_DESCRIPTION + " TEXT);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addEvent(Event event){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, event.getTimeOfCreate());
        values.put(COLUMN_DATE, event.getDate());
        values.put(COLUMN_DESCRIPTION, event.getDescription());
        values.put(COLUMN_END_TIME, event.getEndTime());
        values.put(COLUMN_START_TIME, event.getStartTime());
        values.put(COLUMN_TITLE, event.getTitle());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Event> getEventsForDay(String date){
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {COLUMN_DESCRIPTION, COLUMN_TITLE, COLUMN_END_TIME,
                COLUMN_START_TIME, COLUMN_ID, COLUMN_DATE};
        String whereClause = COLUMN_DATE + " = ?";
        String[] args = {date};
        String sortClause = COLUMN_START_TIME;
        Cursor cursor = db.query(TABLE_NAME, columns, whereClause, args, null, null, sortClause);
        List<Event> events = new ArrayList<>();
        while (cursor.moveToNext()){
            events.add(getEventFromCursor(cursor));
        }
        db.close();
        return events;
    }

    private Event getEventFromCursor(Cursor cursor) {
        Event event = new Event(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
        event.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
        event.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
        event.setEndTime(cursor.getString(cursor.getColumnIndex(COLUMN_END_TIME)));
        event.setStartTime(cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME)));
        event.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        return event;
    }

    public void removeEvent(long id){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = COLUMN_ID + " = ?";
        String[] args = {Long.toString(id)};
        db.delete(TABLE_NAME, whereClause, args);
        db.close();
    }

    public Event getSingleEvent(long id){
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {COLUMN_DESCRIPTION, COLUMN_TITLE, COLUMN_END_TIME,
                COLUMN_START_TIME, COLUMN_ID, COLUMN_DATE};
        String whereClause = COLUMN_ID + " = ?";
        String[] args = {Long.toString(id)};
        String sortClause = COLUMN_START_TIME;
        Cursor cursor = db.query(TABLE_NAME, columns, whereClause, args, null, null, sortClause);
        if(cursor.moveToNext()){
            return getEventFromCursor(cursor);
        }
        db.close();
        return null;
    }
}
