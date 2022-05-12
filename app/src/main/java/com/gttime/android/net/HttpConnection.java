package com.gttime.android.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class HttpConnection {
    public HttpConnection() {
        throw new UnsupportedOperationException();
    };

    public static String read(String target) {
        try {
            URL url = new URL(target);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String temp;
            StringBuilder stringBuilder = new StringBuilder();
            while ((temp = br.readLine()) != null) {
                stringBuilder.append(temp + "\n");
            }
            br.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return stringBuilder.toString();
        }

        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    };
}
