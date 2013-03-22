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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Main extends Activity {

    private Button mServiceButton;
    private Button mAsyncButton;
    private ListView mPersonList;
    private ArrayList<Person> _data;

    /*
     * Persist the list data during rotations
     */
    @Override
   	public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList("PersonData", _data);
    }

    /*
     * Unhook the BroadcastManager that is listening for service returns before rotation
     */
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onNotice);
    }

    /*
     * Re-hookup the BroadcastManager to listen to service returns after rotation
     */
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(RestService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, filter);
    }

    /*
     * Helper method to put the list of persons into the ListView
     */
    private void BindPersonList() {
        PersonAdapter adapter = new PersonAdapter(Main.this, _data);
        mPersonList.setAdapter(adapter);
    }

    /*
     * The listener that responds to intents sent back from the service
     */
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

                //Check to see if we can connect
                if(WebHelper.isOnline(getApplicationContext())) {
                    //Send an intent to the service to get data
                    Intent intent = new Intent(Main.this, RestService.class);
                    intent.setData(Uri.parse("http://devfestdetroit.appspot.com/api/names/10"));
                    startService(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Not currently online", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAsyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        //Put back the person data if it was persisted due to rotation
        if(savedInstanceState != null && savedInstanceState.containsKey("PersonData")) {
            _data = savedInstanceState.getParcelableArrayList("PersonData");
            BindPersonList();
        }
    }
}
