package com.example.dopetheimmortal.turntapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dopetheimmortal.turntapp.Adapters.UpcomingAdapter;
import com.example.dopetheimmortal.turntapp.DataStructures.EventStruct;
import com.example.dopetheimmortal.turntapp.LocalData.UserLocalData;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.Useful.CallBackAttending;
import com.example.dopetheimmortal.turntapp.Useful.ConnectorCallback;
import com.example.dopetheimmortal.turntapp.Useful.Profile_Data;
import com.example.dopetheimmortal.turntapp.connector.Connector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jackson on 2016/10/19.
 */

public class ViewUser extends AppCompatActivity implements ConnectorCallback,CallBackAttending{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_freg);
        Bundle bh=getIntent().getExtras();
        String id=bh.getString("id");
        HashMap<String,String>l=new HashMap<>();
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
        JSONObject object=new JSONObject(info);
        TextView username = (TextView) findViewById(R.id.user_name);
        username.setText(object.getString("username"));

        TextView status = (TextView) findViewById(R.id.status);
        status.setText(object.getString("username"));

        TextView name = (TextView) findViewById(R.id.num_following);
        name.setText(object.getString("following"));

        TextView name1 = (TextView) findViewById(R.id.num_followers);
        name1.setText(object.getString("followers"));
        RelativeLayout followers_wrapper=(RelativeLayout)findViewById(R.id.followers_wraper);
        followers_wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewUser.this, Followers.class));
            }
        });
        TextView name2 = (TextView) findViewById(R.id.num_attending);
        name2.setText( object.getString("num"));
        
    }

    @Override
    public void fail(String info) {

    }

    @Override
    public void display(String info, Context cont) {

    }

    @Override
    public void success_my_events(String s) throws JSONException {

    }
}
