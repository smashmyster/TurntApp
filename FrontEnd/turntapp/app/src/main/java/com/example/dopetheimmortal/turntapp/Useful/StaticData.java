package com.example.dopetheimmortal.turntapp.Useful;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * Created by jackson on 2016/06/01.
 */
public class StaticData {
    public static String[] categories={"Water Leakage", "Pothole","Traffic Lights","Not listed "};
    public static String[] upload_report={"user","longitude","latitude","thumb","report_type","ext"};
    public static String[] sign_up={"user","pass","email","phone","gender","name","surname"};
    public static HashMap<String,ImageView> images=new HashMap<>();
    public static HashMap<String,Bitmap> downloaded_images=new HashMap<>();
}
