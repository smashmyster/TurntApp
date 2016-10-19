package com.example.dopetheimmortal.turntapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dopetheimmortal.turntapp.Adapters.InviteAdapter;
import com.example.dopetheimmortal.turntapp.Adapters.ViewPagerAdapter;
import com.example.dopetheimmortal.turntapp.DataStructures.EventStruct;
import com.example.dopetheimmortal.turntapp.DataStructures.GeneralUser;
import com.example.dopetheimmortal.turntapp.LocalData.UserLocalData;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.Useful.CallBackAttending;
import com.example.dopetheimmortal.turntapp.Useful.ConnectorCallSearch;
import com.example.dopetheimmortal.turntapp.Useful.ConnectorCallback;
import com.example.dopetheimmortal.turntapp.Useful.Profile_Data;
import com.example.dopetheimmortal.turntapp.connector.Connector;
import com.example.dopetheimmortal.turntapp.connector.ConnectorAttending;
import com.example.dopetheimmortal.turntapp.connector.ConnectorInvite;
import com.example.dopetheimmortal.turntapp.connector.ConnectorSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jackson on 2016/08/11.
 */
public class Home extends AppCompatActivity implements ConnectorCallback,ConnectorCallSearch,CallBackAttending {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private EditText search_user;
    private ListView show_search;
    ArrayList<EventStruct>upcomig_events;
    ArrayList<EventStruct> ongoing_events;
    ArrayList<EventStruct> b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        search_user=(EditText)findViewById(R.id.search_user_text);
        show_search=(ListView) findViewById(R.id.show_search_results);
        ArrayList<GeneralUser>dummy=new ArrayList<>();
        InviteAdapter adapter=new InviteAdapter(this,dummy);
        show_search.setAdapter(adapter);
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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, null, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        show_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mDrawerLayout.closeDrawers();
            }
        });
        search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                HashMap<String,String>get=new HashMap<String, String>();
                get.put("type","search_user");
                UserLocalData o=new UserLocalData(Home.this);
                o.open();
                String me=o.actual().dbid;
                o.close();
                get.put("me",me);
                get.put("search",charSequence.toString());
                String link=Home.this.getString(R.string.link);//"http://10.0.0.11/TurntappAssignment/Backend/";
                new ConnectorSearch(link,Home.this,Home.this,get,"","",false,false).execute();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
            String host_name = get.getString("host_name");
            upcoming_events.add(new EventStruct(id, djs, attending, event_type, host_id, rating, tbl_avail, specials, gen_fee, vip_fee, name, start_time, end_time,latlong,address,logo,host_name));
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
            String host_name = get.getString("host_name");
            ongoing_events.add(new EventStruct(id, djs, attending, event_type, host_id, rating, tbl_avail, specials, gen_fee, vip_fee, name, start_time, end_time,latlong,address,logo,host_name));
        }
        this.upcomig_events=upcoming_events;
        this.ongoing_events=ongoing_events;
        get_my_events();
    }
    public void adapt_data(){
        viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),upcomig_events,ongoing_events,b);
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.logout:
                UserLocalData loc = new UserLocalData(this);
                loc.open();
                loc.delete();
                loc.close();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.new_event:
                startActivity(new Intent(this, NewEvent.class));
                break;
            case R.id.search_user:
                open_drawe();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    ArrayList <View>temp=new ArrayList<>();
    @Override
    public void get_users(String info) throws JSONException {
        JSONObject object=new JSONObject(info);
        JSONArray arr=object.getJSONArray("data");
        final ArrayList<GeneralUser>users=new ArrayList<>();
        for (int i = temp.size() - 1; i >= 0; i--)
            show_search.removeFooterView(temp.get(i));
        for (int i=0;i<arr.length();i++){
            JSONObject o=arr.getJSONObject(i);
            String id= o.getString("id");
            String name= o.getString("name");
            String surname= o.getString("surname");
            String image_name= o.getString("image_name");
            String following= o.getString("following");
            String followers= o.getString("followers");
            String status= o.getString("status");
            GeneralUser use=new GeneralUser(id,name,surname,image_name,following,followers,status,"");
            users.add(use);
            View d=get_view(use);
            show_search.addFooterView(d);
            temp.add(d);
        }
    }
    public View get_view(final GeneralUser get){
        View  convertView =getLayoutInflater().inflate(R.layout.invite_people_adapter,null);
        TextView name = (TextView) convertView.findViewById(R.id.invite_user_name);
        name.setText(get.name+" "+get.surname);
        TextView status = (TextView) convertView.findViewById(R.id.invite_user_status);
        status.setText(get.status);
        if(get.invited.equals("0")){
            System.out.println("About to");
            final ImageView image=(ImageView)convertView.findViewById(R.id.send_invite_img);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String,String>send=new HashMap<String, String>();
                    send.put("type","invite_user");
                    send.put("user",get.id);
//                    send.put("event",event);
                    UserLocalData user=new UserLocalData(Home.this);
                    user.open();
                    send.put("me",user.actual().dbid);
                    user.close();
                    String link=Home.this.getString(R.string.link);
//                    new ConnectorInvite(link,Home.this,Home.this,send,"","",true,false,image).execute();
                }
            });
        }else{
            ImageView image=(ImageView)convertView.findViewById(R.id.send_invite_img);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bh=new Bundle();
                bh.putString("id",get.id);
                Intent o=new Intent(Home.this,ViewUser.class);
                o.putExtras(bh);
                startActivity(o);
                System.out.println("Trying");
            }
        });
        return convertView;
    }
    public void open_drawe()
    {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    public void get_my_events() {
        HashMap<String,String>att=new HashMap<>();
        UserLocalData looca=new UserLocalData(this);
        looca.open();
        Profile_Data ll=looca.actual();
        looca.close();
        String id=ll.dbid;
        att.put("me",id);
        att.put("type","get_my_events");
        String link=this.getString(R.string.link);
        new ConnectorAttending(link,this,this,att,"","",false,false).execute();
    }

    @Override
    public void success_my_events(String s) throws JSONException {
        JSONObject o=new JSONObject(s);
        JSONArray arr=o.getJSONArray("data");
        ArrayList<EventStruct>b=new ArrayList<>();
        for (int i=0;i<arr.length();i++){
            JSONObject get=arr.getJSONObject(i);
            String id = get.getString("id");
            String djs = get.getString("djs");
            String attending = get.getString("attending");
            String event_type = get.getString("event_type");
            String host_id = get.getString("host_id");
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
            EventStruct event=new EventStruct(id,djs,attending,event_type,host_id,"0",tbl_avail,specials,gen_fee,vip_fee,name,start_time,end_time,latlong,address,logo,"");
            b.add(event);
        }
        this.b=b;
        adapt_data();
    }
}
