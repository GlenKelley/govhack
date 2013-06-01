package org.govhack.vespene.atlas;

import java.util.EnumSet;

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
  
  static final EnumSet<Category> DEFAULTS = EnumSet.noneOf(Category.class);
  static {
    DEFAULTS.add(ATTRACTION);
    DEFAULTS.add(RESTAURANT);
  }
  
  public static Category fromString(/*@Nullable*/ String str) {
    if (str == null) {
      return UNKNOWN;
    }
    
    return valueOf(str);
  }
}
