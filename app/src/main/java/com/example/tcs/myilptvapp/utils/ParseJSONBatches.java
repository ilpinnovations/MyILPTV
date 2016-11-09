package com.example.tcs.myilptvapp.utils;

import android.util.Log;

import com.example.tcs.myilptvapp.data.Contacts;
import com.example.tcs.myilptvapp.data.Schedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by 1115394 on 11/9/2016.
 */
public class ParseJSONBatches {

    private static final String TAG = ParseJSONBatches.class.getSimpleName();

    private ArrayList<Schedule> batches;
    private static final String JSON_ARRAY = "Android";
    private static final String KEY_BATCH = "batch";

    private JSONArray baseArray = null;

    private String json;

    public ParseJSONBatches(String json){
        this.json = json;
        batches = new ArrayList<>();
    }

    public ArrayList<Schedule> parseJSON(){
        JSONObject jsonObject=null;
        try {
            Log.d(TAG, "fetch contacts response ->" + json);
            jsonObject = new JSONObject(json);
            baseArray = jsonObject.getJSONArray(JSON_ARRAY);

            for(int i = 0; i< baseArray.length(); i++){
                JSONObject jo = baseArray.getJSONObject(i);

                String batch = jo.getString(KEY_BATCH);
                Schedule s = new Schedule(null, null, null, null, null, batch, null);

                batches.add(s);
            }

            return batches;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
