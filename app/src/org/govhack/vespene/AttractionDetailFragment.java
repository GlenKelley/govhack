package org.govhack.vespene;

import org.govhack.vespene.atlas.Product;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AttractionDetailFragment extends Fragment {
  
  private static final int[] SADS = {
    R.drawable.sad1, R.drawable.sad2, R.drawable.sad3, R.drawable.sad4 
  };

  private Product product = null;
    
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, 
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.attraction_detail_layout, container, false);
  }
  
  @Override
  public void onStart() {
    super.onStart(); 
    ActionBar actionBar = getActivity().getActionBar();
    actionBar.setTitle(product.name);
    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
    actionBar.setDisplayHomeAsUpEnabled(true);
    getTv(R.id.detail_place_name).setText(product.name);
//    getTv(R.id.detail_place_address).setText(product.address);
    getTv(R.id.detail_description).setText(product.description);
    
    if (product.phoneNumber != null) {
      getV(R.id.detail_layout_phone).setVisibility(View.VISIBLE);
      getTv(R.id.detail_phone).setText(product.phoneNumber);
    } else {
      getV(R.id.detail_layout_phone).setVisibility(View.GONE);
    }
    
    if (product.emailAddress != null) {
      getV(R.id.detail_layout_email).setVisibility(View.VISIBLE);
      getTv(R.id.detail_email).setText(product.phoneNumber);
    } else {
      getV(R.id.detail_layout_email).setVisibility(View.GONE);
    }
  }
  
  public void setProduct(Product product) {
    this.product = product;
  }
  
  private View getV(int id) {
    return getView().findViewById(id);
  }
  
  private TextView getTv(int id) {
    return (TextView)getV(id);
  }
}
