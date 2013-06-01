package org.govhack.vespene.atlas;

import org.json.JSONObject;

public class ProductHeader {
  
  public final String id;
  public final String name;
  public final String description;
  public final Category categoryId;
  public final String imageUrl;
  // TODO: perhaps also use "boundary" field also. Warning, sometimes is MULTIPOINT(..., ... ) etc.
  public final LatLng location;
  public final double locationKms;
  
  public ProductHeader(String id, String name, String description,
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

  public ProductHeader(JSONObject json) {
    this(
        Json.str(json, "productId"),
        Json.str(json, "productName"),
        Json.str(json, "productDescription"),
        Category.fromString(Json.str(json, "categoryId")),
        Json.str(json, "productImage"),
        LatLng.parse(Json.str(json, "nearestLocation")),
        Double.parseDouble(Json.str(json, "distanceToLocation")));
  }
}
