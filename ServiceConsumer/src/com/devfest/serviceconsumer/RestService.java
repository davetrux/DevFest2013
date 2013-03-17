package com.devfest.serviceconsumer;


import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.devfest.serviceconsumer.WebHelper;

import java.io.IOException;

public class RestService extends IntentService {

    public static final String ACTION="REST-PERSONS";

    public RestService() {
        super("RestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int result = Activity.RESULT_CANCELED;

        Uri data = intent.getData();
        String url = data.getScheme() + "://" + data.getHost() + data.getPath();

        String people = getJson(url);

        if(people != "") {
            result = Activity.RESULT_OK;
        }

        sendResult(result, people);
    }

    private void sendResult(int result, String personJson) {

        Intent sendBack = new Intent(ACTION);

        sendBack.putExtra("result", result);
        sendBack.putExtra("personlist", personJson);

        LocalBroadcastManager.getInstance(this).sendBroadcast(sendBack);
    }

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
