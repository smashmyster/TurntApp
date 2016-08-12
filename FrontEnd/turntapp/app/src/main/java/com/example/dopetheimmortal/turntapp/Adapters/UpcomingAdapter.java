package com.example.dopetheimmortal.turntapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dopetheimmortal.turntapp.DataStructures.EventStruct;
import com.example.dopetheimmortal.turntapp.DataStructures.FollowData;
import com.example.dopetheimmortal.turntapp.R;

import java.util.ArrayList;

/**
 * Created by jackson on 2016/08/11.
 */
public class UpcomingAdapter extends ArrayAdapter<EventStruct> {
    ArrayList<EventStruct> list;
    Context context;
    public UpcomingAdapter(Context context, ArrayList<EventStruct> data) {
        super(context, 0, data);
        this.context = context;
        this.list = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemview = convertView;
        if (itemview == null) {
            itemview = convertView = LayoutInflater.from(context).inflate(
                    R.layout.show_event, parent, false);
        }
        final EventStruct get = list.get(position);

        TextView name = (TextView) convertView.findViewById(R.id.event_name);
        name.setText(get.name);
        return itemview;
    }
}
