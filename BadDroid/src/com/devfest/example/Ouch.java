package com.devfest.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

public class Ouch extends Activity {

    private Button _uiButton;
    private Button _longButton;
    private Button _blockButton;
    private TextView _uiText;
    private TextView _resultText;

    private Person _data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        _uiText = (TextView) findViewById(R.id.uiTestText);
        _resultText = (TextView) findViewById(R.id.resultText);

        _uiButton = (Button) findViewById(R.id.testButton);
        _uiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_uiText.getText() == "Two") {
                    _uiText.setText("One");
                } else {
                    _uiText.setText("Two");
                }
            }
        });

        _blockButton = (Button) findViewById(R.id.blockButton);
        _blockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callService("http://devfestdetroit.appspot.com/api/name");
            }
        });

        _longButton = (Button) findViewById(R.id.longButton);
        _longButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                callService("http://devfestdetroit.appspot.com/api/long");
            }
        });
    }

    private void callService(String url) {

        _resultText.setText("");

        WebHelper http = new WebHelper();

        try {
            String result = http.getHttp(url);

            Gson parser = new Gson();

            _data = parser.fromJson(result, Person.class);

            _resultText.setText(_data.getFirstName() + " " + _data.getLastName());

        } catch (IOException e) {
            _resultText.setText("Error");
        }

    }
}
