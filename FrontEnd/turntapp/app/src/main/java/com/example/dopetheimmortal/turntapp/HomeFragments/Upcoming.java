package com.example.dopetheimmortal.turntapp.HomeFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dopetheimmortal.turntapp.Activities.TouchImageView;
import com.example.dopetheimmortal.turntapp.Adapters.UpcomingAdapter;
import com.example.dopetheimmortal.turntapp.DataStructures.EventStruct;
import com.example.dopetheimmortal.turntapp.DataStructures.FollowData;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.Useful.ViewEvent;

import java.util.ArrayList;


/**
 * Created by jackson on 2016/03/26.
 */
public class Upcoming extends Fragment {
    ArrayList<EventStruct> upcoming_events;

    public Upcoming() {
        // Required empty public constructor
    }

    public void set_data(ArrayList<EventStruct> upcoming_events) {
        this.upcoming_events = upcoming_events;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View lay = inflater.inflate(R.layout.upcoming_freg, container, false);
        ArrayList<EventStruct> dummy = new ArrayList<>();
        ListView list = (ListView) lay.findViewById(R.id.upcoming_events);
        list.setAdapter(new UpcomingAdapter(getContext(), dummy,getActivity()));
        for (int i = 0; i < upcoming_events.size(); i++) {
            View convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.show_event, null, false);
            final EventStruct get = upcoming_events.get(i);

            TextView name = (TextView) convertView.findViewById(R.id.event_name);
            TextView host = (TextView)convertView.findViewById(R.id.host);
            host.setText(get.host_name);
            ImageButton btn=(ImageButton)convertView.findViewById(R.id.nav);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "http://maps.google.com/maps?daddr="+get.latlong;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
                    startActivity(intent);

                }
            });
            name.setText(get.name);
            list.addFooterView(convertView);
//            TouchImageView img = (TouchImageView) lay.findViewById(R.id.logo);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ViewEvent().view_event(get,getContext(),getActivity());
                }
            });
        }
        return lay;
    }
}
