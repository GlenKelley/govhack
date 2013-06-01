package org.govhack.vespene;

import java.util.HashSet;
import java.util.Set;

import org.govhack.vespene.atlas.ProductHeader;

public class Favourites {
  // TODO: sqlite or something
  private Set<ProductHeader> favourites = new HashSet<ProductHeader>();

  /**
   * @param str
   * @return new fav state
   */
  public boolean toggleFave(ProductHeader header) {
    if (favourites.contains(header)) {
      favourites.remove(header);
      return false;
    } else {
      favourites.add(header);
      return true;
    }
  }

  public boolean isFave(ProductHeader header) {
    return favourites.contains(header);
  }
}
