package com.danilatos.test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

public class AsyncUrlFetcher {
  public void fetch(String url, final Callback<String> cb) {
    new AsyncTask<String, Void, String>() {
      @Override
      protected String doInBackground(String... args) {
        try {
          return fetchData(args[0]);
        } catch (IOException e) {
          cb.error(e);
          return null;
        }
      }
      
      protected void onPostExecute(String result) {
        if (result != null) cb.success(result);
      }
    }.execute(url);
  }

  String fetchData(String urlString) throws IOException {
    URL url = new URL(urlString);
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    try {
      InputStream in = new BufferedInputStream(urlConnection.getInputStream());
      return Util.slurp(in);
    } finally {
      urlConnection.disconnect();
    }
  }
}
