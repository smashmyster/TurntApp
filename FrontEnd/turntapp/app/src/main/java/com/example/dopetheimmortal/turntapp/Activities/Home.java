package com.example.dopetheimmortal.turntapp.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.dopetheimmortal.turntapp.Adapters.ViewPagerAdapter;
import com.example.dopetheimmortal.turntapp.DataStructures.EventStruct;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.Useful.ConnectorCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    }

    private void initializetools() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void success(String info) throws JSONException {
        JSONObject obj = new JSONObject(info);
        int code=obj.getInt("code");
        JSONArray object = obj.getJSONArray("upcoming");
        ArrayList<EventStruct>events=new ArrayList<>();
        for (int i = 0; i < object.length(); i++) {
            JSONObject get = object.getJSONObject(i);
            String id = get.getString("id");
            String djs = get.getString("id");
            String attending = get.getString("id");
            String event_type = get.getString("id");
            String host_id = get.getString("host_id");
            String rating = get.getString("rating");
            String tbl_avail = get.getString("tbl_avail");
            String specials = get.getString("specials");
            String gen_fee = get.getString("gen_fee");
            String vip_fee = get.getString("vip_fee");
            String name = get.getString("name");
            String start_time = get.getString("start_time");
            String end_time = get.getString("end_time");
            events.add(new EventStruct(id,djs,attending,event_type,host_id,rating,tbl_avail,specials,gen_fee,vip_fee,name,start_time,end_time));
        }
    }

    @Override
    public void fail(String info) {

    }

    @Override
    public void display(String info, Context cont) {

    }
}
