package org.govhack.vespene.atlas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.govhack.vespene.util.AsyncUrlFetcher;
import org.govhack.vespene.util.Callback;
import org.govhack.vespene.util.Lists;
import org.govhack.vespene.util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Atlas {
	
  public static final DateFormat ATLAS_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
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

  public void search(Search search, final Callback<List<Product>> cb) {
    String args = 
        "&latlong=" + search.location.toAtlasString() +
        "&dist=" + search.distancekms;
    if (!search.categories.isEmpty()) {
      args += "&cats=" + Util.join(",", search.categories);
    }
    
    String endpoint;
    if (search.likeProductId != null) {
    	args += "&productId=" + search.likeProductId;
    	endpoint = "mpl";
    } else {
    	endpoint = "products";
    }
    String url = svcUrl(endpoint, args);
    //url =  "http://govhack.atdw.com.au/productsearchservice.svc/products?key=278965474541&latlong=-27,153&dist=50&out=json";
    
    urlFetcher.fetch(url, new JsonCallback(cb) {
      @Override public void data(JSONObject data) throws JSONException {
        JSONArray list = data.getJSONArray("products");
        List<Product> products = Lists.newArrayList();
        for (int i = 0; i < list.length(); i++) {
          products.add(new Product(list.getJSONObject(i)));
        }
        
        cb.success(products);
      }
    });
  }

  public void lookupProduct(String productId, final Callback<ProductDetail> cb) {
	    String args = "&productId="+ productId;
	    urlFetcher.fetch(svcUrl("product", args), new JsonCallback(cb) {
	      @Override public void data(JSONObject data) throws JSONException {
	        JSONObject jsonObject = new JSONObject("product");
	        ProductDetail productDetail = ProductDetail.parseFromJson(jsonObject);
	        cb.success(productDetail);
	      }
	    });
  }
  static String svcUrl(String service, String args) {
    return URL_PREFIX + service + "?key=" + KEY + args + "&out=json";
  }
}
