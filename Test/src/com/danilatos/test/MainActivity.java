package com.danilatos.test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
  String testUrl = "http://govhack.atdw.com.au/productsearchservice.svc/products?key=278965474541&latlong=-27,153&dist=50&out=json";
  TextView txt;
  
  private final AsyncTask<String, Void, String> fetchTask = new AsyncTask<String, Void, String>() {
    @Override
    protected String doInBackground(String... args) {
      try {
        return fetchData(args[0]);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    
    protected void onPostExecute(String result) {
      txt.setText((String) result);
    };
  };
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    
    txt = (TextView) findViewById(R.id.outputText);
    Button button = (Button) findViewById(R.id.butn);
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View butn) {
        fetchTask.execute(testUrl);
      }
    });
    
    return true;
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
