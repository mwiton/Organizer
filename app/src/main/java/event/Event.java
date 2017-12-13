package event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mateusz Witoń on 08.11.2017.
 */

public class Event {
    private long timeOfCreate;
    private String date;
    private String startTime;
    private String endTime;
    private String title;
    private String description;

    static private final String dateFormatString = "yyyy-MM-dd";
    static public final SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
    static private final String timeFormatString = "HH:mm";
    static public final SimpleDateFormat timeFormat = new SimpleDateFormat(timeFormatString);

    public Event(long timeOfCreate) {
        this.timeOfCreate = timeOfCreate;
        date = "";
        startTime = "";
        endTime = "";
        title = "";
        description = "";
    }


    public void setDate(String date) {
        this.date = date;
    }


    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public long getTimeOfCreate() {
        return timeOfCreate;
    }

    public String getShortDescription(){
        return String.format("%s - %s - %s", startTime, endTime, title);
    }
}

