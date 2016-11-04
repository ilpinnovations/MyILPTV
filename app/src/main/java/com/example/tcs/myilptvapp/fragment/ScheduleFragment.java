package com.example.tcs.myilptvapp.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tcs.myilptvapp.utils.ConnectionDetector;
import com.example.tcs.myilptvapp.utils.Constants;
import com.example.tcs.myilptvapp.utils.CustomLayoutManager;
import com.example.tcs.myilptvapp.R;
import com.example.tcs.myilptvapp.adapter.ScheduleAdapter;
import com.example.tcs.myilptvapp.data.Schedule;
import com.example.tcs.myilptvapp.utils.ParseJSONSchedule;
import com.example.tcs.myilptvapp.utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.sql.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ScheduleFragment extends Fragment {

    private final static String TAG = ScheduleFragment.class.getSimpleName();

    private ArrayList<Schedule> scheduleList;
    private ScheduleAdapter adapter;
    private RecyclerView recyclerView;
    private EditText editText;
    private ImageView submitButton, calendarButton;
    private TextView dateView;

    SharedPreferences sharedPreferences;
    private static final String TAG_PREFERENCE_BATCH = "PrefBatchName";
    private static final String TAG_PREFERENCE_LOCATION_SPINNER = "PrefLocationSpinner";
    private static final String BATCH_KEY = "batchKey";

    private String currentDate;
    private String dateModified;

    private java.sql.Date date;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(
            "E, dd MMM yyyy", Locale.US);

    public static final int BANNER = 0;
    public static int REC_VIEW_CUR_POS = 1;
    public static int REC_VIEW_MAX_POS;
    public static final int REC_VIEW_MIN_POS = 0;

    private static int currFocus = 0;
    public static final int RECVIEW1 = 1;

    private CustomLayoutManager mLayoutManager;

    private DatePickerDialog.OnDateSetListener dateSetListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String dateStr = String.valueOf(year)
                    + "-"
                    + (monthOfYear < 9 ? "0" + String.valueOf(monthOfYear + 1)
                    : String.valueOf(monthOfYear + 1))
                    + "-"
                    + (dayOfMonth < 9 ? "0" + String.valueOf(dayOfMonth)
                    : String.valueOf(dayOfMonth));
            Log.d(TAG, dateStr);
            date = java.sql.Date.valueOf(dateStr);
            dateView.setText(dateFormat.format(date));

            String batchName = sharedPreferences.getString(BATCH_KEY, null);

            if (batchName == null){
                //ToDo
                //batch name doesnt exist
                Log.i(TAG, "No batch key in shared preferences!");
            } else {
                editText.setHint(batchName);
                sendRequest(generateUrl(batchName, dateStr));
            }
//            fetchSchedule();
        }
    };
    private View.OnClickListener dateChangeClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DatePickFragment(dateSetListner, date).show(
                    getActivity().getFragmentManager(), DatePickFragment.TAG);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        currentDate = Constants.paramsDateFormat.format(new java.util.Date());
        dateModified = new SimpleDateFormat("EEE, dd MMM, yyyy").format(new java.util.Date());

        recyclerView = (RecyclerView) rootView.findViewById(R.id.schedule_recycler_view);
        editText = (EditText) rootView.findViewById(R.id.schedule_batch_et);
        dateView = (TextView) rootView.findViewById(R.id.schedule_date_tv);
        submitButton = (ImageView) rootView.findViewById(R.id.schedule_submit_ib);
        calendarButton = (ImageView) rootView.findViewById(R.id.btn_calendar);

        editText.requestFocus();

        recyclerView.setNextFocusUpId(R.id.schedule_batch_et);

        Log.i(TAG, "Modified Date: " + dateModified);
        dateView.setText(dateModified);

        sharedPreferences = getActivity().getSharedPreferences(TAG_PREFERENCE_BATCH, Context.MODE_PRIVATE);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
        submitButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    submitButton.setColorFilter(getActivity().getResources().getColor(R.color.highlighting_color));
                }
                else {
                    submitButton.setColorFilter(getActivity().getResources().getColor(R.color.spinner_color));
                }
            }
        });

        calendarButton.setOnClickListener(dateChangeClickListner);
        calendarButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    calendarButton.setColorFilter(getActivity().getResources().getColor(R.color.highlighting_color));
                }else {
                    calendarButton.setColorFilter(getActivity().getResources().getColor(R.color.spinner_color));
                }
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.i(TAG, "Button Code pressed: " + i + " Action Done: " + EditorInfo.IME_ACTION_DONE);
                if (i == 5){
                    onSubmit();
                }
                return false;
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    editText.getBackground().setColorFilter(getActivity().getResources().getColor(R.color.highlighting_color), PorterDuff.Mode.SRC_ATOP);
                }else {
                    editText.getBackground().setColorFilter(getActivity().getResources().getColor(R.color.spinner_color), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        if (ConnectionDetector.isConnected(getActivity())){
            String batchName = sharedPreferences.getString(BATCH_KEY, null);

            if (batchName == null){
                //ToDo
                //batch name doesnt exist
                Log.i(TAG, "No batch key in shared preferences!");
            } else {
                editText.setHint(batchName);
                sendRequest(generateUrl(batchName, currentDate));
            }
        }else {
            Toast toast = Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT);
            toast.show();
        }

        scheduleList = new ArrayList<>();
        adapter = new ScheduleAdapter(getContext(), scheduleList);

        mLayoutManager = new CustomLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);


