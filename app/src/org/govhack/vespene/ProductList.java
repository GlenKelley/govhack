package org.govhack.vespene;

import java.util.Collections;
import java.util.List;

import org.govhack.vespene.atlas.Atlas;
import org.govhack.vespene.atlas.Product;
import org.govhack.vespene.atlas.ProductDetail;
import org.govhack.vespene.atlas.ProductHeader;
import org.govhack.vespene.atlas.Search;
import org.govhack.vespene.util.Callback;
import org.govhack.vespene.util.Lists;
import org.govhack.vespene.util.Preconditions;

import android.util.Log;

public class ProductList {
  public interface Listener {
    void onSearching();
    void onUpdate();

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
    atlas.search(search, new Callback<List<ProductHeader>>() {
      @Override
      public void success(List<ProductHeader> result) {
        if (searchId != currentSearchId) {
          return;
        }

        clear();

        fetchDetails(searchId, result);
      }
      @Override public void error(Exception e) {
        if (searchId != currentSearchId) {
          return;
        }

        listener.onError(e);
      }
    });
  }

  private void fetchDetails(final int searchId, List<ProductHeader> headers) {
    for (final ProductHeader header : headers) {
      if (header.imageUrl == null) {
        continue;
      }
      atlas.lookupProduct(header.id, new Callback<ProductDetail>() {
        @Override
        public void success(ProductDetail detail) {
          if (searchId != currentSearchId) {
            return;
          }
          Product product = new Product(header, detail);
          addProduct(product);
        }

        @Override
        public void error(Exception e) {
          if (searchId != currentSearchId) {
            return;
          }
          // suppress errors because ATLAS does not contain detailed info
          // for all products
          Log.w("ProductList", e);
        }
      });
    }
  }

  private void clear() {
    products.clear();
    listener.onUpdate();
  }

  private void addProduct(Product product) {
    products.add(product);
    listener.onUpdate();
  }
}
