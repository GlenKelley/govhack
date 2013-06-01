package org.govhack.vespene.atlas;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Json {

  static JSONObject getJson(JSONObject json, String field) {
	try {
		return json.getJSONObject(field);
	} catch (JSONException e) {
	    throw new RuntimeException(e);
	}		
  }

  static Date date(JSONObject json, String field) {
	try {
		return Atlas.ATLAS_DATE_FORMAT.parse(str(json, field));
	} catch (ParseException e) {
	      throw new RuntimeException(e);
	}		
  }
  
  static String str(JSONObject json, String field) {
    try {
      return json.getString(field);
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }
}
