package com.example.dopetheimmortal.turntapp.HomeFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dopetheimmortal.turntapp.Adapters.UpcomingAdapter;
import com.example.dopetheimmortal.turntapp.DataStructures.EventStruct;
import com.example.dopetheimmortal.turntapp.DataStructures.FollowData;
import com.example.dopetheimmortal.turntapp.R;

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
        list.setAdapter(new UpcomingAdapter(getContext(), dummy));
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
                    //Open google maps navigate
                }
            });
            name.setText(get.name);
            list.addFooterView(convertView);
        }
        return lay;
    }
}
