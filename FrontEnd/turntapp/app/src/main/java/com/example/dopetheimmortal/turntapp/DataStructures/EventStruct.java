package com.example.dopetheimmortal.turntapp.DataStructures;

/**
 * Created by jackson on 2016/08/11.
 */
public class EventStruct {
    public  String id,djs,attending,event_type,host_id,rating,tbl_avail,specials,gen_fee,vip_fee,name,start_time,end_time,latlong,address,logo,host_name;
    public boolean me_attending=false;
    public EventStruct(String id, String djs, String attending, String event_type, String host_id, String rating, String tbl_avail, String specials, String gen_fee, String vip_fee, String name, String start_time, String end_time,String latlong,String address,String logo,String host_name,boolean me_attending) {
        this.id = id;
        this.djs = djs;
        this.attending = attending;
        this.event_type = event_type;
        this.host_id = host_id;
        this.rating = rating;
        this.tbl_avail = tbl_avail;
        this.specials = specials;
        this.gen_fee = gen_fee;
        this.vip_fee = vip_fee;
        this.name = name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.latlong=latlong;
        this.address=address;
        this.logo=logo;
        this.host_name=host_name;
        this.me_attending=me_attending;
    }
}
