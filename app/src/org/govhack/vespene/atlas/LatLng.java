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
	  return String.format("%d,%d", latitude, longitude);
  }
  
  public final static LatLng SYDNEY_CBD = new LatLng(-33.868706,151.207556);
}
