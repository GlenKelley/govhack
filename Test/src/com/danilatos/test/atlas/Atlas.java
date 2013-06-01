package com.danilatos.test.atlas;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.danilatos.test.AsyncUrlFetcher;
import com.danilatos.test.Callback;


public class Atlas {
  static abstract class JsonCallback implements Callback<String> {
    final Callback<?> errorHandler;
    
    public JsonCallback(Callback<?> errorHandler) {
      this.errorHandler = errorHandler;
    }

    @Override
    public final void success(String result) {
      try {
        data(new JSONObject(result));
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
    }
    
    @Override
    public void error(Exception e) {
      errorHandler.error(e);
    }
    
    protected abstract void data(JSONObject data) throws JSONException;
  }
  
  private static final String URL_PREFIX = "http://govhack.atdw.com.au/productsearchservice.svc/";
  private static final String KEY = "278965474541";
  
  
  private final AsyncUrlFetcher urlFetcher;

  public Atlas(AsyncUrlFetcher urlFetcher) {
    this.urlFetcher = urlFetcher;
  }

  void search(String str, final Callback<List<Product>> cb) {
    String args = "&latlong=-27,153&dist=50";
    urlFetcher.fetch(svcUrl("products", args), new JsonCallback(cb) {
      @Override public void data(JSONObject data) throws JSONException {
        JSONArray list = data.getJSONArray("products");
        List<Product> products = new ArrayList<Product>();
        for (int i = 0; i < list.length(); i++) {
          products.add(new Product(list.getJSONObject(i)));
        }
        
        cb.success(products);
      }
    });
  }
  
  static String svcUrl(String service, String args) {
    return URL_PREFIX + service + "?key=" + KEY + args + "&out=json";
  }
}