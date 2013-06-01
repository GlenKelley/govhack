package org.govhack.vespene;

import java.util.List;

import org.govhack.vespene.atlas.Product;
import org.govhack.vespene.util.Lists;
import org.govhack.vespene.util.Preconditions;

import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CardPagerAdapter extends BaseAdapter {
  private static final String TAG = "CardPagerAdapter";
  
  private final MainActivity activity;
  private List<Product> products = Lists.newArrayList();
  
  public CardPagerAdapter(MainActivity activity) {
    this.activity = activity;
  }
  
  @Override
  public int getCount() {
    return products.size();
}
  @Override
  public Object getItem(int position) {
      return null;
  }
  
  @Override
  public long getItemId(int position) {
      return 0;
  }
  
  public void setData(List<Product> products) {
    products = Preconditions.checkNotNull(products);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    
    // TODO: reuse convertView where possible, check if this code is ok
//    if (convertView != null) {  // if it's not recycled, initialize some attributes
//      return populateView((ViewGroup) convertView);
//    }
    
    return inflateCard(parent);
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
    
    LinearLayout descriptionHolder = (LinearLayout) cardView.findViewById(R.id.text_description_holder);
    TextView description = new TextView(activity);
    Typeface tf2 = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Thin.ttf");
    description.setTypeface(tf2);
    description.setTextSize(14.0f);
    description.setText("This is a description");
    descriptionHolder.addView(description);
    
    return cardView;
  }
}