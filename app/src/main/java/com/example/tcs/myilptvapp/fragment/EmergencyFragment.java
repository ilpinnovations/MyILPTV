package com.example.tcs.myilptvapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tcs.myilptvapp.utils.Constants;
import com.example.tcs.myilptvapp.utils.CustomLayoutManager;
import com.example.tcs.myilptvapp.R;
import com.example.tcs.myilptvapp.adapter.EmergencyAdapter;
import com.example.tcs.myilptvapp.data.Emergency;
import com.example.tcs.myilptvapp.utils.ParseJSONContacts;
import com.example.tcs.myilptvapp.utils.ParseJSONSchedule;
import com.example.tcs.myilptvapp.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EmergencyFragment extends Fragment {
    private final static String TAG = EmergencyFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private EmergencyAdapter adapter;
    private ArrayList<Emergency> emergencyList;
    private Spinner dropDown;

//    private EditText editText;

    public static final int BANNER = 0;
    public static int REC_VIEW_CUR_POS = 1;
    public static final int REC_VIEW_MAX_POS = 10;
    public static final int REC_VIEW_MIN_POS = 0;
    private static int currFocus = 0;

    private static final String TRIVANDRUM = "Trivandrum";
    private static final String CHENNAI = "Chennai";
    private static final String GUWAHATI = "Guwahati";
    private static final String AHMEDABAD = "Ahmedabad";
    private static final String HYDERABAD = "Hyderabad";


    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_emergency, container, false);

        final ArrayList<String> array = new ArrayList<String>();
        array.add(TRIVANDRUM);
        array.add(CHENNAI);
        array.add(GUWAHATI);
        array.add(AHMEDABAD);
        array.add(HYDERABAD);

        ArrayAdapter<String> mAdapter;
        dropDown= (Spinner) rootView.findViewById(R.id.contacts_spinner);
        mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, array);
        dropDown.setAdapter(mAdapter);

//        dropDown.requestFocus();

        dropDown.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    dropDown.setBackgroundColor(getActivity().getResources().getColor(R.color.highlighting_color));
                }
                else {
                    dropDown.setBackgroundColor(getActivity().getResources().getColor(R.color.spinner_color));
                }
            }
        });

        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                String url;
                switch (selectedItem){
                    case TRIVANDRUM:
                        url = generateUrl(TRIVANDRUM);
                        break;

                    case CHENNAI:
                        url = generateUrl(CHENNAI);
                        break;

                    case GUWAHATI:
                        url = generateUrl(GUWAHATI);
                        break;

                    case AHMEDABAD:
                        url = generateUrl(AHMEDABAD);
                        break;

                    case HYDERABAD:
                        url = generateUrl(HYDERABAD);
                        break;

                    default:
                        url = generateUrl(TRIVANDRUM);
                        break;

                }

                sendRequest(url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.emergency_recycler_view);

        emergencyList = new ArrayList<>();
        adapter = new EmergencyAdapter(getContext(), emergencyList);

        recyclerView.setLayoutManager(new CustomLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

//        recyclerView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.d(TAG, "POS: " + REC_VIEW_CUR_POS);
//                int action = keyCode;
////                Log.d(TAG, "onKey(); KEY_ACTION: " + action + " ACTION_UP: " + KeyEvent.KEYCODE_DPAD_UP + " ACTION_DOWN: " + KeyEvent.KEYCODE_DPAD_DOWN);
//
//                if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN){
//                    if(REC_VIEW_CUR_POS<REC_VIEW_MAX_POS && REC_VIEW_CUR_POS>=REC_VIEW_MIN_POS) {
//                        REC_VIEW_CUR_POS++;
//                        recyclerView.smoothScrollToPosition(REC_VIEW_CUR_POS);
//                    }else {
//                        //ToDo
//                        // implement focuschange to bottom fragment
//                    }
//
//                }
//
//                if (action == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN){
//                    if(REC_VIEW_CUR_POS>REC_VIEW_MIN_POS && REC_VIEW_CUR_POS<=REC_VIEW_MAX_POS) {
//                        REC_VIEW_CUR_POS--;
//                        recyclerView.smoothScrollToPosition(REC_VIEW_CUR_POS);
//                    }else {
////                        editText.requestFocus();
//                    }
//                }
//
//                if (action == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN){
//                    //Todo
//                    //implement focus change to schedule fragment
//                    recyclerView.clearFocus();
//                }
//
//                return true;
//            }
//        });

        String url = generateUrl(TRIVANDRUM);
        sendRequest(url);

//        prepareEmergency();
        return rootView;
    }

    public void prepareEmergency(){
        for(int i=0, j=7; i<10; i++,j+=2){
            emergencyList.add(new Emergency("Helpline " + (i+1), "929347939" + j));
        }

        adapter.notifyDataSetChanged();
        Log.d(TAG, "prepareEmergency");
    }

    private String generateUrl(String location){
        Map<String, String> params = new HashMap<>();
        params.put(Constants.NETWORK_PARAMS.CONTACT.ILP,
                location);

        String url = Constants.URL_CONTACTS + Util.getUrlEncodedString(params);
        Log.i(TAG, "Contacts URL: " + url);

        return url;
    }

    private void sendRequest(String jsonUrl){

        StringRequest stringRequest = new StringRequest(jsonUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: " + error.getMessage() + " | " + error.getStackTrace() + " | " + error.getCause());
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json){
        ParseJSONContacts pj = new ParseJSONContacts(json);
        ArrayList<Emergency> tempList = pj.parseJSON();

        emergencyList.clear();
        for (Emergency e: tempList){
            emergencyList.add(e);
        }

//        Log.i(TAG, "Sample schedule: " + scheduleList.get(3).getCourse());
        adapter.notifyDataSetChanged();
    }
}
