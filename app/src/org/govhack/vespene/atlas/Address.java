package org.govhack.vespene.atlas;

import org.json.JSONObject;

public class Address {

	public final String suburb;
	public final String city;
	public final String state;
	public final String country;
	public final String address;
	public final LatLng latLng;
	
	public Address(String suburb, String city, String state, String country,
			String address, LatLng latLng) {
		super();
		this.suburb = suburb;
		this.city = city;
		this.state = state;
		this.country = country;
		this.address = address;
		this.latLng = latLng;
	}

	public static Address parseJson(JSONObject json, JSONObject addressJson) {
		LatLng latLng = null;
		String addressLine = null;
		if (addressJson != null) {
			addressLine = Json.str(addressJson, "addressLine1");
			latLng = new LatLng(Double.parseDouble(Json.str(addressJson, "geocodeGdaLatitude")), 
	        		Double.parseDouble(Json.str(addressJson, "geocodeGdaLongitude")));
		} else {
			addressLine = Json.str(json, "suburbName");
		}
	    return new Address(
	        Json.str(json, "suburbName"),
	        Json.str(json, "cityName"),
	        Json.str(json, "stateName"),
	        Json.str(json, "countryName"),
	        addressLine,
	        latLng
	        );
	  }
	
}
