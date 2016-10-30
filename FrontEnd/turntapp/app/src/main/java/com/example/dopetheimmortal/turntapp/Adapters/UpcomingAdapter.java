package com.example.dopetheimmortal.turntapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dopetheimmortal.turntapp.DataStructures.EventStruct;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.Useful.ViewEvent;
import com.example.dopetheimmortal.turntapp.connector.GetImage;

import java.util.ArrayList;

/**
 * Created by jackson on 2016/08/11.
 */
public class UpcomingAdapter extends ArrayAdapter<EventStruct> {
    ArrayList<EventStruct> list;
    Context context;
    Activity activity;

    public UpcomingAdapter(Context context, ArrayList<EventStruct> data, Activity activity) {
        super(context, 0, data);
        this.context = context;
        this.list = data;
        this.activity = activity;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final EventStruct get = list.get(position);


        convertView = LayoutInflater.from(context).inflate(
                R.layout.show_event, parent, false);

        TextView name = (TextView) convertView.findViewById(R.id.event_name);
        TextView host = (TextView) convertView.findViewById(R.id.host);
        host.setText(get.host_name);
        ImageButton btn = (ImageButton) convertView.findViewById(R.id.nav);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://maps.google.com/maps?daddr=" + get.latlong;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                activity.startActivity(intent);


            }
        });
        name.setText(get.name);
        ImageView image=(ImageView)convertView.findViewById(R.id.logo);
        String link=getContext().getString(R.string.link)+"EventImages/"+get.logo;
        new GetImage(link,getContext(),image).execute();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ViewEvent().view_event(get,getContext(),activity);
            }
        });
        return convertView;
    }
}
