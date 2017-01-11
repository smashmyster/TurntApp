package com.example.dopetheimmortal.turntapp.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.dopetheimmortal.turntapp.R;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by Dope The Immortal on 2016/10/29.
 */
public class GCMRegistrationIntentService extends IntentService {
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";

    public GCMRegistrationIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        registerGCM();
    }
    private void registerGCM() {
        Intent registrationComplete = null;
        String token = null;
        try {
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//            Log.w("GCMRegIntentService", "token:" + token);
            //notify to UI that registration complete success
            registrationComplete = new Intent(REGISTRATION_SUCCESS);
            registrationComplete.putExtra("token", token);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("token",token);
            editor.commit();
        } catch (Exception e) {
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }
        //Send broadcast
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}