package com.example.dopetheimmortal.turntapp.connector;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.dopetheimmortal.turntapp.Useful.ConnectorCallback;

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
public class Connector extends AsyncTask<String, String, String> {
    String link;
    ConnectorCallback callback;
    HashMap<String,String> data;
    Context cont;
    String prg_message,prg_title;
    boolean dismiss,show;
    ProgressDialog dialog;
    public Connector(String link, ConnectorCallback callback, Context cont, HashMap<String,String> data, String prg_title, String prg_message, boolean prg_dissmis, boolean show){
        this.data=data;
        this.callback=callback;
        this.link=link;
        this.cont=cont;
        this.prg_message=prg_message;
        this.prg_title=prg_title;
        this.dismiss=prg_dissmis;
        this.show=show;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog=new ProgressDialog(cont);
        dialog.setMessage(prg_message);
        dialog.setTitle(prg_title);
        dialog.setCancelable(dismiss);
        if(show){
            dialog.show();
        }
    }
    public String GetText(HashMap<String,String> info)  throws UnsupportedEncodingException {
        // Create data variable for sent values to server
        String data="";
        boolean first=true;
        for(String key:info.keySet()){
            if(first){
                first=false;
            }else{
                data+="&";
            }
            data+= URLEncoder.encode(key, "UTF-8")
                    + "=" + URLEncoder.encode(info.get(key), "UTF-8");

        }
        return data;
    }
    @Override
    protected String doInBackground(String... params) {
        String response="";
        try {
            String send=GetText(data);
            URL url = new URL(link);
            // Defined URL  where to send data
            // Send POST data request
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(send);
            wr.flush();

            // Get the server response

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }


            response = sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            dialog.dismiss();
        } catch (IOException e) {
//            e.printStackTrace();
            dialog.dismiss();
            return "Something is wrong with your connection please reconnect";
        }
        return response;

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        System.out.println(result);
        dialog.dismiss();
        try {
            JSONObject obj=new JSONObject(result);
            if(obj.getInt("success")==1){
                callback.success(result);
            }else{
                callback.fail(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callback.display("Something is wrong with your connection please reconnect",cont);
        }


    }

}