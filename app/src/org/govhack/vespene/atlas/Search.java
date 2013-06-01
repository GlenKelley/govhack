package org.govhack.vespene.atlas;

import java.util.EnumSet;

// Don't treat this as mutable, just set things once
// as if it were a builder and then use read-only.
public class Search {
  public final LatLng location;
  
  public int distancekms = 1;
  
  /**
   * if empty, then all categories (default)
   */
  public final EnumSet<Category> categories = EnumSet.noneOf(Category.class);
  
  public String likeProductId;
  
  public Search(LatLng location) {
    this.location = location;
  }
}
