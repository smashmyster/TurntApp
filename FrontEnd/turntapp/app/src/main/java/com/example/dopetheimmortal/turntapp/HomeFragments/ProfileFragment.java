package com.example.dopetheimmortal.turntapp.HomeFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dopetheimmortal.turntapp.Activities.Followers;
import com.example.dopetheimmortal.turntapp.Activities.Following;
import com.example.dopetheimmortal.turntapp.Activities.invites;
import com.example.dopetheimmortal.turntapp.Adapters.UpcomingAdapter;
import com.example.dopetheimmortal.turntapp.DataStructures.EventStruct;
import com.example.dopetheimmortal.turntapp.LocalData.UserLocalData;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.Useful.Profile_Data;
import com.example.dopetheimmortal.turntapp.Useful.StaticData;
import com.example.dopetheimmortal.turntapp.Useful.ViewEvent;
import com.example.dopetheimmortal.turntapp.connector.GetImage;

import java.util.ArrayList;


/**
 * Created by jackson on 2016/03/26.
 * edited by Thabiso on 2016/
 */
public class ProfileFragment extends Fragment {
    ArrayList<EventStruct> b;
    public ProfileFragment() {
        // Required empty public constructor
    }

    public void set_data(ArrayList<EventStruct> b){
        this.b=b;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View lay =inflater.inflate(R.layout.profile_freg, container, false);
        UserLocalData data=new UserLocalData(getContext());
        data.open();
        Profile_Data person =data.actual();
        data.close();
        ImageView imageView=(ImageView)lay.findViewById(R.id.user_photo);
        String image_link=getContext().getString(R.string.link)+"UserProfilePics/"+StaticData.image_name;
        new GetImage(image_link,getContext(),imageView).execute();
        TextView username = (TextView) lay.findViewById(R.id.user_name);
        username.setText(person.surname);

        TextView status = (TextView) lay.findViewById(R.id.status);
        status.setText(person.status);

        TextView name = (TextView) lay.findViewById(R.id.num_following);
        name.setText(StaticData.following);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bh=new Bundle();
                UserLocalData local=new UserLocalData(getContext());
                local.open();
                String id=local.actual().dbid;
                local.close();
                bh.putString("id",id);
                Intent i=new Intent(getContext(), Following.class);
                i.putExtras(bh);
                startActivity(i);
            }
        });
        TextView name1 = (TextView) lay.findViewById(R.id.num_followers);
        name1.setText(StaticData.followers);
        RelativeLayout followers_wrapper=(RelativeLayout)lay.findViewById(R.id.followers_wraper);
        followers_wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bh=new Bundle();
                UserLocalData local=new UserLocalData(getContext());
                local.open();
                String id=local.actual().dbid;
                local.close();
                bh.putString("id",id);
                Intent i=new Intent(getContext(), Followers.class);
                i.putExtras(bh);
                startActivity(i);
            }
        });
        TextView name2 = (TextView) lay.findViewById(R.id.num_attending);
         name2.setText(StaticData.invite);
        name2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),invites.class));
            }
        });
        ListView l=(ListView)lay.findViewById(R.id.show_my_events);
        ArrayList<EventStruct> dummy = new ArrayList<>();
        l.setAdapter(new UpcomingAdapter(getContext(), dummy,getActivity()));
        for (int i = 0; i < b.size(); i++) {
            View convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.show_event, null, false);
            final EventStruct get = b.get(i);

            TextView name22 = (TextView) convertView.findViewById(R.id.event_name);
            TextView host = (TextView)convertView.findViewById(R.id.host);
            host.setText(get.host_name);
            ImageButton btn=(ImageButton)convertView.findViewById(R.id.nav);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "http://maps.google.com/maps?daddr="+get.latlong;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
                    startActivity(intent);

                }
            });
            name22.setText(get.name);
            ImageView image=(ImageView)convertView.findViewById(R.id.logo);
            String link=getContext().getString(R.string.link)+"EventImages/"+get.logo;
            new GetImage(link,getContext(),image).execute();
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ViewEvent().view_event(get,getContext(),getActivity());
                }
            });
            l.addFooterView(convertView);
        }
        return lay;
    }

}
