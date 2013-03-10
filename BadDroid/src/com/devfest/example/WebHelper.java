package com.devfest.example;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class WebHelper {
    HttpClient _client = new DefaultHttpClient();

    public String getHttp(String url) throws IOException {

        HttpGet request = new HttpGet(url);

        HttpResponse response = _client.execute(request);

        InputStream stream = response.getEntity().getContent();
     	byte byteArray[] = IOUtils.toByteArray(stream);
     	String json = new String( byteArray );
     	stream.close();
        return json;
    }
}
