package org.govhack.vespene;

import java.util.HashSet;
import java.util.Set;

public class Favourites {
  // TODO: sqlite or something
  private Set<String> favourites = new HashSet<String>();

  /**
   * @param str
   * @return new fav state
   */
  public boolean toggleFave(String str) {
    if (favourites.contains(str)) {
      favourites.remove(str);
      return false;
    } else {
      favourites.add(str);
      return true;
    }
  }

  public boolean isFave(String str) {
    return favourites.contains(str);
  }
}
