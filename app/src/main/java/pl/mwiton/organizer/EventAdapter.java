package pl.mwiton.organizer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import event.Event;

/**
 * Created by Mateusz Witoń on 11.11.2017.
 */

public class EventAdapter extends ArrayAdapter<Event> {
    private List<Event> events;

    private class ViewHolder {
        TextView title;
        TextView date;
        TextView start;
        TextView end;
    }

    public EventAdapter(List<Event> events, Context context){
        super(context, R.layout.event_list_item, events);
        this.events = events;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.event_list_item, parent, false);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date_textView);
            viewHolder.end = (TextView) convertView.findViewById(R.id.end_time_textView);
            viewHolder.start = (TextView) convertView.findViewById(R.id.start_time_textView);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title_textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(event.getTitle());
        viewHolder.start.setText(event.getStartTime());
        viewHolder.end.setText(event.getEndTime());
        viewHolder.date.setText(event.getDate());
        return convertView;
    }
}
