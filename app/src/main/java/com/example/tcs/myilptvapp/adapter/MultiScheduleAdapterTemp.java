package com.example.tcs.myilptvapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tcs.myilptvapp.R;
import com.example.tcs.myilptvapp.data.Schedule;
import com.example.tcs.myilptvapp.utils.Util;

import java.util.ArrayList;

/**
 * Created by 1115394 on 11/9/2016.
 */
public class MultiScheduleAdapterTemp extends RecyclerView.Adapter<MultiScheduleAdapterTemp.CustomViewHolder> {


    private Context mContext;
    private ArrayList<Schedule> schedulesList;
    private final static String TAG = ScheduleAdapter.class.getSimpleName();
    private final static int SPAN_COUNT = 4;

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView course, faculty, batchView;
//        private ArrayList<Schedule> scheduleList;

        public CustomViewHolder(View itemView) {
            super(itemView);

            course = (TextView) itemView.findViewById(R.id.schedule_card_course);
            faculty = (TextView) itemView.findViewById(R.id.schedule_card_faculty);
            batchView = (TextView) itemView.findViewById(R.id.schedule_card_no_schedule);

            itemView.setOnClickListener(this);

//            this.scheduleList = schedule;
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            Schedule schedule = schedulesList.get(position);
            if (position%SPAN_COUNT != 0 && !schedule.getCourse().equalsIgnoreCase("No Schedule")){

                String alertMessage = "Batch: " + schedule.getBatch()
                        + "\nTitle: " + schedule.getCourse()
                        + "\nFaculty: " + schedule.getFaculty()
                        + "\nSlot: " + schedule.getSlot()
                        + "\nRoom: " + schedule.getRoom();

                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle("Schedule");
                alert.setMessage(alertMessage);
                alert.setPositiveButton("OK",null);
                alert.show();
            }
        }
    }

    public MultiScheduleAdapterTemp(Context mContext, ArrayList<Schedule> schedulesList){
        this.mContext = mContext;
        this.schedulesList = schedulesList;
    }

    @Override
    public MultiScheduleAdapterTemp.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_multi_schedule, parent, false);
        itemView.setClickable(true);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MultiScheduleAdapterTemp.CustomViewHolder holder, final int position) {
        Schedule schedule = schedulesList.get(position);

//        Log.i(TAG, "Fetched data: " + schedule.getBatch());

        if (position%SPAN_COUNT == 0){
            //WHITE

            holder.batchView.setVisibility(View.VISIBLE);
            holder.course.setText("");
            holder.faculty.setText("");
            holder.itemView.findViewById(R.id.schedule_card_ll).setVisibility(View.INVISIBLE);
            holder.itemView.findViewById(R.id.multi_schedule_card_view).setBackgroundColor(Color.WHITE);
            holder.batchView.setText(schedule.getBatch());
            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b){
                        holder.batchView.setTextColor(mContext.getResources().getColor(R.color.highlighting_color));
                    }else {
                        holder.batchView.setTextColor(mContext.getResources().getColor(R.color.colorText));
                    }
                }
            });
        }else {
            //GREEN

//            holder.itemView.findViewById(R.id.multi_schedule_card_view).setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b){
//                        Log.i(TAG, "onFocusChangeGreen");
                        holder.itemView.findViewById(R.id.schedule_card_ll).setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    }else {
                        holder.itemView.findViewById(R.id.schedule_card_ll).setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
            });

            holder.batchView.setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.schedule_card_ll).setVisibility(View.VISIBLE);

            if (schedule.getResult().equalsIgnoreCase("failed")){
                holder.course.setText(schedule.getCourse());
                holder.batchView.setText("");
                holder.faculty.setText("");
            }else {
                holder.course.setText(schedule.getCourse());
                holder.faculty.setText(schedule.getFaculty());
            }
        }
    }

    @Override
    public int getItemCount() {
        return schedulesList.size();
    }
}

