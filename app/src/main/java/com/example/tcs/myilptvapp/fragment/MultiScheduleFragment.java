package com.example.tcs.myilptvapp.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tcs.myilptvapp.R;
import com.example.tcs.myilptvapp.adapter.MultiScheduleAdapterTemp;
import com.example.tcs.myilptvapp.data.Schedule;
import com.example.tcs.myilptvapp.helper.MultiScheduleArrayListMaker;
import com.example.tcs.myilptvapp.utils.Constants;
import com.example.tcs.myilptvapp.utils.SlotCalculator;
import com.example.tcs.myilptvapp.utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MultiScheduleFragment extends Fragment {

    private static final String TAG = MultiScheduleFragment.class.getSimpleName();
    private static final String FLAG_A = "A";
    private static final String FLAG_B = "B";
    private static final String FLAG_C = "C";
    private static final String FLAG_BATCH = "BATCH";

    private static final String INTENT_KEY_LOCATION = "intent_location";
    private static final String INTENT_KEY_DATE = "intent_date";

    private MultiScheduleAdapterTemp adapter;
    private RecyclerView recyclerView;
    private TextView slotView1, slotView2, slotView3;
    private TextView dateView, timeView;

    private ArrayList<String> batches = new ArrayList<>();
    private ArrayList<Schedule> scheduleSlot1 = new ArrayList<>();
    private ArrayList<Schedule> scheduleSlot2 = new ArrayList<>();
    private ArrayList<Schedule> scheduleSlot3 = new ArrayList<>();
    private ArrayList<Schedule> finalScheduleList = new ArrayList<>();

    private String location;
    private String strSlot1;
    private String strSlot2;
    private String strSlot3;
    private String dateForUrl;
    private String dateForView;

    private static final long ONE_SECOND = 1000;
    private static final long ONE_MINUTE = 60 * ONE_SECOND;
    private static final long ONE_HOUR = 60 * ONE_MINUTE;
    private static final long SLOT_TIME_DIFF_IN_MILLIS = 2 * ONE_HOUR;

    ScheduleReceiver messageReceiver;

    CountDownTimer newtimer;

    private int REC_VIEW_MAX_POS = 10;
    private int SPAN_COUNT = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        messageReceiver = new ScheduleReceiver();
        IntentFilter messageFilter = new IntentFilter("com.example.tcs.myilptvapp.INTENT_SCHEDULE");
        getActivity().registerReceiver(messageReceiver, messageFilter);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_multi_schedule, container, false);

        slotView1 = (TextView) rootView.findViewById(R.id.schedule_slot1);
        slotView2 = (TextView) rootView.findViewById(R.id.schedule_slot2);
        slotView3 = (TextView) rootView.findViewById(R.id.schedule_slot3);

        dateView = (TextView) rootView.findViewById(R.id.multi_schedule_date);
        timeView = (TextView) rootView.findViewById(R.id.multi_schedule_time);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.multi_schedule_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Timer to display current time
        newtimer = new CountDownTimer(1000000000, 1000) {

            public void onTick(long millisUntilFinished) {
                Calendar c = Calendar.getInstance();
                timeView.setText(c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+"  "+(c.get(Calendar.AM_PM)==0?"AM":"PM"));
            }
            public void onFinish() {

            }
        };
        newtimer.start();

//        adapter = new MultiScheduleAdapter(getActivity(), batches, scheduleSlot1, scheduleSlot2, scheduleSlot3);
        adapter = new MultiScheduleAdapterTemp(getActivity(), finalScheduleList);
        recyclerView.setAdapter(adapter);

