package com.example.dopetheimmortal.turntapp.Useful;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by jackson on 2016/06/01.
 */
public class Permissions {
    public void set_read_write(Activity cont) {
        String[] PERMISIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int permission = ActivityCompat.checkSelfPermission(cont, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(cont, PERMISIONS, 1);
        }
    }

    public void set_location_permissions(Activity context) {
        String[] PERMISIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(context, PERMISIONS, 1);
    }
}
