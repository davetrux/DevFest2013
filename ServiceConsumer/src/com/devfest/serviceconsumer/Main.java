package com.devfest.serviceconsumer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.devfest.serviceconsumer.service.RestService;
import com.devfest.serviceconsumer.task.AsyncWorker;

import java.util.List;

public class Main extends Activity {

    private Button _serviceButton;
    private Button _asyncButton;
    private ListView _personList;
    private AsyncWorker _worker;
    private Handler _handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //_worker = (AsyncWorker) getLastCustomNonConfigurationInstance();

        _asyncButton = (Button) findViewById(R.id.callAsync);

        _serviceButton = (Button) findViewById(R.id.callService);

        _personList = (ListView) findViewById(R.id.personList);

        _handler = new Handler() {
            public void handleMessage(Message message) {
                  List<Person> results = (List<Person>)message.obj;
                  if (message.arg1 == RESULT_OK && results != null) {

                    PersonAdapter adapter = new PersonAdapter(Main.this, results);

                    _personList.setAdapter(adapter);

                  } else {
                    Toast.makeText(Main.this, "Rest call failed.",
                        Toast.LENGTH_LONG).show();
                  }
            }
        };

        _serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, RestService.class);
                // Create a new Messenger for the communication back
                Messenger messenger = new Messenger(_handler);
                intent.putExtra("messenger", messenger);
                intent.setData(Uri.parse("http://devfestdetroit.appspot.com/api/names/10"));
                startService(intent);
            }
        });

        _asyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncWorker async = new AsyncWorker(_personList);
                async.execute("http://devfestdetroit.appspot.com/api/name");
            }
        });
    }
}
