package com.example.tcs.myilptvapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tcs.myilptvapp.R;
import com.example.tcs.myilptvapp.data.Schedule;

import java.util.List;

/**
 * Created by tcs on 21/10/16.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.CustomViewHolder> {

    private Context mContext;
    private List<Schedule> scheduleList;
    private int selectedPos = 0;
    private final static String TAG = ScheduleAdapter.class.getSimpleName();

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        public TextView course, slot, room, faculty;

        public CustomViewHolder(View itemView) {
            super(itemView);

            course = (TextView) itemView.findViewById(R.id.schedule_card_course);
            slot = (TextView) itemView.findViewById(R.id.schedule_card_slot);
            room = (TextView) itemView.findViewById(R.id.schedule_card_room);
            faculty = (TextView) itemView.findViewById(R.id.schedule_card_faculty);
        }
    }

    public ScheduleAdapter(Context mContext, List<Schedule> scheduleList){
        this.mContext = mContext;
        this.scheduleList = scheduleList;
    }

    @Override
    public ScheduleAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_schedule, parent, false);
        itemView.setClickable(true);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ScheduleAdapter.CustomViewHolder holder, final int position) {
        Schedule schedule = scheduleList.get(position);

        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    Log.i(TAG, "onFocusChange True");
                    holder.itemView.findViewById(R.id.schedule_card_ll).setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                }else {
                    holder.itemView.findViewById(R.id.schedule_card_ll).setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        });

        holder.course.setText(schedule.getCourse());
        holder.room.setText(schedule.getRoom());
        holder.faculty.setText(schedule.getFaculty());
        holder.slot.setText(schedule.getSlot());

//        if(selectedPos == position){
//            // Here I am just highlighting the background
//            holder.itemView.setBackgroundColor(Color.GREEN);
//        }else{
//            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
//        }
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Updating old as well as new positions
//                notifyItemChanged(selectedPos);
//                selectedPos = position;
//                notifyItemChanged(selectedPos);
//
//                // Do your another stuff for your onClick
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }
}
