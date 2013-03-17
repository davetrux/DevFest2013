package com.devfest.serviceconsumer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.devfest.serviceconsumer.service.RestService;
import com.devfest.serviceconsumer.task.AsyncWorker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Main extends Activity {

    private Button mServiceButton;
    private Button mAsyncButton;
    private ListView mPersonList;
    private ArrayList<Person> _data;

    @Override
   	public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("PersonData", _data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onNotice);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(RestService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, filter);
    }

    private void BindPersonList() {
        PersonAdapter adapter = new PersonAdapter(Main.this, _data);
        mPersonList.setAdapter(adapter);
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int serviceResult = intent.getIntExtra("result", -1);
            if (serviceResult == RESULT_OK) {
                String json = intent.getStringExtra("personlist");
                Gson parser = new Gson();
                _data = parser.fromJson(json, new TypeToken<ArrayList<Person>>(){}.getType());

                BindPersonList();

            } else {
                Toast.makeText(Main.this, "Rest call failed.", Toast.LENGTH_LONG).show();
            }

            Log.d("BroadcastReciever", "onReceive called");
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mAsyncButton = (Button) findViewById(R.id.callAsync);

        mServiceButton = (Button) findViewById(R.id.callService);

        mPersonList = (ListView) findViewById(R.id.personList);

        mServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPersonList.getCount() > 0) {
                    mPersonList.setAdapter(null);
                }
                Intent intent = new Intent(Main.this, RestService.class);
                intent.setData(Uri.parse("http://devfestdetroit.appspot.com/api/names/10"));
                startService(intent);
            }
        });

        mAsyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncWorker async = new AsyncWorker(mPersonList);
                async.execute("http://devfestdetroit.appspot.com/api/name");
            }
        });

        if(savedInstanceState != null && savedInstanceState.containsKey("PersonData")) {
            _data = savedInstanceState.getParcelableArrayList("PersonData");
            BindPersonList();
        }
    }
}
