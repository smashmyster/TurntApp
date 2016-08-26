package com.example.dopetheimmortal.turntapp.HomeFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dopetheimmortal.turntapp.LocalData.UserLocalData;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.Useful.Profile_Data;


/**
 * Created by jackson on 2016/03/26.
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

        return lay;
    }

}
