package com.example.dopetheimmortal.turntapp.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;

import com.example.dopetheimmortal.turntapp.DataStructures.EventStruct;
import com.example.dopetheimmortal.turntapp.HomeFragments.Ongoing;
import com.example.dopetheimmortal.turntapp.HomeFragments.ProfileFragment;
import com.example.dopetheimmortal.turntapp.HomeFragments.Upcoming;

import java.util.ArrayList;
import java.util.HashMap;



/**
 * Created by jackson on 2016/07/18.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int size;
    ActionBar bar;
    ArrayList<EventStruct>upcoming_events;
    ArrayList<EventStruct> ongoing_events;
    public ViewPagerAdapter(FragmentManager fm,ArrayList<EventStruct>upcoming_events,ArrayList<EventStruct> ongoing_events) {
        super(fm);
        this.upcoming_events=upcoming_events;
        this.ongoing_events=ongoing_events;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Upcoming up=new Upcoming();
                up.set_data(upcoming_events);
                return up;
            case 1:
                return new Ongoing();
            case 2:
                return new ProfileFragment();
        }
        return new ProfileFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Upcoming";
            case 1:
                return "Ongoing";
            case 2:
                return "Profile";
        }
        return "";
    }
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
//        View view=(View)object;
//        ((ViewPager)container).removeView(view);
//        view=null;
//
//    }
}
