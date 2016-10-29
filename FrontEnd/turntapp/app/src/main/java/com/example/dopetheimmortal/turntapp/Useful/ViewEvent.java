package com.example.dopetheimmortal.turntapp.Useful;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dopetheimmortal.turntapp.Activities.InviteUsers;
import com.example.dopetheimmortal.turntapp.DataStructures.EventStruct;
import com.example.dopetheimmortal.turntapp.LocalData.UserLocalData;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.connector.CallBackAttending;
import com.example.dopetheimmortal.turntapp.connector.Connector;
import com.example.dopetheimmortal.turntapp.connector.ConnectorAttending;
import com.example.dopetheimmortal.turntapp.connector.ConnectorCallback;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;

import org.json.JSONException;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by jackson on 2016/10/29.
 */

public class ViewEvent implements ConnectorCallback, CallBackAttending {
    Dialog dialog;
    Context context;
    Activity activity;
    SessionConfiguration config = new SessionConfiguration.Builder()
            // mandatory
            .setClientId("_ZrUfd8-PPK5GiQNcuqV8oJqpQWIFEed")
            // required for enhanced button features
            .setServerToken("kVKaKz9cz3u_rIX39QExrlzIgCAVeU6rWChpLKi5")
            // required for implicit grant authentication
            .setRedirectUri("YOUR_REDIRECT_URI")
            // required scope for Ride Request Widget features
            .setScopes(Arrays.asList(Scope.RIDE_WIDGETS))
            // optional: set Sandbox as operating environment
            .setEnvironment(SessionConfiguration.Environment.SANDBOX)
            .build();

    public ViewEvent() {
        UberSdk.initialize(config);
    }

    public void view_event(EventStruct struct, Context context, Activity activity) {
        this.context = context;
        this.activity=activity;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = get_view(struct, context, activity);
        ScrollView scrollView = new ScrollView(context);
        scrollView.addView(v);
        builder.setView(scrollView);
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public View get_view(final EventStruct struct, final Context context, final Activity activity) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_event_details, null, false);
        TextView name = (TextView) view.findViewById(R.id.view_event_name);
        TextView start_time = (TextView) view.findViewById(R.id.view_start_time);
        final TextView fee = (TextView) view.findViewById(R.id.view_general_fee);
        TextView vip = (TextView) view.findViewById(R.id.view_vip_fee);
        TextView attending = (TextView) view.findViewById(R.id.view_attending);
        name.setText(struct.name);
        start_time.setText(struct.start_time);
        fee.setText("General Fee : " + struct.gen_fee);
        vip.setText("VIP fee : " + struct.vip_fee);
        attending.setText(struct.attending + " People are attending");
        ImageView invite = (ImageView) view.findViewById(R.id.invite_users);
        CheckBox check = (CheckBox) view.findViewById(R.id.attending_status);
        if (struct.me_attending) {
            check.setChecked(true);
        }
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String link = context.getString(R.string.link);
                HashMap<String, String> send = new HashMap<String, String>();
                UserLocalData local = new UserLocalData(context);
                local.open();
                String id = local.actual().dbid;
                local.close();
                send.put("id", id);
                send.put("event", struct.id);
                if (isChecked) {
                    send.put("type", "attending");
                } else {
                    send.put("type", "unattending");
                }
                new Connector(link, ViewEvent.this, context, send, "Updating status", "Loading....", false, true).execute();
            }
        });
        final Bundle bh = new Bundle();
        bh.putString("data", struct.id);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = context.getString(R.string.link);
                HashMap<String, String> send = new HashMap<String, String>();
                UserLocalData local = new UserLocalData(context);
                local.open();
                String id = local.actual().dbid;
                local.close();
                send.put("type","get_invitable_list");
                send.put("me", id);
                send.put("event", struct.id);
                new ConnectorAttending(link, ViewEvent.this, context, send, "Getting list", "Please wait", false, true).execute();
            }
        });
        final RideRequestButton requestButton=(RideRequestButton)view.findViewById(R.id.ride);
        ServerTokenSession session = new ServerTokenSession(config);
        requestButton.setSession(session);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run_get_ride(requestButton);
            }
        });
        return view;
    }

    private void run_get_ride(RideRequestButton requestButton) {
//        requestButton.loadRideInformation();
        RideParameters rideParams = new RideParameters.Builder()
                // Required for pickup estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of pickup location
                .setPickupLocation(37.775304, -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
                .build();
// set parameters for the RideRequestButton instance
        requestButton.setRideParameters(rideParams);
        requestButton.loadRideInformation();
    }

    @Override
    public void success(String info) throws JSONException {
        Toast.makeText(context, "Updated", Toast.LENGTH_LONG).show();
    }

    @Override
    public void fail(String info) {

    }

    @Override
    public void display(String info, Context cont) {

    }

    @Override
    public void success_my_events(String s) throws JSONException {
        Bundle bh=new Bundle();
        bh.putString("data",s);
        Intent i = new Intent(context, InviteUsers.class);
        i.putExtras(bh);
        activity.startActivity(i);
        dialog.dismiss();
    }
}
