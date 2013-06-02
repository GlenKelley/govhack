package org.govhack.vespene;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.govhack.vespene.atlas.ProductHeader;
import org.govhack.vespene.util.Lists;
import org.govhack.vespene.util.Util;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Favourites {
  public static final String FAV_PREFS = "FavePrefs";
  private final Context context;
  private final Set<ProductHeader> favourites = new LinkedHashSet<ProductHeader>();

  public Favourites(Context context) {
    this.context = context;
  }

  public void load() {
    favourites.clear();
    SharedPreferences settings = context.getSharedPreferences(FAV_PREFS, Context.MODE_PRIVATE);
    try {
      String saved = settings.getString("favourites", "[]");
      Log.w("Favourites", "YO YO " + saved);
      JSONArray data = new JSONArray(saved);
      for (int i = 0; i < data.length(); i++) {
        favourites.add(new ProductHeader(data.getJSONObject(i)));
      }
    } catch (JSONException e) {
      Log.e("Favourites", e.toString());
    }
  }

  private void save() {  // hack...
    SharedPreferences settings = context.getSharedPreferences(FAV_PREFS, Context.MODE_PRIVATE);
    List<String> items = Lists.newArrayList();
    for (ProductHeader h : favourites) {
      items.add(h.serialized);
    }
    String data = "[" + Util.join(",", items) + "]";
    SharedPreferences.Editor prefEditor = settings.edit();
    prefEditor.putString("favourites", data);
    prefEditor.commit();
  }

  /**
   * @param str
   * @return new fav state
   */
  public boolean toggleFave(ProductHeader header) {
    if (favourites.contains(header)) {
      favourites.remove(header);
      save();
      return false;
    } else {
      favourites.add(header);
      save();
      return true;
    }
  }

  public boolean isFave(ProductHeader header) {
    return favourites.contains(header);
  }

  public Set<ProductHeader> getFavourites() {
    return Collections.unmodifiableSet(favourites);
  }
}
