package com.example.tcs.myilptvapp.data;

/**
 * Created by tcs on 21/10/16.
 */

public class Emergency {
    private String name;
    private String number;
    private String initials;


    public Emergency(){

    }

    public Emergency(String name, String number){
        this.name = name;
        this.number = number;
        this.initials = Character.toString(Character.toUpperCase(this.name.charAt(0)));
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
        return this.initials;
    }

    public void setInitials(String initials){
        this.initials = initials;
    }
}
