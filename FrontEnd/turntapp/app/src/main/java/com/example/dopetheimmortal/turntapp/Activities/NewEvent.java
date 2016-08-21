package com.example.dopetheimmortal.turntapp.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.Useful.Date;

import java.util.HashMap;

/**
 * Created by jackson on 2016/08/21.
 */
public class NewEvent extends Activity implements View.OnClickListener {
    EditText name, address, djs, specials, fee, vip, stime, etime;
    Button create_event;
    HashMap<String, String> upload = new HashMap<>();
    String[] times = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user_event);
        name = (EditText) findViewById(R.id.new_event_name);
        address = (EditText) findViewById(R.id.new_event_address);
        djs = (EditText) findViewById(R.id.new_event_djs);
        specials = (EditText) findViewById(R.id.new_event_specials);
        vip = (EditText) findViewById(R.id.new_event_vip_fee);
        fee = (EditText) findViewById(R.id.new_event_gen_fee);
        stime = (EditText) findViewById(R.id.new_event_start_time);
        etime = (EditText) findViewById(R.id.new_event_end_time);
        create_event = (Button) findViewById(R.id.upload);
        create_event.setOnClickListener(this);
        stime.setOnClickListener(this);
        etime.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_event_start_time:
                System.out.println("Whats going on");
                select_date("Select Starting Date of Event", "Select Starting Time of Event", true);
                break;
            case R.id.new_event_end_time:
                select_date("Select Ending Date of Event", "Select Ending Time of Event", false);
                break;
            case R.id.upload:
                for (String k : upload.keySet())
                    System.out.println(upload.get(k));
                break;
        }
    }

    public void select_date(String title_date, final String title_time, final boolean cont) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title_date);
        LinearLayout lay = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lay.setLayoutParams(lp);
        final TimePicker time_picker = new TimePicker(this);
        final DatePicker date_picker = new DatePicker(this);
        lay.addView(date_picker);
        builder.setView(lay);
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (cont) {
                    android.app.AlertDialog.Builder get_time = new android.app.AlertDialog.Builder(NewEvent.this);
                    get_time.setTitle(title_time);
                    get_time.setView(time_picker);
                    get_time.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            stime.setText(""+date_picker.getDayOfMonth()+"/"+new Date().getMonth(date_picker.getMonth())+"/"+date_picker.getYear()+"  "+time_picker.getHour()+":"+time_picker.getMinute());
                            upload.put("start_time", get_date(date_picker, time_picker));
                        }
                    });
                    get_time.show();
                } else {
                    android.app.AlertDialog.Builder get_time = new android.app.AlertDialog.Builder(NewEvent.this);
                    get_time.setTitle(title_time);
                    get_time.setView(time_picker);
                    get_time.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            etime.setText(""+date_picker.getDayOfMonth()+"/"+new Date().getMonth(date_picker.getMonth())+"/"+date_picker.getYear()+"  "+time_picker.getHour()+":"+time_picker.getMinute());
                            upload.put("end_time", get_date(date_picker, time_picker));
                        }
                    });
                    get_time.show();
                }
            }
        });
        builder.show();
    }

    public String get_date(DatePicker date_picker, TimePicker time_picker) {
        String year, month, day, hour, min;
        year = "" + date_picker.getYear();
        month = ("" + date_picker.getMonth()).length() == 2 ? "" + date_picker.getMonth() : "0" + date_picker.getMonth();
        day = ("" + date_picker.getDayOfMonth()).length() == 2 ? "" + date_picker.getDayOfMonth() : "0" + date_picker.getDayOfMonth();
        hour = ("" + time_picker.getHour()).length() == 2 ? "" + time_picker.getHour() : "0" + time_picker.getHour();
        min = ("" + time_picker.getMinute()).length() == 2 ? "" + time_picker.getMinute() : "0" + time_picker.getMinute();
        String time = year + month + day + hour + min;
        return time;
    }
}