//        recyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                Log.i("ONFOCUSCHANGE- reclist", "focus has changed I repeat the focus has changed! current focus = " + currFocus);
//                if(currFocus != RECVIEW1){
//                    currFocus = RECVIEW1;
//                    recyclerView.getChildAt(0).setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
//                }else {
//                    recyclerView.getChildAt(0).setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
//                }
//            }
//        });


//        recyclerView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
////                Log.d(TAG, "REC_VIEW_MAX_POS: " + REC_VIEW_MAX_POS);
//                Log.d(TAG, "REC_VIEW_CUR_POS: " + REC_VIEW_CUR_POS);
//                CustomLayoutManager llmgr = (CustomLayoutManager) recyclerView.getLayoutManager();
//                int firstVisiblePos = llmgr.findFirstCompletelyVisibleItemPosition();
//                int lastVisiblePos = llmgr.findLastCompletelyVisibleItemPosition();
//
//                int diff = lastVisiblePos - firstVisiblePos;
//
////                Log.i(TAG, "First: " + firstVisiblePos + " last: " + lastVisiblePos + " diff: " + diff);
//
//                int action = keyCode;
////                Log.d(TAG, "onKey(); KEY_ACTION: " + action + " ACTION_UP: " + KeyEvent.KEYCODE_DPAD_UP + " ACTION_DOWN: " + KeyEvent.KEYCODE_DPAD_DOWN);
//
//                if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN){
//                    REC_VIEW_CUR_POS += diff;
//                    Log.d(TAG, "REC_VIEW_CUR_POS Down: " + REC_VIEW_CUR_POS);
//                    if(REC_VIEW_CUR_POS<REC_VIEW_MAX_POS && REC_VIEW_CUR_POS>=REC_VIEW_MIN_POS) {
//                        Log.d(TAG, "REC_VIEW_CUR_POS Down: " + REC_VIEW_CUR_POS);
//                        recyclerView.smoothScrollToPosition(REC_VIEW_CUR_POS);
//                    }else {
//                        REC_VIEW_CUR_POS -= diff;
//                        //ToDo
//                        // implement focuschange to bottom fragment
//                    }
//
//                }
//
//                if (action == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN){
//                    if(REC_VIEW_CUR_POS>REC_VIEW_MIN_POS && REC_VIEW_CUR_POS<=REC_VIEW_MAX_POS) {
//                        REC_VIEW_CUR_POS -= diff;
//                        recyclerView.smoothScrollToPosition(REC_VIEW_CUR_POS);
//                    }else {
//                        REC_VIEW_CUR_POS += diff;
//                        editText.requestFocus();
//                    }
//                }
//
//                if (action == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN){
//                    //Todo
//                    //implement focus change to schedule fragment
//                    recyclerView.clearFocus();
//                }
//
//                if (action == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
//                    getActivity().finish();
//                }
//
//                return true;
//            }
//        });

//        prepareSchedule();
        return rootView;
    }

    public void prepareSchedule(){
        for(int i=0, j=7; i<REC_VIEW_MAX_POS; i++,j+=2){
//            scheduleList.add(new Schedule("Lecture: " + (i+1), "Reshmi", "Slot: " + j, "Room A" + j, "" +j+40));
        }

        adapter.notifyDataSetChanged();
        Log.d(TAG, "prepareSchedule");
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
        ParseJSONSchedule pj = new ParseJSONSchedule(json);
        ArrayList<Schedule> tempList = pj.parseJSON();

        if (tempList.size() != 0){
            scheduleList.clear();
            for (Schedule s: tempList){
                scheduleList.add(s);
            }

//        Log.i(TAG, "Sample schedule: " + scheduleList.get(3).getCourse());
            adapter.notifyDataSetChanged();
        }else {
            scheduleList.clear();
            adapter.notifyDataSetChanged();
            Toast toast = Toast.makeText(getActivity(), "No schedule present for the given Batch!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private String generateUrl(String batchName, String date){
        Map<String, String> params = new HashMap<>();
        params.put(Constants.NETWORK_PARAMS.SCHEDULE.BATCH, batchName);
        params.put(Constants.NETWORK_PARAMS.SCHEDULE.DATE, date);

        Log.i(TAG, "Date: " + date);
        String url = new StringBuilder(
                Constants.NETWORK_PARAMS.SCHEDULE.URL).append(
                Util.getUrlEncodedString(params)).toString();

        Log.i(TAG, "Schedule URL: " + url);

        return url;
    }

    private void onSubmit(){
        //                EditText ed = (EditText) v.findViewById(R.id.schedule_batch_et);
        if (ConnectionDetector.isConnected(getActivity())){
            String batchName = editText.getText().toString().toUpperCase();
//                String baseUrl = "http://theinspirer.in/ilpscheduleapp/schedulelist_json.php?date" + currentDate + "batch=" + batchName;
            if (!batchName.equalsIgnoreCase("")){
                editText.setText(batchName);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(BATCH_KEY, batchName);
                editor.apply();

                sendRequest(generateUrl(batchName, currentDate));

                Toast toast = Toast.makeText(getActivity(), batchName, Toast.LENGTH_SHORT);
                toast.show();
            }else {
                scheduleList.clear();
                adapter.notifyDataSetChanged();
                Toast toast = Toast.makeText(getActivity(), "Please enter a Batch Name!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }else {
            Toast toast = Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}
