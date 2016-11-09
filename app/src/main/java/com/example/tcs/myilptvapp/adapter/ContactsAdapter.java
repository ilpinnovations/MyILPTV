package com.example.tcs.myilptvapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tcs.myilptvapp.data.Contacts;
import com.example.tcs.myilptvapp.R;

import java.util.List;

/**
 * Created by tcs on 21/10/16.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.CustomViewHolder> {

    private static final String TAG = ContactsAdapter.class.getSimpleName();
    private Context mContext;
    private List<Contacts> emergencyList;

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        public TextView name, number, initials;

        public CustomViewHolder(View itemView) {
            super(itemView);

            initials = (TextView) itemView.findViewById(R.id.emergency_card_initials_tv);
            name = (TextView) itemView.findViewById(R.id.emergency_card_name_tv);
            number = (TextView) itemView.findViewById(R.id.emergency_card_number_tv);
        }
    }

    public ContactsAdapter(Context mContext, List<Contacts> emergencyList){
        this.mContext = mContext;
        this.emergencyList = emergencyList;
    }

    @Override
    public ContactsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_emergency, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ContactsAdapter.CustomViewHolder holder, int position) {
        Contacts emergency = emergencyList.get(position);

        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    holder.itemView.findViewById(R.id.contacts_card_ll).setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                }else {
                    holder.itemView.findViewById(R.id.contacts_card_ll).setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        });

//        Log.i(TAG, "Initials: " + emergency.getInitials());
        holder.initials.setText(emergency.getInitials());
        holder.name.setText(emergency.getName());
        holder.number.setText(emergency.getNumber());
    }

    @Override
    public int getItemCount() {
        return emergencyList.size();
    }
}
