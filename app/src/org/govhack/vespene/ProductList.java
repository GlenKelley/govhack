package org.govhack.vespene;

import java.util.List;

import org.govhack.vespene.atlas.Atlas;
import org.govhack.vespene.atlas.Product;
import org.govhack.vespene.atlas.Search;
import org.govhack.vespene.util.Callback;
import org.govhack.vespene.util.Preconditions;

public class ProductList {
  public interface Listener {
    void onChanged();
    void onError(Exception e);
  }

  private final Atlas atlas;
  private Listener listener;

  public ProductList(Atlas atlas) {
    this.atlas = atlas;
  }
  
  public void setListener(Listener l) {
    this.listener = l;
  }
  
  public void doSearch(Search search) {
    Preconditions.checkState(listener != null, "Listener not initialised");
    atlas.search(search, new Callback<List<Product>>() {
      @Override
      public void success(List<Product> result) {
        // TODO Auto-generated method stub
        
      }
      @Override public void error(Exception e) {
        listener.onError(e);
      }
    });
  }
}
