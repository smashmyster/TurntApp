package com.example.dopetheimmortal.turntapp.Useful;

import java.text.DateFormatSymbols;

/**
 * Created by jackson on 2016/08/22.
 */
public class Date {
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }
}