//        prepareSchedule();

        dateForView = new SimpleDateFormat("EEE, dd MMM, yyyy", Locale.US).format(new Date());
        dateView.setText(dateForView);

        Bundle args = getArguments();


        if (args != null) {
            String temp = args.getString(ContactsFragment.LOCATION_BUNDLE_TAG);

            if (temp != null) {
                switch (temp) {
                    case Constants.LOCATIONS.TRIVANDRUM.LOC_NAME:
                        location = Constants.PLANETS.TRIVANDRUM;
                        break;

                    case Constants.LOCATIONS.HYDERABAD.LOC_NAME:
                        location = Constants.PLANETS.HYDERABAD;
                        break;

                    case Constants.LOCATIONS.CHENNAI.LOC_NAME:
                        location = Constants.PLANETS.CHENNAI;
                        break;

                    case Constants.LOCATIONS.GUWAHATI.LOC_NAME:
                        location = Constants.PLANETS.GUWAHATI;
                        break;

                    case Constants.LOCATIONS.AHMEDABAD.LOC_NAME:
                        location = Constants.PLANETS.AHMEDABAD;
                        break;

                    default:
                        location = Constants.PLANETS.TRIVANDRUM;
                        break;
                }
            }


            Log.i(TAG, "Received bundled arguments | location: " + location);
        } else {
            Log.e(TAG, "Received null arguments");
            location = Constants.PLANETS.TRIVANDRUM;
        }

        dateForUrl = Constants.paramsDateFormat.format(new Date());

        //ToDo timer
        // implement timer or scheduler over here to refresh the slot values on time basis
        String curTime = new SimpleDateFormat("HHmmss", Locale.US).format(new Date());
        ArrayList<String> slots = SlotCalculator.getSlots(curTime, location);
        strSlot1 = slots.get(0);
        strSlot2 = slots.get(1);
        strSlot3 = slots.get(2);

        slotView1.setText("Slot " + strSlot1);
        slotView2.setText("Slot " + strSlot2);
        slotView3.setText("Slot " + strSlot3);

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

        adapter.notifyDataSetChanged();

        Calendar cur_cal = Calendar.getInstance();
        cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar

        int hour = SlotCalculator.getNextSlotHour(curTime);
        Log.i(TAG, "Hour: " + hour);
        Log.i(TAG, "Day of year: " + cur_cal.get(Calendar.DAY_OF_YEAR));
        Log.i(TAG, "Date: " + cur_cal.get(Calendar.DATE));
        Log.i(TAG, "Month: " + cur_cal.get(Calendar.MONTH));

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, cur_cal.get(Calendar.MILLISECOND));
        cal.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
        cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));

        Intent intent = new Intent();
        intent.setAction("com.example.tcs.myilptvapp.INTENT_SCHEDULE");
        intent.putExtra(INTENT_KEY_LOCATION, location);
        intent.putExtra(INTENT_KEY_DATE, dateForUrl);
        PendingIntent pintent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), SLOT_TIME_DIFF_IN_MILLIS, pintent);

        // Register the local broadcast receiver
//        IntentFilter messageFilter = new IntentFilter();
//        messageFilter.addAction(null);
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver, messageFilter);
//        getActivity().registerReceiver(messageReceiver, messageFilter);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(messageReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        newtimer.cancel();
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

    public class ScheduleReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "OnReceive");
            String location = intent.getStringExtra(INTENT_KEY_LOCATION);
            String date = intent.getStringExtra(INTENT_KEY_DATE);
            //ToDo timer
            // implement timer or scheduler over here to refresh the slot values on time basis
            String curTime = new SimpleDateFormat("HHmmss", Locale.US).format(new Date());
            ArrayList<String> slots = SlotCalculator.getSlots(curTime, location);
            String slot1 = slots.get(0);
            String slot2 = slots.get(1);
            String slot3 = slots.get(2);

            slotView1.setText(slot1);
            slotView2.setText(slot2);
            slotView3.setText(slot3);

            MultiScheduleArrayListMaker maker = new MultiScheduleArrayListMaker(getActivity(), location, date, slot1, slot2, slot3, new MultiScheduleArrayListMaker.AsyncResponse() {
                @Override
                public void onProcessFinish(ArrayList<Schedule> output) {
                    Log.i(TAG, "onProcessFinish | output Size: " + output.size());
                    finalScheduleList.clear();
                    finalScheduleList.addAll(output);
                    adapter.notifyDataSetChanged();
                }
            });
            maker.generate();
        }
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
}
