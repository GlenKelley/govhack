package org.govhack.vespene;

import android.app.ActionBar.OnMenuVisibilityListener;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import org.govhack.vespene.R;

public class CardsFragment extends Fragment implements OnMenuVisibilityListener {
  private static final String TAG = "CardsFragment";

  private MainActivity activity;
  private GridView root;

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    setMenuVisibility(true);
    activity = (MainActivity) getActivity();

    activity.getActionBar().addOnMenuVisibilityListener(this);
  }

  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return root = (GridView) inflater.inflate(R.layout.gallery, container, false);
  }
  
  public void setAdapter(CardPagerAdapter pagerAdapter) {
    root.setAdapter(pagerAdapter);
  }

  @Override
  public void onStart() {
    super.onStart();
    Log.d(TAG, "onStart");
  }

  @Override
  public void onResume () {
    super.onResume();
  }
  
  @Override
  public void onPause () {
    super.onPause();
  }
  
  @Override
  public void onStop() {
    super.onStop();
    Log.d(TAG, "onStop");
  }

  @Override
  public void onMenuVisibilityChanged(boolean isVisible) {
    // TODO Auto-generated method stub
  }
}
