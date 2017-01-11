package com.example.newpc.qrcode;

import org.json.JSONException;

/**
 * Created by jackson on 2016/10/30.
 */

public interface Scanner {
    void success(String s) throws JSONException;
    void fail(String s);
}
