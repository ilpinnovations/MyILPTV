package com.example.tcs.myilptvapp.data;

/**
 * Created by tcs on 21/10/16.
 */

public class Schedule {
    private String course;
    private String faculty;
    private String slot;
    private String room;
    private String date;
    public String batch;
    public String result;

    public Schedule(){

    }

    public Schedule(String course, String faculty, String slot, String room, String date, String batch, String result){
        this.course = course;
        this.faculty = faculty;
        this.room = room;
        this.slot = slot;
        this.date = date;
        this.batch = batch;
        this.result = result;
    }

    public String getCourse(){
        return this.course;
    }

    public void setCourse(String course){
        this.course = course;
    }

    public String getFaculty(){
        return this.faculty;
    }

    public void setFaculty(String faculty){
        this.faculty = faculty;
    }

    public String getSlot(){
        return this.slot;
    }

    public void setSlot(String slot){
        this.slot = slot;
    }

    public String getRoom(){
        return this.room;
    }

    public void setRoom(String room){
        this.room = room;
    }

    public String getDate(){
        return this.date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getBatch(){
        return this.batch;
    }

    public void setBatch(String batch){
        this.batch = batch;
    }

    public String getResult(){
        return this.result;
    }

    public void setResult(String result){
        this.result = result;
    }

}
