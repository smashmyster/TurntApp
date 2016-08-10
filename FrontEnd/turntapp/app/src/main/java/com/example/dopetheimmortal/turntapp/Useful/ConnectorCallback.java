package za.co.TurntApp.www.Useful;

import android.content.Context;

import org.json.JSONException;

/**
 * Created by jackson on 2016/03/26.
 */
public interface ConnectorCallback {
    public void success(String info) throws JSONException;
    public void fail(String info);
    public void display(String info,Context cont);
}
