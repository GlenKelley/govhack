package org.govhack.vespene;

import java.util.List;

import org.govhack.vespene.atlas.Atlas;
import org.govhack.vespene.atlas.Product;
import org.govhack.vespene.atlas.Search;
import org.govhack.vespene.util.Callback;
import org.govhack.vespene.util.Lists;
import org.govhack.vespene.util.Preconditions;

public class ProductList {
  public interface Listener {
    /**
     * May be called multiple times in a row before onUpdate
     */
    void onSearching();
    /**
     * Will not be called twice in a row, will always be preceded by onSearching
     */
    void onUpdate();
    
    void onError(Exception e);
  }

  private final Atlas atlas;
  private Listener listener;
  private final List<Product> products = Lists.newArrayList();
  private int currentSearchId = 0;
  
  public ProductList(Atlas atlas) {
    this.atlas = atlas;
  }
  
  public void setListener(Listener l) {
    this.listener = Preconditions.checkNotNull(l, "Null listener");
  }
  
  public void doSearch(Search search) {
    Preconditions.checkState(listener != null, "Listener not initialised");
    final int searchId = ++currentSearchId;
    listener.onSearching();
    atlas.search(search, new Callback<List<Product>>() {
      @Override
      public void success(List<Product> result) {
        if (searchId != currentSearchId) {
          return;
        }
        products.clear();
        products.addAll(result);
        listener.onUpdate();
      }
      @Override public void error(Exception e) {
        if (searchId != currentSearchId) {
          return;
        }
        
        listener.onError(e);
      }
    });
  }
}