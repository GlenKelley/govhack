package org.govhack.vespene.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.Charsets;

import android.os.AsyncTask;

public class AsyncUrlFetcher {
  public void fetch(String url, final Callback<String> cb) {
    new AsyncTask<String, Void, String>() {
      IOException error = null;
      
      @Override
      protected String doInBackground(String... args) {
        try {
          return fetchData(args[0]);
        } catch (IOException e) {
          error = e;
          return ""; // TODO: check if null is ok to return
        }
      }
      
      protected void onPostExecute(String result) {
        if (error != null) {
          cb.error(error);
        } else {
          cb.success(result);
        }
      }
    }.execute(url);
  }

  String fetchData(String urlString) throws IOException {
    URL url = new URL(urlString);
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    try {
      InputStream in = new BufferedInputStream(urlConnection.getInputStream());
      return Util.slurp(in, 
          /* atlas appears to be returning this... */
          Charsets.UTF_16LE);
    } finally {
      urlConnection.disconnect();
    }
  }
}
