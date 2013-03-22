package com.devfest.starter;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class WebHelper {
    AbstractHttpClient mClient = new DefaultHttpClient();

    public static boolean isOnline(Context ctx){
        ConnectivityManager manager =  (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public String getHttp(String url) throws IOException {

        //Set up the HTTP calls
        HttpGet request = new HttpGet(url);
        HttpResponse response = mClient.execute(request);

        //Get the data from the body of the response
        InputStream stream = response.getEntity().getContent();
     	byte byteArray[] = IOUtils.toByteArray(stream);
     	String json = new String( byteArray );
     	stream.close();
        return json;
    }
}
