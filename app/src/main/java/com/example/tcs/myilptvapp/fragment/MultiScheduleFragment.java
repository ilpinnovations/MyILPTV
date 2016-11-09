package com.example.tcs.myilptvapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tcs.myilptvapp.R;
import com.example.tcs.myilptvapp.activity.MainActivity;
import com.example.tcs.myilptvapp.adapter.MultiScheduleAdapter;
import com.example.tcs.myilptvapp.adapter.MultiScheduleAdapterTemp;
import com.example.tcs.myilptvapp.data.Schedule;
import com.example.tcs.myilptvapp.helper.MultiScheduleArrayListMaker;
import com.example.tcs.myilptvapp.utils.ConnectionDetector;
import com.example.tcs.myilptvapp.utils.Constants;
import com.example.tcs.myilptvapp.utils.ParseJSONBatches;
import com.example.tcs.myilptvapp.utils.ParseJSONSchedule;
import com.example.tcs.myilptvapp.utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MultiScheduleFragment extends Fragment {

    private static final String TAG = MultiScheduleFragment.class.getSimpleName();
    private static final String FLAG_A = "A";
    private static final String FLAG_B = "B";
    private static final String FLAG_C = "C";
    private static final String FLAG_BATCH = "BATCH";

    private MultiScheduleAdapterTemp adapter;
    private RecyclerView recyclerView;
    private TextView slot1, slot2, slot3;
    private TextView dateView;

    private ArrayList<String> batches = new ArrayList<>();
    private ArrayList<Schedule> scheduleSlot1 = new ArrayList<>();
    private ArrayList<Schedule> scheduleSlot2 = new ArrayList<>();
    private ArrayList<Schedule> scheduleSlot3 = new ArrayList<>();
    private ArrayList<Schedule> finalScheduleList = new ArrayList<>();

    public static final String SLOT_A = "A";
    public static final String SLOT_B = "B";
    public static final String SLOT_C = "C";
    public static final String SLOT_D = "D";
    public static final String SLOT_E = "E";
    public static final String SLOT_F = "F";
    public static final String SLOT_D_PLUS = "D+";
    public static final String SLOT_MINUS_A = "-A";
    public static final String SLOT_A_MINUS = "A-";

    public static final String PLANET_TRIVANDRUM = "TVM_PP_CLC";
    public static final String PLANET_CHENNAI = "CHN_ILP";
    public static final String PLANET_HYDERABAD = "HYD_QCITY";
    public static final String PLANET_GUWAHATI = "GHT_ILP_IIT_GUWAHATI";
    public static final String PLANET_AHMEDABAD = "AHMEDABAD";

    private String location;
    private String strSlot1;
    private String strSlot2;
    private String strSlot3;
    private String dateForUrl;
    private String dateForView;

    private int REC_VIEW_MAX_POS = 10;
    private int SPAN_COUNT = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_multi_schedule, container, false);

        slot1 = (TextView) rootView.findViewById(R.id.schedule_slot1);
        slot2 = (TextView) rootView.findViewById(R.id.schedule_slot2);
        slot3 = (TextView) rootView.findViewById(R.id.schedule_slot3);
        dateView = (TextView) rootView.findViewById(R.id.multi_schedule_date);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.multi_schedule_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        recyclerView.setLayoutManager(gridLayoutManager);

//        adapter = new MultiScheduleAdapter(getActivity(), batches, scheduleSlot1, scheduleSlot2, scheduleSlot3);
        adapter = new MultiScheduleAdapterTemp(getActivity(), finalScheduleList);
        recyclerView.setAdapter(adapter);

//        prepareSchedule();

        dateForView = new SimpleDateFormat("EEE, dd MMM, yyyy").format(new Date());
        dateView.setText(dateForView);

        Bundle args = getArguments();


        if (args != null) {
            String temp = args.getString(ContactsFragment.LOCATION_BUNDLE_TAG);

            if (temp != null) {
                switch (temp) {
                    case MainActivity.TRIVANDRUM:
                        location = PLANET_TRIVANDRUM;
                        break;

                    case MainActivity.HYDERABAD:
                        location = PLANET_HYDERABAD;
                        break;

                    case MainActivity.CHENNAI:
                        location = PLANET_CHENNAI;
                        break;

                    case MainActivity.GUWAHATI:
                        location = PLANET_GUWAHATI;
                        break;

                    case MainActivity.AHMEDABAD:
                        location = PLANET_AHMEDABAD;
                        break;

                    default:
                        location = PLANET_TRIVANDRUM;
                        break;
                }
            }

            Log.i(TAG, "Received bundled arguments | location: " + location);
        } else {
            Log.e(TAG, "Received null arguments");
            location = PLANET_TRIVANDRUM;
        }

        dateForUrl = Constants.paramsDateFormat.format(new Date());

        //ToDo timer
        // implement timer or scheduler over here to refresh the slot values on time basis
        strSlot1 = SLOT_A;
        strSlot2 = SLOT_B;
        strSlot3 = SLOT_C;

