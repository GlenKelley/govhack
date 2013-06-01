package com.danilatos.test;

import org.govhack.vespene.util.AsyncUrlFetcher;
import org.govhack.vespene.util.SimpleCallback;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
  String testUrl = "http://govhack.atdw.com.au/productsearchservice.svc/products?key=278965474541&latlong=-27,153&dist=50&out=json";
  TextView txt;
  final AsyncUrlFetcher urlFetcher = new AsyncUrlFetcher();
  
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
    Button button = (Button) findViewById(R.id.button);
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View butn) {
        urlFetcher.fetch(testUrl, new SimpleCallback<String>() {
          @Override public void success(String result) {
            txt.setText(result);
          }
        });
      }
    });
    
    return true;
  }
  
}
