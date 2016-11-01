package com.example.tcs.myilptvapp.utils;

import android.util.Log;

import com.example.tcs.myilptvapp.data.Emergency;
import com.example.tcs.myilptvapp.data.Schedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by 1007546 on 28-10-2016.
 */
public class ParseJSONContacts {

    private static final String TAG = ParseJSONContacts.class.getSimpleName();

    private ArrayList<Emergency> contacts;
    private JSONArray baseArray = null;

    private String json;

    public ParseJSONContacts(String json){
        this.json = json;
        contacts = new ArrayList<>();
    }

    public ArrayList<Emergency> parseJSON(){
        JSONObject jsonObject=null;
        try {
            Log.d(TAG, "fetch contacts response ->" + json);
            JSONArray jarr = new JSONArray(json);
            JSONObject jobj;
            Emergency contact;
            for (int i = 0; i < jarr.length(); i++) {
                jobj = jarr.getJSONObject(i);
                Iterator<String> it = jobj.keys();
                while (it.hasNext()) {
                    contact = new Emergency();
                    String title = it.next();
                    contact.setName(title);
                    contact.setNumber(jobj.getString(title));
                    contacts.add(contact);
                }
            }
            return contacts;
        } catch (JSONException ex) {
            Log.d(TAG, "error in json data" + ex.getLocalizedMessage());
        }

        return null;
    }
}
