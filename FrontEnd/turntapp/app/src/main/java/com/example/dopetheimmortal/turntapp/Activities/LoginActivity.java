package com.example.dopetheimmortal.turntapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dopetheimmortal.turntapp.LocalData.UserLocalData;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.Services.GCMRegistrationIntentService;
import com.example.dopetheimmortal.turntapp.connector.ConnectorCallback;
import com.example.dopetheimmortal.turntapp.Useful.ImageProcessing;
import com.example.dopetheimmortal.turntapp.Useful.Permissions;
import com.example.dopetheimmortal.turntapp.connector.Connector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener, ConnectorCallback {

    EditText username, password, sign_username, sign_password, sign_confirm, sign_phone, sign_email, sign_name, sign_surname;
    ScrollView log;
    ScrollView reg;
    Button loginto;
    Button regis;
    Spinner SpinnerItems;
    ImageButton pic;
    ProgressDialog prog_dialog;
    int CAMERA_LOAD_IMG = 101, RESULT_LOAD_IMG = 102;
    HashMap<String, String> sign_up = new HashMap<>();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        check_account();
        get_reg_id();
        initialize_views();
        set_image_listener();

    }

    private void check_account() {
        UserLocalData check=new UserLocalData(this);
        check.open();
        if(check.check()){
            startActivity(new Intent(this,Home.class));
            finish();
        }else{

        }
    }

    private void set_image_listener() {
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_read_write(LoginActivity.this);
            }
        });
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
                sign_up.put("ext", gh[1]);

                cursor.close();
                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                pic.setImageBitmap(yourSelectedImage);
                String image_string = new ImageProcessing().encode(yourSelectedImage);
                sign_up.put("thumb", image_string);
                System.out.println(image_string);
            } else if (requestCode == CAMERA_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                Bundle extras = data.getExtras();
                Bitmap image = (Bitmap) extras.get("data");
                pic.setImageBitmap(image);
                String image_string = new ImageProcessing().encode(image);
                sign_up.put("thumb", image_string);
            } else {
                Toast.makeText(this, "You didn't picked an Image",
                        Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        prog_dialog.dismiss();
    }

    public void run_dialog() {
        prog_dialog.setMessage("Processing your image\nPlease wait");
        prog_dialog.setCancelable(true);
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

    private void initialize_views() {
        sign_up.put("ext", "");
        sign_up.put("image", "");

        sign_username = (EditText) findViewById(R.id.sign_up_username);
        sign_password = (EditText) findViewById(R.id.sign_up_password);
        sign_confirm = (EditText) findViewById(R.id.sign_up_confirm_password);
        sign_phone = (EditText) findViewById(R.id.sign_up_phone);
        sign_email = (EditText) findViewById(R.id.sign_up_email);
        sign_name = (EditText) findViewById(R.id.sign_up_name);
        sign_surname = (EditText) findViewById(R.id.sign_up_surname);
        pic = (ImageButton) findViewById(R.id.sign_up_propic);

        log = (ScrollView) findViewById(R.id.loginDetails);
        reg = (ScrollView) findViewById(R.id.registerDetails);
        loginto = (Button) findViewById(R.id.loginbut);
        regis = (Button) findViewById(R.id.signupbut);

        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.logi_password);

        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Female");
        spinnerArray.add("Male");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerItems = (Spinner) findViewById(R.id.sex);
        SpinnerItems.setAdapter(adapter);

    }

    public void select_method() {
        String[] methods = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    public void sign_up() {
        String link = this.getString(R.string.link);
        sign_up.put("type", "sign_up");
        sign_up.put("user", sign_username.getText().toString());
        sign_up.put("pass", sign_password.getText().toString());
        sign_up.put("email", sign_email.getText().toString());
        sign_up.put("phone", sign_phone.getText().toString());
        sign_up.put("gender", Integer.toString(SpinnerItems.getSelectedItemPosition()));
        sign_up.put("name", sign_name.getText().toString());
        sign_up.put("surname", sign_surname.getText().toString());
        sign_up.put("bday","10/03/2016");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token=sharedPref.getString("token","");
        sign_up.put("regid",token);
        new Connector(link, this, this, sign_up, "Signing Up", "Please wait while we sign you up", false, true).execute();

    }

    public void loginbutpushed(View view) {

        if (log.getVisibility() == View.VISIBLE) {
            /*
            * here you write code that should run when user logs in
            * */
            login();
        } else if (log.getVisibility() == View.GONE) {
            log.setVisibility(View.VISIBLE);
            reg.setVisibility(View.GONE);
        }
    }

    public void registerbutpushed(View view) {
        if (reg.getVisibility() == View.VISIBLE) {
            /*
            * here you write code that should run when user logs in
            * */
            sign_up();
        } else if (reg.getVisibility() == View.GONE) {
            reg.setVisibility(View.VISIBLE);
            log.setVisibility(View.GONE);
        }

    }

    public void login() {
        String link = this.getString(R.string.link);
        HashMap<String, String> data = new HashMap<>();
        data.put("user", username.getText().toString());
        data.put("pass", password.getText().toString());
        data.put("regid", "test");
        data.put("type", "login");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token=sharedPref.getString("token","");
        data.put("regid",token);
        new Connector(link, this, this, data, "Logging in", "Logging in\n Please wait", false, true).execute();
    }

    @Override
    public void success(String info) throws JSONException {
        JSONObject obj = new JSONObject(info);
        if (obj.getInt("code") == 1) {
            String id = obj.getString("id");
            String user = sign_up.get("user");
            String name = sign_up.get("name");
            String surname = sign_up.get("surname");
            String email = sign_up.get("email");
            String phone = sign_up.get("phone");
            String bday = sign_up.get("bday");
            String image = sign_up.get("user.")+sign_up.get("ext");
            String state = "0";
            String priv = "0";
            String regid = "reg";
            String followers = "0";
            String following = "0";
            String loyalty = "0";
            String status = "Lets get turnt";
            String gender = sign_up.get("gender");
            UserLocalData insert = new UserLocalData(this);
            insert.open();
            insert.createentry(id, user, email, name, surname, bday, phone, status, image,loyalty, state, priv, regid, followers, following, gender);
            insert.close();
        } else {
            String id = obj.getString("id");
            String user = obj.getString("username");
            String name = obj.getString("name");
            String surname = obj.getString("surname");
            String email = obj.getString("email");
            String phone = obj.getString("phone");
            String bday = obj.getString("birthday");
            String image = obj.getString("image_name");
            String state = obj.getString("state");
            String priv = obj.getString("private");
            String regid = obj.getString("regid");
            String followers = obj.getString("followers");
            String following = obj.getString("following");
            String loyalty = obj.getString("loyalty");
            String status = obj.getString("status");
            String gender = obj.getString("gender");
            UserLocalData insert = new UserLocalData(this);
            insert.open();
            insert.createentry(id, user, email, name, surname, bday, phone, status, image,loyalty, state, priv, regid, followers, following, gender);
            insert.close();
        }
        Intent i = new Intent(this, Home.class);
        startActivity(i);
        finish();
    }

    @Override
    public void fail(String info) {

    }

    @Override
    public void display(String info, Context cont) {
//        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    public void remove_regids(int num, final JSONObject obj) {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Warning");
        build.setMessage("We have found " + num + " other devices linked to this account.\n Would you like to logout of these devices?");
        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    HashMap<String, String> data = new HashMap<>();
                    data.put("user", obj.getString("id"));
                    data.put("type", "remove_regids");
                    String link = LoginActivity.this.getString(R.string.link);
                    new Connector(link, LoginActivity.this, LoginActivity.this, data, "Logging out", "Logging you out of the other devices\n Please wait", false, true).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        build.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
//                startActivity(new_name Intent(LoginActivity.this, MainMenu.class));
                finish();
            }
        });

        build.show();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                select_method();
                break;
        }
    }

    public void set_read_write(Activity cont) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            int permission = ActivityCompat.checkSelfPermission(cont, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(cont, PERMISIONS, 1);
            } else {
                select_method();
            }
        }else{
            select_method();
        }
    }

    public void get_reg_id() {
        //Notifications

        //Initializing our broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Getting the registration token from the intent
                    String token = intent.getStringExtra("token");
                    //Displaying the token as toast
//                    Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();

                    //if the intent is not with success then displaying error messages
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if(ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }

    }
    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("HomeActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }


    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("HomeActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

}
