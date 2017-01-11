package com.example.dopetheimmortal.turntapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dopetheimmortal.turntapp.DataStructures.GeneralUser;
import com.example.dopetheimmortal.turntapp.R;

import java.util.ArrayList;

/**
 * Created by jackson on 2016/09/08.
 */
public class InviteAdapter extends ArrayAdapter<GeneralUser> {
    ArrayList<GeneralUser> list;
    Context context;

    public InviteAdapter(Context context, ArrayList<GeneralUser> data) {
        super(context, 0, data);
        this.context = context;
        this.list = data;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemview = convertView;
        if (itemview == null) {
            itemview = convertView = LayoutInflater.from(context).inflate(
                    R.layout.invite_people_adapter, parent, false);
        }
        final GeneralUser get = list.get(position);

        TextView name = (TextView) convertView.findViewById(R.id.invite_user_name);
        name.setText(get.name+" "+get.surname);
        TextView status = (TextView) convertView.findViewById(R.id.invite_user_status);
        status.setText(get.status);

        return itemview;
    }
}
