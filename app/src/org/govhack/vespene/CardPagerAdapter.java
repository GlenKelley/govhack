package org.govhack.vespene;

import org.govhack.vespene.R;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CardPagerAdapter extends BaseAdapter {
  private static final String TAG = "CardPagerAdapter";
  
  private static final int[] SADS = {
    R.drawable.sad1, R.drawable.sad2, R.drawable.sad3, R.drawable.sad4 
  };
  
  private final MainActivity activity;
  
  public CardPagerAdapter(MainActivity activity) {
    this.activity = activity;
  }
  
  @Override
  public int getCount() {
    return SADS.length;
}
  @Override
  public Object getItem(int position) {
      return null;
  }
  
  @Override
  public long getItemId(int position) {
      return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewGroup cardView;
    if (convertView == null) {  // if it's not recycled, initialize some attributes
      cardView = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.card, parent, false);
      LinearLayout topBarHolder = (LinearLayout) cardView.findViewById(R.id.top_bar_holder);
      TextView text = new TextView(activity);
      text.setText("bla bla");
      topBarHolder.addView(text);
    } else {
      cardView = (ViewGroup) convertView;
    }

    return cardView;
  }
}