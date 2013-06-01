package org.govhack.vespene;

import org.govhack.vespene.atlas.Product;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AttractionDetailFragment extends Fragment {
  private Product product = null;
    
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, 
      Bundle savedInstanceState) {
    getActivity().getActionBar().setTitle("");
    return inflater.inflate(R.layout.attraction_detail_layout, container, false);
  }
  
  @Override
  public void onStart() {
    super.onStart();
  }
  
  public void setProduct(Product product) {
    this.product = product;
  }
}
