package com.example.dopetheimmortal.turntapp.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.dopetheimmortal.turntapp.connector.ConnectorSearch;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jackson on 2016/08/11.
 */
public class Home extends AppCompatActivity implements ConnectorCallback, ConnectorCallSearch, CallBackAttending {
    //Notifications
    private BroadcastReceiver mRegistrationBroadcastReceiver;























    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private EditText search_user;
    private ListView show_search;
    ArrayList<EventStruct> upcomig_events;
    ArrayList<EventStruct> ongoing_events;
    ArrayList<EventStruct> b;
    boolean run=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        search_user = (EditText) findViewById(R.id.search_user_text);
        show_search = (ListView) findViewById(R.id.show_search_results);
        ArrayList<GeneralUser> dummy = new ArrayList<>();
        InviteAdapter adapter = new InviteAdapter(this, dummy);
        show_search.setAdapter(adapter);
        initializetools();
        String link = this.getString(R.string.link);
        HashMap<String, String> data = new HashMap<>();
        data.put("type", "get_upcoming_events");
        data.put("id", "0");
        new Connector(link, this, this, data, "Loading events", "Loading events\nPlease wait..", false, true).execute();


//Notifications

        //Initializing our broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Getting the registration token from the intent
                    String token = intent.getStringExtra("token");
                    //Displaying the token as toast
                    Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();

                    //if the intent is not with success then displaying error messages
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if(ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }




    }

    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("HomeActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }


    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("HomeActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
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
                HashMap<String, String> get = new HashMap<String, String>();
                get.put("type", "search_user");
                UserLocalData o = new UserLocalData(Home.this);
                o.open();
                String me = o.actual().dbid;
                o.close();
                get.put("me", me);
                get.put("search", charSequence.toString());
                String link = Home.this.getString(R.string.link);//"http://10.0.0.11/TurntappAssignment/Backend/";
                new ConnectorSearch(link, Home.this, Home.this, get, "", "", false, false).execute();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void success(String info) throws JSONException {
        if(!run)
            call_show(info);
    }

    private void call_show(String info) throws JSONException {
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
            upcoming_events.add(new EventStruct(id, djs, attending, event_type, host_id, rating, tbl_avail, specials, gen_fee, vip_fee, name, start_time, end_time, latlong, address, logo, host_name));
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
            ongoing_events.add(new EventStruct(id, djs, attending, event_type, host_id, rating, tbl_avail, specials, gen_fee, vip_fee, name, start_time, end_time, latlong, address, logo, host_name));
        }
        this.upcomig_events = upcoming_events;
        this.ongoing_events = ongoing_events;
        get_my_events();
        run=true;
    }

    public void adapt_data() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), upcomig_events, ongoing_events, b);
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
            case R.id.search_event:
                startActivity(new Intent(this,SearchEvent.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    ArrayList<View> temp = new ArrayList<>();

    @Override
    public void get_users(String info) throws JSONException {
        JSONObject object = new JSONObject(info);
        JSONArray arr = object.getJSONArray("data");
        final ArrayList<GeneralUser> users = new ArrayList<>();
        for (int i = temp.size() - 1; i >= 0; i--)
            show_search.removeFooterView(temp.get(i));
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.getJSONObject(i);
            String id = o.getString("id");
            String name = o.getString("name");
            String surname = o.getString("surname");
            String image_name = o.getString("image_name");
            String following = o.getString("following");
            String followers = o.getString("followers");
            String status = o.getString("status");
            boolean followings = o.getString("is_following").equals("1") ? true : false;
            GeneralUser use = new GeneralUser(id, name, surname, image_name, following, followers, status, "", followings);
            users.add(use);
            View d = get_view(use);

            show_search.addFooterView(d);
            temp.add(d);
        }
    }

    public View get_view(final GeneralUser get) {
        View convertView = getLayoutInflater().inflate(R.layout.invite_people_adapter, null);
        TextView name = (TextView) convertView.findViewById(R.id.invite_user_name);
        name.setText(get.name + " " + get.surname);
        TextView status = (TextView) convertView.findViewById(R.id.invite_user_status);
        status.setText(get.status);
        if (get.invited.equals("0")) {
            System.out.println("About to");
            final ImageView image = (ImageView) convertView.findViewById(R.id.send_invite_img);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String, String> send = new HashMap<String, String>();
                    send.put("type", "invite_user");
                    send.put("user", get.id);
//                    send.put("event",event);
                    UserLocalData user = new UserLocalData(Home.this);
                    user.open();
                    send.put("me", user.actual().dbid);
                    user.close();
                    String link = Home.this.getString(R.string.link);
//                    new ConnectorInvite(link,Home.this,Home.this,send,"","",true,false,image).execute();
                }
            });
        } else {
            final ImageView image = (ImageView) convertView.findViewById(R.id.send_invite_img);
            if (get.followings) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap map = BitmapFactory.decodeResource(this.getResources(), R.drawable.following, options);

                image.setImageBitmap(map);
            }
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String,String>pp=new HashMap<String, String>();
                    UserLocalData lo=new UserLocalData(Home.this);
                    lo.open();
                    Profile_Data k=lo.actual();
                    lo.close();
                    pp.put("me",k.dbid);
                    pp.put("user",get.id);
                    if (get.followings) {
                        Bitmap map = BitmapFactory.decodeResource(Home.this.getResources(), android.R.drawable.ic_input_add);
                        image.setImageBitmap(map);
                        pp.put("type","unfollow_user");
                        String link=Home.this.getString(R.string.link);
                        new Connector(link,Home.this,Home.this,pp,"","",false,false).execute();
                    } else {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        Bitmap map = BitmapFactory.decodeResource(Home.this.getResources(), R.drawable.following, options);

                        image.setImageBitmap(map);
                        pp.put("type","follow_user");
                        String link=Home.this.getString(R.string.link);
                        new Connector(link,Home.this,Home.this,pp,"","",false,false).execute();
                    }
                }
            });
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bh = new Bundle();
                bh.putString("id", get.id);
                Intent o = new Intent(Home.this, ViewUser.class);
                o.putExtras(bh);
                startActivity(o);
                System.out.println("Trying");
            }
        });
        return convertView;
    }

    public void open_drawe() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    public void get_my_events() {
        HashMap<String, String> att = new HashMap<>();
        UserLocalData looca = new UserLocalData(this);
        looca.open();
        Profile_Data ll = looca.actual();
        looca.close();
        String id = ll.dbid;
        att.put("me", id);
        att.put("type", "get_my_events");
        String link = this.getString(R.string.link);
        new ConnectorAttending(link, this, this, att, "", "", false, false).execute();
    }

    @Override
    public void success_my_events(String s) throws JSONException {
        JSONObject o = new JSONObject(s);
        JSONArray arr = o.getJSONArray("data");
        ArrayList<EventStruct> b = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject get = arr.getJSONObject(i);
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
            EventStruct event = new EventStruct(id, djs, attending, event_type, host_id, "0", tbl_avail, specials, gen_fee, vip_fee, name, start_time, end_time, latlong, address, logo, "");
            b.add(event);
        }
        this.b = b;
        adapt_data();
    }
}
