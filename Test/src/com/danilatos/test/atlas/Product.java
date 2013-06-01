package com.danilatos.test.atlas;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {
  
  public final String id;
  public final String name;
  public final String description;
  public final Category categoryId;
  public final String imageUrl;
  // TODO: perhaps also use "nearest location" field.
  public final LatLng location;
  public final double locationKms;

  
  public Product(String id, String name, String description,
      Category categoryId, String imageUrl, LatLng location, double locationKms) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.categoryId = categoryId;
    this.imageUrl = imageUrl;
    this.location = location;
    this.locationKms = locationKms;
  }

  public Product(JSONObject json) {
    this(
        str(json, "productId"),
        str(json, "productName"),
        str(json, "productDescription"),
        Category.valueOf(str(json, "categoryId")),
        str(json, "productImage"),
        LatLng.parse(str(json, "boundary")),
        Double.parseDouble(str(json, "distanceToLocation")));
  }
  
  static String str(JSONObject json, String field) {
    try {
      return json.getString(field);
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }

}
