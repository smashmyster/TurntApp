package com.example.dopetheimmortal.turntapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dopetheimmortal.turntapp.Adapters.UpcomingAdapter;
import com.example.dopetheimmortal.turntapp.DataStructures.EventStruct;
import com.example.dopetheimmortal.turntapp.LocalData.UserLocalData;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.connector.CallBackAttending;
import com.example.dopetheimmortal.turntapp.connector.ConnectorCallback;
import com.example.dopetheimmortal.turntapp.Useful.Profile_Data;
import com.example.dopetheimmortal.turntapp.connector.Connector;
import com.example.dopetheimmortal.turntapp.connector.ConnectorAttending;
import com.example.dopetheimmortal.turntapp.connector.GetImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static java.security.AccessController.getContext;

/**
 * Created by jackson on 2016/10/19.
 */

public class ViewUser extends AppCompatActivity implements ConnectorCallback,CallBackAttending{
    HashMap<String,String>l=new HashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_freg);
        Bundle bh=getIntent().getExtras();
        String id=bh.getString("id");
        UserLocalData ll=new UserLocalData(this);
        ll.open();
        Profile_Data p=ll.actual();
        ll.close();
        l.put("type","get_user_info");
        l.put("me",p.dbid);
        l.put("user",id);
        String link=this.getString(R.string.link);
        new Connector(link,this,this,l,"Loading data","Please wait",false,true).execute();
    }

    @Override
    public void success(String info) throws JSONException {
        final JSONObject object=new JSONObject(info);
        TextView username = (TextView) findViewById(R.id.user_name);
        username.setText(object.getString("username"));

        TextView status = (TextView) findViewById(R.id.status);
        status.setText(object.getString("status"));

        TextView name = (TextView) findViewById(R.id.num_following);
        name.setText(object.getString("following"));
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bh=new Bundle();
                try {
                    bh.putString("id",object.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent i=new Intent(ViewUser.this, Following.class);
                i.putExtras(bh);
                startActivity(i);
            }
        });
        TextView name1 = (TextView) findViewById(R.id.num_followers);
        name1.setText(object.getString("followers"));
        name1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bh=new Bundle();
                try {
                    bh.putString("id",object.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent i=new Intent(ViewUser.this, Followers.class);
                i.putExtras(bh);
                startActivity(i);
            }
        });

        RelativeLayout lp=(RelativeLayout)findViewById(R.id.att_wraper);
        lp.setVisibility(View.GONE);
        RelativeLayout followers_wrapper=(RelativeLayout)findViewById(R.id.followers_wraper);
        followers_wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewUser.this, Followers.class));
            }
        });
        TextView name2 = (TextView) findViewById(R.id.num_attending);
        name2.setText( object.getString("num"));
        get_events();
        ImageView imageView=(ImageView)findViewById(R.id.user_photo);
        String image_link=this.getString(R.string.link)+"UserProfilePics/"+object.getString("image_name");
        new GetImage(image_link,this,imageView).execute();

    }
    public void get_events() {
        l.put("type","get_user_events_attending");
        String link=this.getString(R.string.link);
        new ConnectorAttending(link,this,this,l,"","",false,false).execute();
    }
    @Override
    public void fail(String info) {

    }

    @Override
    public void display(String info, Context cont) {

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
            boolean me_attending=get.getInt("me_attending")==1?true:false;
            EventStruct event=new EventStruct(id,djs,attending,event_type,host_id,"0",tbl_avail,specials,gen_fee,vip_fee,name,start_time,end_time,latlong,address,logo,"",me_attending);
            b.add(event);
        }
        ListView l=(ListView)findViewById(R.id.show_my_events);
        ArrayList<EventStruct> dummy = new ArrayList<>();
        l.setAdapter(new UpcomingAdapter(this, dummy,this));
        for (int i = 0; i < b.size(); i++) {
            View convertView = LayoutInflater.from(this).inflate(
                    R.layout.show_event, null, false);
            final EventStruct get = b.get(i);

            TextView name22 = (TextView) convertView.findViewById(R.id.event_name);
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
            name22.setText(get.name);
            ImageView image=(ImageView)convertView.findViewById(R.id.logo);
            String link=this.getString(R.string.link)+"EventImages/"+get.logo;
            new GetImage(link,this,image).execute();
            l.addFooterView(convertView);
        }
    }
}
