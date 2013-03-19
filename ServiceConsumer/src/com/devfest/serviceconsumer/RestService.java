package com.devfest.serviceconsumer;


import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

public class RestService extends IntentService {

    public static final String ACTION="REST-PERSONS";

    public RestService() {
        super("RestService");
    }

    /*
     * Handle the incoming intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        int result = Activity.RESULT_CANCELED;

        //Get the url from the intent
        Uri data = intent.getData();
        String url = data.getScheme() + "://" + data.getHost() + data.getPath();

        //Fetch the data
        String people = getJson(url);

        if(!people.equalsIgnoreCase("")) {
            result = Activity.RESULT_OK;
        }

        //Send back the results
        sendResult(result, people);
    }

    /*
     * Place the results into an intent and return it to the caller
     */
    private void sendResult(int result, String personJson) {

        Intent sendBack = new Intent(ACTION);

        sendBack.putExtra("result", result);
        sendBack.putExtra("personlist", personJson);

        //Keep the intent local to the application
        LocalBroadcastManager.getInstance(this).sendBroadcast(sendBack);
    }

    /*
     * Get the JSON results from the web service
     */
    private String getJson(String url) {
        WebHelper http = new WebHelper();
           String webResult;
           try {
               webResult = http.getHttp(url);

           } catch (IOException e) {
               webResult = "";
               Log.d(getClass().getName(), "Exception calling service", e);
           }

           return webResult;
    }
}
