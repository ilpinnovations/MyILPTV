package com.example.tcs.myilptvapp.utils;

import android.util.Log;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by 1007546 on 27-10-2016.
 */
public class Util {
    private static final String TAG = Util.class.getSimpleName();

    public static String getUrlEncodedString(Map<String, String> parameters) {
        StringBuilder strb = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                strb.append(
                        URLEncoder.encode(entry.getKey(), Constants.CHARSET))
                        .append(Constants.EQUALS)
                        .append(URLEncoder.encode(entry.getValue(),
                                Constants.CHARSET)).append(Constants.AND);
            }
            int length = strb.length();
            if (length > 0) {
                // remove extra & at end of string
                strb.deleteCharAt(length - 1);
            }
        } catch (Exception ex) {
            Log.d(TAG, "invalid parameters " + ex.getLocalizedMessage());
            strb.setLength(0);
        }
        return strb.toString();
    }
}
