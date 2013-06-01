package org.govhack.vespene;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ImageViewFragment extends Fragment {
  private String url = null;
  private ImageView view;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, 
      Bundle savedInstanceState) {
    view = new ImageView(getActivity());
    view.setBackgroundColor(Color.BLACK);
    return view;
  }
  
  @Override
  public void onStart() {
    super.onStart();
    new ImageFetcher(getActivity()).fetchImageForView(url, view);
  }
  
  public void setUrl(String url) {
    this.url = url;
  }
}