//        String urlBatch = generateBatchUrl(location, dateForUrl);
//        String urlSlot1 = generateUrl(location, dateForUrl, strSlot1);
//        String urlSlot2 = generateUrl(location, dateForUrl, strSlot2);
//        String urlSlot3 = generateUrl(location, dateForUrl, strSlot3);
//
//        sendRequest(urlBatch, FLAG_BATCH);
//        sendRequest(urlSlot1, FLAG_A);
//        sendRequest(urlSlot2, FLAG_B);
//        sendRequest(urlSlot3, FLAG_C);

        MultiScheduleArrayListMaker maker = new MultiScheduleArrayListMaker(getActivity(), location, dateForUrl, strSlot1, strSlot2, strSlot3, new MultiScheduleArrayListMaker.AsyncResponse() {
            @Override
            public void onProcessFinish(ArrayList<Schedule> output) {
                Log.i(TAG, "onProcessFinish | output Size: " + output.size());
                finalScheduleList.clear();
                finalScheduleList.addAll(output);
                adapter.notifyDataSetChanged();
            }
        });
        maker.generate();

        Log.i(TAG, "finalScheduleList Size: " + finalScheduleList.size());
        int i=1;
        for (Schedule s: finalScheduleList){
            Log.i(TAG, "FinalScheduleList " + i + ": " + s.getBatch());
            i++;
        }

        adapter.notifyDataSetChanged();

        return rootView;
    }

    public void prepareSchedule() {
        for (int i = 0, j = 7; i < REC_VIEW_MAX_POS; i++, j += 2) {
            batches.add("TMFAO15" + j);
        }

        for (int i = 0, j = 7; i < REC_VIEW_MAX_POS; i++, j += 2) {
            scheduleSlot1.add(new Schedule("Lecture:adlkhfaslkjdfk " + (i + 1), "Reshmi", "Slot: " + j, "Room A" + j + " " + j + 40, "2016-11-07", "TMFAO15" + j, null));
        }
        for (int i = 0, j = 7; i < REC_VIEW_MAX_POS; i++, j += 2) {
            scheduleSlot2.add(new Schedule("Lecture:adlkhfaslkjdfk " + (i + 1), "Reshmi", "Slot: " + j, "Room A" + j + " " + j + 40, "2016-11-07", "TMFAO15" + j, null));
        }
        for (int i = 0, j = 7; i < REC_VIEW_MAX_POS; i++, j += 2) {
            scheduleSlot3.add(new Schedule("Lecture:adlkhfaslkjdfk " + (i + 1), "Reshmi", "Slot: " + j, "Room A" + j + " " + j + 40, "2016-11-07", "TMFAO15" + j, null));
        }

        adapter.notifyDataSetChanged();
        Log.d(TAG, "prepareSchedule");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        ((MainActivity)getActivity()).clearBackStackInclusive("tag"); // tag (addToBackStack tag) should be the same which was used while transacting the F2 fragment
    }

    private String generateUrl(String location, String date, String slot) {
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

    private String generateBatchUrl(String location, String date) {
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
}

//    private void onSubmit(){
//        //                EditText ed = (EditText) v.findViewById(R.id.schedule_batch_et);
//        if (ConnectionDetector.isConnected(getActivity())){
//            String batchName = editText.getText().toString().toUpperCase();
////                String baseUrl = "http://theinspirer.in/ilpscheduleapp/schedulelist_json.php?date" + currentDate + "batch=" + batchName;
//            if (!batchName.equalsIgnoreCase("")){
//                editText.setText(batchName);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString(BATCH_KEY, batchName);
//                editor.apply();
//
//                sendRequest(generateUrl(batchName, currentDate));
//
//                Toast toast = Toast.makeText(getActivity(), batchName, Toast.LENGTH_SHORT);
//                toast.show();
//            }else {
//                scheduleList.clear();
//                adapter.notifyDataSetChanged();
//                Toast toast = Toast.makeText(getActivity(), "Please enter a Batch Name!", Toast.LENGTH_SHORT);
//                toast.show();
//            }
//        }else {
//            Toast toast = Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }

//    private void sendRequest(String jsonUrl, final String flag){
//
//        StringRequest stringRequest = new StringRequest(jsonUrl,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if (flag.equalsIgnoreCase(FLAG_BATCH))
//                            showJSONBatch(response);
//                        else
//                            showJSON(response, flag);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.i(TAG, "onErrorResponse: " + error.getMessage() + " | " + error.getStackTrace() + " | " + error.getCause());
//                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
//                    }
//                });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.add(stringRequest);
//    }
//
//    private void showJSON(String json, String flag) {
//        ParseJSONSchedule pj = new ParseJSONSchedule(json);
//        ArrayList<Schedule> tempList = pj.parseJSON();
//
//        if (tempList.size() != 0) {
//            switch (flag) {
//                case FLAG_A:
//                    scheduleSlot1.clear();
//                    for (Schedule s : tempList) {
//                        scheduleSlot1.add(s);
//                    }
//                    break;
//
//                case FLAG_B:
//                    scheduleSlot2.clear();
//                    for (Schedule s : tempList) {
//                        scheduleSlot2.add(s);
//                    }
//                    break;
//
//                case FLAG_C:
//                    scheduleSlot3.clear();
//                    for (Schedule s : tempList) {
//                        scheduleSlot3.add(s);
//                    }
//                    break;
//            }
//
//        } else {
//            batches.clear();
//            scheduleSlot1.clear();
//            scheduleSlot2.clear();
//            scheduleSlot3.clear();
//            adapter.notifyDataSetChanged();
//            Toast toast = Toast.makeText(getActivity(), "Some error occurred!", Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }
//
//    private void showJSONBatch(String json) {
//        ParseJSONBatches pj = new ParseJSONBatches(json);
//        ArrayList<String> tempList = pj.parseJSON();
//
//        if (tempList.size() != 0) {
//            batches.clear();
//            for (String s : tempList) {
//                batches.add(s);
//            }
//
//        } else {
//            batches.clear();
//            scheduleSlot1.clear();
//            scheduleSlot2.clear();
//            scheduleSlot3.clear();
//            adapter.notifyDataSetChanged();
//            Toast toast = Toast.makeText(getActivity(), "Some error occurred!", Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }
//}
