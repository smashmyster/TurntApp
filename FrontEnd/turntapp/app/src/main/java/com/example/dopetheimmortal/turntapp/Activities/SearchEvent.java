package com.example.dopetheimmortal.turntapp.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.example.dopetheimmortal.turntapp.Adapters.UpcomingAdapter;
import com.example.dopetheimmortal.turntapp.DataStructures.EventStruct;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.connector.ConnectorCallSearch;
import com.example.dopetheimmortal.turntapp.connector.ConnectorSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jackson on 2016/10/19.
 */

public class SearchEvent extends AppCompatActivity implements ConnectorCallSearch {
    private EditText search;
    private ListView show_search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_search);
        search = (EditText) findViewById(R.id.event_txt);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                HashMap<String, String> get = new HashMap<String, String>();
                get.put("type", "search_event");
                get.put("search", charSequence.toString());
                String link = SearchEvent.this.getString(R.string.link);
                new ConnectorSearch(link, SearchEvent.this, SearchEvent.this, get, "", "", false, false).execute();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void get_users(String info) throws JSONException {
        JSONObject object = new JSONObject(info);
        JSONArray arr = object.getJSONArray("data");
        ArrayList<EventStruct> upcoming_events = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject get = arr.getJSONObject(i);
            String id = get.getString("id");
            String djs = get.getString("djs");
            String attending = get.getString("attending");
            String event_type = get.getString("event_type");
            String host_id = get.getString("host_id");
            String rating = get.getString("rating");
            String tbl_avail = get.getString("tbl_avail");
            String specials = get.getString("specials");
            String gen_fee = get.getString("gen_fee");
            String vip_fee = get.getString("vip_fee");
            String name = get.getString("name");
            String start_time = get.getString("start_time");
            String end_time = get.getString("end_time");
            String latlong = get.getString("latlong");
            String address = get.getString("address");
            String logo = get.getString("logo");
            boolean me_attending=get.getInt("me_attending")==1?true:false;
            upcoming_events.add(new EventStruct(id, djs, attending, event_type, host_id, rating, tbl_avail, specials, gen_fee, vip_fee, name, start_time, end_time, latlong, address, logo, "",me_attending));
        }
        UpcomingAdapter adapter = new UpcomingAdapter(this, upcoming_events,this);
        show_search = (ListView) findViewById(R.id.searching_events);
        show_search.setAdapter(adapter);
    }
}
