package com.example.dopetheimmortal.turntapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dopetheimmortal.turntapp.Adapters.FollowDataAdapter;
import com.example.dopetheimmortal.turntapp.DataStructures.FollowData;
import com.example.dopetheimmortal.turntapp.LocalData.UserLocalData;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.connector.ConnectorCallback;
import com.example.dopetheimmortal.turntapp.Useful.Profile_Data;
import com.example.dopetheimmortal.turntapp.connector.Connector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by jackson on 2016/03/26.
 */
public class Followers extends Activity implements ConnectorCallback {
    ListView followers;
    View head,tail;
    ListAdapter adapter;

    ArrayList<FollowData> people;
    Connector connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_data_show);
        get_loading();
        initialize();
        people=new ArrayList<>();
        HashMap<String,String> info=get_data();
        info.put("type","get_my_followers");
        UserLocalData user=new UserLocalData(this);
        user.open();
        String id=user.actual().dbid;
        user.close();
        info.put("id",id);
        String link=this.getString(R.string.link);
        connect=new Connector(link,this,this,info,"Loading","",false,true);
        connect.execute();
    }

    private void initialize() {
        ArrayList<FollowData> people=new ArrayList<>();
        followers=(ListView)findViewById(R.id.follow_data);
        adapter=new FollowDataAdapter(this, people);
        followers.setAdapter(adapter);

        head=get_loading();
        followers.addHeaderView(head);
    }

    public View get_loading() {
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout convertView = null;
        convertView = (LinearLayout) inflater.inflate(R.layout.progressbar, null);
        ProgressBar spinner= (ProgressBar)convertView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        return convertView;
    }

    public HashMap<String, String> get_data() {
        HashMap<String,String> info=new HashMap<>();
        UserLocalData get=new UserLocalData(this);
        get.open();
        Profile_Data person=get.actual();
        get.close();
        info.put("command", "2");
        info.put("me", person.dbid);
        return info;
    }

    @Override
    public void success(String info) throws JSONException {
        followers.removeHeaderView(head);
        JSONObject obj=new JSONObject(info);
        JSONArray arr=obj.getJSONArray("people");
        for(int i=0;i<arr.length();i++){
            JSONObject item=arr.getJSONObject(i);
            String id=item.getString("id");
            String name=item.getString("name");
            String surname=item.getString("surname");
            String image=item.getString("image_name");
            String status=item.getString("status");
//            int state=item.getInt("follow_back");
            final FollowData person=new FollowData(name,image,id,surname,status);
            View toadd=get_person(person);
            toadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Followers.this,person.id, Toast.LENGTH_LONG).show();
                }
            });
            followers.addFooterView(toadd);
            people.add(person);
        }
    }

    @Override
    public void fail(String info) {
        followers.removeHeaderView(head);

    }

    @Override
    public void display(String info, Context cont) {

    }

    public View get_person(FollowData get) {
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout convertView = null;
        convertView = (RelativeLayout) inflater.inflate(R.layout.follow_data_adapter, null);

        TextView username = (TextView) convertView.findViewById(R.id.user_status);
        username.setText(get.status);
        TextView name = (TextView) convertView.findViewById(R.id.follow_name);
        name.setText(get.name+" "+get.surname);

        return convertView;
    }

}
