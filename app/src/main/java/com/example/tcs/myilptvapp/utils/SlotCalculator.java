package com.example.tcs.myilptvapp.utils;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by 1115394 on 11/10/2016.
 */
public class SlotCalculator {
    private static final String TAG = SlotCalculator.class.getSimpleName();

    private Context mContext;

    public SlotCalculator(Context context){
        this.mContext = context;
    }

    public static ArrayList<String> getSlots(String time, String location){
        int timeInInt = Integer.parseInt(time);

        ArrayList<String> slots = new ArrayList<>();

        if (timeInInt >= Constants.TIME_IN_INT.TIME_1 && timeInInt < Constants.TIME_IN_INT.TIME_2){
            // 06:00 - 08:00
            if (location.equalsIgnoreCase(Constants.PLANETS.TRIVANDRUM)){
                slots.add(Constants.SLOTS.SLOT_A_MINUS);
                slots.add(Constants.SLOTS.SLOT_A);
                slots.add(Constants.SLOTS.SLOT_B);
            }else {
                slots.add(Constants.SLOTS.SLOT_MINUS_A);
                slots.add(Constants.SLOTS.SLOT_A);
                slots.add(Constants.SLOTS.SLOT_B);
            }
        }else if (timeInInt >= Constants.TIME_IN_INT.TIME_2 && timeInInt < Constants.TIME_IN_INT.TIME_3){
            // 08:00 - 10:00
            slots.add(Constants.SLOTS.SLOT_A);
            slots.add(Constants.SLOTS.SLOT_B);
            slots.add(Constants.SLOTS.SLOT_C);
        }else if (timeInInt >= Constants.TIME_IN_INT.TIME_3 && timeInInt < Constants.TIME_IN_INT.TIME_4){
            // 10:00 - 12:00
            slots.add(Constants.SLOTS.SLOT_B);
            slots.add(Constants.SLOTS.SLOT_C);
            slots.add(Constants.SLOTS.SLOT_D);
        }else if (timeInInt >= Constants.TIME_IN_INT.TIME_4 && timeInInt < Constants.TIME_IN_INT.TIME_5){
            // 12:00 - 14:00
            slots.add(Constants.SLOTS.SLOT_C);
            slots.add(Constants.SLOTS.SLOT_D);
            slots.add(Constants.SLOTS.SLOT_D_PLUS);
        }else if (timeInInt >= Constants.TIME_IN_INT.TIME_5 && timeInInt < Constants.TIME_IN_INT.TIME_6){
            // 14:00 - 16:00
            slots.add(Constants.SLOTS.SLOT_D);
            slots.add(Constants.SLOTS.SLOT_D_PLUS);
            slots.add(Constants.SLOTS.SLOT_E);
        }else if (timeInInt >= Constants.TIME_IN_INT.TIME_6 && timeInInt < Constants.TIME_IN_INT.TIME_7){
            // 16:00 - 18:00
            slots.add(Constants.SLOTS.SLOT_D_PLUS);
            slots.add(Constants.SLOTS.SLOT_E);
            slots.add(Constants.SLOTS.SLOT_F);
        }else if (timeInInt >= Constants.TIME_IN_INT.TIME_7){
            // 18:00 - ...
            slots.add(Constants.SLOTS.SLOT_D_PLUS);
            slots.add(Constants.SLOTS.SLOT_E);
            slots.add(Constants.SLOTS.SLOT_F);
        }else if (timeInInt < Constants.TIME_IN_INT.TIME_1){
            // ... - 06:00
            if (location.equalsIgnoreCase(Constants.PLANETS.TRIVANDRUM)){
                slots.add(Constants.SLOTS.SLOT_A_MINUS);
                slots.add(Constants.SLOTS.SLOT_A);
                slots.add(Constants.SLOTS.SLOT_B);
            }else {
                slots.add(Constants.SLOTS.SLOT_MINUS_A);
                slots.add(Constants.SLOTS.SLOT_A);
                slots.add(Constants.SLOTS.SLOT_B);
            }
        }else {
            slots.add(Constants.SLOTS.SLOT_A);
            slots.add(Constants.SLOTS.SLOT_C);
            slots.add(Constants.SLOTS.SLOT_E);
        }
        return slots;
    }

    public static int getNextSlotHour(String time){
        int timeInInt = Integer.parseInt(time);

        int slotHour = 6;

        if (timeInInt >= Constants.TIME_IN_INT.TIME_1 && timeInInt < Constants.TIME_IN_INT.TIME_2){
            // 06:00 - 08:00
            slotHour = 8;
        }else if (timeInInt >= Constants.TIME_IN_INT.TIME_2 && timeInInt < Constants.TIME_IN_INT.TIME_3){
            // 08:00 - 10:00
            slotHour = 10;
        }else if (timeInInt >= Constants.TIME_IN_INT.TIME_3 && timeInInt < Constants.TIME_IN_INT.TIME_4){
            // 10:00 - 12:00
            slotHour = 12;
        }else if (timeInInt >= Constants.TIME_IN_INT.TIME_4 && timeInInt < Constants.TIME_IN_INT.TIME_5){
            // 12:00 - 14:00
            slotHour = 14;
        }else if (timeInInt >= Constants.TIME_IN_INT.TIME_5 && timeInInt < Constants.TIME_IN_INT.TIME_6){
            // 14:00 - 16:00
            slotHour = 16;
        }else if (timeInInt >= Constants.TIME_IN_INT.TIME_6 && timeInInt < Constants.TIME_IN_INT.TIME_7){
            // 16:00 - 18:00
            slotHour = 18;
        }else if (timeInInt >= Constants.TIME_IN_INT.TIME_7){
            // 18:00 - ...
            slotHour = 6;
        }else if (timeInInt < Constants.TIME_IN_INT.TIME_1){
            // ... - 06:00
            slotHour = 6;
        }else {
            slotHour = 6;
        }
        return slotHour;
    }
}
