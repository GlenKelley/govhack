package org.govhack.vespene.atlas;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json {

  static JSONObject getJson(JSONObject json, String field) {
	if (json.isNull(field)) {
		return null;
	} else {
		try {
			return json.getJSONObject(field);
		} catch (JSONException e) {
		    throw new RuntimeException(e);
		}		
	}
  }
  
  static JSONArray getArray(JSONObject json, String field) {
	if (json.isNull(field)) {
		return null;
	} else {
		try {
			return json.getJSONArray(field);
		} catch (JSONException e) {
		    throw new RuntimeException(e);
		}		
	}
  }
  
  static JSONObject getObjectAt(JSONArray json, int index) {
	try {
		return json.getJSONObject(index);
	} catch (JSONException e) {
	    throw new RuntimeException(e);
	}	
  }
  
  static String strAt(JSONArray json, int index) {
	try {
		return json.getString(index);
	} catch (JSONException e) {
	    throw new RuntimeException(e);
	}	
  }

  static Date date(JSONObject json, String field) {
	if (json.isNull(field)) {
		return null;
	} else {
		try {
			return Atlas.ATLAS_DATE_FORMAT.parse(str(json, field));
		} catch (ParseException e) {
		      throw new RuntimeException(e);
		}	
	}
  }
  
  static String str(JSONObject json, String field) {
	if (json.has(field)) {
		return null;
	} else {
	    try {
	      return json.getString(field);
	    } catch (JSONException e) {
	      throw new RuntimeException(e);
	    }	
	}
  }
}
