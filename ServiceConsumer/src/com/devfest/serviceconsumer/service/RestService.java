package com.devfest.serviceconsumer.service;


import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import com.devfest.serviceconsumer.Person;
import com.devfest.serviceconsumer.WebHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

public class RestService extends IntentService {

    public RestService() {
        super("RestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int result = Activity.RESULT_CANCELED;

        Uri data = intent.getData();
        String url = data.getScheme() + "://" + data.getHost() + data.getPath();

        List<Person> people = getFromService(url);

        if(people != null) {
            result = Activity.RESULT_OK;
        }

        completeCall(intent.getExtras(), result, people);

    }

    private void completeCall(Bundle extras, int result, List<Person> people) {
        if (extras != null) {
            Messenger messenger = (Messenger) extras.get("messenger");
            Message msg = Message.obtain();
            msg.arg1 = result;
            msg.obj = people;
            try {
                messenger.send(msg);
            } catch (android.os.RemoteException e1) {
                Log.w(getClass().getName(), "Exception sending message", e1);
            }
        }
    }

    private List<Person> getFromService(String url) {
        List<Person> result;

        WebHelper http = new WebHelper();
        String webResult;
        try {
            webResult = http.getHttp(url);

            Gson parser = new Gson();

            result = parser.fromJson(webResult, new TypeToken<List<Person>>(){}.getType());

        } catch (IOException e) {
            result = null;
            Log.w(getClass().getName(), "Exception calling service", e);
        }

        return result;
    }
}
