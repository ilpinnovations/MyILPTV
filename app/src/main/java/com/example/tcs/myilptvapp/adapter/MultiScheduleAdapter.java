package com.example.tcs.myilptvapp.adapter;

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
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by tcs on 21/10/16.
 */

public class MultiScheduleAdapter extends RecyclerView.Adapter<MultiScheduleAdapter.CustomViewHolder> {

    private static int aCount = 0;
    private static int bCount = 0;
    private static int cCount = 0;

    private Context mContext;
    private ArrayList<String> batches;
    private ArrayList<Schedule> scheduleListA;
    private ArrayList<Schedule> scheduleListB;
    private ArrayList<Schedule> scheduleListC;
    private int selectedPos = 0;
    private final static String TAG = ScheduleAdapter.class.getSimpleName();
    private final static int SPAN_COUNT = 4;

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        public TextView course, faculty, batchView;

        public CustomViewHolder(View itemView) {
            super(itemView);

            course = (TextView) itemView.findViewById(R.id.schedule_card_course);
            faculty = (TextView) itemView.findViewById(R.id.schedule_card_faculty);
            batchView = (TextView) itemView.findViewById(R.id.schedule_card_no_schedule);
        }
    }

    public MultiScheduleAdapter(Context mContext, ArrayList<String> batches, ArrayList<Schedule> scheduleListA, ArrayList<Schedule> scheduleListB, ArrayList<Schedule> scheduleListC){
        this.mContext = mContext;
        this.batches = batches;
        this.scheduleListA = scheduleListA;
        this.scheduleListB = scheduleListB;
        this.scheduleListC = scheduleListC;

        this.aCount = 0;
        this.cCount = 0;
        this.bCount = 0;
    }

    @Override
    public MultiScheduleAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_multi_schedule, parent, false);
        itemView.setClickable(true);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MultiScheduleAdapter.CustomViewHolder holder, final int position) {
        Schedule schedule;
        int row = Util.getRowNumber(position, SPAN_COUNT);
        String batch = batches.get(row);

        Log.i(TAG, "Position: " + position + " pos-1%4: " + ((position-1)%SPAN_COUNT));
        if (position%SPAN_COUNT == 0){
            //LEFT
            schedule = scheduleListA.get(row);

            holder.batchView.setVisibility(View.VISIBLE);
            holder.course.setText(schedule.getCourse());
            holder.faculty.setText(schedule.getFaculty());
            holder.itemView.findViewById(R.id.schedule_card_ll).setVisibility(View.INVISIBLE);
            holder.itemView.findViewById(R.id.multi_schedule_card_view).setBackgroundColor(Color.WHITE);
            holder.batchView.setText(batch);
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
        }else if ((position-1)%SPAN_COUNT == 0){
            //First schedule position
            Log.i(TAG, "Slot A: Position: " + position);
            schedule = scheduleListA.get(row - aCount);

            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b){
                        holder.itemView.findViewById(R.id.schedule_card_ll).setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    }else {
                        holder.itemView.findViewById(R.id.schedule_card_ll).setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
            });

            if (batch.equalsIgnoreCase(schedule.getBatch())){
                holder.batchView.setVisibility(View.GONE);
                holder.itemView.findViewById(R.id.schedule_card_ll).setVisibility(View.VISIBLE);
                holder.course.setText(schedule.getCourse());
                holder.faculty.setText(schedule.getFaculty());
            }else {
                aCount++;
                holder.batchView.setVisibility(View.VISIBLE);
                holder.itemView.findViewById(R.id.schedule_card_ll).setVisibility(View.GONE);
                holder.batchView.setText("No Schedule!");
            }
        }else if ((position-2)%SPAN_COUNT == 0){
            //second schedule position
            schedule = scheduleListB.get(row - bCount);

            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b){
                        holder.itemView.findViewById(R.id.schedule_card_ll).setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    }else {
                        holder.itemView.findViewById(R.id.schedule_card_ll).setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
            });

            if (batch.equalsIgnoreCase(schedule.getBatch())){
                holder.batchView.setVisibility(View.GONE);
                holder.itemView.findViewById(R.id.schedule_card_ll).setVisibility(View.VISIBLE);
                holder.course.setText(schedule.getCourse());
                holder.faculty.setText(schedule.getFaculty());
            }else {
                bCount++;
                holder.batchView.setVisibility(View.VISIBLE);
                holder.itemView.findViewById(R.id.schedule_card_ll).setVisibility(View.GONE);
                holder.batchView.setText("No Schedule!");
            }
        }else if ((position-3)%SPAN_COUNT == 0){
            //third schedule position
            schedule = scheduleListC.get(row - cCount);

            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b){
                        holder.itemView.findViewById(R.id.schedule_card_ll).setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    }else {
                        holder.itemView.findViewById(R.id.schedule_card_ll).setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
            });

            if (batch.equalsIgnoreCase(schedule.getBatch())){
                holder.batchView.setVisibility(View.GONE);
                holder.itemView.findViewById(R.id.schedule_card_ll).setVisibility(View.VISIBLE);
                holder.course.setText(schedule.getCourse());
                holder.faculty.setText(schedule.getFaculty());
            }else {
                cCount++;
                holder.batchView.setVisibility(View.VISIBLE);
                holder.itemView.findViewById(R.id.schedule_card_ll).setVisibility(View.GONE);
                holder.batchView.setText("No Schedule!");
            }
        }
//        else {
//            //RIGHT
//            holder.batchView.setVisibility(View.GONE);
//            holder.itemView.findViewById(R.id.schedule_card_ll).setVisibility(View.VISIBLE);
//            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View view, boolean b) {
//                    if (b){
//                        holder.itemView.findViewById(R.id.schedule_card_ll).setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
//                    }else {
//                        holder.itemView.findViewById(R.id.schedule_card_ll).setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
//                    }
//                }
//            });
//
//            holder.course.setText(schedule.getCourse());
//            holder.faculty.setText(schedule.getFaculty());
//        }
    }

    @Override
    public int getItemCount() {
        return batches.size() * SPAN_COUNT;
    }
}
