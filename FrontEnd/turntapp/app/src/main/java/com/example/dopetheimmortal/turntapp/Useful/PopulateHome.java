package com.example.dopetheimmortal.turntapp.Useful;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dopetheimmortal.turntapp.Activities.Followers;
import com.example.dopetheimmortal.turntapp.LocalData.UserLocalData;
import com.example.dopetheimmortal.turntapp.connector.CallBackAction;


/**
 * Created by jackson on 2016/03/26.
 */
public class PopulateHome {
    CallBackAction from;
    Context page;
    public PopulateHome(CallBackAction from,Context page){
        this.from=from;
        this.page=page;
    }
    public void populateProfile(TextView username,TextView status,TextView following,TextView followers,TextView attending,RelativeLayout following_wrapper,RelativeLayout followers_wrapper,RelativeLayout attending_wrapper){
        UserLocalData get=new UserLocalData(page);
        get.open();
        Profile_Data person=get.actual();
        get.close();
        username.setText(person.user);
        status.setText(person.status);
        following.setText(person.following);
        followers.setText(person.followers);
        followers_wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from.nextActivity(page, Followers.class);
            }
        });

    }
}
