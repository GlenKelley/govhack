package org.govhack.vespene.atlas;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProductDetail {
  public final String id;
  public final String name;
  public final Date startDate;
  public final Date endDate;
  public final String categoryDescription;
  public final String description;
  public final String openTimes;
  public final Address address;
  public final List<String> multimedia;
  public final String email;
  public final String phone;
  public final String url;

  public ProductDetail(String id, String name, Date startDate, Date endDate,
		String categoryDescription, String description, String openTimes, Address address, List<String> multimedia, String email, String phone, String url) {
	this.id = id;
	this.name = name;
	this.startDate = startDate;
	this.endDate = endDate;
	this.categoryDescription = categoryDescription;
	this.description = description;
	this.openTimes = openTimes;
	this.address = address;
	this.multimedia = multimedia;
	this.email = email;
	this.phone = phone;
	this.url = url;
  }

  public static ProductDetail parseFromJson(JSONObject json) {
	  String openTimes = null;
	  if (!json.isNull("openTimes")) {
		  JSONArray jsonOpenTimes = Json.getArray(json, "openTimes");
		  if (jsonOpenTimes.length() > 0) {
			  openTimes = Json.strAt(jsonOpenTimes, 0);
		  }
	  }
	  List<String> multimedia = new ArrayList<String>();
	  if (!json.isNull("multimedia")) {
		  JSONArray jsonMultimedia = Json.getArray(json, "multimedia");
		  for (int i = 0; i < jsonMultimedia.length(); ++i) {
			  JSONObject jsonMediaItem = Json.getObjectAt(jsonMultimedia, i);

			  String type = Json.str(jsonMediaItem, "attributeIdMultimediaContent");
			  if (type.equals("IMAGE")) {
				  multimedia.add(Json.str(jsonMediaItem, "serverPath"));			  
			  }
		  }
	  }

	  String email = null;
	  String phone = null;
	  String url = null;
	  if (!json.isNull("communication")) {
		  JSONArray jsonContact = Json.getArray(json, "communication");
		  for (int i = 0; i < jsonContact.length(); ++i) {
			  JSONObject jsonCommsItem = Json.getObjectAt(jsonContact, i);
			  String type = Json.str(jsonCommsItem, "attributeIdCommunication");
			  String contact = Json.str(jsonCommsItem, "communicationDetail"); 
			  if (type.equals("CAEMENQUIR")) {
				  email = contact;
			  } else if (type.equals("CAPHENQUIR")) {
				  phone = contact;
			  } else if (type.equals("CAURENQUIR")) {
				  url = contact;
			  }
		  }
	  }
	  
	  JSONObject address = null;
	  if (!json.isNull("addresses")) {
		  JSONArray addressesArray = Json.getArray(json, "addresses");
		  if (addressesArray.length() > 0) {
			  address = Json.getObjectAt(addressesArray, 0);
		  }
	  }
	  
	  return new ProductDetail(
		Json.str(json, "productId"),
		Json.str(json, "productName"),
		Json.date(json, "validityDateFrom"),
        Json.date(json, "validityDateTo"),
        Json.str(json, "productCategoryDescription"),
        Json.str(json, "productDescription"),
        openTimes,
        Address.parseJson(json, address),
        multimedia,
        email,
        phone,
        url);
  }
	
  	//TODO: entryCosts
	//TODO: eventFrequency
	//TODO: multimedia
	//TODO: attributes
	
}
