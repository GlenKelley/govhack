package org.govhack.vespene.atlas;

import java.util.ArrayList;
import java.util.List;

import org.govhack.vespene.util.AsyncUrlFetcher;
import org.govhack.vespene.util.Callback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.govhack.vespene.atlas.LatLng;
import org.govhack.vespene.atlas.Product;
import org.govhack.vespene.atlas.Atlas.JsonCallback;



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

  void search(String str, LatLng location, int distanceKms, final Callback<List<Product>> cb) {
    String args = "&latlong=" + location.toAtlasString() + "&dist=" + distanceKms;
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

  
  void lookupProduct(String productId, final Callback<Product> cb) {
	    String args = "&productId="+ productId;
	    urlFetcher.fetch(svcUrl("product", args), new JsonCallback(cb) {
	      @Override public void data(JSONObject data) throws JSONException {
	        JSONObject jsonObject = new JSONObject("product");
	        Product product = new Product(jsonObject);
	        cb.success(product);
	      }
	    });
  }
  
  void moreLike(String productId, LatLng location, int distanceKms, final Callback<List<Product>> cb) {
	    String args = "&productId="+ productId +"&latlong=" + location.toAtlasString() + "&dist=" + distanceKms;
	    urlFetcher.fetch(svcUrl("mlp", args), new JsonCallback(cb) {
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
