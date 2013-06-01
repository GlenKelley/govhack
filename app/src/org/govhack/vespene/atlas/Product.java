package org.govhack.vespene.atlas;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {
  
  public final String id;
  public final String name;
  public final String description;
  public final Category categoryId;
  public final String imageUrl;
  // TODO: perhaps also use "boundary" field also. Warning, sometimes is MULTIPOINT(..., ... ) etc.
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
  
  public String toString() {
    return name;
  }

  public Product(JSONObject json) {
    this(
        Json.str(json, "productId"),
        Json.str(json, "productName"),
        Json.str(json, "productDescription"),
        Category.ACCOM, //XXX XXX Category.valueOf(Json.str(json, "categoryId")),
        Json.str(json, "productImage"),
        LatLng.parse(Json.str(json, "nearestLocation")),
        Double.parseDouble(Json.str(json, "distanceToLocation")));
  }
}
