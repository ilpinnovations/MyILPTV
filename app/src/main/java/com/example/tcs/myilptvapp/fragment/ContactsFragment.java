package com.example.tcs.myilptvapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.tcs.myilptvapp.adapter.ContactsAdapter;
import com.example.tcs.myilptvapp.data.Contacts;
import com.example.tcs.myilptvapp.utils.ParseJSONContacts;
import com.example.tcs.myilptvapp.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ContactsFragment extends Fragment {
    private final static String TAG = ContactsFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private ArrayList<Contacts> emergencyList;
    private Spinner dropDown;
    private String location;

//    private EditText editText;

    public static final int BANNER = 0;
    public static int REC_VIEW_CUR_POS = 1;
    public static final int REC_VIEW_MAX_POS = 10;
    public static final int REC_VIEW_MIN_POS = 0;
    private static int currFocus = 0;

    public static final String LOCATION_BUNDLE_TAG = "LOCATION_BUNDLE";


    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

        Bundle args = getArguments();

        if (args != null){
            // successfully received the arguments/location
            location = args.getString(LOCATION_BUNDLE_TAG);
        }else {
            Log.d(TAG, "Received null value in bundle arguments");
            location = "Trivandrum";
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.emergency_recycler_view);

        emergencyList = new ArrayList<>();
        adapter = new ContactsAdapter(getContext(), emergencyList);

        recyclerView.setLayoutManager(new CustomLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        String url = generateUrl(location);
        sendRequest(url);

//        prepareEmergency();
        return rootView;
    }

    public void prepareEmergency(){
        for(int i=0, j=7; i<10; i++,j+=2){
            emergencyList.add(new Contacts("Helpline " + (i+1), "929347939" + j));
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
        ArrayList<Contacts> tempList = pj.parseJSON();

        emergencyList.clear();
        for (Contacts e: tempList){
            emergencyList.add(e);
        }

//        Log.i(TAG, "Sample schedule: " + scheduleList.get(3).getCourse());
        adapter.notifyDataSetChanged();
    }
}
