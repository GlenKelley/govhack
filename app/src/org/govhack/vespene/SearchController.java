package org.govhack.vespene;

import org.govhack.vespene.atlas.ProductDetail;

import android.content.Context;
import android.widget.Toast;

/**
 * Routes product list updates to the appropriate UI.
 * 
 * @author dan
 */
public class SearchController {
  private final Context cxt;
  private final ProductList products;
  private final CardPagerAdapter cards;
  
  public SearchController(Context context, ProductList prods, CardPagerAdapter cardz) {
    this.cxt = context;
    this.products = prods;
    
    this.cards = cardz;
    // TODO: image loads, details updates, etc.
    
    this.products.setListener(new ProductList.Listener() {
      @Override public void onUpdate() {
        cards.setData(products.getList());
      }
      
      @Override
      public void onProductDetails(String id, ProductDetail productDetail) {
        // TODO Auto-generated method stub
      }
      
      @Override public void onSearching() {
        Toast.makeText(cxt, "Searching", Toast.LENGTH_SHORT).show();
      }

      @Override public void onError(Exception e) {
        Toast.makeText(cxt, "ERROR", Toast.LENGTH_SHORT).show();
        throw new RuntimeException(e);
      }
    });
    
    cards.setData(products.getList());
  }

  
}
