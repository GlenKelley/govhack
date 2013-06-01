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
      cardView = (ViewGroup) convertView;
    }

    return cardView;
  }
  
  private ViewGroup inflateCard(ViewGroup parent) {
    ViewGroup cardView = 
        (ViewGroup) activity.getLayoutInflater().inflate(R.layout.card, parent, false);
    
    cardView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(activity, "Clicked on Attraction", Toast.LENGTH_SHORT).show();
        AttractionDetailFragment detailFragment = new AttractionDetailFragment();
        activity.getFragmentManager().beginTransaction()
        	.add(android.R.id.content, detailFragment)
        	.hide(activity.getFragmentManager().findFragmentById(R.id.fragment_gallery))
        	.addToBackStack("prefs")
        	.commit();
      }
    });

    TextView titleText = (TextView) cardView.findViewById(R.id.card_title);
    Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Bold.ttf");
    titleText.setTypeface(tf);
    titleText.setTextSize(20.0f);
    titleText.setText("Attraction Title");
    
    FrameLayout thumbnailHolder = (FrameLayout) cardView.findViewById(R.id.thumbnail_holder);
    ImageView thumbnail = new ImageView(activity);
    thumbnail.setImageResource(SADS[1]);
    thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
    thumbnailHolder.addView(thumbnail); 
    
    TextView addressText = (TextView) cardView.findViewById(R.id.card_address);
    Typeface tf3 = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Black.ttf");
    addressText.setTypeface(tf3);
    addressText.setTextSize(16.0f);
    addressText.setText("Circular Quay");
    
    TextView description =  (TextView) cardView.findViewById(R.id.description_text);
    Typeface tf2 = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Thin.ttf");
    description.setTypeface(tf2);
    description.setTextSize(14.0f);
    description.setMaxLines(3);
    description.setText("This is a description of Circular Quay where lots of awesome people live and " +
    		"like to dance like a bunch of pretty and happy people. There lived a jolly person.");
    
    return cardView;
  }
}