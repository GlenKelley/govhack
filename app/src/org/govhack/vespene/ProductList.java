package org.govhack.vespene;

import java.util.Collections;
import java.util.List;

import org.govhack.vespene.atlas.Atlas;
import org.govhack.vespene.atlas.Product;
import org.govhack.vespene.atlas.ProductDetail;
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

    /**
     * Will be called for each product in products, will always be preceded by onUpdate
     */
    void onProductDetails(String id, ProductDetail productDetail);
    
    void onError(Exception e);
  }

  private final Atlas atlas;
  private Listener listener;
  private final List<Product> products = Lists.newArrayList();
  private int currentSearchId = 0;
  
  private final List<Product> view = Collections.unmodifiableList(products);
  
  public ProductList(Atlas atlas) {
    this.atlas = atlas;
  }
  
  public void setListener(Listener l) {
    this.listener = Preconditions.checkNotNull(l, "Null listener");
  }
  
  public List<Product> getList() {
    return view;
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
        
        for (final Product product : result) {
        	atlas.lookupProduct(product.id, new Callback<ProductDetail>() {
				@Override
				public void success(ProductDetail result) {
			        if (searchId != currentSearchId) {
			            return;
			        }
			        listener.onProductDetails(product.id, result);
				}

				@Override
				public void error(Exception e) {
			        if (searchId != currentSearchId) {
			            return;
			        }
			        listener.onError(e);
				}
			});
        }
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
