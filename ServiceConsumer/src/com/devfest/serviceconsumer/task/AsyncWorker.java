package com.devfest.serviceconsumer.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;
import com.devfest.serviceconsumer.Person;
import com.devfest.serviceconsumer.WebHelper;
import com.google.gson.Gson;

import java.io.IOException;


public class AsyncWorker extends AsyncTask<String, Void, String> {

    private ListView _result;
    private Activity _context;

    public AsyncWorker(ListView result){
        _result = result;
    }

    public void connectContext(Activity context) {
        this._context = context;
    }

    public void disconnectContext() {
        this._context = null;
    }

    @Override
    protected String doInBackground(String... url) {
        WebHelper http = new WebHelper();

        try {
            return http.getHttp(url[0]);
        } catch (IOException e) {
            //_result.setText("Async Error");
            return "";
        }    }

    @Override
    protected void onPostExecute(String result) {

        if(result != "") {
            Gson parser = new Gson();

            Person data = parser.fromJson(result, Person.class);

            //_result.setText(data.getFirstName() + " " + data.getLastName());
        }
    }
}
