package com.example.dopetheimmortal.turntapp.Activities;

import android.app.IntentService;
import android.content.Intent;
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
        //Registration complete intent initially null
        Intent registrationComplete = null;

        //Register token is also null
        //we will get the token on successfull registration
       // String token = null;
        try {
            //Creating an instanceid

            //InstanceID instanceID = InstanceID.getInstance(getApplicationContext());

            //Getting the token from the instance id
           // token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
          //  InstanceID instanceID = InstanceID.getInstance(this);
            //String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
              //      GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            String token = "22";
            //Displaying the token in the log so that we can copy it to send push notification
            //You can also extend the app by storing the token in to your server
            Log.w("GCMRegIntentService", "token:" + token);
            //Toast.makeText(getApplicationContext(),"Ok ",Toast.LENGTH_LONG).show();
            //on registration complete creating intent with success
            registrationComplete = new Intent(REGISTRATION_SUCCESS);

            //Putting the token to the intent
            registrationComplete.putExtra("token", token);
        }  catch (Exception e) {
            //If any error occurred
           // Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
            Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }

        //Sending the broadcast that registration is completed
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }
}