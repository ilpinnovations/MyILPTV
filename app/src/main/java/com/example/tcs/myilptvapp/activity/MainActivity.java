package com.example.tcs.myilptvapp.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.example.tcs.myilptvapp.R;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
