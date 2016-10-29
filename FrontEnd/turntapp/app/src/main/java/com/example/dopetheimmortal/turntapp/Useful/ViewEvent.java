package com.example.dopetheimmortal.turntapp.Useful;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dopetheimmortal.turntapp.Activities.InviteUsers;
import com.example.dopetheimmortal.turntapp.DataStructures.EventStruct;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.connector.ConnectorCallback;

import org.json.JSONException;
import org.w3c.dom.Text;

/**
 * Created by jackson on 2016/10/29.
 */

public class ViewEvent implements ConnectorCallback{
    public void view_event(EventStruct struct, Context context,Activity activity){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
    }
    public void get_view(EventStruct struct, final Context context, final Activity activity){
        View view= LayoutInflater.from(context).inflate(R.layout.view_event_details,null,false);
        TextView name=(TextView)view.findViewById(R.id.view_event_name);
        TextView start_time=(TextView)view.findViewById(R.id.view_start_time);
        TextView fee=(TextView)view.findViewById(R.id.view_general_fee);
        TextView vip=(TextView)view.findViewById(R.id.view_vip_fee);
        TextView attending=(TextView)view.findViewById(R.id.view_attending);
        name.setText(struct.name);
        start_time.setText(struct.start_time);
        fee.setText("General Fee : "+struct.gen_fee);
        vip.setText("VIP fee : "+struct.vip_fee);
        attending.setText(struct.attending+" People are attending");
        ImageView invite=(ImageView)view.findViewById(R.id.invite_users);
        CheckBox check=(CheckBox)view.findViewById(R.id.attending_status);
        if(struct.attending.equals("1")){
            check.setChecked(true);
        }
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                }
            }
        });
       final  Bundle bh=new Bundle();
        bh.putString("data",struct.id);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, InviteUsers.class);
                i.putExtras(bh);
                activity.startActivity(i);
            }
        });
    }

    @Override
    public void success(String info) throws JSONException {

    }

    @Override
    public void fail(String info) {

    }

    @Override
    public void display(String info, Context cont) {

    }
}
