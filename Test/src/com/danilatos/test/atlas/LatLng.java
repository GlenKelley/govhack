package com.danilatos.test.atlas;

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
}