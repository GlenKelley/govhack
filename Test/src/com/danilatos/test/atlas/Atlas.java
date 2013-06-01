package com.danilatos.test.atlas;

import java.util.List;

import com.danilatos.test.AsyncUrlFetcher;
import com.danilatos.test.Callback;
import com.danilatos.test.SimpleCallback;


public class Atlas {
  private static final String URL_PREFIX = "http://govhack.atdw.com.au/productsearchservice.svc/";
  private static final String KEY = "278965474541";
  
  
  private final AsyncUrlFetcher urlFetcher;

  public Atlas(AsyncUrlFetcher urlFetcher) {
    this.urlFetcher = urlFetcher;
  }

  public static class Product {
    public String productId;
    public String productName;
    
  }

  void search(String str, Callback<List<Product>> results) {
    String args = "&latlong=-27,153&dist=50";
    urlFetcher.fetch(svcUrl("products", args), new SimpleCallback<String>() {
      @Override public void success(String result) {
        
      }
    });
  }
  
  static String svcUrl(String service, String args) {
    return URL_PREFIX + service + "?key=" + KEY + args + "&out=json";
  }
}
