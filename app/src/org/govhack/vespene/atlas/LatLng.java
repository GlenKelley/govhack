package org.govhack.vespene.atlas;

public class LatLng {
  public final double latitude;
  public final double longitude;
  public LatLng(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }
  
  public static LatLng parse(String str) {
    String[] bits = str.split(",");
    return new LatLng(Double.parseDouble(bits[0]), Double.parseDouble(bits[1]));
  }
  
  public String toAtlasString() {
	  return String.format("%f,%f", latitude, longitude);
  }
  
  public com.google.android.gms.maps.model.LatLng realLatLng() {
    return new com.google.android.gms.maps.model.LatLng(latitude, longitude);
  }
  
  static public double EARTH_RADIUS_KMS = 6371;
  
  public double bearingToDeg(LatLng other) {
	  double lat1 = latitude; 
	  double lng1 = longitude;
	  double lat2 = other.latitude;
	  double lng2 = other.longitude;
	  
	  double dlng = Math.toRadians(lng2 - lng1);

	  double y = Math.sin(dlng) * Math.cos(lat2);
	  double x = Math.cos(lat1)*Math.sin(lat2) - Math.sin(lat1)*Math.cos(lat2)*Math.cos(dlng);
	  return Math.toDegrees(Math.atan2(y, x) + Math.PI);
  }

  public double distanceTo(LatLng other) {
	  double lat1 = latitude; 
	  double lng1 = longitude;
	  double lat2 = other.latitude;
	  double lng2 = other.longitude;
	  
	  double dlng = Math.toRadians(lng2 - lng1);
	  double dlat = Math.toRadians(lat2 - lat1);

	  // Haversine formula:
	  double R = EARTH_RADIUS_KMS;
	  double a = Math.sin(dlat/2)*Math.sin(dlat/2) + Math.cos(lat1)*Math.cos(lat2)*Math.sin(dlng/2)*Math.sin(dlng/2);
	  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a) );
	  double d = R * c;
	  return d * 1000;
  }
  
  public final static LatLng SYDNEY_CBD = new LatLng(-33.868706,151.207556);
  public final static LatLng CANBERRA = new LatLng(-35.281255,149.128933);
}
