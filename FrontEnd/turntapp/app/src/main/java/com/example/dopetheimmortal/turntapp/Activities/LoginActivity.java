package com.example.dopetheimmortal.turntapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

import com.example.dopetheimmortal.turntapp.LocalData.UserLocalData;
import com.example.dopetheimmortal.turntapp.R;
import com.example.dopetheimmortal.turntapp.Useful.ConnectorCallback;
import com.example.dopetheimmortal.turntapp.connector.Connector;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements OnClickListener, ConnectorCallback {


    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private TextView size;
    LoginButton loginButton;
    CallbackManager callbackManager;
    ProfileTracker mProfileTracker;
    Button mEmailSignInButton;
    Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        initialize_fb();
        initialize_components();
        check_account();
        // Set up the login form.
    }
    private void check_account() {
        UserLocalData get=new UserLocalData(this);
        get.open();
        boolean check= get.check();
        get.close();
        if(check){
            Toast.makeText(LoginActivity.this, "OKay you have been verified, then you'd be sent to the home screen", Toast.LENGTH_LONG).show();
        }
    }


    private void initialize_components() {
        size = (TextView) findViewById(R.id.size);
        mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailView = (EditText) findViewById(R.id.email);
        mEmailView.setBackgroundResource(R.drawable.error);
        mEmailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 6) {
                    size.setTextColor(Color.RED);
                    if (6 - s.length() > 0) {
                        size.setText(Integer.toString(6 - s.length()));
                    } else {
                        size.setText("");
                    }
                    mEmailView.setBackgroundResource(R.drawable.error);
                } else {
                    size.setTextColor(Color.GREEN);
                    String pass = mPasswordView.getText().toString();
                    mEmailView.setBackgroundResource(R.drawable.accepted);
                    if (pass.length() > 7 && !pass.contains(" ")) {
                        mEmailSignInButton.setClickable(true);
                    }
                }
            }
        });
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setBackgroundResource(R.drawable.error);
        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 7) {
                    mPasswordView.setBackgroundResource(R.drawable.error);
                } else {
                    String name = mEmailView.getText().toString();
                    if (name.length() >= 6 && !p.matcher(s).find()) {
                        mEmailSignInButton.setClickable(true);
                        mPasswordView.setBackgroundResource(R.drawable.accepted);
                    }
                }
            }
        });
        mEmailSignInButton.setOnClickListener(this);
        mEmailSignInButton.setClickable(false);
    }

    private void initialize_fb() {

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            mProfileTracker.stopTracking();
                        }
                    };
                    mProfileTracker.startTracking();
                    Profile profile = Profile.getCurrentProfile();
                    Log.v("facebook - profile", profile.getFirstName());
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    Log.v("facebook - profile", profile.getFirstName());
                }
            }


            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "You canceled the login", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                HashMap<String, String> data = new HashMap<>();
                data.put("user", mEmailView.getText().toString());
                data.put("pass", mPasswordView.getText().toString());
                String link = this.getString(R.string.ip) + this.getString(R.string.login);
                new Connector(link, this, this, data, "Logging in", "Loggin in\n Please wait....", false, true).execute();
                break;
            case R.id.sign_up_redirect_button:
                break;
        }
    }


    @Override
    public void success(String success) throws JSONException {
        JSONObject obj = new JSONObject(success);
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

        Toast.makeText(LoginActivity.this, "Okay you have been succesfully logged in, then you'd be sent to the home screen", Toast.LENGTH_LONG).show();
       // Intent i=new Intent(Login.this,Home.class);
        //startActivity(i);
      //  finish();

    }
    @Override
    public void fail(String fail) {

    }

    @Override
    public void display(String Message, Context cont) {
        Toast.makeText(cont, Message, Toast.LENGTH_LONG).show();
    }
}
