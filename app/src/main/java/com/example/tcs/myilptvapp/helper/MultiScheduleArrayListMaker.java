package com.example.tcs.myilptvapp.helper;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tcs.myilptvapp.R;
import com.example.tcs.myilptvapp.data.Contacts;
import com.example.tcs.myilptvapp.data.Schedule;
import com.example.tcs.myilptvapp.fragment.MultiScheduleFragment;
import com.example.tcs.myilptvapp.utils.Constants;
import com.example.tcs.myilptvapp.utils.ParseJSONBatches;
import com.example.tcs.myilptvapp.utils.ParseJSONSchedule;
import com.example.tcs.myilptvapp.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1115394 on 11/9/2016.
 */
public class MultiScheduleArrayListMaker {

    private static final String TAG = MultiScheduleArrayListMaker.class.getSimpleName();

    private static final String FLAG_A = "A";
    private static final String FLAG_B = "B";
    private static final String FLAG_C = "C";
    private static final String FLAG_BATCH = "BATCH";

    private int COUNT_SLOT_1 = 0;
    private int COUNT_SLOT_2 = 0;
    private int COUNT_SLOT_3 = 0;

    private ArrayList<Schedule> batches = new ArrayList<>();
    private ArrayList<Schedule> scheduleSlot1 = new ArrayList<>();
    private ArrayList<Schedule> scheduleSlot2 = new ArrayList<>();
    private ArrayList<Schedule> scheduleSlot3 = new ArrayList<>();

    private ArrayList<Schedule> finalScheduleList = new ArrayList<>();

    private Context mContext;
    private String mLocation;
    private String mDate;
    private String mSlot1;
    private String mSlot2;
    private String mSlot3;

    private int SPAN_COUNT = 4;

    public interface AsyncResponse{
        void onProcessFinish(ArrayList<Schedule> output);
    }

    public AsyncResponse delegate = null;

    public MultiScheduleArrayListMaker(Context context, String location, String date, String slot1, String slot2, String slot3, AsyncResponse delegate){
        this.mContext = context;
        this.mLocation = location;
        this.mDate = date;
        this.mSlot1 = slot1;
        this.mSlot2 = slot2;
        this.mSlot3 = slot3;
        this.delegate = delegate;
    }

    public void generate(){

        String urlBatch = generateBatchUrl(mLocation, mDate);
//        String urlSlot1 = generateUrl(mLocation, mDate, mSlot1);
//        String urlSlot2 = generateUrl(mLocation, mDate, mSlot2);
//        String urlSlot3 = generateUrl(mLocation, mDate, mSlot3);

        sendRequest(urlBatch, FLAG_BATCH);
//        sendRequest(urlSlot1, FLAG_A);
//        sendRequest(urlSlot2, FLAG_B);
//        sendRequest(urlSlot3, FLAG_C);

//        for (int i=0; i < batches.size()*SPAN_COUNT; i++){
//            Schedule s;
//
//            int row = Util.getRowNumber(i, SPAN_COUNT);
//            Schedule batch = batches.get(row);
//
//            if (i%SPAN_COUNT == 0){
//                //BATCH NAME ELEMENT
//                finalScheduleList.add(batch);
//            }else if ((i-1)%SPAN_COUNT == 0){
//                //First schedule position
//                // SLOT 1
//                s = scheduleSlot1.get(i - COUNT_SLOT_1);
//
//                if (batch.getBatch().equalsIgnoreCase(s.getBatch())){
//                    finalScheduleList.add(s);
//                }else {
//                    COUNT_SLOT_1++;
//                    finalScheduleList.add(new Schedule("No Schedule",null,null,null,null,null,"failed"));
//                }
//            }else if ((i-2)%SPAN_COUNT == 0){
//                //second schedule position
//                // SLOT 2
//                s = scheduleSlot2.get(i - COUNT_SLOT_2);
//
//                if (batch.getBatch().equalsIgnoreCase(s.getBatch())){
//                    finalScheduleList.add(s);
//                }else {
//                    COUNT_SLOT_2++;
//                    finalScheduleList.add(new Schedule("No Schedule",null,null,null,null,null,"failed"));
//                }
//            }else if ((i-3)%SPAN_COUNT == 0){
//                //third schedule position
//                s = scheduleSlot3.get(i - COUNT_SLOT_1);
//
//                if (batch.getBatch().equalsIgnoreCase(s.getBatch())){
//                    finalScheduleList.add(s);
//                }else {
//                    COUNT_SLOT_3++;
//                    finalScheduleList.add(new Schedule("No Schedule",null,null,null,null,null,"failed"));
//                }
//            }
//        }


        return ;
    }

    private String generateUrl(String location, String date, String slot){
        Map<String, String> params = new HashMap<>();
        params.put(Constants.NETWORK_PARAMS.SCHEDULE_LOCATION.LOCATION, location);
        params.put(Constants.NETWORK_PARAMS.SCHEDULE_LOCATION.DATE, date);
        params.put(Constants.NETWORK_PARAMS.SCHEDULE_LOCATION.SLOT, slot);

        Log.i(TAG, "Date: " + date);
        String url = new StringBuilder(
                Constants.NETWORK_PARAMS.SCHEDULE_LOCATION.URL).append(
                Util.getUrlEncodedString(params)).toString();

        Log.i(TAG, "Schedule URL: " + url);

        return url;
    }

