package com.example.dopetheimmortal.turntapp.Useful;

import java.text.DateFormatSymbols;

/**
 * Created by jackson on 2016/08/22.
 */
public class Date {
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }
    public String get_date_formated(String date){
        String year=date.substring(0,4);
        String month=date.substring(4,6);
        String day=date.substring(6,8);
        String hour=date.substring(8,10);
        String min=date.substring(10,12);
        return "On "+day+"/"+month+"/"+year+"\n@ "+hour+":"+min;

    }
}
