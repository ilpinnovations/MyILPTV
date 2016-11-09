package com.example.tcs.myilptvapp.utils;

import android.util.Log;

import com.example.tcs.myilptvapp.data.Schedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shubham Sahu on 9/22/2015.
 */
public class ParseJSONSchedule {

    private static final String TAG = ParseJSONSchedule.class.getSimpleName();

    private static String[] ids;
    private static String[] names;
    private static String[] emails;

    private ArrayList<Schedule> schedules;
    private static final String JSON_ARRAY = "Android";
    private static final String KEY_DATE = "date1";
    private static final String KEY_BATCH = "batch";
    private static final String KEY_SLOT = "slot";
    private static final String KEY_COURSE = "course";
    private static final String KEY_FACULTY = "faculty";
    private static final String KEY_ROOM = "room";
    private static final String KEY_RESULT = "result";

    private JSONArray baseArray = null;

    private String json;

    public ParseJSONSchedule(String json){
        this.json = json;
        schedules = new ArrayList<>();
    }

    public ArrayList<Schedule> parseJSON(){
        JSONObject jsonObject=null;
        try {
            Log.d(TAG, "fetch contacts response ->" + json);
            jsonObject = new JSONObject(json);
            baseArray = jsonObject.getJSONArray(JSON_ARRAY);

            for(int i = 0; i< baseArray.length(); i++){
                JSONObject jo = baseArray.getJSONObject(i);

                String course = jo.getString(KEY_COURSE);
                String faculty = jo.getString(KEY_FACULTY);
                String slot = jo.getString(KEY_SLOT);
                String room = jo.getString(KEY_ROOM);
                String date = jo.getString(KEY_DATE);
                String batch = jo.getString(KEY_BATCH);
                String result = jo.getString(KEY_RESULT);

                Schedule schedule = new Schedule(course, faculty, slot, room, date, batch, result);

                schedules.add(schedule);
            }

            return schedules;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}