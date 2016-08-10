package com.example.dopetheimmortal.turntapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.dopetheimmortal.turntapp.Useful.ConnectorCallback;
import com.example.dopetheimmortal.turntapp.Useful.ImageProcessing;
import com.example.dopetheimmortal.turntapp.Useful.Permissions;
import com.example.dopetheimmortal.turntapp.connector.Connector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements OnClickListener, ConnectorCallback {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initialize_views();
        set_image_listener();
    }

    private void set_image_listener() {
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_method();
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
        sign_up.put("thumb", "");
        sign_up.put("regid", "");
        sign_up.put("ext", "");

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
        new Connector(link, this, this, data, "Logging in", "Logging in\n Please wait", false, true).execute();
    }

    @Override
    public void success(String info) throws JSONException {
        JSONObject obj = new JSONObject(info);
        String id = obj.getString("id");
        String user = obj.getString("user");
        String pass = obj.getString("pass");
        String name = obj.getString("name");
        String surname = obj.getString("surname");
        String email = obj.getString("email");
        String phone = obj.getString("phone");
        String bday = obj.getString("bday");
        String image = obj.getString("image");
        String pp = obj.getString("pp");
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
        insert.createentry(id, user, pass, email, name, surname, bday, phone, status, image, pp, loyalty, state, priv, regid, followers, following, gender);
        insert.close();
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
//                startActivity(new Intent(LoginActivity.this, MainMenu.class));
                finish();
            }
        });

        build.show();
    }

    @Override
    public void onClick(View view) {

    }
}
