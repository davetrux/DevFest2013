package com.devfest.serviceconsumer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Main extends Activity {

    private Button mServiceButton;
    private ListView mPersonList;
    private ArrayList<Person> mData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mServiceButton = (Button) findViewById(R.id.callService);

        mPersonList = (ListView) findViewById(R.id.personList);

        mServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPersonList.getCount() > 0) {
                    mPersonList.setAdapter(null);
                }
                //Send an intent to the service to get data

            }
        });
    }
}
