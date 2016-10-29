package com.example.newpc.qrcode;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;


/**
 * Created by jackson on 2016/05/25.
 */
public class Remover extends AsyncTask<String, String, String> {
    TextView t;
    public Remover(TextView t) {
        this.t=t;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Thread.sleep(Long.parseLong("5000"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        t.setText("");
    }

}