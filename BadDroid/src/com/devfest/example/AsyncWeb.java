package com.devfest.example;

import android.os.AsyncTask;
import android.widget.TextView;
import com.google.gson.Gson;

import java.io.IOException;

public class AsyncWeb extends AsyncTask<String, Void, String> {

    private TextView _result;

    public AsyncWeb(TextView result) {
        _result = result;
    }

    @Override
    protected String doInBackground(String... url) {
        WebHelper http = new WebHelper();

        try {
            return http.getHttp(url[0]);
        } catch (IOException e) {
            _result.setText("Async Error");
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {

        if(result != "") {
            Gson parser = new Gson();

            Person data = parser.fromJson(result, Person.class);

            _result.setText(data.getFirstName() + " " + data.getLastName());
        }
    }
}
