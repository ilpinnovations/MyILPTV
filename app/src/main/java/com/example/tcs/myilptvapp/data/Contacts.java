package com.example.tcs.myilptvapp.data;

/**
 * Created by tcs on 21/10/16.
 */

public class Contacts {
    private String name;
    private String number;
    private String initials;

    private static final String TAG = Contacts.class.getSimpleName();


    public Contacts(){

    }

    public Contacts(String name, String number){
        this.name = name;
        this.number = number;
        this.initials = name.substring(0,1).toUpperCase();
//        Log.i(TAG, "Initials: " + this.initials);
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getNumber(){
        return this.number;
    }

    public void setNumber(String number){
        this.number = number;
    }

    public String getInitials(){
//        Log.i(TAG, "getInitials: " + this.initials);
        return this.initials;
    }

    public void setInitials(String initials){
        this.initials = initials;
    }
}
