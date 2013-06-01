package org.govhack.vespene.atlas;

public enum Category {
  ACCOM,
  JOURNEY,
  ATTRACTION,
  DESTINFO,
  EVENT,
  TOUR,
  GENSERVICE,
  HIRE,
  INFO,
  RESTAURANT,
  TRANSPORT, 
  UNKNOWN;
  
  public static Category fromString(/*@Nullable*/ String str) {
    if (str == null) {
      return UNKNOWN;
    }
    
    return valueOf(str);
  }
}