    private String generateBatchUrl(String location, String date){
        Map<String, String> params = new HashMap<>();
        params.put(Constants.NETWORK_PARAMS.BATCHES.LOCATION, location);
        params.put(Constants.NETWORK_PARAMS.BATCHES.DATE, date);

        Log.i(TAG, "Date: " + date);
        String url = new StringBuilder(
                Constants.NETWORK_PARAMS.BATCHES.URL).append(
                Util.getUrlEncodedString(params)).toString();

        Log.i(TAG, "Schedule URL: " + url);

        return url;
    }

    private void sendRequest(String jsonUrl, final String flag){

        StringRequest stringRequest = new StringRequest(jsonUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            showJSON(response, flag);
                        if (flag.equalsIgnoreCase(FLAG_BATCH)){
                            String urlSlot1 = generateUrl(mLocation, mDate, mSlot1);
                            sendRequest(urlSlot1, FLAG_A);
                        }else if (flag.equalsIgnoreCase(FLAG_A)){
                            String urlSlot1 = generateUrl(mLocation, mDate, mSlot2);
                            sendRequest(urlSlot1, FLAG_B);
                        }else if (flag.equalsIgnoreCase(FLAG_B)){
                            String urlSlot1 = generateUrl(mLocation, mDate, mSlot3);
                            sendRequest(urlSlot1, FLAG_C);
                        }else if (flag.equalsIgnoreCase(FLAG_C)){
                            Log.i(TAG, "Batches Size: " + batches.size());
                            Log.i(TAG, "Slot 1 Size: " + scheduleSlot1.size());
                            Log.i(TAG, "Slot 2 Size: " + scheduleSlot2.size());
                            Log.i(TAG, "Slot 3 Size: " + scheduleSlot3.size());

                            for (int i=0; i < batches.size()*SPAN_COUNT; i++){
                                Schedule s;

                                int row = Util.getRowNumber(i, SPAN_COUNT);
                                Log.i(TAG, "ROW: " + row);
                                Schedule batch = batches.get(row);

                                if (i%SPAN_COUNT == 0){
                                    //BATCH NAME ELEMENT
                                    finalScheduleList.add(batch);
                                }else if ((i-1)%SPAN_COUNT == 0){
                                    //First schedule position
                                    // SLOT 1
                                    if (row-COUNT_SLOT_1 != scheduleSlot1.size()) {
                                        s = scheduleSlot1.get(row - COUNT_SLOT_1);

                                        if (batch.getBatch().equalsIgnoreCase(s.getBatch())) {
                                            finalScheduleList.add(s);
                                        } else {
                                            COUNT_SLOT_1++;
                                            finalScheduleList.add(new Schedule("No Schedule", null, null, null, null, null, "failed"));
                                        }
                                    }else {
                                        COUNT_SLOT_1++;
                                        finalScheduleList.add(new Schedule("No Schedule", null, null, null, null, null, "failed"));
                                    }
                                }else if ((i-2)%SPAN_COUNT == 0){
                                    //second schedule position
                                    // SLOT 2
                                    if (row-COUNT_SLOT_2 != scheduleSlot2.size()) {
                                        s = scheduleSlot2.get(row - COUNT_SLOT_2);

                                        if (batch.getBatch().equalsIgnoreCase(s.getBatch())) {
                                            finalScheduleList.add(s);
                                        } else {
                                            COUNT_SLOT_2++;
                                            finalScheduleList.add(new Schedule("No Schedule", null, null, null, null, null, "failed"));
                                        }
                                    }else {
                                        COUNT_SLOT_2++;
                                        finalScheduleList.add(new Schedule("No Schedule", null, null, null, null, null, "failed"));
                                    }
                                }else if ((i-3)%SPAN_COUNT == 0){
                                    //third schedule position
                                    if (row-COUNT_SLOT_3 != scheduleSlot3.size()) {
                                        s = scheduleSlot3.get(row - COUNT_SLOT_3);

                                        if (batch.getBatch().equalsIgnoreCase(s.getBatch())) {
                                            finalScheduleList.add(s);
                                        } else {
                                            COUNT_SLOT_3++;
                                            finalScheduleList.add(new Schedule("No Schedule", null, null, null, null, null, "failed"));
                                        }
                                    }else {
                                        COUNT_SLOT_3++;
                                        finalScheduleList.add(new Schedule("No Schedule", null, null, null, null, null, "failed"));
                                    }
                                }
                            }

                            delegate.onProcessFinish(finalScheduleList);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: " + error.getMessage() + " | " + error.getStackTrace() + " | " + error.getCause());
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json, String flag) {
        ArrayList<Schedule> tempList;

        if (flag.equalsIgnoreCase(FLAG_BATCH)){
            ParseJSONBatches pj = new ParseJSONBatches(json);
            tempList = pj.parseJSON();
        }else {
            ParseJSONSchedule pj = new ParseJSONSchedule(json);
            tempList = pj.parseJSON();
        }

        if (tempList.size() != 0) {
            switch (flag) {
                case FLAG_A:
                    scheduleSlot1.clear();
                    scheduleSlot1.addAll(tempList);
                    break;

                case FLAG_B:
                    scheduleSlot2.clear();
                    scheduleSlot2.addAll(tempList);
                    break;

                case FLAG_C:
                    scheduleSlot3.clear();
                    scheduleSlot3.addAll(tempList);
                    break;

                case FLAG_BATCH:
                    batches.clear();
                    batches.addAll(tempList);
            }

        } else {
            Toast toast = Toast.makeText(mContext, "No data in arrayList | Flag: " + flag, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
