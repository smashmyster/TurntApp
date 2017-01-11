package com.example.dopetheimmortal.turntapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dopetheimmortal.turntapp.DataStructures.FollowData;
import com.example.dopetheimmortal.turntapp.R;

import java.util.ArrayList;


public class FollowDataAdapter extends ArrayAdapter<FollowData> {
    ArrayList<FollowData> list;
    Context context;
    public FollowDataAdapter(Context context, ArrayList<FollowData> data) {
        super(context, 0, data);
        this.context = context;
        this.list = data;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemview = convertView;
        if (itemview == null) {
            itemview = convertView = LayoutInflater.from(context).inflate(
                    R.layout.follow_data_adapter, parent, false);
        }
        final FollowData get = list.get(position);

        TextView username = (TextView) convertView.findViewById(R.id.user_status);
        username.setText(get.status);
        TextView name = (TextView) convertView.findViewById(R.id.follow_name);
        name.setText(get.name);

        return itemview;
    }
}
