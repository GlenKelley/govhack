package org.govhack.vespene.atlas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetail {
  public final String id;
  public final String name;
  public final Date startDate;
  public final Date endDate;
  public final String categoryDescription;
  public final String description;
  public final Address address;

  public ProductDetail(String id, String name, Date startDate, Date endDate,
		String categoryDescription, String description, Address address) {
	super();
	this.id = id;
	this.name = name;
	this.startDate = startDate;
	this.endDate = endDate;
	this.categoryDescription = categoryDescription;
	this.description = description;
	this.address = address;
  }

  public ProductDetail(JSONObject json) {
    this(
		Json.str(json, "productId"),
		Json.str(json, "productName"),
		Json.date(json, "validityDateFrom"),
        Json.date(json, "validityDateTo"),
        Json.str(json, "productCategoryDescription"),
        Json.str(json, "productCategoryDescription"),
        new Address(json, Json.getJson(json, "address")));
  }
	
  	//TODO: entryCosts
	//TODO: eventFrequency
	//TODO: multimedia
	//TODO: attributes
	//TODO: open times
	
}
