package com.example.dopetheimmortal.turntapp.HomeFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dopetheimmortal.turntapp.Activities.Followers;
import com.example.dopetheimmortal.turntapp.LocalData.UserLocalData;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.Useful.Profile_Data;


/**
 * Created by jackson on 2016/03/26.
 * edited by Thabiso on 2016/
 */
public class ProfileFragment extends Fragment {
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View lay =inflater.inflate(R.layout.profile_freg, container, false);
        UserLocalData data=new UserLocalData(getContext());
        data.open();
        Profile_Data person =data.actual();
        data.close();

        TextView username = (TextView) lay.findViewById(R.id.user_name);
        username.setText(person.surname);

        TextView status = (TextView) lay.findViewById(R.id.status);
        status.setText(person.status);

        TextView name = (TextView) lay.findViewById(R.id.num_following);
        name.setText(person.following);

        TextView name1 = (TextView) lay.findViewById(R.id.num_followers);
        name1.setText(person.followers);
        RelativeLayout followers_wrapper=(RelativeLayout)lay.findViewById(R.id.followers_wraper);
        followers_wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Followers.class));
            }
        });
        TextView name2 = (TextView) lay.findViewById(R.id.num_attending);
        // name2.setText( person.regid);


        return lay;
    }

}
