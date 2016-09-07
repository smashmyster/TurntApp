package com.example.dopetheimmortal.turntapp.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dopetheimmortal.turntapp.LocalData.UserLocalData;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.Useful.ConnectorCallback;
import com.example.dopetheimmortal.turntapp.Useful.Date;
import com.example.dopetheimmortal.turntapp.Useful.ImageProcessing;
import com.example.dopetheimmortal.turntapp.Useful.Permissions;
import com.example.dopetheimmortal.turntapp.connector.Connector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by jackson on 2016/08/21.
 */
public class NewEvent extends Activity implements View.OnClickListener,ConnectorCallback{
    EditText name, address, djs, specials, fee, vip, stime, etime;
    Button create_event;
    HashMap<String, String> upload = new HashMap<>();
    String[] times = new String[2];
    ProgressDialog prog_dialog;
    int CAMERA_LOAD_IMG = 101, RESULT_LOAD_IMG = 102;
    ImageView pic;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client;
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
        pic = (ImageView) findViewById(R.id.choose_image);
        pic.setOnClickListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
            case R.id.choose_image:
//                if(check_permissions())
                    select_method();
                break;
            case R.id.upload:

                upload.put("type","create_user_event");
                upload.put("name",name.getText().toString());
                upload.put("address",address.getText().toString());
                upload.put("latlong","10.0,14.23");
                UserLocalData user=new UserLocalData(this);
                user.open();
                String me=user.actual().dbid;
                user.close();
                upload.put("me",me);
                upload.put("djs",djs.getText().toString());
                upload.put("specials",specials.getText().toString());
                upload.put("gen_fee",fee.getText().toString());
                upload.put("vip_fee",vip.getText().toString());
                String link=this.getString(R.string.link);
                new Connector(link,this,this,upload,"Creating event","Please wait......",false,true).execute();
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
                            stime.setText("" + date_picker.getDayOfMonth() + "/" + new Date().getMonth(date_picker.getMonth()) + "/" + date_picker.getYear() + "  " + time_picker.getHour() + ":" + time_picker.getMinute());
                            upload.put("start_time", get_send(date_picker, time_picker));

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
                            etime.setText("" + date_picker.getDayOfMonth() + "/" + new Date().getMonth(date_picker.getMonth()) + "/" + date_picker.getYear() + "  " + time_picker.getHour() + ":" + time_picker.getMinute());
                            upload.put("end_time", get_send(date_picker, time_picker));
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

    public String get_send(DatePicker date_picker, TimePicker time_picker) {
        String year, month, day, hour, min;
        year = "" + date_picker.getYear();
        month = (Integer.toString(date_picker.getMonth())).length() == 2 ? Integer.toString(date_picker.getMonth()): "0" + Integer.toString(date_picker.getMonth()+1);
        day = ("" + date_picker.getDayOfMonth()).length() == 2 ? "" + date_picker.getDayOfMonth() : "0" + date_picker.getDayOfMonth();
        hour = ("" + time_picker.getHour()).length() == 2 ? "" + time_picker.getHour() : "0" + time_picker.getHour();
        min = ("" + time_picker.getMinute()).length() == 2 ? "" + time_picker.getMinute() : "0" + time_picker.getMinute();
        String time = year + month + day + hour + min;
        return time;
    }

    public void select_method() {
        String[] methods = {"Camera", "Gallery"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Image selection method");
        builder.setMessage("Please select method to select image");
        prog_dialog = new ProgressDialog(this);
        run_dialog();
        final Dialog[] dialog = new Dialog[1];
        ListView model = new ListView(this);
        ArrayAdapter<String> modeladapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, methods);
        model.setAdapter(modeladapter);
        model.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                     {
                                         @Override
                                         public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                             switch (i) {
                                                 case 0:
                                                     capture_image();
                                                     prog_dialog.show();
                                                     break;
                                                 case 1:
                                                     select_gallery();
                                                     prog_dialog.show();
                                                     break;
                                             }
                                             dialog[0].dismiss();
                                             dialog[0].dismiss();
                                         }
                                     }

        );
        builder.setView(model);
        dialog[0] = builder.create();
//        manager.
        dialog[0].show();

    }

    public void capture_image() {
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (in.resolveActivity(this.getPackageManager()) != null) {
            this.startActivityForResult(in, CAMERA_LOAD_IMG);
        }
    }

    public void select_gallery() {
        new Permissions().set_read_write(this);
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        this.startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    public void run_dialog() {
        prog_dialog.setMessage("Processing your image\nPlease wait");
        prog_dialog.setCancelable(true);
    }

    public boolean check_permissions() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int defaultValue = -1;
        int requested = sharedPref.getInt("requested", defaultValue);
        return (requested == -1) ? false : true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(
                        selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                String[] f = filePath.split("/");
                String filename = f[f.length - 1];
                String[] gh = filename.split("\\.");
                upload.put("ext", gh[1]);

                cursor.close();
                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                pic.setImageBitmap(yourSelectedImage);
                String image_string = new ImageProcessing().encode(yourSelectedImage);
                upload.put("thumb", image_string);

            } else if (requestCode == CAMERA_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                Bundle extras = data.getExtras();
                Bitmap image = (Bitmap) extras.get("data");
                pic.setImageBitmap(image);
                String image_string = new ImageProcessing().encode(image);
                upload.put("thumb", image_string);
            } else {
                Toast.makeText(this, "You didn't picked an Image",
                        Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        prog_dialog.dismiss();
    }

    @Override
    public void success(String info) throws JSONException {
        JSONObject obj=new JSONObject(info);
        Toast.makeText(this,obj.getString("message"),Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void fail(String info) {

    }

    @Override
    public void display(String info, Context cont) {

    }
}
