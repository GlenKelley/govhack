package org.govhack.vespene;

import org.govhack.vespene.R;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
      cardView = inflateCard(parent);
    } else {
      cardView = populateView((ViewGroup) convertView);
    }

    return cardView;
  }
  
  private ViewGroup inflateCard(ViewGroup parent) {
    ViewGroup cardView = 
        (ViewGroup) activity.getLayoutInflater().inflate(R.layout.card, parent, false);
    
    return populateView(cardView);
  }
  
  private ViewGroup populateView(ViewGroup cardView) {
    cardView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(activity, "Clicked on Attraction", Toast.LENGTH_SHORT).show();
        AttractionDetailFragment detailFragment = new AttractionDetailFragment();
        activity.getFragmentManager().beginTransaction().add(android.R.id.content, detailFragment)
        	.hide(activity.getFragmentManager().findFragmentById(R.id.fragment_gallery)).commit();
      }
    });

    LinearLayout topBarHolder = (LinearLayout) cardView.findViewById(R.id.top_bar_holder);
    TextView text = new TextView(activity);
    text.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
    text.setTextSize(20.0f);
    text.setText("Attraction Title");
    topBarHolder.addView(text);  
    
    FrameLayout thumbnailHolder = (FrameLayout) cardView.findViewById(R.id.thumbnail_holder);
    ImageView thumbnail = new ImageView(activity);
    thumbnail.setImageResource(SADS[1]);
    thumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
    thumbnailHolder.addView(thumbnail); 
    
    LinearLayout descriptionHolder = (LinearLayout) cardView.findViewById(R.id.text_description_holder);
    TextView description = new TextView(activity);
    description.setTextSize(14.0f);
    description.setText("This is a description");
    descriptionHolder.addView(description);
    
    return cardView;
  }
}