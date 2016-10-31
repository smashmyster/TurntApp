package com.example.newpc.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ReaderActivity extends AppCompatActivity implements Scanner {
    private Button scan_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
                TextView textView=(TextView)findViewById(R.id.person_info);
                textView.setText("");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                String s=result.getContents();
                HashMap<String,String>daata=new HashMap<>();
                daata.put("type","check_in");
                daata.put("data",s);
                String link=this.getString(R.string.link);
                new Connector(link,this,this,daata,"Confirming","Please wait",false,true).execute();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void success(String s) throws JSONException {
        TextView textView=(TextView)findViewById(R.id.person_info);
        JSONObject object=new JSONObject(s);
        JSONObject event=object.getJSONObject("event");
        textView.setText("Welcome to the "+event.getString("name")+" party "+object.getString("name"));
        new Remover(textView).execute();
    }

    @Override
    public void fail(String s) {

    }
}
