package com.example.dopetheimmortal.turntapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dopetheimmortal.turntapp.Adapters.InviteAdapter;
import com.example.dopetheimmortal.turntapp.Adapters.UpcomingAdapter;
import com.example.dopetheimmortal.turntapp.Adapters.UpcomingInvite;
import com.example.dopetheimmortal.turntapp.DataStructures.EventStruct;
import com.example.dopetheimmortal.turntapp.DataStructures.GeneralUser;
import com.example.dopetheimmortal.turntapp.LocalData.UserLocalData;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.Useful.Profile_Data;
import com.example.dopetheimmortal.turntapp.Useful.StaticData;
import com.example.dopetheimmortal.turntapp.connector.Connector;
import com.example.dopetheimmortal.turntapp.connector.ConnectorCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dope The Immortal on 2016/10/30.
 */
public class invites extends  AppCompatActivity implements ConnectorCallback {
    public ArrayList<EventStruct> upcoming_events = new ArrayList<>();
    public ListView d;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invites);

        UserLocalData o = new UserLocalData(invites.this);
        o.open();
        String me = o.actual().dbid;
        o.close();
        String link = this.getString(R.string.link);
        HashMap<String, String> data = new HashMap<>();
        data.put("type", "get_my_invites");
        data.put("me", me);
//        data.put("me", "6");
        new Connector(link, this, this, data, "Loading invites", "Loading invites\nPlease wait..", false, true).execute();
        d=(ListView)findViewById(R.id.my_invites);
        StaticData.invites=this;
    }

    @Override
    public void success(String info) throws JSONException {

        Button accept = (Button)findViewById(R.id.accept);
        Button decline = (Button)findViewById(R.id.decline);

        JSONObject obj = new JSONObject(info);
        JSONArray object = obj.getJSONArray("data");
        // ArrayList<EventStruct> ongoing_events = new ArrayList<>();
        LayoutInflater myInflater = getLayoutInflater();
        for (int i = 0; i < object.length(); i++) {
            RelativeLayout ll = (RelativeLayout)myInflater.inflate(R.layout.resp_event,null);
//            Toast.makeText(getApplicationContext(),"okay: " + i,Toast.LENGTH_LONG).show();
            JSONObject get_all = object.getJSONObject(i);
            JSONObject get=get_all.getJSONObject("event");
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
            String host_name = "lnjljlkjl";//get.getString("host_name");
            boolean me_attending = false;
            upcoming_events.add(new EventStruct(id, djs, attending, event_type, host_id, rating, tbl_avail, specials, gen_fee, vip_fee, name, start_time, end_time, latlong, address, logo, host_name, me_attending));

        }

        set_adapter(upcoming_events);
    }
    public  void set_adapter(ArrayList<EventStruct> upcoming_events){
        UpcomingInvite adapter = new UpcomingInvite(this, upcoming_events,this);
        d.setAdapter(adapter);
    }
    @Override
    public void fail(String info) {

    }

    @Override
    public void display(String info, Context cont) {

    }
    public void refresh(){
        startActivity(new Intent(this,invites.class));
        finish();
    }
}
