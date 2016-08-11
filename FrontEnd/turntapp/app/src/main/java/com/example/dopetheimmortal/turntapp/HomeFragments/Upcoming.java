package com.example.dopetheimmortal.turntapp.HomeFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dopetheimmortal.turntapp.R;


/**
 * Created by jackson on 2016/03/26.
 */
public class Upcoming extends Fragment {
    public Upcoming() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.upcoming_freg, container, false);

    }

}
