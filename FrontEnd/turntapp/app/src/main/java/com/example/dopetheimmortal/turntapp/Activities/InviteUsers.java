package com.example.dopetheimmortal.turntapp.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dopetheimmortal.turntapp.Adapters.InviteAdapter;
import com.example.dopetheimmortal.turntapp.DataStructures.GeneralUser;
import com.example.dopetheimmortal.turntapp.LocalData.UserLocalData;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.Useful.ConnectorCallbackInvite;
import com.example.dopetheimmortal.turntapp.connector.ConnectorInvite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jackson on 2016/09/08.
 */
public class InviteUsers extends Activity implements ConnectorCallbackInvite{
    ListView list;
    String event="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_people_list);
        list=(ListView)findViewById(R.id.invite_people_list);
        ArrayList<GeneralUser>users=new ArrayList<>();
        ArrayList<GeneralUser>dummy=new ArrayList<>();
        InviteAdapter adapter=new InviteAdapter(this,dummy);
        list.setAdapter(adapter);
        Bundle bh=getIntent().getExtras();
        String data=bh.getString("data");
        try {
            event=new JSONObject(data).getString("id");
            JSONArray arr=(new JSONObject(data)).getJSONArray("people");
            for (int i=0;i<arr.length();i++){
                JSONObject o=arr.getJSONObject(i);
                String id= o.getString("id");
                String name= o.getString("name");
                String surname= o.getString("surname");
                String image_name= o.getString("image_name");
                String following= o.getString("following");
                String followers= o.getString("followers");
                String status= o.getString("status");
                String invited= o.getString("invited");
                GeneralUser use=new GeneralUser(id,name,surname,image_name,following,followers,status,invited);
                users.add(use);
                list.addFooterView(get_view(use));
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
            final  ImageView image=(ImageView)convertView.findViewById(R.id.send_invite_img);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String,String>send=new HashMap<String, String>();
                    send.put("type","invite_user");
                    send.put("user",get.id);
                    send.put("event",event);
                    UserLocalData user=new UserLocalData(InviteUsers.this);
                    user.open();
                    send.put("me",user.actual().dbid);
                    user.close();
                    String link=InviteUsers.this.getString(R.string.link);
                    new ConnectorInvite(link,InviteUsers.this,InviteUsers.this,send,"","",true,false,image).execute();
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
        return convertView;
    }

    @Override
    public void success(ImageView imageView) {
//        imageView.setImageBitmap(this.getResources());
        imageView.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
    }

    @Override
    public void fail(String fail) {
        Toast.makeText(this,fail,Toast.LENGTH_LONG).show();
    }
}
