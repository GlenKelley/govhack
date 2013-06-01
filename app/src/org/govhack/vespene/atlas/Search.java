package org.govhack.vespene.atlas;

import java.util.EnumSet;

// Don't treat this as mutable, just set things once
// as if it were a builder and then use read-only.
public class Search {
  public final LatLng location;
  
  public double distancekms = 1.0;
  
  /**
   * if empty, then all categories (default)
   */
  public final EnumSet<Category> categories = EnumSet.noneOf(Category.class);
  
  public Search(LatLng location) {
    this.location = location;
  }
}
