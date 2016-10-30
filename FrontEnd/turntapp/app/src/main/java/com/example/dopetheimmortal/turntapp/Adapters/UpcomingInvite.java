package com.example.dopetheimmortal.turntapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dopetheimmortal.turntapp.Activities.invites;
import com.example.dopetheimmortal.turntapp.DataStructures.EventStruct;
import com.example.dopetheimmortal.turntapp.HomeFragments.Upcoming;
import com.example.dopetheimmortal.turntapp.LocalData.UserLocalData;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.Useful.StaticData;
import com.example.dopetheimmortal.turntapp.connector.Connector;
import com.example.dopetheimmortal.turntapp.connector.ConnectorCallback;
import com.example.dopetheimmortal.turntapp.connector.GetImage;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jackson on 2016/08/11.
 */
public class UpcomingInvite extends ArrayAdapter<EventStruct> implements ConnectorCallback{
    ArrayList<EventStruct> list;
    Context context;
    Activity activity;
    String id="";
    public UpcomingInvite(Context context, ArrayList<EventStruct> data, Activity activity) {
        super(context, 0, data);
        this.context = context;
        this.list = data;
        this.activity = activity;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final EventStruct get = list.get(position);


        convertView = LayoutInflater.from(context).inflate(
                R.layout.show_event_invite, parent, false);

        TextView name = (TextView) convertView.findViewById(R.id.event_name);
        TextView host = (TextView) convertView.findViewById(R.id.host);
        //CheckBox response = (CheckBox) convertView.findViewById(R.id.response);
        host.setText(get.host_name);
        ImageButton btn = (ImageButton) convertView.findViewById(R.id.nav);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://maps.google.com/maps?daddr=" + get.latlong;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                activity.startActivity(intent);


            }
        });
       // if(response.isChecked()){

       // }
        name.setText(get.name);
        ImageView image=(ImageView)convertView.findViewById(R.id.logo);
        String link=getContext().getString(R.string.link)+"EventImages/"+get.logo;
        new GetImage(link,getContext(),image).execute();
        Button accept=(Button)convertView.findViewById(R.id.accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLocalData o = new UserLocalData(context);
                o.open();
                String me = o.actual().dbid;
                o.close();
                String link = context.getString(R.string.link);
                HashMap<String, String> data = new HashMap<>();
                data.put("type", "respond_to_invite");
                data.put("me", me);
                data.put("event",get.id);
                data.put("accept", "1");
                id=get.id;
                new Connector(link,UpcomingInvite.this, context, data, "", "", false, true).execute();
//                Toast.makeText(getApplicationContext(),"gets here",Toast.LENGTH_LONG).show();
            }
        });
        Button decline=(Button)convertView.findViewById(R.id.decline);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLocalData o = new UserLocalData(context);
                o.open();
                String me = o.actual().dbid;
                o.close();
                String link = context.getString(R.string.link);
                HashMap<String, String> data = new HashMap<>();
                data.put("type", "respond_to_invite");
                data.put("me", me);
                data.put("event",get.id);
                data.put("accept", "0");
                id=get.id;
                new Connector(link,UpcomingInvite.this, context, data, "", "", false, true).execute();
            }
        });

        return convertView;
    }

    @Override
    public void success(String info) throws JSONException {
        StaticData.invites.refresh();
    }

    @Override
    public void fail(String info) {

    }

    @Override
    public void display(String info, Context cont) {

    }
}
