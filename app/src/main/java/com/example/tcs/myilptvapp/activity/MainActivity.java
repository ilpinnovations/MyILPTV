package com.example.tcs.myilptvapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.TintAwareDrawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.example.tcs.myilptvapp.R;
import com.example.tcs.myilptvapp.adapter.ContactsAdapter;
import com.example.tcs.myilptvapp.data.Contacts;
import com.example.tcs.myilptvapp.data.Schedule;
import com.example.tcs.myilptvapp.fragment.ContactsFragment;
import com.example.tcs.myilptvapp.fragment.MultiScheduleFragment;
import com.example.tcs.myilptvapp.fragment.ScheduleFragment;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String SCHEDULE_FRAGMENT_TAG = "SCHEDULE";
    private static final String MULTI_SCHEDULE_FRAGMENT_TAG = "MULTI_SCHEDULE";
    private static final String CONTACTS_FRAGMENT_TAG = "CONTACTS";

    public static final String TRIVANDRUM = "Trivandrum";
    public static final String CHENNAI = "Chennai";
    public static final String GUWAHATI = "Guwahati";
    public static final String AHMEDABAD = "Ahmedabad";
    public static final String HYDERABAD = "Hyderabad";

    private ContactsAdapter adapter;
    private ArrayList<Contacts> emergencyList;
    private Spinner dropDown;
    private CheckBox checkBox;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_schedule_container, new ScheduleFragment(), SCHEDULE_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();

        final ArrayList<String> array = new ArrayList<String>();
        array.add(TRIVANDRUM);
        array.add(CHENNAI);
        array.add(GUWAHATI);
        array.add(AHMEDABAD);
        array.add(HYDERABAD);

        ArrayAdapter<String> mAdapter;
        dropDown= (Spinner) findViewById(R.id.contacts_spinner);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        mAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, array);
        dropDown.setAdapter(mAdapter);

        Bundle args = new Bundle();
        args.putString(ContactsFragment.LOCATION_BUNDLE_TAG, dropDown.getSelectedItem().toString());

        ContactsFragment contactsFragment = new ContactsFragment();
        contactsFragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_contacts_container, contactsFragment, CONTACTS_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();

//        dropDown.requestFocus();

        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args1 = new Bundle();
                ContactsFragment contactsFragment1 = new ContactsFragment();

                String selectedItem = dropDown.getSelectedItem().toString();

                args1.putString(ContactsFragment.LOCATION_BUNDLE_TAG, selectedItem);

                contactsFragment1.setArguments(args1);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_contacts_container, contactsFragment1, CONTACTS_FRAGMENT_TAG)
                        .addToBackStack(null)
                        .commit();

                if (checkBox.isChecked()){
                    MultiScheduleFragment fragment = new MultiScheduleFragment();
                    fragment.setArguments(args1);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_schedule_container, fragment, MULTI_SCHEDULE_FRAGMENT_TAG)
                            .addToBackStack(null)
                            .commit();
                }else {
                    Log.d(TAG, "Single Schedule fragment!");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        dropDown.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    dropDown.setBackground(getResources().getDrawable(R.drawable.spinner_background_accent));
                }
                else {
                    dropDown.setBackground(getResources().getDrawable(R.drawable.spinner_background));
                }
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()){
                    // use multi schedule fragment

                    Bundle args = new Bundle();
                    args.putString(ContactsFragment.LOCATION_BUNDLE_TAG, dropDown.getSelectedItem().toString());
                    MultiScheduleFragment fragment = new MultiScheduleFragment();
                    fragment.setArguments(args);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_schedule_container, fragment, MULTI_SCHEDULE_FRAGMENT_TAG)
                            .addToBackStack(null)
                            .commit();

                }else {
                    // use single schedule fragment
                    ScheduleFragment fragment = new ScheduleFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_schedule_container, fragment, SCHEDULE_FRAGMENT_TAG)
                            .addToBackStack(null)
                            .commit();


                }
            }
        });

        checkBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    checkBox.setBackground(getResources().getDrawable(R.drawable.checkbox_background_accent));
                }
                else {
                    checkBox.setBackground(getResources().getDrawable(R.drawable.checkbox_background));
                }
            }
        });



    }

//    public void clearBackStackInclusive(String tag) {
//        getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//    }

}
