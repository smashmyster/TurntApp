package com.example.dopetheimmortal.turntapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dopetheimmortal.turntapp.Adapters.ViewPagerAdapter;
import com.example.dopetheimmortal.turntapp.DataStructures.EventStruct;
import com.example.dopetheimmortal.turntapp.LocalData.UserLocalData;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.Useful.ConnectorCallback;
import com.example.dopetheimmortal.turntapp.connector.Connector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jackson on 2016/08/11.
 */
public class Home extends AppCompatActivity implements ConnectorCallback {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        initializetools();
        String link = this.getString(R.string.link);
        HashMap<String, String> data = new HashMap<>();
        data.put("type", "get_upcoming_events");
        data.put("id", "0");
        new Connector(link, this, this, data, "Loading events", "Loading events\nPlease wait..", false, true).execute();

    }

    private void initializetools() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void success(String info) throws JSONException {
        JSONObject obj = new JSONObject(info);
        JSONArray object = obj.getJSONArray("upcoming");
        ArrayList<EventStruct> upcoming_events = new ArrayList<>();
        ArrayList<EventStruct> ongoing_events = new ArrayList<>();
        for (int i = 0; i < object.length(); i++) {
            JSONObject get = object.getJSONObject(i);
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
            upcoming_events.add(new EventStruct(id, djs, attending, event_type, host_id, rating, tbl_avail, specials, gen_fee, vip_fee, name, start_time, end_time,latlong,address,logo));
        }
        object = obj.getJSONArray("ongoing");
        for (int i = 0; i < object.length(); i++) {
            JSONObject get = object.getJSONObject(i);
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
            ongoing_events.add(new EventStruct(id, djs, attending, event_type, host_id, rating, tbl_avail, specials, gen_fee, vip_fee, name, start_time, end_time,latlong,address,logo));
        }
        adapt_data(upcoming_events,ongoing_events);
    }
    public void adapt_data(ArrayList<EventStruct>upcomig_events,ArrayList<EventStruct> ongoing_events){
        viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),upcomig_events,ongoing_events);
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public void fail(String info) {

    }

    @Override
    public void display(String info, Context cont) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                UserLocalData loc = new UserLocalData(this);
                loc.open();
                loc.delete();
                loc.close();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
